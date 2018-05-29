/*******************************************************************************
 * Copyright (c) 2018 Red Hat, Inc.
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

import org.eclipse.ui.PartInitException;
import org.jboss.tools.windup.windup.ConfigurationElement;
import org.jboss.tools.windup.windup.IgnorePattern;
import org.jboss.tools.windup.windup.WindupFactory;
import org.junit.Test;

public class UserIgnoreTests extends WindupUiTest {

	@Test
	public void testDefaultIgnoreFile() throws PartInitException {
		ConfigurationElement configuration = super.createRunConfiguration();
		String location = configuration.getInputs().get(0).getLocation();
		location = location + "/*";
		IgnorePattern ignore = WindupFactory.eINSTANCE.createIgnorePattern();
		ignore.setPattern(location);
		ignore.setEnabled(true);
		configuration.getIgnorePatterns().add(ignore);
		
		System.out.println("Ignoring location: " + location);
		runWindup(configuration);
		assertTrue("Expected NOT to have Issues after running Windup, but issues were found.", 
				configuration.getWindupResult().getIssues().isEmpty());
	}
	
	@Test
	public void testUserSpecifiedIgnoreFile() throws PartInitException {
		ConfigurationElement configuration = super.createRunConfiguration();
		String location = configuration.getInputs().get(0).getLocation();
		location = location + "/*";
		IgnorePattern ignore = WindupFactory.eINSTANCE.createIgnorePattern();
		ignore.setPattern(location);
		ignore.setEnabled(true);
		configuration.getIgnorePatterns().add(ignore);
		
		System.out.println("Ignoring location: " + location);
		runWindup(configuration);
		assertTrue("Expected NOT to have Issues after running Windup, but issues were found.", 
				configuration.getWindupResult().getIssues().isEmpty());
	}
}
