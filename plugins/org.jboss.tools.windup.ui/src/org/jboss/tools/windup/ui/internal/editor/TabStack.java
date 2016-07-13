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
package org.jboss.tools.windup.ui.internal.editor;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.google.common.collect.Maps;

/**
 * Represents a stack of stabs.
 */
public class TabStack {

	private Map<CTabItem, TabWrapper> tabs = Maps.newHashMap();
	
	@Inject private IEclipseContext context;
	@Inject private FormToolkit toolkit;
	
	protected CTabFolder folder;
	
	@PostConstruct
	protected void create(Composite parent) {
		this.folder = new CTabFolder(parent, SWT.BOTTOM | SWT.FLAT);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(folder);
		folder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) { 
				CTabItem item = (CTabItem) e.item;
				TabWrapper tab = tabs.get(item);
				ContextInjectionFactory.invoke(
						tab.getObject(), 
						Focus.class, 
						tab.getContext());
			}
		});
	}
	
	protected <T> T addTab(Class<T> clazz) {
		CTabItem item = new CTabItem(folder, SWT.NONE);
		Composite parent = new Composite(folder, SWT.NONE);
		parent.setLayout(new FillLayout());
		item.setControl(parent);
		IEclipseContext child = createTabContext(parent);
		child.set(CTabItem.class, item);
		T object = create(clazz, child);
		tabs.put(item, new TabWrapper(object, child));
		return object;
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
	
	public Composite getControl() {
		return folder;
	}
	
	/**
	 * A wrapper for each child tab.
	 */
	public static class TabWrapper {
		
		private Object object;
		private IEclipseContext context;
		
		public TabWrapper(Object object, IEclipseContext context) {
			this.context = context;
			this.object = object;
		}

		public Object getObject() {
			return object;
		}
		
		public IEclipseContext getContext() {
			return context;
		}
	}
}
