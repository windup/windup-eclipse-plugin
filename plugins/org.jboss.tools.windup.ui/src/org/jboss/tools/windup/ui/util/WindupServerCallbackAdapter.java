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
package org.jboss.tools.windup.ui.util;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.windup.ui.util.WindupLauncher.WinupServerCallback;

public class WindupServerCallbackAdapter implements WinupServerCallback {
	private Shell shell;
	public WindupServerCallbackAdapter(Shell shell) {
		this.shell = shell;
	}
	@Override
	public void serverShutdown(IStatus status) {
	}
	@Override
	public void serverStart(IStatus status) {
	}
	@Override
	public void windupNotExecutable() {
	}
	@Override
	public Shell getShell() {
		return shell;
	}
}