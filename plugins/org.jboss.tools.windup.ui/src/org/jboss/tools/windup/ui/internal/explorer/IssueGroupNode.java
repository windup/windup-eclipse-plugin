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

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.jboss.tools.windup.model.domain.WindupConstants;

import com.google.common.collect.Lists;

/**
 * Base class for groupings within the issue explorer.
 */
public abstract class IssueGroupNode<T> {
	
	private List<IssueGroupNode<?>> children = Lists.newArrayList();
	private List<IMarker> issues = Lists.newArrayList();
	
	private IssueGroupNode<?> parent;
	
	public IssueGroupNode(IssueGroupNode<?> parent, List<IMarker> issues) {
		this(parent);
		this.issues = issues;
	}
	
	public IssueGroupNode(IssueGroupNode<?> parent) {
		this.parent = parent;
	}
	
	public IssueGroupNode<?> getParent() {
		return parent;
	}
	
	public List<IMarker> getIssues() {
		return issues;
	}
	
	public void addChild(IssueGroupNode<?> group) {
		children.add(group);
	}
	
	public List<IssueGroupNode<?>> getChildren() {
		return children;
	}
	
	public void setChildren(List<IssueGroupNode<?>> children) {
		this.children = children;
	}
	
	protected Dictionary<String, Object> createData() {
		Dictionary<String, Object> props = new Hashtable<String, Object>();
		props.put(WindupConstants.EVENT_ISSUE, this);
		return props;
	}
	
	public abstract String getLabel();
	public abstract T getType();
}
