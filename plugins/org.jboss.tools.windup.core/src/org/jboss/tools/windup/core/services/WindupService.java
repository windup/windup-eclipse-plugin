/*******************************************************************************
 * Copyright (c) 2018 Red Hat, Inc.
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
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.osgi.util.NLS;
import org.jboss.tools.windup.core.IWindupListener;
import org.jboss.tools.windup.core.WindupCorePlugin;
import org.jboss.tools.windup.core.WindupProgressMonitorAdapter;
import org.jboss.tools.windup.core.internal.Messages;
import org.jboss.tools.windup.core.utils.FileUtils;
import org.jboss.tools.windup.model.OptionFacades;
import org.jboss.tools.windup.model.OptionFacades.OptionTypeFacade;
import org.jboss.tools.windup.model.OptionFacades.OptionsFacadeManager;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.model.domain.WorkspaceResourceUtils;
import org.jboss.tools.windup.runtime.WindupRmiClient;
import org.jboss.tools.windup.runtime.WindupRuntimePlugin;
import org.jboss.tools.windup.runtime.options.IOptionKeys;
import org.jboss.tools.windup.runtime.options.OptionDescription;
import org.jboss.tools.windup.windup.ConfigurationElement;
import org.jboss.tools.windup.windup.Input;
import org.jboss.tools.windup.windup.MigrationPath;
import org.jboss.tools.windup.windup.Pair;
import org.jboss.windup.tooling.ExecutionBuilder;
import org.jboss.windup.tooling.ExecutionResults;
import org.jboss.windup.tooling.data.Classification;
import org.jboss.windup.tooling.data.Hint;
import org.jboss.windup.tooling.data.ReportLink;

import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

/**
 * <p>
 * Service used to perform Windup operations inside of Eclipse.
 * </p>
 */
@Singleton
@Creatable
public class WindupService
{
    private List<IWindupListener> windupListeners = new ArrayList<IWindupListener>();
    private Map<IProject, ExecutionResults> projectToResults = new HashMap<>();
    
    @Inject private ModelService modelService;
    @Inject private WindupRmiClient windupClient; 
    
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
    
    public IStatus generateGraph(ConfigurationElement configuration, IProgressMonitor progress) {
    	
	    	progress.subTask(Messages.startingWindup);
	    	modelService.synch(configuration);
	    	IPath basePath = modelService.getGeneratedReportsBaseLocation(configuration);
	    	File baseOutputDir = basePath.toFile();
	
	    	progress.subTask(Messages.removing_old_report);
	    FileUtils.delete(baseOutputDir, true);
	    IStatus status = null;

        try {
	        	for (Input input : configuration.getInputs()) {
                ExecutionBuilder execBuilder = windupClient.getExecutionBuilder();
                execBuilder.clear();
            	
                Path projectPath = WorkspaceResourceUtils.computePath(input.getUri());
                progress.beginTask(NLS.bind(Messages.generate_windup_graph_for, input.getName()), IProgressMonitor.UNKNOWN);
                
                IPath outputPath = modelService.getGeneratedReportBaseLocation(configuration, input);
                execBuilder.setWindupHome(WindupRuntimePlugin.computeWindupHome().toString());
                execBuilder.setInput(projectPath.toString());
                execBuilder.setOutput(outputPath.toFile().toPath().toString());
                execBuilder.setProgressMonitor(new WindupProgressMonitorAdapter(progress));
                execBuilder.setOption(IOptionKeys.sourceModeOption, true);
                execBuilder.setOption(IOptionKeys.skipReportsRenderingOption, !configuration.isGenerateReport());
            		execBuilder.ignore("\\.class$");

            		List<String> sources = Lists.newArrayList();
            		List<String> targets = Lists.newArrayList();
            		
                MigrationPath path = configuration.getMigrationPath();
                if (path.getSource() != null) {
                		sources.add(path.getSource().getId());
                }
                if (path.getTarget() != null) {
                		String versionRange = path.getTarget().getVersionRange();
                		String versionRangeSuffix = !Strings.isNullOrEmpty(versionRange) ? ":" + versionRange : "";
                		String value = path.getTarget().getId() + versionRangeSuffix;
                		targets.add(value);
                }
                if (!configuration.getPackages().isEmpty()) {
                		execBuilder.setOption(IOptionKeys.scanPackagesOption, Lists.newArrayList(configuration.getPackages()));
                }
                modelService.cleanCustomRuleRepositories(configuration);
                if (!configuration.getUserRulesDirectories().isEmpty()) {
	                	List<File> customRules = Lists.newArrayList();
	                	configuration.getUserRulesDirectories().stream().forEach(d -> customRules.add(new File(d)));
	                	execBuilder.setOption(IOptionKeys.userRulesDirectoryOption, customRules);
	                	//execBuilder.addUserRulesPath(file.getParentFile().toString());
                }
                
                OptionsFacadeManager facadeMgr = modelService.getOptionFacadeManager();
                
                Multimap<String, String> optionMap = ArrayListMultimap.create();
                for (Pair pair : configuration.getOptions()) {
	                	String name = pair.getKey();
	                	String value = pair.getValue();
	                	optionMap.put(name, value);
                }
                
                for (String name : optionMap.keySet()) {
	                	List<String> values = (List<String>)optionMap.get(name);
	                	OptionDescription option = facadeMgr.findOptionDescription(name);
	                	OptionTypeFacade<?> typeFacade = facadeMgr.getFacade(option, OptionTypeFacade.class);
	                	if (OptionFacades.isSingleValued(option)) {
	                		Object optionValue = typeFacade.newInstance(values.get(0));
	                		execBuilder.setOption(name, optionValue);
	                	}
	                	else {
	                		List<?> optionValues = typeFacade.newInstance(values);
	                		if (name.equals(IOptionKeys.targetOption)) {
	                			targets.addAll((List<String>)optionValues);
	                		}
	                		else if (name.equals(IOptionKeys.sourceOption)) {
	                			sources.addAll((List<String>)optionValues);
	                		}
	                		else {
	                			execBuilder.setOption(name, optionValues);
	                		}
	                	}
                }
                
                if (!targets.isEmpty()) {
                		execBuilder.setOption(IOptionKeys.targetOption, targets);
                }
                if (!sources.isEmpty()) {
                		execBuilder.setOption(IOptionKeys.sourceOption, sources);
                }
                
                WindupCorePlugin.logInfo("WindupService is executing the ExecutionBuilder"); //$NON-NLS-1$
                WindupCorePlugin.logInfo("Using input: " + projectPath.toString()); //$NON-NLS-1$
                WindupCorePlugin.logInfo("Using output: " + outputPath.toFile().toPath().toString()); //$NON-NLS-1$
                WindupCorePlugin.logInfo("Using sources: " + sources); //$NON-NLS-1$
                WindupCorePlugin.logInfo("Using targets: " + targets); //$NON-NLS-1$
                ExecutionResults results = execBuilder.execute();
                WindupCorePlugin.logInfo("ExecutionBuilder has returned the Windup results"); //$NON-NLS-1$
                modelService.populateConfiguration(configuration, input, outputPath, results);
	        	}
	        	
	        	modelService.save();
	        status = Status.OK_STATUS;
        }
        catch (Exception e)
        {
        		WindupCorePlugin.log(e);
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
	 *            get the location of the Windup report for this {@link IResource}
	 * 
	 * @return location of the Windup report for the given {@link IResource}
	 */
	public IPath getReportLocation(IResource resource) {
		IPath projectReportPath = getProjectReportPath(resource);

		IPath reportPath = null;
		switch (resource.getType()) {
		// if selected resource is a file get the file specific report page
		case IResource.FILE: {
			File resourceAsFile = resource.getLocation().toFile().getAbsoluteFile();
			ExecutionResults executionResults = projectToResults.get(resource.getProject());
			if (executionResults == null)
				break;
			for (ReportLink reportLink : executionResults.getReportLinks()) {
				if (resourceAsFile.equals(reportLink.getInputFile())) {
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
		 * if selected resource is the project then get the Windup report home page for
		 * that project
		 */
		case IResource.PROJECT: {
			reportPath = projectReportPath.append(ModelService.PROJECT_REPORT_HOME_PAGE);
			break;
		}

		default: {
			break;
		}
		}

		// determine if the report of the given file exists, if it doesn't return null
		if (reportPath != null) {
			File reportFile = new File(reportPath.toString());
			if (!reportFile.exists()) {
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
	 * @return location of the Windup report parent directory for the given
	 *         {@link IResource}
	 */
	public IPath getReportParentDirectoryLocation(IResource resource) {
		return getProjectReportPath(resource);
	}

	/**
	 * <p>
	 * Registers a {@link IWindupListener}.
	 * </p>
	 * 
	 * @param listener
	 *            {@link IWindupListener} to register
	 */
	public void addWindupListener(IWindupListener listener) {
		this.windupListeners.add(listener);
	}

	/**
	 * <p>
	 * Removes an already registered {@link IWindupListener}.
	 * </p>
	 * 
	 * @param listener
	 *            {@link IWindupListener} to unregister
	 */
	public void removeWindupListener(IWindupListener listener) {
		this.windupListeners.remove(listener);
	}

	/**
	 * <p>
	 * Get the location where the report should be stored for the {@link IProject}
	 * containing the given {@link IResource}
	 * </p>
	 * 
	 * @param resource
	 *            get the location where the report should be stored for the
	 *            {@link IProject} containing this {@link IResource}
	 * 
	 * @return location where the report should be stored for the {@link IProject}
	 *         containing the given {@link IResource}
	 */
	private IPath getProjectReportPath(IResource resource) {
		return ModelService.reportsDir.append(resource.getProject().getName());
	}
}