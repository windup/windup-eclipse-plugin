/*---------------------------------------------------------------------------------------------
 *  Copyright (c) Red Hat. All rights reserved.
 *--------------------------------------------------------------------------------------------*/
package org.jboss.tools.windup.runtime.kantra;

import static org.jboss.tools.windup.runtime.WindupRuntimePlugin.logInfo;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteResultHandler;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.exec.PumpStreamHandler;
import org.jboss.tools.windup.runtime.WindupRuntimePlugin;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class KantraRunner {
	
	private ExecuteWatchdog watchdog;
	
	public void runKantra(String cli, Set<String> inputs, String output, List<String> sources, List<String> targets, List<String> rules, boolean analyzeKnownLibraries, Consumer<String> onMessage, Consumer<Boolean> onComplete, Consumer<String> onFailed) {
		logInfo("Begin start kantra."); //$NON-NLS-1$
		
		// todo: compute or parameterize from configuration
//		CommandLine cmdLine = CommandLine.parse("/usr/local/bin/kantra");
		CommandLine cmdLine = CommandLine.parse(cli);
		
		
		List<String> params = Lists.newArrayList();
		params.add("analyze");
		
		params.add("--overwrite");
		params.add("--mode");
		params.add("source-only");
		
		if (analyzeKnownLibraries) {
			params.add("--analyze-known-libraries");
		}
		
		for (String input : inputs) {
			params.add("--input");
			params.add(input);
		}
		
		params.add("--output");
		params.add(output);
		
		for (String source : sources) {
			params.add("--source");
			params.add(source);
		}
		
		for (String target : targets) {
			params.add("--target");
			params.add(target);
		}
		
		for (String rule : rules) {
			params.add("--rules");
			params.add(rule);
		}
		
		
		cmdLine.addArguments(params.toArray(new String[params.size()]), true);
		
		watchdog = new ExecuteWatchdog(ExecuteWatchdog.INFINITE_TIMEOUT);
		ExecuteResultHandler handler = new ExecuteResultHandler() {
			@Override
			public void onProcessFailed(ExecuteException e) {
				logInfo("kantra process failed:"); //$NON-NLS-1$
				logInfo(e.getMessage()); //$NON-NLS-1$
				onFailed.accept(e.getMessage());
			}
			@Override
			public void onProcessComplete(int exitValue) {
				logInfo("kantra process has completed."); //$NON-NLS-1$
				onComplete.accept(true);
			}
		};
		DefaultExecutor executor = new DefaultExecutor();
		executor.setStreamHandler(new PumpStreamHandler(new LogOutputStream() {
			@Override
			protected void processLine(String line, int logLevel) {
				logInfo("kantra output: " + line); //$NON-NLS-1$
				onMessage.accept(line);
			}
		}));
		executor.setWatchdog(watchdog);
		executor.setExitValue(1);
		Map<String, String> env = Maps.newHashMap();
		for (Map.Entry<String, String> entry : System.getenv().entrySet()) {
			env.put(entry.getKey(), entry.getValue());
		}
		// TODO: parameterize somewhere
		env.put("PODMAN_BIN", "/usr/local/bin/podman");
		try {
			logInfo("Starting kantra..."); //$NON-NLS-1$
			logInfo("Command-line: " + cmdLine); //$NON-NLS-1$
			onMessage.accept("Command-line: " + cmdLine);
			executor.execute(cmdLine, env, handler);
		} catch (IOException e) {
			WindupRuntimePlugin.log(e);
		}
	}
	
	public void kill() {
		if (this.watchdog != null) {
			this.watchdog.stop();
			this.watchdog.destroyProcess();
		}
	}
}
