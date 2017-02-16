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
package org.jboss.tools.windup.model.domain;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.jboss.tools.windup.windup.WindupPackage;

@Creatable
public class WindupDomainListener extends EContentAdapter {
	
	@Inject private IEventBroker broker;

	@Override
	public void notifyChanged(Notification notification) {
		super.notifyChanged(notification);
		
		if (notification.isTouch()) {
			return;
		}
		Object feature = notification.getFeature();
		if (feature != null && feature == WindupPackage.eINSTANCE.getWindupModel_CustomRuleRepositories()) {
			broker.send(WindupConstants.CUSTOM_RULESET_CHANGED, true);
		}
	}
}
