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
package org.jboss.tools.windup.ui.internal.explorer;

//import jakarta.inject.Inject;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.jboss.tools.windup.model.domain.WindupConstants;
import org.jboss.tools.windup.model.domain.WindupMarker;
import org.jboss.tools.windup.ui.internal.explorer.IssueConstants.Severity;
import org.jboss.tools.windup.ui.internal.explorer.IssueExplorerContentProvider.TreeNode;
import org.jboss.tools.windup.windup.Hint;
import org.jboss.tools.windup.windup.Issue;

public class MarkerNode extends TreeNode {
	
	private Issue issue;
	private IMarker marker;
	
//	@Inject
	public MarkerNode(IMarker marker, Issue issue) {
		super (marker);
		this.marker = marker;
		this.issue = issue;
	}
	
	public void setMarker(IMarker marker) {
		this.marker = marker;
	}
	
	public String getTitle() {
		if (issue instanceof Hint) {
			return ((Hint)issue).getTitle();
		}
		return "classification"; //$NON-NLS-1$ 
	}
	
	public String getFileName() {
		return marker.getResource().getName();
	}
	
	public int getLineNumber() {
		if (issue instanceof Hint) {
			return ((Hint)issue).getLineNumber();
		}
		return 0;
	}
	
	public Issue getIssue() {
		return issue;
	}
	
	public int getSeverity() {
		String severity = marker.getAttribute(WindupMarker.SEVERITY, Severity.OPTIONAL.toString());
		return MarkerUtil.convertSeverity(severity);
	}
	
	public boolean hasQuickFix() {
		return !issue.getQuickFixes().isEmpty() && !issue.isFixed();
	}
	
	public boolean isFixed() {
		return issue.isFixed();
	}
	
	public IMarker getMarker() {
		return marker;
	}
	
	public IResource getResource() {
		return marker.getResource();
	}
	
	public String getRule() {
		return marker.getAttribute(WindupMarker.RULE_ID, WindupConstants.DEFAULT_RULE_ID);
	}
}