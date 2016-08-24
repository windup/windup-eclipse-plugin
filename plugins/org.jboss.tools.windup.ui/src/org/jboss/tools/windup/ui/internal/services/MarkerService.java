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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.model.domain.WorkspaceResourceUtils;
import org.jboss.tools.windup.runtime.WindupRuntimePlugin;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.explorer.MarkerUtil;
import org.jboss.tools.windup.windup.Classification;
import org.jboss.tools.windup.windup.ConfigurationElement;
import org.jboss.tools.windup.windup.Hint;
import org.jboss.tools.windup.windup.Input;
import org.jboss.tools.windup.windup.Issue;

import com.google.common.collect.Lists;
import com.google.inject.Singleton;

/**
 * Service for annotating eclipse {@link IResource}s with Windup's generated hints and classifications.
 */
@Singleton
@Creatable
public class MarkerService {
	
	@Inject private IEventBroker broker;
	@Inject private ModelService modelService;
	
	@Inject
	@Optional
	private void updateMarkers(@UIEventTopic(LAUNCH_COMPLETED) ConfigurationElement configuration) {
		try {
			populateHints(configuration);
			broker.post(MARKERS_CHANGED, true);
		} catch (CoreException e) {
			WindupUIPlugin.log(e);
		}
	}
	
	private void populateHints(ConfigurationElement configuration) throws CoreException {
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
					
					//populateLinePosition(marker, hint.getLineNumber(), new File(hint.getFileAbsolutePath()));
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
	            
			}
		}
	}
	
	public void deleteAllWindupMarkers() {
		List<Input> all = Lists.newArrayList();
		for (ConfigurationElement configuration : modelService.getModel().getConfigurationElements()) {
			all.addAll(configuration.getPreviousInput());
		}
		flattenInput(all).forEach(input -> deleteWindupMarkers(input));
		broker.post(MARKERS_CHANGED, true);
	}
	
	private List<Input> flattenInput(List<Input> all) {
		return all.stream().filter(unique(input -> input.getName())).collect(Collectors.toList());
	}
	
	private static Predicate<Input> unique(Function<Input, String> extractor) {
		Map<String, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(extractor.apply(t), Boolean.TRUE) == null;
	}
	
	private void deleteWindupMarkers(Input input) {
		try {
			IResource resource = WorkspaceResourceUtils.findResource(input.getUri());
			if (resource != null && resource.exists()) {
				resource.deleteMarkers(WINDUP_HINT_MARKER_ID, true, IResource.DEPTH_INFINITE);
				resource.deleteMarkers(WINDUP_CLASSIFICATION_MARKER_ID, true, IResource.DEPTH_INFINITE);
			}
		} catch (CoreException e) {
			WindupUIPlugin.log(e);
		}
	}
	
	private IFile getResource(String absolutePath) {
		return ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(new Path(absolutePath));
	}
	
	private void populateLinePosition(IMarker marker, int lineNumber, File file) {
		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
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
                if (currentLine == lineNumber && startPos == -1) {
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
