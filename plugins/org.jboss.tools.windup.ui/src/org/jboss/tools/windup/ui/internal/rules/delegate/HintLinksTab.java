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
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.xtext.util.Pair;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.RuleMessages;
import org.jboss.tools.windup.ui.internal.editor.AddNodeAction;
import org.jboss.tools.windup.ui.internal.editor.ElementAttributesContainer;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.RulesetConstants;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.google.common.collect.Lists;

@SuppressWarnings({"restriction"})
public class HintLinksTab extends ElementAttributesContainer {
	
	private static final int MIN_WIDTH = 350;
	private ListContainer listContainer;
	private Composite parentControl;
	private ScrolledComposite scroll;
		
	@PostConstruct
	public void createControls(Composite parent, CTabItem item) {
		item.setText(RuleMessages.link_title);
		Pair<Section, Composite> result = ElementAttributesContainer.createScrolledSection(toolkit, parent, RuleMessages.link_title, RuleMessages.link_description,
				ExpandableComposite.TITLE_BAR | Section.DESCRIPTION | Section.NO_TITLE_FOCUS_BOX);
		Section section = result.getFirst();
		Composite client = result.getSecond();
		this.scroll = (ScrolledComposite)section.getClient();
		this.parentControl = client;
		this.listContainer =  new ListContainer(toolkit, contentHelper, modelQuery, model, uiDelegateFactory, context);
		listContainer.createControls(client, collectLinks());
		ConfigurationBlock.addToolbarListener(client);
		createSectionToolbar(section);
	}
	
	/*private ListContainer createListContainer() {
		ListContainer listContainer = new ListContainer() {
			@Override
			protected ListItem createListItem(Composite parent, Element listElement) {
				ListItem item = new ListItem(parent, listElement) {
					@SuppressWarnings("unchecked")
					@Override
					public void createControls() {
						CMElementDeclaration ed = modelQuery.getCMElementDeclaration(itemElement);
						if (ed != null) {
							Group group = new Group(this, SWT.SHADOW_IN);
							group.setBackground(toolkit.getColors().getBackground());
							group.setForeground(toolkit.getColors().getBackground());
							GridDataFactory.fillDefaults().grab(true, false).applyTo(group);
							group.setLayout(new FormLayout());
							
							Composite left = new Composite(group, SWT.NONE);
							GridLayoutFactory.fillDefaults().numColumns(2).applyTo(left);
							FormData data = new FormData();
							data.left = new FormAttachment(0);
							data.right = new FormAttachment(100);
							left.setLayoutData(data);
							
							Composite right = new Composite(group, SWT.NONE);
							GridLayoutFactory.fillDefaults().numColumns(1).applyTo(right);
							data = new FormData();
							data.right = new FormAttachment(100);
							data.bottom = new FormAttachment(73);
							right.setLayoutData(data);
							right.setVisible(false);
							
							createToolbar(right);
							
							this.toolbarContainer = new ToolbarContainer(group, left, right);
							
							List<CMAttributeDeclaration> availableAttributeList = modelQuery.getAvailableContent(itemElement, ed, ModelQuery.INCLUDE_ATTRIBUTES);
							
						    for (CMAttributeDeclaration declaration : availableAttributeList) {
						    	  	if (Objects.equal(declaration.getAttrName(), RulesetConstants.LINK_HREF)) {
									ReferenceNodeRow row = new ReferenceNodeRow(element, declaration) {
										@Override
										protected Node getNode() {
											return ElementUiDelegate.findNode(itemElement, ed, declaration);
										}

										@Override
										protected void openReference() {
											Node node = getNode();
											if (node != null && !node.getNodeValue().isEmpty()) {
												try {
													PlatformUI.getWorkbench().getBrowserSupport()
															.createBrowser(WindupUIPlugin.PLUGIN_ID)
															.openURL(new URL(node.getNodeValue()));
												} catch (Exception e) {
													WindupUIPlugin.log(e);
												}
											}
										}
									};
						    	  		rows.add(row);
									row.createContents(left, toolkit, 2);
									addToolbar(group, left, right, row);
						    	  	}
						    	  	else {
						    	  		TextNodeRow row = ElementAttributesContainer.createTextAttributeRow(itemElement, toolkit, declaration, left, 2);
						    	  		rows.add(row);
						    	  		addToolbar(group, left, right, row);
						    	  	}
						    }
						}
					}
					
					private void addToolbar(Group group, Composite left, Composite right, TextNodeRow row) {
						group.setData(ConfigurationBlock.TOOLBAR_CONTROL, toolbarContainer);
						left.setData(ConfigurationBlock.TOOLBAR_CONTROL, toolbarContainer);
						right.setData(ConfigurationBlock.TOOLBAR_CONTROL, toolbarContainer);
						row.getLabelControl().setData(ConfigurationBlock.TOOLBAR_CONTROL, toolbarContainer);
						row.getTextControl().setData(ConfigurationBlock.TOOLBAR_CONTROL, toolbarContainer);
					}
					
					private Control createToolbar(Composite parent) {
						ToolBar toolbar = new ToolBar(parent, SWT.FLAT|SWT.VERTICAL);
						ToolItem deleteLinkItem = new ToolItem(toolbar, SWT.PUSH);
						deleteLinkItem.setToolTipText(Messages.RulesetEditor_remove);
						deleteLinkItem.setImage(WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_GARBAGE));
						//deleteLinkItem.getControl().setCursor(parent.getShell().getDisplay().getSystemCursor(SWT.CURSOR_HAND));
						ListItem thisItem = this;
						deleteLinkItem.addSelectionListener(new SelectionAdapter() {
					    		@Override
					    		public void widgetSelected(SelectionEvent e) {
					    			ListItem nextLinkContainer = findNextListItem(thisItem);
					    			if (nextLinkContainer != null) {
					    				FormData nextData = (FormData)nextLinkContainer.getLayoutData();
					    				FormData thisData = (FormData)(thisItem.getLayoutData());
					    				if (thisData.top != null && thisData.top.control != null && !thisData.top.control.isDisposed()) {
					    					nextData.top = new FormAttachment(thisData.top.control);
					    				}
					    				else {
					    					nextData.top = new FormAttachment(null);
					    				}
					    				parentControl.layout(true);
					    				nextLinkContainer.getToolbarContainer().update(true, true);
					    				parentControl.setData(ConfigurationBlock.TOOLBAR_CONTROL, nextLinkContainer.getToolbarContainer());
					    			}
					    			Display.getDefault().asyncExec(() -> {
					    				DeleteNodeAction deleteAction = new DeleteNodeAction(model, Lists.newArrayList(itemElement));
						    			deleteAction.run();
					    			});
					    		}
						});
					    GridLayoutFactory.fillDefaults().applyTo(toolbar);
					    return toolbar;
					}
				};
				return item;
			}
		};
		return listContainer; 
	}*/
	
	private void createSectionToolbar(Section section) {
		ToolBar toolbar = new ToolBar(section, SWT.FLAT|SWT.HORIZONTAL);
		ToolItem addItem = new ToolItem(toolbar, SWT.PUSH);
		addItem.setImage(WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_ADD));
		addItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				CMElementDeclaration linkCmNode = getLinkCmNode();
				AddNodeAction action = (AddNodeAction)ElementUiDelegate.createAddElementAction(
						model, element, linkCmNode, element.getChildNodes().getLength(), null);
				action.run();
			}
		});
		section.setTextClient(toolbar);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private CMElementDeclaration getLinkCmNode() {
		List candidates = modelQuery.getAvailableContent(element, elementDeclaration, 
				ModelQuery.VALIDITY_STRICT);
		Optional<CMElementDeclaration> found = candidates.stream().filter(candidate -> {
			if (candidate instanceof CMElementDeclaration) {
				return RulesetConstants.LINK_NAME.equals(((CMElementDeclaration)candidate).getElementName());
			}
			return false;
		}).findFirst();
		if (found.isPresent()) {
			return found.get();
		}
		return null;
	}
	
	@Override
	protected void bind() {
		super.bind();
		loadLinks();
		scroll.setMinHeight(listContainer.computeHeight());
		int width = listContainer.getItemCount() > 0 ? MIN_WIDTH : 0;
		scroll.setMinWidth(width);
		parentControl.getParent().getParent().getParent().layout(true, true);
	}
	
	private void loadLinks() {
		listContainer.createControls(parentControl, collectLinks());
		listContainer.bind();
	}
	
	private List<Element> collectLinks() {
		List<Element> links = Lists.newArrayList();
		NodeList list = element.getElementsByTagName(RulesetConstants.LINK_NAME);
		for (int i = 0; i < list.getLength(); i++) {
			links.add((Element)list.item(i));
		}
		return links;
	}
}
