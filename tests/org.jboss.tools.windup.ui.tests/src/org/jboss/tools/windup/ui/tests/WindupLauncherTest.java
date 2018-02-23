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
package org.jboss.tools.windup.ui.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.jboss.tools.windup.runtime.IPreferenceConstants;
import org.jboss.tools.windup.runtime.WindupRuntimePlugin;
import org.jboss.tools.windup.ui.util.FutureUtils;
import org.jboss.tools.windup.ui.util.WindupLauncher;
import org.junit.Test;

public class WindupLauncherTest extends WindupUiTest {

	@Test
	public void testWindupServerStarted() {
		Job job = windupLauncher.createStartWindupJob(WindupRuntimePlugin.computeJRELocation());
		IStatus status = FutureUtils.runWithProgress(job, IPreferenceConstants.DEFAULT_WINDUP_START_DURATION_TIMEOUT, 5, Display.getDefault().getActiveShell(),
				"Starting Windup");
		assertTrue("Windup did not start successfully: " + status.getMessage(), status.isOK());
		assertNotNull("Execution Builder is NULL after starting Windup.", windupClient.getExecutionBuilder());
	}
	
	@Test
	public void testWindupServerStopped() throws InterruptedException, ExecutionException, TimeoutException {
		windupClient.shutdownWindup();
		Future<IStatus> future = WindupLauncher.getTerminateWindupFuture(windupClient);
		FutureUtils.waitForFuture(IPreferenceConstants.DEFAULT_WINDUP_STOP_DURATION_TIMEOUT, future, new NullProgressMonitor());
		assertNull("Execution Builder still exists after shutting down Windup.", windupClient.getExecutionBuilder());
	}
}
