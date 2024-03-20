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
package org.jboss.tools.windup.ui.internal.explorer;

import java.rmi.RemoteException;

import jakarta.inject.Inject;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.xtext.util.Pair;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.runtime.WindupRmiClient;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.explorer.IssueExplorerContentProvider.ReportNode;
import org.jboss.tools.windup.ui.internal.explorer.IssueExplorerContentProvider.TreeNode;
import org.jboss.tools.windup.ui.internal.rules.xml.XMLRulesetModelUtil;
import org.jboss.tools.windup.ui.internal.services.IssueGroupService;
import org.jboss.tools.windup.windup.Issue;
import org.jboss.windup.tooling.ExecutionBuilder;
import org.w3c.dom.Node;

/**
 * Property testers for the Issue Explorer.
 */
public class IssueExplorerPropertyTesters {
	
	public static final String QUICKFIX = "hasQuickFix";  //$NON-NLS-1$
	public static final String FIXED = "isFixed";  //$NON-NLS-1$
	public static final String HIERARCHY = "isGroupByHierarchy"; //$NON-NLS-1$
	public static final String IS_GROUP = "isGroupNode"; //$NON-NLS-1$
	public static final String HAS_REPORT = "hasReport"; //$NON-NLS-1$
	public static final String HAS_RULE_DEFINITION = "hasRuleDefinition"; //$NON-NLS-1$
	
	public static class QuickFixPropertyTester extends PropertyTester {
		
		@Inject private IssueGroupService groupService;
		
		@Override
		public boolean test(Object element, String property, Object[] args, Object expectedValue) {
			if (QUICKFIX.equals(property)) {
				if (element instanceof MarkerNode) {
					MarkerNode node = (MarkerNode)element;
					if (node.getIssue().isStale()) {
						return false;
					}
					return ((MarkerNode)element).hasQuickFix();
				}
			}
			else if (FIXED.equals(property)) {
				if (element instanceof MarkerNode) {
					return !((MarkerNode)element).isFixed();
				}
			}
			else if (HIERARCHY.equals(property)) {
				return groupService.isGroupByHierarchy();
			}
			return false;
		}
	}
	
	public static class GroupPropertyTester extends PropertyTester {
		@Override
		public boolean test(Object element, String property, Object[] args, Object expectedValue) {
			if (IS_GROUP.equals(property)) {
				if (element instanceof TreeNode && !(element instanceof MarkerNode) && !(element instanceof ReportNode)) {
					return containsQuickFix((TreeNode)element);
				}
			}
			return false;
		}
	}
	
	private static boolean containsQuickFix(TreeNode node) {
		for (TreeNode child : node.getChildren()) {
			if (child instanceof MarkerNode) {
				Issue issue = ((MarkerNode)child).getIssue();
				if (QuickfixService.isIssueFixable(issue)) {
					return true;
				}
			}
			else {
				if (containsQuickFix(child)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static class ReportPropertyTester extends PropertyTester {
		@Override
		public boolean test(Object element, String property, Object[] args, Object expectedValue) {
			if (HAS_REPORT.equals(property)) {
				if (element instanceof MarkerNode) {
					MarkerNode node = (MarkerNode)element;
					Issue issue = node.getIssue();
					if (issue.getGeneratedReportLocation() != null) {
						return true;
					}
				}
			}
			return false;
		}
	}
	
	public static class RuleDefinitionPropertyTester extends PropertyTester {
		@Inject private ModelService modelService;
		@Inject private WindupRmiClient windupClient;
		@Override
		public boolean test(Object element, String property, Object[] args, Object expectedValue) {
			if (HAS_RULE_DEFINITION.equals(property)) {
				if (element instanceof MarkerNode) {
					MarkerNode node = (MarkerNode)element;
					Issue issue = node.getIssue();
					String ruleId = issue.getRuleId();
					if (windupClient.isWindupServerRunning()) {
						ExecutionBuilder executionBuilder = windupClient.getExecutionBuilder();
						try {
							Pair<Object, Node> pair = XMLRulesetModelUtil.findRuleProvider(
									ruleId, executionBuilder.getSystemRuleProviderRegistry(), modelService);
							if (pair != null) {
								return true;
							}
						} catch (RemoteException e) {
							WindupUIPlugin.log(e);
						}
					}
				}
			}
			return false;
		}
	}
}
