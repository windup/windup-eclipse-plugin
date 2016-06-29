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

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ListSelectionDialog;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.jboss.tools.windup.core.services.WindupService;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.Utils;
import org.jboss.tools.windup.ui.internal.services.ViewService;

/**
 * Generates a Windup report. 
 */
public class GenerateWindupReportHandler {

	@Inject private WindupService windupService;
	@Inject private ViewService viewService;
	
    @Execute
    public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell, @Named(IServiceConstants.ACTIVE_SELECTION) @Optional ISelection selection)
    {
        List<IProject> currentSelectionProjects = Utils.getSelectedProjects(selection);

        // open multiple project selection dialog
        ListSelectionDialog projectSelectDialog = new ListSelectionDialog(
                    shell,
                    ResourcesPlugin.getWorkspace().getRoot(),
                    new BaseWorkbenchContentProvider(),
                    new WorkbenchLabelProvider(),
                    Messages.select_projects_to_generate_windup_reports_for);
        projectSelectDialog.setTitle(Messages.generate_windup_report);
        projectSelectDialog.setInitialElementSelections(currentSelectionProjects);
        // if user made selection
        if (projectSelectDialog.open() == Window.OK)
        {
            Object[] userSelectedObjects = projectSelectDialog.getResult();
            final IProject[] userSelectedProjects = Arrays.copyOf(userSelectedObjects,
                        userSelectedObjects.length, IProject[].class);

            if (userSelectedProjects != null && userSelectedProjects.length > 0)
            {
                // create a job to generate the report in
                Job job = new Job(Messages.generate_windup_report)
                {
                    @Override
                    protected IStatus run(IProgressMonitor monitor)
                    {
                        // generate the report
                        IStatus status = windupService.generateGraph(userSelectedProjects, monitor);

                        // show the report view for the selected resource
                        Display.getDefault().asyncExec(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                            	GenerateWindupReportHandler.this.viewService.activateWindupReportView().updateSelection(selection);
                            }
                        });

                        return status;
                    }
                };
                job.setUser(true);
                job.schedule();
            }
        }
    }
}
