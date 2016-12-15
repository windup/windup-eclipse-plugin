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

import static org.jboss.tools.windup.model.domain.WindupConstants.LAUNCH_COMPLETED;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.windup.core.services.WindupService;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.runtime.WindupRmiClient;
import org.jboss.tools.windup.runtime.WindupRmiClient.ProgressCallback;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.services.ConsoleService;
import org.jboss.tools.windup.ui.internal.services.MarkerService;
import org.jboss.tools.windup.ui.util.FutureUtils;
import org.jboss.tools.windup.ui.util.FutureUtils.AbstractDelegatingMonitorJob;
import org.jboss.tools.windup.windup.ConfigurationElement;

/**
 * The launch delegate for Windup.
 */
public class WindupLaunchDelegate implements ILaunchConfigurationDelegate {
	
	public static final long WINDUP_START_DURATION_TIMEOUT = 15000;
	
	@Inject private ModelService modelService;
	@Inject private WindupService windupService;
	@Inject private IEventBroker broker;
	@Inject private MarkerService markerService;
	@Inject private ConsoleService consoleService;
	@Inject private WindupRmiClient windupClient;
	@Inject @Named (IServiceConstants.ACTIVE_SHELL) Shell shell;
	
	public void launch(ILaunchConfiguration config, String mode, ILaunch launch, IProgressMonitor monitor) {
		ConfigurationElement configuration = modelService.findConfiguration(config.getName());
		if (configuration == null || configuration.getInputs().isEmpty()) {
			Display.getDefault().asyncExec(() -> {
				MessageDialog.openInformation(Display.getDefault().getActiveShell(), 
						Messages.launchErrorTitle, Messages.launchErrorMessage);
			});
		}
		else {
			markerService.deleteAllWindupMarkers();
			if (!windupClient.isWindupServerRunning()) {
				startWindup(configuration);
			}
			else {
				runWindup(configuration);
			}
		}
	}
	
	private void startWindup(ConfigurationElement configuration) {
		WindupRmiClient.ProgressCallback callback = new WindupRmiClient.ProgressCallback() {
			private boolean serverStarted = false;
			@Override
			public void serverStarted() {
				serverStarted = true;
			}
			@Override
			public boolean isServerStarted() {
				return serverStarted;
			}
			@Override
			public void log(String line) {
				consoleService.write(line);
			}
			@Override
			public void processFailed(String message) {
				Display.getDefault().asyncExec(() -> {
					MessageDialog.openError(shell, Messages.WindupServerError, message);
				});
			}
		};

		Job job = createStartWindupJob(callback);
		Display.getDefault().syncExec(() -> {
			IStatus status = FutureUtils.runWithProgress(job, WINDUP_START_DURATION_TIMEOUT, 7, shell, Messages.WindupStartingDetail);
			if (status.isOK()) {
				runWindup(configuration);
			}
			else {
				WindupLaunchDelegate.openError(shell, status.getMessage());
			}
		});
	}
	
	public Job createStartWindupJob(final ProgressCallback callback) {
		Job job = new AbstractDelegatingMonitorJob(Messages.WindupStartingTitle) {
			@Override
			protected IStatus doRun(IProgressMonitor monitor) {
				Future<IStatus> future = new Future<IStatus>() {
					private AtomicBoolean cancelled = new AtomicBoolean(false);
					@Override
					public boolean cancel(boolean mayInterruptIfRunning) {
						cancelled.set(true);
						return true;
					}
					@Override
					public boolean isCancelled() {
						return cancelled.get();
					}
					@Override
					public boolean isDone() {
						return callback.isServerStarted();
					}
					@Override
					public IStatus get() throws InterruptedException, ExecutionException {
						return null;
					}
					@Override
					public IStatus get(long timeout, TimeUnit unit)
							throws InterruptedException, ExecutionException, TimeoutException {
						return null;
					}
				};
				windupClient.startWindup(monitor, callback);
				try {
					FutureUtils.waitForFuture(WINDUP_START_DURATION_TIMEOUT, future, monitor);
				} catch (ExecutionException | TimeoutException | InterruptedException e) {
					WindupUIPlugin.log(e);
					WindupLaunchDelegate.openError(shell, e.getMessage());
				} finally {
					monitor.done();
				}
				return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		return job;
	}
	
	private static void openError(Shell shell, String message) {
		Display.getDefault().asyncExec(() -> {
			String msg = Messages.WindupStartingError + " " + message;
			MessageDialog.openError(shell,  Messages.WindupServerError, msg);
		});
	}
	
	private void runWindup(ConfigurationElement configuration) {
		Job job = new Job(NLS.bind(Messages.generate_windup_report_for, configuration.getName())) {
            @Override
            protected IStatus run(IProgressMonitor monitor) {
            	IStatus status = windupService.generateGraph(configuration, monitor);
            	broker.post(LAUNCH_COMPLETED, configuration);
                return status;
            }
        };
        job.setUser(true);
        job.schedule();
	}
}
