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
import org.eclipse.core.resources.IResource;

/**
 * Represents a resource grouping.
 */
public class ResourceGroupNode extends IssueGroupNode<IResource> {
	
	private IResource resource;

	public ResourceGroupNode(IssueGroupNode<?> parent, List<IMarker> issues, IResource resource) {
		super(parent, issues);
		this.resource = resource;
	}
	
	@Override
	public String getLabel() {
		return resource.getName();
	}
	
	@Override
	public IResource getType() {
		return resource;
	}
}
