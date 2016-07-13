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
package org.jboss.tools.windup.ui.internal.editor;

import javax.annotation.PostConstruct;

import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.windup.ui.internal.editor.launch.WindupConfigurationTab;

/**
 * Represents a stack of tabs within the Windup editor.
 */
public class WindupTabStack extends TabStack {
	
	@PostConstruct
	@Override
	protected void create(Composite parent) {
		super.create(parent);
		addTab(WindupConfigurationTab.class);
		folder.setSelection(0);
	}
}
