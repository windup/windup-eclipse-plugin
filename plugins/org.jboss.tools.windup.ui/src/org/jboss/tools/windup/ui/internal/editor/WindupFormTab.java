/*******************************************************************************
 * Copyright (c) 2019 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.editor;

import javax.inject.Inject;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.Form;
import org.jboss.tools.windup.windup.ConfigurationElement;

/**
 * Base tab for Windup's editor.
 */
public class WindupFormTab extends FormTab {
	@Inject protected Form editorForm;
	@Inject protected ConfigurationElement configuration;
	
	protected Composite createContainer(Composite parent) {
		Composite container = toolkit.createComposite(parent);
		GridLayoutFactory.fillDefaults().applyTo(container);
		return container;
	}
}
