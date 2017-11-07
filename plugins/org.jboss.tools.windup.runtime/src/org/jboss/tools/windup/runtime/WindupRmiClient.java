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

import static org.jboss.tools.windup.runtime.WindupRuntimePlugin.logError;
import static org.jboss.tools.windup.runtime.WindupRuntimePlugin.logInfo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;

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
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.jboss.windup.tooling.ExecutionBuilder;

import com.google.common.collect.Maps;

@Singleton
@Creatable
public class WindupRmiClient {
	
	public static final String JAVA_HOME = "JAVA_HOME";
	
	/**
	 * Server status
	 */
	public static final String WINDUP_SERVER_STATUS = "windup/server/status"; //$NON-NLS-1$
	
	private ExecuteWatchdog watchdog;
	
	private ExecutionBuilder executionBuilder;
	
	@Inject private IEventBroker eventBroker;

	private IEclipsePreferences defaultPreferences = DefaultScope.INSTANCE.getNode(WindupRuntimePlugin.PLUGIN_ID);
	private IEclipsePreferences preferences = InstanceScope.INSTANCE.getNode(WindupRuntimePlugin.PLUGIN_ID);
	
	public Path getWindupHome() {
		String path = preferences.get(IPreferenceConstants.WINDUP_HOME, "");
		if (path.isEmpty()) {
			path = defaultPreferences.get(IPreferenceConstants.WINDUP_HOME, "");
		}
		if (path.isEmpty()) {
			path = WindupRuntimePlugin.getDefaultWindupHome();
		}
		return new File(path).toPath();
	}
	
	public int getRmiPort() {
		int port = preferences.getInt(IPreferenceConstants.RMI_PORT, -1);
		if (port == -1) {
			port = defaultPreferences.getInt(IPreferenceConstants.RMI_PORT, -1); 
		}
		if (port == -1) {
			port = IPreferenceConstants.DEFAULT_RMI_PORT;
		}
		return port; 
	}
	
	private String computeJRELocation() {
		String location = preferences.get(IPreferenceConstants.WINDUP_JRE_HOME, null);
		if (location == null) {
			IVMInstall jre = JavaRuntime.getDefaultVMInstall();
			if (jre != null) {
				location = jre.getInstallLocation().getAbsolutePath();
			}
		}
		return location;
	}

	public void startWindup(final IProgressMonitor monitor) {
		logInfo("Begin start RHAMT."); //$NON-NLS-1$
		monitor.worked(1);
		
		CommandLine cmdLine = CommandLine.parse(getWindupHome().toString());
		String jre = computeJRELocation();
		Map<String, String> env = Maps.newHashMap();
		if (jre != null && !jre.trim().isEmpty()) {
			env.put(JAVA_HOME, jre);
			logInfo("Using " + JAVA_HOME + " - " + jre);
		}
		
		cmdLine.addArgument("--startServer"); //$NON-NLS-1$
		cmdLine.addArgument(String.valueOf(getRmiPort()));
		watchdog = new ExecuteWatchdog(ExecuteWatchdog.INFINITE_TIMEOUT);
		ExecuteResultHandler handler = new ExecuteResultHandler() {
			@Override
			public void onProcessFailed(ExecuteException e) {
				logInfo("onProcessFailed"); //$NON-NLS-1$
				executionBuilder = null;
				notifyServerChanged();
			}
			@Override
			public void onProcessComplete(int exitValue) {
				logInfo("onProcessComplete"); //$NON-NLS-1$
				executionBuilder = null;
				notifyServerChanged();
			}
		};
		DefaultExecutor executor = new DefaultExecutor();
		executor.setStreamHandler(new PumpStreamHandler(new LogOutputStream() {
			@Override
			protected void processLine(String line, int logLevel) {
				logInfo("Message from RHAMT executor: " + line); //$NON-NLS-1$
				monitor.worked(1);
			}
		}));
		executor.setWatchdog(watchdog);
		executor.setExitValue(1);
		monitor.worked(1);
		try {
			logInfo("Starting RHAMT in server mode..."); //$NON-NLS-1$
			logInfo("Command-line: " + cmdLine); //$NON-NLS-1$
			executor.execute(cmdLine, env, handler);
		} catch (IOException e) {
			WindupRuntimePlugin.log(e);
		}
	}
	
	public boolean isWindupServerRunning() {
		return executionBuilder != null;
	}
	
	/**
	 * @return true if the ExecutionBuilder is not null, false otherwise.
	 */
	public boolean updateWindupServer() {
		if (isWindupServerRunning()) {
			return true;
		}
		else {
			this.executionBuilder = WindupRmiClient.getExecutionBuilder(getRmiPort());
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
	
	public String getWindupVersion() {
		String version = "unknown";
		try {
			version = executionBuilder.getVersion();
		} catch (RemoteException e) {
	        logInfo("Issue while attempting to retrieve RHAMT server version."); //$NON-NLS-1$
		}
		return version;
	}
	
	private static ExecutionBuilder getExecutionBuilder(int rmiPort) {
		logInfo("Attempting to retrieve ExecutionBuilder from registry."); //$NON-NLS-1$
		try {
			Registry registry = LocateRegistry.getRegistry(rmiPort);
	        ExecutionBuilder executionBuilder = (ExecutionBuilder) registry.lookup(ExecutionBuilder.LOOKUP_NAME);
	        executionBuilder.clear();
	        logInfo("ExecutionBuilder retrieved from registry."); //$NON-NLS-1$
	        return executionBuilder;
		} catch (ConnectException e) {
		} catch (RemoteException e) {
			logError("Error while attempting to retrieve the ExecutionBuilder from RMI registry.", e); //$NON-NLS-1$
		} catch (NotBoundException e) {
			logError("ExecutionBuilder not yet bound.", e); //$NON-NLS-1$
		}
		return null;
	}

	@PreDestroy
	public void shutdownWindup() {
		ExecutionBuilder builder = WindupRmiClient.getExecutionBuilder(getRmiPort());
		if (builder != null) {
			try {
				logInfo("ExecutionBuilder found in RMI Registry. Attempting to terminate it."); //$NON-NLS-1$
				builder.terminate();
				if (executionBuilder != null && executionBuilder != builder) {
					logInfo("Attempting to terminate it current reference to ExecutionBuilder."); //$NON-NLS-1$
					executionBuilder.terminate();
				}
			} catch (RemoteException e) {
				logError("Error while terminating a previous RHAMT server instance.", e); //$NON-NLS-1$ 
			}
		}
		executionBuilder = null;
		notifyServerChanged();
	}
}
