/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.rules;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.windup.CustomRuleProvider;
import org.jboss.windup.tooling.rules.RuleProvider;
import org.jboss.windup.tooling.rules.RuleProvider.RuleProviderType;
import org.jboss.windup.tooling.rules.RuleProviderRegistry;

public class RulesNode {
	
	protected RuleProviderRegistry ruleProviderRegistry;
	
	public RulesNode (RuleProviderRegistry ruleProviderRegistry) {
		this.ruleProviderRegistry = ruleProviderRegistry;
	}

	public Object[] getChildren() {
		if (ruleProviderRegistry == null) {
			return new Object[0];
		}
		List<RuleProvider> ruleProviders = ruleProviderRegistry.getRuleProviders().stream().filter(provider -> {
			return isFileBasedProvider(provider);
		}).collect(Collectors.toList());
		sortRuleProviders(ruleProviders);
		return ruleProviders.stream().toArray(RuleProvider[]::new);
	}
	
	private boolean isFileBasedProvider(RuleProvider provider) {
		switch (provider.getRuleProviderType()) {
			case GROOVY:
			case XML:
				return true;
			default: 
				return false;
		}
	}
	
	private void sortRuleProviders(List<RuleProvider> ruleProviders) {
		Collections.sort(ruleProviders, (RuleProvider provider1, RuleProvider provider2) -> {
			if (provider1.getProviderID() == null) {
				return -1;
			}
			if (provider2.getProviderID() == null) {
				return 1;
			}
			return provider1.getProviderID().compareTo(provider2.getProviderID());
		});
	}
	
	public static class SystemRulesNode extends RulesNode {
		public SystemRulesNode (RuleProviderRegistry registry) {
			super (registry);
		}
	}
	
	public static class CustomRulesNode extends RulesNode {
				
		private ModelService modelService;
		
		public CustomRulesNode (ModelService modelService) {
			super (null);
			this.modelService = modelService;
		}
		
		@Override
		public Object[] getChildren() {
			return modelService.getModel().getCustomRuleRepositories().stream().toArray(CustomRuleProvider[]::new);
		}
	}
	
	public static class RulesetFileNode {
		
		private File file;
		private RuleProviderType type;

		public RulesetFileNode (File file, RuleProviderType type) {
			this.file = file;
			this.type = type;
		}
		
		public String getName() {
			return file.getName();
		}
		
		public File getFile() {
			return file;
		}
		
		public RuleProviderType getRuleProviderType () {
			return type;
		}
	}
}
