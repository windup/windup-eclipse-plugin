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

import java.net.URL;
import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.xml.core.internal.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQueryAction;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.RuleMessages;
import org.jboss.tools.windup.ui.internal.editor.ElementAttributesContainer;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.NodeRow;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.ReferenceNodeRow;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.RulesetConstants;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.common.base.Objects;

@SuppressWarnings({"restriction"})
public class LinkDelegate extends ElementUiDelegate {
	
	@Override
	protected boolean shouldFilterElementInsertAction(ModelQueryAction action) {
		return true;
	}
	
	@Override
	protected void createTabs() {
		addTab(DetailsTab.class);
	}
	
	@SuppressWarnings("unchecked")
	public void createControls(Composite parent, Element element, CMElementDeclaration ed, List<NodeRow> rows) {
		List<CMAttributeDeclaration> availableAttributeList = modelQuery.getAvailableContent(element, ed, ModelQuery.INCLUDE_ATTRIBUTES);
		for (CMAttributeDeclaration declaration : availableAttributeList) {
    			Node node = findNode(element, ed, declaration);
		    	if (Objects.equal(declaration.getAttrName(), RulesetConstants.LINK_HREF)) {
		    		IProject project = context.get(IFile.class).getProject();
		    		ReferenceNodeRow row = new ReferenceNodeRow(element, declaration) {
					@Override
					protected void openReference() {
						if (node != null && !node.getNodeValue().isEmpty()) {
						 	try {
								PlatformUI.getWorkbench().getBrowserSupport().
									createBrowser(WindupUIPlugin.PLUGIN_ID).openURL(new URL(node.getNodeValue()));
							}
							catch (Exception e) {
								WindupUIPlugin.log(e);
							}
						}
					}
		    		}; 
				rows.add(row);
				row.createContents(parent, toolkit, 2);
		    	}
		    	else {
		    		rows.add(ElementAttributesContainer.createTextAttributeRow(element, toolkit, declaration, parent, 2));
		    	}
		}
	}
	
	public static class DetailsTab extends ElementAttributesContainer {
		
		@PostConstruct
		private void createControls(Composite parent) {
			Composite client = super.createSection(parent, 2, RuleMessages.link_title, RuleMessages.link_description);
			CMElementDeclaration ed = modelQuery.getCMElementDeclaration(element);
			if (ed != null) {
				uiDelegate.createControls(client, element, ed, rows);
			}
		}
	}
}