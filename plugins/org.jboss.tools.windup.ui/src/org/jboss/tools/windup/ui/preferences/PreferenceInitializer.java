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
package org.jboss.tools.windup.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.jboss.tools.windup.runtime.IPreferenceConstants;
import org.jboss.tools.windup.runtime.WindupRuntimePlugin;

public class PreferenceInitializer extends AbstractPreferenceInitializer {
	
	@Override
	public void initializeDefaultPreferences() {
		String windupHome = WindupRuntimePlugin.getDefaultWindupHome(); 
		IEclipsePreferences defaultPreferences = DefaultScope.INSTANCE.getNode(WindupRuntimePlugin.PLUGIN_ID);
		defaultPreferences.put(IPreferenceConstants.WINDUP_HOME, windupHome);
		defaultPreferences.put(IPreferenceConstants.RMI_PORT, String.valueOf(IPreferenceConstants.DEFAULT_RMI_PORT));
		
		defaultPreferences.put(IPreferenceConstants.START_TIMEOUT, String.valueOf(IPreferenceConstants.DEFAULT_WINDUP_START_DURATION_TIMEOUT));
		defaultPreferences.put(IPreferenceConstants.STOP_TIMEOUT, String.valueOf(IPreferenceConstants.DEFAULT_WINDUP_STOP_DURATION_TIMEOUT));
	}
}
