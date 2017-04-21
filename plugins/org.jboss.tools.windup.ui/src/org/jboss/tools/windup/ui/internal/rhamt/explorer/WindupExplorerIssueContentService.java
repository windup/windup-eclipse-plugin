package org.jboss.tools.windup.ui.internal.rhamt.explorer;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.annotations.Optional;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.ui.internal.explorer.MarkerNode;
import org.jboss.tools.windup.ui.internal.explorer.MarkerUtil;
import org.jboss.tools.windup.ui.internal.rhamt.explorer.WindupExplorerContentProvider.TreeNode;
import org.jboss.tools.windup.ui.internal.rhamt.explorer.WindupExplorerContentProvider.TreeNodeBuilder;
import org.jboss.tools.windup.ui.internal.services.IssueGroupService;
import org.jboss.tools.windup.ui.internal.services.MarkerService;

/**
 * A service for computing the issue explorer's input.
 */
@Singleton
@Creatable
public class WindupExplorerIssueContentService {
	
	@Inject private IssueGroupService groupService;
	@Inject private IEclipseContext context;
	@Inject private ModelService modelService;
	@Inject @Optional private WindupExplorer issueExplorer;
	@Inject private MarkerService markerService;
	
	public void setIssuExplorer(WindupExplorer issueExplorer) {
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
		return createNodeGroups(MarkerUtil.collectWindupIssueMarkers());
	}
	
	private Object[] createNodeGroups(List<IMarker> markers) {
		TreeNodeBuilder builder = new TreeNodeBuilder(markers, issueExplorer, groupService, context, markerService, modelService.getRecentConfiguration());
		Object[] input = builder.build();
		this.nodeMap = builder.getNodeMap();
		return input;
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
