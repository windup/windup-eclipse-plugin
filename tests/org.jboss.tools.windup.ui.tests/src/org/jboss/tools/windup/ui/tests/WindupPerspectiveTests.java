/*******************************************************************************
 * Copyright (c) 2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.tests;

import static org.junit.Assert.assertTrue;

import java.util.Objects;

import org.eclipse.core.runtime.CoreException;
import org.jboss.tools.test.util.TestProjectProvider;
import org.jboss.tools.windup.ui.WindupPerspective;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WindupPerspectiveTests extends WindupUiTest {
	
	private static final String PROJECT = "Portal-WAR";
	
	private TestProjectProvider projectProvider;
	
	@Before
	public void init() throws CoreException {
		projectProvider = workbenchBot.importProject(Activator.PLUGIN_ID, null, PROJECT, false);
	}
	
	@After
	public void clean() {
		projectProvider.dispose();
	}
	
	@Test
	public void testPerspective() {
		openWindupPerspective();
		assertTrue(Objects.equals(WindupPerspective.ID,	
				modelService.getActivePerspective(application.getSelectedElement()).getElementId()));
	}
}
