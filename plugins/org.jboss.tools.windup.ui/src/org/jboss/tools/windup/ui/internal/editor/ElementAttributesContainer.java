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
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wst.xml.core.internal.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.NodeRow;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.TextNodeRow;
import org.jboss.tools.windup.ui.internal.rules.ElementDetailsSection;
import org.jboss.tools.windup.ui.internal.rules.ElementUiDelegate;
import org.w3c.dom.Node;

import com.google.common.collect.Lists;

@SuppressWarnings("restriction")
public class ElementAttributesContainer extends ElementDetailsSection {
	
	protected List<NodeRow> rows = Lists.newArrayList();
	
    @SuppressWarnings("unchecked")
	protected void createControls(Composite parent, int span) {
		CMElementDeclaration ed = modelQuery.getCMElementDeclaration(element);
		if (ed != null) {
			List<CMAttributeDeclaration> availableAttributeList = modelQuery.getAvailableContent(element, ed, ModelQuery.INCLUDE_ATTRIBUTES);
		    for (CMAttributeDeclaration declaration : availableAttributeList) {
		    		Node node = ElementUiDelegate.findNode(element, ed, declaration);
		    		rows.add(ElementAttributesContainer.createTextAttributeRow(element, toolkit, node, declaration, parent, /*getSpan()*/ span));
		    }
		    if (availableAttributeList.isEmpty() && !Boolean.TRUE.equals(ed.getProperty("isInferred"))) { //$NON-NLS-1$
		    		rows.add(ElementAttributesContainer.createTextAttributeRow(element.getParentNode(), toolkit, element, ed, parent, /*getSpan()*/ span));
		    }
		}
	}
    
    protected static TextNodeRow createTextAttributeRow(Node parentNode, FormToolkit toolkit, Node node, CMNode cmNode, Composite parent, int columns) {
    		TextNodeRow row = new TextNodeRow(parentNode, node, cmNode);
		row.createContents(parent, toolkit, columns);
		return row;
    }
    
    @Override
	public void update() {
		rows.forEach(row -> row.bind());
	}
}
