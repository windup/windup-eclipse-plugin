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

import static org.jboss.tools.windup.model.domain.WindupConstants.DEFAULT_RULE_ID;
import static org.jboss.tools.windup.model.domain.WindupMarker.RULE_ID;
import static org.jboss.tools.windup.model.domain.WindupMarker.SEVERITY;
import static org.jboss.tools.windup.ui.internal.explorer.MarkerUtil.findJavaElementForMarker;

import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.jboss.windup.reporting.model.Severity;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

public abstract class Group<T, E extends IssueGroupNode<?>> {

	private List<Group<?, ?>> children = Lists.newArrayList();
	private Group<?, ?> parent;
	private IEclipseContext context;
	
	public Group(Group<?, ?> parent) {
		this.parent = parent;
		if (parent != null) {
			this.parent.children.add(this);
		}
	}
	
	public Group(Group<?, ?> parent, IEclipseContext context) {
		this(parent);
		this.context = context;
	}
		
	public List<IssueGroupNode<?>> createGroupNodes(List<IMarker> markers) {
		return createGroupNodesHelper(null, markers);
	}
	
	public IEclipseContext getContext() {
		if (context == null && parent != null) {
			return parent.getContext();
		}
		return context;
	}
	
	public Group<?, ?> getRoot() {
		if (parent != null) {
			return parent.getRoot();
		}
		return this;
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
			E node = createGroupNode(parent, id, groupMarkers, getContext().createChild());
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
		public ProjectGroup(Group<?, ?> parent, IEclipseContext context) {
			super(parent, context);
		}

		@Override
		protected IProject findIdentifier(IMarker marker) {
			return marker.getResource().getProject();
		}
		
		@Override
		protected ProjectGroupNode createGroupNode(IssueGroupNode<?> parent, IProject identifier, List<IMarker> markers, IEclipseContext context) {
			return new ProjectGroupNode(parent, markers, identifier);
		}
	}
	
	public static class JavaPackageGroup extends Group<IPackageFragment, JavaPackageGroupNode> {
		public JavaPackageGroup(Group<?, ?> parent, IEclipseContext context) {
			super(parent, context);
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
		protected JavaPackageGroupNode createGroupNode(IssueGroupNode<?> parent, IPackageFragment identifier, List<IMarker> markers, IEclipseContext context) {
			return new JavaPackageGroupNode(parent, markers, identifier);
		}
	}
	
	public static class JavaElementGroup extends Group<IJavaElement, JavaElementGroupNode> {
		public JavaElementGroup(Group<?, ?> parent, IEclipseContext context) {
			super(parent, context);
		}
		
		@Override
		protected IJavaElement findIdentifier(IMarker marker) {
			return JavaElementGroup.findJavaElement(marker);
		}
		
		public static IJavaElement findJavaElement(IMarker marker) {
			IJavaElement javaElement = findJavaElementForMarker(marker);
			if (javaElement != null) {
				return javaElement;
			}
			javaElement = JavaCore.create(marker.getResource());
			if (javaElement != null) {
				return javaElement;
			}
			return null;
		}

		@Override
		protected JavaElementGroupNode createGroupNode(IssueGroupNode<?> parent, IJavaElement identifier, List<IMarker> markers, IEclipseContext context) {
			return new JavaElementGroupNode(parent, markers, identifier);
		}
	}
	
	public static class ResourceGroup extends Group<IResource, ResourceGroupNode> {
		public ResourceGroup(Group<?, ?> parent, IEclipseContext context) {
			super(parent, context);
		}
		
		@Override
		protected IResource findIdentifier(IMarker marker) {
			if (JavaElementGroup.findJavaElement(marker) == null) {
				return marker.getResource();
			}
			return null;
		}

		@Override
		protected ResourceGroupNode createGroupNode(IssueGroupNode<?> parent, IResource identifier, List<IMarker> markers, IEclipseContext context) {
			return new ResourceGroupNode(parent, markers, identifier);
		}
	}
	
	public static class IssueGroup extends Group<IMarker, IssueNode> {
		public IssueGroup(Group<?, ?> parent, IEclipseContext context) {
			super(parent, context);
		}
		
		@Override
		protected IMarker findIdentifier(IMarker marker) {
			return marker;
		}
		
		@Override
		protected IssueNode createGroupNode(IssueGroupNode<?> parent, IMarker identifier, List<IMarker> markers, IEclipseContext context) {
			context.set(IssueGroupNode.class, parent);
			context.set(IMarker.class, identifier);
			return create(IssueNode.class, context);
		}
	}
	
	public static class SeverityGroup extends Group<Severity, SeverityGroupNode> {

		public SeverityGroup(Group<?, ?> parent, IEclipseContext context) {
			super(parent, context);
		}
		
		@Override
		protected Severity findIdentifier(IMarker marker) {
			String severity = marker.getAttribute(SEVERITY, Severity.POTENTIAL.toString());
			return MarkerUtil.getSeverity(severity);
		}
		
		@Override
		protected SeverityGroupNode createGroupNode(IssueGroupNode<?> parent, Severity identifier, List<IMarker> markers, IEclipseContext context) {
			return new SeverityGroupNode(parent, markers, identifier);
		}
	}
	
	public static class RuleGroup extends Group<String, RuleGroupNode> {
		
		public RuleGroup(Group<?, ?> parent, IEclipseContext context) {
			super(parent, context);
		}
		
		@Override
		protected String findIdentifier(IMarker marker) {
			return marker.getAttribute(RULE_ID, DEFAULT_RULE_ID);
		}
		
		@Override
		protected RuleGroupNode createGroupNode(IssueGroupNode<?> parent, String identifier, List<IMarker> markers, IEclipseContext context) {
			return new RuleGroupNode(parent, markers, identifier);
		}
	}
	
	protected abstract T findIdentifier(IMarker marker);
	protected abstract E createGroupNode(IssueGroupNode<?> parent, T identifier, List<IMarker> markers, IEclipseContext context);
	@SuppressWarnings("hiding")
	protected <T> T create(Class<T> clazz, IEclipseContext context) {
		return ContextInjectionFactory.make(clazz, context);
	}
}
