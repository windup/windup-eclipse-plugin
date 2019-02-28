/*******************************************************************************
 * Copyright (c) 2019 Red Hat, Inc.
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

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQueryAction;
import org.jboss.tools.windup.ui.internal.RuleMessages;
import org.jboss.tools.windup.ui.internal.editor.ElementAttributesContainer;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.NodeRow;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.TextNodeRow;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.common.base.Objects;

@SuppressWarnings({"restriction"})
public class CommentDelegate extends ElementUiDelegate {
	
	@Override
	protected boolean shouldFilterElementInsertAction(ModelQueryAction action) {
		return true;
	}
	
	@Override
	protected void createTabs() {
		addTab(DetailsTab.class);
	}
	
	public void createControls(Composite parent, Element element, CMElementDeclaration ed, List<NodeRow> rows) {
		
		CommentEditor commentEditor = createCommentEditor(parent, element, ed);
		
//		TextNodeRow row = new TextNodeRow(element, null) {
//			protected Node getNode() {
//				return element;
//			}
//			@Override
//			public String getDocumentation() {
//				return "";
//			}
//			@Override
//			protected void update() {
//				if (!Objects.equal(text.getText(), getNode().getTextContent())) {
//					text.setText(getNode().getTextContent());
//				}
//			}
//		};
//		row.createContents(parent, toolkit, 2);
//		rows.add(row);
	}
	
	private CommentEditor createCommentEditor(Composite parent, Element element, CMElementDeclaration dec) {
		Element task = super.element;
//		Element task = findTaskElement();
//		CMElementDeclaration dec = modelQuery.getCMElementDeclaration(task);
		IEclipseContext commentContext = context.createChild();
		commentContext.set(Composite.class, parent);
		commentContext.set(Element.class, task);
		commentContext.set(CMElementDeclaration.class, dec);
		return ContextInjectionFactory.make(CommentEditor.class, commentContext);
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
