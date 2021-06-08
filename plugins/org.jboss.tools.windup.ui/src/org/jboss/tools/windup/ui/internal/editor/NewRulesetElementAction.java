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
package org.jboss.tools.windup.ui.internal.editor;

import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.actions.BaseSelectionListenerAction;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.core.internal.modelquery.ModelQueryUtil;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.rules.delegate.ElementUiDelegate;
import org.jboss.tools.windup.ui.internal.services.RulesetDOMService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

@SuppressWarnings("restriction")
public class NewRulesetElementAction extends BaseSelectionListenerAction {

	private Viewer viewer;
	private RulesetDOMService domService;
	
	public NewRulesetElementAction(Viewer viewer, RulesetDOMService domService) {
		super(Messages.newElement);
		this.viewer = viewer;
		this.domService = domService;
		viewer.addSelectionChangedListener(this);
	}
	
	@Override
	public void run() {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				performAction();
			}
		};
		BusyIndicator.showWhile(viewer.getControl().getShell().getDisplay(), r);
	}
	
	private void performAction() {
		Document document = (Document)viewer.getInput();
		Element rulesetElement = domService.findOrCreateRulesetElement(document);
		Element rulesElement = domService.findOrCreateRulesElement(rulesetElement);
		createRule(rulesElement);
	}
	
	private void createRule(Element rulesElement) {
		IStructuredModel model = ((IDOMDocument)viewer.getInput()).getModel();
		ModelQuery modelQuery = ModelQueryUtil.getModelQuery(model);
		CMElementDeclaration ed = modelQuery.getCMElementDeclaration(rulesElement);
		if (ed != null) {
			CMElementDeclaration cmNode = (CMElementDeclaration)modelQuery.getAvailableContent(rulesElement, ed, ModelQuery.VALIDITY_STRICT).get(1);
			AddNodeAction action = (AddNodeAction)ElementUiDelegate.createAddElementAction(
					model, rulesElement, cmNode, rulesElement.getChildNodes().getLength(), null, (TreeViewer)viewer);
			action.run();
			List<Node> result = action.getResult();
			if (!result.isEmpty()) {
				Element ruleElement = (Element)result.get(0);
				domService.generateNextRuleId(ruleElement);
				((TreeViewer)viewer).expandToLevel(ruleElement, TreeViewer.ALL_LEVELS);
				((TreeViewer)viewer).setSelection(new StructuredSelection(ruleElement), true);
			}
		}
	}
	
	@Override
	public ImageDescriptor getImageDescriptor() {
		return WindupUIPlugin.getImageDescriptor(WindupUIPlugin.IMG_ADD);
	}
	
	@Override
	public String getToolTipText() {
		return Messages.newElement;
	}
}
