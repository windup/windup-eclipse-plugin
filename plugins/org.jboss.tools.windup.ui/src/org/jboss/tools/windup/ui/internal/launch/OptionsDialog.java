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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.windup.core.services.WindupOptionsService;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.launch.OptionUiFacades.OptionUiFacade;
import org.jboss.tools.windup.windup.ConfigurationElement;
import org.jboss.windup.bootstrap.help.OptionDescription;

public class OptionsDialog extends Dialog {

	private ModelService modelService;
	private WindupOptionsService optionsService;

	private Label optionLabel;
	private Combo optionCombo;
	
	private Composite stackComposite;
	
	private OptionsWidgetManager widgetManager;
	private OptionUiFacade selectedOption;
	
	private ConfigurationElement configuration;
	
	public OptionsDialog(Shell shell, ModelService modelService, 
			WindupOptionsService optionsService, ConfigurationElement configuration) {
		super(shell);
		this.modelService = modelService;
		this.optionsService = optionsService;
		this.configuration = configuration;
	}
	
	private void loadHelp(Composite parent) {
		optionsService.loadOptions(() -> {
			this.widgetManager = new OptionsWidgetManager(modelService.getOptionFacadeManager(), 
					parent, configuration, () -> {
				updateButtons();
			});
			for (String option : widgetManager.getOptions()) {
				optionCombo.add(option);
			}
		});
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite comp = (Composite) super.createDialogArea(parent);
		((GridLayout) comp.getLayout()).numColumns = 1;
		
		Composite container = new Composite(comp, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(2).applyTo(container);
		GridDataFactory.fillDefaults().grab(true, false).hint(400, SWT.DEFAULT).applyTo(container);
		optionLabel = new Label(container, SWT.NONE);
		optionLabel.setText(Messages.OPTION+":");
		optionLabel.setFont(comp.getFont());
		GridDataFactory.fillDefaults().hint(50, SWT.DEFAULT).align(SWT.RIGHT, SWT.CENTER).applyTo(optionLabel);
		
		optionCombo = new Combo(container, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(optionCombo);
		optionCombo.setFont(comp.getFont());
		
		stackComposite = new Composite(comp, SWT.NONE);
		stackComposite.setLayout(new StackLayout());
		GridDataFactory.fillDefaults().grab(true, true).hint(400, 25).applyTo(stackComposite);
		
		loadHelp(stackComposite);
		
		optionCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String selection = widgetManager.getOptions().get(optionCombo.getSelectionIndex());
				OptionUiFacade selectedOption = widgetManager.getOptionUiFacade(selection);
				selectedOption.setFocus();
				Control top = selectedOption.getControl();
				OptionsDialog.this.selectedOption = selectedOption;
				((StackLayout)stackComposite.getLayout()).topControl = top;
				stackComposite.layout(true, true);
				updateButtons();
			}
		});
		return comp;
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId != IDialogConstants.OK_ID) {
			this.selectedOption = null;
		}
		super.buttonPressed(buttonId);
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(Messages.windupOption);
	}

	protected void updateButtons() {
		boolean enabled = this.selectedOption != null ? this.selectedOption.isValid() : false; 
		getButton(IDialogConstants.OK_ID).setEnabled(enabled);
	}
	
	public OptionDescription getSelectedOption() {
		return this.selectedOption.getOptionDescription();
	}
	
	public String getSelectedOptionValue() {
		return this.selectedOption.getValue();
	}

	@Override
	public void create() {
		super.create();
		updateButtons();
	}
}