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
import org.eclipse.jdt.core.IPackageFragment;

/**
 * Represents a package grouping.
 */
public class PackageGroupNode extends IssueGroupNode<IPackageFragment> {
	
	private IPackageFragment element;
	
	public PackageGroupNode(IssueGroupNode<?> parent, List<IMarker> issues, IPackageFragment element) {
		super(parent, issues);
		this.element = element;
	}
	
	@Override
	public String getLabel() {
		String name = "unknown package";
        if (element != null) {
        	String temp = element.getElementName();
        	if (temp != null && temp.length() != 0) {
        		name = temp;
        	}
        }
        return name;
	}
	
	@Override
	public IPackageFragment getType() {
		return element;
	}
}
