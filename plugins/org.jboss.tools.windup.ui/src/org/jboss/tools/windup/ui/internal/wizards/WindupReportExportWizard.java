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
package org.jboss.tools.windup.ui.internal.wizards;

import javax.inject.Inject;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.jboss.tools.windup.core.services.WindupService;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;

/**
 * <p>
 * Wizard used to export Windup Reports.
 * </p>
 */
public class WindupReportExportWizard extends Wizard implements IExportWizard {
    /**
     * The ID of the wizard.
     */
    public static final String ID = "org.jboss.tools.windup.ui.exportWizard.WindupReport"; //$NON-NLS-1$

    /**
     * The selection when the wizard was opened used to populate default list of
     * projects to export the Windup Reports for.
     */
    private IStructuredSelection selection;

    /**
     * The main page of the wizard.
     */
    private WindupReportExportWizardPage1 mainPage;

    @Inject
    private WindupService windup;

    /**
     * <p>
     * Required default constructor for extension point creation.
     * </p>
     */
    public WindupReportExportWizard() {
        this.setWindowTitle(Messages.WindupReportExport_exportReportsTitle);

        IDialogSettings workbenchSettings = WindupUIPlugin.getDefault().getDialogSettings();
        IDialogSettings section = workbenchSettings.getSection("WindupReportExportWizard");//$NON-NLS-1$
        if (section == null) {
            section = workbenchSettings.addNewSection("WindupReportExportWizard");//$NON-NLS-1$
        }
        this.setDialogSettings(section);
    }

    /**
     * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
     *      org.eclipse.jface.viewers.IStructuredSelection)
     */
    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.selection = selection;
    }

    /**
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    @Override
    public void addPages() {
        this.mainPage = new WindupReportExportWizardPage1(windup, this.selection);
        this.addPage(mainPage);
    }

    /**
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {
        return this.mainPage.finish();
    }

    /**
     * <p>
     * The export process can be a lengthy one, especially if new Windup reports
     * need to be generated first, therefore show a progress bar for any runables
     * run during the finish operation.
     * </p>
     * 
     * @see org.eclipse.jface.wizard.Wizard#needsProgressMonitor()
     */
    @Override
    public boolean needsProgressMonitor() {
        return true;
    }
}