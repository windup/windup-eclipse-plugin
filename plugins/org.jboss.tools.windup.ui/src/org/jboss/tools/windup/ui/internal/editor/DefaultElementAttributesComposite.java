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
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.core.internal.modelquery.ModelQueryUtil;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.AttributeRow;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.TextAttributeRow;

import com.google.common.collect.Lists;

@SuppressWarnings("restriction")
public class DefaultElementAttributesComposite extends ElementAttributesComposite {
	
	protected List<AttributeRow> rows = Lists.newArrayList();

    @SuppressWarnings("unchecked")
	@Override
	protected void createControls(Composite parent) {
		IStructuredModel model = ((IDOMDocument)element.getOwnerDocument()).getModel();
		ModelQuery modelQuery = ModelQueryUtil.getModelQuery(model);
		CMElementDeclaration ed = modelQuery.getCMElementDeclaration(element);
		if (ed != null) {
			List<CMAttributeDeclaration> availableAttributeList = modelQuery.getAvailableContent(element, ed, ModelQuery.INCLUDE_ATTRIBUTES);
		    for (CMAttributeDeclaration declaration : availableAttributeList) {
		    	rows.add(createRow(declaration, parent));
		    }
		}
	}
    
    protected TextAttributeRow createRow(CMAttributeDeclaration declaration, Composite parent) {
    	boolean required = declaration.getUsage() == CMAttributeDeclaration.REQUIRED;
    	TextAttributeRow row = new TextAttributeRow(element, declaration.getAttrName(), required);
		row.createContents(parent, toolkit, 2);
		return row;
    }

	@Override
	public void update() {
		rows.forEach(row -> row.bind());
	}
}
