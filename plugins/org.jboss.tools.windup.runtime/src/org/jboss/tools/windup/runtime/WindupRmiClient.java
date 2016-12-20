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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Creatable
public class WindupRmiClient {
	
	private static Logger logger = LoggerFactory.getLogger(WindupRmiClient.class);
	
	private Path windupHome;
	private int rmiPort;
	
	private ExecuteWatchdog watchdog;
	private ExecutionBuilder executionBuilder;
	
	@PostConstruct
	private void init() {
		// TODO: Move these to a preference page, and account for them being changed.
		this.rmiPort = 1100;
		this.windupHome = WindupRuntimePlugin.findWindupHome().toPath().resolve("bin").resolve("windup");
	}
	
	public Path getWindupHome() {
		return windupHome;
	}
	
	public int getRmiPort() {
		return rmiPort;
	}

	public void startWindup(final IProgressMonitor monitor, final ProgressCallback callback) {
		logger.info("Begin start Windup."); //$NON-NLS-1$
		monitor.worked(1);
		CommandLine cmdLine = CommandLine.parse(windupHome.toString());
		cmdLine.addArgument("--startServer"); //$NON-NLS-1$
		cmdLine.addArgument(String.valueOf(rmiPort));
		watchdog = new ExecuteWatchdog(ExecuteWatchdog.INFINITE_TIMEOUT);
		ExecuteResultHandler handler = new ExecuteResultHandler() {
			@Override
			public void onProcessFailed(ExecuteException e) {
				logger.info("onProcessFailed"); //$NON-NLS-1$
				executionBuilder = null;
			}
			@Override
			public void onProcessComplete(int exitValue) {
				logger.info("onProcessComplete"); //$NON-NLS-1$
				executionBuilder = null;
			}
		};
		DefaultExecutor executor = new DefaultExecutor();
		executor.setStreamHandler(new PumpStreamHandler(new LogOutputStream() {
			@Override
			protected void processLine(String line, int logLevel) {
				logger.info(line);
				monitor.worked(1);
			}
		}));
		executor.setWatchdog(watchdog);
		executor.setExitValue(1);
		monitor.worked(1);
		try {
			logger.info("Starting Windup in server mode..."); //$NON-NLS-1$
			executor.execute(cmdLine, new HashMap<String, String>(), handler);
		} catch (IOException e) {
			WindupRuntimePlugin.log(e);
		}
	}
	
	public boolean isWindupServerStarted() {
		return executionBuilder != null;
	}
	
	/**
	 * @return true if the ExecutionBuilder is not null, false otherwise.
	 */
	public boolean updateWindupServer() {
		if (isWindupServerStarted()) {
			return true;
		}
		else {
			this.executionBuilder = WindupRmiClient.getExecutionBuilder(rmiPort);
			return executionBuilder != null;
		}
	}
	
	public ExecutionBuilder getExecutionBuilder() {
		return executionBuilder;
	}
	
	public boolean isWindupServerRunning() {
		return getExecutionBuilder() != null || WindupRmiClient.getExecutionBuilder(rmiPort) != null;
	}

	private static ExecutionBuilder getExecutionBuilder(int rmiPort) {
		logger.info("Attempting to retrieve ExecutionBuilder from registry."); //$NON-NLS-1$
		try {
			Registry registry = LocateRegistry.getRegistry(rmiPort);
	        ExecutionBuilder executionBuilder = (ExecutionBuilder) registry.lookup(ExecutionBuilder.LOOKUP_NAME);
	        executionBuilder.clear();
			logger.info("ExecutionBuilder retrieved from registry."); //$NON-NLS-1$
	        return executionBuilder;
		} catch (RemoteException e) {
			WindupRuntimePlugin.logError("Error while attemptint to retrieve the ExecutionBuilder from RMI registry.", e); //$NON-NLS-1$
		} catch (NotBoundException e) {
			logger.info("ExecutionBuilder not yet bound.", e); //$NON-NLS-1$
		}
		return null;
	}
	
	@PreDestroy
	public void shutdownWindup() {
		ExecutionBuilder builder = WindupRmiClient.getExecutionBuilder(rmiPort);
		if (builder != null) {
			logger.info(""); //$NON-NLS-1$
			try {
				logger.info("ExecutionBuilder found in RMI Registry. Attempting to terminate it."); //$NON-NLS-1$
				builder.terminate();
				if (executionBuilder != null && executionBuilder != builder) {
					logger.info("Attempting to terminate it current reference to ExecutionBuilder."); //$NON-NLS-1$
					executionBuilder.terminate();
				}
			} catch (RemoteException e) {
				WindupRuntimePlugin.logError("Error while terminating a previous Windup server instance.", e); //$NON-NLS-1$ 
			}
		}
		executionBuilder = null;
	}
	
	public static interface ProgressCallback {
		boolean isServerStarted();
	}
}
