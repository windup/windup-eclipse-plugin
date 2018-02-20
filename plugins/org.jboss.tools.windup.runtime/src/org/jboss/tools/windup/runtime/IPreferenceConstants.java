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
package org.jboss.tools.windup.runtime;

public interface IPreferenceConstants {
	static final String WINDUP_HOME = "WINDUP_HOME";
	static final String RMI_PORT = "RMI_PORT";
	static final int DEFAULT_RMI_PORT = 1100;
	
	static final String START_TIMEOUT = "WINDUP_START_TIMEOUT";
	static final String STOP_TIMEOUT = "WINDUP_STOP_TIMEOUT";
	
	static final String WINDUP_JRE_HOME = "JRE_HOME";
	
	static final int DEFAULT_WINDUP_START_DURATION_TIMEOUT = 45000;
	static final int DEFAULT_WINDUP_STOP_DURATION_TIMEOUT = 35000;
}
