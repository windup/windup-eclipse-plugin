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

import java.util.List;

import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.ui.internal.rules.RulesNode.CustomRulesNode;
import org.jboss.tools.windup.ui.internal.rules.RulesNode.SystemRulesNode;
import org.jboss.tools.windup.ui.internal.server.WindupServerNode;
import org.jboss.windup.tooling.rules.RuleProviderRegistry;

import com.google.common.collect.Lists;

public class RuleRepositoryInput {

	private ModelService modelService;
	private RuleProviderRegistry systemRuleProviderRegistry;

	private List<Object> children = Lists.newArrayList();
	
	public RuleRepositoryInput (RuleProviderRegistry systemRuleProviderRegistry, ModelService modelService) {
		this.systemRuleProviderRegistry = systemRuleProviderRegistry;
		this.modelService = modelService;
	}
	
	public Object[] getChildren() {
		return children.stream().toArray(Object[]::new);
	}
	
	public static RuleRepositoryInput computeInput(RuleProviderRegistry systemRuleProviderRegistry,
			ModelService modelService) {
		RuleRepositoryInput root = new RuleRepositoryInput(systemRuleProviderRegistry, modelService);
		root.buildInput();
		return root;
	}
	
	private void buildInput() {
		children.add(new WindupServerNode());
		children.add(new SystemRulesNode(systemRuleProviderRegistry));
		children.add(new CustomRulesNode(modelService));
	}
}
