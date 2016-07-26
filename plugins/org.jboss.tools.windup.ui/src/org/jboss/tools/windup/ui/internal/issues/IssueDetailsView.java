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
package org.jboss.tools.windup.ui.internal.issues;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.resources.IMarker;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * View for displaying the details of an Issue.
 */
public class IssueDetailsView {
	
	private FormToolkit toolkit;
	private Label detailsLabel;

	@PostConstruct
	private void create(Composite parent) {
		toolkit = new FormToolkit(Display.getDefault());
		Composite container = toolkit.createComposite(parent);
		GridLayoutFactory.fillDefaults().applyTo(container);
		this.detailsLabel = toolkit.createLabel(container, "");
		GridDataFactory.fillDefaults().grab(true, false).applyTo(detailsLabel);
		
	}
	
	@Inject
	private void showIssueDetails(@Optional IMarker marker) {
		if (this.detailsLabel != null && marker != null) {
			String hint = marker.getAttribute(IMarker.MESSAGE, "no issue details available.");
			this.detailsLabel.setText(hint);
		}
	}
}
