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
package org.jboss.tools.windup.core.services;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.jboss.tools.windup.core.IWindupListener;
import org.jboss.tools.windup.core.WindupCorePlugin;
import org.jboss.tools.windup.core.WindupProgressMonitorAdapter;
import org.jboss.tools.windup.core.internal.Messages;
import org.jboss.tools.windup.core.utils.FileUtils;
import org.jboss.tools.windup.model.OptionFacades;
import org.jboss.tools.windup.model.OptionFacades.OptionTypeFacade;
import org.jboss.tools.windup.model.OptionFacades.OptionsFacadeManager;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.runtime.WindupRmiClient;
import org.jboss.tools.windup.runtime.WindupRuntimePlugin;
import org.jboss.tools.windup.runtime.options.IOptionKeys;
import org.jboss.tools.windup.runtime.options.OptionDescription;
import org.jboss.tools.windup.windup.ConfigurationElement;
import org.jboss.tools.windup.windup.IgnorePattern;
import org.jboss.tools.windup.windup.MigrationPath;
import org.jboss.tools.windup.windup.Pair;
import org.jboss.tools.windup.windup.Report;
import org.jboss.tools.windup.windup.WindupFactory;
import org.jboss.windup.tooling.ExecutionBuilder;
import org.jboss.windup.tooling.ExecutionResults;
import org.jboss.windup.tooling.data.Classification;
import org.jboss.windup.tooling.data.Hint;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
    
    private ConfigurationElement recentConfiguration;
    
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
    
    @SuppressWarnings("unchecked")
	public IStatus generateGraph(ConfigurationElement configuration, IProgressMonitor progress) {
    	
		progress.subTask(Messages.startingWindup);
		modelService.synch(configuration);
		
		IPath basePath = org.eclipse.core.runtime.Path.fromOSString(configuration.getOutputLocation());
		File baseOutputDir = basePath.toFile();
	
		progress.subTask(Messages.removing_old_report);
	    FileUtils.delete(baseOutputDir, true);
	    
	    progress.subTask(Messages.writingUserIgnoreFile);
	    
		writeUserIgnoreFile(configuration);
    		
	    IStatus status = null;

        try {
            ExecutionBuilder execBuilder = windupClient.getExecutionBuilder();
            execBuilder.clear();
        	
            Set<String> input = configuration.getInputs().stream().map(
            		i -> i.getLocation()).collect(Collectors.toSet());
            String outputDir = configuration.getOutputLocation();
            
            execBuilder.setWindupHome(WindupRuntimePlugin.computeWindupHome().toString());
            execBuilder.setOutput(outputDir);
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
					if (IOptionKeys.targetOption.equals(name)) {
						targets.addAll((List<String>)optionValues);
					}
					else if (IOptionKeys.sourceOption.equals(name)) {
						sources.addAll((List<String>)optionValues);
					}
					else if (IOptionKeys.inputOption.equals(name)) {
						List<Path> paths = (List<Path>)optionValues;
						paths.forEach(p -> input.add(p.toString()));
						syncInput(configuration, paths);
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
            
            if (!optionMap.containsKey(IOptionKeys.userIgnorePathOption)) {
				execBuilder.setOption(IOptionKeys.userIgnorePathOption, 
				modelService.getDefaultUserIgnore());
				WindupCorePlugin.logInfo("Using ignore file: " + modelService.getDefaultUserIgnore().getAbsolutePath());
            }
            
            execBuilder.setInput(input);
            
            WindupCorePlugin.logInfo("WindupService is executing the ExecutionBuilder"); //$NON-NLS-1$
            WindupCorePlugin.logInfo("Using input: " + input); //$NON-NLS-1$
            WindupCorePlugin.logInfo("Using output: " + outputDir); //$NON-NLS-1$
            WindupCorePlugin.logInfo("Using sources: " + sources); //$NON-NLS-1$
            WindupCorePlugin.logInfo("Using targets: " + targets); //$NON-NLS-1$
            ExecutionResults results = execBuilder.execute();
            WindupCorePlugin.logInfo("ExecutionBuilder has returned the Windup results"); //$NON-NLS-1$
            
            this.recentConfiguration = configuration;
            modelService.populateConfiguration(configuration, results);
	        	
			modelService.save();
	        status = Status.OK_STATUS;
        }
        catch (Exception e) {
			status = new Status(Status.ERROR, WindupCorePlugin.PLUGIN_ID, e.getMessage());
			WindupCorePlugin.log(e);
        }
        finally {
            // mark the monitor as complete
            progress.done();
        }

        return status;
    }
    
    private void syncInput(ConfigurationElement configuration, List<Path> paths) {
    		for (Path path : paths) {
    			boolean exists = configuration.getInputs().stream().filter(input -> {
    				return Objects.equal(input.getLocation(), path.toString());
    			}).findFirst().isPresent();
    			if (!exists) {
    				modelService.createInput(configuration, Lists.newArrayList(path.toString()));
    			}
    		}
    }
    
    private void syncIgnoreWithConfig(ConfigurationElement configuration, List<String> markedForDeletion) {
		Optional<Pair> optional = configuration.getOptions().stream().filter(option -> { 
			return Objects.equal(option.getKey(), IOptionKeys.userIgnorePathOption);
		}).findFirst();
		File ignoreFile = null;
		if (optional.isPresent()) {
			ignoreFile = new File(optional.get().getValue());
		} else {
			ignoreFile = modelService.getDefaultUserIgnore();
		}
		doSyncIgnoreWithConfig(configuration, ignoreFile, markedForDeletion);
	}
	
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void doSyncIgnoreWithConfig(ConfigurationElement configuration, File ignoreFile, 
    		List<String> markedForDeletion) {
		Map<String, IgnorePattern> defaultPatterns = generateDefaultPatterns(configuration);
		Map<String, IgnorePattern> existingPatterns = Maps.newHashMap();
		configuration.getIgnorePatterns().forEach(p -> existingPatterns.put(p.getPattern(), p));
		List<IgnorePattern> copy = new ArrayList(configuration.getIgnorePatterns());
		configuration.getIgnorePatterns().clear();
		
		if (ignoreFile.exists()) {
			List<String> lines = null;
			try {
				lines = org.apache.commons.io.FileUtils.readLines(ignoreFile);
			}
			catch (IOException e) {
				WindupCorePlugin.log(e);
				MessageDialog.openError(Display.getDefault().getActiveShell(),
	    				"User ignore file error", 
	    				"Error while reading user ignore file - " + ignoreFile.getAbsolutePath());
			}
			if (lines != null) {
				for (String line : lines) {
					if (Strings.isNullOrEmpty(line)) continue;
					if (markedForDeletion.contains(line)) continue;
					IgnorePattern pattern = WindupFactory.eINSTANCE.createIgnorePattern();
					pattern.setEnabled(false);
					if (defaultPatterns.containsKey(line)) {
						pattern.setEnabled(defaultPatterns.get(line).isEnabled());
						defaultPatterns.remove(line);
					}
					if (existingPatterns.containsKey(line)) {
						copy.remove(existingPatterns.get(line));
						pattern.setEnabled(existingPatterns.get(line).isEnabled());
					}
					pattern.setPattern(line);
					configuration.getIgnorePatterns().add(pattern);
				}
			}
		}
		for (IgnorePattern pattern : copy) {
			configuration.getIgnorePatterns().add(pattern);
		}
		for (IgnorePattern pattern : defaultPatterns.values()) {
			// TODO: We need to handle if a defaultPattern was removed by user.
			// We'll need to add a list of removedDefaultPatterns to the configuration at some point
			// to track this.
			if (!existingPatterns.containsKey(pattern.getPattern())) { 
				configuration.getIgnorePatterns().add(pattern);
			}
		}
	}
	
    private void writeUserIgnoreFile(ConfigurationElement configuration) {
		Optional<Pair> optional = configuration.getOptions().stream().filter(option -> { 
			return Objects.equal(option.getKey(), IOptionKeys.userIgnorePathOption);
		}).findFirst();
		if (optional.isPresent()) {
			doWriteUserIgnoreFile(configuration, new File(optional.get().getValue()));
			
		} else {
			doWriteUserIgnoreFile(configuration, modelService.getDefaultUserIgnore());
		}
    }
    
    private void doWriteUserIgnoreFile(ConfigurationElement configuration, File ignoreFile) {
		if (!configuration.getIgnorePatterns().isEmpty()) {
			try {
				PrintWriter writer = new PrintWriter(ignoreFile);
				for (IgnorePattern pattern : configuration.getIgnorePatterns()) {
					if (pattern.isEnabled()) {
						writer.write(pattern.getPattern());
						writer.println();
					}
				}
				writer.close();
			} catch (IOException e) {
				WindupCorePlugin.log(e);
				MessageDialog.openError(Display.getDefault().getActiveShell(),
	    				"User ignore file error", 
	    				"Error while writing user ignore file.");
			}
		}
    }
    
	private Map<String, IgnorePattern> generateDefaultPatterns(ConfigurationElement configuration) {
		/*IgnorePattern pattern = WindupFactory.eINSTANCE.createIgnorePattern();
		pattern.setPattern("");
		pattern.setEnabled(false);*/
		//patterns.put(pattern.getPattern(), pattern);
		return Maps.newHashMap();
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
	public String getReportLocation(IResource resource) {
		
		if (this.recentConfiguration == null) {
			this.recentConfiguration = modelService.getRecentConfiguration();
			if (recentConfiguration == null) {
				return null;
			}
		}

		String resourceLocation = resource.getLocation().toFile().getAbsolutePath();
		
		for (Report report : recentConfiguration.getReports()) {
			if (report.getInputFile().equals(resourceLocation)) {
				return report.getLocation();
			}
		}
		
		File output = new File(recentConfiguration.getOutputLocation()+File.separator+ModelService.REPORT_FOLDER);
		if (output.exists()) {
			String name = ModelService.INPUT_INDEX+resource.getName();
			for (File report : output.listFiles()) {
				if (report.getName().contains(name)) {
					return report.getAbsolutePath();
				}
			}
		}

		return null;
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
}