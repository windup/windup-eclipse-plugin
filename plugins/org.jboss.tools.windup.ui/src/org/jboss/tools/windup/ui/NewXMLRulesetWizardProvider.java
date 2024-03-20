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
package org.jboss.tools.windup.ui;

import jakarta.inject.Inject;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.jboss.tools.windup.ui.internal.rules.NewXMLRulesetWizard;
import org.jboss.tools.windup.ui.internal.rules.CreateRulesetOperation;
import com.google.inject.Injector;
import com.google.inject.Provider;

public class NewXMLRulesetWizardProvider implements Provider<NewXMLRulesetWizard> {

	@Inject private Injector injector;
	
	@Override
	public NewXMLRulesetWizard get() {
		NewXMLRulesetWizard wizard = new NewXMLRulesetWizard();
		injector.injectMembers(wizard);
		wizard.init(ContextInjectionFactory.make(CreateRulesetOperation.class, 
				WindupUIPlugin.getDefault().getContext()));
		return wizard;
	}
}
