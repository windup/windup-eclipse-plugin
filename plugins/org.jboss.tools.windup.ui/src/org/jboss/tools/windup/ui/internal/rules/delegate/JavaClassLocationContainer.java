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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.xtext.util.Pair;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.RuleMessages;
import org.jboss.tools.windup.ui.internal.editor.AddNodeAction;
import org.jboss.tools.windup.ui.internal.editor.DeleteNodeAction;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.ChoiceAttributeRow;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.RulesetConstants;
import org.jboss.tools.windup.ui.internal.rules.delegate.JavaClassDelegate.JAVA_CLASS_REFERENCE_LOCATION;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

@SuppressWarnings({"restriction"})
public class JavaClassLocationContainer {
	
	private static final int MIN_WIDTH = 350;
	
	private Element element;
	private IStructuredModel model;
	private ModelQuery modelQuery;
	private CMElementDeclaration elementDeclaration;

	private FormToolkit toolkit;
	private Composite parentControl;
	private ScrolledComposite scroll;
	private ListContainer locationListContainer;
	
	public JavaClassLocationContainer(Element element, IStructuredModel model, ModelQuery modelQuery, CMElementDeclaration elementDeclaration, 
			FormToolkit toolkit) {
		this.element = element;
		this.model = model;
		this.modelQuery = modelQuery;
		this.elementDeclaration = elementDeclaration;
		this.toolkit = toolkit;
	}
	
	public void bind() {
		loadLocations();
		scroll.setMinHeight(locationListContainer.computeHeight());
		int width = locationListContainer.getItemCount() > 0 ? MIN_WIDTH : 0;
		scroll.setMinWidth(width);
		parentControl.getParent().getParent().getParent().layout(true, true);
	}
	
	private void loadLocations() {
		locationListContainer.createControls(parentControl, collectLocations());
		locationListContainer.bind();
	}
	
	private List<Element> collectLocations() {
		List<Element> links = Lists.newArrayList();
		NodeList list = element.getElementsByTagName(RulesetConstants.JAVA_CLASS_LOCATION);
		for (int i = 0; i < list.getLength(); i++) {
			links.add((Element)list.item(i));
		}
		return links;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private CMElementDeclaration getLocationCmNode() {
		List candidates = modelQuery.getAvailableContent(element, elementDeclaration, 
				ModelQuery.VALIDITY_STRICT);
		Optional<CMElementDeclaration> found = candidates.stream().filter(candidate -> {
			if (candidate instanceof CMElementDeclaration) {
				return RulesetConstants.JAVA_CLASS_LOCATION.equals(((CMElementDeclaration)candidate).getElementName());
			}
			return false;
		}).findFirst();
		if (found.isPresent()) {
			return found.get();
		}
		return null;
	}
	
	public void createControls(Composite parent) {
		Pair<Section, Composite> result = ElementDetailsSection.createScrolledSection(toolkit, parent,RuleMessages.javaclass_locationSectionTitle, RuleMessages.javaclass_locationDescription,
				ExpandableComposite.TITLE_BAR | Section.DESCRIPTION | Section.NO_TITLE_FOCUS_BOX | Section.TWISTIE);
		Section section = result.getFirst();
		Composite client = result.getSecond();
		this.scroll = (ScrolledComposite)section.getClient();
		this.parentControl = client;
		this.locationListContainer = createLocationListContainer();
		createSectionToolbar(section);
 	}
	
	private void createSectionToolbar(Section section) {
		ToolBar toolbar = new ToolBar(section, SWT.FLAT|SWT.HORIZONTAL);
		ToolItem addItem = new ToolItem(toolbar, SWT.PUSH);
		addItem.setImage(WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_ADD));
		addItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				CMElementDeclaration linkCmNode = getLocationCmNode();
				AddNodeAction action = (AddNodeAction)ElementUiDelegate.createAddElementAction(
						model, element, linkCmNode, element.getChildNodes().getLength(), null);
				action.run();
			}
		});
		section.setTextClient(toolbar);
	}
	
	private ListContainer createLocationListContainer() {
		ListContainer listContainer = new ListContainer() {
			@Override
			protected ListItem createListItem(Composite parent, Element listElement) {
				ListItem item = new ListItem(parent, listElement) {
					@Override
					protected void createControls() {
						CMElementDeclaration ed = modelQuery.getCMElementDeclaration(itemElement);
						
						Composite left = toolkit.createComposite(this);
						GridLayoutFactory.fillDefaults().numColumns(2).applyTo(left);
						GridDataFactory.fillDefaults().grab(true, false).applyTo(left);
						
						ChoiceAttributeRow row = createLocationRow(ed, listElement);
						row.createContents(left, toolkit, 2);
			    	  		rows.add(row);
			    	  		
			    	  		createDeleteLocationElementButton(left, listElement);
					}
				};
				return item;
			}
		};
		return listContainer;
	}
	
	private void createDeleteLocationElementButton(Composite parent, Element locationElement) {
		ToolBar toolbar = new ToolBar(parent, SWT.FLAT|SWT.HORIZONTAL);
		ToolItem addItem = new ToolItem(toolbar, SWT.PUSH);
		addItem.setImage(WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_DELETE_CONFIG));
		addItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DeleteNodeAction deleteAction = new DeleteNodeAction(model, Lists.newArrayList(locationElement));
				deleteAction.run();
			}
		});
	}
	
	private ChoiceAttributeRow createLocationRow(CMNode cmNode, Element listElement) {
		return new ChoiceAttributeRow(element, cmNode, true) {
			@Override
			protected Control createLabel(Composite parent, FormToolkit toolkit) {
				return null;
			}
			
			@Override
			protected Node getNode() {
				return listElement;
			}
			
			@Override
			protected List<String> getOptions() {
				return Arrays.stream(JAVA_CLASS_REFERENCE_LOCATION.values()).map(e -> computeUiValue(e)).
						collect(Collectors.toList());
			}
			
			private String computeUiValue(JAVA_CLASS_REFERENCE_LOCATION location) {
				return location.getLabel() + " - " + location.getDescription();
			}

			@Override
			protected String modelToDisplayValue(String modelValue) {
				if (modelValue == null || modelValue.isEmpty()) {
					return "";
				}
				
				Optional<JAVA_CLASS_REFERENCE_LOCATION> location = Arrays.stream(JAVA_CLASS_REFERENCE_LOCATION.values()).filter(e -> {
					return Objects.equal(e.getLabel(), modelValue);
				}).findFirst();
				
				if (location.isPresent()) {
					return computeUiValue(location.get());
				}
				
				return "";
			}
			
			@Override
			protected String displayToModelValue(String uiValue) {
				if (uiValue.isEmpty()) {
					return "";
				}
				
				Optional<JAVA_CLASS_REFERENCE_LOCATION> location = Arrays.stream(JAVA_CLASS_REFERENCE_LOCATION.values()).filter(e -> {
					return Objects.equal(uiValue, computeUiValue(e));
				}).findFirst();
				
				if (location.isPresent()) {
					return location.get().getLabel();
				}
				
				return "";
			}
		};
	}
}
