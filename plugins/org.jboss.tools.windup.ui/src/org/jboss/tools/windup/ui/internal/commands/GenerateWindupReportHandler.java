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
package org.jboss.tools.windup.ui.internal.commands;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ListSelectionDialog;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.jboss.tools.windup.core.WindupService;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.Utils;
import org.jboss.tools.windup.ui.internal.views.WindupReportView;

/**
 * <p>
 * Handles generating the windup report for a project.
 * </p>
 */
public class GenerateWindupReportHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
			Shell activeShell = HandlerUtil.getActiveShellChecked(event);
			
			final ISelection selection = HandlerUtil.getCurrentSelection(event);
			List<IProject> currentSelectionProjects = Utils.getSelectedProjects(selection);
			
			//open multiple project selection dialog
			ListSelectionDialog projectSelectDialog = new ListSelectionDialog(
					activeShell,
					ResourcesPlugin.getWorkspace().getRoot(), 
					new BaseWorkbenchContentProvider(),
					new WorkbenchLabelProvider(),
					Messages.select_projects_to_generate_windup_reports_for);
			projectSelectDialog.setTitle(Messages.generate_windup_report); 
			projectSelectDialog.setInitialElementSelections(currentSelectionProjects);
			projectSelectDialog.open();
			
			//if user made selection
			if(projectSelectDialog.getReturnCode() == Window.OK) {
				Object[] userSelectedObjects = projectSelectDialog.getResult();
				final IProject[] userSelectedProjects = Arrays.copyOf(userSelectedObjects,
						userSelectedObjects.length, IProject[].class);
				
				if(userSelectedProjects != null && userSelectedProjects.length > 0) {
				
					//create a job to generate the report in
					Job job = new Job(Messages.generate_windup_report) {
						@Override
						protected IStatus run(IProgressMonitor monitor) {	
							//generate the report
							IStatus status = WindupService.getDefault().generateGraph(userSelectedProjects, monitor);
							
							//show the report view for the selected resource
							Display.getDefault().asyncExec(new Runnable() {
								@Override
								public void run() {
									try {
										WindupReportView windupView = (WindupReportView)PlatformUI.getWorkbench()
												.getActiveWorkbenchWindow().getActivePage().showView(WindupReportView.ID);
										windupView.updateSelection(selection);
									} catch (PartInitException e) {
										WindupUIPlugin.logError("Error opening the Windup Report view", e); //$NON-NLS-1$
									}
								}
							});
							
							return status;
						}
					};
					job.setUser(true);
					job.schedule();
				}
			}
		
		return null;
	}
}