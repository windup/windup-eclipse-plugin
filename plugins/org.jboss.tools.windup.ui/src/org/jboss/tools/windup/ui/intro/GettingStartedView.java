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
package org.jboss.tools.windup.ui.intro;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.jboss.tools.windup.ui.WindupUIPlugin;

/**
 * The Windup getting started view.
 */
public class GettingStartedView extends ViewPart {
	
	public static final String VIEW_ID = "org.jboss.tools.windup.ui.gettingStarted";

	@Override
	public void createPartControl(Composite parent) {
		Browser browser = new Browser(parent, SWT.NONE);
    	try {
    		URL url = FileLocator.find(
        			WindupUIPlugin.getDefault().getBundle(), 
        			new Path("html/windup.html"), null);
			url = FileLocator.toFileURL(url);
			browser.setUrl(url.getPath());
		} catch (IOException e) {
			WindupUIPlugin.log(e);
		}
	}

	@Override
	public void setFocus() {
	}
}
