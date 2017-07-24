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

import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.pde.internal.ui.editor.FormLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wst.xml.core.internal.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.editor.ConfigurationBlock.ToolbarContainer;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.ChoiceAttributeRow;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.HINT_EFFORT;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.NodeRow;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.ReferenceNodeRow;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.RulesetConstants;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.TextNodeRow;
import org.jboss.tools.windup.ui.internal.rules.ElementUiDelegate;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

@SuppressWarnings("restriction")
public class HintDelegate extends ElementUiDelegate {
	
	@Override
	protected void createTabs() {
		addTab(DetailsTab.class);
		addTab(LinksTab.class);
		addTab(MessageTab.class);
	}
	
	public static class DetailsTab extends ElementAttributesContainer {
		
		private CheckboxTreeViewer tagsTreeViewer;
		
		private ChoiceAttributeRow createEffortRow(Node node, CMNode cmNode) {
			return new ChoiceAttributeRow(element, node, cmNode, true) {
				@Override
				protected List<String> getOptions() {
					return Arrays.stream(HINT_EFFORT.values()).map(e -> computeUiValue(e)).
							collect(Collectors.toList());
				}
				@Override
				protected String modelToDisplayValue(String modelValue) {
					if (modelValue == null || modelValue.isEmpty()) {
						return "";
					}
					
					int effort;
					
					try {
						effort = Integer.valueOf(modelValue);
					} catch (Exception e) {
						return "";
					}
					
					Optional<HINT_EFFORT> hintEffort = Arrays.stream(HINT_EFFORT.values()).filter(e -> {
						return Objects.equal(e.getEffort(), effort);
					}).findFirst();
					
					if(hintEffort.isPresent()) {
						return computeUiValue(hintEffort.get());
					}

					return "";
				}
				
				@Override
				protected String displayToModelValue(String uiValue) {
					if (uiValue.isEmpty()) {
						return "";
					}
					
					Optional<HINT_EFFORT> hintEffort = Arrays.stream(HINT_EFFORT.values()).filter(e -> {
						return Objects.equal(uiValue, computeUiValue(e));
					}).findFirst(); 
					
					if (hintEffort.isPresent()) {
						return String.valueOf(hintEffort.get().getEffort());
					}
					return "";
				}
				
				private String computeUiValue(HINT_EFFORT effort) {
					return effort.getLabel() + " - " + effort.getDescription();
				}
				
				@Override
				public String getHoverContent(Control c) {
					return Messages.RulesetEditor_hintEffortTooltip;
				}
			};
		}
		
		@PostConstruct
		@SuppressWarnings("unchecked")
		public void createControls(Composite parent, CTabItem item) {
			item.setText(Messages.ruleElementDetails);
			Composite client = super.createSection(parent, 2);
			CMElementDeclaration ed = modelQuery.getCMElementDeclaration(element);
			if (ed != null) {
				List<CMAttributeDeclaration> availableAttributeList = modelQuery.getAvailableContent(element, ed, ModelQuery.INCLUDE_ATTRIBUTES);
			    for (CMAttributeDeclaration declaration : availableAttributeList) {
			    		Node node = findNode(element, ed, declaration);
				    	if (Objects.equal(declaration.getAttrName(), RulesetConstants.EFFORT)) {
				    		ChoiceAttributeRow row = createEffortRow(node, declaration);
				    		rows.add(row);
				    		row.createContents(client, toolkit, 2);
				    	}
				    	else {
				    		rows.add(ElementAttributesContainer.createTextAttributeRow(element, toolkit, node, declaration, client, 2));
				    	}
			    }
			}
			createTagsSection(parent);
		}
		
		private Section createTagsSection(Composite parent) {
			Section section = createSection(parent, Messages.RulesetEditor_tagsSection, Section.DESCRIPTION|ExpandableComposite.TITLE_BAR|Section.NO_TITLE_FOCUS_BOX);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(section);
			section.setDescription(NLS.bind(Messages.RulesetEditor_tagsSectionDescription, RulesetConstants.HINT_NAME));
			tagsTreeViewer = new CheckboxTreeViewer(toolkit.createTree((Composite)section.getClient(), SWT.CHECK));
			tagsTreeViewer.setContentProvider(new TreeContentProvider());
			tagsTreeViewer.setLabelProvider(new LabelProvider() {
				@Override
				public String getText(Object element) {
					return ((Node)element).getTextContent();
				}
				@Override
				public Image getImage(Object element) {
					return WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_TAG);
				}
			});
			tagsTreeViewer.setAutoExpandLevel(0);
			
			Tree tree = tagsTreeViewer.getTree();
			GridDataFactory.fillDefaults().grab(true, true).hint(SWT.DEFAULT, 50).applyTo(tree);
			
			tagsTreeViewer.addCheckStateListener(new ICheckStateListener() {
				@Override
				public void checkStateChanged(CheckStateChangedEvent event) {
					Node tagNode = (Node)event.getElement();
					if (event.getChecked()) {
						CMElementDeclaration tagCmNode = getTagCmNode();
						AddNodeAction action = (AddNodeAction)ElementUiDelegate.createAddElementAction(
								model, element, tagCmNode, element.getChildNodes().getLength(), null);
						action.run();
						List<Node> result = action.getResult();
						if (!result.isEmpty()) {
							Element ruleElement = (Element)result.get(0);
							contentHelper.setNodeValue(ruleElement, contentHelper.getNodeValue(tagNode));
						}
					}
					else {
						new DeleteNodeAction(model, tagNode).run();
						tagsTreeViewer.setSelection(new StructuredSelection(tagNode), true);
					}
				}
			});
			loadTags();
			section.setExpanded(true);
			createSectionToolbar(section);
			return section;
		}
		
		private void createSectionToolbar(Section section) {
			ToolBar toolbar = new ToolBar(section, SWT.FLAT|SWT.HORIZONTAL);
			ToolItem addItem = new ToolItem(toolbar, SWT.PUSH);
			addItem.setImage(WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_ADD));
			addItem.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					CMElementDeclaration linkCmNode = getTagCmNode();
					AddNodeAction action = (AddNodeAction)ElementUiDelegate.createAddElementAction(
							model, element, linkCmNode, element.getChildNodes().getLength(), null);
					action.run();
				}
			});
			section.setTextClient(toolbar);
		}
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private CMElementDeclaration getTagCmNode() {
			List candidates = modelQuery.getAvailableContent(element, elementDeclaration, 
					ModelQuery.VALIDITY_STRICT);
			Optional<CMElementDeclaration> found = candidates.stream().filter(candidate -> {
				if (candidate instanceof CMElementDeclaration) {
					return RulesetConstants.TAG_NAME.equals(((CMElementDeclaration)candidate).getElementName());
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
			loadTags();
		}
		
		@SuppressWarnings("deprecation")
		private void loadTags() {
			Map<String, Node> otherTagsMap = Maps.newLinkedHashMap();
			NodeList list = element.getOwnerDocument().getElementsByTagName(RulesetConstants.TAG_NAME);
			for (int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);
				otherTagsMap.put(contentHelper.getNodeValue(node), node);
			}
			
			List<Node> localTags = Lists.newLinkedList();
			NodeList tagList = element.getElementsByTagName(RulesetConstants.TAG_NAME);
			for (int i = 0; i < tagList.getLength(); i++) {
				Node node = tagList.item(i);
				localTags.add(node);
				if (otherTagsMap.containsKey(contentHelper.getNodeValue(node))) {
					otherTagsMap.replace(contentHelper.getNodeValue(node), node);
				}
			}
			
			Set<Node> tags = Sets.newLinkedHashSet();
			tags.addAll(otherTagsMap.values());
			tags.addAll(localTags);
			
			tagsTreeViewer.setAllChecked(false);
			tagsTreeViewer.setInput(tags.toArray());
			
			for (Node localTag : localTags) {
				tagsTreeViewer.setChecked(localTag, true);
			}
		}
		
	}
	
	public static class LinksTab extends ElementAttributesContainer {
		
		private static final int MIN_WIDTH = 350;
		
		private LinkContainerManager linkManager = new LinkContainerManager();
		private Composite parentControl;
		ScrolledComposite scroll;
 		
		@PostConstruct
		public void createControls(Composite parent, CTabItem item) {
			item.setText(Messages.linksTab);
			Section section = super.createScrolledSection(parent, 2);
			section.setText(Messages.linksTab);
			section.setDescription(Messages.RulesetEditor_linksSectionDescription);
			this.scroll = (ScrolledComposite)section.getClient();
			Composite client = toolkit.createComposite(scroll);
			GridLayout glayout = FormLayoutFactory.createSectionClientGridLayout(false, /*span*/ 2);
			client.setLayout(glayout);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(client);
			scroll.setContent(client);
			toolkit.paintBordersFor(client);
			this.parentControl = client;
			client.setLayout(new FormLayout());
			linkManager.createControls(client, collectLinks());
			ConfigurationBlock.addToolbarListener(client);
			createSectionToolbar(section);
		}
		
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
			scroll.setMinHeight(linkManager.computeHeight());
			int width = linkManager.getLinkContainerCount() > 0 ? MIN_WIDTH : 0;
			scroll.setMinWidth(width);
			parentControl.getParent().getParent().getParent().layout(true, true);
		}
		
		private void loadLinks() {
			linkManager.createControls(parentControl, collectLinks());
			linkManager.bind();
		}
		
		private List<Element> collectLinks() {
			List<Element> links = Lists.newArrayList();
			NodeList list = element.getElementsByTagName(RulesetConstants.LINK_NAME);
			for (int i = 0; i < list.getLength(); i++) {
				links.add((Element)list.item(i));
			}
			return links;
		}
		
		private class LinkContainerManager {
			private List<LinkContainer> containers = Lists.newArrayList();
			
			public void createControls(Composite parent, List<Element> links) {
				for (Iterator<LinkContainer> iter = containers.iterator(); iter.hasNext();) {
					LinkContainer container = iter.next();
					if (!links.contains(container.getLinkElement())) {
						container.dispose();
						iter.remove();
					}
				}
				LinkContainer previousContainer = null;
				for (Element linkElement : links) {
					
					LinkContainer linkContainer = findContainer(linkElement);
					
					if (linkContainer == null) {
						linkContainer = new LinkContainer(parent, linkElement, this);
						containers.add(linkContainer);
					}
					
					if (previousContainer != null) {
						linkContainer.setPreviousContainer(previousContainer);
					}
					
					previousContainer = linkContainer;
				}
			}
			
			public int computeHeight() {
				int height = 0;
				for (LinkContainer container : containers) {
					height += container.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
				}
				return height;
			}
			
			public void bind() {
				containers.forEach(container -> container.bind());
			}
			
			public int getLinkContainerCount() {
				return containers.size();
			}
			
			private LinkContainer findContainer(Element element) {
				Optional<LinkContainer> option = containers.stream().filter(container -> container.isContainerFor(element)).findFirst();
				if (option.isPresent()) {
					return option.get();
				}
				return null;
			}
			
			private LinkContainer findNextContainer(LinkContainer container) {
				int index = containers.indexOf(container);
				if (index != -1 && index != containers.size()-1) {
					return containers.get(index+1);
				}
				return null;
			}
		}
		
		private class LinkContainer extends Composite {
			
			private Element linkElement;
			protected List<NodeRow> rows = Lists.newArrayList();
			private ToolbarContainer toolbarContainer;
			private LinkContainerManager containerManager;
			
			public LinkContainer(Composite parent, Element linkElement, LinkContainerManager containerManager) {
				super(parent, SWT.NONE);
				GridLayoutFactory.fillDefaults().numColumns(1).applyTo(this);
				FormData data = new FormData();
				data.left = new FormAttachment(0);
				data.right = new FormAttachment(100);
				setLayoutData(data);
				this.linkElement = linkElement;
				this.containerManager = containerManager;
				this.setData(ConfigurationBlock.TOOLBAR_CONTROL, toolbarContainer);
				createControls();
			}
			
			public void setPreviousContainer(LinkContainer previousContainer) {
				FormData data = (FormData)getLayoutData();
				data.top = new FormAttachment(previousContainer);
			}
			
			public void bind() {
				rows.forEach(row -> row.bind());
			}
			
			@SuppressWarnings("unchecked")
			private void createControls() {
				
				CMElementDeclaration ed = modelQuery.getCMElementDeclaration(linkElement);
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
					
					List<CMAttributeDeclaration> availableAttributeList = modelQuery.getAvailableContent(linkElement, ed, ModelQuery.INCLUDE_ATTRIBUTES);
					
				    for (CMAttributeDeclaration declaration : availableAttributeList) {
				    		Node node = ElementUiDelegate.findNode(linkElement, ed, declaration);
				    	  	if (Objects.equal(declaration.getAttrName(), RulesetConstants.LINK_HREF)) {
							ReferenceNodeRow row = new ReferenceNodeRow(element, node, declaration) {
								@Override
								protected void openReference() {
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
				    	  		TextNodeRow row = ElementAttributesContainer.createTextAttributeRow(linkElement, toolkit, node, declaration, left, 2);
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
				deleteLinkItem.addSelectionListener(new SelectionAdapter() {
			    		@Override
			    		public void widgetSelected(SelectionEvent e) {
			    			LinkContainer nextLinkContainer = containerManager.findNextContainer(LinkContainer.this);
			    			if (nextLinkContainer != null) {
			    				FormData nextData = (FormData)nextLinkContainer.getLayoutData();
			    				FormData thisData = (FormData)(LinkContainer.this.getLayoutData());
			    				if (thisData.top != null && thisData.top.control != null && !thisData.top.control.isDisposed()) {
			    					nextData.top = new FormAttachment(thisData.top.control);
			    				}
			    				else {
			    					nextData.top = new FormAttachment(null);
			    				}
			    				parentControl.layout(true);
			    				nextLinkContainer.toolbarContainer.update(true, true);
			    				parentControl.setData(ConfigurationBlock.TOOLBAR_CONTROL, nextLinkContainer.toolbarContainer);
			    			}
			    			Display.getDefault().asyncExec(() -> {
			    				DeleteNodeAction deleteAction = new DeleteNodeAction(model, Lists.newArrayList(linkElement));
				    			deleteAction.run();
			    			});
			    		}
				});
			    GridLayoutFactory.fillDefaults().applyTo(toolbar);
			    return toolbar;
			}
			
			public boolean isContainerFor(Element element) {
				return Objects.equal(this.linkElement, element);
			}
			
			public Element getLinkElement() {
				return linkElement;
			}
		}
	}
	
	protected static TextNodeRow createTextAttributeRow(Node parentNode, FormToolkit toolkit, Node node, CMNode cmNode, Composite parent, int columns) {
		TextNodeRow row = new TextNodeRow(parentNode, node, cmNode);
		row.createContents(parent, toolkit, columns);
		return row;
	}
	
	public static class MessageTab extends ElementAttributesContainer {
		
		@PostConstruct
		public void createControls(Composite parent, CTabItem item) {
			item.setText(Messages.messageTab);
			/*Composite client = super.createSection(parent, 2);
			CMElementDeclaration ed = modelQuery.getCMElementDeclaration(element);
			if (ed != null) {
				List<CMAttributeDeclaration> availableAttributeList = modelQuery.getAvailableContent(element, ed, ModelQuery.INCLUDE_ATTRIBUTES);
			    for (CMAttributeDeclaration declaration : availableAttributeList) {
			    		Node node = findNode(element, ed, declaration);
				    	if (Objects.equal(declaration.getAttrName(), RulesetConstants.EFFORT)) {
				    		ChoiceAttributeRow row = createEffortRow(node, declaration);
				    		rows.add(row);
				    		row.createContents(client, toolkit, 2);
				    	}
				    	else {
				    		rows.add(ElementAttributesContainer.createTextAttributeRow(element, toolkit, node, declaration, client, 2));
				    	}
			    }
			}*/
		}
	}
	
	/*
	@Override
	protected Composite createClient() {
		Composite client = super.createClient();
		
		createTagsSection();
		createLinksSection();
		createMessageSection();
		
		return client;
	}*/
	
	@Override
	public Object[] getChildren() {
		Object[] result = super.getChildren();
		if (result != null) {
			result = Arrays.stream(result).filter(n -> {
				if (n instanceof Node) {
					return !Objects.equal(((Node)n).getNodeName(), RulesetConstants.TAG_NAME) &&
								!Objects.equal(((Node)n).getNodeName(), RulesetConstants.LINK_NAME) &&
								 	!Objects.equal(((Node)n).getNodeName(), RulesetConstants.MESSAGE);
				}
				return true;
			}).collect(Collectors.toList()).toArray();
		}
		return result;
	}
	
	/*
	private Section createTagsSection() {
		Section section = ElementAttributesContainer.createSection(parent, toolkit, Messages.RulesetEditor_tagsSection, Section.DESCRIPTION|ExpandableComposite.TITLE_BAR|Section.TWISTIE|Section.NO_TITLE_FOCUS_BOX);
		section.setDescription(NLS.bind(Messages.RulesetEditor_tagsSectionDescription, RulesetConstants.HINT_NAME));
		section.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(true);
			}
		});
		
		tagsTreeViewer = new CheckboxTreeViewer(toolkit.createTree((Composite)section.getClient(), SWT.CHECK));
		tagsTreeViewer.setContentProvider(new TreeContentProvider());
		tagsTreeViewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				return ((Node)element).getTextContent();
			}
			@Override
			public Image getImage(Object element) {
				return WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_TAG);
			}
		});
		tagsTreeViewer.setAutoExpandLevel(0);
		
		Tree tree = tagsTreeViewer.getTree();
		GridDataFactory.fillDefaults().grab(true, true).hint(SWT.DEFAULT, 300).applyTo(tree);
		
		loadTags();
		return section;
	}
	
	@Override
	public void update() {
		super.update();
		loadTags();
	}
	
	private void loadTags() {
		List<Node> tags = Lists.newArrayList();
		NodeList list = element.getOwnerDocument().getElementsByTagName(RulesetConstants.TAG_NAME);
		for (int i = 0; i < list.getLength(); i++) {
			tags.add(list.item(i));
		}
		tagsTreeViewer.setInput(tags.toArray());
	}
	
	private Section createLinksSection() {
		Section section = ElementAttributesContainer.createSection(parent, toolkit, Messages.RulesetEditor_linksSection, Section.DESCRIPTION|ExpandableComposite.TITLE_BAR|Section.TWISTIE|Section.NO_TITLE_FOCUS_BOX);
		section.setDescription(Messages.RulesetEditor_linksSectionDescription);
		section.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(true);
			}
		});

		linksTreeViewer = new CheckboxTreeViewer(toolkit.createTree((Composite)section.getClient(), SWT.CHECK));
		linksTreeViewer.setContentProvider(new TreeContentProvider());
		linksTreeViewer.setLabelProvider(new WorkbenchLabelProvider());
		linksTreeViewer.setAutoExpandLevel(0);
		
		Tree tree = linksTreeViewer.getTree();
		GridDataFactory.fillDefaults().grab(true, true).hint(SWT.DEFAULT, 150).applyTo(tree);

		return section;
	}
	
	private Section createMessageSection() {
		Section section = ElementAttributesContainer.createSection(parent, toolkit, Messages.RulesetEditor_messageSection, Section.DESCRIPTION|ExpandableComposite.TITLE_BAR|Section.TWISTIE|Section.NO_TITLE_FOCUS_BOX);
		section.setDescription(Messages.RulesetEditor_messageSectionDescription);
		section.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(true);
			}
		});
		return section;
	}
	*/
}