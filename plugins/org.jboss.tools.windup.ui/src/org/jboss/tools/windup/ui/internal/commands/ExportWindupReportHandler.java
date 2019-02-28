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
package org.jboss.tools.windup.ui.internal.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.wizards.IWizardDescriptor;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.wizards.WindupReportExportWizard;

/**
 * <p>
 * Handler to invoke the Export Windup Report wizard.
 * </p>
 */
public class ExportWindupReportHandler extends AbstractHandler
{

    /**
     * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
     */
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        // get the export wizard
        IWizardDescriptor descriptor = PlatformUI.getWorkbench()
                    .getExportWizardRegistry().findWizard(WindupReportExportWizard.ID);

        try
        {
            // set up and open the windup export wizard
            IWorkbenchWizard wizard = (IWorkbenchWizard) descriptor.createWizard();
            ISelection selection = HandlerUtil.getCurrentSelection(event);
            if (selection instanceof IStructuredSelection)
            {
                wizard.init(PlatformUI.getWorkbench(), (IStructuredSelection) selection);
            }
            Shell activeShell = HandlerUtil.getActiveShellChecked(event);
            WizardDialog wizardDialog = new WizardDialog(activeShell, wizard);
            wizardDialog.setTitle(wizard.getWindowTitle());
            wizardDialog.open();
        }
        catch (CoreException e)
        {
            WindupUIPlugin.logError("Unexpected error opening Windup Export Wizard", e); //$NON-NLS-1$
        }

        return null;
    }
}