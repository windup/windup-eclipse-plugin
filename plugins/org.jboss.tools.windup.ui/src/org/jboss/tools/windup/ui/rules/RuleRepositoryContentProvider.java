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

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.jboss.tools.windup.ui.rules.SystemRulesNode.SystemRulesetFileNode;
import org.jboss.windup.tooling.rules.Rule;
import org.jboss.windup.tooling.rules.RuleProvider;

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
		else if (parentElement instanceof RuleProvider) {
			List<Rule> rules = ((RuleProvider)parentElement).getRules();
			List<Object> children = Lists.newArrayList(rules);
			children.add(0, new SystemRulesetFileNode((RuleProvider)parentElement));
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
		else if (element instanceof RuleProvider) {
			return !((RuleProvider)element).getRules().isEmpty();
		}
		return false;
	}
}
