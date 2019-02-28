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
package org.jboss.tools.windup.model.domain;

/**
 * Common constants used to share data throughout the Windup tooling.
 */
public interface WindupConstants {
	
	String RULESET_EDITOR_CONTEXT = "org.jboss.tools.windup.ui.rulesetEditorScope";

	/**
	 * Windup launch configuration IDs.
	 */
	String LAUNCH_TYPE = "org.jboss.tools.windup.ui.WindupLaunchConfigurationType";
	String LAUNCH_GROUP = "org.eclipse.debug.ui.launchGroup.run";
	
	/**
	 * Getting Started
	 */
	String SHOW_GETTING_STARTED = "org.jboss.tools.windup.ui.gettingStarted";
	String INIT_GETTING_STARTED = "Windup Getting Started Initialization";
	
	/**
	 * Windup Services
	 */
	String LOADING_OPTIONS = "Loading Windup Options";
	
	/**
	 * Projects launch configuration attribute.
	 */
	String DEFAULT = "";
	String DEFAULT_RULE_ID = "unknown rule";
	
	
	/**
	 * Ruleset Events
	 */
	String CUSTOM_RULESET_CHANGED = "windup/ruleset/added";
	
	/**
	 * Windup completed
	 */
	String WINDUP_RUN_COMPLETED = "windup/run/completed";
	
	/**
	 * UI Events
	 */
	String ACTIVE_CONFIG = "windup/rules/selected";
	String SYNCH = "windup/config/synch";
	String MARKERS_CHANGED = "windup/issue/markers";
	String GROUPS_CHANGED = "windup/issue/groups";
	String INPUT_CHANGED = "windup/configuration/input";
	
	/**
	 * Model Events
	 */
	String CONFIG_DELETED = "windup/model/config/deleted";
	String CONFIG_CREATED = "windup/model/config/created";
}
