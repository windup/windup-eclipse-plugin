/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.tests;

import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.jboss.tools.windup.ui.internal.explorer.QuickFixUtil;
import org.jboss.tools.windup.windup.ConfigurationElement;
import org.jboss.tools.windup.windup.Hint;
import org.jboss.tools.windup.windup.QuickFix;
import org.jboss.windup.reporting.model.QuickfixType;
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
		Hint hint = configuration.getInputs().get(0).getWindupResult().getIssues().
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
		QuickFixUtil.applyReplacementQuickFix(hint.getQuickFixes().get(0), hint);
		try {
			ResourcesPlugin.getWorkspace().build(IncrementalProjectBuilder.INCREMENTAL_BUILD, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
		}
		Assert.assertTrue(hint.isStale());
	}
}
