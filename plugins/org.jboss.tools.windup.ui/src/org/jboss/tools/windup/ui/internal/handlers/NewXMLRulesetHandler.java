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
package org.jboss.tools.windup.ui.internal.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.rules.NewXMLRulesetWizard;

public class NewXMLRulesetHandler extends AbstractHandler {
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		NewXMLRulesetWizard wizard = WindupUIPlugin.getDefault().getInjector().getInstance(NewXMLRulesetWizard.class);
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection == null || !(selection instanceof IStructuredSelection)) {
			selection = new StructuredSelection();
		}
		wizard.init(PlatformUI.getWorkbench(), (IStructuredSelection)selection);
		new WizardDialog(HandlerUtil.getActiveShell(event), wizard).open();
		return null;
	}
}
