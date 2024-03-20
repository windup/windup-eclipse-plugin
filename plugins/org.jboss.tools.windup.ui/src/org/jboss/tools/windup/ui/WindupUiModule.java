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

import org.jboss.tools.windup.ui.internal.explorer.IssueExplorerContentService;
//import org.eclipse.xtext.service.AbstractGenericModule;
import org.jboss.tools.windup.ui.internal.rules.NewXMLRulesetWizard;
import org.jboss.tools.windup.ui.internal.rules.RulesetGenerator;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;

public class WindupUiModule extends AbstractModule {
	
	public Class<? extends RulesetGenerator> bindXmlRulesetGenerator() {
		return RulesetGenerator.class;
	}
	
//	public Class<? extends RulesetGenerator> bindXmlRulesetGenerator() {
//		return RulesetGenerator.class;
//	}
	public Class<? extends Provider<NewXMLRulesetWizard>> provideNewXMLRulesetWizardProvider() {
		return NewXMLRulesetWizardProvider.class;
	}
	
//	
	@Provides
    static Integer provideIssueExplorer() {
      return 3;
    }

    @Provides
    static String provideMessage() {
      return "hello world";
    }
}
