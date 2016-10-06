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
package org.jboss.tools.windup.model.domain;

/**
 * Common constants used to share data throughout the Windup tooling.
 */
public interface WindupConstants {

	/**
	 * Windup launch configuration IDs.
	 */
	String LAUNCH_TYPE = "org.jboss.tools.windup.ui.WindupLaunchConfigurationType";
	String LAUNCH_GROUP = "org.eclipse.debug.ui.launchGroup.run";
	
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
	 * Launch Events
	 */
	String LAUNCH_COMPLETED = "windup/launch/completed";
	
	/**
	 * Windup completed
	 */
	String WINDUP_RUN_COMPLETED = "windup/run/completed";
	
	/**
	 * UI Events
	 */
	String ACTIVE_CONFIG = "windup/config/selected";
	String SYNCH = "windup/config/synch";
	String MARKER_DELETED = "windup/issues/deleted";
	String MARKERS_CHANGED = "windup/issue/markers";
	String GROUPS_CHANGED = "windup/issue/groups";
	String INPUT_CHANGED = "windup/configuration/input";
	String MARKER_CHANGED = "windup/marker/changed";
	
	String EVENT_ISSUE_MARKER = "data";
	String EVENT_ISSUE_MARKER_UPDATE = "dataUpdate";
	
	/**
	 * Model Events
	 */
	String CONFIG_DELETED = "windup/model/config/deleted";
	String CONFIG_CREATED = "windup/model/config/created";
}
