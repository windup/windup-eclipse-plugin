/*******************************************************************************
 * Copyright (c) 2019 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.rules;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
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
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.format.IStructuredFormatProcessor;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.sse.ui.StructuredTextViewerConfiguration;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.eclipse.wst.sse.ui.internal.provisional.style.LineStyleProvider;
import org.eclipse.wst.xml.core.internal.provisional.contenttype.ContentTypeIdForXML;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.format.FormatProcessorXML;
import org.eclipse.wst.xml.ui.StructuredTextViewerConfigurationXML;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.explorer.TempProject;
import org.jboss.tools.windup.ui.internal.rules.xml.XMLRuleTemplate.Template;
import org.jboss.tools.windup.ui.internal.rules.xml.XMLRulesetModelUtil;
import org.jboss.tools.windup.ui.internal.rules.xml.XMLTemplateFactory;
import org.jboss.tools.windup.ui.internal.rules.xml.XMLTemplateVariableProcessor;
import org.jboss.tools.windup.windup.CustomRuleProvider;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@SuppressWarnings("restriction")
public class NewXMLRuleWizard extends Wizard implements IImportWizard {

	@Inject private XMLTemplateFactory templateFactory;
	
	private CustomRuleProvider provider;
	private NewXMLRuleWizardPage xmlPage;
	
	public NewXMLRuleWizard() {
		setNeedsProgressMonitor(true);
		setWindowTitle(Messages.newXMLRule_title);
	}
	
	public void setRuleProvider(CustomRuleProvider provider) {
		this.provider = provider;
	}
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}
	
	@Override
	public void addPages() {
		xmlPage = new NewXMLRuleWizardPage();
		addPage(xmlPage);
	}

	@Override
	public boolean performFinish() {
		SourceViewer sourceViewer = xmlPage.getTemplatePreviewer();
		IDocument document = sourceViewer.getDocument();
		IFile file = (IFile)TempProject.createResource(document.get());
		IStructuredModel scratchModel;
		try {
			scratchModel = StructuredModelManager.getModelManager().createUnManagedStructuredModelFor(file);
			IDOMModel model = (IDOMModel)scratchModel;
			
			IFile ruleset = XMLRulesetModelUtil.getRuleset(provider);
			IDOMModel rulesetModel = XMLRulesetModelUtil.getModel(ruleset, true);
			
			// for selection
			Node firstNode = null; 
			
			if (rulesetModel != null) {
				NodeList nodeList = model.getDocument().getChildNodes();
				for (int i = 0; i < nodeList.getLength(); i++) {
					Node node = nodeList.item(i);
					Node clone = rulesetModel.getDocument().importNode(node, true);
					
					if (i == 0) {
						firstNode = clone;
					}
					
					NodeList rules = rulesetModel.getDocument().getElementsByTagName("rules"); //$NON-NLS-1$
					if (rules.getLength() > 0) {
						Node rulesNode = rules.item(0);
						rulesNode.appendChild(clone);
					}
					else {
						NodeList rulesetNodes = rulesetModel.getDocument().getElementsByTagName("ruleset"); //$NON-NLS-1$
						if (rulesetNodes.getLength() == 1) {
							Node rulesNode = rulesetModel.getDocument().createElement("rules"); //$NON-NLS-1$
							rulesetNodes.item(0).appendChild(rulesNode);
							rulesNode.appendChild(clone);
						}
					}
				}
				
				FileEditorInput input = new FileEditorInput(ruleset);
				IEditorPart sharedEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findEditor(input);
				
				if (rulesetModel.isDirty() && sharedEditor == null) {
					rulesetModel.save();
				}
				
				rulesetModel.releaseFromEdit();
				
				if (firstNode != null) {
					IStructuredFormatProcessor formatProcessor = new FormatProcessorXML();
					formatProcessor.formatNode(firstNode);
				}
				
				if (sharedEditor != null && firstNode != null) {
					sharedEditor.getSite().getSelectionProvider().setSelection(new StructuredSelection(firstNode));
					ITextEditor textEditor = sharedEditor.getAdapter(ITextEditor.class);
					if (firstNode instanceof IndexedRegion && textEditor != null) {
						int start = ((IndexedRegion) firstNode).getStartOffset();
						int length = ((IndexedRegion) firstNode).getEndOffset() - start;
						if ((start > -1) && (length > -1)) {
							textEditor.selectAndReveal(start, length);
						}
					}
				}
			}
		} catch (IOException | CoreException e) {
			WindupUIPlugin.log(e);
		}
		return true;
	}

	
	private class NewXMLRuleWizardPage extends WizardPage {
		
		private Text filterText;
		private TableViewer templateTable;
		private SourceViewer templatePreviewer;
		
		private XMLTemplateVariableProcessor templateProcessor= new XMLTemplateVariableProcessor();
		
		public NewXMLRuleWizardPage() {
			super("xmlRuleTemplatePage"); //$NON-NLS-1$
			setTitle(Messages._UI_WIZARD_NEW_XML_RULE_HEADING);
			setDescription(Messages._UI_WIZARD_NEW_XML_RULE_EXPL);
		}
		
		@Override
		public void createControl(Composite parent) {
			GridLayoutFactory.fillDefaults().applyTo(parent);
			
			Composite container = new Composite(parent, SWT.NONE);
			GridLayoutFactory.fillDefaults().margins(5, 5).applyTo(container);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(container);

			Composite top = new Composite(container, SWT.NONE);
			GridLayoutFactory.fillDefaults().numColumns(1).applyTo(top);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(top);
			
			filterText = new Text(top, SWT.SEARCH | SWT.ICON_SEARCH);
			filterText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			filterText.addModifyListener(onSearch());

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
			List<Template> templates = templateFactory.getTemplates();
			templateTable.setInput(templates);
			templateTable.addFilter(createTemplateFilter());
			
			templatePreviewer = createSourcePreviewer(sashForm);
			
			templateTable.addSelectionChangedListener(new ISelectionChangedListener() {
				@Override
				public void selectionChanged(SelectionChangedEvent e) {
					updateTemplatePreview();
				}
			});
			// Preview the first template.
			templateTable.setSelection(new StructuredSelection(templates.get(0)));
			sashForm.setWeights(new int[] {30, 70});
			setControl(container);
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
		
		public SourceViewer getTemplatePreviewer() {
			return templatePreviewer;
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
		
		private ViewerFilter createTemplateFilter() {
			return new ViewerFilter() {
				@Override
				public boolean select(Viewer viewer, Object parentElement,
						Object element) {
					if (element instanceof Template) {
						return ((Template)element).getName().matches( ".*" + filterText.getText() + ".*");
					}
					return false; 
				}
			};
		}
	}
}
