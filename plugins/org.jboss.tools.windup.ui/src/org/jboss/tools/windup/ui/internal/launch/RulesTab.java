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

import static org.jboss.tools.windup.ui.internal.Messages.Rules;

import java.util.List;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.internal.ui.SWTFactory;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
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

import com.google.common.collect.Lists;

@SuppressWarnings("restriction")
public class RulesTab extends AbstractLaunchConfigurationTab {
	
	private static final String ID = "org.jboss.tools.windup.ui.launch.RulesTab"; //$NON-NLS-1$

	private ModelService modelService;
	private ConfigurationElement configuration;
	
	private TableViewer rulesRepositoryViewer;
	
	// TODO: We probably want to use this once we start using an external Windup launcher.
	private WindupOptionsService optionsService;
	
	public RulesTab(ModelService modelService, WindupOptionsService optionsService) {
		this.modelService = modelService;
		this.optionsService = optionsService;
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().margins(5, 5).applyTo(container);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(container);
		createCustomRulesGroup(container);
		super.setControl(container);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(container, ID);
	}
	
	private void createCustomRulesGroup(Composite parent) {
		Group group = SWTFactory.createGroup(parent, Messages.windupCustomRules+":", 2, 1, GridData.FILL_BOTH);
		GridDataFactory.fillDefaults().grab(true, true).hint(70, 100).applyTo(group);
		rulesRepositoryViewer = new TableViewer(group, SWT.MULTI|SWT.BORDER|SWT.FULL_SELECTION|SWT.H_SCROLL|SWT.V_SCROLL);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(rulesRepositoryViewer.getTable());
		rulesRepositoryViewer.setContentProvider(ArrayContentProvider.getInstance());
		rulesRepositoryViewer.setLabelProvider(new LabelProvider());
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
				StructuredSelection ss = (StructuredSelection)rulesRepositoryViewer.getSelection();
				if (!ss.isEmpty()) {
					@SuppressWarnings("unchecked")
					List<String> paths = (List<String>)ss.toList();
					configuration.getUserRulesDirectories().removeAll(paths);
					reloadCustomRules();
				}
			}
		});
	}
	
	private void reload() {
		reloadCustomRules();
	}
	
	private void reloadCustomRules() {
		if (rulesRepositoryViewer != null) {
			rulesRepositoryViewer.setInput(Lists.newArrayList(configuration.getUserRulesDirectories()));
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
		return Rules;
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
