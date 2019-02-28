/*******************************************************************************
 * Copyright (c) 2019 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.model.edit.domain;

import static org.jboss.tools.windup.model.domain.WindupConstants.SYNCH;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.windup.ConfigurationElement;
import org.jboss.tools.windup.windup.WindupModel;

/**
 * The content provider Windup's configuration element tree.
 */
public class ConfigurationElementTreeContentProvider extends AdapterFactoryContentProvider {
	
	private boolean synched;
	
	@Inject
	public ConfigurationElementTreeContentProvider(ModelService modelService, AdapterFactory adapterFactory) {
		super(adapterFactory);
		adapterFactory.adapt(modelService.getModel(), IStructuredItemContentProvider.class);
	}
	
	@Inject
	@Optional
	private void synch(@UIEventTopic(SYNCH) Boolean synch) {
		this.synched = synch;
		viewer.refresh();
	}
	
	@Override
	public Object[] getChildren(Object object) { 
		if (object instanceof ConfigurationElement) {
			return getChildren((ConfigurationElement)object);
		}
		return new Object[0];
	}
	
	@Override
	public Object[] getElements(Object object) {
		if (object instanceof WindupModel) {
			List<ConfigurationElement> configurations = ((WindupModel) object).getConfigurationElements();
			return configurations.toArray(new Object[configurations.size()]);
		}
		return new Object[0];
	}
	
	@Override
	public boolean hasChildren(Object object) {
		if (!synched) {
			return false;
		}
		if (object instanceof ConfigurationElement) {
			return getChildren((ConfigurationElement)object).length > 0;
		}
		return false;
	}
	
	private Object[] getChildren(ConfigurationElement configuration) {
		/*WindupResult windupResult = configuration.getWindupResult();
		if (windupResult != null && windupResult.getExecutionResults() != null) {
			List<Object> children = Lists.newArrayList();
			children.add(windupResult);
			for (Hint hint : windupResult.getExecutionResults().getHints()) {
				IFile resource = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(new Path(hint.getFile().getAbsolutePath()));
				IJavaElement element = JavaCore.create(resource);
				IJavaElement parent = element.getParent();
			}
			return children.toArray(new Object[children.size()]);
		}*/
		return new Object[0];
		
	}
	
	@Override
	public void notifyChanged(Notification notification) {
		if (viewer != null && viewer.getControl() != null && !viewer.getControl().isDisposed()) {
			Display.getDefault().asyncExec(() -> {
				viewer.refresh();
				((TreeViewer)viewer).expandAll();
			});
		}
	 }
}
