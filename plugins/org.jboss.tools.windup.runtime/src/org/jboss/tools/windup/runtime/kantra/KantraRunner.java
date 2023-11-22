/*---------------------------------------------------------------------------------------------
 *  Copyright (c) Red Hat. All rights reserved.
 *--------------------------------------------------------------------------------------------*/
package org.jboss.tools.windup.runtime.kantra;

import static org.jboss.tools.windup.runtime.WindupRuntimePlugin.logInfo;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteResultHandler;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.exec.PumpStreamHandler;
import org.eclipse.core.runtime.IProgressMonitor;
import org.jboss.tools.windup.runtime.WindupRuntimePlugin;

import com.google.common.collect.Maps;

public class KantraRunner {
	
	private ExecuteWatchdog watchdog;
	
	public void runKantra(final IProgressMonitor monitor, String[] input, String output, Consumer<String> onMessage, Consumer<Boolean> onComplete, Consumer<String> onFailed) {
		logInfo("Begin start kantra."); //$NON-NLS-1$
		monitor.worked(1);
		
		String windupExecutable = WindupRuntimePlugin.computeWindupHome().resolve("bin").resolve("cli").toString(); //$NON-NLS-1$ //$NON-NLS-2$
		
		
		if (windupExecutable == null) {
			WindupRuntimePlugin.logErrorMessage("kantra CLI not specified."); //$NON-NLS-1$
			return;
		}
		
		boolean executable = new File(windupExecutable).setExecutable(true);
		if (!executable) {
			WindupRuntimePlugin.logErrorMessage("kantra CLI not executable."); //$NON-NLS-1$
			return;
		}
		
		CommandLine cmdLine = CommandLine.parse(windupExecutable);
		
		Map<String, String> env = Maps.newHashMap();
		for (Map.Entry<String, String> entry : System.getenv().entrySet()) {
			env.put(entry.getKey(), entry.getValue());
		}
		
		cmdLine.addArgument("analyze"); //$NON-NLS-1$
		cmdLine.addArgument("--input"); //$NON-NLS-1$
		
		watchdog = new ExecuteWatchdog(ExecuteWatchdog.INFINITE_TIMEOUT);
		ExecuteResultHandler handler = new ExecuteResultHandler() {
			@Override
			public void onProcessFailed(ExecuteException e) {
				logInfo("kantra process failed:"); //$NON-NLS-1$
				logInfo(e.getMessage()); //$NON-NLS-1$
				onFailed.accept(e.getMessage());
//				executionBuilder = null;
//				notifyServerChanged();
			}
			@Override
			public void onProcessComplete(int exitValue) {
				logInfo("kantra process has completed."); //$NON-NLS-1$
				onComplete.accept(true);
//				executionBuilder = null;
//				notifyServerChanged();
			}
		};
		DefaultExecutor executor = new DefaultExecutor();
		executor.setStreamHandler(new PumpStreamHandler(new LogOutputStream() {
			@Override
			protected void processLine(String line, int logLevel) {
				logInfo("kantra output: " + line); //$NON-NLS-1$
				monitor.worked(1);
				onMessage.accept(line);
			}
		}));
		executor.setWatchdog(watchdog);
		executor.setExitValue(1);
		monitor.worked(1);
		try {
			logInfo("Starting kantra..."); //$NON-NLS-1$
			logInfo("Command-line: " + cmdLine); //$NON-NLS-1$
			executor.execute(cmdLine, env, handler);
		} catch (IOException e) {
			WindupRuntimePlugin.log(e);
		}
	}
}
