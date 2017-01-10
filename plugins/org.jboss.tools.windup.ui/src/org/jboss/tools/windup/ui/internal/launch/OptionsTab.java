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
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.windup.core.services.WindupOptionsService;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.windup.ConfigurationElement;
import org.jboss.tools.windup.windup.Pair;
import org.jboss.tools.windup.windup.WindupFactory;
import org.jboss.windup.bootstrap.help.OptionDescription;

import com.google.common.collect.Lists;

/**
 * Tab for configuration Windup options.
 */
@SuppressWarnings("restriction")
public class OptionsTab extends AbstractLaunchConfigurationTab {

	private static final String ID = "org.jboss.tools.windup.ui.launch.OptionsTab"; //$NON-NLS-1$
	
	private ModelService modelService;
	private ConfigurationElement configuration;
	
	private Button generateReportButton;
	private TableViewer optionsViewer;
	
	// TODO: We probably want to use this once we start using an external Windup launcher.
	private WindupOptionsService optionsService;
	
	public OptionsTab(ModelService modelService, WindupOptionsService optionsService) {
		this.modelService = modelService;
		this.optionsService = optionsService;
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().margins(5, 5).applyTo(container);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(container);
		createReportGroup(container);
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
		column.getColumn().setText(Messages.OPTION);
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
				OptionsDialog dialog = new OptionsDialog(parent.getShell(), 
						modelService, optionsService, configuration);
				if (dialog.open() == IDialogConstants.OK_ID) {
					OptionDescription option = dialog.getSelectedOption();
					String value = dialog.getSelectedOptionValue();
					if (!value.isEmpty()) {
						Pair pair = WindupFactory.eINSTANCE.createPair();
						pair.setKey(option.getName());
						pair.setValue(value);
						configuration.getOptions().add(pair);
					}
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
		reloadOptions();
	}

	private void reloadReportGroup() {
		if (generateReportButton != null) {
			generateReportButton.setSelection(configuration.isGenerateReport());
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
		return WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_OPTIONS_TAB);
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
