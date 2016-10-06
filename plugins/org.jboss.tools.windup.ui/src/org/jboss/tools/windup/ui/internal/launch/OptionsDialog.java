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
import java.util.function.Consumer;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.windup.core.services.WindupOptionsService;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.windup.config.ConfigurationOption;

public class OptionsDialog extends Dialog {

	private String key;
	private String value;

	private Label keyLabel;
	private Text keyText;
	private Label valueLabel;
	private Text valueText;
	
	private WindupOptionsService optionsService;

	public OptionsDialog(Shell shell, WindupOptionsService optionsService) {
		super(shell);
		this.optionsService = optionsService;
	}
	
	@Override
	protected void initializeBounds() {
		super.initializeBounds();
		ProgressBar progress = new ProgressBar(getParentShell(), SWT.HORIZONTAL|SWT.INDETERMINATE);
		optionsService.loadOptions(new Consumer<List<ConfigurationOption>>() {
			@Override
			public void accept(List<ConfigurationOption> options) {
				progress.dispose();
				System.out.println(options);
			}
		});
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite comp = (Composite) super.createDialogArea(parent);
		((GridLayout) comp.getLayout()).numColumns = 2;

		keyLabel = new Label(comp, SWT.NONE);
		keyLabel.setText(Messages.KEY);
		keyLabel.setFont(comp.getFont());

		keyText = new Text(comp, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 300;
		keyText.setLayoutData(gd);
		keyText.setFont(comp.getFont());
		keyText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				updateButtons();
			}
		});

		valueLabel = new Label(comp, SWT.NONE);
		valueLabel.setText(Messages.VALUE);
		valueLabel.setFont(comp.getFont());

		valueText = new Text(comp, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 300;
		valueText.setLayoutData(gd);
		valueText.setFont(comp.getFont());
		valueText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				updateButtons();
			}
		});

		return comp;
	}

	public String getKey() {
		return key;
	}
	
	public String getValue() {
		return value;
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			key = keyText.getText().trim();
			value = valueText.getText().trim();
		} else {
			key = null;
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
		String name = keyText.getText().trim();
		String value = valueText.getText().trim();
		getButton(IDialogConstants.OK_ID).setEnabled((name.length() > 0) && (value.length() > 0));
	}

	@Override
	public void create() {
		super.create();
		updateButtons();
	}
}