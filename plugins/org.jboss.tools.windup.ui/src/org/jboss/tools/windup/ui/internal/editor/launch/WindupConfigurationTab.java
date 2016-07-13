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
package org.jboss.tools.windup.ui.internal.editor.launch;

import static org.jboss.tools.windup.ui.internal.Messages.launchTab;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.windup.ui.internal.editor.WindupFormTab;

/**
 * The page for configuring Windup.
 */
public class WindupConfigurationTab extends WindupFormTab {
	
	@Override
	protected void createFormContent(Composite parent) {
		item.setText(launchTab);
		createContent(parent);
	}
	
	private void createContent(Composite parent) {
		fillBody(parent);
	}
	
	private void fillBody(Composite parent) {
		GridLayoutFactory.fillDefaults().extendedMargins(5, 0, 10, 0).applyTo(parent);
		IEclipseContext context = createChildContext();
		context.set(Composite.class, parent);
		ContextInjectionFactory.make(GeneralInfoSection.class, context);
	}
}
