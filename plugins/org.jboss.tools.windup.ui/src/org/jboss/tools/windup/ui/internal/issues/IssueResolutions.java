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
package org.jboss.tools.windup.ui.internal.issues;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.views.markers.WorkbenchMarkerResolution;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.explorer.QuickfixService;
import org.jboss.tools.windup.ui.internal.services.MarkerService;
import org.jboss.tools.windup.windup.Hint;
import org.jboss.tools.windup.windup.Issue;
import org.jboss.tools.windup.windup.QuickFix;

import com.google.common.collect.Lists;

/**
 * Windup's quick fix resolutions.
 */
public class IssueResolutions {

	public static class FirstQuickFixResolution extends WorkbenchMarkerResolution {
		
		private static String LABEL = "Apply first quick fix for the selected migration issue."; //$NON-NLS-1$ 
		
		private QuickfixService quickfixService;
		private MarkerService markerService;
		private Issue issue;
		
		public FirstQuickFixResolution(QuickfixService quickfixService, MarkerService markerService, Issue issue) {
			this.quickfixService = quickfixService;
			this.markerService = markerService;
			this.issue = issue;
		}
		
		@Override
		public String getDescription() {
			return LABEL;
		}
		
		@Override
		public String getLabel() {
			return LABEL;
		}
		
		@Override
		public Image getImage() {
			return WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_WINDUP);
		}
		
		@Override
		public IMarker[] findOtherMarkers(IMarker[] markers) {
			List<IMarker> others = Lists.newArrayList();
			for (IMarker marker : markers) {
				Hint hint = markerService.find(marker);
				if (this.issue != hint && !hint.isStale() && !hint.isFixed() && !hint.getQuickFixes().isEmpty()) {
					others.add(marker);
				}
			}
			return others.toArray(new IMarker[others.size()]);
		}
		
		@Override
		public void run(IMarker[] markers, IProgressMonitor monitor) {
			WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
				@Override
				protected void execute(IProgressMonitor monitor)
						throws CoreException, InvocationTargetException, InterruptedException {
					for (IMarker marker : markers) {
						Hint hint = markerService.find(marker);
						for (QuickFix quickfix : hint.getQuickFixes()) {
							quickfixService.applyQuickFix(quickfix);
						}
					}
				}
			};
			try {
				new ProgressMonitorDialog(Display.getDefault().getActiveShell()).run(false, false, op);
			} catch (InvocationTargetException | InterruptedException e) {
				WindupUIPlugin.log(e);
			}
		}
		
		@Override
		public void run(IMarker marker) {
			// do nothing;
		}
	}
}
