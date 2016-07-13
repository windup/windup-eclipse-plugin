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
package org.jboss.tools.windup.model.edit.domain;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.windup.ConfigurationElement;
import org.jboss.tools.windup.windup.WindupModel;

/**
 * The content provider Windup's configuration element tree.
 */
public class ConfigurationElementTreeContentProvider extends AdapterFactoryContentProvider {
	
	private static final String WINDUP_CONFIGS = "Launch Configurations";
	
	public static class ConfigurationTreeInput {
		
		private ConfigurationTreeInputRoot root;
		
		public ConfigurationTreeInput(ModelService modelService) {
			this.root = new ConfigurationTreeInputRoot(modelService.getModel());
		}
		
		public Object[] getElements() {
			return new Object[]{root};
		}
	}
	
	private static class ConfigurationTreeInputRoot {
		private WindupModel model;
		
		public ConfigurationTreeInputRoot(WindupModel model) {
			this.model = model;
		}
		
		public Object[] getElements() {
			List<ConfigurationElement> configurations = model.getConfigurationElements();
			return configurations.toArray(new Object[configurations.size()]);
		}
		
		@Override
		public String toString() {
			return WINDUP_CONFIGS;
		}
	}

	public ConfigurationElementTreeContentProvider(ModelService modelService, AdapterFactory adapterFactory) {
		super(adapterFactory);
		adapterFactory.adapt(modelService.getModel(), IStructuredItemContentProvider.class);
	}
	
	@Override
	public Object[] getChildren(Object object) { 
		if (object instanceof ConfigurationTreeInputRoot) {
			return ((ConfigurationTreeInputRoot)object).getElements();
		}
		return new Object[0];
	}
	
	@Override
	public Object[] getElements(Object object) {
		if (object instanceof ConfigurationTreeInput) {
			return ((ConfigurationTreeInput)object).getElements();
		}
		return new Object[0];
	}
	
	@Override
	public boolean hasChildren(Object object) {
		if (object instanceof ConfigurationTreeInputRoot) {
			ConfigurationTreeInputRoot root = (ConfigurationTreeInputRoot)object;
			return root.getElements().length > 0;
		}
		return false;
	}
	
	@Override
	public void notifyChanged(Notification notification) {
		if (viewer != null && viewer.getControl() != null && !viewer.getControl().isDisposed()) {
			viewer.refresh();
			((TreeViewer)viewer).expandAll();
		}
	 }
}
