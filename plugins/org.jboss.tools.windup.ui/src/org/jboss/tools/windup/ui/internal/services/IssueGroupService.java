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
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.jboss.tools.windup.model.domain.WindupConstants;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.explorer.Group;
import org.jboss.tools.windup.ui.internal.explorer.Group.JavaElementGroup;
import org.jboss.tools.windup.ui.internal.explorer.Group.IssueGroup;
import org.jboss.tools.windup.ui.internal.explorer.Group.ResourceGroup;
import org.jboss.tools.windup.ui.internal.explorer.Group.JavaPackageGroup;
import org.jboss.tools.windup.ui.internal.explorer.Group.ProjectGroup;
import org.jboss.tools.windup.ui.internal.explorer.Group.RuleGroup;
import org.jboss.tools.windup.ui.internal.explorer.Group.SeverityGroup;
import org.osgi.service.prefs.BackingStoreException;

/**
 * Service for managing the tree node groupings from within the Issue Explorer.
 */
@Singleton
@Creatable
public class IssueGroupService {
	
	private IEclipsePreferences preferences = InstanceScope.INSTANCE.getNode(WindupUIPlugin.PLUGIN_ID);

	private static final String GROUP_BY_PROJECT = "GROUP_BY_PROJECT"; //$NON-NLS-1$
	private static final String GROUP_BY_PACKAGE = "GROUP_BY_PACKAGE"; //$NON-NLS-1$
	private static final String GROUP_BY_FILE = "GROUP_BY_FILE"; //$NON-NLS-1$
	private static final String GROUP_BY_SEVERITY = "GROUP_BY_SEVERITY"; //$NON-NLS-1$
	private static final String GROUP_BY_RULE = "GROUP_BY_RULE"; //$NON-NLS-1$
	
	private boolean groupByProject;
	private boolean groupByPackage;
	private boolean groupByFile;
	private boolean groupBySeverity;
	private boolean groupByRule;
	
	@Inject private IEclipseContext context;
	@Inject	private IEventBroker broker;
	
	@PostConstruct
	private void start() {
		this.groupByProject = preferences.getBoolean(GROUP_BY_PROJECT, false);
		this.groupByPackage = preferences.getBoolean(GROUP_BY_PROJECT, false);
		this.groupByFile = preferences.getBoolean(GROUP_BY_FILE, false);
		this.groupBySeverity = preferences.getBoolean(GROUP_BY_SEVERITY, false);
		this.groupByRule = preferences.getBoolean(GROUP_BY_RULE, false);
	}
	
	@PreDestroy
	public void save() {
		preferences.putBoolean(GROUP_BY_PROJECT, groupByProject);
		preferences.putBoolean(GROUP_BY_PACKAGE, groupByPackage);
		preferences.putBoolean(GROUP_BY_FILE, groupByFile);
		preferences.putBoolean(GROUP_BY_SEVERITY, groupBySeverity);
		preferences.putBoolean(GROUP_BY_RULE, groupByRule);
		try {
			preferences.flush();
		} catch (BackingStoreException e) {
			WindupUIPlugin.log(e);
		}
	}
	
	public void setGroupByProject(boolean groupByProject) {
		this.groupByProject = groupByProject;
		notifyChanged();
	}
	
	public void setGroupByPackage(boolean groupByPackage) {
		this.groupByPackage = groupByPackage;
		notifyChanged();
	}
	
	public void setGroupByFile(boolean groupByFile) {
		this.groupByFile = groupByFile;
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
	
	public boolean isGroupByProject() {
		return groupByProject;
	}
	
	public boolean isGroupByPackage() {
		return groupByPackage;
	}
	
	public boolean isGroupByFile() {
		return groupByFile;
	}
	
	public boolean isGroupBySeverity() {
		return groupBySeverity;
	}
	
	public boolean isGroupByRule() {
		return groupByRule;
	}
	
	private void notifyChanged() {
		save();
		broker.post(WindupConstants.GROUPS_CHANGED, true);
	}
	
	public Group<?, ?> getRoot() {
		
		Group<?, ?> parent = null;
		
		if (groupByProject) {
			parent = new ProjectGroup(parent, context);
			if (groupByFile) {
				buildSeverityRuleGrouping(new ResourceGroup(parent, context));
			}
		}
		
		if (groupByPackage) {
			parent = new JavaPackageGroup(parent, context);
		}
		
		if (groupByFile) {
			Group<?, ?> oldParent = parent;
			parent = new JavaElementGroup(parent, context);
			if (!groupByProject) {
				buildSeverityRuleGrouping(new ResourceGroup(oldParent, context));
			}
		}

		parent = buildSeverityRuleGrouping(parent);
		
		return parent.getRoot();
	}
	
	public Group<?, ?> buildSeverityRuleGrouping(Group<?, ?> parent) {
		
		if (groupBySeverity) {
			parent = new SeverityGroup(parent, context);
			if (!groupByRule) {
				parent = new IssueGroup(parent, context);
			}
		}
		
		if (groupByRule) {
			parent = new IssueGroup(new RuleGroup(parent, context), context);
		}
		 
		if (!groupBySeverity && !groupByRule) {
			parent = new IssueGroup(parent, context);
		}
		
		return parent;
	}
}
