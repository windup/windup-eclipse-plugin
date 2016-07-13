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
package org.jboss.tools.windup.ui.internal.launch;

import static org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME;
import static org.jboss.tools.windup.model.domain.WindupConstants.DEFAULT;
import static org.jboss.tools.windup.model.domain.WindupConstants.LAUNCH_COMPLETED;
import static org.jboss.tools.windup.ui.internal.util.ResourceUtils.findProject;

import javax.inject.Inject;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.osgi.util.NLS;
import org.jboss.tools.windup.core.WindupCorePlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.core.services.WindupService;

/**
 * The launch delegate for Windup.
 */
public class WindupLaunchDelegate implements ILaunchConfigurationDelegate {
	
	@Inject private WindupService windup;
	@Inject private IEventBroker broker;
	
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) {
		ILaunchConfiguration config = launch.getLaunchConfiguration();
		try {
			IProject project = findProject(config.getAttribute(ATTR_PROJECT_NAME, DEFAULT));
			Job job = new Job(NLS.bind(Messages.generate_windup_report_for, project.getName())) {
                @Override
                protected IStatus run(IProgressMonitor monitor) {
                	windup.generateGraph(project);
                    IStatus status = windup.generateGraph(new IProject[]{project}, monitor);
                    broker.post(LAUNCH_COMPLETED, config);
                    return status;
                }
            };
            job.setUser(true);
            job.schedule();
		} catch (CoreException e) {
			WindupCorePlugin.log(e);
		}
	}
}
