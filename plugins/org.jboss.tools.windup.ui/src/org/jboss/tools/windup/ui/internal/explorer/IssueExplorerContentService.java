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

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.annotations.Optional;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.ui.internal.explorer.IssueExplorerContentProvider.TreeNode;
import org.jboss.tools.windup.ui.internal.explorer.IssueExplorerContentProvider.TreeNodeBuilder;
import org.jboss.tools.windup.ui.internal.services.IssueGroupService;

import com.google.common.collect.Lists;

/**
 * A service for computing the issue explorer's input.
 */
@Singleton
@Creatable
public class IssueExplorerContentService {
	
	@Inject private IssueGroupService groupService;
	@Inject private IEclipseContext context;
	@Inject private ModelService modelService;
	@Inject @Optional private IssueExplorer issueExplorer;
	
	public void setIssuExplorer(IssueExplorer issueExplorer) {
		this.issueExplorer = issueExplorer;
	}
	
	private BidiMap nodeMap = new DualHashBidiMap();
	
	public boolean hasChildren(Object element) {
		if (element instanceof TreeNode) {
			TreeNode node = (TreeNode)element;
			return !node.getChildren().isEmpty();
		}
		return element instanceof IWorkspaceRoot;
	}
	
	public Object[] getChildren(Object parent) {
		if (parent instanceof TreeNode) {
			TreeNode node = (TreeNode)parent;
			return node.getChildren().stream().toArray(TreeNode[]::new);	
		}
		else if (parent instanceof IWorkspaceRoot) {
			return createNodeGroups();
		}
		return new Object[0];
	}
	
	public Object getParent(Object element) {
		if (element instanceof TreeNode) {
			return ((TreeNode)element).getParent();
		}
		return null;
	}
	
	private Object[] createNodeGroups() {
		return createNodeGroups(collectMarkers());
	}
	
	private Object[] createNodeGroups(List<IMarker> markers) {
		TreeNodeBuilder builder = new TreeNodeBuilder(markers, issueExplorer, groupService, context, modelService);
		Object[] input = builder.build();
		this.nodeMap = builder.getNodeMap();
		return input;
	}

	public List<IMarker> collectMarkers() {
		List<IMarker> markers = Lists.newArrayList();
		for (IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
			if (project.exists() && project.isAccessible()) {
				markers.addAll(getMarkers(
						WINDUP_HINT_MARKER_ID, 
						project, 
						IResource.DEPTH_INFINITE));
				markers.addAll(getMarkers(
						WINDUP_CLASSIFICATION_MARKER_ID, 
						project, 
						IResource.DEPTH_INFINITE));
			}
		}
		return markers;
	}
	
	public MarkerNode findMarkerNode(IMarker marker) {
		return (MarkerNode)nodeMap.get(marker);
	}
	
	public void updateNodeMapping(IMarker original, IMarker updatedMarker) {
		MarkerNode node = (MarkerNode)nodeMap.get(original);
		if (node != null) {
			node.setMarker(updatedMarker);
			nodeMap.remove(original);
			nodeMap.put(updatedMarker, node);
		}
	}
}
