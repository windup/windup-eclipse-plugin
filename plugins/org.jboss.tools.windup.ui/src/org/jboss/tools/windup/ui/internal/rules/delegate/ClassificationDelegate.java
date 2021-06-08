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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.pde.internal.ui.editor.FormLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wst.xml.core.internal.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.ui.internal.tabletree.TreeContentHelper;
import org.eclipse.wst.xml.ui.internal.tabletree.TreeExtension;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.editor.AddNodeAction;
import org.jboss.tools.windup.ui.internal.editor.DeleteNodeAction;
import org.jboss.tools.windup.ui.internal.editor.ElementAttributesContainer;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.ChoiceAttributeRow;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.RulesetConstants;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.TextNodeRow;
import org.jboss.tools.windup.ui.internal.editor.TreeContentProvider;
import org.jboss.tools.windup.ui.internal.rules.delegate.HintDelegate.HINT_EFFORT;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

@SuppressWarnings("restriction")
public class ClassificationDelegate extends ElementUiDelegate {

	@Override
	protected void createTabs() {
		addTab(DetailsTab.class);
	}
	
	public static class DetailsTab extends ElementAttributesContainer {
		
		private static final String TAG_VALUE_COLUMN = "tagValueColumn";
		
		private ClassificationDescriptionTab descriptionTab;
		private HintLinksTab linksTab;
		private QuickfixesDelegate quickfixTab;
		
		private CheckboxTreeViewer tagsTreeViewer;
		
		private Section tagsSection;
		
		private ChoiceAttributeRow createEffortRow(CMNode cmNode) {
			return new ChoiceAttributeRow(element, cmNode, true) {
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
					return effort.getEffort() + " - " + effort.getLabel() + " - " + effort.getDescription();
				}
			};
		}
		
		@PostConstruct
		@SuppressWarnings("unchecked")
		public void createControls(Composite parent) {
			Composite mainDetailsContainer = toolkit.createComposite(parent);
			
			GridLayout layout = new GridLayout();
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			mainDetailsContainer.setLayout(layout);
		
			GridDataFactory.fillDefaults().grab(true, false).applyTo(mainDetailsContainer);
			
			Composite client = super.createSection(mainDetailsContainer, 2, toolkit, element, ExpandableComposite.TITLE_BAR |Section.NO_TITLE_FOCUS_BOX, 
					null, null);
			
			GridLayout glayout = FormLayoutFactory.createSectionClientGridLayout(false, 2);
			glayout.marginTop = 0;
			glayout.marginBottom = 5;
			client.setLayout(glayout);
			
			CMElementDeclaration ed = modelQuery.getCMElementDeclaration(element);
			if (ed != null) {
				List<CMAttributeDeclaration> availableAttributeList = modelQuery.getAvailableContent(element, ed, ModelQuery.INCLUDE_ATTRIBUTES);
			    for (CMAttributeDeclaration declaration : availableAttributeList) {
				    	if (Objects.equal(declaration.getAttrName(), RulesetConstants.EFFORT)) {
				    		ChoiceAttributeRow row = createEffortRow(declaration);
				    		rows.add(row);
				    		row.createContents(client, toolkit, 2);
				    	}
				    else if (Objects.equal(declaration.getAttrName(), RulesetConstants.CATEGORY_ID)) {
				    		ChoiceAttributeRow row = RulesetElementUiDelegateFactory.createCategoryRow(element, declaration);
				    		rows.add(row);
				    		row.createContents(client, toolkit, 2);
				    	}
				    	else {
				    		rows.add(ElementAttributesContainer.createTextAttributeRow(element, toolkit, declaration, client, 2));
				    	}
			    }
			    createSections(parent, mainDetailsContainer);
			}
			((Section)client.getParent()).setExpanded(true);
		}
		
		private void createSections(Composite parent, Composite detailsClient) {
			
			this.descriptionTab = createDescriptionArea(parent);
			
			Composite container = toolkit.createComposite(parent);
			GridLayout layout = new GridLayout();
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			container.setLayout(layout);
			
			GridDataFactory.fillDefaults().grab(true, false).applyTo(container);
			
			createTagsSection(container);
			
			container = toolkit.createComposite(parent);
			layout = new GridLayout();
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			container.setLayout(layout);
			
			GridDataFactory.fillDefaults().grab(true, false).applyTo(container);
			
			linksTab = createLinksSection(container);
			
			quickfixTab = createQuickfixSection(container);
			
			if (tagsTreeViewer.getTree().getItemCount() > 0) {
				tagsSection.setExpanded(true);
			}
			
			linksTab.initExpansion();
		}
		
		private HintLinksTab createLinksSection(Composite parent) {
			IEclipseContext child = context.createChild();
			child.set(Composite.class, parent);
			return ContextInjectionFactory.make(HintLinksTab.class, child);
		}
		
		private QuickfixesDelegate createQuickfixSection(Composite parent) {
			IEclipseContext child = context.createChild();
			child.set(Composite.class, parent);
			return ContextInjectionFactory.make(QuickfixesDelegate.class, child);
		}
		
		private Section createTagsSection(Composite parent) {
			Section section = createSection(parent, Messages.RulesetEditor_tagsSection, ExpandableComposite.TITLE_BAR|Section.TWISTIE|Section.NO_TITLE_FOCUS_BOX,
					NLS.bind(Messages.RulesetEditor_tagsSectionDescription, RulesetConstants.CLASSIFICATION));
			GridDataFactory.fillDefaults().grab(true, true).applyTo(section);
			
			this.tagsSection = section;
			
			Composite client = (Composite)section.getClient();
			((GridLayout)client.getLayout()).marginBottom = 5;
			
			tagsTreeViewer = new CheckboxTreeViewer(toolkit.createTree(client, SWT.CHECK|SWT.SINGLE));
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
			GridDataFactory.fillDefaults().grab(true, true).hint(SWT.DEFAULT, 150).applyTo(tree);
		
			
			tagsTreeViewer.setCellEditors(new CellEditor[] {new TextCellEditor(tagsTreeViewer.getTree())});

			tagsTreeViewer.setCellModifier(new TagCellModifier());
			tagsTreeViewer.setColumnProperties(new String[] {"image", TAG_VALUE_COLUMN}); //$NON-NLS-1$
			
			tagsTreeViewer.addCheckStateListener(new ICheckStateListener() {
				@Override
				public void checkStateChanged(CheckStateChangedEvent event) {
					Node tagNode = (Node)event.getElement();
					if (event.getChecked()) {
						CMElementDeclaration tagCmNode = getTagCmNode();
						AddNodeAction action = (AddNodeAction)ElementUiDelegate.createAddElementAction(
								model, element, tagCmNode, element.getChildNodes().getLength(), null, null);
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
			createSectionToolbar(section);
			return section;
		}
		
		private class TagCellModifier implements ICellModifier, TreeExtension.ICellEditorProvider {
			
			protected TreeContentHelper treeContentHelper = new TreeContentHelper();
			
			public boolean canModify(Object object, String property) {
				Node node = (Node)object;
				if (!Objects.equal(node.getParentNode(), element)) {
					return false;
				}
				return true;
			}

			public Object getValue(Object object, String property) {
				String result = null;
				if (object instanceof Node) {
					result = treeContentHelper.getNodeValue((Node) object);
				}
				return (result != null) ? result : ""; //$NON-NLS-1$
			}

			public void modify(Object element, String property, Object value) {
				Item item = (Item) element;
				String oldValue = treeContentHelper.getNodeValue((Node) item.getData());
				String newValue = value.toString();
				if ((newValue != null) && !newValue.equals(oldValue)) {
					treeContentHelper.setNodeValue((Node) item.getData(), value.toString(), tagsTreeViewer.getControl().getShell());
				}
			}

			@Override
			public CellEditor getCellEditor(Object o, int col) {
				return new TextCellEditor(tagsTreeViewer.getTree());
			}
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
							model, element, linkCmNode, element.getChildNodes().getLength(), null, null);
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
			super.bind();
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
			
			Set<Node> localTags = Sets.newLinkedHashSet();
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
			
			StringBuffer buff = new StringBuffer();
			buff.append(Messages.RulesetEditor_tagsSection);
			buff.append(" ("+tags.size()+")");
			tagsSection.setText(buff.toString());
		}
		
		private ClassificationDescriptionTab createDescriptionArea(Composite parent) {
			IEclipseContext child = context.createChild();
			child.set(Composite.class, parent);
			return ContextInjectionFactory.make(ClassificationDescriptionTab.class, child);
		}
		
		@Override
		public void update() {
			super.update();
			linksTab.update();
			quickfixTab.update();
			descriptionTab.update();
		}
	}
	
	protected static TextNodeRow createTextAttributeRow(Node parentNode, FormToolkit toolkit, CMNode cmNode, Composite parent, int columns) {
		TextNodeRow row = new TextNodeRow(parentNode, cmNode);
		row.createContents(parent, toolkit, columns);
		return row;
	}
	
	@Override
	public Object[] getChildren() {
		return new Object[] {};
	}
}
