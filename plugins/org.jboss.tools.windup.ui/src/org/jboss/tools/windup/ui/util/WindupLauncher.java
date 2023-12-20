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
package org.jboss.tools.windup.ui.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

//import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.jboss.tools.windup.runtime.IPreferenceConstants;
import org.jboss.tools.windup.runtime.WindupRmiClient;
import org.jboss.tools.windup.runtime.WindupRuntimePlugin;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.util.FutureUtils.AbstractDelegatingMonitorJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Creatable
public class WindupLauncher {
	
	private static Logger logger = LoggerFactory.getLogger(WindupLauncher.class);
	
//	@Inject private WindupRmiClient windupClient;
	
	private ScopedPreferenceStore preferences = new ScopedPreferenceStore(InstanceScope.INSTANCE, WindupRuntimePlugin.PLUGIN_ID);
	
	public void shutdown(WinupServerCallback callback) {
		Display.getDefault().syncExec(() -> {
			Job job = new Job(Messages.WindupShuttingDown) {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					IStatus result = Status.OK_STATUS;
					try {
						monitor.beginTask(Messages.WindupShuttingDown, 3);
						shutdown(monitor);
					} catch (ExecutionException | TimeoutException | InterruptedException e) {
						WindupUIPlugin.log(e);
						result = new Status(IStatus.ERROR, WindupUIPlugin.PLUGIN_ID, e.getMessage(), e);
					} finally {
						monitor.done();
					}
					if (monitor.isCanceled()) {
						result = Status.CANCEL_STATUS;
					}
					return result;
				}
			};
			job.setUser(true);
			job.addJobChangeListener(new JobChangeAdapter() {
				public void done(IJobChangeEvent event) {
					callback.serverShutdown(event.getResult());
				}
			});
			job.schedule();
		});
	}
	
	private void shutdown(IProgressMonitor monitor) throws InterruptedException, ExecutionException, TimeoutException {
		logger.info("Shutting Windup down."); //$NON-NLS-1$
		monitor.worked(1);
//		windupClient.shutdownWindup();
		monitor.worked(1);
//		Future<IStatus> future = WindupLauncher.getTerminateWindupFuture(windupClient);
//		int duration = preferences.getInt(IPreferenceConstants.STOP_TIMEOUT);
//		FutureUtils.waitForFuture(duration, future, monitor);
		monitor.worked(1);
	}
	
	public static Future<IStatus> getTerminateWindupFuture(WindupRmiClient windupClient) {
		return new Future<IStatus>() {
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
				return !windupClient.isWindupServerRunning();
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
	}
	
	public void start(WinupServerCallback callback, String jreHome) {
		Display.getDefault().syncExec(() -> {
			logger.info("Start Windup Server."); //$NON-NLS-1$
			Job job = createStartWindupJob(jreHome);
			int duration = preferences.getInt(IPreferenceConstants.START_TIMEOUT);
			IStatus status = FutureUtils.runWithProgress(job, duration, 5, callback.getShell(),
					Messages.WindupStartingDetail);
			callback.serverStart(status);
		});
	}
	
	public Job createStartWindupJob(String jreHome) {
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
						return false;
//						return windupClient.updateWindupServer();
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
				try {
					monitor.subTask(Messages.WindupRunStartScipt);
					
					
					
//					windupClient.startWindup(monitor, jreHome);
					int duration = preferences.getInt(IPreferenceConstants.START_TIMEOUT);
					FutureUtils.waitForFuture(duration, future, monitor);
				} catch (ExecutionException | TimeoutException | InterruptedException e) {
					WindupUIPlugin.log(e);
				} finally {
					monitor.done();
				}
				monitor.done();
				return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		return job;
	}
	
	public static interface WinupServerCallback {
		void serverShutdown(IStatus status);
		void serverStart(IStatus status);
		void windupNotExecutable();
		Shell getShell();
	}
}
