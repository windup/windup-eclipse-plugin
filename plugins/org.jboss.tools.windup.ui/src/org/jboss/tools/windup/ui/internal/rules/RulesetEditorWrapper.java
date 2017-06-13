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

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.ui.internal.tabletree.IDesignViewer;
import org.eclipse.wst.xml.ui.internal.tabletree.XMLMultiPageEditorPart;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.editor.RulesetEditorDocumentationPage;
import org.jboss.tools.windup.ui.internal.editor.RulesetEditorOverviewPage;
import org.jboss.tools.windup.ui.internal.editor.RulesetExamplesPage;
import org.w3c.dom.Document;

@SuppressWarnings("restriction")
public class RulesetEditorWrapper extends XMLMultiPageEditorPart {
	
	private RulesetDesignPage designPage;

	@Override
	protected void createPages() {
		RulesetEditorOverviewPage overviewPage = new RulesetEditorOverviewPage(getContainer());
		int index = addPage(overviewPage.getControl());
		setPageText(index, Messages.rulesOverview);
		super.createPages();
		RulesetEditorDocumentationPage documentationPage = new RulesetEditorDocumentationPage(getContainer());
		index = addPage(documentationPage.getControl());
		setPageText(index, Messages.documentationTitle);
		RulesetExamplesPage examplesPage = new RulesetExamplesPage(getContainer());
		index = addPage(examplesPage.getControl());
		setPageText(index, Messages.examplesTitle);
	}
	
	@Override
	protected IDesignViewer createDesignPage() {
		designPage = new RulesetDesignPage();
		designPage.createControls(getContainer());
		return designPage;
	}
	
	public static final class RulesetDesignPage implements IDesignViewer {
		
		private RulesetEditor editor;
		
		public void createControls(Composite container) {
			IEclipseContext context = WindupUIPlugin.getDefault().getContext();
			context.set(Composite.class, container);
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
			return Messages.rulesEditor_title;
		}

		@Override
		public void setDocument(IDocument document) {
			/*
			 * let the text editor to be the one that manages the model's lifetime
			 */
			IStructuredModel model = null;
			try {
				model = StructuredModelManager.getModelManager().getExistingModelForRead(document);

				if ((model != null) && (model instanceof IDOMModel)) {
					Document domDoc = null;
					domDoc = ((IDOMModel) model).getDocument();
					editor.setDocument(domDoc);
				}
			}
			finally {
				if (model != null) {
					model.releaseFromRead();
				}
			}
		}

		@Override
		public ISelectionProvider getSelectionProvider() {
			return editor.getSelectionProvider();
		}
	}
}