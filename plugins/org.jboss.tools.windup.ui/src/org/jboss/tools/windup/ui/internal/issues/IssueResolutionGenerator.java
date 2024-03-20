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
package org.jboss.tools.windup.ui.internal.issues;

import jakarta.inject.Inject;

import org.eclipse.core.resources.IMarker;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator2;
import org.jboss.tools.windup.ui.internal.explorer.QuickfixService;
import org.jboss.tools.windup.ui.internal.issues.IssueResolutions.FirstQuickFixResolution;
import org.jboss.tools.windup.ui.internal.services.MarkerService;
import org.jboss.tools.windup.windup.Issue;

/**
 * Resolution generator for Windup issues.
 */
public class IssueResolutionGenerator implements IMarkerResolutionGenerator2 {
	
	@Inject private MarkerService markerService;
	@Inject private QuickfixService quickfixService;

	@Override
	public IMarkerResolution[] getResolutions(IMarker marker) {
		Issue issue = markerService.find(marker);
		if (issue != null) {
			return collectResolutions(issue);
		}
		return new IMarkerResolution[0];
	}

	@Override 
	public boolean hasResolutions(IMarker marker) {
		return getResolutions(marker).length > 0;
	}
	
	private IMarkerResolution[] collectResolutions(Issue issue) {
		if (!issue.getQuickFixes().isEmpty()) {
			return new IMarkerResolution[]{new FirstQuickFixResolution(quickfixService, markerService, issue)};
		}
		return new IMarkerResolution[0];
	}
}
