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
package org.jboss.tools.windup.ui.internal.editor;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class RulesetEditorDocumentationPage {
	
	private static final String URL = "https://access.redhat.com/documentation/en-us/red_hat_application_migration_toolkit/4.1/html-single/rules_development_guide/";

	private Browser browser;
	
	public RulesetEditorDocumentationPage(Composite parent) {
		createControls(parent);
	}
	
	private void createControls(Composite parent) {
		this.browser = new Browser(parent, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(browser);
		browser.setUrl(URL);
	}
	
	public Control getControl() {
		return browser;
	}
}
