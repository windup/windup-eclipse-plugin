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

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.jboss.tools.windup.runtime.IPreferenceConstants;
import org.jboss.tools.windup.runtime.WindupRuntimePlugin;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.osgi.service.prefs.BackingStoreException;

public class WindupPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private IEclipsePreferences preferences = InstanceScope.INSTANCE.getNode(WindupRuntimePlugin.PLUGIN_ID);
	
	private FileFieldEditor homeEditor;
	private IntegerFieldEditor portEditor;
	
	public WindupPreferencePage() {
		super(GRID);
		setPreferenceStore(new ScopedPreferenceStore(InstanceScope.INSTANCE, WindupRuntimePlugin.PLUGIN_ID));
	}
	
	@Override
	protected void createFieldEditors() {
		addField(homeEditor = new FileFieldEditor(
				IPreferenceConstants.WINDUP_HOME, 
				Messages.WindupPreferenceHome, 
				true, 
				StringFieldEditor.VALIDATE_ON_KEY_STROKE, 
				getFieldEditorParent()));
		
		addField(portEditor = new IntegerFieldEditor(
				IPreferenceConstants.RMI_PORT, 
				Messages.WindupPreferenceRmiPort, 
				getFieldEditorParent()));
	}
	
	@Override
	public boolean performOk() {
		boolean result = super.performOk();
		preferences.put(IPreferenceConstants.WINDUP_HOME, homeEditor.getStringValue());
		preferences.put(IPreferenceConstants.RMI_PORT, portEditor.getStringValue());
		try {
			preferences.flush();
		} catch (BackingStoreException e) {
			WindupUIPlugin.log(e);
		}
		return result;
	}
	
	
	@Override
	public void init(IWorkbench workbench) {
	}
}
