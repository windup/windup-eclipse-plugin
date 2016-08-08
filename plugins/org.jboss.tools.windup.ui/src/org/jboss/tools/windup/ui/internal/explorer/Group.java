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

import static org.jboss.tools.windup.ui.internal.explorer.MarkerUtil.findJavaElementForMarker;
import static org.jboss.tools.windup.model.domain.WindupConstants.*;

import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.jboss.windup.reporting.model.Severity;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import static org.jboss.tools.windup.core.utils.WindupMarker.*;

public abstract class Group<T, E extends IssueGroupNode<?>> {

	private List<Group<?, ?>> children = Lists.newArrayList();
	private Group<?, ?> parent;

	public Group(Group<?, ?> parent) {
		this.parent = parent;
		if (parent != null) {
			this.parent.children.add(this);
		}
	}
		
	public List<IssueGroupNode<?>> createGroupNodes(List<IMarker> markers) {
		return createGroupNodesHelper(null, markers);
	}
	
	private List<IssueGroupNode<?>> createGroupNodesHelper(IssueGroupNode<?> parent, List<IMarker> markers) {
		List<IssueGroupNode<?>> nodes = Lists.newArrayList();
		Multimap<T, IMarker> groups = ArrayListMultimap.create();
		markers.forEach(marker -> {
			T identifier = findIdentifier(marker);
			if (identifier != null) {
				groups.put(identifier, marker);
			}
		});
		for (T id : groups.keySet()) {
			List<IMarker> groupMarkers = Lists.newArrayList(groups.get(id));
			E node = createGroupNode(parent, id, groupMarkers);
			if (!children.isEmpty()) {
				List<IssueGroupNode<?>> nodeChildren = Lists.newArrayList();
				for (Group<?, ?> child : children) {
					List<IssueGroupNode<?>> tempChildren = child.createGroupNodesHelper(node, groupMarkers);
					if (!tempChildren.isEmpty()) {
						nodeChildren.addAll(tempChildren);
					}
				}
				node.setChildren(nodeChildren);
			}
			nodes.add(node);
		}
		return nodes;
	}
	
	public static class ProjectGroup extends Group<IProject, ProjectGroupNode> {
		public ProjectGroup(Group<?, ?> parent) {
			super(parent);
		}

		@Override
		protected IProject findIdentifier(IMarker marker) {
			return marker.getResource().getProject();
		}
		
		@Override
		protected ProjectGroupNode createGroupNode(IssueGroupNode<?> parent, IProject identifier, List<IMarker> markers) {
			return new ProjectGroupNode(parent, markers, identifier);
		}
	}
	
	public static class PackageGroup extends Group<IPackageFragment, PackageGroupNode> {
		public PackageGroup(Group<?, ?> parent) {
			super(parent);
		}
		
		@Override
		protected IPackageFragment findIdentifier(IMarker marker) {
			IJavaElement javaElement = findJavaElementForMarker(marker);
			if (javaElement == null) {
				javaElement = JavaCore.create(marker.getResource());
			}
			if (javaElement != null) {
				return (IPackageFragment) javaElement.getAncestor(IJavaElement.PACKAGE_FRAGMENT);
			}
			return null;
		}
		
		@Override
		protected PackageGroupNode createGroupNode(IssueGroupNode<?> parent, IPackageFragment identifier, List<IMarker> markers) {
			return new PackageGroupNode(parent, markers, identifier);
		}
	}
	
	public static class ClassGroup extends Group<IJavaElement, ClassGroupNode> {
		public ClassGroup(Group<?, ?> parent) {
			super(parent);
		}
		
		@Override
		protected IJavaElement findIdentifier(IMarker marker) {
			IJavaElement javaElement = findJavaElementForMarker(marker);
			if (javaElement != null) {
				return javaElement;
			}
			return JavaCore.create(marker.getResource());
		}

		@Override
		protected ClassGroupNode createGroupNode(IssueGroupNode<?> parent, IJavaElement identifier, List<IMarker> markers) {
			return new ClassGroupNode(parent, markers, identifier);
		}
	}
	
	public static class IssueGroup extends Group<IMarker, IssueNode> {
		public IssueGroup(Group<?, ?> parent) {
			super(parent);
		}
		
		@Override
		protected IMarker findIdentifier(IMarker marker) {
			return marker;
		}
		
		@Override
		protected IssueNode createGroupNode(IssueGroupNode<?> parent, IMarker identifier, List<IMarker> markers) {
			return new IssueNode(parent, identifier);
		}
	}
	
	public static class SeverityGroup extends Group<Severity, SeverityGroupNode> {
		private Severity identifier;
		public SeverityGroup(Group<?, ?> parent, Severity severity) {
			super(parent);
			this.identifier = severity;
		}
		
		@Override
		protected Severity findIdentifier(IMarker marker) {
			String severity = marker.getAttribute(SEVERITY, Severity.POTENTIAL.toString());
			Severity markerSeverity = MarkerUtil.getSeverity(severity);
			if (markerSeverity.equals(this.identifier)) {
				return identifier;
			}
			return null;
		}
		
		@Override
		protected SeverityGroupNode createGroupNode(IssueGroupNode<?> parent, Severity identifier, List<IMarker> markers) {
			return new SeverityGroupNode(parent, markers, identifier);
		}
	}
	
	public static class RuleGroup extends Group<String, RuleGroupNode> {
		
		public RuleGroup(Group<?, ?> parent) {
			super(parent);
		}
		
		@Override
		protected String findIdentifier(IMarker marker) {
			return marker.getAttribute(RULE_ID, DEFAULT_RULE_ID);
		}
		
		@Override
		protected RuleGroupNode createGroupNode(IssueGroupNode<?> parent, String identifier, List<IMarker> markers) {
			return new RuleGroupNode(parent, markers, identifier);
		}
	}
	
	protected abstract T findIdentifier(IMarker marker);
	protected abstract E createGroupNode(IssueGroupNode<?> parent, T identifier, List<IMarker> markers);
}
