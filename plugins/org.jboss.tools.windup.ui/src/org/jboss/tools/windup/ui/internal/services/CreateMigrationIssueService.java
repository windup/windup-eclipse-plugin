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
package org.jboss.tools.windup.ui.internal.services;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.resources.IFile;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.internal.ui.actions.SelectionConverter;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jdt.internal.ui.javaeditor.JavaTextSelection;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;
import org.eclipse.ui.internal.e4.compatibility.CompatibilityPart;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.sse.ui.internal.editor.EditorModelUtil;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.xpath.core.util.XSLTXPathHelper;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.rules.NewRuleFromSelectionWizard;
import org.jboss.tools.windup.ui.internal.rules.RulesetEditor;
import org.jboss.tools.windup.ui.internal.rules.RulesetEditorWrapper;
import org.jboss.tools.windup.ui.internal.services.ContextMenuService.WindupAction;
import org.jboss.tools.windup.ui.internal.views.TaskListView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.common.collect.Lists;

@SuppressWarnings("restriction")
@Creatable
public class CreateMigrationIssueService implements MouseListener, IMenuListener {
	
	@Inject private EPartService partService;
	@Inject private RulesetSelectionCreationService creationService;
	@Inject private RulesetDOMService domService;
	
	private ITextEditor editor;
	
	private WindupAction CREATE_MIGRATION_TASK = new WindupAction(Messages.createMigrationIssue,
		WindupUIPlugin.getImageDescriptor(WindupUIPlugin.IMG_WINDUP), () -> {
		MPart part = partService.showPart(TaskListView.VIEW_ID, PartState.ACTIVATE);
		TaskListView view = (TaskListView)part.getObject();
		view.createMigrationIssueFromSelection();
	});
	
	@Inject
	private void execute(@Named(IServiceConstants.ACTIVE_PART) MPart part) {
		unregisterListener();
		if (part != null) {
			ITextEditor theEditor = getTextEditor(part);
			if (theEditor != null) {
				MenuManager menuManager = getMenuManager(theEditor);
				if (menuManager != null) {
					registerListener(theEditor, menuManager);
				}
			}
		}
	}
	
	public ITextEditor getTextEditor(MPart part) {
		ITextEditor theEditor = null;
		Object client = part.getObject();
		if (client instanceof CompatibilityPart) {
			IWorkbenchPart workbenchPart = ((CompatibilityPart) client).getPart();
			if (!(workbenchPart instanceof ITextEditor)) {
				workbenchPart = workbenchPart.getAdapter(ITextEditor.class);
			}
			if (workbenchPart instanceof ITextEditor) {
				theEditor = (ITextEditor)workbenchPart;
			}
		}
		return theEditor;
	}
	
	private void unregisterListener() {
		if (editor != null) {
			MenuManager menuManager = getMenuManager(editor);
			if (menuManager != null) {
				menuManager.removeMenuListener(this);
			}
			editor = null;
		}
	}
	
	private MenuManager getMenuManager(ITextEditor editor) {
		Control control = editor.getAdapter(Control.class);
		if (control != null && !control.isDisposed()) {
			Menu menu = control.getMenu();
			if (menu != null && !menu.isDisposed()) {
				return (MenuManager)menu.getData(MenuManager.MANAGER_KEY);
			}
		}
		return null;
	}
	
	private void registerListener(ITextEditor editor, MenuManager menuManager) {
		this.editor = editor;
		menuManager.addMenuListener(this);
	}

	@Override
	public void menuAboutToShow(IMenuManager manager) {
		//manager.add(CREATE_MIGRATION_TASK);
		WindupAction action = createRuleFromSelectionAction(editor);
		if (action != null) {
			manager.add(action);
		}
		action = createRuleFromXPathAction(editor);
		if (action != null) {
			manager.add(action);
		}
	}
	
	@Override
	public void mouseDoubleClick(MouseEvent e) {
	}
	@Override
	public void mouseDown(MouseEvent e) {
	}
	@Override
	public void mouseUp(MouseEvent e) {
	}
	
	private WindupAction createRuleFromSelectionAction(ITextEditor theEditor) {
		if (theEditor instanceof JavaEditor) {
			JavaEditor editor = (JavaEditor)theEditor;
			if (SelectionConverter.getInputAsCompilationUnit(editor) != null) {
				ITextSelection textSelection = (ITextSelection)editor.getSelectionProvider().getSelection();
				JavaTextSelection javaSelection = new JavaTextSelection(domService.getEditorInput(editor), domService.getDocument(editor), textSelection.getOffset(), textSelection.getLength());
				ASTNode[] selectedNodes = javaSelection.resolveSelectedNodes();
				if (selectedNodes.length == 0) {
					ASTNode coveringNode = javaSelection.resolveCoveringNode();
					if (coveringNode != null) {
						selectedNodes = new ASTNode[] {coveringNode};
					}
				}
				return createRuleFromSelectionAction(selectedNodes);
			}
		}
		return null;
	}
	
	public WindupAction createRuleFromSelectionAction(ASTNode[] nodes) {
		WindupAction action = null;
		/*
		 * TODO: I'm pretty sure we can create a JavaTextSelection without requiring the selection to come from within a JavaEditor.
		 * This would make it flexible to allow creating the rule from a selection of text regardless of the editor the text is within.
		 * We did something similar when we had an embedded java editor in the javaclass UI delegate. Could we re-use this functionality
		 * and set it up in the background, and make it re-usable?
		 */
		action = new WindupAction(Messages.createRuleFromSelection,
			WindupUIPlugin.getImageDescriptor(WindupUIPlugin.IMG_WINDUP), () -> {
				IEclipseContext context = WindupUIPlugin.getDefault().getContext();
				NewRuleFromSelectionWizard wizard = ContextInjectionFactory.make(NewRuleFromSelectionWizard.class, context);
				WizardDialog wizardDialog = new WizardDialog(Display.getDefault().getActiveShell(), wizard) {
					public Point getInitialSize() {
						return new Point(575, 220);
					}
				};
				if (wizardDialog.open() == Window.OK) {
					IFile ruleset = wizard.getRuleset();
					if (ruleset != null && ruleset.exists()) {
						Document document = getDocument(ruleset);
						if (document != null) {
							IStructuredModel model = ((IDOMNode) document).getModel();
							IStructuredDocument doc = model.getStructuredDocument();
							boolean dirty = model.isDirty();
							if (doc == null) {
								FileEditorInput input = new FileEditorInput(ruleset);
								TextFileDocumentProvider provider = new TextFileDocumentProvider();
								
								try {
									provider.connect(input);
									
									IDocument iDoc = provider.getDocument(input);
									model = StructuredModelManager.getModelManager().getModelForEdit((IStructuredDocument) iDoc);
									EditorModelUtil.addFactoriesTo(model);
									
									document = ((IDOMModel) model).getDocument();
									List<Element> elements = creationService.createRuleFromJavaEditorSelection(document, nodes);
									openEditor(wizard.openEditor(), document, elements, ruleset);
									provider.disconnect(input);
								}
								catch (Exception e) {
									WindupUIPlugin.log(e);
								}
							}
							else {
								List<Element> elements = creationService.createRuleFromJavaEditorSelection(document, nodes);
								openEditor(wizard.openEditor(), document, elements, ruleset);
							}
							if (!dirty) {
								try {
									model.save();
								}
								catch (Exception e) {
									WindupUIPlugin.log(e);
								}
							}
						}
						else {
							WindupUIPlugin.logErrorMessage("Unable to obtain Document for rule generation." ); //$NON-NLS-1$
						}
					}
				}
			});
		return action;
	}
	
	private WindupAction createRuleFromXPathAction(ITextEditor theEditor) {
		WindupAction action = null;
		
		ISelection selection = theEditor.getSelectionProvider().getSelection();
		if (selection instanceof IStructuredSelection) {
			Object obj = ((IStructuredSelection)selection).getFirstElement();
			if (obj != null && obj instanceof IDOMNode) {
				IDOMNode node = (IDOMNode) obj;
				String location = XSLTXPathHelper.calculateXPathToNode(node);
				action = doCreateRuleFromXPathAction(location);
			}
		}
		return action;
	}
	
	private WindupAction doCreateRuleFromXPathAction(String xpath) {
		WindupAction action = new WindupAction(Messages.createRuleFromXPath,
				WindupUIPlugin.getImageDescriptor(WindupUIPlugin.IMG_WINDUP), () -> {
					IEclipseContext context = WindupUIPlugin.getDefault().getContext();
					NewRuleFromSelectionWizard wizard = ContextInjectionFactory.make(NewRuleFromSelectionWizard.class, context);
					WizardDialog wizardDialog = new WizardDialog(Display.getDefault().getActiveShell(), wizard) {
						public Point getInitialSize() {
							return new Point(575, 220);
						}
					};
					if (wizardDialog.open() == Window.OK) {
						IFile ruleset = wizard.getRuleset();
						if (ruleset != null && ruleset.exists()) {
							Document document = getDocument(ruleset);
							if (document != null) {
								IStructuredModel model = ((IDOMNode) document).getModel();
								IStructuredDocument doc = model.getStructuredDocument();
								boolean dirty = model.isDirty();
								if (doc == null) {
									FileEditorInput input = new FileEditorInput(ruleset);
									TextFileDocumentProvider provider = new TextFileDocumentProvider();
									
									try {
										provider.connect(input);
										
										IDocument iDoc = provider.getDocument(input);
										model = StructuredModelManager.getModelManager().getModelForEdit((IStructuredDocument) iDoc);
										EditorModelUtil.addFactoriesTo(model);
										
										document = ((IDOMModel) model).getDocument();
										
										Element xpathElement = creationService.createRuleFromXPath(document, xpath);
										openEditor(wizard.openEditor(), document, Lists.newArrayList(xpathElement), ruleset);
										provider.disconnect(input);
									}
									catch (Exception e) {
										WindupUIPlugin.log(e);
									}
								}
								else {
									Element xpathElement = creationService.createRuleFromXPath(document, xpath);
									openEditor(wizard.openEditor(), document, Lists.newArrayList(xpathElement), ruleset);
								}
								if (!dirty) {
									try {
										model.save();
									}
									catch (Exception e) {
										WindupUIPlugin.log(e);
									}
								}
							}
							else {
								WindupUIPlugin.logErrorMessage("Unable to obtain Document for rule generation." ); //$NON-NLS-1$
							}
						}
					}
				});
		return action;
	}
	
	private void openEditor(boolean openEditor, Document document, List<Element> elements, IFile ruleset) {
		try {
			if (elements != null && !elements.isEmpty()) {
				if (!openEditor) {
					IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findEditor(new FileEditorInput(ruleset));
					if (editorPart != null && editorPart instanceof RulesetEditorWrapper) {
						RulesetEditorWrapper wrapper = (RulesetEditorWrapper)editorPart;
						wrapper.selectAndReveal(elements.get(elements.size()-1));
					}
				}
				if (openEditor) {
					IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new FileEditorInput(ruleset), 
		            		RulesetEditor.ID, true, IWorkbenchPage.MATCH_INPUT | IWorkbenchPage.MATCH_ID);
					if (editorPart instanceof RulesetEditorWrapper) {
						RulesetEditorWrapper wrapper = (RulesetEditorWrapper)editorPart;
						wrapper.selectAndReveal(elements.get(elements.size()-1));
					}					
				}
			}
		}
		catch (Exception e) {
			WindupUIPlugin.log(e);
		}
	}
	
	private Document getDocument(IFile rulesetFile) {
		IStructuredModel model = null;
		try {
			model = StructuredModelManager.getModelManager().getExistingModelForRead(rulesetFile);
			if (model == null) {
				model = StructuredModelManager.getModelManager().getModelForRead(rulesetFile);
			}
			if ((model != null) && (model instanceof IDOMModel)) {
				return ((IDOMModel) model).getDocument();
			}
		} 
		catch (Exception e) {
			WindupUIPlugin.log(e);
		}
		finally {
			if (model != null) {
				model.releaseFromRead();
			}
		}
		return null;
	}
}
