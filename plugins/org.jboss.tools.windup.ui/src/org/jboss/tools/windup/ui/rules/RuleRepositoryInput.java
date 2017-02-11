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

import java.util.List;

import org.jboss.windup.tooling.rules.RuleProviderRegistry;

import com.google.common.collect.Lists;

public class RuleRepositoryInput {

	private RuleProviderRegistry ruleProviderRegistry;
	
	private List<Object> children = Lists.newArrayList();
	
	public RuleRepositoryInput (RuleProviderRegistry ruleProviderRegistry) {
		this.ruleProviderRegistry = ruleProviderRegistry;
	}
	
	public Object[] getChildren() {
		return children.stream().toArray(Object[]::new);
	}
	
	public static RuleRepositoryInput computeInput(RuleProviderRegistry ruleProviderRegistry) {
		RuleRepositoryInput root = new RuleRepositoryInput(ruleProviderRegistry);
		root.buildInput();
		return root;
	}
	
	private void buildInput() {
		if (ruleProviderRegistry != null) {
			SystemRulesNode node = new SystemRulesNode(ruleProviderRegistry);
			children.add(node);
		}
	}
}
