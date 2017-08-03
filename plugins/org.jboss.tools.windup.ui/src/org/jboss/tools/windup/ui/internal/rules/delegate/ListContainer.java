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

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.ui.internal.tabletree.TreeContentHelper;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory;
import org.w3c.dom.Element;

import com.google.common.collect.Lists;

@SuppressWarnings({"restriction"})
public class ListContainer {
	
	private List<ListItem> items = Lists.newArrayList();
	
	protected FormToolkit toolkit;
	protected TreeContentHelper contentHelper;
	protected ModelQuery modelQuery;
	protected IStructuredModel model;
	
	protected RulesetElementUiDelegateFactory uiDelegateFactory;
	protected IEclipseContext context;
	
	public ListContainer(FormToolkit toolkit, TreeContentHelper contentHelper, ModelQuery modelQuery, IStructuredModel model,
			RulesetElementUiDelegateFactory uiDelegateFactory, IEclipseContext context) {
		this.toolkit = toolkit;
		this.contentHelper = contentHelper;
		this.modelQuery = modelQuery;
		this.model = model;
		this.uiDelegateFactory = uiDelegateFactory;
		this.context = context;
	}
	
	public void createControls(Composite parent, List<Element> elements) {
		for (Iterator<ListItem> iter = items.iterator(); iter.hasNext();) {
			ListItem item = iter.next();
			if (!elements.contains(item.getLinkElement())) {
				item.dispose();
				iter.remove();
			}
		}
		ListItem previousContainer = null;
		for (Element element : elements) {
			
			ListItem listItem = findListItem(element);
			
			if (listItem == null) {
				listItem = createListItem(parent, element);
				items.add(listItem);
			}
			
			if (previousContainer != null) {
				listItem.setPreviousContainer(previousContainer);
			}
			
			ListItem lastChild = listItem.createChildControls(parent, listItem, 1);
			previousContainer = lastChild != null ? lastChild : listItem;
		}
	}
	
	private ListItem createListItem(Composite parent, Element element) {
		return new ListItem(parent, toolkit, element, contentHelper, modelQuery, model, uiDelegateFactory, context);
	}
	
	
	public int computeHeight() {
		int height = 0;
		for (ListItem item : items) {
			height += item.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
		}
		return height;
	}
	
	public void bind() {
		items.forEach(item -> item.bind());
	}
	
	public int getItemCount() {
		int count = items.size();
		for (ListItem child : items) {
			count += child.getItemCount();
		}
		return count;
	}
	
	public ListItem findListItem(Element element) {
		Optional<ListItem> option = items.stream().filter(item -> item.isContainerFor(element)).findFirst();
		if (option.isPresent()) {
			return option.get();
		}
		return null;
	}
	
	public ListItem findNextListItem(ListItem item) {
		int index = items.indexOf(item);
		if (index != -1 && index != items.size()-1) {
			return items.get(index+1);
		}
		return null;
	}
}