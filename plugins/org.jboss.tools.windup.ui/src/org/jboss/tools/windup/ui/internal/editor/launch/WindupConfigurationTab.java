/*******************************************************************************
 * Copyright (c) 2019 Red Hat, Inc.
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
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.windup.ui.internal.editor.WindupFormTab;

/**
 * The page for configuring Windup.
 */
public class WindupConfigurationTab extends WindupFormTab {
	
	private InputProjectsSection projectSection;
	private InputPackagesSection packageSection;
	
	
	@Override
	protected void createFormContent(Composite parent) {
		item.setText(launchTab);
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
		Composite info = createContainer(parent);
		ContextInjectionFactory.make(GeneralInfoSection.class, createChildContext(info));
		Composite options = createContainer(parent);
		ContextInjectionFactory.make(OptionsSections.class, createChildContext(options));
		Composite input = createContainer(parent);
		GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(true).applyTo(input);
		projectSection = ContextInjectionFactory.make(InputProjectsSection.class, createChildContext(input));
		packageSection = ContextInjectionFactory.make(InputPackagesSection.class, createChildContext(input));
		
		FormLayout layout = new FormLayout();
		layout.marginTop = 10;
		layout.marginLeft = 5;
		layout.marginRight = 5;
		layout.marginBottom = 5;
		parent.setLayout(layout);
		
		FormData data = new FormData();
		data.left = new FormAttachment(0);
		data.right = new FormAttachment(100);
		info.setLayoutData(data);
		
		data = new FormData();
		data.top = new FormAttachment(info);
		data.left = new FormAttachment(0);
		data.right = new FormAttachment(100);
		options.setLayoutData(data);
		
		data = new FormData();
		data.top = new FormAttachment(options);
		data.left = new FormAttachment(0);
		data.right = new FormAttachment(100);
		data.bottom = new FormAttachment(100);
		input.setLayoutData(data);
		
		form.layout(true, true);
	}
	
	@Focus
	private void focus() {
		projectSection.focus();
		packageSection.focus();
	}
}
