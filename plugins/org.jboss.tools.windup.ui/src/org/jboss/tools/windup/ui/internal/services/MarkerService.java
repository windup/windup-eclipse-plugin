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

import static org.jboss.tools.windup.core.utils.WindupMarker.ELEMENT_ID;
import static org.jboss.tools.windup.core.utils.WindupMarker.WINDUP_CLASSIFICATION_MARKER_ID;
import static org.jboss.tools.windup.core.utils.WindupMarker.WINDUP_HINT_MARKER_ID;
import static org.jboss.tools.windup.model.domain.WindupConstants.LAUNCH_COMPLETED;
import static org.jboss.tools.windup.model.domain.WindupConstants.MARKERS_ATTACHED;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.jboss.tools.windup.runtime.WindupRuntimePlugin;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.explorer.MarkerUtil;
import org.jboss.tools.windup.windup.ConfigurationElement;
import org.jboss.tools.windup.windup.Input;
import org.jboss.windup.tooling.data.Classification;
import org.jboss.windup.tooling.data.Hint;

import com.google.common.collect.Sets;

/**
 * Service for annotating eclipse {@link IResource}s with Windup's generated hints and classifications.
 */
public class MarkerService {
	
	@Inject private IEventBroker broker;
	
	@Inject
	@Optional
	private void activeWindupReportView(@UIEventTopic(LAUNCH_COMPLETED) ConfigurationElement configuration) {
		try {
			deleteOldMarkers(configuration);
			populateHints(configuration);
			populateClassifications(configuration);
			broker.post(MARKERS_ATTACHED, true);
		} catch (CoreException e) {
			WindupUIPlugin.log(e);
		}
	}
	
	private void populateHints(ConfigurationElement configuration) throws CoreException {
		Set<String> paths = Sets.newHashSet();
		for (Hint hint : configuration.getWindupResult().getExecutionResults().getHints()) {
			String absolutePath = hint.getFile().getAbsolutePath();
			IFile resource = getResource(absolutePath);
			if (paths.add(absolutePath)) {
                resource.deleteMarkers(WINDUP_HINT_MARKER_ID, true, IResource.DEPTH_INFINITE);
			}
			IMarker marker = resource.createMarker(WINDUP_HINT_MARKER_ID);
			IJavaElement element = JavaCore.create(resource);
			if (element != null) {
				marker.setAttribute(ELEMENT_ID, element.getHandleIdentifier());
			}
			marker.setAttribute(IMarker.MESSAGE, hint.getHint());
			marker.setAttribute(IMarker.SEVERITY, MarkerUtil.convertSeverity(hint.getSeverity()));
			marker.setAttribute(IMarker.LINE_NUMBER, hint.getLineNumber());
            marker.setAttribute(IMarker.USER_EDITABLE, true);
            populateLinePosition(marker, hint);
		}
	}
	
	private void populateClassifications(ConfigurationElement configuration) throws CoreException {
		Set<String> paths = Sets.newHashSet();
		for (Classification classification : configuration.getWindupResult().getExecutionResults().getClassifications()) {
			String absolutePath = classification.getFile().getAbsolutePath();
			IFile resource = getResource(absolutePath);
			if (paths.add(absolutePath)) {
                //resource.deleteMarkers(WINDUP_CLASSIFICATION_MARKER_ID, true, IResource.DEPTH_INFINITE);
			}
			IMarker marker = resource.createMarker(WINDUP_CLASSIFICATION_MARKER_ID);
			IJavaElement element = JavaCore.create(resource);
			if (element != null) {
				//marker.setAttribute(ELEMENT_ID, element.getHandleIdentifier());
			}
			marker.setAttribute(IMarker.SEVERITY, MarkerUtil.convertSeverity(classification.getSeverity()));
			marker.setAttribute(IMarker.LINE_NUMBER, 1);
			marker.setAttribute(IMarker.CHAR_START, 0);
			marker.setAttribute(IMarker.CHAR_END, 0);
		}
	}
	
	private void deleteOldMarkers(ConfigurationElement configuration) throws CoreException {
		for (Input input : configuration.getInputs()) {
			URI uri = URI.createURI(input.getUri());
			Path path = new Path(uri.toPlatformString(false));
			IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(path);
			resource.deleteMarkers(WINDUP_HINT_MARKER_ID, true, IResource.DEPTH_INFINITE);
			resource.deleteMarkers(WINDUP_CLASSIFICATION_MARKER_ID, true, IResource.DEPTH_INFINITE);
		}
	}
	
	private IFile getResource(String absolutePath) {
		return ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(new Path(absolutePath));
	}
	
	private void populateLinePosition(IMarker marker, Hint hint) {
		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(hint.getFile()))) {
			int currentLine = 1;
            int pos = 0;
            int currentByte = 0;
            int lastByte = 0;

            int startPos = -1;
            int endPos = -1;
            
            while ((currentByte = bis.read()) != -1) {
                pos++;
                if (currentByte == '\n' && lastByte != '\r') {
                    currentLine++;
                    if (startPos != -1) {
                        endPos = pos;
                        break;
                    }
                }
                if (currentLine == hint.getLineNumber() && startPos == -1) {
                    startPos = pos;
                }
                lastByte = currentByte;
            }
            if (endPos == -1) {
                endPos = pos;
            }
            marker.setAttribute(IMarker.CHAR_START, startPos);
            marker.setAttribute(IMarker.CHAR_END, endPos);
        }
        catch (Exception e) {
            WindupRuntimePlugin.logError(e.getMessage(), e);
		}
	}
}
