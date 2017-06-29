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
package org.jboss.tools.windup.ui.internal.editor;

import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.wst.xml.core.internal.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.core.internal.contentmodel.util.DOMNamespaceHelper;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.NodeRow;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.TextNodeRow;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.common.collect.Lists;

@SuppressWarnings("restriction")
public class DefaultElementAttributesComposite extends ElementAttributesComposite {
	
	protected List<NodeRow> rows = Lists.newArrayList();

    @SuppressWarnings("unchecked")
	@Override
	protected void createControls(Composite parent) {
		CMElementDeclaration ed = modelQuery.getCMElementDeclaration(element);
		if (ed != null) {
			List<CMAttributeDeclaration> availableAttributeList = modelQuery.getAvailableContent(element, ed, ModelQuery.INCLUDE_ATTRIBUTES);
		    for (CMAttributeDeclaration declaration : availableAttributeList) {
		    	Node node = findNode(element, ed, declaration);
		    	rows.add(createTextAttributeRow(element, node, declaration, parent, getSpan()));
		    }
		    if (availableAttributeList.isEmpty() && !Boolean.TRUE.equals(ed.getProperty("isInferred"))) { //$NON-NLS-1$
		    	rows.add(createTextAttributeRow(element.getParentNode(), element, ed, parent, getSpan()));
		    }
		}
	}
    
	protected Node findNode(Element parent, CMElementDeclaration ed, CMNode cmNode) {
		Node node = null;
		switch (cmNode.getNodeType()) {
			case CMNode.ATTRIBUTE_DECLARATION: {
				String attributeName = DOMNamespaceHelper.computeName(cmNode, parent, null);
				node = parent.getAttributeNode(attributeName);
				break;
			}
		}
		return node;
	}
    
    protected TextNodeRow createTextAttributeRow(Node parentNode, Node node, CMNode cmNode, Composite parent, int columns) {
    	TextNodeRow row = new TextNodeRow(parentNode, node, cmNode);
		row.createContents(parent, toolkit, columns);
		return row;
    }
    
	@Override
	public void update() {
		rows.forEach(row -> row.bind());
	}
}
