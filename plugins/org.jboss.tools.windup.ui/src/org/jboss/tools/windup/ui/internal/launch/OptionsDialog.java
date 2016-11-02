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
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.launch.OptionTypeControls.OptionTypeControl;
import org.jboss.windup.bootstrap.help.Help;

public class OptionsDialog extends Dialog {

	private WindupOptionsService optionsService;

	private Label optionLabel;
	private Combo optionCombo;
	
	private Composite stackComposite;
	
	private OptionsWidgetManager widgetManager;
	private OptionTypeControl selectedOption;
	
	public OptionsDialog(Shell shell, WindupOptionsService optionsService) {
		super(shell);
		this.optionsService = optionsService;
	}
	
	private void loadHelp(Composite parent) {
		optionsService.loadOptions((Help help) -> {
			this.widgetManager = new OptionsWidgetManager(help, parent, () -> {
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
		GridDataFactory.fillDefaults().grab(true, true).hint(400, SWT.DEFAULT).applyTo(stackComposite);
		
		loadHelp(stackComposite);
		
		optionCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String selection = widgetManager.getOptions().get(optionCombo.getSelectionIndex());
				OptionsDialog.this.selectedOption = widgetManager.findOptionTypeControl(selection); 
				Control top = OptionsDialog.this.selectedOption.getControl();
				((StackLayout)stackComposite.getLayout()).topControl = top;
				stackComposite.layout(true, true);
				getShell().layout(true, true);
				getShell().notifyListeners(SWT.Resize, null);
				updateButtons();
			}
		});
		
		/*valueLabel = new Label(container, SWT.NONE);
		valueLabel.setText(Messages.VALUE+":");
		valueLabel.setFont(comp.getFont());
		GridDataFactory.fillDefaults().hint(50, SWT.DEFAULT).align(SWT.RIGHT, SWT.CENTER).applyTo(valueLabel);
		
		valueText = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(valueText);
		
		valueText.setFont(comp.getFont());
		valueText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				updateButtons();
			}
		});

		
		valueText.setFocus();*/
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

	@Override
	public void create() {
		super.create();
		updateButtons();
	}
}