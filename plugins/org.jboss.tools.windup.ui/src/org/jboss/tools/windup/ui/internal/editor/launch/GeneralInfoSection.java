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
package org.jboss.tools.windup.ui.internal.editor.launch;

import static org.jboss.tools.windup.ui.internal.Messages.generalInfoDescription;
import static org.jboss.tools.windup.ui.internal.Messages.generalInfoTitle;
import static org.jboss.tools.windup.ui.internal.Messages.launchName;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.jboss.tools.windup.windup.ConfigurationElement;
import org.jboss.tools.windup.windup.WindupPackage;

/**
 * Section for configuring general Windup launch configuration information.
 */
public class GeneralInfoSection {

	@Inject	private FormToolkit toolkit;
	@Inject private ConfigurationElement configuration;
	@Inject private DataBindingContext bindingContext;
	
	private Section section;
	
	@PostConstruct
	private void createSection(Composite parent) {
		this.section = toolkit.createSection(parent, Section.DESCRIPTION|Section.TITLE_BAR);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(section);
		section.setText(generalInfoTitle);
		section.setDescription(generalInfoDescription);
		Composite client = toolkit.createComposite(section);
		toolkit.paintBordersFor(client);
		GridLayoutFactory.fillDefaults().numColumns(2).margins(0, 10).equalWidth(false).applyTo(client);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(client);
		section.setClient(client);
		
		fillSection(client);
	}
	
	@SuppressWarnings("unchecked")
	private void fillSection(Composite parent) {
		Label nameLabel = toolkit.createLabel(parent, launchName);
		nameLabel.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
		Text nameText = toolkit.createText(parent, configuration.getName());
		GridDataFactory.fillDefaults().grab(true, false).applyTo(nameText);
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(nameText),
				EMFProperties.value(WindupPackage.eINSTANCE.getNamedElement_Name()).
					observe(configuration));
	}
}
