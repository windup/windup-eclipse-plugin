package org.jboss.tools.windup.runtime;

import java.io.IOException;
import java.nio.file.Path;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteResultHandler;
import org.apache.commons.exec.ExecuteWatchdog;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.jboss.windup.tooling.ExecutionBuilder;

@Singleton
@Creatable
public class WindupRmiClient {
	
	/**
	 * The current status of Windup.
	 */
	public static enum WindupRunningStatus {
		RUNNING, IDLE 
	};
	
	private static final Logger LOG = Logger.getLogger("");
	
	private Path windupHome;
	private int rmiPort;
	
	private ExecuteWatchdog watchdog;
	private WindupRunningStatus windupRunningStatus;
	
	@PostConstruct
	private void init() {
		this.rmiPort = 1100;
		this.windupHome = WindupRuntimePlugin.findWindupHome().toPath().resolve("bin").resolve("windup");
		startWindup();
	}

	/**
	 * Starts Windup in server mode.
	 */
	public void startWindup() {
		CommandLine cmdLine = CommandLine.parse(windupHome.toString());
		cmdLine.addArgument("--startServer");
		cmdLine.addArgument(String.valueOf(rmiPort));
		watchdog = new ExecuteWatchdog(ExecuteWatchdog.INFINITE_TIMEOUT);
		ExecuteResultHandler handler = new ExecuteResultHandler() {
			@Override
			public void onProcessFailed(ExecuteException e) {
				System.err.println("onProcessFailed..." + e);
				LOG.log(Level.SEVERE, e.getMessage());
				// TODO: Shutdown Windup server.
			}
			@Override
			public void onProcessComplete(int exitValue) {
				System.err.println("onProcessComplete.. exitValue == " + exitValue);
			}
		};
		DefaultExecutor executor = new DefaultExecutor();
		executor.setWatchdog(watchdog);
		executor.setExitValue(1);
		try {
			executor.execute(cmdLine, new HashMap<String, String>(), handler);
		} catch (IOException e) {
			System.err.println("Windup exception: " + e);
			LOG.log(Level.SEVERE, e.getMessage());
		}
	}
	
	@PreDestroy
	public void shutdownWindup() throws InterruptedException {
		watchdog.destroyProcess();
	}
	
	/**
	 * Returns the current status of Windup. 
	 */
	public WindupRunningStatus getWindupStatus() {
		return windupRunningStatus;
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
}
