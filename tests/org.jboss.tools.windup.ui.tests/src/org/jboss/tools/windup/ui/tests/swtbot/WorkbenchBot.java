/*******************************************************************************
 * Copyright (c) 2019 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.tests.swtbot;

import javax.inject.Singleton;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.tools.test.util.TestProjectProvider;

/**
 * An {@link SWTBot} with capabilities for testing Eclipse workbench related functionality.
 */
@Creatable
@Singleton
public class WorkbenchBot extends SWTWorkbenchBot {
	
	public TestProjectProvider importProject(String bundleName, String projectPath, 
			String name, boolean makeCopy)  throws CoreException {
		return new TestProjectProvider(bundleName, projectPath, name, makeCopy);
	}
}
