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
package org.jboss.tools.windup.runtime;

import java.io.IOException;
import java.nio.file.Path;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteResultHandler;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.exec.PumpStreamHandler;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.jboss.windup.tooling.ExecutionBuilder;

@Singleton
@Creatable
public class WindupRmiClient {

	private Path windupHome;
	private int rmiPort;
	
	private ExecuteWatchdog watchdog;
	
	private AtomicBoolean started = new AtomicBoolean(false);
	
	@PostConstruct
	private void init() {
		// TODO: Move these to a preference page, and account for them being changed.
		this.rmiPort = 1100;
		this.windupHome = WindupRuntimePlugin.findWindupHome().toPath().resolve("bin").resolve("windup");
	}

	public void startWindup(final IProgressMonitor monitor, final ProgressCallback callback) {
		monitor.worked(1);
		CommandLine cmdLine = CommandLine.parse(windupHome.toString());
		cmdLine.addArgument("--startServer"); //$NON-NLS-1$
		cmdLine.addArgument(String.valueOf(rmiPort));
		watchdog = new ExecuteWatchdog(ExecuteWatchdog.INFINITE_TIMEOUT);
		ExecuteResultHandler handler = new ExecuteResultHandler() {
			@Override
			public void onProcessFailed(ExecuteException e) {
				started.set(false);
				WindupRuntimePlugin.log(e);
				callback.processFailed(e.getMessage());
			}
			@Override
			public void onProcessComplete(int exitValue) {
				started.set(false);
			}
		};
		DefaultExecutor executor = new DefaultExecutor();
		executor.setStreamHandler(new PumpStreamHandler(new LogOutputStream() {
			@Override
			protected void processLine(String line, int logLevel) {
				callback.log(line);
				monitor.worked(1);
				if (line.contains("Server started")) { //$NON-NLS-1$
					started.set(true);
					callback.serverStarted();
				}
			}
		}));
		executor.setWatchdog(watchdog);
		executor.setExitValue(1);
		monitor.worked(1);
		try {
			executor.execute(cmdLine, new HashMap<String, String>(), handler);
		} catch (IOException e) {
			WindupRuntimePlugin.log(e);
		}
	}
	
	@PreDestroy
	public void shutdownWindup() {
		watchdog.destroyProcess();
		started.set(false);
	}
	
	public boolean isWindupServerRunning() {
		return started.get();
	}
	
	/**
	 * Returns the ExecutionBuilder.
	 */
	public ExecutionBuilder getExecutionBuilder() {
		try {
			Registry registry = LocateRegistry.getRegistry(rmiPort);
	        ExecutionBuilder executionBuilder = (ExecutionBuilder) registry.lookup(ExecutionBuilder.LOOKUP_NAME);
	        return executionBuilder;
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static interface ProgressCallback {
		void log(String line);
		void serverStarted();
		boolean isServerStarted();
		void processFailed(String message);
	}
}
