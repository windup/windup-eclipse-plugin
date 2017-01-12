/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.model.domain;

import static org.jboss.tools.windup.model.domain.WindupConstants.CONFIG_DELETED;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jdt.core.IPackageFragment;
import org.jboss.tools.common.xml.IMemento;
import org.jboss.tools.common.xml.XMLMemento;
import org.jboss.tools.windup.model.Activator;
import org.jboss.tools.windup.model.OptionFacades;
import org.jboss.tools.windup.model.OptionFacades.OptionsFacadeManager;
import org.jboss.tools.windup.model.util.DocumentUtils;
import org.jboss.tools.windup.runtime.WindupRuntimePlugin;
import org.jboss.tools.windup.windup.ConfigurationElement;
import org.jboss.tools.windup.windup.Input;
import org.jboss.tools.windup.windup.Issue;
import org.jboss.tools.windup.windup.MigrationPath;
import org.jboss.tools.windup.windup.RuleRepository;
import org.jboss.tools.windup.windup.Technology;
import org.jboss.tools.windup.windup.WindupFactory;
import org.jboss.tools.windup.windup.WindupModel;
import org.jboss.tools.windup.windup.WindupResult;
import org.jboss.windup.bootstrap.help.Help;
import org.jboss.windup.tooling.ExecutionResults;
import org.jboss.windup.tooling.data.Hint;
import org.jboss.windup.tooling.data.Link;
import org.jboss.windup.tooling.data.Quickfix;
import org.jboss.windup.tooling.data.ReportLink;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

/**
 * Service for interacting with Windup's model and editing domain.
 */
@Singleton
@Creatable
public class ModelService {
	
	private static final String DOMAIN_NAME = "org.jboss.tools.windup.WindupEditingDomain"; //$NON-NLS-1$
	
    public static final String CONFIG_XML_PATH = "model/migration-paths/migration-paths.xml";
	
    private static final String TIMESTAMP_FORMAT = "yyyy.MM.dd.HH.mm.ss"; //$NON-NLS-1$
	
	public static IPath reportsDir = Activator.getDefault().getStateLocation().append("reports"); //$NON-NLS-1$
    public static final String PROJECT_REPORT_HOME_PAGE = "index.html"; //$NON-NLS-1$
    
    private static final String MODEL_FILE = "windup.xmi";

    private OptionsFacadeManager optionsFacadeManager;
    
	@Inject private IEventBroker broker;
	
	private WindupModel model;
	private TransactionalEditingDomain domain;
	
	@PostConstruct
	private void initialize() {
		domain = TransactionalEditingDomain.Registry.INSTANCE.getEditingDomain(DOMAIN_NAME);
		load();
	}
	
	@SuppressWarnings("unchecked")
	public <T extends EditingDomain> T getDomain() {
		return (T)domain;
	}
	
	public WindupModel getModel() {
		return model;
	}
	
	private void loadMigrationPaths() {
    	Bundle bundle = FrameworkUtil.getBundle(ModelService.class);
        URL url = FileLocator.find(bundle, new Path(CONFIG_XML_PATH), null);
        try (InputStream input = url.openStream()) {
        	XMLMemento root = XMLMemento.createReadRoot(input);
            for (IMemento element : root.getChildren("path")) {
            	MigrationPath path = WindupFactory.eINSTANCE.createMigrationPath();
            	model.getMigrationPaths().add(path);
            	path.setId(element.getString("id"));
            	
            	XMLMemento child = (XMLMemento)element.getChild("name");
            	path.setName(child.getTextData());
            	
            	child = (XMLMemento)element.getChild("target");
            	Technology target = WindupFactory.eINSTANCE.createTechnology();
            	path.setTarget(target);
            	target.setId(child.getString("id"));
            	target.setVersionRange(child.getString("version-range"));
            	
            	child = (XMLMemento)element.getChild("source");
            	if (child != null) {
            		Technology source = WindupFactory.eINSTANCE.createTechnology();
            		path.setSource(source);
            		source.setId(child.getString("id"));
            		source.setVersionRange(child.getString("version-range"));
            	}
            }
        } catch (IOException e) {
			Activator.log(e);
		}
    }
	
	public OptionsFacadeManager getOptionFacadeManager() {
		if (this.optionsFacadeManager == null) {
			Help help = WindupRuntimePlugin.findWindupHelpCache();
			this.optionsFacadeManager = OptionFacades.createOptionsFacadeManager(help);
		}
		return this.optionsFacadeManager;
	}
	
	/**
	 * Executes the provided runnable on the command stack of Windup's editing domain.
	 * 
	 * @return true if the runner executed without throwing an exception, false otherwise.
	 */
	public boolean write(Runnable runner) {
		CommandWithResult<Boolean> cmd = new CommandWithResult<Boolean>(domain) {
			@Override
			protected void doExecute() {
				try {
					runner.run();
				} catch (Exception e) {
					Activator.log(e);
					setResultObject(Boolean.FALSE);
					return;
				}
				setResultObject(Boolean.TRUE);
			}
		};
		domain.getCommandStack().execute(cmd);
		return cmd.getResultObject();
	}
	
	/**
	 * Executes the provided supplier on the command stack of Windup's editing domain.
	 * 
	 * @return the result of the supplier, null if an error occurred.
	 */
	public <T> T write(Supplier<T> supplier) {
		CommandWithResult<T> cmd = new CommandWithResult<T>(domain) {
			@Override
			protected void doExecute() {
				try {
					setResultObject(supplier.get());
				} catch (Exception e) {
					Activator.log(e);
				}
			}
		};
		domain.getCommandStack().execute(cmd);
		return cmd.getResultObject();
	}
	
	private void load() {
		File location = getWindupStateLocation();
		Resource resource = createResource();
		if (location != null && location.exists()) {
			try {
				resource.load(null);
			} catch (IOException e) {
				Activator.log(e);
				return;
			}
			model = (WindupModel)resource.getContents().get(0);
		}
		else {
			model = WindupFactory.eINSTANCE.createWindupModel();
			resource.getContents().add(model);
			loadMigrationPaths();
		}
	}
	
	public void save() {
		try {
			model.eResource().save(null);
		} catch (IOException e) {
			Activator.log(e);
		}
	}
	
	private Resource createResource() {
		ResourceSet resourceSet = new ResourceSetImpl();
        
        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put
            (Resource.Factory.Registry.DEFAULT_EXTENSION, 
             new XMIResourceFactoryImpl());
        
        Resource resource = resourceSet.createResource(getStateURI());
        resource.setTrackingModification(true);
        return resource;
	}
	
	private URI getStateURI() {
		return URI.createFileURI(getWindupStateLocation().getAbsolutePath());
	}
	
	private File getWindupStateLocation() {
		File file = null;
		try {
			File bundleFile = FileLocator.getBundleFile(Activator.getDefault().getBundle());
			file = new File(bundleFile, MODEL_FILE);
			if (file != null) {
				file = file.getCanonicalFile();
			}
		} catch (IOException e) {
			Activator.log(e);
		}
		if (file == null) {
			// Fall-back to creating it in workspace.
			file = Activator.getDefault().getStateLocation().append(MODEL_FILE).toFile();
		}
		return file;
	}
	
	public ConfigurationElement findConfigurationElement(String name) {
		Optional<ConfigurationElement> configuration = model.getConfigurationElements().stream().filter(c -> Objects.equal(name, c.getName())).findFirst();
		if (configuration.isPresent()) {
			return configuration.get();
		}
		return null;
	}
	
	public Issue findIssue(IMarker marker) {
		URI uri = URI.createURI(marker.getAttribute(WindupMarker.URI_ID, ""));
		return (Issue)getModel().eResource().getEObject(uri.fragment());
	}
	
	public org.jboss.tools.windup.windup.Hint findHint(IMarker marker) {
		Issue issue = findIssue(marker);
		if (issue instanceof org.jboss.tools.windup.windup.Hint) {
			return (org.jboss.tools.windup.windup.Hint)issue;
		}
		return null;
	}
	
	public void deleteConfiguration(ConfigurationElement configuration) {
		model.getConfigurationElements().remove(configuration);
		broker.post(CONFIG_DELETED, configuration);
	}
	
	public ConfigurationElement createConfiguration(String name) {
		ConfigurationElement configuration = WindupFactory.eINSTANCE.createConfigurationElement();
		configuration.setName(name);
		configuration.setWindupHome(WindupRuntimePlugin.findWindupHome().toPath().toString());
		configuration.setGeneratedReportsLocation(getGeneratedReportsBaseLocation(configuration).toOSString());
		configuration.setSourceMode(true);
		configuration.setMigrationPath(model.getMigrationPaths().get(model.getMigrationPaths().size()-1));
		model.getConfigurationElements().add(configuration);
		return configuration;
	}
	
	public void createInput(ConfigurationElement configuration, List<IProject> projects) {
		projects.forEach(project -> {
			Input input = WindupFactory.eINSTANCE.createInput();
			URI uri = WorkspaceResourceUtils.createPlatformPluginURI(project.getFullPath());
			input.setName(project.getName());
			input.setUri(uri.toString());
			configuration.getInputs().add(input);
		});
	}
	
	public void deleteInput(ConfigurationElement configuration, List<IProject> projects) {
		projects.forEach(project -> {
			Optional<Input> input = configuration.getInputs().stream().filter(i -> {
				return i.getName().equals(project.getName());
			}).findFirst();
			if (input.isPresent()) {
				configuration.getInputs().remove(input.get());
			}
		});
		for (IPackageFragment fragment : ConfigurationResourceUtil.getCurrentPackages(configuration)) {
			if (projects.contains(fragment.getJavaProject().getProject())) {
				URI uri = WorkspaceResourceUtils.createPlatformPluginURI(fragment.getPath());
				configuration.getPackages().remove(uri.toString());
			}
		}
	}
	
	public void addPackages(ConfigurationElement configuration, List<IPackageFragment> packages) {
		List<String> uris = packages.stream().map(p -> {
			return WorkspaceResourceUtils.createPlatformPluginURI(p.getPath()).toString();
		}).collect(Collectors.toList());
		configuration.getPackages().addAll(uris);
	}
	
	public void addRuleRepository(String location) {
		if (!model.getCustomRuleRepositories().stream().anyMatch(repo -> repo.getLocation().equals(location))) {
			RuleRepository repo = WindupFactory.eINSTANCE.createRuleRepository();
			repo.setLocation(location);
			model.getCustomRuleRepositories().add(repo);
		}
	}
	
	public List<String> computeExistingRepositories() {
		List<String> repos = Lists.newArrayList();
		model.getCustomRuleRepositories().forEach(repo -> {
			repos.add(repo.getLocation());
		});
		return repos;
	}
	
	public void removePackages(ConfigurationElement configuration, List<IPackageFragment> packages) {
		List<String> uris = packages.stream().map(p -> {
			return WorkspaceResourceUtils.createPlatformPluginURI(p.getPath()).toString();
		}).collect(Collectors.toList());
		configuration.getPackages().removeAll(uris);
	}
	
	public void synch(ConfigurationElement configuration) {
		for (Iterator<String> iter = configuration.getPackages().iterator(); iter.hasNext();) {
			IResource resource = (IResource)WorkspaceResourceUtils.findResource(iter.next());
			if (resource == null || !resource.exists()) {
				iter.remove();
			}
		}
		for (Iterator<Input> iter = configuration.getInputs().iterator(); iter.hasNext();) {
			Input input = iter.next();
			IResource resource = WorkspaceResourceUtils.findResource(input.getUri());
			if (resource == null || !resource.exists()) {
				iter.remove();
			}
		}
	}
	
	public void addDirtyListener(Consumer<Boolean> runner) {
		domain.getCommandStack().addCommandStackListener((e) -> {
			runner.accept(model.eResource().isModified());
		});
	}
	
	@PreDestroy
	private void dispose() {
		save();
	}
	
	public ConfigurationElement findConfiguration(String name) {
		Optional<ConfigurationElement> found = model.getConfigurationElements().stream().filter(configuration -> {
			return Objects.equal(configuration.getName(), name);
		}).findFirst();
		return found.isPresent() ? found.get() : null;
	}
	
	public IPath getReportPath(ConfigurationElement configuration) {
		return ModelService.reportsDir.append(configuration.getName()).append(PROJECT_REPORT_HOME_PAGE);
	}
	
	public IPath getGeneratedReportsBaseLocation(ConfigurationElement configuration) {
		String path = configuration.getName().replaceAll("\\s+", "");
		path = path.concat(File.separator);
		return reportsDir.append(path);
	}
	
	public IPath getGeneratedReportsBaseLocation(Issue issue) {
		Input input = (Input)issue.eContainer().eContainer();
		ConfigurationElement configuration = (ConfigurationElement)input.eContainer();
		return getGeneratedReportBaseLocation(configuration, input);
	}
	
	public IPath getGeneratedReportBaseLocation(ConfigurationElement configuration, Input input) {
		IPath path = getGeneratedReportsBaseLocation(configuration);
		path = path.append(input.getName());
		path = path.append(File.separator);
		return path;
	}
	
	public IPath getGeneratedReport(ConfigurationElement configuration, Input input) {
		IPath path = getGeneratedReportBaseLocation(configuration, input);
		path = path.append(PROJECT_REPORT_HOME_PAGE);
		return path;
	}
	
	public static IFile getIssueResource(Issue issue) {
		return getResource(issue.getFileAbsolutePath());
	}
	
	private static IFile getResource(String path) {
		return ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(new Path(path));
	}
	
	/**
	 * Populates the configuration element with the execution results.
	 */
	public void populateConfiguration(ConfigurationElement configuration, Input input, ExecutionResults results) {
    	WindupResult result = WindupFactory.eINSTANCE.createWindupResult();
        result.setExecutionResults(results);
        input.setWindupResult(result);
        configuration.setTimestamp(createTimestamp());
        for (Hint wHint : results.getHints()) {
        	String path = wHint.getFile().getAbsolutePath();
        	IFile resource = ModelService.getResource(path);
			if (resource == null) {
				Activator.logErrorMessage("ModelService:: No workspace resource associated with file: " + path); //$NON-NLS-1$
				continue;
			}
			
        	org.jboss.tools.windup.windup.Hint hint = WindupFactory.eINSTANCE.createHint();
        	result.getIssues().add(hint);
        	
        	String line = DocumentUtils.getLine(resource, wHint.getLineNumber()-1);
        	hint.setOriginalLineSource(line);

        	for (Quickfix fix : wHint.getQuickfixes()) {
        		org.jboss.tools.windup.windup.QuickFix quickFix = WindupFactory.eINSTANCE.createQuickFix();
        		quickFix.setName(fix.getName());
        		quickFix.setQuickFixType(fix.getType().toString());
        		quickFix.setSearchString(fix.getSearch());
        		quickFix.setReplacementString(fix.getReplacement());
        		quickFix.setNewLine(fix.getNewline());
        		hint.getQuickFixes().add(quickFix);
        	}

        	// TODO: I think we might want to change this to project relative for portability.
        	hint.setFileAbsolutePath(wHint.getFile().getAbsolutePath());
        	hint.setSeverity(wHint.getIssueCategory().getCategoryID().toUpperCase());
        	hint.setRuleId(wHint.getRuleID());
        	hint.setEffort(wHint.getEffort());
        	
        	hint.setTitle(wHint.getTitle());
        	hint.setHint(wHint.getHint());
        	hint.setLineNumber(wHint.getLineNumber());
        	hint.setColumn(wHint.getColumn());
        	hint.setLength(wHint.getLength());
        	hint.setSourceSnippet(wHint.getSourceSnippit());
        	
        	for (Link wLink : wHint.getLinks()) {
        		org.jboss.tools.windup.windup.Link link = WindupFactory.eINSTANCE.createLink();
        		link.setDescription(wLink.getDescription());
        		link.setUrl(wLink.getUrl());
        		hint.getLinks().add(link);
        	}
        }
        
        // TODO: Classifications
        linkReports(results, result.getIssues());
	}
	
	private void linkReports(ExecutionResults results, List<Issue> issues) {
		for (Issue issue : issues) {
			IFile resource = ModelService.getIssueResource(issue);
			if (resource == null) {
				Activator.logErrorMessage("ModelService:: No resource associated with issue file: " + issue.getFileAbsolutePath());
				continue;
			}
			File file = resource.getRawLocation().toFile();
			for (ReportLink link : results.getReportLinks()) {
				if (link.getInputFile().equals(file)) {
					File report = link.getReportFile();
					issue.setGeneratedReportLocation(report.getAbsolutePath());
					break;
				}
			}
		}
	}
	
	private SimpleDateFormat getTimestampFormat() {
		return new SimpleDateFormat(TIMESTAMP_FORMAT);
	}
	
	public String createTimestamp() {
		return getTimestampFormat().format(new Date());
	}
	
	private Date getTimestamp(ConfigurationElement configuration) {
		try {
			return new SimpleDateFormat(TIMESTAMP_FORMAT).parse(configuration.getTimestamp());
		} catch (ParseException e) {
			Activator.log(e);
		}
		return null;
	}
	
	public ConfigurationElement getRecentConfiguration() {
		ConfigurationElement mostRecentConfiguration = null;
		for (ConfigurationElement configuration : getModel().getConfigurationElements()) {
			if (mostRecentConfiguration == null) {
				mostRecentConfiguration = configuration;
			}
			else if (!mostRecentConfiguration.getName().equals(configuration.getName()) &&
					mostRecentConfiguration.getTimestamp() != null && configuration.getTimestamp() != null) {
				Date thisTime = getTimestamp(mostRecentConfiguration);
				Date otherTime = getTimestamp(configuration);
				if (thisTime.before(otherTime)) {
					mostRecentConfiguration = configuration;
				}
			}
		}
		return mostRecentConfiguration;
	}
}
