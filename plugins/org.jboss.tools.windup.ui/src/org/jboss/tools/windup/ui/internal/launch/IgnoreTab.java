/*******************************************************************************
 * Copyright (c) 2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.launch;

import java.io.IOException;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.util.TextProcessor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.jboss.tools.windup.core.services.WindupService;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.windup.ConfigurationElement;
import org.jboss.tools.windup.windup.IgnorePattern;
import org.jboss.tools.windup.windup.WindupFactory;

/**
 * Tab for specifying ignore patterns. 
 */
public class IgnoreTab extends AbstractLaunchConfigurationTab {

	private Table ignoreTable;
	private Button addButton;
	private Button removeButton;
	
	private ModelService modelService;
	private WindupService windupService;
	private ConfigurationElement configuration;
	
	public IgnoreTab(ModelService modelService, WindupService windupService) {
		this.modelService = modelService;
		this.windupService = windupService;
	}
	
	@Override
	public void createControl(Composite ancestor) {
		
		Composite parent = new Composite(ancestor, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 5;
		layout.marginHeight = 5;
		layout.numColumns = 2;
		parent.setLayout(layout);
		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		parent.setLayoutData(data);

		Label l1 = new Label(parent, SWT.NULL);
		l1.setText(Messages.ignoreDescription);
		data = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		data.horizontalSpan = 2;
		l1.setLayoutData(data);

		ignoreTable = new Table(parent, SWT.CHECK | SWT.BORDER | SWT.MULTI);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 300;
		ignoreTable.setLayoutData(gd);
		ignoreTable.addListener(SWT.Selection, e -> handleSelection());
		
		ignoreTable.addListener(SWT.Selection, (e) -> {
			if (e.detail == SWT.CHECK) {
				TableItem item = (TableItem)e.item;
				IgnorePattern pattern = (IgnorePattern)item.getData(IgnorePattern.class.getName());
				pattern.setEnabled(item.getChecked());
			}
		});
		
		Composite buttons = new Composite(parent, SWT.NULL);
		buttons.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		buttons.setLayout(layout);

		addButton = new Button(buttons, SWT.PUSH);
		addButton.setText(Messages.ignorePatternAdd);
		addButton.addListener(SWT.Selection, e -> addIgnore());
		GridDataFactory.fillDefaults().grab(true, false).applyTo(addButton);

		removeButton = new Button(buttons, SWT.PUSH);
		removeButton.setText(Messages.ignorePatternRemove);
		removeButton.setEnabled(false);
		removeButton.addListener(SWT.Selection, e -> removeIgnore());
		GridDataFactory.fillDefaults().grab(true, false).applyTo(removeButton);
		
		ignoreTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean enabled = ignoreTable.getSelectionCount() > 0 ? true : false;
				removeButton.setEnabled(enabled);
				refresh();
			}
		});
		
		Dialog.applyDialogFont(ancestor);
		
		super.setControl(parent);
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy launchConfig) {
		int count = ignoreTable.getItemCount();
		String[] patterns = new String[count];
		boolean[] enabled = new boolean[count];
		TableItem[] items = ignoreTable.getItems();
		for (int i = 0; i < count; i++) {
			patterns[i] = items[i].getText();
			enabled[i] = items[i].getChecked();
		}
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy launchConfig) {
		refresh();
	}

	private void refresh() {
		if (ignoreTable != null) {
			try {
				windupService.syncIgnoreWithConfig(this.configuration);
			} catch (IOException e) {
				WindupUIPlugin.log(e);
				MessageDialog.openError(Display.getDefault().getActiveShell(),
	    				"Ignore file error", 
	    				"Error while reading ignore file.");
			}
			ignoreTable.removeAll();
			for (IgnorePattern ignore : configuration.getIgnorePatterns()) {
				createPatternItem(ignore);
			}
		}
	}
	
	private void createPatternItem(IgnorePattern pattern) {
		TableItem item = new TableItem(ignoreTable, SWT.NONE);
		item.setText(TextProcessor.process(pattern.getPattern(), ".*")); //$NON-NLS-1$
		item.setChecked(pattern.isEnabled());
		item.setData(IgnorePattern.class.getName(), pattern);
	}

	private void addIgnore() {
		InputDialog dialog = new InputDialog(getShell(), Messages.ignorePatternShort, Messages.ignorePatternLong, null, null);
		dialog.open();
		if (dialog.getReturnCode() != Window.OK) return;
		String pattern = dialog.getValue();
		if (pattern.equals("")) return; //$NON-NLS-1$
		TableItem[] items = ignoreTable.getItems();
		for (int i = 0; i < items.length; i++) {
			if (items[i].getText().equals(pattern)) {
				MessageDialog.openWarning(getShell(), Messages.ignorePatternExistsShort, Messages.ignorePatternExistsLong);
				return;
			}
		}
		IgnorePattern ignore = WindupFactory.eINSTANCE.createIgnorePattern();
		ignore.setPattern(pattern);
		ignore.setEnabled(true);
		configuration.getIgnorePatterns().add(ignore);
		
		refresh();
	}

	private void removeIgnore() {
		for (TableItem item : ignoreTable.getSelection()) {
			IgnorePattern ignore = (IgnorePattern)item.getData(IgnorePattern.class.getName());
			configuration.getIgnorePatterns().remove(ignore);
		}
		int[] selection = ignoreTable.getSelectionIndices();
		ignoreTable.remove(selection);
		refresh();
	}
	
	private void handleSelection() {
		if (ignoreTable.getSelectionCount() > 0) {
			removeButton.setEnabled(true);
		} else {
			removeButton.setEnabled(false);
		}
	}
	
	@Override
	public void initializeFrom(ILaunchConfiguration launchConfig) {
		initializeConfiguration(launchConfig);
	}
	
	private void initializeConfiguration(ILaunchConfiguration launchConfig) {
		this.configuration = modelService.findConfiguration(launchConfig.getName());
		if (configuration == null) {
			this.configuration = LaunchUtils.createConfiguration(launchConfig.getName(), modelService);
		}
		refresh();
	}

	@Override
	public String getName() {
		return Messages.ignorePatternsLabel;
	}
	
	@Override
	public Image getImage() {
		return WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_PATTERN);
	}
	
	@Override
	public boolean isValid(ILaunchConfiguration launchConfig) {
		return true;
	}
}
