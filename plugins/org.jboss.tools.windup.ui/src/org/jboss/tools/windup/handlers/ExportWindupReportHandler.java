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
package org.jboss.tools.windup.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.windup.ui.internal.wizards.WindupReportExportWizard;

/**
 * Opens the {@link WindupReportExportWizard}.
 */
public class ExportWindupReportHandler {
	@Execute
	public void run(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell, @Named(IServiceConstants.ACTIVE_SELECTION) ISelection selection, IEclipseContext context) {
		WindupReportExportWizard wizard = ContextInjectionFactory.make(WindupReportExportWizard.class, context);
		if (selection instanceof IStructuredSelection) {
			wizard.init(PlatformUI.getWorkbench(), (IStructuredSelection) selection);
		}
		WizardDialog wizardDialog = new WizardDialog(shell, wizard);
		wizardDialog.setTitle(wizard.getWindowTitle());
		wizardDialog.open();
	}
}
