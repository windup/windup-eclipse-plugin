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

import static org.jboss.tools.windup.model.domain.WindupMarker.WINDUP_CLASSIFICATION_MARKER_ID;
import static org.jboss.tools.windup.model.domain.WindupMarker.WINDUP_HINT_MARKER_ID;
import static org.jboss.tools.windup.ui.internal.explorer.MarkerUtil.getMarkers;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.jboss.tools.windup.ui.internal.services.IssueGroupService;

import com.google.common.collect.Lists;

/**
 * A service for computing the issue explorer's input.
 */
@Singleton
@Creatable
public class IssueExplorerContentService {
	
	@Inject private IssueGroupService groupService;
	
	public boolean hasChildren(Object element) {
		if (element instanceof IssueGroupNode) {
			return !((IssueGroupNode<?>)element).getChildren().isEmpty();
		}
		return element instanceof IWorkspaceRoot;
	}
	
	public Object[] getChildren(Object parent) {
		if (parent instanceof IssueGroupNode) {
			List<IssueGroupNode<?>> children = ((IssueGroupNode<?>)parent).getChildren();
			return children.stream().toArray(Object[]::new);
		}
		else if (parent instanceof IWorkspaceRoot) {
			return createNodeGroups();
		}
		return new Object[0];
	}
	
	public Object getParent(Object element) {
		if (element instanceof IssueGroupNode) {
			return ((IssueGroupNode<?>)element).getParent();
		}
		return null;
	}
	
	private Object[] createNodeGroups() {
		List<IMarker> markers = Lists.newArrayList();
		for (IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
			markers.addAll(getMarkers(
					WINDUP_HINT_MARKER_ID, 
					project, 
					IResource.DEPTH_INFINITE));
			markers.addAll(getMarkers(
					WINDUP_CLASSIFICATION_MARKER_ID, 
					project, 
					IResource.DEPTH_INFINITE));
		}
		return createNodeGroups(markers);
	}
	
	private Object[] createNodeGroups(List<IMarker> markers) {
		List<IssueGroupNode<?>> nodes = groupService.getRoot().createGroupNodes(markers);
		return nodes.toArray(new IssueGroupNode[nodes.size()]);
	}
}
