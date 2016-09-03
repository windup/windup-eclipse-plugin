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
package org.jboss.tools.windup.ui.internal.services;

import static org.jboss.tools.windup.model.domain.WindupConstants.LAUNCH_COMPLETED;
import static org.jboss.tools.windup.model.domain.WindupConstants.MARKERS_CHANGED;
import static org.jboss.tools.windup.model.domain.WindupMarker.CLASSIFICATION;
import static org.jboss.tools.windup.model.domain.WindupMarker.COLUMN;
import static org.jboss.tools.windup.model.domain.WindupMarker.CONFIGURATION_ID;
import static org.jboss.tools.windup.model.domain.WindupMarker.DESCRIPTION;
import static org.jboss.tools.windup.model.domain.WindupMarker.EFFORT;
import static org.jboss.tools.windup.model.domain.WindupMarker.ELEMENT_ID;
import static org.jboss.tools.windup.model.domain.WindupMarker.HINT;
import static org.jboss.tools.windup.model.domain.WindupMarker.LENGTH;
import static org.jboss.tools.windup.model.domain.WindupMarker.LINE;
import static org.jboss.tools.windup.model.domain.WindupMarker.RULE_ID;
import static org.jboss.tools.windup.model.domain.WindupMarker.SEVERITY;
import static org.jboss.tools.windup.model.domain.WindupMarker.SOURCE_SNIPPET;
import static org.jboss.tools.windup.model.domain.WindupMarker.TITLE;
import static org.jboss.tools.windup.model.domain.WindupMarker.URI_ID;
import static org.jboss.tools.windup.model.domain.WindupMarker.WINDUP_CLASSIFICATION_MARKER_ID;
import static org.jboss.tools.windup.model.domain.WindupMarker.WINDUP_HINT_MARKER_ID;

import java.lang.reflect.InvocationTargetException;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.explorer.MarkerUtil;
import org.jboss.tools.windup.windup.Classification;
import org.jboss.tools.windup.windup.ConfigurationElement;
import org.jboss.tools.windup.windup.Hint;
import org.jboss.tools.windup.windup.Input;
import org.jboss.tools.windup.windup.Issue;

/**
 * Service for annotating eclipse {@link IResource}s with Windup's generated hints and classifications.
 */
@Singleton
@Creatable
public class MarkerService {
	
	@Inject private IEventBroker broker;
	
	@Inject
	@Optional
	public void updateMarkers(@UIEventTopic(LAUNCH_COMPLETED) ConfigurationElement configuration) {
		try {
			WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
				@Override
				protected void execute(IProgressMonitor monitor)
						throws CoreException, InvocationTargetException, InterruptedException {
					monitor.beginTask(Messages.generateIssues, getTotalIssueCount(configuration));
					monitor.subTask(Messages.generateIssues);
					createWindupMarkers(configuration, monitor);
				}
			};
			new ProgressMonitorDialog(Display.getDefault().getActiveShell()).run(true, false, op);
			broker.post(MARKERS_CHANGED, true);
		} catch (InvocationTargetException | InterruptedException e) {
			Display.getDefault().syncExec(() -> {
				MessageDialog.openInformation(Display.getDefault().getActiveShell(), 
						Messages.launchErrorTitle, Messages.markersCreateError);
			});
			WindupUIPlugin.log(e);
		}
	}
	
	public int getTotalIssueCount(ConfigurationElement configuration) {
		int count = 0;
		for (Input input : configuration.getInputs()) {
			if (input.getWindupResult() != null) {
				count += input.getWindupResult().getIssues().size();
			}
		}
		return count;
	}
	
	private void createWindupMarkers(ConfigurationElement configuration, IProgressMonitor monitor) throws CoreException {
		int count = 0;
		for (Input input : configuration.getInputs()) {
			for (Issue issue : input.getWindupResult().getIssues()) {
				String absolutePath = issue.getFileAbsolutePath();
				IFile resource = getResource(absolutePath);
				String type = issue instanceof Classification ? WINDUP_CLASSIFICATION_MARKER_ID : WINDUP_HINT_MARKER_ID;
				IMarker marker = resource.createMarker(type);
				marker.setAttribute(CONFIGURATION_ID, configuration.getName());
				
				IJavaElement element = JavaCore.create(resource);
				if (element != null) {
					marker.setAttribute(ELEMENT_ID, element.getHandleIdentifier());
				}
				marker.setAttribute(URI_ID, EcoreUtil.getURI(issue).toString());
				marker.setAttribute(IMarker.SEVERITY, MarkerUtil.convertSeverity(issue.getSeverity()));
				marker.setAttribute(SEVERITY, issue.getSeverity());
	            marker.setAttribute(RULE_ID, issue.getRuleId());
	            marker.setAttribute(EFFORT, issue.getEffort());
				
				if (issue instanceof Hint) {
					Hint hint = (Hint)issue;
					
					marker.setAttribute(IMarker.MESSAGE, hint.getTitle());
					marker.setAttribute(IMarker.LINE_NUMBER, hint.getLineNumber());
					
					marker.setAttribute(TITLE, hint.getTitle());
					marker.setAttribute(HINT, hint.getHint());
					marker.setAttribute(LINE, hint.getLineNumber());
					marker.setAttribute(COLUMN, hint.getColumn());
					marker.setAttribute(LENGTH, hint.getLength());
					
					marker.setAttribute(SOURCE_SNIPPET, hint.getSourceSnippet());
				}
				else {
					Classification classification = (Classification)issue;
					marker.setAttribute(IMarker.MESSAGE, classification.getClassification());
					marker.setAttribute(CLASSIFICATION, classification.getClassification());
					marker.setAttribute(DESCRIPTION, classification.getDescription());
					
					marker.setAttribute(IMarker.LINE_NUMBER, 1);
					marker.setAttribute(IMarker.CHAR_START, 0);
					marker.setAttribute(IMarker.CHAR_END, 0);
				}
	            marker.setAttribute(IMarker.USER_EDITABLE, false);
	            monitor.worked(++count);
			}
		}
	}
	
	public void deleteAllWindupMarkers() {
		for (IProject input : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
			if (input.isAccessible()) {
				deleteWindupMarkers(input);
			}
		}
		broker.post(MARKERS_CHANGED, true);
	}
	
	private void deleteWindupMarkers(IResource input) {
		try {
			input.deleteMarkers(WINDUP_HINT_MARKER_ID, true, IResource.DEPTH_INFINITE);
			input.deleteMarkers(WINDUP_CLASSIFICATION_MARKER_ID, true, IResource.DEPTH_INFINITE);
		} catch (CoreException e) {
			WindupUIPlugin.log(e);
		}
	}
	
	private IFile getResource(String absolutePath) {
		return ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(new Path(absolutePath));
	}
}
