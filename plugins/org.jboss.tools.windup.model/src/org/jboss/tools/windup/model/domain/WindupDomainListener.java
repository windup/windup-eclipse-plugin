/*******************************************************************************
 * Copyright (c) 2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.model.domain;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.jboss.tools.windup.windup.CustomRuleProvider;
import org.jboss.tools.windup.windup.WindupPackage;

import com.google.common.collect.Lists;

@Creatable
public class WindupDomainListener extends EContentAdapter {
	
	@Inject private IEventBroker broker;

	@SuppressWarnings("unchecked")
	@Override
	public void notifyChanged(Notification notification) {
		super.notifyChanged(notification);
		
		if (notification.isTouch()) {
			return;
		}
		Object feature = notification.getFeature();
		if (feature != null && feature == WindupPackage.eINSTANCE.getWindupModel_CustomRuleRepositories()) {
			RulesetChange change = null;
			if (notification.getEventType() == Notification.REMOVE_MANY || notification.getEventType() == Notification.REMOVE) {
				change = new RulesetChange(Lists.newArrayList(), true);
			}
			else if (notification.getEventType() == Notification.ADD_MANY) {
				List<CustomRuleProvider> newValues = (List<CustomRuleProvider>)notification.getNewValue();
				List<CustomRuleProvider> added = Lists.newArrayList(newValues);
				change = new RulesetChange(added, false);
			}
			else if (notification.getEventType() == Notification.ADD) {
				List<CustomRuleProvider> added = Lists.newArrayList((CustomRuleProvider)notification.getNewValue());
				change = new RulesetChange(added, false);
			}
			if (change != null) {
				broker.send(WindupConstants.CUSTOM_RULESET_CHANGED, change);
			}
		}
	}
	
	public static class RulesetChange {
		private final List<CustomRuleProvider> providers;
		private final boolean isDelete;
		public RulesetChange(List<CustomRuleProvider> providers, boolean isDelete) {
			this.providers = providers;
			this.isDelete = isDelete;
		}
		public List<CustomRuleProvider> getProviders() {
			return providers;
		}
		public boolean isDelete() {
			return isDelete;
		}
	}
}
