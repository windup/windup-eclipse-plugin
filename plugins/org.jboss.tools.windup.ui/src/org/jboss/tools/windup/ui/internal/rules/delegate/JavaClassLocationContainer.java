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
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.ui.internal.tabletree.TreeContentHelper;
import org.eclipse.xtext.util.Pair;
import org.eclipse.xtext.util.Tuples;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.RuleMessages;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.RulesetConstants;
import org.jboss.tools.windup.ui.internal.services.AnnotationService;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.google.common.collect.Lists;

@SuppressWarnings({"restriction"})
public class JavaClassLocationContainer {
	
	private Element element;
	private IStructuredModel model;
	private ModelQuery modelQuery;
	private CMElementDeclaration elementDeclaration;

	private FormToolkit toolkit;
	private Composite parentControl;
	private ScrolledComposite scroll;
	private ListContainer locationListContainer;
	
	private RulesetElementUiDelegateFactory uiDelegateFactory;
	private IEclipseContext context;
	private TreeContentHelper contentHelper;
	
	public JavaClassLocationContainer(Element element, IStructuredModel model, ModelQuery modelQuery, CMElementDeclaration elementDeclaration, 
			FormToolkit toolkit, RulesetElementUiDelegateFactory uiDelegateFactory, IEclipseContext context, TreeContentHelper contentHelper) {
		this.element = element;
		this.model = model;
		this.modelQuery = modelQuery;
		this.elementDeclaration = elementDeclaration;
		this.toolkit = toolkit;
		this.uiDelegateFactory = uiDelegateFactory;
		this.context = context;
		this.contentHelper = contentHelper;
	}
	
	private void initExpansionState() {
		if (locationListContainer.getItemCount() > 0) {
			((Section)scroll.getParent()).setExpanded(true);
		}
	}
	
	public void bind() {
		loadLocations(collectLocations());
		scroll.setMinHeight(locationListContainer.computeHeight());
		
		StringBuffer buff = new StringBuffer();
		buff.append(RuleMessages.javaclass_locationSectionTitle);
		buff.append(" (" + locationListContainer.getItemCount() + ")");
		((Section)scroll.getParent()).setText(buff.toString());
	}
	
	private void loadLocations(List<Element> location) {
		locationListContainer.createControls(parentControl, location);
		locationListContainer.bind();
	}
	
	public boolean isEmpty() {
		NodeList list = element.getElementsByTagName(RulesetConstants.JAVA_CLASS_LOCATION);
		return list.getLength() == 0;
	}
	
	private List<Element> collectLocations() {
		List<Element> links = Lists.newArrayList();
		NodeList list = element.getElementsByTagName(RulesetConstants.JAVA_CLASS_LOCATION);
		for (int i = 0; i < list.getLength(); i++) {
			links.add((Element)list.item(i));
		}
		return links;
	}
	
	public Section createControls(Composite parent) {
		Pair<Section, Composite> result = ElementDetailsSection.createScrolledSection(toolkit, parent,RuleMessages.javaclass_locationSectionTitle, RuleMessages.javaclass_locationDescription,
				ExpandableComposite.TITLE_BAR | Section.NO_TITLE_FOCUS_BOX | Section.TWISTIE,
				ElementDetailsSection.DEFAULT_SCROLL_SECTION_MAX_HEGHT);
		Section section = result.getFirst();
		Composite client = result.getSecond();
		this.scroll = (ScrolledComposite)section.getClient();
		this.parentControl = client;
		this.locationListContainer = new ListContainer(toolkit, contentHelper, modelQuery, model, uiDelegateFactory, context) {
			protected ListItem createListItem(Composite parent, Element element) {
				return new ListItem(parent, toolkit, element, contentHelper, modelQuery, model, uiDelegateFactory, context) {
					@Override
					protected Pair<Composite, Composite> createListItemContainers(Composite parent) {

						Composite group = toolkit.createComposite(parent);
						GridDataFactory.fillDefaults().grab(true, false).applyTo(group);
						group.setLayout(new FormLayout());
						
						Composite left = toolkit.createComposite(group);
						GridLayoutFactory.fillDefaults().numColumns(1).margins(0, 0).applyTo(left);
						FormData leftData = new FormData();
						leftData.left = new FormAttachment(0);
						left.setLayoutData(leftData);
						
						Composite right = toolkit.createComposite(group);
						
						GridLayout gridLayout = new GridLayout(1, false);
						gridLayout.marginWidth = 0;
						gridLayout.marginHeight = 0;
						gridLayout.marginTop = 3;
						gridLayout.verticalSpacing = 0;
						gridLayout.horizontalSpacing = 0;
						right.setLayout(gridLayout);
						
						FormData rightData = new FormData();
						rightData.right = new FormAttachment(100);
						rightData.bottom = new FormAttachment(73);
						right.setLayoutData(rightData);
						
						leftData.right = new FormAttachment(right);
						
						return Tuples.create(left, right);
					}
				};
			};
		};
		locationListContainer.createControls(client, collectLocations());
		initExpansionState();
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
				AnnotationService annotationService = new AnnotationService();	
				annotationService.createLocationWithName(element, null);
			}
		});
		section.setTextClient(toolbar);
	}
}
