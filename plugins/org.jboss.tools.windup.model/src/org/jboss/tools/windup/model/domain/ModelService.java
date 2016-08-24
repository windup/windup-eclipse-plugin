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

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
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
import org.jboss.tools.windup.model.Activator;
import org.jboss.tools.windup.runtime.WindupRuntimePlugin;
import org.jboss.tools.windup.windup.ConfigurationElement;
import org.jboss.tools.windup.windup.Input;
import org.jboss.tools.windup.windup.Issue;
import org.jboss.tools.windup.windup.WindupFactory;
import org.jboss.tools.windup.windup.WindupModel;
import org.jboss.tools.windup.windup.WindupResult;
import org.jboss.windup.tooling.ExecutionResults;
import org.jboss.windup.tooling.data.Hint;
import org.jboss.windup.tooling.data.Link;

import com.google.common.base.Objects;

/**
 * Service for interacting with Windup's model and editing domain.
 */
@Singleton
@Creatable
public class ModelService {
	
	private static final String DOMAIN_NAME = "org.jboss.tools.windup.WindupEditingDomain"; //$NON-NLS-1$
	
    private static final String PROJECT_REPORT_HOME_PAGE = "index.html"; //$NON-NLS-1$
    private static final String TIMESTAMP_FORMAT = "yyyy.MM.dd.HH.mm.ss"; //$NON-NLS-1$
	
	public static IPath reportsDir = Activator.getDefault().getStateLocation().append("reports"); //$NON-NLS-1$

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
		return Activator.getDefault().getStateLocation().append("windup.xmi").toFile();
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
	
	public IPath getGeneratedReportHomeLocation(Input input) {
		return new Path(input.getGeneratedReportLocation().concat(PROJECT_REPORT_HOME_PAGE));
	}
	
	public IPath getGeneratedReportsBaseLocation(ConfigurationElement configuration) {
		String path = configuration.getName().replaceAll("\\s+", "");
		path = path.concat(File.separator);
		return reportsDir.append(path);
	}
	
	public IPath getGeneratedReportBaseLocation(ConfigurationElement configuration, Input input) {
		IPath path = getGeneratedReportsBaseLocation(configuration);
		path = path.append(input.getName());
		path = path.append(File.separator);
		return path;
	}
	
	/**
	 * Populates the configuration element with the execution results.
	 */
	public void populateConfiguration(ConfigurationElement configuration, Input input, ExecutionResults results) {
    	WindupResult result = WindupFactory.eINSTANCE.createWindupResult();
        result.setExecutionResults(results);
        input.setWindupResult(result);
        createPreviousInput(configuration, input);
        for (Hint wHint : results.getHints()) {
        	org.jboss.tools.windup.windup.Hint hint = WindupFactory.eINSTANCE.createHint();
        	result.getIssues().add(hint);

        	hint.setFileAbsolutePath(wHint.getFile().getAbsolutePath());
        	hint.setSeverity(wHint.getSeverity().toString());
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
	}
	
	private void createPreviousInput(ConfigurationElement configuration, Input input) {
		Input previousInput = WindupFactory.eINSTANCE.createInput();
		previousInput.setName(input.getName());
		previousInput.setUri(input.getUri());
		previousInput.setGeneratedReportLocation(input.getGeneratedReportLocation());
		configuration.getPreviousInput().add(previousInput);
	}
	
	private SimpleDateFormat getTimestampFormat() {
		return new SimpleDateFormat(TIMESTAMP_FORMAT);
	}
	
	public String createTimestamp() {
		return getTimestampFormat().format(new Date());
	}
}
