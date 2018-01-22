/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.core;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.LogRecord;

import org.eclipse.core.runtime.IProgressMonitor;
import org.jboss.windup.tooling.WindupToolingProgressMonitor;

/**
 * <p>
 * Adapts an {@link IProgressMonitor} to an {@link WindupProgressMonitor}.
 * </p>
 */
public class WindupProgressMonitorAdapter extends UnicastRemoteObject implements WindupToolingProgressMonitor, Remote {
	private static final long serialVersionUID = 1L;

	/**
     * {@link IProgressMonitor} being adapted to a {@link WindupProgressMonitor}
     */
    private IProgressMonitor wrappedMonitor;
    
    public WindupProgressMonitorAdapter(IProgressMonitor monitor) throws RemoteException {
        this.wrappedMonitor = monitor;
    }

	@Override
	public void beginTask(String task, int totalWork) throws RemoteException {
		wrappedMonitor.beginTask(task, totalWork);
	}

	@Override
	public void done() throws RemoteException {
		wrappedMonitor.done();
	}

	@Override
	public boolean isCancelled() throws RemoteException {
		return wrappedMonitor.isCanceled();
	}

	@Override
	public void setCancelled(boolean value) throws RemoteException {
		wrappedMonitor.setCanceled(value);
	}

	@Override
	public void setTaskName(String name) throws RemoteException {
		wrappedMonitor.setTaskName(name);
	}

	@Override
	public void subTask(String name) throws RemoteException {
		wrappedMonitor.subTask(name);
	}

	@Override
	public void worked(int work) throws RemoteException {
		wrappedMonitor.worked(work);
	}

	@Override
	public void logMessage(LogRecord logRecord) {
	}
}