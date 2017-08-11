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
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.ui.internal.tabletree.TreeContentHelper;
import org.eclipse.xtext.util.Pair;
import org.eclipse.xtext.util.Tuples;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.editor.DeleteNodeAction;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.IElementUiDelegate;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.NodeRow;
import org.jboss.tools.windup.ui.internal.rules.delegate.ConfigurationBlock.ToolbarContainer;
import org.w3c.dom.Element;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

@SuppressWarnings({"restriction"})
public class ListItem extends Composite {
	
	protected Element itemElement;
	protected List<NodeRow> rows = Lists.newArrayList();
	protected ToolbarContainer toolbarContainer;
	
	protected FormToolkit toolkit;
	protected TreeContentHelper contentHelper;
	protected ModelQuery modelQuery;
	protected IStructuredModel model;
	
	protected RulesetElementUiDelegateFactory uiDelegateFactory;
	protected IEclipseContext context;
	
	protected List<ListItem> childItems = Lists.newArrayList();
	
	public ListItem(Composite parent, FormToolkit toolkit, Element itemElement, TreeContentHelper contentHelper, ModelQuery modelQuery, IStructuredModel model,
			RulesetElementUiDelegateFactory uiDelegateFactory, IEclipseContext context) {
		super(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(1).applyTo(this);
		FormData data = new FormData();
		data.left = new FormAttachment(0);
		data.right = new FormAttachment(100);
		setLayoutData(data);
		this.toolkit = toolkit;
		this.itemElement = itemElement;
		this.contentHelper = contentHelper;
		this.modelQuery = modelQuery;
		this.model = model;
		this.uiDelegateFactory = uiDelegateFactory;
		this.context = context;
		createControls();
	}
	
	protected void createControls() {
		CMElementDeclaration ed = modelQuery.getCMElementDeclaration(itemElement);
		
		Pair<Composite, Composite> containers = createListItemContainers(this);
		
		Composite left = containers.getFirst();
		Composite right = containers.getSecond();
		
		createToolbar(right, this);
		IElementUiDelegate delegate = uiDelegateFactory.createElementUiDelegate(itemElement, context);
		delegate.createControls(left, itemElement, ed, rows);
	}
	
	protected Control createToolbar(Composite parent, ListItem thisItem) {
		ToolBar toolbar = new ToolBar(parent, SWT.FLAT|SWT.VERTICAL);
		ToolItem deleteLinkItem = new ToolItem(toolbar, SWT.PUSH);
		deleteLinkItem.setToolTipText(Messages.RulesetEditor_remove);
		deleteLinkItem.setImage(WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_DELETE_CONFIG));
		//deleteLinkItem.getControl().setCursor(parent.getShell().getDisplay().getSystemCursor(SWT.CURSOR_HAND));
		deleteLinkItem.addSelectionListener(new SelectionAdapter() {
	    		@Override
	    		public void widgetSelected(SelectionEvent e) {
    				DeleteNodeAction deleteAction = new DeleteNodeAction(model, Lists.newArrayList(thisItem.getLinkElement()));
	    			deleteAction.run();
	    		}
		});
	    GridLayoutFactory.fillDefaults().applyTo(toolbar);
	    return toolbar;
	}
	
	protected Pair<Composite, Composite> createListItemContainers(Composite parent) {
		Group group = new Group(parent, SWT.SHADOW_IN);
		group.setBackground(toolkit.getColors().getBackground());
		group.setForeground(toolkit.getColors().getBackground());
		GridDataFactory.fillDefaults().grab(true, false).applyTo(group);
		group.setLayout(new FormLayout());
		
		Composite left = new Composite(group, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(2).applyTo(left);
		FormData leftData = new FormData();
		leftData.left = new FormAttachment(0);
		left.setLayoutData(leftData);
		
		Composite right = new Composite(group, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(1).applyTo(right);
		FormData rightData = new FormData();
		rightData.right = new FormAttachment(100);
		rightData.bottom = new FormAttachment(73);
		right.setLayoutData(rightData);
		
		leftData.right = new FormAttachment(right);
		
		return Tuples.create(left, right);
	}
	
	public ListItem createChildControls(Composite parent, ListItem previousContainer, int childLevel) {
		List<Element> childElements = collectChildElements();
		for (Iterator<ListItem> iter = childItems.iterator(); iter.hasNext();) {
			ListItem item = iter.next();
			if (!childElements.contains(item.getLinkElement())) {
				item.dispose();
				iter.remove();
			}
		}
		for (Element childElement : childElements) {
			
			ListItem listItem = findListItem(childElement);
			
			if (listItem == null) {
				listItem = createChildItem(parent, childElement);
				childItems.add(listItem);
			}
			
			if (previousContainer != null) {
				listItem.setPreviousContainer(previousContainer);
			}
			
			FormData data = (FormData)listItem.getLayoutData();
			data.left = new FormAttachment(childLevel*5);
			
			ListItem lastChild = listItem.createChildControls(parent, listItem, childLevel+1);
			previousContainer = lastChild != null ? lastChild : listItem;
		}
		return previousContainer;
	}
	
	protected ListItem createChildItem(Composite parent, Element element) {
		return new ListItem(parent, toolkit, element, contentHelper, modelQuery, model, uiDelegateFactory, context);
	}
	
	public ListItem findListItem(Element element) {
		Optional<ListItem> option = childItems.stream().filter(item -> item.isContainerFor(element)).findFirst();
		if (option.isPresent()) {
			return option.get();
		}
		return null;
	}
	
	private List<Element> collectChildElements() {
		Object[] elementChildren = contentHelper.getChildren(itemElement);
		return Arrays.stream(elementChildren).filter(o -> o instanceof Element).map
				(o -> (Element)o).collect(Collectors.toList());
	}

	protected void setToolbarContainer(ToolbarContainer toolbarContainer) {
		this.toolbarContainer = toolbarContainer;
		this.setData(ConfigurationBlock.TOOLBAR_CONTROL, toolbarContainer);
	}
	
	public void setPreviousContainer(ListItem previousItem) {
		FormData data = (FormData)getLayoutData();
		data.top = new FormAttachment(previousItem);
	}
	
	public void bind() {
		rows.forEach(row -> row.bind());
		childItems.forEach(child -> child.bind());
	}
	
	public ToolbarContainer getToolbarContainer() {
		return toolbarContainer;
	}
	
	public boolean isContainerFor(Element element) {
		return Objects.equal(this.itemElement, element);
	}
	
	public Element getLinkElement() {
		return itemElement;
	}
	
	public List<ListItem> getChildListItems() {
		return childItems;
	}
	
	public int getItemCount() {
		int count = childItems.size();
		for (ListItem child : childItems) {
			count += child.getItemCount();
		}
		return count;
	}
}
