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
package org.jboss.tools.windup.ui.internal.explorer;

import static org.jboss.tools.windup.model.domain.WindupMarker.SEVERITY;
import static org.jboss.tools.windup.ui.internal.Messages.issueDeleteError;
import static org.jboss.tools.windup.ui.internal.Messages.operationError;

import javax.inject.Inject;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.ide.IDE;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.model.domain.WindupConstants;
import org.jboss.tools.windup.model.domain.WindupMarker;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.explorer.IssueExplorerContentProvider.TreeNode;
import org.jboss.tools.windup.windup.Hint;
import org.jboss.tools.windup.windup.Issue;
import org.jboss.windup.reporting.model.Severity;

public class MarkerNode extends TreeNode {
	
	private Issue issue;
	private IMarker marker;
	
	@Inject private IEventBroker broker;
	
	@Inject
	public MarkerNode(IMarker marker, ModelService modelService) {
		super (marker);
		this.marker = marker;
		this.issue = modelService.findIssue(marker);
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
		String severity = marker.getAttribute(SEVERITY, Severity.OPTIONAL.toString());
		return MarkerUtil.convertSeverity(severity);
	}
	
	public String getQuickFix() {
		return issue.getQuickFix();
	}
	
	public boolean hasQuickFix() {
		IMarkerResolution[] resolutions = IDE.getMarkerHelpRegistry().getResolutions(marker);
		return resolutions.length > 0 && !issue.isFixed();
	}
	
	public void markAsFixed() {
		issue.setFixed(true);
		try {
			marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO);
		} catch (CoreException e) {
			WindupUIPlugin.log(e);
		}
		broker.post(WindupConstants.ISSUE_CHANGED, createData());
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
	
	public void delete() {
		try {
			marker.delete();
		} catch (CoreException e) {
			WindupUIPlugin.log(e);
			MessageDialog.open(MessageDialog.ERROR, Display.getDefault().getActiveShell(), 
					operationError, issueDeleteError, SWT.NONE);
		}
	}
}