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
package org.jboss.tools.windup.ui.internal.editor;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.jboss.tools.windup.core.services.WindupService;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.windup.ConfigurationElement;

/**
 * Base class for sections.
 */
public abstract class AbstractSection {

	@Inject	protected FormToolkit toolkit;
	@Inject protected ConfigurationElement configuration;
	@Inject protected DataBindingContext bindingContext;
	@Inject protected ModelService modelService;
	@Inject protected IEventBroker broker;
	@Inject protected WindupService windupService;
	
	protected Section section;
	
	@PostConstruct
	protected void createSection(Composite parent) {
		this.section = toolkit.createSection(parent, Section.TITLE_BAR);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(section);
		Composite client = toolkit.createComposite(section);
		toolkit.paintBordersFor(client);
		GridLayoutFactory.fillDefaults().numColumns(2).margins(0, 10).equalWidth(false).applyTo(client);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(client);
		section.setClient(client);
		
		fillSection(client);
	}
	
	protected void fillSection(Composite parent) {
	}
	
	protected Label createLabel(Composite parent, String labelText) {
		Label label = toolkit.createLabel(parent, labelText);
		label.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
		return label; 
	}
}
