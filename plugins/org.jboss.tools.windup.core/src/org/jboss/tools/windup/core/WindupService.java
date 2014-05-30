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
package org.jboss.tools.windup.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.osgi.util.NLS;
import org.jboss.tools.forge.core.furnace.FurnaceProvider;
import org.jboss.tools.forge.core.furnace.FurnaceService;
import org.jboss.tools.windup.core.internal.Messages;
import org.jboss.tools.windup.core.internal.utils.FileUtils;
import org.jboss.windup.WindupEngine;
import org.jboss.windup.WindupEnvironment;
import org.jboss.windup.WindupFactory;
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
	 * Windup itself is not designed to be run in a multi-threaded/multi-access
	 * environment. This lock is used to make the use of a single shared
	 * {@link WindupEngine} and {@link ReportEngine} thread safe.
	 * </p>
	 */
	private final Object windupLock;
	
	/**
	 * <p>
	 * List of the subscribed {@link IWindupReportListener}s.
	 * </p>
	 */
	private List<IWindupReportListener> windupReportListeners;
	
	/**
	 * <p>
	 * Private constructor for singleton instance.
	 * </p>
	 */
	private WindupService() {
		this.windupLock = new Object();
		this.windupReportListeners = new ArrayList<IWindupReportListener>();
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
			synchronized(this.windupLock) {
				meta = this.getWindupEngine().processFile(resource.getLocation().toFile());
			}
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
	 * <p>
	 * This can be a long running operation, it should be run in a Job.
	 * </p>
	 * 
	 * @param resource
	 *            Generate a Windup report for the project containing this
	 *            resource
	 * @param monitor
	 *            {@link IProgressMonitor} to report progress to
	 */
	public IStatus generateReport(IResource resource, IProgressMonitor monitor) {
		return this.generateReport(new IProject[] {resource.getProject()}, monitor);
	}
	
	/**
	 * <p>
	 * Generate a Windup report for the given projects.
	 * </p>
	 * 
	 * <p>
	 * This can be a long running operation, it should be run in a Job.
	 * </p>
	 * 
	 * @param projects
	 *            Generate a Windup reports for these {@link IProject}s
	 * @param monitor
	 *            {@link IProgressMonitor} to report progress to
	 */
	public IStatus generateReport(IProject[] projects, IProgressMonitor monitor) {
		//protect against a null given for the progress monitor
		IProgressMonitor progress;
		if(monitor != null) {
			progress = monitor;
		} else {
			progress = new NullProgressMonitor();
		}
		
		//start the task
		progress.beginTask(Messages.generate_windup_reports, projects.length);
		IStatus status = null;
		
		try {
			//for each project generate a report 
			for(IProject project : projects) {
				// if not canceled, continue generating reports for the given projects
				if(!progress.isCanceled()) {
					status = this.generateReport(project, progress);
					
					//if not an okay status stop generating reports
					if(!status.equals(Status.OK_STATUS)) {
						break;
					}
				} else {
					status = Status.CANCEL_STATUS;
					break;
				}
			}
		} finally {
			progress.done();
		}
		
		return status;
	}
	
	/**
	 * <p>
	 * Generate a Windup report for the given project.
	 * </p>
	 * 
	 * <p>
	 * <b>NOTE:</b> This can be a long running operation, it should be run in a
	 * Job.
	 * </p>
	 * 
	 * @param project
	 *            Generate a Windup report for this project
	 * @param monitor
	 *            {@link IProgressMonitor} to report progress to
	 */
	private IStatus generateReport(IProject project, IProgressMonitor monitor) {
		//protect against a null given for the progress monitor
		IProgressMonitor progress;
		if(monitor != null) {
			progress = new SubProgressMonitor(monitor, 1);;
		} else {
			progress = new NullProgressMonitor();
		}
		
		String projectName = project.getName();
		
		//start the task
		progress.beginTask(NLS.bind(Messages.generate_windup_report_for, projectName), IProgressMonitor.UNKNOWN);
		IStatus status = null;
		
		try {
			File inputDir = project.getLocation().toFile();
			IPath outputPath = reportsDir.append(projectName);
			
			File outputDir = outputPath.toFile();
			
			//clear out existing report
			progress.subTask(Messages.removing_old_report);
			FileUtils.delete(outputDir, true);
			
			//wait for the engine to be avaialbe and then generate the report
			progress.subTask(Messages.waiting_for_windup_to_be_avaialbe);
			synchronized(this.windupLock) {
				progress.subTask(NLS.bind(Messages.generate_windup_report_for, projectName));
				
				//generate new report
				WindupService.this.getWindupReportEngine().process(
						inputDir, outputDir);
			}
			
			//notify listeners that a report was just generated
			this.notifyReportGenerated(project);
			
			status = Status.OK_STATUS;
		} catch (IOException e) {
			status = new Status(IStatus.ERROR,
					WindupCorePlugin.PLUGIN_ID,
					NLS.bind(Messages.error_generating_report_for, projectName));
			
			WindupCorePlugin.logError("There was an error generating the Windup report " //$NON-NLS-1$
					+ "for the project " + projectName, e); //$NON-NLS-1$
		} finally {
			//mark the monitor as complete
			progress.done();
		}
		
		return status;
	}
	
	/**
	 * <p>
	 * Determines if a report exists for the {@link IProject} containing the
	 * given {@link IResource}.
	 * </p>
	 * 
	 * @param resource
	 *            determine if a report exists for the {@link IProject}
	 *            containing this {@link IResource}
	 * 
	 * @return <code>true</code> if a report exists for the {@link IProject}
	 *         containing the given {@link IResource}, <code>false</code>
	 *         otherwise.
	 */
	public boolean reportExists(IResource resource) {
		IPath reportPath = getProjectReportPath(resource);
		File reportDir = new File(reportPath.toString());
		
		return reportDir.exists();
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
		
		IPath projectReportPath = getProjectReportPath(resource);
		
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
		
		//determine if the report of the given file exists, if it doesn't return null
		if(reportPath != null) {
			File reportFile = new File(reportPath.toString());
			if(!reportFile.exists()) {
				reportPath = null;
			}
		}
		
		return reportPath;
	}
	
	/**
	 * <p>
	 * Get the Windup report parent directory for the given resource.
	 * </p>
	 * 
	 * @param resource
	 *            get the location of the Windup report parent directory for this
	 *            {@link IResource}
	 * 
	 * @return location of the Windup report parent directory for the given {@link IResource}
	 */
	public IPath getReportParentDirectoryLocation(IResource resource) {
		return getProjectReportPath(resource);
	}
	
	/**
	 * <p>
	 * Registers a {@link IWindupReportListener}.
	 * </p>
	 * 
	 * @param listener {@link IWindupReportListener} to register
	 */
	public void addWindupReportListener(IWindupReportListener listener) {
		this.windupReportListeners.add(listener);
	}
	
	/**
	 * <p>
	 * Removes an already registered {@link IWindupReportListener}.
	 * </p>
	 * 
	 * @param listener {@link IWindupReportListener} to unregister
	 */
	public void removeWindupReportListener(IWindupReportListener listener) {
		this.windupReportListeners.remove(listener);
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
				this.windupEngine = this.getWindupFactory().createWindupEngine(this.getWindupSettings());
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
				this.reportEngine = this.getWindupFactory().createReportEngine(
						this.getWindupSettings(), this.getWindupEngine());
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
	private WindupEnvironment getWindupSettings() {
		WindupEnvironment settings = new WindupEnvironment();
		settings.setSource(true);
		
		return settings;
	}
	
	/**
	 * @return {@link WindupFactory} for interacting with Windup via Furnace
	 */
	private WindupFactory getWindupFactory() {
		FurnaceProvider.INSTANCE.startFurnace();
		try {
			FurnaceService.INSTANCE.waitUntilContainerIsStarted();
		} catch (InterruptedException e) {
			WindupCorePlugin.logError("Could not load Furance", e); //$NON-NLS-1$
		}
		return FurnaceService.INSTANCE.lookup(WindupFactory.class);
	}
	
	/**
	 * <p>
	 * Notifies all of the registered {@link IWindupReportListener} that a
	 * Windup report was just generated for the given {@link IProject}.
	 * </p>
	 * 
	 * @param project
	 *            Notify all registered {@link IWindupReportListener} that a
	 *            Windup report was just generated for this {@link IProject}
	 */
	private void notifyReportGenerated(IProject project) {
		for(IWindupReportListener listener : WindupService.this.windupReportListeners) {
			listener.reportGenerated(project);
		}
	}
	
	/**
	 * <p>
	 * Get the location where the report should be stored for the
	 * {@link IProject} containing the given {@link IResource}
	 * </p>
	 * 
	 * @param resource
	 *            get the location where the report should be stored for the
	 *            {@link IProject} containing this {@link IResource}
	 * 
	 * @return location where the report should be stored for the
	 *         {@link IProject} containing the given {@link IResource}
	 */
	private static IPath getProjectReportPath(IResource resource) {
		return reportsDir.append(resource.getProject().getName());
	}
}