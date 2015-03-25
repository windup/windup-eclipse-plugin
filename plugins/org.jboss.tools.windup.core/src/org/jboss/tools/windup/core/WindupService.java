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
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.jboss.windup.exec.WindupProcessor;
import org.jboss.windup.exec.WindupProgressMonitor;
import org.jboss.windup.exec.configuration.WindupConfiguration;
import org.jboss.windup.graph.GraphContext;
import org.jboss.windup.graph.GraphContextFactory;
import org.jboss.windup.graph.model.report.IgnoredFileRegexModel;
import org.jboss.windup.graph.model.resource.FileModel;
import org.jboss.windup.graph.service.FileService;
import org.jboss.windup.graph.service.GraphService;
import org.jboss.windup.reporting.model.InlineHintModel;
import org.jboss.windup.reporting.service.InlineHintService;
import org.jboss.windup.rules.apps.java.model.WindupJavaConfigurationModel;
import org.jboss.windup.rules.apps.java.service.WindupJavaConfigurationService;

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
	 * List of the subscribed {@link IWindupListener}s.
	 * </p>
	 */
	private List<IWindupListener> windupListeners;
	
	/**
	 * <p>
	 * Map of Windup {@link GraphContext}s to their associated {@link IProject}s.
	 * </p>
	 */
	private Map<IProject, GraphContext> windupGraphContexts;
	
	/**
	 * <p>
	 * Private constructor for singleton instance.
	 * </p>
	 */
	private WindupService() {
		this.windupGraphContexts = new HashMap<>();
		this.windupListeners = new ArrayList<IWindupListener>();
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
	 * TODO: IAN: doc me
	 */
	public Iterable<InlineHintModel> getInlineHints(IResource resource, IProgressMonitor monitor) {
		//InlineHintService and FileService
		
		IProject project = resource.getProject();
		GraphContext context = this.getGraph(project, monitor);
		
		FileService fileService = this.getTypeFromFurnace(FileService.class, monitor);
		fileService.setGraphContext(context);
		FileModel fileModel = fileService.findByPath(resource.getFullPath().toString());
		
		InlineHintService hintService = this.getTypeFromFurnace(InlineHintService.class, monitor);
		return hintService.getHintsForFile(fileModel);
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
	public IStatus generateGraph(IResource resource, IProgressMonitor monitor) {
		return this.generateGraph(new IProject[] {resource.getProject()}, monitor);
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
	public IStatus generateGraph(IProject[] projects, IProgressMonitor monitor) {
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
					status = this.generateGraph(project, progress);
					
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
	private IStatus generateGraph(IProject project, IProgressMonitor monitor) {
		//protect against a null given for the progress monitor
		IProgressMonitor progress;
		if(monitor != null) {
			progress = new SubProgressMonitor(monitor, 1);;
		} else {
			progress = new NullProgressMonitor();
		}
		
		String projectName = project.getName();
		
		//start the task
		progress.beginTask(NLS.bind(Messages.generate_windup_graph_for, projectName), IProgressMonitor.UNKNOWN);
		IStatus status = null;
		
		try {
			File inputDir = project.getLocation().toFile();
			IPath outputPath = getProjectReportPath(project);
			File outputDir = outputPath.toFile();
			
			//clear out existing report
			progress.subTask(Messages.removing_old_report);
			FileUtils.delete(outputDir, true);
	
			//set up monitoring
			progress.subTask(Messages.get_windup_graph_context_factory);
			
			
			//set configuration
			WindupConfiguration windupConfiguration = this.getServiceFromFurnace(WindupConfiguration.class, progress);
			windupConfiguration.setInputPath(inputDir.toPath());
			windupConfiguration.setOutputDirectory(outputDir.toPath());
			
			//set up graph context factory
			GraphContextFactory graphContextFactory = this.getServiceFromFurnace(GraphContextFactory.class, progress);
			Path graphPath = windupConfiguration.getOutputDirectory().resolve("graph"); //$NON-NLS-1$
			
			//create new graph
			progress.subTask(NLS.bind(Messages.generate_windup_graph_for, projectName));
			WindupProgressMonitor windupProgressMonitor = new WindupProgressMonitorAdapter(progress);
			GraphContext graphContext = graphContextFactory.create(graphPath);
            windupConfiguration
                        .setProgressMonitor(windupProgressMonitor)
                        .setGraphContext(graphContext);
            
            //set up ignore rules for the graph
            GraphService<IgnoredFileRegexModel> graphService = this.getTypeFromFurnace(GraphService.class, progress);
			graphService.setGraphContext(graphContext);
			graphService.setType(IgnoredFileRegexModel.class);
			IgnoredFileRegexModel ignored = graphService.create();
			ignored.setRegex(".*\\.class"); //$NON-NLS-1$
			WindupJavaConfigurationService windupJavaConfigurationService = this.getTypeFromFurnace(WindupJavaConfigurationService.class, progress);
			WindupJavaConfigurationModel javaCfg = windupJavaConfigurationService.getJavaConfigurationModel(graphContext);
			javaCfg.addIgnoredFileRegex(ignored);
			
			//generate the graph
			this.getServiceFromFurnace(WindupProcessor.class, progress).execute(windupConfiguration);
			
			//store the new graph
			this.setGraph(project, graphContext);
			
			//notify listeners that a graph was just generated
			this.notifyGraphGenerated(project);
			
			status = Status.OK_STATUS;
		} finally {
			//mark the monitor as complete
			progress.done();
		}
		
		return status;
	}

	/**
	 * <p>
	 * Sets the current {@link GraphContext} for a given {@link IProject}. If a {@link GraphContext}
	 * already exists for the given {@link IProject} then the old {@link GraphContext} will be cleaned up
	 * before the new one is set.
	 * </p>
	 * 
	 * @param project {@link IProject} to associated the given {@link GraphContext} with
	 * @param graphContext {@link GraphContext} to associate with the given {@link IProject}
	 */
	private void setGraph(IProject project, GraphContext graphContext) {
		GraphContext oldContext = this.windupGraphContexts.get(project);
		if(oldContext != null) {
			try {
				oldContext.close();
				FileUtils.delete(oldContext.getGraphDirectory().toFile(), true);
			} catch (IOException e) {
				WindupCorePlugin.logError(
					"Error deteling old Windup GraphContext, '" + //$NON-NLS-1$
					oldContext.getGraphDirectory() +
					",' for project '" //$NON-NLS-1$
					+ project.getName() + "'.", e); //$NON-NLS-1$
			}
		}
		this.windupGraphContexts.put(project, graphContext);
	}
	
	private GraphContext getGraph(IProject project, IProgressMonitor monitor) {
		GraphContext context = this.windupGraphContexts.get(project);
		
		if(context == null) {
			this.generateGraph(project, monitor);
			context = this.windupGraphContexts.get(project);
		}
		
		return context;
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
			
			/* if selected resource is the project then get the Windup
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
	 * Registers a {@link IWindupListener}.
	 * </p>
	 * 
	 * @param listener {@link IWindupListener} to register
	 */
	public void addWindupListener(IWindupListener listener) {
		this.windupListeners.add(listener);
	}
	
	/**
	 * <p>
	 * Removes an already registered {@link IWindupListener}.
	 * </p>
	 * 
	 * @param listener {@link IWindupListener} to unregister
	 */
	public void removeWindupListener(IWindupListener listener) {
		this.windupListeners.remove(listener);
	}
	
	/**
	 * <p>
	 * Notifies all of the registered {@link IWindupListener} that a
	 * Windup report was just generated for the given {@link IProject}.
	 * </p>
	 * 
	 * @param project
	 *            Notify all registered {@link IWindupListener} that a
	 *            Windup report was just generated for this {@link IProject}
	 */
	private void notifyGraphGenerated(IProject project) {
		for(IWindupListener listener : WindupService.this.windupListeners) {
			listener.graphGenerated(project);
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
	
	/**
	 * TODO: DOC ME
	 * 
	 * @param monitor
	 */
	private void waitForFurnace(IProgressMonitor monitor) {
		//protect against a null given for the progress monitor
		IProgressMonitor progress;
		if (monitor != null) {
			progress = new SubProgressMonitor(monitor, 1);
			;
		} else {
			progress = new NullProgressMonitor();
		}

		// start the task
		progress.beginTask(Messages.waiting_for_furnace,IProgressMonitor.UNKNOWN);

		FurnaceProvider.INSTANCE.startFurnace();
		try {
			FurnaceService.INSTANCE.waitUntilContainerIsStarted();
		} catch (InterruptedException e) {
			WindupCorePlugin.logError("Could not load Furance", e); //$NON-NLS-1$
		}
		
		progress.done();
	}
	
	/**
	 * TODO: DOC ME
	 * 
	 * @param clazz
	 * @return
	 */
	private <T> T getServiceFromFurnace(Class<T> clazz, IProgressMonitor monitor) {
		this.waitForFurnace(monitor);
		
		return FurnaceService.INSTANCE.lookup(clazz);
	}
	
	/**
	 * TODO: DOC ME
	 * 
	 * @param clazz
	 * @return
	 */
	private <T> T getTypeFromFurnace(Class<T> clazz, IProgressMonitor monitor) {
		this.waitForFurnace(monitor);
		
		return FurnaceService.INSTANCE.lookupType(clazz);
	}
}