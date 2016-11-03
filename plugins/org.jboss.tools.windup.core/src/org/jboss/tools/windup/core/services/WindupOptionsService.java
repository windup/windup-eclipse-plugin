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

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.swt.widgets.Display;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.model.domain.WindupConstants;

/**
 * Service for retrieving Windup options.
 */
@Singleton
@Creatable
public class WindupOptionsService {
	
	private boolean initialized = false;
	@Inject private ModelService modelService;

	private void doLoadOptions(Runnable runner) {
		Job job = new Job(WindupConstants.LOADING_OPTIONS) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				modelService.getOptionFacadeManager();
				Display.getDefault().syncExec(() -> {
					runner.run();
				});
				return Status.OK_STATUS;
			}
		};
		job.setUser(false);
		job.setPriority(Job.SHORT);
		job.schedule();
	}
	
	
	public void loadOptions(Runnable runner) {
		if (!initialized) {
			doLoadOptions(runner);
		}
		else {
			runner.run();
		}
	}
}
