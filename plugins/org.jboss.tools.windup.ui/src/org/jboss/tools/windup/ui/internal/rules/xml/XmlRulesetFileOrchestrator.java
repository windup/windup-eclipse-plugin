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
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.collect.Lists;

@SuppressWarnings("restriction")
public class XmlRulesetFileOrchestrator {

	private TreeViewer treeViewer;
	private CustomRuleProvider ruleProvider;
	private FileEditorInput editorInput;
	
	private IDocumentProvider documentProvider;
	
	public XmlRulesetFileOrchestrator(TreeViewer treeViewer, CustomRuleProvider ruleProvider) {
		this.treeViewer = treeViewer;
		this.ruleProvider = ruleProvider;
		editorInput = new FileEditorInput(WorkspaceResourceUtils.getFile(ruleProvider.getLocationURI()));
	}
	
	public IDocument getDocument() {
		return getDocumentProvider().getDocument(editorInput);
	}
	
	private IDocumentProvider getDocumentProvider() {
		if (documentProvider == null) {
			documentProvider = DocumentProviderRegistry.getDefault().getDocumentProvider(editorInput);
			try {
				documentProvider.connect(editorInput);
			} catch (CoreException e) {
				WindupUIPlugin.log(e);
			}
		}
		return documentProvider;
	}
	
	public void disconnect() {
		if (documentProvider != null) {
			documentProvider.disconnect(editorInput);
			documentProvider = null;
		}
	}
	
	//
	//
	//
	public String getRulesteId(String locationURI) {
		String rulesetId = null;
		IDOMModel model = getModel();
		if (model != null) {
			Document document = model.getDocument();
			NodeList rulesets = document.getElementsByTagName("ruleset");  //$NON-NLS-1$
			if (rulesets.getLength() > 0) {
				Node ruleset = rulesets.item(0);
				NamedNodeMap attributes = ruleset.getAttributes();
				Node rulesetIdNode = attributes.getNamedItem("id");  //$NON-NLS-1$
				if (rulesetIdNode != null) {
					rulesetId = ((Attr) rulesetIdNode).getValue();
				}
			}
			model.releaseFromRead();
		}
		return rulesetId;
	}
	
	public List<Node> getRules() {
		List<Node> rules = Lists.newArrayList();
		IDOMModel model = getModel();
		Document document = model.getDocument();
		NodeList ruleNodes = document.getElementsByTagName("rule");  //$NON-NLS-1$
		if (ruleNodes.getLength() > 0) {
			for (int i = 0; i < ruleNodes.getLength(); i++) {
				Node ruleNode = ruleNodes.item(i);
				rules.add(ruleNode);
			}
		}
		model.releaseFromRead();
		return rules;
	}
	
	public String getRuleId(Node node) {
		String ruleId = "";
		NamedNodeMap attributes = node.getAttributes();
		Node ruleIdNode = attributes.getNamedItem("id");  //$NON-NLS-1$
		if (ruleIdNode != null) {
			ruleId = ((Attr) ruleIdNode).getValue();
		}
		return ruleId;
	}
	
	public IDOMModel getModel() {
		try {
			IStructuredModel model = StructuredModelManager.getModelManager().getModelForRead(WorkspaceResourceUtils.getFile(ruleProvider.getLocationURI()));
			if (model != null && model instanceof IDOMModel) {
				return (IDOMModel) model;
			}
		} catch (IOException | CoreException e) {
			WindupUIPlugin.log(e);
		}
		return null;
	}
}
