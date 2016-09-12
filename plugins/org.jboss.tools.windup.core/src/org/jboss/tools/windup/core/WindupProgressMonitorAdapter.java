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

import org.eclipse.core.runtime.IProgressMonitor;
import org.jboss.windup.tooling.WindupToolingProgressMonitor;

/**
 * <p>
 * Adapts an {@link IProgressMonitor} to an {@link WindupProgressMonitor}.
 * </p>
 */
public class WindupProgressMonitorAdapter implements WindupToolingProgressMonitor
{

    /**
     * {@link IProgressMonitor} being adapted to a {@link WindupProgressMonitor}
     */
    IProgressMonitor wrappedMonitor;

    public WindupProgressMonitorAdapter(IProgressMonitor monitor)
    {
        this.wrappedMonitor = monitor;
    }

    @Override
    public void logMessage(String level, String category, String message) {
        WindupCorePlugin.logErrorMessage("Received a record: " + message);
    }

    @Override
    public void logMessage(String level, String category, String message, Throwable throwable) {
        WindupCorePlugin.logErrorMessage("Received a record with exception: " + message, throwable);
    }

    /**
     * @see org.jboss.windup.exec.WindupProgressMonitor#beginTask(java.lang.String, int)
     */
    @Override
    public void beginTask(String name, int totalWork)
    {
        this.wrappedMonitor.beginTask(name, totalWork);
    }

    /**
     * @see org.jboss.windup.exec.WindupProgressMonitor#done()
     */
    @Override
    public void done()
    {
        this.wrappedMonitor.done();
    }

    /**
     * @see org.jboss.windup.exec.WindupProgressMonitor#isCancelled()
     */
    @Override
    public boolean isCancelled()
    {
        return this.wrappedMonitor.isCanceled();
    }

    /**
     * @see org.jboss.windup.exec.WindupProgressMonitor#setCancelled(boolean)
     */
    @Override
    public void setCancelled(boolean cancelled)
    {
        this.wrappedMonitor.setCanceled(cancelled);
    }

    /**
     * @see org.jboss.windup.exec.WindupProgressMonitor#setTaskName(java.lang.String)
     */
    @Override
    public void setTaskName(String name)
    {
        this.wrappedMonitor.setTaskName(name);
    }

    /**
     * @see org.jboss.windup.exec.WindupProgressMonitor#subTask(java.lang.String)
     */
    @Override
    public void subTask(String name)
    {
        this.wrappedMonitor.subTask(name);
    }

    /**
     * @see org.jboss.windup.exec.WindupProgressMonitor#worked(int)
     */
    @Override
    public void worked(int work)
    {
        this.wrappedMonitor.worked(work);
    }
}