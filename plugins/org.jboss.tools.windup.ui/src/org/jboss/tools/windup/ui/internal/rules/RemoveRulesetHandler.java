/*******************************************************************************
 * Copyright (c) 2019 Red Hat, Inc.
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

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.model.domain.WorkspaceResourceUtils;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.windup.CustomRuleProvider;

import com.google.common.collect.Lists;

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
		List<IResource> toDelete = Lists.newArrayList();
		for (CustomRuleProvider provider : providers) {
			IResource resource = WorkspaceResourceUtils.getResource(provider.getWorkspaceResourceLocation());
			if (resource != null && resource.exists() && resource.isLinked()) {
				toDelete.add(resource);
			}
		}
		
		WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {
			@Override
			public void execute(IProgressMonitor monitor) {
				for (IResource resource : toDelete) {
					try {
						String msg = "Deleting resource: " + resource.getLocation().toString(); //$NON-NLS-1$
						 WindupUIPlugin.getDefault().getLog().log(
				                    new Status(IStatus.INFO, WindupUIPlugin.PLUGIN_ID, msg));
						resource.delete(true, new NullProgressMonitor());
					}
					catch (Exception e) {
						WindupUIPlugin.log(e);
					}
				}
			}
		};
		
		try {
			new ProgressMonitorDialog(Display.getDefault().getActiveShell()).run(false, false, operation);
		}
		catch (Exception e) {
			WindupUIPlugin.log(e);
		}
		
	}
}
