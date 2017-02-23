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
package org.jboss.tools.windup.ui.internal.rules.xml;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.DocumentProviderRegistry;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.jboss.tools.windup.model.domain.WorkspaceResourceUtils;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.windup.CustomRuleProvider;

import com.google.common.collect.Lists;

@SuppressWarnings("restriction")
public class XmlRulesetFileOrchestrator {

	private TreeViewer treeViewer;
	private CustomRuleProvider ruleProvider;
	private FileEditorInput editorInput;
	
	public XmlRulesetFileOrchestrator(TreeViewer treeViewer, CustomRuleProvider ruleProvider) {
		this.treeViewer = treeViewer;
		this.ruleProvider = ruleProvider;
		editorInput = new FileEditorInput(WorkspaceResourceUtils.getFile(ruleProvider.getLocationURI()));
	}
	
	public Object[] getRules() {
		List<Object> rules = Lists.newArrayList();

		return rules.stream().toArray(Object[]::new);
	}
	
	private IDocument getDocument() {
		return getDocumentProvider().getDocument(editorInput);
	}
	
	private IDocumentProvider getDocumentProvider() {
		return DocumentProviderRegistry.getDefault().getDocumentProvider(editorInput);
	}
	
	public void disconnect() {
		getDocumentProvider().disconnect(editorInput);
	}
}
