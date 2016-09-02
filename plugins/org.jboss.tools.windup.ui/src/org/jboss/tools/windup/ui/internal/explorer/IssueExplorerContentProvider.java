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

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jdt.internal.ui.navigator.IExtensionStateConstants;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.internal.navigator.NavigatorContentServiceContentProvider;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonContentProvider;
import org.eclipse.ui.navigator.IExtensionStateModel;
import org.eclipse.ui.navigator.INavigatorContentService;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.model.domain.WindupConstants;
import org.jboss.tools.windup.model.domain.WindupMarker;
import org.jboss.tools.windup.ui.internal.services.IssueGroupService;
import org.jboss.tools.windup.windup.Hint;
import org.jboss.tools.windup.windup.Issue;
import org.jboss.windup.reporting.model.Severity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Content provider for the Windup explorer.
 */
@SuppressWarnings("restriction")
public class IssueExplorerContentProvider implements ICommonContentProvider {
	
	@Inject private IssueExplorerContentService contentService;
	
	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parent) {
		return contentService.getChildren(parent);
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}
	
	@Override
	public Object getParent(Object element) {
		return contentService.getParent(element);
	}

	@Override
	public boolean hasChildren(Object element) {
		return contentService.hasChildren(element);
	}

	@Override
	public void restoreState(IMemento aMemento) {}
	@Override
	public void saveState(IMemento aMemento) {}
	@Override
	public void init(ICommonContentExtensionSite aConfig) {}
	
	public static class TreeNodeBuilder {
		
		private List<IMarker> markers;
		private NavigatorContentServiceContentProvider contentProvider;
		private IssueGroupService groupService;
		private ModelService modelService;
		private IEclipseContext context;
		
		public TreeNodeBuilder(List<IMarker> markers, IssueExplorer explorer, 
				IssueGroupService groupService, IEclipseContext context,
				ModelService modelService) {
			this.markers = markers;
			this.groupService = groupService;
			this.modelService = modelService;
			this.contentProvider = (NavigatorContentServiceContentProvider)explorer.getCommonViewer().getContentProvider();
			contentProvider.getParents(ResourcesPlugin.getWorkspace().getRoot());
			// TODO: Correct this temporary hack to get flat package layout.
			INavigatorContentService contentService = explorer.getCommonViewer().getNavigatorContentService();
			IExtensionStateModel m = contentService.findStateModel(IssueConstants.JDT_CONTENT);
			m.setBooleanProperty(IExtensionStateConstants.Values.IS_LAYOUT_FLAT, true);
			this.context = context;
		}
		
		public TreeNode[] build() {
			TreeNode root = new TreeNode(null);
			for (IMarker marker : markers) {
				List<TreePath> paths = Lists.newArrayList(contentProvider.getParents(marker.getResource()));
				if (!paths.isEmpty()) {
					TreePath path = paths.get(0);
					build(root, root, path, marker, 1);
				}
			}
			List<TreeNode> children = root.getChildren();
			if (children.isEmpty()) {
				return new TreeNode[0];
			}
			return children.stream().toArray(TreeNode[]::new);
		}
		
		public void build(TreeNode root, TreeNode node, TreePath path, IMarker marker, int index) {
			if (groupService.isGroupByHierarchy()) {
				// build the hierarchy.
				if (index < path.getSegmentCount()) {
					Object segment = path.getSegment(index);
					TreeNode child = node.getChildPath(segment);
					if (child == null) {
						child = new TreeNode(segment);
						node.addChild(child);
					}
					build(root, child, path, marker, ++index);
					return;
				}
			}
			TreeNode parent = groupService.isGroupByHierarchy() ? node : root;
			if (groupService.isGroupByFile()) {
				TreeNode resourceNode = parent.getChildPath(marker.getResource());
				if (resourceNode == null) {
					resourceNode = new TreeNode(marker.getResource());
					parent.addChild(resourceNode);
				}
				parent = resourceNode;
			}
			
			if (groupService.isGroupBySeverity()) {
				Object segment = marker.getAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO);
				String severity = marker.getAttribute(SEVERITY, Severity.POTENTIAL.toString());
				TreeNode severityNode = parent.getChildPath(segment);
				if (severityNode == null) {
					severityNode = new SeverityNode(segment, severity);
					parent.addChild(severityNode);
				}
				parent = severityNode;
			}
			
			if (groupService.isGroupByRule()) {
				String ruleId = marker.getAttribute(WindupMarker.RULE_ID, WindupConstants.DEFAULT_RULE_ID);
				String title = "";
				Issue issue = modelService.findIssue(marker);
				if (issue instanceof Hint) {
					title = ((Hint)issue).getTitle();
				}
				String segment = ruleId+title;
				TreeNode ruleNode = parent.getChildPath(segment);
				if (ruleNode == null) {
					ruleNode = new RuleGroupNode(segment, ruleId, title);
					parent.addChild(ruleNode);
				}
				parent = ruleNode;
			}
			
			IEclipseContext child = context.createChild();
			child.set(IMarker.class, marker);
			MarkerNode markerNode = ContextInjectionFactory.make(MarkerNode.class, child);
			parent.addChild(markerNode);
		}
	}
	
	public static class TreeNode {
		
		private Object segment;
		private TreeNode parent;
		private Map<Object, TreeNode> children = Maps.newHashMap();
		
		public TreeNode(Object segment) {
			this.segment = segment;
		}
		
		public List<TreeNode> getChildren() {
			return Lists.newArrayList(children.values());
		}
		
		public TreeNode getParent() {
			return parent;
		}
		
		public Object getSegment() {
			return segment;
		}
		
		public TreeNode getChildPath(Object path) {
			return children.get(path);
		}
		
		public void addChild(TreeNode node) {
			node.parent = this;
			children.put(node.segment, node);
		}
		
		protected Dictionary<String, Object> createData() {
			Dictionary<String, Object> props = new Hashtable<String, Object>();
			props.put(WindupConstants.EVENT_ISSUE, this);
			return props;
		}
		
		public boolean isLeafParent() {
			if (children.isEmpty()) {
				return false;
			}
			Map.Entry<Object, TreeNode> child = children.entrySet().iterator().next();
			return child.getValue().getChildren().isEmpty();
		}
	}
	
	public static class SeverityNode extends TreeNode {
		private String severity;
		public SeverityNode(Object segment, String severity) {
			super(segment);
			this.severity = severity;
		}
		public String getSeverity() {
			return severity;
		}
	}
	
	public static class RuleGroupNode extends TreeNode {
		private String ruleId;
		private String title;
		public RuleGroupNode(Object segment, String ruleId, String title) {
			super(segment);
			this.ruleId = ruleId;
			this.title = title;
		}
		public String getRuleId() {
			return ruleId;
		}
		public String getTitle() {
			return title;
		}
	}
	
	public static class TreeGroupNode extends TreeNode {
		public TreeGroupNode(Object segment) {
			super(segment);
		}
	}
}
