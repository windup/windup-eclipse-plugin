/*******************************************************************************
 * Copyright (c) 2021 Red Hat, Inc.
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
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.m2e.core.internal.MavenPluginActivator;
import org.eclipse.m2e.core.internal.project.registry.ProjectRegistryManager;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.jboss.tools.common.xml.IMemento;
import org.jboss.tools.common.xml.XMLMemento;
import org.jboss.tools.windup.model.Activator;
import org.jboss.tools.windup.model.OptionFacades;
import org.jboss.tools.windup.model.OptionFacades.OptionsFacadeManager;
import org.jboss.tools.windup.model.util.DocumentUtils;
import org.jboss.tools.windup.runtime.WindupRuntimePlugin;
import org.jboss.tools.windup.runtime.options.Help;
import org.jboss.tools.windup.windup.ConfigurationElement;
import org.jboss.tools.windup.windup.CustomRuleProvider;
import org.jboss.tools.windup.windup.Input;
import org.jboss.tools.windup.windup.Issue;
import org.jboss.tools.windup.windup.MigrationPath;
import org.jboss.tools.windup.windup.Pair;
import org.jboss.tools.windup.windup.Report;
import org.jboss.tools.windup.windup.Technology;
import org.jboss.tools.windup.windup.WindupFactory;
import org.jboss.tools.windup.windup.WindupModel;
import org.jboss.tools.windup.windup.WindupResult;
import org.jboss.windup.tooling.ExecutionResults;
import org.jboss.windup.tooling.data.Classification;
import org.jboss.windup.tooling.data.Hint;
import org.jboss.windup.tooling.data.Link;
import org.jboss.windup.tooling.data.Quickfix;
import org.jboss.windup.tooling.data.ReportLink;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;

/**
 * Service for interacting with Windup's model and editing domain.
 * 
 * TODO: Initially I wanted to design our domain so that it's transactional; however, 
 * I haven't enforced wrapping writes in a transaction, and we may want to.
 */
@SuppressWarnings("restriction")
@Singleton
@Creatable
public class ModelService {
	
	private static final String DOMAIN_NAME = "org.jboss.tools.windup.WindupEditingDomain"; //$NON-NLS-1$
	
    public static final String CONFIG_XML_PATH = "model/migration-paths/migration-paths.xml";
	
    private static final String TIMESTAMP_FORMAT = "yyyy.MM.dd.HH.mm.ss"; //$NON-NLS-1$
	
	public static IPath reportsDir = Activator.getDefault().getStateLocation().append("reports"); //$NON-NLS-1$
	
    public static final String PROJECT_REPORT_HOME_PAGE = "index.html"; //$NON-NLS-1$
    public static final String REPORT_FOLDER = "reports"; //$NON-NLS-1$
    public static final String INPUT_INDEX = "report_index_"; //$NON-NLS-1$
    
    private static final String MODEL_FILE = "windup.xmi";
    private static final String IGNORE_FILE = "default-windup-ignore.txt";

    private IEventBroker broker;
	
	private WindupModel model;
	private TransactionalEditingDomain domain;
	
	private Map<ConfigurationElement, KantraConfiguration> kantraModelDelegates = Maps.newHashMap(); 
	
	@Inject
	public ModelService(IEventBroker broker, WindupDomainListener modelListener) {
		this.broker = broker;
		domain = TransactionalEditingDomain.Registry.INSTANCE.getEditingDomain(DOMAIN_NAME);
		load();
		model.eAdapters().add(modelListener);
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
		Help help = WindupRuntimePlugin.findWindupHelpCache();
		return OptionFacades.createOptionsFacadeManager(help);
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
		File location = getWindupStateLocation(MODEL_FILE);
		Resource resource = createResource();
		if (location != null && location.exists()) {
			try {
				resource.load(null);
				model = (WindupModel)resource.getContents().get(0);
				model.getConfigurationElements().forEach(configuration -> {
						this.kantraModelDelegates.put(configuration, new KantraConfiguration(configuration));
					}
				);
			} catch (IOException e) {
				Activator.logInfo("Something has gone wrong and invalidated the underlying model. Creating another one...");
				resource.getContents().clear();
				initModel(resource);
			}
		}
		else {
			initModel(resource);
		}
	}
	
	private void initModel(Resource resource) {
		model = WindupFactory.eINSTANCE.createWindupModel();
		resource.getContents().add(model);
		loadMigrationPaths();
		ECollections.sort(model.getMigrationPaths(), (MigrationPath path1, MigrationPath path2) -> {
			if (path1.getName() == null) {
				return -1;
			}
			if (path2.getName() == null) {
				return -1;
			}
			String p1 = path1.getName().substring(0, path1.getName().length()-2);
			String p2 = path2.getName().substring(0, path2.getName().length()-2);
					
			// primarily to put eap7 at top.
			if (p1.equals(p2)) {
				if (path1.getName().endsWith("7")) return -1;
			}
			
			return path1.getName().compareTo(path2.getName());
		});
		save();
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
		return URI.createFileURI(getWindupStateLocation(MODEL_FILE).getAbsolutePath());
	}
	
	public File getDefaultUserIgnore() {
		return getWindupStateLocation(IGNORE_FILE);
	}
	
	public File getWindupStateLocation(String fileName) {
		File file = null;
		try {
			File bundleFile = FileLocator.getBundleFile(Activator.getDefault().getBundle());
			file = new File(bundleFile, fileName);
			if (file != null) {
				file = file.getCanonicalFile();
			}
		} catch (IOException e) {
			Activator.log(e);
		}
		if (file == null) {
			// Fall-back to creating it in workspace.
			file = Activator.getDefault().getStateLocation().append(fileName).toFile();
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
	
	public void deleteConfiguration(ConfigurationElement configuration) {
		model.getConfigurationElements().remove(configuration);
		broker.post(CONFIG_DELETED, configuration);
	}
	
	public void deleteIssue(Issue issue) {
		((WindupResult)issue.eContainer()).getIssues().remove(issue);
	}
	
	public KantraConfiguration getKantraDelegate(ConfigurationElement configuration) {
		return this.kantraModelDelegates.get(configuration);
	}
	
	public ConfigurationElement createConfiguration(String name) {
		CommandWithResult<ConfigurationElement> cmd = new CommandWithResult<ConfigurationElement>(domain) {
			@Override
			protected void doExecute() {
				try {
					ConfigurationElement configuration = WindupFactory.eINSTANCE.createConfigurationElement();
					configuration.setName(name);
					configuration.setWindupHome(WindupRuntimePlugin.computeWindupHome().toString());
					Pair pair = WindupFactory.eINSTANCE.createPair();
					pair.setKey("output");
//					pair.setValue();
					configuration.getOptions().add(pair);
					configuration.setOutputLocation(getDefaultOutputLocation(configuration));
					configuration.setSourceMode(true);
					configuration.setGenerateReport(true);
					configuration.setMigrationPath(model.getMigrationPaths().get(1));
					model.getConfigurationElements().add(configuration);
					KantraConfiguration delegate = new KantraConfiguration(configuration);
					ModelService.this.kantraModelDelegates.put(configuration, delegate);
					setResultObject(configuration);
					save();
				} catch (Exception e) {
					Activator.log(e);
				}
			}
		};
		domain.getCommandStack().execute(cmd);
		return cmd.getResultObject();
	}
	
	public void createInput(ConfigurationElement configuration, List<String> projects) {
		projects.forEach(project -> {
			Input input = WindupFactory.eINSTANCE.createInput();
			input.setLocation(project);
			configuration.getInputs().add(input);
		});
	}
	
	public void deleteProjects(ConfigurationElement configuration, Set<IProject> projects) {
		projects.forEach(project -> {
			Optional<Input> input = configuration.getInputs().stream().filter(i -> {
				return i.getLocation().equals(project.getLocation().toString());
			}).findFirst();
			if (input.isPresent()) {
				configuration.getInputs().remove(input.get());
			}
		});
	}
	
	public void deletePackages(ConfigurationElement configuration, Set<IProject> projects) {
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
	
	public CustomRuleProvider addRulesetRepository(String location, String workspaceResourceLocation) {
		return write(() -> {
			CustomRuleProvider repo = WindupFactory.eINSTANCE.createCustomRuleProvider();
			repo.setLocationURI(location);
			repo.setWorkspaceResourceLocation(workspaceResourceLocation);
			model.getCustomRuleRepositories().add(repo);
			return repo;
		});
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
			if (!new File(input.getLocation()).exists()) {
				iter.remove();
			}
		}
	}
	
	public void addDirtyListener(Consumer<Boolean> runner) {
		domain.getCommandStack().addCommandStackListener((e) -> {
			runner.accept(model.eResource().isModified());
		});
	}
	
	public void cleanCustomRuleRepositories(ConfigurationElement configuration) {
		// Delete non-existing registered providers.
		for (Iterator<CustomRuleProvider> iter = getModel().getCustomRuleRepositories().iterator(); iter.hasNext();) {
			if (!new File(iter.next().getLocationURI()).exists()) {
				write(() -> iter.remove());
			}
		}
		// Delete non-existing referenced locations.
		for (Iterator<String> iter = configuration.getUserRulesDirectories().iterator(); iter.hasNext();) {
			if (!new File(iter.next()).exists()) {
				write(() -> iter.remove());
			}
		}
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
	
	public IPath getReport(ConfigurationElement configuration, Input input) {
		StringBuffer buff = new StringBuffer();
		buff.append(configuration.getOutputLocation());
		buff.append(File.separator);
		buff.append(input.getName());
		buff.append(ModelService.PROJECT_REPORT_HOME_PAGE);
		return Path.fromOSString(buff.toString());
	}
	
	public String getDefaultOutputLocation(ConfigurationElement configuration) {
		String configName = configuration.getName().replaceAll("\\s+", "");
		return ModelService.reportsDir.append(configName).toOSString();
	}
	
	/**
	 * Populates the configuration element with the execution results.
	 */
	public void populateConfiguration(ConfigurationElement configuration, ExecutionResults results) {
    		WindupResult result = WindupFactory.eINSTANCE.createWindupResult();
        result.setExecutionResults(results);
        configuration.setWindupResult(result);
        configuration.setTimestamp(createTimestamp());

        for (Iterator<Hint> iter = results.getHints().iterator(); iter.hasNext();) {
        		
        		Hint wHint = iter.next();
        	
	        	String path = wHint.getFile().getAbsolutePath();
	        	IFile resource = WorkspaceResourceUtils.getResource(path);
	        	
			if (resource == null || !resource.exists()) {
				Activator.logErrorMessage("ModelService::hint No workspace resource associated with file: " + path); //$NON-NLS-1$
				Activator.logErrorMessage("Rule ID: " + wHint.getRuleID() + " Hint: " + wHint.getHint()); //$NON-NLS-1$ //$NON-NLS-2$
				iter.remove();
				continue;
			}
			
			/*if (isMavenBuildFile(resource)) {
				iter.remove();
				continue;
			}*/
				
	        	org.jboss.tools.windup.windup.Hint hint = WindupFactory.eINSTANCE.createHint();
	        	result.getIssues().add(hint);
	        	
	        	if (isTextMimeType(wHint)) {
		        	String line = DocumentUtils.getLine(resource, wHint.getLineNumber()-1);
		        	hint.setOriginalLineSource(line);
	        	}
	
	        	for (Quickfix fix : wHint.getQuickfixes()) {
	    			org.jboss.tools.windup.windup.QuickFix quickFix = WindupFactory.eINSTANCE.createQuickFix();
	        		quickFix.setQuickFixType(fix.getType().toString());
	        		quickFix.setSearchString(fix.getSearch());
	        		quickFix.setReplacementString(fix.getReplacement());
	        		quickFix.setNewLine(fix.getNewline());
	        		quickFix.setTransformationId(fix.getTransformationID());
	        		quickFix.setName(fix.getName());
	        		if (fix.getFile() != null) {
	        			quickFix.setFile(fix.getFile().getAbsolutePath());
	        		}
	        		else {
	        			// Fallback for quickfixes not assigned to file. Assume quickfix applies to file associated with the hint.
	        			quickFix.setFile(path);
	        		}
	        		hint.getQuickFixes().add(quickFix);
	        	}
	
	        	hint.setFileAbsolutePath(wHint.getFile().getAbsolutePath());
	        	hint.setSeverity(wHint.getIssueCategory().getCategoryID().toUpperCase());
	        	hint.setRuleId(wHint.getRuleID());
	        	hint.setEffort(wHint.getEffort());
	        	
	        	hint.setTitle(wHint.getTitle());
	        	hint.setMessageOrDescription(wHint.getHint());
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
        
        for (Classification wClassification : results.getClassifications()) {
	        	String path = wClassification.getFile().getAbsolutePath();
	        	IFile resource = WorkspaceResourceUtils.getResource(path);
	        	
			if (resource == null || !resource.exists()) {
				Activator.logErrorMessage("ModelService::classification No workspace resource associated with file: " + path); //$NON-NLS-1$
				Activator.logErrorMessage("Rule ID: " + wClassification.getRuleID() + " Classification: " + wClassification.getClassification()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				continue;
			}
			
			/*if (isMavenBuildFile(resource)) {
				continue;
			}*/
				
	        	org.jboss.tools.windup.windup.Classification classification = WindupFactory.eINSTANCE.createClassification();
	        	result.getIssues().add(classification);
	        	
	        //	String line = DocumentUtils.getLine(resource, wClassification.getLineNumber()-1);
	        	// hint.setOriginalLineSource(line);
	
	        	for (Quickfix fix : wClassification.getQuickfixes()) {
	    			org.jboss.tools.windup.windup.QuickFix quickFix = WindupFactory.eINSTANCE.createQuickFix();
	        		quickFix.setQuickFixType(fix.getType().toString());
	        		quickFix.setSearchString(fix.getSearch());
	        		quickFix.setReplacementString(fix.getReplacement());
	        		quickFix.setNewLine(fix.getNewline());
	        		quickFix.setTransformationId(fix.getTransformationID());
	        		quickFix.setName(fix.getName());
	        		if (fix.getFile() != null) {
	        			quickFix.setFile(fix.getFile().getAbsolutePath());
	        		}
	        		else {
	        			// Fallback for quickfixes not assigned to file. Assume quickfix applies to file associated with the hint.
	        			quickFix.setFile(path);
	        		}
	        		classification.getQuickFixes().add(quickFix);
	        	}
	
	        	// TODO: I think we might want to change this to project relative for portability.
	        	classification.setFileAbsolutePath(wClassification.getFile().getAbsolutePath());
	        	classification.setSeverity(wClassification.getIssueCategory().getCategoryID().toUpperCase());
	        	classification.setRuleId(wClassification.getRuleID());
	        	classification.setEffort(wClassification.getEffort());
	        	classification.setTitle(wClassification.getClassification());
	        	classification.setMessageOrDescription(wClassification.getDescription());

	        //classification.setHint(wClassification.getHint());
	        //classification.setLineNumber(wClassification.getLineNumber());
	        //classification.setColumn(wClassification.getColumn());
	        	//classification.setLength(wClassification.getLength());
	        //classification.setSourceSnippet(wClassification.getSourceSnippit());
	        	
	        	for (Link wLink : wClassification.getLinks()) {
	        		org.jboss.tools.windup.windup.Link link = WindupFactory.eINSTANCE.createLink();
	        		link.setDescription(wLink.getDescription());
	        		link.setUrl(wLink.getUrl());
	        		classification.getLinks().add(link);
	        	}
        }
        
        //
        linkReports(configuration, results, result.getIssues());
	}
	
	private boolean isTextMimeType(Hint hint) {
		String mimeType = null;
		try {
			mimeType = Files.probeContentType(hint.getFile().toPath());
			if (mimeType != null && mimeType.startsWith("text")) { //$NON-NLS-1$
				return true;
			}
		} catch (IOException e) {
		}
		Activator.logInfo("Hint corresponding to file " + hint.getFile().getAbsolutePath() + //$NON-NLS-1$
				" contains unsupported mime type " + mimeType + //$NON-NLS-1$ 
				" - Rule ID: " + hint.getRuleID() + " Hint: " + hint.getHint()); //$NON-NLS-1$ //$NON-NLS-2$
		return false;
	}
	
	private boolean isMavenBuildFile(IResource resource) {
		ProjectRegistryManager mavenProjectManager = MavenPluginActivator.getDefault().getMavenProjectManagerImpl();
		IMavenProjectFacade mavenFacade = mavenProjectManager.create(resource.getProject(), new NullProgressMonitor());
		if (mavenFacade != null) {
			IPath outputLocation = mavenFacade.getOutputLocation();
			if (outputLocation != null) {
				IResource container = ResourcesPlugin.getWorkspace().getRoot().findMember(outputLocation);
				if (container != null && container instanceof IContainer) {
					IResource found = ((IContainer)container).findMember(resource.getFullPath());
					return found != null && found.exists();
				}
			}
		}
		return false;
	}
	
	private void linkReports(ConfigurationElement configuration, ExecutionResults results, List<Issue> issues) {
		configuration.getReports().clear();
		for (Issue issue : issues) {
			IFile resource = WorkspaceResourceUtils.getResource(issue.getFileAbsolutePath());
			if (resource == null || !resource.exists()) {
				Activator.logErrorMessage("ModelService::linkReports No resource associated with issue file: " + issue.getFileAbsolutePath());
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
		for (ReportLink link : results.getReportLinks()) {
			Report report = WindupFactory.eINSTANCE.createReport();
			report.setInputFile(link.getInputFile().getAbsolutePath());
			report.setLocation(link.getReportFile().getAbsolutePath());
			configuration.getReports().add(report);
		}
	}
	
	private static SimpleDateFormat getTimestampFormat() {
		return new SimpleDateFormat(TIMESTAMP_FORMAT);
	}
	
	public static String createTimestamp() {
		return getTimestampFormat().format(new Date());
	}
	
	public static Date getTimestamp(String timestamp) {
		try {
			return new SimpleDateFormat(TIMESTAMP_FORMAT).parse(timestamp);
		} catch (ParseException e) {
			Activator.log(e);
		}
		return null;
	}
	
	public ConfigurationElement getRecentConfiguration() {
		if (getModel() == null) return null;
		ConfigurationElement mostRecentConfiguration = null;
		for (ConfigurationElement configuration : getModel().getConfigurationElements()) {
			if (mostRecentConfiguration == null) {
				mostRecentConfiguration = configuration;
			}
			else if (!mostRecentConfiguration.getName().equals(configuration.getName()) &&
					mostRecentConfiguration.getTimestamp() != null && configuration.getTimestamp() != null) {
				Date thisTime = getTimestamp(mostRecentConfiguration.getTimestamp());
				Date otherTime = getTimestamp(configuration.getTimestamp());
				if (thisTime.before(otherTime)) {
					mostRecentConfiguration = configuration;
				}
			}
		}
		return mostRecentConfiguration;
	}
	
	public void cleanPhantomCustomRuleProviders() {
		for (Iterator<CustomRuleProvider> iter = getModel().getCustomRuleRepositories().iterator(); iter.hasNext();) {
			CustomRuleProvider provider = iter.next();
			if (!(new File(provider.getLocationURI()).exists())) {
				// delete from temp project too (if it's in the temp project)?
				write(() -> iter.remove());
			}
		}
	}
	
	public boolean ruleProviderExists(String workspaceLocation) {
		for (CustomRuleProvider provider : getModel().getCustomRuleRepositories()) {
			if (Objects.equal(workspaceLocation, provider.getWorkspaceResourceLocation())) {
				return true;
			}
		}
		return false;
	}
}
