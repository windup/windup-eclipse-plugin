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
package org.jboss.tools.windup.ui.internal.launch;

import static org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME;
import static org.jboss.tools.windup.model.domain.WindupConstants.DEFAULT;
import static org.jboss.tools.windup.model.domain.WindupConstants.LAUNCH_TYPE;
import static org.jboss.tools.windup.model.domain.WorkspaceResourceUtils.getProjectsFromSelection;
import static org.jboss.tools.windup.ui.internal.Messages.errorConfiguringWindup;
import static org.jboss.tools.windup.ui.internal.Messages.selectExistinConfiguration;
import static org.jboss.tools.windup.ui.internal.Messages.selectLaunchConfiguration;

import java.text.Collator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.IDebugModelPresentation;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.model.domain.WindupConstants;
import org.jboss.tools.windup.runtime.WindupRuntimePlugin;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.windup.ConfigurationElement;

import com.google.common.collect.Lists;

/**
 * Utility for launching Windup.  
 */
public class LaunchUtils {
	
	public static void launch(ISelection selection, String mode, ModelService modelService) {
		List<IProject> projects = getProjectsFromSelection(selection);
		if (!projects.isEmpty()) {
			IProject project = projects.get(0);
			launch(project, mode, modelService);
		}
	}
	
	public static void launch(IEditorPart editor, String mode, ModelService modelService) {
		IProject project = LaunchUtils.getProjectFromEditor(editor).getProject();
		launch(project, mode, modelService);
	}
	
	public static IProject getProjectFromEditor(IEditorPart editor) {
		IEditorInput input = editor.getEditorInput();
		if (!(input instanceof IFileEditorInput)) {
			return null;
		}
		return ((IFileEditorInput)input).getFile().getProject();
	}
	
	private static void launch(IProject project, String mode, ModelService modelService) {
		ILaunchConfiguration currentConfig = computeConfiguration(project, modelService);
		if (currentConfig != null) {
			DebugUITools.launch(currentConfig, mode);
		}
	}
	
	private static ILaunchConfiguration computeConfiguration(IProject project, ModelService modelService) {
		ILaunchConfiguration currentConfig = null;
		LaunchConfig launchConfig = new LaunchConfig(project);
		try {
			List<ILaunchConfiguration> candidates = collectCandidates(launchConfig);
			if (candidates.isEmpty()) {
				currentConfig = createConfiguration(launchConfig, modelService);
			}
			else if (candidates.size() == 1) {
				currentConfig = candidates.get(0);
			}
			else {
				currentConfig = chooseConfiguration(candidates);
			}
		} catch (CoreException e) {
			MessageDialog.openError(null, errorConfiguringWindup, e.getMessage());
		}
		return currentConfig;
	}
	
	private static ILaunchConfiguration chooseConfiguration(List<ILaunchConfiguration> configList) {
		IDebugModelPresentation labelProvider = DebugUITools.newDebugModelPresentation();
		ElementListSelectionDialog dialog= new ElementListSelectionDialog(
				WindupUIPlugin.getActiveWorkbenchShell(), 
				labelProvider);
		dialog.setElements(configList.toArray());
		dialog.setTitle(selectLaunchConfiguration);  
		dialog.setMessage(selectExistinConfiguration);
		dialog.setMultipleSelection(false);
		int result = dialog.open();
		labelProvider.dispose();
		if (result == Window.OK) {
			return (ILaunchConfiguration) dialog.getFirstResult();
		}
		return null;	
	}
	
	private static List<ILaunchConfiguration> collectCandidates(LaunchConfig launchConfig) throws CoreException {
		List<ILaunchConfiguration> candidates = Lists.newArrayList();
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
		for (ILaunchConfiguration configuration : launchManager.getLaunchConfigurations()) {
			if (launchConfig.matches(configuration)) {
				candidates.add(configuration);
			}
		}
		return candidates;
	}
	
	public static ILaunchConfiguration createConfiguration(LaunchConfig config, ModelService modelService) throws CoreException {
		String name = config.project.getName();
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType configType = launchManager.getLaunchConfigurationType(LAUNCH_TYPE);
		ILaunchConfigurationWorkingCopy wc = configType.newInstance(null, 
				launchManager.generateLaunchConfigurationName(name));
		wc.setAttribute(ATTR_PROJECT_NAME, config.project.getName());
		ConfigurationElement configuration = modelService.findConfiguration(name);
		if (configuration == null) {
			configuration = LaunchUtils.createConfiguration(config.project.getName(), modelService);
		}
		
		boolean exists = configuration.getInputs().stream().filter(i -> { 
			return i.getName().equals(name); }
		).findAny().isPresent();
		
		if (!exists) {
			modelService.createInput(configuration, Lists.newArrayList(config.project));
		}
		
		return wc.doSave();
	}
	
	public static ConfigurationElement createConfiguration(String name, ModelService modelService) {
		ConfigurationElement configuration = modelService.createConfiguration(name);
		configuration.setJreHome(WindupRuntimePlugin.computeJRELocation());
		return configuration;
	}
	
	private static class LaunchConfig {
		
		private IProject project;

		private LaunchConfig(IProject project) {
			this.project = project;
		}

		private boolean matches(ILaunchConfiguration other) throws CoreException {
			return WindupConstants.LAUNCH_TYPE.equals(other.getType().getIdentifier())
					&& Collator.getInstance().equals(project.getName(), 
							other.getAttribute(ATTR_PROJECT_NAME, DEFAULT));
		}
	}
}
