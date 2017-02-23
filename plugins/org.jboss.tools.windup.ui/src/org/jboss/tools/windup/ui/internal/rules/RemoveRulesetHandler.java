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

import javax.inject.Inject;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.windup.CustomRuleProvider;

public class RemoveRulesetHandler extends Action {
	
	@Inject private ModelService modelService;
	private List<CustomRuleProvider> providers;
	
	public void setProviders(List<CustomRuleProvider> providers) {
		this.providers = providers;
	}
	
	@Override
	public String getText() {
		return Messages.removeRuleset;
	}
	
	@Override
	public ImageDescriptor getImageDescriptor() {
		return WindupUIPlugin.getImageDescriptor(WindupUIPlugin.IMG_REMOVE_RULESET);
	}
	
	@Override
	public void run() {
		modelService.write(() -> {
			modelService.getModel().getCustomRuleRepositories().removeAll(providers);
		});
	}
}
