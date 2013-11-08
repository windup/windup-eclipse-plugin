/*******************************************************************************
* Copyright (c) 2011 Red Hat, Inc.
* Distributed under license by Red Hat, Inc. All rights reserved.
* This program is made available under the terms of the
* Eclipse Public License v1.0 which accompanies this distribution,
* and is available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   Red Hat, Inc. - initial API and implementation
******************************************************************************/
package org.jboss.tools.windup.ui.internal.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.views.WindupReportView;

/**
 * <p>
 * Handles viewing the current selection in the Windup Report Viewer
 */
public class ViewWindupReportHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			WindupReportView windupView = (WindupReportView)PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage().showView(WindupReportView.ID);
			ISelection selection = HandlerUtil.getCurrentSelection(event);
			windupView.updateSelection(selection);
		} catch (PartInitException e) {
			WindupUIPlugin.logError("Error opening the Windup Report view", e); //$NON-NLS-1$
		}
		
		return null;
	}
}