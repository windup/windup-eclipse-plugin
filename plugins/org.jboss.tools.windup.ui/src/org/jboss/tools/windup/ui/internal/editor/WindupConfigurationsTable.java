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
package org.jboss.tools.windup.ui.internal.editor;

import static org.jboss.tools.windup.model.domain.WindupConstants.ACTIVE_CONFIG;
import static org.jboss.tools.windup.model.domain.WindupConstants.CONFIG_CREATED;
import static org.jboss.tools.windup.model.domain.WindupConstants.CONFIG_DELETED;
import static org.jboss.tools.windup.ui.internal.Messages.launchTabTitle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.action.ContributionManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.menus.IMenuService;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.windup.ConfigurationElement;

/**
 * The composite containing Windup's configurations.
 */
public class WindupConfigurationsTable {
	
	private static final String TOOLBAR_ID = "toolbar:org.jboss.tools.windup.toolbar.configurations.table"; //$NON-NLS-1$
	
	private static final String SELECTED_CONFIGURATION = "selectedConfiguration";
 
	@Inject private IEventBroker broker;
	@Inject private IMenuService menuService;
	@Inject private ModelService modelService;
	@Inject private FormToolkit toolkit;
	@Inject private IEclipsePreferences preferences;
	
	private ToolBarManager toolBarManager;
	private TableViewer tableViewer;
	
	private ConfigurationElement selectedConfiguration;
	
	@PostConstruct
	private void create(Composite parent) {
		parent.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		
		Composite container = toolkit.createComposite(parent);
		GridLayoutFactory.fillDefaults().margins(0, 10).applyTo(container);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(container);
		
		Section section = toolkit.createSection(container, Section.TITLE_BAR);
		section.setText(launchTabTitle);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(section);
		
		Composite client = toolkit.createComposite(section);
		toolkit.paintBordersFor(client);
		GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).applyTo(client);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(client);
		section.setClient(client);
		
		createControls(client, section);
		
		focus();
	}
	
	private void createControls(Composite parent, Section section) {
		createToolbar(section);
		this.tableViewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		tableViewer.addSelectionChangedListener((e) -> {
			this.selectedConfiguration = (ConfigurationElement)((StructuredSelection)e.getSelection()).getFirstElement(); 
			broker.post(ACTIVE_CONFIG, selectedConfiguration);
			update();
		});
		
		GridDataFactory.fillDefaults().grab(true, true).applyTo(tableViewer.getTable());
		
		AdapterFactoryEditingDomain domain = modelService.getDomain();
		AdapterFactory factory = domain.getAdapterFactory();
		tableViewer.setContentProvider(new AdapterFactoryContentProvider(factory));
		tableViewer.setLabelProvider(new AdapterFactoryLabelProvider(factory));
		tableViewer.setInput(modelService.getModel());
	}
	
	private void createToolbar(Section section) {
		this.toolBarManager = new ToolBarManager(SWT.FLAT);
		ToolBar toolBar = this.toolBarManager.createControl(section);
		section.setTextClient(toolBar);
		menuService.populateContributionManager((ContributionManager)toolBarManager, TOOLBAR_ID);
	}
	
	@Inject
	@Optional
	private void configCreated(@UIEventTopic(CONFIG_CREATED) ConfigurationElement configuration) {
		tableViewer.setSelection(new StructuredSelection(configuration), true);
	}
	
	@Inject
	@Optional
	private void configDeleted(@UIEventTopic(CONFIG_DELETED) ConfigurationElement configuration) {
	}
	
	@PreDestroy
	private void dispose() {
		if (selectedConfiguration != null) {
			preferences.put(SELECTED_CONFIGURATION, selectedConfiguration.getName());
		}
	}
	
	private void update() {
		broker.post(UIEvents.REQUEST_ENABLEMENT_UPDATE_TOPIC, UIEvents.ALL_ELEMENT_ID);
	}
	
	private void focus() {
		String previouslySelected = preferences.get(SELECTED_CONFIGURATION, null);
		if (previouslySelected != null) {
			ConfigurationElement configuration = modelService.findConfiguration(previouslySelected);
			if (configuration != null) {
				tableViewer.setSelection(new StructuredSelection(configuration), true);
			}
		}
		tableViewer.getTable().setFocus();
	}
}
