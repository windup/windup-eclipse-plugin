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
package org.jboss.tools.windup.ui.internal.rules;

import java.rmi.RemoteException;

import javax.inject.Inject;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IMarker;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.xtext.util.Pair;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.runtime.WindupRmiClient;
import org.jboss.tools.windup.ui.internal.explorer.IssueExplorerContentProvider.TreeNode;
import org.jboss.tools.windup.ui.internal.rules.xml.XMLRulesetModelUtil;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.explorer.MarkerNode;
import org.jboss.tools.windup.ui.internal.services.MarkerService;
import org.jboss.tools.windup.windup.Issue;
import org.jboss.windup.tooling.ExecutionBuilder;
import org.w3c.dom.Node;

/**
 * Handler responsible for opening a rule set within an editor and traversing to a specific
 * rule within the rule set. 
 */
public class OpenRuleDefinitionHandler extends AbstractHandler {
	
	@Inject private MarkerService markerService;
	@Inject private WindupRmiClient windupClient;
	@Inject private ModelService modelService;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		TreeSelection selection = (TreeSelection) HandlerUtil.getCurrentSelection(event);
		TreeNode node = (TreeNode)selection.getFirstElement();
		if (node instanceof MarkerNode) {
			IMarker marker = ((MarkerNode) node).getMarker();
			Issue issue = markerService.find(marker);
			String ruleId = issue.getRuleId();
			if (windupClient.isWindupServerRunning()) {
				ExecutionBuilder executionBuilder = windupClient.getExecutionBuilder();
				try {
					Pair<Object, Node> pair = XMLRulesetModelUtil.findRuleProvider(
							ruleId, executionBuilder.getSystemRuleProviderRegistry(), modelService);
					if (pair != null) {
						XMLRulesetModelUtil.openRuleInEditor(pair.getFirst(), pair.getSecond());
					}
				} catch (RemoteException e) {
					WindupUIPlugin.log(e);
				}
			}
		}
		return null;
	}
}
