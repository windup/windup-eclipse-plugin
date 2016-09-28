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
package org.jboss.tools.windup.ui.internal.issues;

import static org.jboss.tools.windup.ui.internal.issues.IssueResolutions.DELETE_RESOLUTION;
import static org.jboss.tools.windup.ui.internal.issues.IssueResolutions.INSERT_RESOLUTION;
import static org.jboss.tools.windup.ui.internal.issues.IssueResolutions.REPLACE_RESOLUTION;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.core.resources.IMarker;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator2;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.windup.Issue;
import org.jboss.windup.reporting.model.QuickfixType;

import com.google.common.collect.Lists;

/**
 * Resolution generator for Windup issues.
 */
public class IssueResolutionGenerator implements IMarkerResolutionGenerator2 {
	
	@Inject private ModelService modelService;

	@Override
	public IMarkerResolution[] getResolutions(IMarker marker) {
		Issue issue = modelService.findIssue(marker);
		List<String> types = collectQuickFixTypes(issue);
		return collectResolutions(types);
	}

	@Override 
	public boolean hasResolutions(IMarker marker) {
		return getResolutions(marker).length > 0;
	}
	
	private IMarkerResolution[] collectResolutions(List<String> types) {
		List<IMarkerResolution> resolutions = Lists.newArrayList();
		for (String type : types) {
			if (type.equals(QuickfixType.DELETE_LINE.toString())) {
				resolutions.add(DELETE_RESOLUTION);
			}
			else if (type.equals(QuickfixType.REPLACE.toString())) {
				resolutions.add(REPLACE_RESOLUTION);
			}
			else if (type.endsWith(QuickfixType.INSERT_LINE.toString())) {
				resolutions.add(INSERT_RESOLUTION);
			}
		}
		return resolutions.toArray(new IMarkerResolution[resolutions.size()]);
	}
	
	private List<String> collectQuickFixTypes(Issue issue) {
		return issue.getQuickFixes().stream().
					flatMap(i -> issue.getQuickFixes().stream()).
					map(f -> f.getQuickFixType()).
					collect(Collectors.toList());
	}
}
