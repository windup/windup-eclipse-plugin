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

import java.util.List;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.wst.sse.core.internal.provisional.IModelStateListener;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.jboss.tools.windup.model.domain.WorkspaceResourceUtils;
import org.jboss.tools.windup.windup.CustomRuleProvider;
import org.w3c.dom.Node;

@SuppressWarnings("restriction")
public class XMLRulesetFileOrchestrator {

	private TreeViewer treeViewer;
	private CustomRuleProvider ruleProvider;
	
	private IDOMModel model;
	private IModelStateListener modelListener;
			
	public XMLRulesetFileOrchestrator(TreeViewer treeViewer, CustomRuleProvider ruleProvider) {
		this.treeViewer = treeViewer;
		this.ruleProvider = ruleProvider;
		this.modelListener = createListener();
	}
	
	public void connect() {
		if (this.model != null) {
			model.removeModelStateListener(modelListener);
		}
		model = XMLRulesetModelUtil.getModel(WorkspaceResourceUtils.getFile(ruleProvider.getLocationURI()), false);
		model.addModelStateListener(modelListener);
	}
	
	public List<Node> getRules() {
		return XMLRulesetModelUtil.getRules(ruleProvider.getLocationURI());
	}
	
	public String getRulesetId() {
		return XMLRulesetModelUtil.getRulesetId(ruleProvider.getLocationURI());
	}
	
	private void refresh() {
		model.removeModelStateListener(modelListener);
		if (!treeViewer.getTree().isDisposed()) {
			treeViewer.refresh(ruleProvider);
		}
	}
	
	private IModelStateListener createListener() {
		return new IModelStateListener() {
			@Override
			public void modelResourceMoved(IStructuredModel oldModel, IStructuredModel newModel) {
				refresh();
			}
			@Override
			public void modelResourceDeleted(IStructuredModel theModel) {
				refresh();
			}
			@Override
			public void modelReinitialized(IStructuredModel structuredModel) {
				refresh();
			}
			@Override
			public void modelDirtyStateChanged(IStructuredModel model, boolean isDirty) {
			}
			@Override
			public void modelChanged(IStructuredModel theModel) {
				refresh();
			}
			@Override
			public void modelAboutToBeReinitialized(IStructuredModel structuredModel) {
			}
			@Override
			public void modelAboutToBeChanged(IStructuredModel model) {
			}
		};
	}
}
