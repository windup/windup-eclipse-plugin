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

import java.util.function.Consumer;

import javax.inject.Singleton;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.swt.widgets.Display;
import org.jboss.tools.windup.model.domain.WindupConstants;
import org.jboss.tools.windup.runtime.WindupRuntimePlugin;
import org.jboss.windup.bootstrap.help.Help;

/**
 * Service for retrieving Windup options.
 */
@Singleton
@Creatable
public class WindupOptionsService {

	private Help help;
	
	private boolean initialized = false;
	
	private void doLoadOptions(Consumer<Help> runner) {
		Job job = new Job(WindupConstants.LOADING_OPTIONS) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				loadOptions();
				Display.getDefault().syncExec(() -> {
					runner.accept(help);
				});
				return Status.OK_STATUS;
			}
		};
		job.setUser(false);
		job.setPriority(Job.SHORT);
		job.schedule();
	}
	
	
	private void loadOptions() {
		if (!initialized) {
			this.help = WindupRuntimePlugin.findWindupHelpCache();
		}
		initialized = true;
	}
	
	public void loadOptions(Consumer<Help> runner) {
		if (!initialized) {
			doLoadOptions(runner);
		}
		else {
			runner.accept(help);
		}
	}
}
