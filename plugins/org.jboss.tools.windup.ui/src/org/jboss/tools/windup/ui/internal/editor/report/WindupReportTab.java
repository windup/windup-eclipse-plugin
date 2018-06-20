/*******************************************************************************
 * Copyright (c) 2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.editor.report;

import static org.jboss.tools.windup.ui.internal.Messages.windupReport;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.windup.ui.internal.editor.WindupFormTab;

/**
 * Tab for displaying the Windup reports.
 */
public class WindupReportTab extends WindupFormTab {
	
	@SuppressWarnings("unused")
	private BrowserSection browserSection;
	
	@Override
	protected void createFormContent(Composite parent) {
		item.setText(windupReport);
		createContent(parent);
	}
	
	private void createContent(Composite parent) {
		fillBody(parent);
	}

	private IEclipseContext createChildContext(Composite parent) {
		IEclipseContext context = createChildContext();
		context.set(Composite.class, parent);
		return context;
	}
	
	private void fillBody(Composite parent) {
		Composite container = createContainer(parent);
		GridLayoutFactory.fillDefaults().extendedMargins(5, 5, 10, 5).applyTo(container);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(container);
		this.browserSection = ContextInjectionFactory.make(BrowserSection.class, createChildContext(container));
	}
}
