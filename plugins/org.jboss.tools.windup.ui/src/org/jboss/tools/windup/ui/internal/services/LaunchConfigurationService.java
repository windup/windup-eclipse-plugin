/*******************************************************************************
 * Copyright (c) 2021 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.services;

import static org.jboss.tools.windup.model.domain.WindupConstants.CONFIG_DELETED;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationListener;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.jboss.tools.windup.core.utils.FileUtils;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.model.domain.WindupConstants;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.launch.LaunchUtils;
import org.jboss.tools.windup.windup.ConfigurationElement;

import com.google.common.base.Objects;

/**
 * Service for listening to {@link ILaunchConfiguration} events. 
 */
public class LaunchConfigurationService implements ILaunchConfigurationListener {
	
	@Inject private ModelService modelService;
	@Inject private MarkerService markerService;
	
	@PostConstruct
	private void init() {
		DebugPlugin.getDefault().getLaunchManager().addLaunchConfigurationListener(this);
	}

	@PreDestroy
	private void dipose() {
		DebugPlugin.getDefault().getLaunchManager().removeLaunchConfigurationListener(this);
	}
	
	@Override
	public void launchConfigurationAdded(ILaunchConfiguration launchConfig) {
		if (isWindupConfig(launchConfig) && modelService.findConfiguration(launchConfig.getName()) == null) {
			LaunchUtils.createConfiguration(launchConfig.getName(), modelService);
		}
	}

	@Override
	public void launchConfigurationRemoved(ILaunchConfiguration launchConfig) {
		ConfigurationElement configuration = modelService.findConfiguration(launchConfig.getName());
		ConfigurationElement lastConfiguration = modelService.getRecentConfiguration();
		if (configuration != null) {
			if (configuration.getName().equals(lastConfiguration.getName())) {
				markerService.clear();
			}
			modelService.deleteConfiguration(configuration);
		}
	}
	
	private boolean isWindupConfig(ILaunchConfiguration launchConfig) {
		try {
			return Objects.equal(launchConfig.getType().getIdentifier(), WindupConstants.LAUNCH_TYPE);
		} catch (CoreException e) {
			WindupUIPlugin.log(e);
		}
		return false;
	}

	@Override
	public void launchConfigurationChanged(ILaunchConfiguration launchConfig) {
	}
	
	@Inject
	@Optional
	private void configDeleted(@UIEventTopic(CONFIG_DELETED) ConfigurationElement configuration) {
		if (configuration == null)
			return;
		
		String reportsOutput = configuration.getOutputLocation();
		Path reportsOutputPath = new Path(reportsOutput);
		if ( reportsOutputPath != null) {
			FileUtils.delete(reportsOutputPath.toFile(), true);
		}
	}
}
