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
package org.jboss.tools.windup.ui.internal.launch;

import static org.jboss.tools.windup.ui.internal.Messages.Options;

import java.util.List;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.internal.ui.SWTFactory;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.windup.core.services.WindupOptionsService;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.windup.ConfigurationElement;
import org.jboss.tools.windup.windup.Pair;
import org.jboss.tools.windup.windup.WindupFactory;

import com.google.common.collect.Lists;

/**
 * Tab for configuration Windup rules.
 */
@SuppressWarnings("restriction")
public class OptionsRulesTab extends AbstractLaunchConfigurationTab {

	private static final String ID = "org.jboss.tools.windup.ui.launch.WindupRulesTab"; //$NON-NLS-1$
	
	private ModelService modelService;
	private ConfigurationElement configuration;
	
	private Button generateReportButton;
	private TableViewer rulesDirectoryViewer;
	private TableViewer optionsViewer;
	
	private WindupOptionsService optionsService;
	
	public OptionsRulesTab(ModelService modelService, WindupOptionsService optionsService) {
		this.modelService = modelService;
		this.optionsService = optionsService;
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().margins(5, 5).applyTo(container);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(container);
		createReportGroup(container);
		createCustomRulesGroup(container);
		createOptionsGroup(container);
		super.setControl(container);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(container, ID);
	}
	
	private void createReportGroup(Composite parent) {
		Group group = SWTFactory.createGroup(parent, Messages.windupReport+":", 1, 1, GridData.FILL_HORIZONTAL);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(group);
		generateReportButton = SWTFactory.createCheckButton(group, Messages.windupGenerateReport, null, true, GridData.GRAB_HORIZONTAL);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(generateReportButton);
		generateReportButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				configuration.setGenerateReport(generateReportButton.getSelection());
			}
		});
	}
	
	private void createCustomRulesGroup(Composite parent) {
		Group group = SWTFactory.createGroup(parent, Messages.windupCustomRules+":", 2, 1, GridData.FILL_BOTH);
		GridDataFactory.fillDefaults().grab(true, false).hint(70, 100).applyTo(group);
		rulesDirectoryViewer = new TableViewer(group, SWT.MULTI|SWT.BORDER|SWT.FULL_SELECTION|SWT.H_SCROLL|SWT.V_SCROLL);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(rulesDirectoryViewer.getTable());
		rulesDirectoryViewer.setContentProvider(ArrayContentProvider.getInstance());
		rulesDirectoryViewer.setLabelProvider(new LabelProvider());
		createCustomRulesButtonBar(group);
	}
	
	private void createCustomRulesButtonBar(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().margins(0, 0).spacing(0, 0).applyTo(container);
		GridDataFactory.fillDefaults().grab(false, true).applyTo(container);
		
		Button addButton = new Button(container, SWT.PUSH);
		addButton.setText(Messages.windupAdd);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(addButton);
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(parent.getShell());
				String directory = dialog.open();
				if (directory != null) {
					if (!configuration.getUserRulesDirectories().contains(directory)) {
						configuration.getUserRulesDirectories().add(directory);
						reloadCustomRules();
					}
				}
			}
		});
		
		Button removeButton = new Button(container, SWT.PUSH);
		removeButton.setText(Messages.windupRemove);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(removeButton);
		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				StructuredSelection ss = (StructuredSelection)rulesDirectoryViewer.getSelection();
				if (!ss.isEmpty()) {
					@SuppressWarnings("unchecked")
					List<String> paths = (List<String>)ss.toList();
					configuration.getUserRulesDirectories().removeAll(paths);
					reloadCustomRules();
				}
			}
		});
	}
	
	private void createOptionsGroup(Composite parent) {
		Group group = SWTFactory.createGroup(parent, Messages.Options+":", 2, 1, GridData.FILL_BOTH);
		GridDataFactory.fillDefaults().grab(true, true).hint(70, 100).applyTo(group);
		optionsViewer = new TableViewer(group, SWT.MULTI|SWT.BORDER|SWT.FULL_SELECTION|SWT.H_SCROLL|SWT.V_SCROLL);
		optionsViewer.getTable().setHeaderVisible(true);
		optionsViewer.getTable().setLinesVisible(true);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(optionsViewer.getTable());
		optionsViewer.setContentProvider(ArrayContentProvider.getInstance());
		
		TableViewerColumn column = new TableViewerColumn(optionsViewer, SWT.NONE);
		column.getColumn().setResizable(true);
		column.getColumn().setMoveable(true);
		column.getColumn().setWidth(200);
		column.getColumn().setText(Messages.KEY);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((Pair)element).getKey();
			}
		});
		
		column = new TableViewerColumn(optionsViewer, SWT.NONE);
		column.getColumn().setResizable(true);
		column.getColumn().setMoveable(true);
		column.getColumn().setWidth(200);
		column.getColumn().setText(Messages.VALUE);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((Pair)element).getValue();
			}
		});
		createOptionsButtonBar(group);
	}
	
	private void createOptionsButtonBar(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().margins(0, 0).spacing(0, 0).applyTo(container);
		GridDataFactory.fillDefaults().grab(false, true).applyTo(container);
		
		Button addButton = new Button(container, SWT.PUSH);
		addButton.setText(Messages.windupAdd);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(addButton);
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				OptionsDialog dialog = new OptionsDialog(parent.getShell(), optionsService);
				if (dialog.open() == IDialogConstants.OK_ID) {
					String key = dialog.getKey();
					String value = dialog.getValue();
					Pair pair = WindupFactory.eINSTANCE.createPair();
					pair.setKey(key);
					pair.setValue(value);
					configuration.getOptions().add(pair);
					reloadOptions();
				}
			}
		});
		
		Button removeButton = new Button(container, SWT.PUSH);
		removeButton.setText(Messages.windupRemove);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(removeButton);
		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				StructuredSelection ss = (StructuredSelection)optionsViewer.getSelection();
				if (!ss.isEmpty()) {
					@SuppressWarnings("unchecked")
					List<Pair> options = (List<Pair>)ss.toList();
					configuration.getOptions().removeAll(options);
					reloadOptions();
				}
			}
		});
	}
	
	private void reload() {
		reloadReportGroup();
		reloadCustomRules();
		reloadOptions();
	}

	private void reloadReportGroup() {
		if (generateReportButton != null) {
			generateReportButton.setSelection(configuration.isGenerateReport());
		}
	}
	
	private void reloadCustomRules() {
		if (rulesDirectoryViewer != null) {
			rulesDirectoryViewer.setInput(Lists.newArrayList(configuration.getUserRulesDirectories()));
		}
	}
	
	private void reloadOptions() {
		if (optionsViewer != null) {
			optionsViewer.setInput(Lists.newArrayList(configuration.getOptions()));
		}
	}
	
	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy launchConfig) {
		initializeConfiguration(launchConfig);
	}
	
	@Override
	public void performApply(ILaunchConfigurationWorkingCopy launchConfig) {
		configuration.setName(launchConfig.getName());
	}

	@Override
	public String getName() {
		return Options;
	}
	
	@Override
	public Image getImage() {
		return WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_RULE);
	}
	
	@Override
	public boolean isValid(ILaunchConfiguration launchConfig) {
		return true;
	}
	
	@Override
	public void initializeFrom(ILaunchConfiguration launchConfig) {
		initializeConfiguration(launchConfig);
	}
	
	private void initializeConfiguration(ILaunchConfiguration launchConfig) {
		this.configuration = modelService.findConfiguration(launchConfig.getName());
		if (configuration == null) {
			this.configuration = modelService.createConfiguration(launchConfig.getName());
		}
		reload();
	}
}
