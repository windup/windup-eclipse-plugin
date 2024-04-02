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
package org.jboss.tools.windup.ui.internal.services;

import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
//import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.ui.internal.rules.xml.XMLRulesetModelUtil;
import org.jboss.tools.windup.windup.CustomRuleProvider;
import org.jboss.tools.windup.windup.WindupModel;

import com.google.common.collect.Lists;

@Singleton
@Creatable
public class RefreshService implements IResourceChangeListener {
	
//	@Inject private ModelService modelService;
	private ModelService modelService = MarkerSyncService.getInstance("org.jboss.tools.windup.model.domain.ModelService");

	@PostConstruct
	private void init() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.POST_CHANGE);
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		IResourceDelta delta = event.getDelta();
		IResourceDelta[] projectDeltas = delta.getAffectedChildren();
		for (int i = 0; i < projectDeltas.length; i++) {
			IResourceDelta projectDelta = projectDeltas[i];
			IResource resource = projectDelta.getResource();
			syncCustomRulesetRepositories(resource);					
		}
	}
	
	private void syncCustomRulesetRepositories(IResource resource) {
		List<CustomRuleProvider> providersToDelete = Lists.newArrayList();
		WindupModel model = modelService.getModel();
		for (Iterator<CustomRuleProvider> iter = model.getCustomRuleRepositories().iterator(); iter.hasNext();) {
			CustomRuleProvider provider = iter.next();
			IFile file = XMLRulesetModelUtil.getRuleset(provider);
			if (file == null || !file.exists()) {
				providersToDelete.add(provider);
			}
		}
		if (!providersToDelete.isEmpty()) {
			modelService.write(() -> {
				model.getCustomRuleRepositories().removeAll(providersToDelete);
			});
		}
	}
}
