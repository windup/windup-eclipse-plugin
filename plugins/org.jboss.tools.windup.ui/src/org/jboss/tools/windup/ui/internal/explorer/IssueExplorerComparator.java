/*******************************************************************************
 * Copyright (c) 2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.explorer;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.views.navigator.ResourceComparator;
import org.jboss.tools.windup.ui.internal.explorer.IssueExplorerContentProvider.ReportNode;
import org.jboss.tools.windup.ui.internal.explorer.IssueExplorerContentProvider.RootReportNode;
import org.jboss.tools.windup.ui.internal.explorer.IssueExplorerContentProvider.TreeNode;

import com.google.common.base.Objects;

/**
 * Issue explorer comparator.
 */
public class IssueExplorerComparator extends ResourceComparator {
	
	public IssueExplorerComparator() {
		super(NAME);
	}

	@Override
	public int compare(Viewer viewer, Object o1, Object o2) {
		if (o1 instanceof MarkerNode && o2 instanceof MarkerNode) {
			MarkerNode n1 = (MarkerNode)o1;
			MarkerNode n2 = (MarkerNode)o2;
			o1 = n1.getMarker().getResource();
			o2 = n2.getMarker().getResource();
			if (Objects.equal(o1, o2)) {
				return compareLineNumber(n1, n2);
			}
		}
		if (o1 instanceof ReportNode || o1 instanceof RootReportNode) {
			return -1;
		}
		if (o2 instanceof ReportNode || o1 instanceof RootReportNode) {
			return 1;
		}
		if (o1 instanceof TreeNode) {
			o1 = ((TreeNode)o1).getSegment();
		}
		if (o2 instanceof TreeNode) {
			o2 = ((TreeNode)o2).getSegment();
		}
		return super.compare(viewer, o1, o2);
	}
	
	private int compareLineNumber(MarkerNode n1, MarkerNode n2) {
		return new Integer(n1.getLineNumber()).compareTo(n2.getLineNumber());
	}
}
