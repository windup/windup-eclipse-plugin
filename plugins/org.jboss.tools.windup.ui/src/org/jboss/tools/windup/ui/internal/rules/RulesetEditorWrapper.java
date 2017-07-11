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

import org.eclipse.core.resources.IFile;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelStateListener;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.ui.internal.tabletree.IDesignViewer;
import org.eclipse.wst.xml.ui.internal.tabletree.XMLMultiPageEditorPart;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.editor.RulesetEditorDependenciesPage;
import org.jboss.tools.windup.ui.internal.editor.RulesetEditorDocumentationPage;
import org.jboss.tools.windup.ui.internal.editor.RulesetEditorOverviewPage;
import org.jboss.tools.windup.ui.internal.editor.RulesetExamplesPage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@SuppressWarnings("restriction")
public class RulesetEditorWrapper extends XMLMultiPageEditorPart {
	
	private RulesetDesignPage designPage;
	
	@Override
	protected void createPages() {
		CTabFolder folder = (CTabFolder)super.getContainer();
		RulesetEditorOverviewPage overviewPage = new RulesetEditorOverviewPage(getContainer());
		int index = addPage(overviewPage.getControl());
		setPageText(index, Messages.rulesOverview);
		RulesetEditorDependenciesPage dependenciesPage = new RulesetEditorDependenciesPage(getContainer());
		index = addPage(dependenciesPage.getControl());
		setPageText(index, Messages.rulesEditor_dependencies);
		super.createPages();
		folder.getItem(index+1).setImage(WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_RULES_FORM));
		folder.getItem(index+2).setImage(WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_XML));
		RulesetEditorDocumentationPage documentationPage = new RulesetEditorDocumentationPage(getContainer());
		index = addPage(documentationPage.getControl());
		setPageText(index, Messages.documentationTitle);
		folder.getItem(index).setImage(WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_REPORT));
		RulesetExamplesPage examplesPage = new RulesetExamplesPage(getContainer());
		index = addPage(examplesPage.getControl());
		setPageText(index, Messages.examplesTitle);
		setActivePage(2);
	}
	
	@Override
	protected IDesignViewer createDesignPage() {
		designPage = new RulesetDesignPage();
		IFile file = null;
		if (super.getEditorInput() instanceof FileEditorInput) {
			file = ((FileEditorInput)super.getEditorInput()).getFile();
		}
		designPage.createControls(getContainer(), file);
		return designPage;
	}
	
	@Override
	public void dispose() {
		super.dispose();
		designPage.dispose();
	}
	
	@Override
	public void setFocus() {
		super.setFocus();
	}
	
	public Document getDocument() {
		return designPage.getDocument();
	}
	
	public void selectAndReveal(Element element) {
		designPage.getEditor().selectAndReveal(element);
	}
	
	public static final class RulesetDesignPage implements IDesignViewer {
		
		private RulesetEditor editor;
		private Document document;
		
		private IDOMModel domModel;
		private IModelStateListener modelListener = new ModelListener();
		
		public void createControls(Composite container, IFile file) {
			IEclipseContext context = WindupUIPlugin.getDefault().getContext();
			context.set(Composite.class, container);
			context.set(IFile.class, file);
			editor = ContextInjectionFactory.make(RulesetEditor.class, context.createChild());
		}
		
		public RulesetEditor getEditor() {
			return editor;
		}

		@Override
		public Control getControl() {
			return editor.getControl();
		}

		@Override
		public String getTitle() {
			return Messages.rulesEditor_tabTitle;
		}
		
		public Document getDocument() {
			return document;
		}

		@Override
		public void setDocument(IDocument document) {
			if (domModel != null) {
				domModel.removeModelStateListener(modelListener);
			}
			/*
			 * let the text editor to be the one that manages the model's lifetime
			 */
			IStructuredModel model = null;
			try {
				model = StructuredModelManager.getModelManager().getExistingModelForRead(document);

				if ((model != null) && (model instanceof IDOMModel)) {
					this.domModel = (IDOMModel)model;
					domModel.addModelStateListener(modelListener);
					
					Document domDoc = null;
					domDoc = ((IDOMModel) model).getDocument();
					this.document = domDoc;
					editor.setDocument(domDoc);
				}
			}
			finally {
				if (model != null) {
					model.releaseFromRead();
				}
			}
		}
		
		public void dispose() {
			if (domModel != null) {
				domModel.removeModelStateListener(modelListener);
			}
		}
		
		@Override
		public ISelectionProvider getSelectionProvider() {
			return editor.getSelectionProvider();
		}
		
		private void refreshDocument(IDOMModel model) {
			if (editor.getControl() != null && !editor.getControl().isDisposed()) {
				editor.setDocument(((IDOMModel) model).getDocument());
			}
		}
		
		private class ModelListener implements IModelStateListener {
			@Override
			public void modelResourceMoved(IStructuredModel oldModel, IStructuredModel newModel) {
				refreshDocument((IDOMModel)newModel);
			}
			@Override
			public void modelResourceDeleted(IStructuredModel theModel) {
				refreshDocument((IDOMModel)theModel);
			}
			@Override
			public void modelReinitialized(IStructuredModel structuredModel) {
				refreshDocument((IDOMModel)structuredModel);
			}
			@Override
			public void modelDirtyStateChanged(IStructuredModel model, boolean isDirty) {
			}
			@Override
			public void modelChanged(IStructuredModel theModel) {
				refreshDocument((IDOMModel)theModel);
			}
			@Override
			public void modelAboutToBeReinitialized(IStructuredModel structuredModel) {
			}
			@Override
			public void modelAboutToBeChanged(IStructuredModel model) {
			}
		}
	}
}