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
import javax.inject.Inject;
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
import org.eclipse.e4.core.services.events.IEventBroker;
import org.jboss.windup.tooling.ExecutionBuilder;

import static org.jboss.tools.windup.runtime.WindupRuntimePlugin.*;

@Singleton
@Creatable
public class WindupRmiClient {
	
	/**
	 * Server status
	 */
	public static final String WINDUP_SERVER_STATUS = "windup/server/status";
	
	private Path windupHome;
	private int rmiPort;
	
	private ExecuteWatchdog watchdog;
	private ExecutionBuilder executionBuilder;
	
	@Inject private IEventBroker eventBroker;
	
	@PostConstruct
	private void init() {
		// TODO: Move these to a preference page, and account for them being changed.
		this.rmiPort = 1100;
		this.windupHome = findWindupHome().toPath().resolve("bin").resolve("windup");
	}
	
	public Path getWindupHome() {
		return windupHome;
	}
	
	public int getRmiPort() {
		return rmiPort;
	}

	public void startWindup(final IProgressMonitor monitor) {
		logInfo("Begin start Windup."); //$NON-NLS-1$
		monitor.worked(1);
		CommandLine cmdLine = CommandLine.parse(windupHome.toString());
		cmdLine.addArgument("--startServer"); //$NON-NLS-1$
		cmdLine.addArgument(String.valueOf(rmiPort));
		watchdog = new ExecuteWatchdog(ExecuteWatchdog.INFINITE_TIMEOUT);
		ExecuteResultHandler handler = new ExecuteResultHandler() {
			@Override
			public void onProcessFailed(ExecuteException e) {
				logInfo("onProcessFailed"); //$NON-NLS-1$
				executionBuilder = null;
			}
			@Override
			public void onProcessComplete(int exitValue) {
				logInfo("onProcessComplete"); //$NON-NLS-1$
				executionBuilder = null;
			}
		};
		DefaultExecutor executor = new DefaultExecutor();
		executor.setStreamHandler(new PumpStreamHandler(new LogOutputStream() {
			@Override
			protected void processLine(String line, int logLevel) {
				logInfo("Message from Windup executor: " + line);
				monitor.worked(1);
			}
		}));
		executor.setWatchdog(watchdog);
		executor.setExitValue(1);
		monitor.worked(1);
		try {
			logInfo("Starting Windup in server mode..."); //$NON-NLS-1$
			executor.execute(cmdLine, null, handler);
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
			notifyServerChanged();
			return executionBuilder != null;
		}
	}
	
	private void notifyServerChanged() {
		eventBroker.post(WINDUP_SERVER_STATUS, executionBuilder);
	}
	
	public ExecutionBuilder getExecutionBuilder() {
		return executionBuilder;
	}
	
	public boolean isWindupServerRunning() {
		return getExecutionBuilder() != null || WindupRmiClient.getExecutionBuilder(rmiPort) != null;
	}

	private static ExecutionBuilder getExecutionBuilder(int rmiPort) {
		logInfo("Attempting to retrieve ExecutionBuilder from registry."); //$NON-NLS-1$
		try {
			Registry registry = LocateRegistry.getRegistry(rmiPort);
	        ExecutionBuilder executionBuilder = (ExecutionBuilder) registry.lookup(ExecutionBuilder.LOOKUP_NAME);
	        executionBuilder.clear();
	        logInfo("ExecutionBuilder retrieved from registry."); //$NON-NLS-1$
	        return executionBuilder;
		} catch (RemoteException e) {
			logError("Error while attempting to retrieve the ExecutionBuilder from RMI registry.", e); //$NON-NLS-1$
		} catch (NotBoundException e) {
			logError("ExecutionBuilder not yet bound.", e); //$NON-NLS-1$
		}
		return null;
	}
	
	@PreDestroy
	public void shutdownWindup() {
		ExecutionBuilder builder = WindupRmiClient.getExecutionBuilder(rmiPort);
		if (builder != null) {
			try {
				logInfo("ExecutionBuilder found in RMI Registry. Attempting to terminate it."); //$NON-NLS-1$
				builder.terminate();
				if (executionBuilder != null && executionBuilder != builder) {
					logInfo("Attempting to terminate it current reference to ExecutionBuilder."); //$NON-NLS-1$
					executionBuilder.terminate();
				}
			} catch (RemoteException e) {
				logError("Error while terminating a previous Windup server instance.", e); //$NON-NLS-1$ 
			}
		}
		executionBuilder = null;
		notifyServerChanged();
	}
}
