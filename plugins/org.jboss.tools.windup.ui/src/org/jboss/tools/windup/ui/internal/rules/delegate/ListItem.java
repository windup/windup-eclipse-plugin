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

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.windup.ui.internal.editor.ConfigurationBlock;
import org.jboss.tools.windup.ui.internal.editor.ConfigurationBlock.ToolbarContainer;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.NodeRow;
import org.w3c.dom.Element;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

public abstract class ListItem extends Composite {
	
	protected Element itemElement;
	protected List<NodeRow> rows = Lists.newArrayList();
	protected ToolbarContainer toolbarContainer;
	
	public ListItem(Composite parent, Element itemElement) {
		super(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(1).applyTo(this);
		FormData data = new FormData();
		data.left = new FormAttachment(0);
		data.right = new FormAttachment(100);
		setLayoutData(data);
		this.itemElement = itemElement;
		createControls();
	}
	
	protected abstract void createControls();
	
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
}
