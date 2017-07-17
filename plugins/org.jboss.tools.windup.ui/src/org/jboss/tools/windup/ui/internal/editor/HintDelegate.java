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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wst.xml.core.internal.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.ChoiceAttributeRow;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.HINT_EFFORT;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.RulesetConstants;
import org.jboss.tools.windup.ui.internal.rules.ElementUiDelegate;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;

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
			return new ChoiceAttributeRow(element, node, cmNode) {
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
								model, element, tagCmNode, element.getChildNodes().getLength(), tagsTreeViewer);
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
			return section;
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
		public void update() {
			super.update();
			loadTags();
		}
		
		private void loadTags() {
			Map<String, Node> tags = Maps.newLinkedHashMap();
			NodeList list = element.getOwnerDocument().getElementsByTagName(RulesetConstants.TAG_NAME);
			for (int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);
				tags.put(contentHelper.getNodeValue(node), node);
			}
			NodeList tagList = element.getElementsByTagName(RulesetConstants.TAG_NAME);
			for (int i = 0; i < tagList.getLength(); i++) {
				Node node = tagList.item(i);
				tags.put(node.getTextContent(), node);
			}
			tagsTreeViewer.setAllChecked(false);
			tagsTreeViewer.setInput(tags.values().toArray());

			for (int i = 0; i < tagList.getLength(); i++) {
				Node tagNode = tagList.item(i);
				tagNode = tags.get(tagNode.getTextContent());
				tagsTreeViewer.setChecked(tagNode, true);
			}
		}
		
	}
	
	public static class LinksTab extends ElementAttributesContainer {
		
		@PostConstruct
		@SuppressWarnings("unchecked")
		public void createControls(Composite parent, CTabItem item) {
			item.setText(Messages.linksTab);
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
	
	public static class MessageTab extends ElementAttributesContainer {
		
		@PostConstruct
		@SuppressWarnings("unchecked")
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