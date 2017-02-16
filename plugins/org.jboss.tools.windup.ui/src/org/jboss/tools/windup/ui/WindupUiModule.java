package org.jboss.tools.windup.ui;

import org.eclipse.xtext.service.AbstractGenericModule;
import org.jboss.tools.windup.ui.rules.NewXMLRulesetWizard;
import org.jboss.tools.windup.ui.rules.RulesetGenerator;

import com.google.inject.Provider;

public class WindupUiModule extends AbstractGenericModule {
	
	public Class<? extends RulesetGenerator> bindXmlRulesetGenerator() {
		return RulesetGenerator.class;
	}
	
	public Class<? extends Provider<NewXMLRulesetWizard>> provideNewXMLRulesetWizardProvider() {
		return NewXMLRulesetWizardProvider.class;
	}
}
