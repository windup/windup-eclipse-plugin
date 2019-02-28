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
package org.jboss.tools.windup.ui.tests;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.jboss.tools.windup.model.util.DocumentUtils;
import org.jboss.tools.windup.windup.ConfigurationElement;
import org.jboss.tools.windup.windup.Hint;
import org.jboss.tools.windup.windup.QuickFix;
import org.jboss.windup.tooling.data.QuickfixType;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the MarkerSyncService.
 */
public class MarkerSyncServiceTest extends WindupUiTest {

	@Test
	public void testStaleIssue() {
		ConfigurationElement configuration = super.createRunConfiguration();
		runWindup(configuration);
		// get the first hint that contains a replacement quick fix.
		Hint hint = configuration.getWindupResult().getIssues().
				stream().filter(issue -> { 
					return issue instanceof Hint;
				}).map(issue -> (Hint)issue).
				filter(issue -> { 
					if (!issue.getQuickFixes().isEmpty()) {
						QuickFix fix = issue.getQuickFixes().get(0);
						if (QuickfixType.REPLACE.toString().equals(fix.getQuickFixType())) {
							return true;
						}
					}
					return false;
				}).findFirst().get();
		QuickFix quickFix = hint.getQuickFixes().get(0);
		IMarker marker = (IMarker)hint.getMarker();
		IResource original = marker.getResource();
		IResource newResource = quickfixService.getQuickFixedResource(null, quickFix, marker);
		DocumentUtils.replace(original, newResource);
		try {
			ResourcesPlugin.getWorkspace().build(IncrementalProjectBuilder.INCREMENTAL_BUILD, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		Assert.assertTrue(hint.isStale());
	}
}
