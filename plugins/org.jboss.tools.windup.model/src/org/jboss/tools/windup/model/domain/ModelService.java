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

import static org.jboss.tools.windup.model.domain.WindupConstants.CONFIG_CREATED;
import static org.jboss.tools.windup.model.domain.WindupConstants.CONFIG_DELETED;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.runtime.IPath;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.jboss.tools.windup.model.Activator;
import org.jboss.tools.windup.model.util.NameUtil;
import org.jboss.tools.windup.runtime.WindupRuntimePlugin;
import org.jboss.tools.windup.windup.ConfigurationElement;
import org.jboss.tools.windup.windup.WindupFactory;
import org.jboss.tools.windup.windup.WindupModel;

import com.google.common.base.Objects;

/**
 * Service for interacting with Windup's model and editing domain.
 */
@Singleton
@Creatable
public class ModelService {
	
	private static final String DOMAIN_NAME = "org.jboss.tools.windup.WindupEditingDomain"; //$NON-NLS-1$
	
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
	
	public void deleteConfiguration(ConfigurationElement configuration) {
		EcoreUtil.delete(configuration);
		broker.post(CONFIG_DELETED, configuration);
	}
	
	public void createConfiguration() {
		String name = NameUtil.generateUniqueConfigurationElementName(model);
		ConfigurationElement configuration = createConfiguration(name);
		configuration.setWindupHome(WindupRuntimePlugin.findWindupHome().toPath().toString());
		configuration.setGeneratedReportLocation(getGeneratedReportLocation(configuration).toOSString());
		configuration.setSourceMode(true);
		broker.post(CONFIG_CREATED, configuration);
	}
	
	public ConfigurationElement createConfiguration(String name) {
		return write(() -> {
			ConfigurationElement config = WindupFactory.eINSTANCE.createConfigurationElement();
			config.setName(name);
			model.getConfigurationElements().add(config);
			return config;
		});
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
	
	public IPath getGeneratedReportLocation(ConfigurationElement configuration) {
		return reportsDir.append(configuration.getName());
	}
}
