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
import org.jboss.windup.reporting.model.Severity;

/**
 * Represents a severity grouping.
 */
public class SeverityGroupNode extends IssueGroupNode<Severity> {

	private Severity element;
	
	public SeverityGroupNode(IssueGroupNode<?> parent, List<IMarker> issues, Severity element) {
		super(parent, issues);
		this.element = element;
	}
	
	@Override
	public String getLabel() {
		return element.toString();
	}
	
	@Override
	public Severity getType() {
		return element;
	}
}
