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
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.eclipse.pde.internal.ui.editor.FormLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.wst.xml.core.internal.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQueryAction;
import org.jboss.tools.windup.ui.internal.RuleMessages;
import org.jboss.tools.windup.ui.internal.editor.ElementAttributesContainer;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.NodeRow;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.RulesetConstants;
import org.jboss.tools.windup.ui.internal.rules.xml.XMLRulesetModelUtil;
import org.w3c.dom.Element;

@SuppressWarnings("restriction")
public class WhereDelegate extends ElementUiDelegate {

	@Override
	protected void createTabs() {
		addTab(DetailsTab.class);
	}
	
	@Override
	public Object[] getChildren() {
		return new Object[] {};
	}
	
	@Override
	protected boolean shouldFilterElementInsertAction(ModelQueryAction action) {
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public void createControls(Composite parent, Element element, CMElementDeclaration ed, List<NodeRow> rows) {
		List<CMAttributeDeclaration> availableAttributeList = modelQuery.getAvailableContent(element, ed, ModelQuery.INCLUDE_ATTRIBUTES);
		for (CMAttributeDeclaration declaration : availableAttributeList) {
			rows.add(ElementAttributesContainer.createTextAttributeRow(element, toolkit, declaration, parent, 2));
		}
	}
	
	public static class DetailsTab extends ElementAttributesContainer {
		
		private PatternContainer patternContainer;
		
		@PostConstruct
		private void createControls(Composite parent) {
			Composite client = super.createSection(parent, 2, RuleMessages.whereTitle, RuleMessages.whereDescription);
			CMElementDeclaration ed = modelQuery.getCMElementDeclaration(element);
			if (ed != null) {
				uiDelegate.createControls(client, element, ed, rows);
			}
			patternContainer = new PatternContainer(client);
		}
		
		@Override
		protected void bind() {
			super.bind();
			patternContainer.bind();
		}
		
		private class PatternContainer {
			
			private Text text;
			
			public PatternContainer(Composite parent) {
				createControls(parent);
			}
			
			public void bind() {
				Element matchesElement = findMatchesElement();
				if (matchesElement != null) {
					String value = matchesElement.getAttribute(RulesetConstants.PATTERN);
					if (!Objects.equals(text.getText(), value)) {
						text.setText(value);
					}
				}
				else {
					text.setText("");
				}
			}
			
			private void createControls(Composite parent) {
				Label label = toolkit.createLabel(parent, RulesetConstants.PATTERN + ":", SWT.NULL); //$NON-NLS-1$
				label.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
				
				this.text = toolkit.createText(parent, "", SWT.SINGLE); //$NON-NLS-1$
				text.setLayoutData(createGridData(2));
				text.addModifyListener(new ModifyListener() {
					@Override
					public void modifyText(ModifyEvent e) {
						if (!blockNotification) {
							try {
								model.aboutToChangeModel();
								Element matchesElement = findMatchesElement();
								if (matchesElement == null) {
									matchesElement = element.getOwnerDocument().createElement(RulesetElementUiDelegateFactory.RulesetConstants.MATCHES);
									element.appendChild(matchesElement);
								}
								matchesElement.setAttribute(RulesetConstants.PATTERN, text.getText());
							}
							finally {
								model.changedModel();
							}
						}
					}
				});
			}
			
			protected GridData createGridData(int span) {
				GridData gd = new GridData(span == 2 ? GridData.FILL_HORIZONTAL : GridData.HORIZONTAL_ALIGN_FILL);
				gd.widthHint = 20;
				gd.horizontalSpan = span - 1;
				gd.horizontalIndent = FormLayoutFactory.CONTROL_HORIZONTAL_INDENT;
				return gd;
			}
			
			private Element findMatchesElement() {
				return XMLRulesetModelUtil.findChildWithName(element, RulesetConstants.MATCHES);
			}
		}
	}
}
