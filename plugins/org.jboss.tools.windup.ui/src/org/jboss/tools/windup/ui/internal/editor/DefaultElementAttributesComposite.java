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
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.core.internal.contentmodel.modelqueryimpl.ModelQueryImpl;
import org.eclipse.wst.xml.core.internal.contentmodel.util.DOMNamespaceHelper;
import org.eclipse.wst.xml.core.internal.modelquery.ModelQueryUtil;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.NodeRow;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.TextNodeRow;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
		    	rows.add(createTextAttributeRow(element, node, declaration, parent));
		    }
		    if (availableAttributeList.isEmpty() && !Boolean.TRUE.equals(ed.getProperty("isInferred"))) { //$NON-NLS-1$
		    	rows.add(createTextAttributeRow(element.getParentNode(), element, ed, parent));
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
		  /*case CMNode.ELEMENT_DECLARATION:
		  case CMNode.GROUP: {
		      NodeList childElements = parent.getElementsByTagName(cmNode.getNodeName());
		    break;
		  }
		  case CMNode.DATA_TYPE: {
		    int contentType = ed.getContentType();
		    result = (contentType == CMElementDeclaration.MIXED ||
		              contentType == CMElementDeclaration.PCDATA ||
		              contentType == CMElementDeclaration.ANY);
		    break;
		  }*/
        }
        return node;
    }
    
    protected TextNodeRow createTextAttributeRow(Node parentNode, Node node, CMNode cmNode, Composite parent) {
    	TextNodeRow row = new TextNodeRow(parentNode, node, cmNode);
		row.createContents(parent, toolkit, 2);
		return row;
    }
    
	@Override
	public void update() {
		rows.forEach(row -> row.bind());
	}
}
