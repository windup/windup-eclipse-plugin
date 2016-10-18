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

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.windup.ui.internal.Messages;

public class OptionsDialog extends Dialog {

	private String configurationOption;
	private String value;

	private Label optionLabel;
	private Combo optionCombo;
	private Label valueLabel;
	private Text valueText;
	
	private List<String> configurationOptions;
	
	public OptionsDialog(Shell shell, List<String> configurationOptions) {
		super(shell);
		this.configurationOptions = configurationOptions;
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
			
		for (String option : configurationOptions) {
			optionCombo.add(option);
		}
		
		optionCombo.select(0);
		
		container = new Composite(comp, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(2).applyTo(container);
		GridDataFactory.fillDefaults().grab(true, false).hint(400, SWT.DEFAULT).applyTo(container);
		
		valueLabel = new Label(container, SWT.NONE);
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

		
		valueText.setFocus();
		return comp;
	}

	public String getOption() {
		return configurationOption;
	}
	
	public String getValue() {
		return value;
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			configurationOption = configurationOptions.get(optionCombo.getSelectionIndex());
			value = valueText.getText().trim();
		} else {
			configurationOption = null;
			value = null;
		}
		super.buttonPressed(buttonId);
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(Messages.windupOption);
	}

	protected void updateButtons() {
		String value = valueText.getText().trim();
		getButton(IDialogConstants.OK_ID).setEnabled((value.length() > 0));
	}

	@Override
	public void create() {
		super.create();
		updateButtons();
	}
}