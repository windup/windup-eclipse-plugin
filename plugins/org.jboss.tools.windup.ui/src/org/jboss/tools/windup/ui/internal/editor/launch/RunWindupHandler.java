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
package org.jboss.tools.windup.ui.internal.editor.launch;

import static org.jboss.tools.windup.model.domain.WindupConstants.ACTIVE_CONFIG;

import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.osgi.util.NLS;
import org.jboss.tools.windup.core.services.WindupService;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.services.MarkerService;
import org.jboss.tools.windup.ui.internal.services.ViewService;
import org.jboss.tools.windup.windup.ConfigurationElement;

/**
 * Handler for running Windup using the active {@link ConfigurationElement}.
 */
public class RunWindupHandler {

	@Inject private WindupService windupService;
	@Inject private ViewService viewService;
	@Inject private MarkerService markerService;
	private ConfigurationElement configuration;
	
	@Inject
	@Optional
	public void updateConfiguration(@UIEventTopic(ACTIVE_CONFIG) ConfigurationElement configuration) {
		this.configuration = configuration;
	}
	
	@Execute
	public void run(WindupService windup) {
		Job job = new Job(NLS.bind(Messages.generate_windup_report_for, configuration.getName())) {
            @Override
            protected IStatus run(IProgressMonitor monitor) {
            	viewService.launchStarting();
            	IStatus status = windupService.generateGraph(configuration, monitor);
            viewService.renderReport(configuration);
            	markerService.generateMarkersForConfiguration(configuration);
            	return status;
            }
        };
        job.setUser(true);
        job.schedule();
	}
	
	@CanExecute
	public boolean canExecute() {
		return configuration != null && !configuration.getInputs().isEmpty();
	}
}
