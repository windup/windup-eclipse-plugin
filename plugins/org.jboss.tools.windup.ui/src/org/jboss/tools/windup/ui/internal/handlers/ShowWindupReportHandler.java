/*******************************************************************************
 * Copyright (c) 2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.ISelection;
import org.jboss.tools.windup.ui.internal.services.ViewService;
import org.jboss.tools.windup.ui.internal.views.WindupReportView;

/**
 * Loads the Windup report for the current selection in the Windup report view.
 */
public class ShowWindupReportHandler {
	@Execute
	public void show(@Named(IServiceConstants.ACTIVE_SELECTION) ISelection selection, 
			ViewService viewService) {
		((WindupReportView)viewService.activateWindupReportView()).updateSelection(selection);
	}
}
