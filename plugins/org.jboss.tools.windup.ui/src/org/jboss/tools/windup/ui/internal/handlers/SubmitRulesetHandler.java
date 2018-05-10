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
package org.jboss.tools.windup.ui.internal.handlers;

import java.net.URL;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.jboss.tools.foundation.ui.util.BrowserUtility;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.osgi.framework.Bundle;

public class SubmitRulesetHandler extends AbstractHandler {
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String form = computeFormLocation();
		if (form != null) {
			new BrowserUtility().openExtenalBrowser(form);
		}
		return null;
	}
	
	private String computeFormLocation() {
		Bundle bundle = WindupUIPlugin.getDefault().getBundle();
		URL fileURL = FileLocator.find(bundle, new Path("html/submit-ruleset-form.html"), null);
		String location = null;
		try {
			location = FileLocator.resolve(fileURL).toURI().toURL().toString();
		}
		catch (Exception e) {
			MessageDialog.openError(Display.getDefault().getActiveShell(), "Error opening ruleset submission form.", e.getMessage());
		}
		return location;
	}
}