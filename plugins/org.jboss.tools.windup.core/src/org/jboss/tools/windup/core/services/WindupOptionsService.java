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

import java.util.List;
import java.util.function.Consumer;

import javax.inject.Singleton;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.jboss.tools.windup.model.domain.WindupConstants;
import org.jboss.windup.config.ConfigurationOption;

import com.google.common.collect.Lists;

/**
 * Service for retrieving Windup options.
 */
@Singleton
@Creatable
public class WindupOptionsService {

	private List<ConfigurationOption> options = Lists.newArrayList();
	
	private boolean initialized = false;
	
	private void doLoadOptions(Consumer<List<ConfigurationOption>> runner) {
		Job job = new Job(WindupConstants.LOADING_OPTIONS) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				loadOptions();
				runner.accept(options);
				return null;
			}
		};
		job.setUser(false);
		job.setPriority(Job.SHORT);
		job.schedule();
	}
	
	
	private void loadOptions() {
		this.options = WindupService.getWindupConfigurationOptions();
		initialized = true;
	}
	
	public void loadOptions(Consumer<List<ConfigurationOption>> runner) {
		if (!initialized) {
			doLoadOptions(runner);
		}
		else {
			runner.accept(options);
		}
	}
	
	
}
