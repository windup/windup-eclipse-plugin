/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
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

import javax.inject.Inject;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.ui.StructuredTextViewerConfiguration;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.eclipse.wst.sse.ui.internal.provisional.style.LineStyleProvider;
import org.eclipse.wst.xml.core.internal.provisional.contenttype.ContentTypeIdForXML;
import org.eclipse.wst.xml.ui.StructuredTextViewerConfigurationXML;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.model.domain.WorkspaceResourceUtils;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.rules.xml.XMLRuleTemplate.Template;
import org.jboss.tools.windup.ui.internal.rules.xml.XMLTemplateVariableProcessor;
import org.jboss.tools.windup.windup.CustomRuleProvider;

@SuppressWarnings("restriction")
public class NewRuleFromSelectionWizard extends Wizard implements IImportWizard {

	@Inject private ModelService modelService;
	private Combo rulesetCombo;
	
	private SelectCustomRulesetWizardPage rulesetPage;
	
	public NewRuleFromSelectionWizard() {
		setNeedsProgressMonitor(true);
		setWindowTitle(Messages.newXMLRule_title);
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
		return true;
	}
	
	private class SelectCustomRulesetWizardPage extends WizardPage {
		
		private TableViewer templateTable;
		private SourceViewer templatePreviewer;
		
		private XMLTemplateVariableProcessor templateProcessor= new XMLTemplateVariableProcessor();
		
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
			
			Composite top = new Composite(container, SWT.NONE);
			GridLayoutFactory.fillDefaults().numColumns(1).applyTo(top);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(top);
			
			Composite bottom = new Composite (container, SWT.NONE);
			GridLayoutFactory.fillDefaults().applyTo(bottom);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(bottom);
			
			SashForm sashForm = new SashForm(bottom, SWT.VERTICAL|SWT.SMOOTH);
			GridData gd = new GridData(GridData.FILL_BOTH);
			gd.heightHint = 300;
			sashForm.setLayoutData(gd);
			
			templateTable = new TableViewer(sashForm, SWT.BORDER|SWT.SINGLE|SWT.FULL_SELECTION|SWT.H_SCROLL|SWT.V_SCROLL);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(templateTable.getControl());
			templateTable.setContentProvider(ArrayContentProvider.getInstance());
			templateTable.setLabelProvider(new LabelProvider() {
				@Override
				public Image getImage(Object element) {
					return WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_RULE);
				}
				@Override
				public String getText(Object element) {
					return ((Template)element).getName();
				}
			});
			
			templatePreviewer = createSourcePreviewer(sashForm);
			
			templateTable.addSelectionChangedListener(new ISelectionChangedListener() {
				@Override
				public void selectionChanged(SelectionChangedEvent e) {
					updateTemplatePreview();
				}
			});
			// Preview the first template.
			sashForm.setWeights(new int[] {30, 70});
			setControl(container);
			
			modelService.cleanPhantomCustomRuleProviders();
			
			if (!modelService.getModel().getCustomRuleRepositories().isEmpty()) {
				CustomRuleProvider ruleProvider = modelService.getModel().getCustomRuleRepositories().get(0);
				selectRuleset(ruleProvider.getLocationURI());
			}
		}
		
		private void selectRuleset(String rulesetFile) {
			List<String> locations = modelService.getModel().getCustomRuleRepositories().stream().map(provider -> {
				IFile file = WorkspaceResourceUtils.getFile(provider.getLocationURI());
				return file.getProject().getName() + "/" + file.getName();
			}).collect(Collectors.toList());
			rulesetCombo.setItems(locations.toArray(new String[locations.size()]));
			IFile rulesetIFile = WorkspaceResourceUtils.getFile(rulesetFile);
			if (rulesetIFile != null && rulesetIFile.exists()) {
				rulesetFile = rulesetIFile.getProject().getName() + "/" + rulesetIFile.getName();
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
		
		private void updateTemplatePreview() {
			IStructuredSelection ss = (IStructuredSelection)templateTable.getSelection();
			if (ss.isEmpty()) {
				templatePreviewer.getDocument().set("");
			}
			else {
				Template template = (Template)ss.getFirstElement();
				templatePreviewer.getDocument().set(template.generate());
			}
		}
		
		private SourceViewer createSourcePreviewer(Composite parent) {
			SourceViewerConfiguration sourceViewerConfiguration = new StructuredTextViewerConfiguration() {
				StructuredTextViewerConfiguration baseConfiguration = new StructuredTextViewerConfigurationXML();

				public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
					return baseConfiguration.getConfiguredContentTypes(sourceViewer);
				}

				public LineStyleProvider[] getLineStyleProviders(ISourceViewer sourceViewer, String partitionType) {
					return baseConfiguration.getLineStyleProviders(sourceViewer, partitionType);
				}

				public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
					ContentAssistant assistant = new ContentAssistant();
					assistant.enableAutoActivation(true);
					assistant.enableAutoInsert(true);
					assistant.setContentAssistProcessor(getTemplateProcessor(), IDocument.DEFAULT_CONTENT_TYPE);
					return assistant;
				}
			};
			return doCreateViewer(parent, sourceViewerConfiguration);
		}
		
		private SourceViewer doCreateViewer(Composite parent, SourceViewerConfiguration viewerConfiguration) {
			SourceViewer viewer = new StructuredTextViewer(parent, null, null, false, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
			viewer.getTextWidget().setFont(JFaceResources.getFont("org.eclipse.wst.sse.ui.textfont")); //$NON-NLS-1$
			IStructuredModel scratchModel = StructuredModelManager.getModelManager().createUnManagedStructuredModelFor(ContentTypeIdForXML.ContentTypeID_XML);
			viewer.configure(viewerConfiguration);
			viewer.setDocument(scratchModel.getStructuredDocument());
			return viewer;
		}
		
		private IContentAssistProcessor getTemplateProcessor() {
			return templateProcessor;
		}
		
		private ModifyListener onSearch() {
			return e -> {
				templateTable.refresh();
				TableItem[] items = templateTable.getTable().getItems();
				if (items.length > 0) {
					templateTable.setSelection(new StructuredSelection(items[0].getData()));
				}
			};
		}
	}
}
