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

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.jboss.tools.windup.model.domain.WindupConstants;
import org.jboss.tools.windup.model.util.DocumentUtils;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.windup.Hint;
import org.jboss.tools.windup.windup.Issue;

/**
 * Service for synchronizing Windup markers with resources changes.
 */
public class MarkerSyncService implements IResourceChangeListener, IResourceDeltaVisitor {
	
	@Inject private MarkerService markerService;
	@Inject private IEventBroker broker;
	
	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		try {
			event.getDelta().accept(this);
		} catch (CoreException e) {
			WindupUIPlugin.log(e);
		}
	}
	
	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {
		IResource resource = delta.getResource();
		if (resource instanceof IFile) {
			switch (delta.getKind()) {
				case IResourceDelta.CHANGED: {
					if ((delta.getFlags() & IResourceDelta.CONTENT) != 0) {
						Map<Issue, IMarker> map = markerService.buildIssueMarkerMap(resource);
						if (!map.isEmpty()) {
							update(resource, map);
						}
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * Marks issues as stale if the issue's original line of code differs from the current. 
	 */
	private void update(IResource resource, Map<Issue, IMarker> map) {
		for (Issue issue : map.keySet()) {
			if (issue instanceof Hint && !issue.isStale() && !issue.isFixed()) {
				IMarker marker = map.get(issue);
				int lineNumber = marker.getAttribute(IMarker.LINE_NUMBER, ((Hint)issue).getLineNumber());
				if (DocumentUtils.differs(resource, lineNumber-1, issue.getOriginalLineSource())) {
					issue.setStale(true);
					try {
						Map<String, Object> attributes = marker.getAttributes();
						marker.delete();
						IMarker updatedMarker = MarkerService.createMarker(issue, resource);
						updatedMarker.setAttributes(attributes);
						updatedMarker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO);
						setStale(marker, updatedMarker);
					} catch (CoreException e) {
						WindupUIPlugin.log(e);
					}
				}
			}
		}
	}
	
	/**
	 * Updates the specified marker's severity to 'stale'.
	 */
	private void setStale(IMarker original, IMarker update) {
		Dictionary<String, Object> props = new Hashtable<String, Object>();
		props.put(WindupConstants.EVENT_ISSUE_MARKER, original);
		props.put(WindupConstants.EVENT_ISSUE_MARKER_UPDATE, update);
		broker.post(WindupConstants.MARKER_CHANGED, props);
	}
	
	@PostConstruct
	private void init() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.PRE_BUILD);
	}
	
	@PreDestroy
	private void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
	}
}
