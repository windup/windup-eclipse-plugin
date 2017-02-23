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
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.jboss.tools.windup.ui.internal.rules.RulesNode.CustomRulesNode;
import org.jboss.tools.windup.ui.internal.rules.RulesNode.RulesetFileNode;
import org.jboss.tools.windup.ui.internal.rules.RulesNode.SystemRulesNode;
import org.jboss.tools.windup.ui.internal.rules.xml.XmlRulesetContentProvider;
import org.jboss.tools.windup.windup.CustomRuleProvider;
import org.jboss.windup.tooling.rules.Rule;
import org.jboss.windup.tooling.rules.RuleProvider;
import org.jboss.windup.tooling.rules.RuleProvider.RuleProviderType;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class RuleRepositoryContentProvider implements ITreeContentProvider {
	
	private Map<CustomRuleProvider, XmlRulesetContentProvider> customProviders = Maps.newHashMap();
	private TreeViewer treeViewer;
	
	public RuleRepositoryContentProvider (TreeViewer treeViewer) {
		this.treeViewer = treeViewer;
	}

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
			
			XmlRulesetContentProvider customProvider = customProviders.get(provider);
			if (customProvider == null) {
				customProvider = new XmlRulesetContentProvider(provider, treeViewer);
				customProviders.put(provider, customProvider);
			}
			
			children.addAll(Lists.newArrayList(customProvider.getRules()));
			
			return children.stream().toArray(Object[]::new);
		}
		else if (parentElement instanceof Node) {
			
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
		else if (element instanceof Node) {
			return getChildren(element).length > 0;
		}
		return false;
	}
}
