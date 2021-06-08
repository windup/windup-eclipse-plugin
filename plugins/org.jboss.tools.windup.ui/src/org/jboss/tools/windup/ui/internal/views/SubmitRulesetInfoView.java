/*******************************************************************************
 * Copyright (c) 2021 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.views;

import java.io.IOException;
import java.net.URL;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.internal.browser.BrowserViewer;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.osgi.framework.Bundle;

@SuppressWarnings("restriction")
public class SubmitRulesetInfoView {
	
	public static final String ID = "org.jboss.tools.windup.ui.submitRulesetInfo";
	
	private BrowserViewer browserViewer;
	
	@PostConstruct
	private void create(Composite parent, IEclipseContext context) {
		GridLayoutFactory.fillDefaults().applyTo(parent);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(parent);
		browserViewer = new BrowserViewer(parent, BrowserViewer.BUTTON_BAR | BrowserViewer.LOCATION_BAR);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(browserViewer);
		try {
			Bundle bundle = WindupUIPlugin.getDefault().getBundle();
			URL fileURL = FileLocator.find(bundle, new Path("html/submit-ruleset-form.html"), null);
			String formPath = FileLocator.resolve(fileURL).getPath();
			browserViewer.setURL(formPath);
		} catch (IOException e) {
			WindupUIPlugin.log(e);
		}
	}
	
	@PreDestroy
	private void dispose() {
	}
}