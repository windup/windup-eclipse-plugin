/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.core.services;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.osgi.util.NLS;
import org.jboss.tools.forge.core.furnace.FurnaceProvider;
import org.jboss.tools.forge.core.furnace.FurnaceService;
import org.jboss.tools.windup.core.IWindupListener;
import org.jboss.tools.windup.core.WindupCorePlugin;
import org.jboss.tools.windup.core.WindupProgressMonitorAdapter;
import org.jboss.tools.windup.core.internal.Messages;
import org.jboss.tools.windup.core.utils.FileUtils;
import org.jboss.tools.windup.core.utils.ResourceUtils;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.model.domain.WindupConstants;
import org.jboss.tools.windup.runtime.WindupRuntimePlugin;
import org.jboss.tools.windup.windup.ConfigurationElement;
import org.jboss.windup.exec.WindupProgressMonitor;
import org.jboss.windup.exec.configuration.options.TargetOption;
import org.jboss.windup.rules.apps.java.config.SourceModeOption;
import org.jboss.windup.tooling.ExecutionBuilder;
import org.jboss.windup.tooling.ExecutionResults;
import org.jboss.windup.tooling.data.Classification;
import org.jboss.windup.tooling.data.Hint;
import org.jboss.windup.tooling.data.ReportLink;

import com.google.common.collect.Lists;

/**
 * <p>
 * Service used to perform Windup operations inside of Eclipse.
 * </p>
 */
@Singleton
@Creatable
public class WindupService
{
    private static final String PROJECT_REPORT_HOME_PAGE = "index.html"; //$NON-NLS-1$

    private List<IWindupListener> windupListeners = new ArrayList<IWindupListener>();
    private Map<IProject, ExecutionResults> projectToResults = new HashMap<>();
    
    @Inject private IEventBroker broker;
    @Inject private ModelService modelService;

    /**
     * Returns an {@link Iterable} with all {@link Hint}s returned by Windup during the last run.
     * 
     * NOTE: This will return an empty list if Windup has not yet been run on this project.
     */
    public Iterable<Hint> getHints(IResource resource)
    {
        ExecutionResults results = projectToResults.get(resource.getProject());
        if (results == null)
            return Collections.emptyList();
        else
            return results.getHints();
    }

    /**
     * Returns an {@link Iterable} with all {@link Classification}s returned by Windup during the last run.
     * 
     * NOTE: This will return an empty list if Windup has not yet been run on this project.
     */
    public Iterable<Classification> getClassifications(IResource resource, IProgressMonitor monitor)
    {
        ExecutionResults results = projectToResults.get(resource.getProject());
        if (results == null)
            return Collections.emptyList();
        else
            return results.getClassifications();
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
     * @param projects Generate a Windup reports for these {@link IProject}s
     * @param monitor {@link IProgressMonitor} to report progress to
     */
    public IStatus generateGraph(IProject[] projects, IProgressMonitor monitor)
    {
        // protect against a null given for the progress monitor
        IProgressMonitor progress;
        if (monitor != null)
        {
            progress = monitor;
        }
        else
        {
            progress = new NullProgressMonitor();
        }

        // start the task
        progress.beginTask(Messages.generate_windup_reports, projects.length);
        IStatus status = null;

        try
        {
            // for each project generate a report
            for (IProject project : projects)
            {
                // if not canceled, continue generating reports for the given projects
                if (!progress.isCanceled())
                {
                    status = this.generateGraph(project, progress);

                    // if not an okay status stop generating reports
                    if (!status.equals(Status.OK_STATUS))
                    {
                        break;
                    }
                }
                else
                {
                    status = Status.CANCEL_STATUS;
                    break;
                }
            }
        }
        finally
        {
            progress.done();
        }

        return status;
    }
    
    public IStatus generateGraph(IProject project) {
    	return generateGraph(project, null);
    }
    
    public IStatus generateGraph(ConfigurationElement configuration, IProgressMonitor progress) {
        progress.subTask(Messages.generate_windup_reports);
        IStatus status = null;

        try
        {
        	progress.subTask(Messages.startingWindup);
            WindupProgressMonitor windupProgressMonitor = new WindupProgressMonitorAdapter(progress);
            ExecutionBuilder execBuilder = getServiceFromFurnace(ExecutionBuilder.class, progress);
        	
            IPath outputPath = modelService.getGeneratedReportBaseLocation(configuration);
            File outputDir = outputPath.toFile();

            // clear out existing report
            progress.subTask(Messages.removing_old_report);
            FileUtils.delete(outputDir, true);
            
            progress.subTask(Messages.generate_windup_reports);

            /*ExecutionBuilderSetOptions options = execBuilder.begin(WindupRuntimePlugin.findWindupHome().toPath())
            	.setInput(getInputPath(configuration.getInputs().get(0)))
            	.setOutput(outputDir.toPath())
            	.setProgressMonitor(windupProgressMonitor);
            
            options.setOption(SourceModeOption.NAME, true);
        	options.setOption(TargetOption.NAME, Lists.newArrayList("eap"));
            	
            for (int i = 1; i < configuration.getInputs().size(); i++) {
            	Input input = configuration.getInputs().get(i);
            	options.setOption(InputPathOption.NAME, getInputPath(input));
            }
            
            try {
            	ExecutionResults results = options.ignore("\\.class$").execute();
            	WindupResult windupResult = WindupFactory.eINSTANCE.createWindupResult();
                windupResult.setExecutionResults(results);
                configuration.setWindupResult(windupResult);
            } catch (Exception e) {
            	WindupCorePlugin.log(e);
            }*/

            ExecutionResults results = execBuilder.begin(WindupRuntimePlugin.findWindupHome().toPath())
                    .setInput(ResourceUtils.computePath(configuration.getInputs().get(0).getUri()))
                    .setOutput(outputDir.toPath())
                    .setProgressMonitor(windupProgressMonitor)
                    .setOption(SourceModeOption.NAME, true)
                    .setOption(TargetOption.NAME, Lists.newArrayList("eap"))
                    .ignore("\\.class$")
                    .execute();
            
            modelService.populateConfiguration(configuration, results);
            broker.post(WindupConstants.WINDUP_RUN_COMPLETED, configuration);
            status = Status.OK_STATUS;
        }
        catch (Exception e)
        {
        	System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        finally
        {
            // mark the monitor as complete
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
     * <b>NOTE:</b> This can be a long running operation, it should be run in a Job.
     * </p>
     * 
     * @param project Generate a Windup report for this project
     * @param monitor {@link IProgressMonitor} to report progress to
     */
    public IStatus generateGraph(IProject project, IProgressMonitor monitor)
    {
        // protect against a null given for the progress monitor
        IProgressMonitor progress;
        if (monitor != null)
        {
            progress = new SubProgressMonitor(monitor, 1);
        }
        else
        {
            progress = new NullProgressMonitor();
        }

        String projectName = project.getName();

        // start the task
        progress.beginTask(NLS.bind(Messages.generate_windup_graph_for, projectName), IProgressMonitor.UNKNOWN);
        IStatus status = null;

        try
        {
            File inputDir = project.getLocation().toFile();
            IPath outputPath = getProjectReportPath(project);
            File outputDir = outputPath.toFile();

            // clear out existing report
            progress.subTask(Messages.removing_old_report);
            FileUtils.delete(outputDir, true);

            // create new graph
            progress.subTask(NLS.bind(Messages.generate_windup_graph_for, projectName));

            WindupProgressMonitor windupProgressMonitor = new WindupProgressMonitorAdapter(progress);
            ExecutionBuilder execBuilder = getServiceFromFurnace(ExecutionBuilder.class, progress);
            
            /*ExecutionResults results = execBuilder.begin(WindupRuntimePlugin.findWindupHome().toPath())
                        .setInput(inputDir.toPath())
                        .setOutput(outputDir.toPath())
                        .setProgressMonitor(windupProgressMonitor)
                        .ignore("\\.class$")
                        .execute();*/
            
            ExecutionResults results = execBuilder.begin(WindupRuntimePlugin.findWindupHome().toPath())
                    .setInput(inputDir.toPath())
                    .setOutput(outputDir.toPath())
                    .setProgressMonitor(windupProgressMonitor)
                    .setOption(SourceModeOption.NAME, true)
                    .setOption(TargetOption.NAME, Lists.newArrayList("eap"))
                    .ignore("\\.class$")
                    .execute();
            
            projectToResults.put(project, results);

            // notify listeners that a graph was just generated
            this.notifyGraphGenerated(project);
            status = Status.OK_STATUS;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            // mark the monitor as complete
            progress.done();
        }

        return status;
    }

    /**
     * <p>
     * Determines if a report exists for the {@link IProject} containing the given {@link IResource}.
     * </p>
     * 
     * @param resource determine if a report exists for the {@link IProject} containing this {@link IResource}
     * 
     * @return <code>true</code> if a report exists for the {@link IProject} containing the given {@link IResource}, <code>false</code> otherwise.
     */
    public boolean reportExists(IResource resource)
    {
        IPath reportPath = getProjectReportPath(resource);
        File reportDir = new File(reportPath.toString());

        return reportDir.exists();
    }

    /**
     * <p>
     * Get the Windup report for the given resource.
     * </p>
     * 
     * @param resource get the location of the Windup report for this {@link IResource}
     * 
     * @return location of the Windup report for the given {@link IResource}
     */
    public IPath getReportLocation(IResource resource)
    {

        IPath projectReportPath = getProjectReportPath(resource);

        IPath reportPath = null;
        switch (resource.getType())
        {
        // if selected resource is a file get the file specific report page
        case IResource.FILE:
        {
            File resourceAsFile = resource.getLocation().toFile().getAbsoluteFile();
            ExecutionResults executionResults = projectToResults.get(resource.getProject());
            if (executionResults == null)
                break;
            for (ReportLink reportLink : executionResults.getReportLinks())
            {
                if (resourceAsFile.equals(reportLink.getInputFile()))
                {
                    File reportFile = reportLink.getReportFile();
                    Path projectPath = resource.getProject().getLocation().toFile().toPath();
                    Path reportFileRelativeToProject = projectPath.relativize(reportFile.toPath());
                    IPath projectLocation = resource.getProject().getLocation();
                    reportPath = projectLocation.append(reportFileRelativeToProject.toString());
                    break;
                }
            }
            break;
        }

        /*
         * if selected resource is the project then get the Windup report home page for that project
         */
        case IResource.PROJECT:
        {
            reportPath = projectReportPath.append(PROJECT_REPORT_HOME_PAGE);
            break;
        }

        default:
        {
            break;
        }
        }

        // determine if the report of the given file exists, if it doesn't return null
        if (reportPath != null)
        {
            File reportFile = new File(reportPath.toString());
            if (!reportFile.exists())
            {
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
     * @param resource get the location of the Windup report parent directory for this {@link IResource}
     * 
     * @return location of the Windup report parent directory for the given {@link IResource}
     */
    public IPath getReportParentDirectoryLocation(IResource resource)
    {
        return getProjectReportPath(resource);
    }

	/**
     * <p>
     * Registers a {@link IWindupListener}.
     * </p>
     * 
     * @param listener {@link IWindupListener} to register
     */
    public void addWindupListener(IWindupListener listener)
    {
        this.windupListeners.add(listener);
    }

    /**
     * <p>
     * Removes an already registered {@link IWindupListener}.
     * </p>
     * 
     * @param listener {@link IWindupListener} to unregister
     */
    public void removeWindupListener(IWindupListener listener)
    {
        this.windupListeners.remove(listener);
    }

    /**
     * <p>
     * Notifies all of the registered {@link IWindupListener} that a Windup report was just generated for the given {@link IProject}.
     * </p>
     * 
     * @param project Notify all registered {@link IWindupListener} that a Windup report was just generated for this {@link IProject}
     */
    private void notifyGraphGenerated(IProject project)
    {
        for (IWindupListener listener : WindupService.this.windupListeners)
        {
            listener.graphGenerated(project);
        }
    }

    public IPath getReportPath(ConfigurationElement configuration) {
    	return ModelService.reportsDir.append(configuration.getName()).append(PROJECT_REPORT_HOME_PAGE);
    }
    
    /**
     * <p>
     * Get the location where the report should be stored for the {@link IProject} containing the given {@link IResource}
     * </p>
     * 
     * @param resource get the location where the report should be stored for the {@link IProject} containing this {@link IResource}
     * 
     * @return location where the report should be stored for the {@link IProject} containing the given {@link IResource}
     */
    private IPath getProjectReportPath(IResource resource)
    {
        return ModelService.reportsDir.append(resource.getProject().getName());
    }

    /**
     * TODO: DOC ME
     * 
     * @param monitor
     */
    private void waitForFurnace(IProgressMonitor monitor)
    {
        // protect against a null given for the progress monitor
        IProgressMonitor progress;
        if (monitor != null)
        {
            progress = new SubProgressMonitor(monitor, 1);
        }
        else
        {
            progress = new NullProgressMonitor();
        }

        // start the task
        progress.beginTask(Messages.waiting_for_furnace, IProgressMonitor.UNKNOWN);

        FurnaceProvider.INSTANCE.startFurnace();
        try
        {
            FurnaceService.INSTANCE.waitUntilContainerIsStarted();
        }
        catch (InterruptedException e)
        {
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
    private <T> T getServiceFromFurnace(Class<T> clazz, IProgressMonitor monitor)
    {
        this.waitForFurnace(monitor);

        return FurnaceService.INSTANCE.lookupImported(clazz).get();
    }
}