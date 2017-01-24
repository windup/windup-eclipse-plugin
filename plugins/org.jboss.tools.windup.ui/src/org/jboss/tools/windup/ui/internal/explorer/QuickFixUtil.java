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

import java.util.Dictionary;
import java.util.Hashtable;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.text.Document;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.windup.model.domain.WindupConstants;
import org.jboss.tools.windup.model.util.DocumentUtils;
import org.jboss.tools.windup.ui.internal.services.MarkerService;
import org.jboss.tools.windup.windup.Hint;
import org.jboss.tools.windup.windup.Issue;
import org.jboss.tools.windup.windup.QuickFix;
import org.jboss.windup.tooling.data.QuickfixType;

/**
 * Utility for interacting with quick fixes.
 */
public class QuickFixUtil {
	
	public static void applyQuickFix(QuickFix quickFix, Hint hint, IMarker marker, 
			IEventBroker broker, MarkerService markerService) {
		IResource original = marker.getResource();
		IResource newResource = QuickFixUtil.getQuickFixedResource(original, quickFix, hint);
		DocumentUtils.replace(original, newResource);
		QuickFixUtil.setFixed(hint, marker, broker, markerService);
	}
	
	public static void setFixed(Issue issue, IMarker marker, IEventBroker broker, MarkerService markerService) {
		Dictionary<String, Object> props = new Hashtable<String, Object>();
		IMarker updatedMarker = markerService.createFixedMarker(marker, issue);
		props.put(WindupConstants.EVENT_ISSUE_MARKER, marker);
		props.put(WindupConstants.EVENT_ISSUE_MARKER_UPDATE, updatedMarker);
		broker.post(WindupConstants.MARKER_CHANGED, props);
	}
	
	public static IResource getQuickFixedResource(IResource original, QuickFix quickFix, Hint hint) {
		TempProject project = new TempProject();
		if (QuickfixType.REPLACE.toString().equals(quickFix.getQuickFixType())) {
			int lineNumber = hint.getLineNumber()-1;
			String searchString = quickFix.getSearchString();
			String replacement = quickFix.getReplacementString();
			Document document = DocumentUtils.replace(original, lineNumber, searchString, replacement);
			return project.createResource(document.get());
		}
		else if (QuickfixType.DELETE_LINE.toString().equals(quickFix.getQuickFixType())) {
			int lineNumber = hint.getLineNumber()-1;
			Document document = DocumentUtils.deleteLine(original, lineNumber);
			return project.createResource(document.get());
		}
		else if (QuickfixType.INSERT_LINE.toString().equals(quickFix.getQuickFixType())) {
			int lineNumber = hint.getLineNumber();
			lineNumber = lineNumber > 1 ? lineNumber - 2 : lineNumber - 1;
			String newLine = quickFix.getReplacementString();
			Document document = DocumentUtils.insertLine(original, lineNumber, newLine);
			return project.createResource(document.get());
		}
		return null;
	}
	
	public static void previewQuickFix(Hint hint, IMarker marker, IEventBroker broker,
			MarkerService markerService) {
		IResource left = marker.getResource();
		Shell shell = Display.getCurrent().getActiveShell();
		QuickFix firstQuickFix = hint.getQuickFixes().get(0); 
		IResource right = QuickFixUtil.getQuickFixedResource(left, firstQuickFix, hint);
		QuickFixDiffDialog dialog = new QuickFixDiffDialog(shell, left, right, hint);
		if (dialog.open() == IssueConstants.APPLY_FIX) {
			QuickFixUtil.applyQuickFix(dialog.getQuickFix(), hint, marker, broker, markerService);
		}
	}
	
	public static boolean isIssueFixable(Issue issue) {
		return !issue.isStale() && !issue.isFixed() && !issue.getQuickFixes().isEmpty();
	}
}
