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
import org.jboss.tools.windup.ui.internal.explorer.Group;
import org.jboss.tools.windup.ui.internal.explorer.Group.ClassGroup;
import org.jboss.tools.windup.ui.internal.explorer.Group.IssueGroup;
import org.jboss.tools.windup.ui.internal.explorer.Group.PackageGroup;
import org.jboss.tools.windup.ui.internal.explorer.Group.ProjectGroup;
import org.jboss.tools.windup.ui.internal.explorer.Group.RuleGroup;
import org.jboss.tools.windup.ui.internal.explorer.Group.SeverityGroup;
import org.jboss.windup.reporting.model.Severity;
import org.osgi.service.prefs.BackingStoreException;

/**
 * Service for managing the tree node groupings from within the Issue Explorer.
 */
@Singleton
@Creatable
public class IssueGroupService {
	
	private IEclipsePreferences preferences = InstanceScope.INSTANCE.getNode(WindupUIPlugin.PLUGIN_ID);

	private static final String GROUP_BY_SEVERITY = "GROUP_BY_SEVERITY"; //$NON-NLS-1$
	private static final String GROUP_BY_RULE = "GROUP_BY_RULE"; //$NON-NLS-1$
	
	private boolean groupBySeverity;
	private boolean groupByRule;
	
	@Inject	private IEventBroker broker;
	
	@PostConstruct
	private void start() {
		this.groupBySeverity = preferences.getBoolean(GROUP_BY_SEVERITY, false);
		this.groupByRule = preferences.getBoolean(GROUP_BY_RULE, false);
	}
	
	@PreDestroy
	private void save() {
		preferences.putBoolean(GROUP_BY_SEVERITY, groupBySeverity);
		preferences.putBoolean(GROUP_BY_RULE, groupByRule);
		try {
			preferences.flush();
		} catch (BackingStoreException e) {
			WindupUIPlugin.log(e);
		}
	}
	
	public void setGroupBySeverity(boolean groupBySeverity) {
		this.groupBySeverity = groupBySeverity;
		notifyChanged();
	}
	
	public void setGroupByType(boolean groupByType) {
		this.groupByRule = groupByType;
		notifyChanged();
	}
	
	private void notifyChanged() {
		save();
		broker.post(WindupConstants.GROUPS_CHANGED, true);
	}
	
	public Group<?, ?> getRoot() {
		Group<?, ?> root = new ProjectGroup(null);
		PackageGroup packageGroup = new PackageGroup(root);
		ClassGroup classGroup = new ClassGroup(packageGroup);
		SeverityGroup mandatoryGroup = null;
		SeverityGroup potentialGroup = null;
		SeverityGroup optionalGroup = null;
		if (groupBySeverity) {
			mandatoryGroup = new SeverityGroup(classGroup, Severity.MANDATORY);
			potentialGroup = new SeverityGroup(classGroup, Severity.POTENTIAL);
			optionalGroup = new SeverityGroup(classGroup, Severity.OPTIONAL);
			if (!groupByRule) {
				new IssueGroup(mandatoryGroup);
				new IssueGroup(potentialGroup);
				new IssueGroup(optionalGroup);
			}
		}
		if (groupByRule) {
			if (groupBySeverity) {
				new IssueGroup(new RuleGroup(mandatoryGroup));
				new IssueGroup(new RuleGroup(potentialGroup));
				new IssueGroup (new RuleGroup(optionalGroup));
			}
			else {
				new IssueGroup(new RuleGroup(classGroup));
			}
		}
		if (!groupBySeverity && !groupByRule) {
			new IssueGroup(classGroup);
		}
		return root;
	}
}
