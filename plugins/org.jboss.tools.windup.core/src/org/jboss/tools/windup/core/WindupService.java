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

import org.apache.commons.io.FileUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.osgi.util.NLS;
import org.jboss.tools.windup.core.internal.Messages;
import org.jboss.windup.WindupEngine;
import org.jboss.windup.WindupEnvironment;
import org.jboss.windup.metadata.type.FileMetadata;
import org.jboss.windup.reporting.ReportEngine;

/**
 * <p>
 * Service used to perform Windup operations inside of Eclipse.
 * </p>
 */
public class WindupService {
	private static final String PROJECT_REPORT_HOME_PAGE = "index.html"; //$NON-NLS-1$
	private static final String HTML_FILE_EXTENSION = "html"; //$NON-NLS-1$

	private static WindupService service;
	
	private static IPath reportsDir = WindupCorePlugin.getDefault().getStateLocation().append("reports"); //$NON-NLS-1$
	
	/**
	 * <p>
	 * {@link WindupEngine} instance used by this plugin.
	 * </p>
	 * 
	 * <p>
	 * This is really expensive to initialize, so it is only done once.
	 * </p>
	 */
	private WindupEngine windupEngine;
	
	/**
	 * <p>
	 * {@link ReportEngine} instance used by this plugin.
	 * </p>
	 * 
	 * <p>
	 * This is really expensive to initialize, so it is only done once.
	 * </p>
	 */
	private ReportEngine reportEngine;
	
	/**
	 * <p>
	 * Private constructor for singleton instance.
	 * </p>
	 */
	private WindupService() {
	}
	
	/**
	 * @return singleton instance of the {@link WindupService}
	 */
	public static WindupService getDefault() {
		if(service == null) {
			service = new WindupService();
		}
		
		return service;
	}
	
	/**
	 * <p>
	 * Use the {@link WindupEngine} to get the {@link FileMetadata} for the
	 * given {@link IResource}.
	 * </p>
	 * 
	 * @param resource
	 *            get the Windup {@link FileMetadata} for this {@link IResource}
	 * 
	 * @return {@link FileMetadata} for the given {@link IResource} or
	 *         <code>null</code> if none can be calculated
	 */
	public FileMetadata getFileMetadata(IResource resource) {
		FileMetadata meta = null;
		
		try {
			meta = this.getWindupEngine().processFile(resource.getLocation().toFile());
		} catch (IOException e) {
			WindupCorePlugin.logError("Error getting Windup metadata for: " + resource, e); //$NON-NLS-1$
		}
		
		return meta;
	}
	
	/**
	 * <p>
	 * Generate a Windup report for the project containing the given resource.
	 * </p>
	 * 
	 * @param resource
	 *            Generate a Windup report for the project containing this
	 *            resource
	 */
	public void generateReport(IResource resource) {
		final IProject selectedProject = resource.getProject();
		final String projectName = selectedProject.getName();
		
		Job generateJob = new Job(NLS.bind(Messages.generate_windup_report_for, projectName)) {
			
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				monitor.beginTask(this.getName(), IProgressMonitor.UNKNOWN);
				IStatus status = null;
				
				try {
					File inputDir = selectedProject.getLocation().toFile();
					IPath outputPath = reportsDir.append(projectName);
					
					File outputDir = outputPath.toFile();
					
					//clear out existing report
					FileUtils.deleteDirectory(outputDir);
					
					//generate new report
					WindupService.this.getWindupReportEngine().process(
							inputDir, outputDir);
					
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
	
	/**
	 * <p>
	 * Get the Windup report for the given resource.
	 * </p>
	 * 
	 * @param resource
	 *            get the location of the Windup report for this
	 *            {@link IResource}
	 * 
	 * @return location of the Windup report for the given {@link IResource}
	 */
	public IPath getReportLocation(IResource resource) {
		
		IPath projectReportPath = reportsDir.append(resource.getProject().getName());
		
		IPath reportPath = null;
		switch (resource.getType()) {
			// if selected resource is a file get the file specific report page
			case IResource.FILE: {
				IPath workspaceRelativePath = resource.getProject().getFullPath().append(resource.getProjectRelativePath());
				
				reportPath = projectReportPath.append(workspaceRelativePath).addFileExtension(HTML_FILE_EXTENSION);
				
				break;
			}
			
			/*  if selected resource is the project then get the Windup
			 * report home page for that project */
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
	 * The {@link WindupEngine} is expensive to initialize so making sure there
	 * is ever only one helps.
	 * </p>
	 * 
	 * @return the single instance of the {@link WindupEngine} to be used in Eclipse
	 */
	private WindupEngine getWindupEngine() {
		if(this.windupEngine == null) {
			try {
				this.windupEngine = new WindupEngine(this.getWindupEnv());
			} catch(Throwable t) {
				WindupCorePlugin.logError("Error getting Windup Engine.", t); //$NON-NLS-1$
			}
		}
		
		return this.windupEngine;
	}
	
	/**
	 * <p>
	 * The {@link ReportEngine} is expensive to initialize so making sure there
	 * is ever only one helps.
	 * </p>
	 * 
	 * @return the single instance of the {@link ReportEngine} to be used in Eclipse
	 */
	private ReportEngine getWindupReportEngine() {
		if(this.reportEngine == null) {
			try {
				this.reportEngine = new ReportEngine(this.getWindupEnv());
			} catch(Throwable t) {
				WindupCorePlugin.logError("Error getting Windup Report Engine.", t); //$NON-NLS-1$
			}
		}
		
		return this.reportEngine;
	}
	
	/**
	 * <p>
	 * Single source for the {@link WindupEnvironment} to use within Eclipse.
	 * </p>
	 * 
	 * @return {@link WindupEnvironment} used to initialize Windup
	 */
	private WindupEnvironment getWindupEnv() {
		WindupEnvironment settings = new WindupEnvironment();
		settings.setSource(true);
		
		return settings;
	}
}