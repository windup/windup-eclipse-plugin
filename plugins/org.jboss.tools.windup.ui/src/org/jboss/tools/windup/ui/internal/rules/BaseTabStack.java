/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.rules;

import java.util.Map;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.google.common.collect.Maps;

/**
 * Represents a stack of stabs.
 */
public class BaseTabStack {

	protected Map<CTabItem, TabWrapper> tabs = Maps.newHashMap();
	
	@Inject protected IEclipseContext context;
	@Inject @Optional protected FormToolkit toolkit;
	
	protected CTabFolder folder;
	
	public BaseTabStack() {
	}
	
	protected void createFolder(Composite parent) {
		GridDataFactory.fillDefaults().grab(true, true).applyTo(parent);
		this.folder = new CTabFolder(parent, SWT.BOTTOM | SWT.FLAT);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(folder);
	}
	
	protected <T> TabWrapper addTab(Class<T> clazz) {
		CTabItem item = new CTabItem(folder, SWT.NONE);
		Composite parent = new Composite(folder, SWT.NONE);
		GridLayoutFactory.fillDefaults().applyTo(parent);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(parent);
		item.setControl(parent);
		IEclipseContext child = createTabContext(parent);
		child.set(CTabItem.class, item);
		T object = create(clazz, child);
		TabWrapper wrapper = new TabWrapper(object, child, item);
		tabs.put(item, wrapper);
		return wrapper;
	}
	
	protected IEclipseContext createTabContext(Composite parent) {
		IEclipseContext child = context.createChild();
		child.set(Composite.class, parent);
		child.set(FormToolkit.class, toolkit);
		return child;
	}
	
	protected <T> T create(Class<T> clazz, IEclipseContext context) {
		return ContextInjectionFactory.make(clazz, context);
	}
	
	public Control getControl() {
		return folder;
	}
	
	protected void closeTab() {
	}
	
	/**
	 * A wrapper for each child tab.
	 */
	public static class TabWrapper {
		
		private Object object;
		private IEclipseContext context;
		private CTabItem item;
		
		public TabWrapper(Object object, IEclipseContext context, CTabItem item) {
			this.context = context;
			this.object = object;
			this.item = item;
		}

		public Object getObject() {
			return object;
		}
		
		public IEclipseContext getContext() {
			return context;
		}
		
		public CTabItem getItem() {
			return item;
		}
	}
}
