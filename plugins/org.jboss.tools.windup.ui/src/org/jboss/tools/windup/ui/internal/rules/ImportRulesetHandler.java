/*******************************************************************************
 * Copyright (c) 2021 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.rules;

import jakarta.inject.Inject;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.handlers.HandlerUtil;

public class ImportRulesetHandler extends AbstractHandler {
		
	@Inject private IEclipseContext context;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ImportExistingRulesetWizard wizard = ContextInjectionFactory.make(ImportExistingRulesetWizard.class, context);
		new WizardDialog(HandlerUtil.getActiveShell(event), wizard).open();
		return null;
	}
}
