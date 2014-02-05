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

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.operation.IRunnableWithProgress;

/**
 * <p>
 * An {@link IRunnableWithProgress} for generating Windup Reports using the
 * {@link WindupService}.
 * </p>
 */
public class GenerateWindupReportsOperation implements IRunnableWithProgress {

	/**
	 * {@link List} of {@link IProject}s to generate Windup reports for
	 */
	private final List<IProject> projectsToGenerateReportsFor;
	
	private IStatus result;
	
	/**
	 * @param projectsToGenerateReportsFor
	 *            operation will generate Windup reports for these
	 *            {@link IProject}s
	 */
	public GenerateWindupReportsOperation(List<IProject> projectsToGenerateReportsFor) {
		this.projectsToGenerateReportsFor = projectsToGenerateReportsFor;
	}
	
	/**
	 * @see org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		
		// generate the reports
		this.result = WindupService.getDefault().generateReport(
				this.projectsToGenerateReportsFor.toArray(
						new IProject[this.projectsToGenerateReportsFor.size()])
				, monitor);
	}
	
	/**
	 * <p>
	 * Returns the status of the operation. If there were any errors, the result
	 * is a status object containing individual status objects for each error.
	 * If there were no errors, the result is a status object with error code
	 * <code>OK</code>.
	 * </p>
	 * 
	 * @return the status of the operation.
	 */
	public IStatus getStatus() {
		return this.result;
	}
}