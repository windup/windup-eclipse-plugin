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
package org.jboss.tools.windup.ui.internal.rules;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.windup.CustomRuleProvider;

public class NewXMLRuleHandler extends Action {
	
	@Inject private IEclipseContext context;
	private CustomRuleProvider provider;
	
	public void setProvider(CustomRuleProvider provider) {
		this.provider = provider;
	}
	
	@Override
	public String getText() {
		return Messages.newXmlRule;
	}
	
	@Override
	public ImageDescriptor getImageDescriptor() {
		return WindupUIPlugin.getImageDescriptor(WindupUIPlugin.IMG_NEW_XML_RULE);
	}
	
	@Override
	public void run() {
		NewXMLRuleWizard wizard = ContextInjectionFactory.make(NewXMLRuleWizard.class, context);
		wizard.setRuleProvider(provider);
		new WizardDialog(Display.getDefault().getActiveShell(), wizard).open();
	}
}
