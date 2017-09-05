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
package org.jboss.tools.windup.ui.internal.rules.delegate;

import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.wst.xml.core.internal.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.jboss.tools.windup.ui.internal.RuleMessages;
import org.jboss.tools.windup.ui.internal.editor.ElementAttributesContainer;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.NodeRow;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.TextNodeRow;
import org.w3c.dom.Element;

@SuppressWarnings({"restriction"})
public class JavaClassAnnotationLiteralDelegate extends ElementUiDelegate {
	
	protected void createTabs() {
		addTab(DetailsTab.class);
	}

	@Override
	public Object[] getChildren() {
		return super.getChildren();
	}
	
	@SuppressWarnings("unchecked")
	public void createControls(Composite parent, Element element, CMElementDeclaration ed, List<NodeRow> rows) {
		List<CMAttributeDeclaration> availableAttributeList = modelQuery.getAvailableContent(element, ed, ModelQuery.INCLUDE_ATTRIBUTES);
	    for (CMAttributeDeclaration declaration : availableAttributeList) {
	  		TextNodeRow row = ElementAttributesContainer.createTextAttributeRow(element, toolkit, declaration, parent, 2);
    	  		rows.add(row);
	    }
	}
	
	public static class DetailsTab extends ElementAttributesContainer {
		
		@PostConstruct
		public void createControls(Composite parent) {
			Composite client = super.createSection(parent, 2, RuleMessages.javaclass_annotation_literal_sectionTitle, RuleMessages.javaclass_annotation_literal_description);
			CMElementDeclaration ed = modelQuery.getCMElementDeclaration(element);
			if (ed != null) {
				uiDelegate.createControls(client, element, ed, rows);
			}
		}
	}
}