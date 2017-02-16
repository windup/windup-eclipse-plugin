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
import java.util.List;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.jboss.tools.windup.ui.rules.RulesNode.CustomRulesNode;
import org.jboss.tools.windup.ui.rules.RulesNode.RulesetFileNode;
import org.jboss.tools.windup.ui.rules.RulesNode.SystemRulesNode;
import org.jboss.tools.windup.windup.CustomRuleProvider;
import org.jboss.windup.tooling.rules.Rule;
import org.jboss.windup.tooling.rules.RuleProvider;
import org.jboss.windup.tooling.rules.RuleProvider.RuleProviderType;

import com.google.common.collect.Lists;

@Creatable
public class RuleRepositoryContentProvider implements ITreeContentProvider {

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof RuleRepositoryInput) {
			return ((RuleRepositoryInput)parentElement).getChildren();
		}
		else if (parentElement instanceof SystemRulesNode) {
			return ((SystemRulesNode)parentElement).getChildren();
		}
		else if (parentElement instanceof CustomRulesNode) {
			return ((CustomRulesNode)parentElement).getChildren();
		}
		else if (parentElement instanceof RuleProvider) {
			List<Rule> rules = ((RuleProvider)parentElement).getRules();
			List<Object> children = Lists.newArrayList(rules);
			RuleProvider provider = (RuleProvider)parentElement;
			children.add(0, new RulesetFileNode(new File(provider.getOrigin()), provider.getRuleProviderType()));
			return children.stream().toArray(Object[]::new);
		}
		else if (parentElement instanceof CustomRuleProvider) {
			CustomRuleProvider provider = (CustomRuleProvider)parentElement;
			List<Object> children = Lists.newArrayList();
			children.add(new RulesetFileNode(new File(provider.getLocationURI()), RuleProviderType.XML));
			return children.stream().toArray(Object[]::new);
		}
		return new Object[0];
	}
	
	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof RuleRepositoryInput) {
			return ((RuleRepositoryInput)element).getChildren().length > 0;
		}
		else if (element instanceof SystemRulesNode) {
			return ((SystemRulesNode)element).getChildren().length > 0;
		}
		else if (element instanceof CustomRulesNode) {
			return ((CustomRulesNode)element).getChildren().length > 0;
		}
		else if (element instanceof RuleProvider || element instanceof CustomRuleProvider) {
			return true; // file node
		}
		return false;
	}
}
