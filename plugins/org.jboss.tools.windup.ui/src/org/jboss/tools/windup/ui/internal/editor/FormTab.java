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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.menus.IMenuService;

/**
 * Represents a tab within a {@link MultiPageForm}.
 */
public class FormTab {

	@Inject protected FormToolkit toolkit;
	@Inject protected CTabItem item;
	@Inject protected IMenuService menuService;
	@Inject protected IEclipseContext context;
	
	protected ScrolledForm form;

	@PostConstruct
	protected void create(Composite parent) {
		form = toolkit.createScrolledForm(parent);
		GridLayoutFactory.fillDefaults().applyTo(form.getBody());
		BusyIndicator.showWhile(parent.getDisplay(), new Runnable() {
			@Override
			public void run() {
				createFormContent(form.getBody());
			}
		});
		toolkit.paintBordersFor(form.getBody());
	}
	
	@PreDestroy
	protected void dispose() {
		form.dispose();
	}
	
	protected void createFormContent(Composite parent) {}
	
	protected IEclipseContext createChildContext() {
		IEclipseContext child = context.createChild();
		return child;
	}
}
