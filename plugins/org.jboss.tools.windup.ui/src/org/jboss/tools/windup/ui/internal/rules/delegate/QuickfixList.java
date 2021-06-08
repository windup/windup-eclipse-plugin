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
package org.jboss.tools.windup.ui.internal.rules.delegate;

import java.util.List;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.pde.internal.ui.editor.FormLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.ui.internal.tabletree.TreeContentHelper;
import org.eclipse.xtext.util.Pair;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.RuleMessages;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory;
import org.jboss.tools.windup.ui.internal.rules.delegate.QuickfixDelegate.QUICKFIX_TYPE;
import org.w3c.dom.Element;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

@SuppressWarnings({"restriction"})
public class QuickfixList extends ListContainer {

	public QuickfixList(FormToolkit toolkit, TreeContentHelper contentHelper, ModelQuery modelQuery, IStructuredModel model,
			RulesetElementUiDelegateFactory uiDelegateFactory, IEclipseContext context) {
		super(toolkit, contentHelper, modelQuery, model, uiDelegateFactory, context);
	}
	
	@Override
	protected ListItem createListItem(Composite parent, Element element) {
		return new QuickfixListItem(parent, toolkit, element, contentHelper, modelQuery, model, uiDelegateFactory, context);
	}
	
	private static class QuickfixListItem extends ListItem {
		
		private SearchItem searchItem;
		private TextItem textItem;
		
		private boolean blockNotification;
		
		@Override
		public void delete() {
			super.delete();
			if (searchItem != null) {
				searchItem.dispose();
			}
		}
		
		public QuickfixListItem(Composite parent, FormToolkit toolkit, Element element, TreeContentHelper contentHelper, ModelQuery modelQuery, IStructuredModel model,
				RulesetElementUiDelegateFactory uiDelegateFactory, IEclipseContext context) {
			super(parent, toolkit, element, contentHelper, modelQuery, model, uiDelegateFactory, context);
		}
		
		@Override
		public ListItem createChildControls(Composite parent, ListItem previousContainer, int childLevel) {
			
			if (isQuickfixType("") && searchItem != null) {
				searchItem.dispose();
				searchItem = null;
			}
			
			else if (searchItem == null) {
				searchItem = new SearchItem(parent, toolkit, contentHelper, modelQuery, model, uiDelegateFactory, context);
				FormData data = (FormData)searchItem.getLayoutData();
				data.left = new FormAttachment(5);
				searchItem.setPreviousContainer(this);
			}
			
			if (isQuickfixType(QUICKFIX_TYPE.REPLACE.name()) || isQuickfixType(QUICKFIX_TYPE.INSERT_LINE.name())) {
				if (textItem == null) {
					textItem = new TextItem(searchItem.getContainer());
				}
			}
			
			else if (textItem != null) {
				textItem.dispose();
				textItem = null;
			}
			
			return searchItem != null ? searchItem : this;
		}
		
		private Element findChildWithNodeName(String nodeName) {
			for (Element child : collectChildElements()) {
				if (Objects.equal(child.getNodeName(), nodeName)) {
					return child;
				}
			}
			return null;
		}
		
		private Element findSearchElement() {
			return findChildWithNodeName(RulesetElementUiDelegateFactory.RulesetConstants.SEARCH);
		}
		
		private Element findReplacementElement() {
			return findChildWithNodeName(RulesetElementUiDelegateFactory.RulesetConstants.REPLACEMENT);
		}
		
		private Element findNewlineElement() {
			return findChildWithNodeName(RulesetElementUiDelegateFactory.RulesetConstants.NEWLINE); 
		}
		
		private Element findTextElement() {
			if (isQuickfixType(QUICKFIX_TYPE.REPLACE.name())) {
				return findReplacementElement();
			}
			if (isQuickfixType(QUICKFIX_TYPE.INSERT_LINE.name())) {
				return findNewlineElement();
			}
			return null;
		}
		
		@Override
		public void bind() {
			super.bind();
			blockNotification = true;
			if (searchItem != null) {
				searchItem.bind();
			}
			if (textItem != null) {
				textItem.bind();
			}
			blockNotification = false;
		}
		
		private boolean isQuickfixType(String name) {
			return Objects.equal(itemElement.getAttribute(RulesetElementUiDelegateFactory.RulesetConstants.QUICKFIX_TYPE), name);
		}
		
		private Element getQuickfixElement() {
			return itemElement;
		}
		
		protected GridData createGridData(int span) {
			GridData gd = new GridData(span == 2 ? GridData.FILL_HORIZONTAL : GridData.HORIZONTAL_ALIGN_FILL);
			gd.widthHint = 20;
			gd.horizontalSpan = span - 1;
			gd.horizontalIndent = FormLayoutFactory.CONTROL_HORIZONTAL_INDENT;
			return gd;
		}
		
		private void clear() {
			try {
				model.aboutToChangeModel();
				Element searchElement = findSearchElement();
	    			if (searchElement != null) {
	    				getQuickfixElement().removeChild(searchElement);
	    			}
				Element textElement = findTextElement();
				if (textElement != null) {
					getQuickfixElement().removeChild(textElement);
				}
			}
			finally {
				model.changedModel();
			}
		}
		
		private class SearchItem extends ListItem {
			
			private Text text;
			private Composite container;
			
			public SearchItem(Composite parent, FormToolkit toolkit, TreeContentHelper contentHelper, ModelQuery modelQuery, IStructuredModel model,
					RulesetElementUiDelegateFactory uiDelegateFactory, IEclipseContext context) {
				super(parent, toolkit, null, contentHelper, modelQuery, model, uiDelegateFactory, context);
			}
			
			public Composite getContainer() {
				return container;
			}
			
			@Override
			public void bind() {
				if (!Objects.equal(text.getText(), getSearchValue())) {
					text.setText(getSearchValue());
				}
			}
			
			private String getSearchValue() {
				Element searchElement = findSearchElement();
				String result = ""; //$NON-NLS-1$
				if (searchElement != null) {
					result = contentHelper.getNodeValue(searchElement);
				}
				return result != null ? result : ""; //$NON-NLS-1$
			}
			
			protected void createControls() {				
				Pair<Composite, Composite> containers = createListItemContainers(this);
				
				Composite left = containers.getFirst();
				Composite right = containers.getSecond();
				
				createToolbar(right, this);
				
				Label label = toolkit.createLabel(left, RulesetElementUiDelegateFactory.RulesetConstants.SEARCH + ":", SWT.NULL); //$NON-NLS-1$
				label.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
				
				this.text = toolkit.createText(left, "", SWT.SINGLE); //$NON-NLS-1$
				text.setLayoutData(createGridData(2));

				text.addModifyListener(new ModifyListener() {
					@Override
					public void modifyText(ModifyEvent e) {
						if (!blockNotification) {
							try {
								model.aboutToChangeModel();
								Element searchElement = findSearchElement();
								if (searchElement == null) {
									searchElement = getQuickfixElement().getOwnerDocument().createElement(RulesetElementUiDelegateFactory.RulesetConstants.SEARCH);
									getQuickfixElement().appendChild(searchElement);
								}
								contentHelper.setNodeValue(searchElement, text.getText());
							}
							finally {
								model.changedModel();
							}
						}
					}
				});
				
				this.container = left;
			}
			
			protected Control createToolbar(Composite parent, ListItem thisItem) {
				ToolBar toolbar = new ToolBar(parent, SWT.FLAT|SWT.VERTICAL);
				ToolItem deleteLinkItem = new ToolItem(toolbar, SWT.PUSH);
				deleteLinkItem.setToolTipText(RuleMessages.clearText);
				deleteLinkItem.setImage(WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_CLEAR));
				deleteLinkItem.addSelectionListener(new SelectionAdapter() {
			    		@Override
			    		public void widgetSelected(SelectionEvent e) {
			    			clear();
			    		}
				});
			    GridLayoutFactory.fillDefaults().applyTo(toolbar);
			    return toolbar;
			}
		}
		
		private class TextItem {
			
			private Text text;
			private Label label;
			
			public TextItem(Composite parent) {
				createControls(parent);
			}
			
			public void dispose() {
				text.dispose();
				label.dispose();
			}
			
			public void bind() {
				if (isQuickfixType(QUICKFIX_TYPE.REPLACE.name())) {
					this.label.setText(RulesetElementUiDelegateFactory.RulesetConstants.REPLACEMENT  + ":"); //$NON-NLS-1$
				}
				else if (isQuickfixType(QUICKFIX_TYPE.INSERT_LINE.name())) {
					this.label.setText(RulesetElementUiDelegateFactory.RulesetConstants.NEWLINE  + ":"); //$NON-NLS-1$
				}
				if (!Objects.equal(text.getText(), getTextElementValue())) {
					text.setText(getTextElementValue());
				}
			}
			
			private String getTextElementValue() {
				Element textElement = findTextElement();
				String result = ""; //$NON-NLS-1$
				if (textElement != null) {
					result = textElement.getTextContent();
				}
				return result != null ? result : ""; //$NON-NLS-1$
			}
			
			protected void createControls(Composite parent) {
				
				this.label = toolkit.createLabel(parent, "", SWT.NULL); //$NON-NLS-1$
				label.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
				
				this.text = toolkit.createText(parent, "", SWT.SINGLE); //$NON-NLS-1$
				text.setLayoutData(createGridData(2));

				text.addModifyListener(new ModifyListener() {
					@Override
					public void modifyText(ModifyEvent e) {
						if (!blockNotification) {
							try {
								model.aboutToChangeModel();
								Element textElement = findTextElement();
								Element theSearchElement = findSearchElement();
								List<Element> validChildren = Lists.newArrayList();

								if (textElement == null) {
									if (isQuickfixType(QUICKFIX_TYPE.REPLACE.name())) {
										textElement = getQuickfixElement().getOwnerDocument().createElement(RulesetElementUiDelegateFactory.RulesetConstants.REPLACEMENT);
									}
									else if (isQuickfixType(QUICKFIX_TYPE.INSERT_LINE.name())) {
										textElement = getQuickfixElement().getOwnerDocument().createElement(RulesetElementUiDelegateFactory.RulesetConstants.NEWLINE);
									}
									if (textElement != null) {
										getQuickfixElement().appendChild(textElement);
									}
								}
								
								if (textElement != null) {
									contentHelper.setNodeValue(textElement, text.getText());
									validChildren.add(textElement);
									if (theSearchElement != null) {
										validChildren.add(theSearchElement);
										getQuickfixElement().insertBefore(textElement, theSearchElement);
									}
								}
								
								List<Element> currentChildren = collectChildElements();
								currentChildren.removeAll(validChildren);
								for (Element child : currentChildren) {
									getQuickfixElement().removeChild(child);
								}
							}
							finally {
								model.changedModel();
							}
						}
					}
				});
			}
		}
	}
}
