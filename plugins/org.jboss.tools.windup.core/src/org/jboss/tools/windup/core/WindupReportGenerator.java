/*******************************************************************************
* Copyright (c) 2011 Red Hat, Inc.
* Distributed under license by Red Hat, Inc. All rights reserved.
* This program is made available under the terms of the
* Eclipse Public License v1.0 which accompanies this distribution,
* and is available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   Red Hat, Inc. - initial API and implementation
******************************************************************************/
package org.jboss.tools.windup.core;

import java.io.File;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.jboss.windup.WindupEngine;

/**
 * TODO: IAN: doc me
 */
public class WindupReportGenerator {
	private static final String PROJECT_REPORT_HOME_PAGE = "index.html"; //$NON-NLS-1$
	private static final String HTML_FILE_EXTENSION = "html"; //$NON-NLS-1$

	private static WindupReportGenerator generator;
	
	private static IPath reportsDir = WindupCorePlugin.getDefault().getStateLocation().append("reports"); //$NON-NLS-1$
	
	private WindupReportGenerator() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @return singleton instance of the {@link WindupReportGenerator}
	 */
	public static WindupReportGenerator getDefault() {
		if(generator == null) {
			generator = new WindupReportGenerator();
		}
		
		return generator;
	}
	
	/**
	 * <p>
	 * Get the Windup report for the given resource.
	 * </p>
	 * 
	 * @param resource
	 * @return
	 */
	public IPath getReportPath(IResource resource) {
		
		IPath projectReportPath = reportsDir.append(resource.getProject().getName());
		
		IPath reportPath = null;
		switch (resource.getType()) {
			// if selected resource is a file get the file specific report page
			case IResource.FILE: {
				IPath workspaceRelativePath = resource.getProject().getFullPath().append(resource.getProjectRelativePath());
				
				reportPath = projectReportPath.append(workspaceRelativePath).addFileExtension(HTML_FILE_EXTENSION);
				
				break;
			}
			
			// if selected resource is the project then get the windup report home page
			case IResource.PROJECT: {
				reportPath = projectReportPath.append(PROJECT_REPORT_HOME_PAGE);
				break;
			}
			
			default: {
				break;
			}
		}
		
		//determine if the report of the given file exists
		File reportFile = new File(reportPath.toString());
		if(!reportFile.exists()) {
			
		}
		
		return reportPath;
	}
	
	/**
	 * @return the shared {@link WindupEngine} instance
	 */
	private WindupEngine getEngine() {
		return WindupCorePlugin.getDefault().getEngine();
	}
	
}