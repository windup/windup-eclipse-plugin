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

import static org.jboss.tools.windup.core.utils.WindupMarker.*;
import org.eclipse.core.resources.IMarker;
import org.jboss.windup.reporting.model.Severity;

/**
 * Represents a marker grouping.
 */
public class IssueNode extends IssueGroupNode<IMarker> {

	private IMarker marker;
	
	public IssueNode(IssueGroupNode<?> parent, IMarker marker) {
		super(parent);
		this.marker = marker;
	}
	
	@Override
	public String getLabel() {
		return marker.getAttribute(IMarker.MESSAGE, "unknown issue");
	}
	
	@Override
	public IMarker getType() {
		return marker;
	}
	
	public int getSeverity() {
		return marker.getAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
	}
}
