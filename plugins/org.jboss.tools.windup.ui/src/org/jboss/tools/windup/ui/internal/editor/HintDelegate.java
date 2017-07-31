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

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.bindings.TriggerSequence;
import org.eclipse.jface.bindings.keys.KeySequence;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.URLHyperlink;
import org.eclipse.jface.text.source.AnnotationModel;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.IAnnotationAccess;
import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.ISharedTextColors;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.OverviewRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.mylyn.internal.wikitext.ui.WikiTextUiPlugin;
import org.eclipse.mylyn.internal.wikitext.ui.editor.MarkupProjectionViewer;
import org.eclipse.mylyn.internal.wikitext.ui.editor.syntax.FastMarkupPartitioner;
import org.eclipse.mylyn.internal.wikitext.ui.editor.syntax.MarkupDocumentProvider;
import org.eclipse.mylyn.wikitext.markdown.MarkdownLanguage;
import org.eclipse.mylyn.wikitext.parser.Attributes;
import org.eclipse.mylyn.wikitext.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.parser.builder.HtmlDocumentBuilder;
import org.eclipse.mylyn.wikitext.parser.markup.AbstractMarkupLanguage;
import org.eclipse.mylyn.wikitext.ui.editor.MarkupSourceViewerConfiguration;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.ProgressAdapter;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.internal.editors.text.EditorsPlugin;
import org.eclipse.ui.keys.IBindingService;
import org.eclipse.ui.texteditor.AnnotationPreference;
import org.eclipse.ui.texteditor.DefaultMarkerAnnotationAccess;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.MarkerAnnotationPreferences;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;
import org.eclipse.wst.xml.core.internal.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.ui.internal.tabletree.TreeContentHelper;
import org.eclipse.wst.xml.ui.internal.tabletree.TreeExtension;
import org.eclipse.xtext.util.Pair;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.editor.ConfigurationBlock.ToolbarContainer;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.ChoiceAttributeRow;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.HINT_EFFORT;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.ReferenceNodeRow;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.RulesetConstants;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.TextNodeRow;
import org.jboss.tools.windup.ui.internal.rules.delegate.ElementUiDelegate;
import org.jboss.tools.windup.ui.internal.rules.delegate.ListContainer;
import org.jboss.tools.windup.ui.internal.rules.delegate.ListItem;
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
		
		private static final String TAG_VALUE_COLUMN = "tagValueColumn";
		
		private CheckboxTreeViewer tagsTreeViewer;
		
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
				    	if (Objects.equal(declaration.getAttrName(), RulesetConstants.EFFORT)) {
				    		ChoiceAttributeRow row = createEffortRow(declaration);
				    		rows.add(row);
				    		row.createContents(client, toolkit, 2);
				    	}
				    	else {
				    		rows.add(ElementAttributesContainer.createTextAttributeRow(element, toolkit, declaration, client, 2));
				    	}
			    }
			}
			createTagsSection(parent);
		}
		
		private Section createTagsSection(Composite parent) {
			Section section = createSection(parent, Messages.RulesetEditor_tagsSection, Section.DESCRIPTION|ExpandableComposite.TITLE_BAR|Section.NO_TITLE_FOCUS_BOX);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(section);
			section.setDescription(NLS.bind(Messages.RulesetEditor_tagsSectionDescription, RulesetConstants.HINT_NAME));
			tagsTreeViewer = new CheckboxTreeViewer(toolkit.createTree((Composite)section.getClient(), SWT.CHECK|SWT.SINGLE));
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
		}
		
	}
	
	public static class LinksTab extends ElementAttributesContainer {
		
		private static final int MIN_WIDTH = 350;
		
		private ListContainer listContainer;
		private Composite parentControl;
		private ScrolledComposite scroll;
 		
		@PostConstruct
		public void createControls(Composite parent, CTabItem item) {
			item.setText(Messages.linksTab);
			Pair<Section, Composite> result = super.createScrolledSection(parent, Messages.linksTab, Messages.RulesetEditor_linksSectionDescription,
					ExpandableComposite.TITLE_BAR | Section.DESCRIPTION | Section.NO_TITLE_FOCUS_BOX);
			Section section = result.getFirst();
			Composite client = result.getSecond();
			this.scroll = (ScrolledComposite)section.getClient();
			this.parentControl = client;
			listContainer = createListContainer();
			listContainer.createControls(client, collectLinks());
			ConfigurationBlock.addToolbarListener(client);
			createSectionToolbar(section);
		}
		
		private ListContainer createListContainer() {
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
	
	protected static TextNodeRow createTextAttributeRow(Node parentNode, FormToolkit toolkit, CMNode cmNode, Composite parent, int columns) {
		TextNodeRow row = new TextNodeRow(parentNode, cmNode);
		row.createContents(parent, toolkit, columns);
		return row;
	}
	
	public static class MessageTab extends ElementAttributesContainer {
		
		protected static final int VERTICAL_RULER_WIDTH = 12;
		
		private static final String CSS_CLASS_EDITOR_PREVIEW = "editorPreview"; //$NON-NLS-1$
		
		private Browser browser;
		private IDocument document;
		private SashForm sash;
		
		private SourceViewer sourceViewer;
		private MarkdownLanguage language = new MarkdownLanguage();
		
		private IHandlerService handlerService;
		private IHandlerActivation contentAssistHandlerActivation;
		
		@PostConstruct
		public void createControls(Composite parent, CTabItem item) {
			item.setText(Messages.messageTab);
			handlerService = PlatformUI.getWorkbench().getService(IHandlerService.class);
			parent = createMessageSection(parent);
			createSash(parent);
			createSourceViewer(sash);
			parent = createPreviewSection(sash);
			createBrowser(parent);
			updatePreview();
		}
		
		private Composite createMessageSection(Composite parent) {
			Section section = createSection(parent, Messages.RulesetEditor_messageSection, Section.DESCRIPTION|ExpandableComposite.TITLE_BAR|Section.NO_TITLE_FOCUS_BOX);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(section);
			section.setDescription(NLS.bind(Messages.RulesetEditor_messageContentAssist, getBinding()));
			return (Composite)section.getClient();
		}
		
		private String getBinding() {
		    final IBindingService bindingSvc= PlatformUI.getWorkbench().getAdapter(IBindingService.class);
			TriggerSequence binding= bindingSvc.getBestActiveBindingFor(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
			if (binding instanceof KeySequence) {
				return binding.format();
			}
			return "";
	    }
		
		private Composite createPreviewSection(Composite parent) {
			Section section = createSection(parent, Messages.RulesetEditor_previewSection, ExpandableComposite.TITLE_BAR|Section.NO_TITLE_FOCUS_BOX);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(section);
			section.setDescription(NLS.bind(Messages.RulesetEditor_messageSectionDescription, RulesetConstants.HINT_NAME));
			return (Composite)section.getClient();
		}
		
		@Override
		protected void bind() {
			String message = getElementMessage();
			if (!Objects.equal(document.get(), message)) {
				document.set(getElementMessage());
			}
			sourceViewer.invalidateTextPresentation();
			updatePreview();
		}
		
		private void createSash(Composite parent) {
			this.sash = new SashForm(parent, SWT.SMOOTH|SWT.HORIZONTAL);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(sash);
			sash.setOrientation(SWT.VERTICAL);
			sash.setFont(parent.getFont());
			sash.setVisible(true);
		}
		
		private void createBrowser(Composite parent) {
			browser = new Browser(parent, SWT.NONE);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(browser);
			browser.addLocationListener(new LocationListener() {
				public void changed(LocationEvent event) {
					event.doit = false;
				}
				public void changing(LocationEvent event) {
					// if it looks like an absolute URL
					if (event.location.matches("([a-zA-Z]{3,8})://?.*")) { //$NON-NLS-1$
						// workaround for browser problem (bug 262043)
						int idxOfSlashHash = event.location.indexOf("/#"); //$NON-NLS-1$
						if (idxOfSlashHash != -1) {
							// allow javascript-based scrolling to work
							if (!event.location.startsWith("file:///#")) { //$NON-NLS-1$
								event.doit = false;
							}
							return;
						}
						// workaround end
						event.doit = false;
						try {
							IWebBrowser webBrowser = PlatformUI.getWorkbench()
									.getBrowserSupport()
									.createBrowser("org.eclipse.ui.browser"); //$NON-NLS-1$
							webBrowser.openURL(new URL(event.location));
						} catch (Exception e) {
							new URLHyperlink(new Region(0, 1), event.location).open();
						}
					}
				}
			});
		}
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private CMElementDeclaration getMessageCmNode() {
			List candidates = modelQuery.getAvailableContent(element, elementDeclaration, 
					ModelQuery.VALIDITY_STRICT);
			Optional<CMElementDeclaration> found = candidates.stream().filter(candidate -> {
				if (candidate instanceof CMElementDeclaration) {
					return RulesetConstants.MESSAGE.equals(((CMElementDeclaration)candidate).getElementName());
				}
				return false;
			}).findFirst();
			if (found.isPresent()) {
				return found.get();
			}
			return null;
		} 
		
		private Node getMessageNode() {
			NodeList list = element.getElementsByTagName(RulesetConstants.MESSAGE);
			if (list.getLength() > 0) {
				return list.item(0);
			}
			return null;
		}
		
		private String getElementMessage() {
			String message = "";
			Element node = (Element)getMessageNode();
			if (node != null) {
				message = getCdataMessage(node);
				if (message == null) {
					message = contentHelper.getNodeValue(node);
				}
			}
			return message;
		}
		
		private String getCdataMessage(Node messageNode) {
			String message = null;
			Node cdataNode = getCdataNode(messageNode);
			if (cdataNode != null) {
				message = contentHelper.getNodeValue(cdataNode);
			}
			return message;
		}

		private Node getCdataNode(Node messageNode) {
			NodeList childList = messageNode.getChildNodes();
			for (int i = 0; i < childList.getLength(); i++) {
				Node child = childList.item(i);
				if (Objects.equal(child.getNodeName(), RulesetConstants.CDATA)) {
					return child;
				}
			}
			return null;
		}
		
		protected void setMessage(String value) {
			Node node = getMessageNode();
			CMNode cmNode = getMessageCmNode();
			try {
				model.aboutToChangeModel();
				if (node != null) {
					Node cdataNode = getCdataNode(node);
					String currentMessage = contentHelper.getNodeValue(node);
					if (currentMessage != null && !currentMessage.isEmpty()) {
						contentHelper.setNodeValue(node, value);
					}
					else if (cdataNode != null) {
						contentHelper.setNodeValue(cdataNode, value);
					}
					else {
						createCdataNode(node, value);
					}
				}
				else {
					AddNodeAction newNodeAction = new AddNodeAction(model, cmNode, element, element.getChildNodes().getLength());
					newNodeAction.runWithoutTransaction();
					List<Node> newNodes = newNodeAction.getResult();
					if (!newNodes.isEmpty()) {
						node = newNodes.get(0);
						contentHelper.setNodeValue(node, "");
						createCdataNode(node, value);
					}
				}
			}
			finally {
				model.changedModel();
			}
		}
		
		private void createCdataNode(Node messageNode, String value) {
			Node cdataNode = messageNode.getOwnerDocument().createCDATASection(value);
			messageNode.appendChild(cdataNode);
		}
		
		protected void createSourceViewer(Composite parent) {
			String string = getElementMessage();
			IStorage storage = new StringInput.StringStorage(string);
			IEditorInput editorInput = new StringInput(storage);
			CompositeRuler ruler = new CompositeRuler();
			ISharedTextColors colors = EditorsPlugin.getDefault().getSharedTextColors();
			int styles = SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.WRAP;
			IOverviewRuler overviewRuler = new OverviewRuler(new DefaultMarkerAnnotationAccess(), VERTICAL_RULER_WIDTH, colors);
			
			this.sourceViewer = new MarkupProjectionViewer(parent, ruler, overviewRuler, true,	styles);
			
			GridDataFactory.fillDefaults().grab(true, false).hint(SWT.DEFAULT, 500).applyTo(sourceViewer.getControl());
			try {
				MarkupDocumentProvider documentProvider = new MarkupDocumentProvider();
				documentProvider.connect(editorInput);
				this.document = documentProvider.getDocument(editorInput);
				//sourceViewer.setDocument(document);
				
				((AbstractMarkupLanguage) language).setEnableMacros(false);
				documentProvider.setMarkupLanguage(language);
				
				MarkupSourceViewerConfiguration sourceViewerConfiguration = new MarkupSourceViewerConfiguration(WikiTextUiPlugin.getDefault().getPreferenceStore());
				sourceViewerConfiguration.initializeDefaultFonts();
				sourceViewerConfiguration.setMarkupLanguage(language);
				sourceViewer.configure(sourceViewerConfiguration);
				
				IDocumentPartitioner partitioner = document.getDocumentPartitioner();
				FastMarkupPartitioner fastMarkupPartitioner = (FastMarkupPartitioner) partitioner;
				fastMarkupPartitioner.setMarkupLanguage(language);
			} catch (CoreException e) {
				WindupUIPlugin.log(e);
			}
			
			sourceViewer.getTextWidget().addFocusListener(new FocusListener() {
				@Override
				public void focusLost(FocusEvent e) {
					deactivateEditorHandlers();
				}
				@Override
				public void focusGained(FocusEvent e) {
					activatEditorHandlers();
				}
			});
			
			IDocumentListener documentListener = new IDocumentListener() {
				public void documentAboutToBeChanged(DocumentEvent event) {}
				public void documentChanged(DocumentEvent event) {
					if (!blockNotification) {
						setMessage(document.get());
						updatePreview();
					}
				}
			};
			
			sourceViewer.getTextWidget().addDisposeListener(new DisposeListener() {
				@Override
				public void widgetDisposed(DisposeEvent e) {
					document.removeDocumentListener(documentListener);
					deactivateEditorHandlers();
				}
			});
			
			document.addDocumentListener(documentListener);
			
			configureAsEditor(sourceViewer, (Document)document);
		}
		
		private void configureAsEditor(ISourceViewer viewer, Document document) {
			IAnnotationAccess annotationAccess = new DefaultMarkerAnnotationAccess();
			final SourceViewerDecorationSupport support = new SourceViewerDecorationSupport(viewer, null, annotationAccess,
					EditorsUI.getSharedTextColors());
			Iterator<?> e = new MarkerAnnotationPreferences().getAnnotationPreferences().iterator();
			while (e.hasNext()) {
				support.setAnnotationPreference((AnnotationPreference) e.next());
			}
			support.install(EditorsUI.getPreferenceStore());
			viewer.getTextWidget().addDisposeListener(new DisposeListener() {
				public void widgetDisposed(DisposeEvent e) {
					support.uninstall();
				}
			});
			AnnotationModel annotationModel = new AnnotationModel();
			viewer.setDocument(document, annotationModel);
		}
		
		private void activatEditorHandlers() {
			contentAssistHandlerActivation = handlerService.activateHandler(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS, 
					new ActionHandler(new AssistAction(sourceViewer)));
		}
		
		private void deactivateEditorHandlers() {
			if (contentAssistHandlerActivation != null) {
				handlerService.deactivateHandler(contentAssistHandlerActivation);
				contentAssistHandlerActivation = null;
			}
		}
		
		private static class AssistAction extends Action {
			private ITextOperationTarget fOperationTarget;

			public AssistAction(SourceViewer sourceViewer) {
				this.fOperationTarget = sourceViewer.getTextOperationTarget();
			}
			
			@Override
			public void run() {
				BusyIndicator.showWhile(Display.getDefault(), new Runnable() {
					@Override
					public void run() {
						fOperationTarget.doOperation(ISourceViewer.CONTENTASSIST_PROPOSALS);
					}
				});
			}
		}
		
		/**
		 * JavaScript that returns the current top scroll position of the browser widget
		 */
		private static final String JAVASCRIPT_GETSCROLLTOP = "function getScrollTop() { " //$NON-NLS-1$
				+ "  if(typeof pageYOffset!='undefined') return pageYOffset;" //$NON-NLS-1$
				+ "  else{" + //$NON-NLS-1$
				"var B=document.body;" + //$NON-NLS-1$
				"var D=document.documentElement;" + //$NON-NLS-1$
				"D=(D.clientHeight)?D:B;return D.scrollTop;}" //$NON-NLS-1$
				+ "}; return getScrollTop();"; //$NON-NLS-1$
		
		private void updatePreview() {
			Object result = browser.evaluate(JAVASCRIPT_GETSCROLLTOP);
			final int verticalScrollbarPos = result != null ? ((Number) result).intValue() : 0;
			String title = file == null ? "" : file.getName(); //$NON-NLS-1$
			if (title.lastIndexOf('.') != -1) {
				title = title.substring(0, title.lastIndexOf('.'));
			}
			StringWriter writer = new StringWriter();
			HtmlDocumentBuilder builder = new HtmlDocumentBuilder(writer) {
				@Override
				protected void emitAnchorHref(String href) {
					if (href != null && href.startsWith("#")) { //$NON-NLS-1$
						writer.writeAttribute("onclick", //$NON-NLS-1$
								String.format("javascript: window.location.hash = '%s'; return false;", href)); //$NON-NLS-1$
						writer.writeAttribute("href", "#"); //$NON-NLS-1$//$NON-NLS-2$
					} else {
						super.emitAnchorHref(href);
					}
				}

				@Override
				public void beginHeading(int level, Attributes attributes) {
					attributes.appendCssClass(CSS_CLASS_EDITOR_PREVIEW);
					super.beginHeading(level, attributes);
				}

				@Override
				public void beginBlock(BlockType type, Attributes attributes) {
					attributes.appendCssClass(CSS_CLASS_EDITOR_PREVIEW);
					super.beginBlock(type, attributes);
				}
			};
			builder.setTitle(title);

			String css = WikiTextUiPlugin.getDefault().getPreferences().getMarkupViewerCss();
			builder.addCssStylesheet(new HtmlDocumentBuilder.Stylesheet(new StringReader(css)));

			AbstractMarkupLanguage markupLanguage = (AbstractMarkupLanguage)language.clone();
			markupLanguage.setEnableMacros(true);

			markupLanguage.setFilterGenerativeContents(false);
			markupLanguage.setBlocksOnly(false);

			MarkupParser markupParser = new MarkupParser();
			markupParser.setBuilder(builder);
			markupParser.setMarkupLanguage(markupLanguage);

			markupParser.parse(document.get());
			browser.addProgressListener(new ProgressAdapter() {
				@Override
				public void completed(ProgressEvent event) {
					browser.removeProgressListener(this);
					browser.execute(String.format("window.scrollTo(0,%d);", verticalScrollbarPos)); //$NON-NLS-1$
				}
			});
			String xhtml = writer.toString();
			browser.setText(xhtml);
		}
	}

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