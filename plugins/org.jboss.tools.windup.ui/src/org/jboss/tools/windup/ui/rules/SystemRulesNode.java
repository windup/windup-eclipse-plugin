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
package org.jboss.tools.windup.ui.rules;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.jboss.windup.tooling.rules.RuleProvider;
import org.jboss.windup.tooling.rules.RuleProviderRegistry;

public class SystemRulesNode {
	
	private RuleProviderRegistry ruleProviderRegistry;
	
	public SystemRulesNode (RuleProviderRegistry ruleProviderRegistry) {
		this.ruleProviderRegistry = ruleProviderRegistry;
	}
	
	public Object[] getChildren() {
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
	
	public static class SystemRulesetFileNode {
		private RuleProvider ruleProvider;
		
		public SystemRulesetFileNode (RuleProvider ruleProvider) {
			this.ruleProvider = ruleProvider;
		}
		
		public RuleProvider getRuleProvider() {
			return ruleProvider;
		}
		
		public String getFileName() {
			return new File(ruleProvider.getOrigin()).getName();
		}
	}
}