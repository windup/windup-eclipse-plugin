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
package org.jboss.tools.windup.core;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.osgi.util.NLS;
import org.jboss.tools.windup.core.internal.Messages;

/**
 * <p>
 * Service used to perform Windup operations inside of Eclipse.
 * </p>
 * 
 * TODO: IAN: turn this into a true Eclipse service
 * 
 * TODO: IAN: move {@link WindupCorePlugin#getWindupEngine()} here
 * TODO: IAN: move {@link WindupCorePlugin#getWindupReportEngine()} here
 */
public class WindupReportGenerator {
	private static final String PROJECT_REPORT_HOME_PAGE = "index.html"; //$NON-NLS-1$
	private static final String HTML_FILE_EXTENSION = "html"; //$NON-NLS-1$

	private static WindupReportGenerator generator;
	
	private static IPath reportsDir = WindupCorePlugin.getDefault().getStateLocation().append("reports"); //$NON-NLS-1$
	
	private WindupReportGenerator() {
	}
	
	/**
	 * @return singleton instance of the {@link WindupReportGenerator}
	 */
	public static WindupReportGenerator getDefault() {
		if(generator == null) {
			generator = new WindupReportGenerator();
		}
		
		return generator;
	}
	
	/**
	 * <p>
	 * Get the Windup report for the given resource.
	 * </p>
	 * 
	 * @param resource
	 * @return
	 */
	public IPath getReportPath(IResource resource) {
		
		IPath projectReportPath = reportsDir.append(resource.getProject().getName());
		
		IPath reportPath = null;
		switch (resource.getType()) {
			// if selected resource is a file get the file specific report page
			case IResource.FILE: {
				IPath workspaceRelativePath = resource.getProject().getFullPath().append(resource.getProjectRelativePath());
				
				reportPath = projectReportPath.append(workspaceRelativePath).addFileExtension(HTML_FILE_EXTENSION);
				
				break;
			}
			
			// if selected resource is the project then get the windup report home page
			case IResource.PROJECT: {
				reportPath = projectReportPath.append(PROJECT_REPORT_HOME_PAGE);
				break;
			}
			
			default: {
				break;
			}
		}
		
		//determine if the report of the given file exists
		File reportFile = new File(reportPath.toString());
		if(!reportFile.exists()) {
			
		}
		
		return reportPath;
	}
	
	/**
	 * <p>
	 * Generate a Windup report for the project containing the given resource.
	 * </p>
	 * 
	 * @param forResource
	 *            Generate a Windup report for the project containing this
	 *            resource
	 */
	public void generateReport(IResource forResource) {
		final IProject selectedProject = forResource.getProject();
		final String projectName = selectedProject.getName();
		
		Job generateJob = new Job(NLS.bind(Messages.generate_windup_report_for, projectName)) {
			
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				monitor.beginTask(this.getName(), IProgressMonitor.UNKNOWN);
				IStatus status = null;
				
				try {
					File inputDir = selectedProject.getLocation().toFile();
					IPath outputPath = reportsDir.append(projectName);
					WindupCorePlugin.getDefault().getWindupReportEngine().process(
							inputDir, outputPath.toFile());
					
					status = Status.OK_STATUS;
				} catch (IOException e) {
					status = new Status(IStatus.ERROR,
							WindupCorePlugin.PLUGIN_ID,
							NLS.bind(Messages.error_generating_report_for, projectName));
					
					WindupCorePlugin.logError("There was an error generating the Windup report " //$NON-NLS-1$
							+ "for the project " + projectName, e); //$NON-NLS-1$
				} finally {
					monitor.done();
				}
				
				return status;
			}
		};
		
		generateJob.schedule();
	}
}