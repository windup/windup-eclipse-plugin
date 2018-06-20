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
package org.jboss.tools.windup.ui.util;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.common.ui.JobResultFuture;
import org.jboss.tools.foundation.core.jobs.DelegatingProgressMonitor;
import org.jboss.tools.windup.runtime.WindupRuntimePlugin;
import org.jboss.tools.windup.ui.WindupUIPlugin;

public class FutureUtils {
	
	private static final long THREAD_SLEEP = 1 * 1000;
	
	public static IStatus runWithProgress(final Job job, long duration, int totalWork, Shell shell, String taskName) {
		final JobResultFuture future = new JobResultFuture(job);
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
		try {
			dialog.run(true, false, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					if (job instanceof AbstractDelegatingMonitorJob) {
						((AbstractDelegatingMonitorJob)job).getDelegatingProgressMonitor().add(monitor);
					}
					monitor.beginTask(taskName, totalWork);
					job.schedule();
					try {
						waitForFuture(duration, future, monitor);
					} catch (ExecutionException e) {
					} catch (TimeoutException e) {
					} finally {
						monitor.done();
					}
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			WindupRuntimePlugin.log(e);
			String message = "Error occurred during operation - " + job.getName();
			message += ": Exeception is: " + e.getMessage();
			return new Status(IStatus.ERROR, WindupUIPlugin.PLUGIN_ID, message);
		}
		return getStatus(job, future);
	}
	
	private static IStatus getStatus(final Job job, final JobResultFuture future) {
		if (future.isCancelled()) {
			String message = NLS.bind("The operation ''{0}'' was cancelled", job.getName());
			WindupUIPlugin.logErrorMessage(message);
			return new Status(IStatus.CANCEL, WindupUIPlugin.PLUGIN_ID, message);
		}
		if (future.isDone()) {
			return job.getResult();
		}
		String message = NLS.bind("The operation ''{0}'' did not complete in a reasonnable amount of time", job.getName());
		return new Status(IStatus.ERROR, WindupUIPlugin.PLUGIN_ID, message);
	}

	public static void waitForFuture(long timeout, Future<?> future, IProgressMonitor monitor)
			throws InterruptedException, ExecutionException, TimeoutException {
		long startTime = System.currentTimeMillis();
		while (!future.isDone()) {
			if ((timeout > 0 && isTimeouted(startTime, timeout))
					|| monitor.isCanceled()) {
				future.cancel(true);
				break;
			}
			Thread.sleep(THREAD_SLEEP);
		}
	}
	
	private static boolean isTimeouted(long startTime, long timeout) {
		return (System.currentTimeMillis() - startTime) > timeout;
	}
	
	public static abstract class AbstractDelegatingMonitorJob extends Job {

		public static final int OK = 0;
		public static final int TIMEOUTED = 1;

		protected DelegatingProgressMonitor delegatingMonitor;

		public AbstractDelegatingMonitorJob(String name) {
			super(name);
			this.delegatingMonitor = new DelegatingProgressMonitor();
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			delegatingMonitor.add(monitor);
			return doRun(delegatingMonitor);
		}

		public DelegatingProgressMonitor getDelegatingProgressMonitor() {
			return delegatingMonitor;
		}

		protected abstract IStatus doRun(IProgressMonitor monitor);
			
		public boolean isTimeouted(IStatus status){
			return status.getCode() == TIMEOUTED;
		}
	}
}
