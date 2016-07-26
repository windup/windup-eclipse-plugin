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

import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;

/**
 * Represents a project grouping.
 */
public class ProjectGroupNode extends IssueGroupNode<IProject> {
	
	private IProject project;
	
	public ProjectGroupNode(IssueGroupNode<?> parent, List<IMarker> issues, IProject project) {
		super(parent, issues);
		this.project = project;
	}
	
	@Override
	public String getLabel() {
		return project.getName();
	}
	
	public IProject getType() {
		return project;
	}
}
