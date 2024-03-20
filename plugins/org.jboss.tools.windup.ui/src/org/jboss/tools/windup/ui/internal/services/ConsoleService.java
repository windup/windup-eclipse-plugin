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
package org.jboss.tools.windup.ui.internal.services;

import java.io.PrintStream;

import javax.annotation.PostConstruct;
import jakarta.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.jboss.tools.windup.ui.WindupUIPlugin;

/**
 * Service for redirecting System.out and System.error streams to the console view.
 */
@Singleton
@Creatable
public class ConsoleService {
	
	private static final String CONSOLE = "Windup Console"; //$NON-NLS-1$

	private MessageConsole console;
	private PrintStream printStream;
	private PrintStream oldOut;
	private PrintStream oldError;

	@PostConstruct
	private void init() {
		console = new MessageConsole(CONSOLE, null);
		ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[]{console});
		MessageConsoleStream consoleStream = console.newMessageStream();
		printStream = new PrintStream(consoleStream);
	}
	
	/**
	 * Starts capturing logging and redirects it to the console view.
	 */
	public void startCapturing() {
		showWindupConsole();
		console.clearConsole();
		oldOut = System.out;
		oldError = System.err;
		System.setOut(printStream);
		System.setErr(printStream);
	}
	
	private void showWindupConsole() {
		Display.getDefault().syncExec(() -> {
			try {
				IConsoleView view = (IConsoleView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().
						getActivePage().showView(IConsoleConstants.ID_CONSOLE_VIEW);
				view.display(console);
			} catch (PartInitException e) {
				WindupUIPlugin.log(e);
			}
		});
	}
	
	public void write(String line) {
		showWindupConsole();
		printStream.println(line);
	}
	
	/**
	 * Stops capturing logging info, and restores streams.
	 */
	public void stopCapturing() {
		System.setOut(oldOut);
		System.setErr(oldError);
	}
}
