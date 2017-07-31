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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Element;

import com.google.common.collect.Lists;

public abstract class ListContainer {
	
	private List<ListItem> items = Lists.newArrayList();
	
	public void createControls(Composite parent, List<Element> elements) {
		for (Iterator<ListItem> iter = items.iterator(); iter.hasNext();) {
			ListItem item = iter.next();
			if (!elements.contains(item.getLinkElement())) {
				item.dispose();
				iter.remove();
			}
		}
		ListItem previousContainer = null;
		for (Element linkElement : elements) {
			
			ListItem linkContainer = findListItem(linkElement);
			
			if (linkContainer == null) {
				linkContainer = createListItem(parent, linkElement);
				items.add(linkContainer);
			}
			
			if (previousContainer != null) {
				linkContainer.setPreviousContainer(previousContainer);
			}
			
			previousContainer = linkContainer;
		}
	}
	
	protected abstract ListItem createListItem(Composite parent, Element listElement);
	
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
		return items.size();
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