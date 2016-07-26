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

import static org.jboss.tools.windup.ui.internal.explorer.MarkerUtil.*;

import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

public abstract class Group<T, E extends IssueGroupNode<?>> {

	private Group<?, ?> child;
	private Group<?, ?> parent;

	public Group(Group<?, ?> parent) {
		this.parent = parent;
		if (parent != null) {
			this.parent.child = this;
		}
	}
		
	public List<IssueGroupNode<?>> createGroupNodes(List<IMarker> markers) {
		return createGroupNodesHelper(null, markers);
	}
	
	private List<IssueGroupNode<?>> createGroupNodesHelper(IssueGroupNode<?> parent, List<IMarker> markers) {
		List<IssueGroupNode<?>> nodes = Lists.newArrayList();
		Multimap<T, IMarker> groups = ArrayListMultimap.create();
		markers.forEach(marker -> groups.put(findIdentifier(marker), marker));
		for (T id : groups.keySet()) {
			List<IMarker> groupMarkers = Lists.newArrayList(groups.get(id));
			E node = createGroupNode(parent, id, groupMarkers);
			if (child != null) {
				node.setChildren(child.createGroupNodesHelper(node, groupMarkers));
			}
			nodes.add(node);
		}
		return nodes;
	}
	
	protected abstract T findIdentifier(IMarker marker);
	protected abstract E createGroupNode(IssueGroupNode<?> parent, T identifier, List<IMarker> markers);
	
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
}
