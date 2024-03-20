/*******************************************************************************
 * Copyright (c) 2021 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.rules;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.inject.Inject;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.RuleMessages;
import org.jboss.tools.windup.ui.internal.rules.xml.XMLRulesetModelUtil;
import org.jboss.tools.windup.windup.CustomRuleProvider;

public class NewRuleFromSelectionWizard extends Wizard implements IImportWizard {

	@Inject private ModelService modelService;
	private Combo rulesetCombo;
	
	private IFile selectedRuleset;
	private boolean openEditor = true;
	
	private SelectCustomRulesetWizardPage rulesetPage;
	
	public NewRuleFromSelectionWizard() {
		setNeedsProgressMonitor(false);
		setWindowTitle(Messages.newXMLRule_title);
		setHelpAvailable(false);
	}
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}
	
	@Override
	public void addPages() {
		rulesetPage = new SelectCustomRulesetWizardPage();
		addPage(rulesetPage);
	}

	@Override
	public boolean performFinish() {
		this.selectedRuleset = (IFile)ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(rulesetCombo.getText())); 
		return true;
	}
	
	public IFile getRuleset() {
		return selectedRuleset;
	}
	
	public boolean openEditor() {
		return openEditor;
	}
	
	private class SelectCustomRulesetWizardPage extends WizardPage {
		
		public SelectCustomRulesetWizardPage() {
			super("selectCustomRulesetPage"); //$NON-NLS-1$
			setTitle(Messages.newRuleFromSelectionWizard_heading);
			setDescription(Messages.newRuleFromSelectionWizard_message);
		}
		
		@Override
		public void createControl(Composite parent) {
			GridLayoutFactory.fillDefaults().applyTo(parent);
			
			Composite container = new Composite(parent, SWT.NONE);
			GridLayoutFactory.fillDefaults().margins(5, 5).applyTo(container);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(container);

			Composite rulesetContainer = new Composite(container, SWT.NONE);
			GridLayoutFactory.fillDefaults().numColumns(3).applyTo(rulesetContainer);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(rulesetContainer);
			
			Label rulesetLabel = new Label(rulesetContainer, SWT.NONE);
			rulesetLabel.setText(Messages.newRuleFromSelectionWizard_ruleset);
			GridData data = new GridData();
			data.verticalIndent = -2;
			rulesetLabel.setLayoutData(data);
			
			rulesetCombo = new Combo(rulesetContainer, SWT.READ_ONLY);
			GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.CENTER).applyTo(rulesetCombo);
			
			rulesetCombo.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					validate();
				}
			});
			
			Button newRulesetButton = new Button(rulesetContainer, SWT.PUSH);
			newRulesetButton.setText(Messages.newRuleFromSelectionWizard_newRuleset);
			data = new GridData();
			data.verticalIndent = 2;
			newRulesetButton.setLayoutData(data);
			
			newRulesetButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					NewXMLRulesetWizard wizard = WindupUIPlugin.getDefault().getInjector().getInstance(NewXMLRulesetWizard.class);
					IStructuredSelection selection = new StructuredSelection();
					wizard.init(PlatformUI.getWorkbench(), selection);
					WizardDialog dialog = new WizardDialog(parent.getShell(), wizard);
					if (Window.OK == dialog.open()) {
						selectRuleset(wizard.getNewRulestLocation());
					}
				}
			});
			
			Button openEditorButton = new Button(container, SWT.CHECK);
			openEditorButton.setText(RuleMessages.ruleGenerationOpenEditor);
			openEditorButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					openEditor = openEditorButton.getSelection();
				}
			});
			openEditorButton.setSelection(true);
			
			setControl(container);
			modelService.cleanPhantomCustomRuleProviders();
			
			if (!modelService.getModel().getCustomRuleRepositories().isEmpty()) {
				CustomRuleProvider ruleProvider = modelService.getModel().getCustomRuleRepositories().get(0);
				selectRuleset(ruleProvider.getLocationURI());
			}
		}
		
		private void selectRuleset(String rulesetFile) {
			List<String> locations = modelService.getModel().getCustomRuleRepositories().stream().map(provider -> {
				IFile file = XMLRulesetModelUtil.getRuleset(provider);
				return file.getFullPath().toString();
			}).collect(Collectors.toList());
			rulesetCombo.setItems(locations.toArray(new String[locations.size()]));
			IFile rulesetIFile = XMLRulesetModelUtil.getRuleset(rulesetFile);
			if (rulesetIFile != null && rulesetIFile.exists()) {
				rulesetFile = rulesetIFile.getFullPath().toString();
				int index = locations.indexOf(rulesetFile);
				if (index != -1) {
					rulesetCombo.select(index);	
				}
			}
			validate();
		}
		
		private void validate() {
			getContainer().updateButtons();
		}
		
		private boolean isRulesetValid() {
			return rulesetCombo.getSelectionIndex() != -1;
		}
		
		@Override
		public boolean isPageComplete() {
			return isRulesetValid();
		}
	}
}
