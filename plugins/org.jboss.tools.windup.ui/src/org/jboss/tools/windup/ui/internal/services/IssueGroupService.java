/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.services;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.jboss.tools.windup.model.domain.WindupConstants;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.osgi.service.prefs.BackingStoreException;

/**
 * Service for managing the tree node groupings from within the Issue Explorer.
 */
@Singleton
@Creatable
public class IssueGroupService {
	
	private IEclipsePreferences preferences = InstanceScope.INSTANCE.getNode(WindupUIPlugin.PLUGIN_ID);

	private static final String GROUP_BY_HIERARCHY = "GROUP_BY_HIERARCH"; //$NON-NLS-1$
	private static final String GROUP_BY_SEVERITY = "GROUP_BY_SEVERITY"; //$NON-NLS-1$
	private static final String GROUP_BY_RULE = "GROUP_BY_RULE"; //$NON-NLS-1$
	private static final String GROUP_BY_FILE = "GROUP_BY_FILE"; //$NON-NLS-1$
	
	private boolean groupByHierarchy;
	private boolean groupBySeverity;
	private boolean groupByRule;
	private boolean groupByFile;
	
	@Inject	private IEventBroker broker;
	
	@PostConstruct
	private void start() {
		this.groupByHierarchy = preferences.getBoolean(GROUP_BY_HIERARCHY, true);
		this.groupBySeverity = preferences.getBoolean(GROUP_BY_SEVERITY, false);
		this.groupByRule = preferences.getBoolean(GROUP_BY_RULE, false);
		this.groupByFile = preferences.getBoolean(GROUP_BY_FILE, false);
	}
	
	@PreDestroy
	public void save() {
		preferences.putBoolean(GROUP_BY_HIERARCHY, groupByHierarchy);
		preferences.putBoolean(GROUP_BY_SEVERITY, groupBySeverity);
		preferences.putBoolean(GROUP_BY_RULE, groupByRule);
		preferences.putBoolean(GROUP_BY_FILE, groupByFile);
		try {
			preferences.flush();
		} catch (BackingStoreException e) {
			WindupUIPlugin.log(e);
		}
	}
	
	public void setGroupByHierachy(boolean groupByHierarchy) {
		this.groupByHierarchy = groupByHierarchy;
		notifyChanged();
	}
	
	public void setGroupBySeverity(boolean groupBySeverity) {
		this.groupBySeverity = groupBySeverity;
		notifyChanged();
	}
	
	public void setGroupByRule(boolean groupByRule) {
		this.groupByRule = groupByRule;
		notifyChanged();
	}
	
	public void setGroupByFile(boolean groupByFile, boolean notify) {
		this.groupByFile = groupByFile;
		if (notify) {
			notifyChanged();
		}
	}
	
	public boolean isGroupByHierarchy() {
		return groupByHierarchy;
	}
	
	public boolean isGroupBySeverity() {
		return groupBySeverity;
	}
	
	public boolean isGroupByRule() {
		return groupByRule;
	}
	
	public boolean isGroupByFile() {
		return groupByFile;
	}
	
	private void notifyChanged() {
		save();
		broker.post(WindupConstants.GROUPS_CHANGED, true);
	}
}
