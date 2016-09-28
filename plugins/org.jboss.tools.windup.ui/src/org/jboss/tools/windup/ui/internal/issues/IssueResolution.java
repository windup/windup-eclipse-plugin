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

import org.eclipse.core.resources.IMarker;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.markers.WorkbenchMarkerResolution;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.windup.Issue;
import org.jboss.tools.windup.windup.QuickFix;

/**
 * Marker resolution for Windup issues.
 */
public class IssueResolution extends WorkbenchMarkerResolution {

	private ModelService modelService;
	private Issue issue;
	private QuickFix quickFix;
	
	private String description;
	private String label;
	
	public IssueResolution(Issue issue, QuickFix quickFix, ModelService modelService) {
		this.issue = issue;
		this.quickFix = quickFix;
		this.modelService = modelService;
		this.description = computeIssueDescription();
		this.label = computeIssueLabel();
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Image getImage() {
		return WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_WINDUP);
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public void run(IMarker marker) {
		Issue issue = modelService.findIssue(marker);
	}

	@Override
	public IMarker[] findOtherMarkers(IMarker[] markers) {
		for (IMarker marker : markers) {
			
		}
		return new IMarker[0];
	}
	
	private String computeIssueDescription() {
		return "";
	}
	
	private String computeIssueLabel() {
		return "";
	}
}
