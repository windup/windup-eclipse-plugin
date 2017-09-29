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
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
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
	
	private IntegerFieldEditor startTimeoutDurationEditor;
	private IntegerFieldEditor stopTimeoutDurationEditor;
	
	public WindupPreferencePage() {
		super(GRID);
		setPreferenceStore(new ScopedPreferenceStore(InstanceScope.INSTANCE, WindupRuntimePlugin.PLUGIN_ID));
	}
	
	@Override
	protected Control createContents(Composite parent) {
		new Label(parent, SWT.NONE).setLayoutData(GridDataFactory.swtDefaults().create());
		return super.createContents(parent);
	}
	
	@Override
	protected void createFieldEditors() {
		homeEditor = new FileFieldEditor(
				IPreferenceConstants.WINDUP_HOME, 
				Messages.WindupPreferenceHome, 
				true, 
				StringFieldEditor.VALIDATE_ON_KEY_STROKE, 
				getFieldEditorParent());
		homeEditor.setEmptyStringAllowed(false);
		addField(homeEditor);
		
		portEditor = new IntegerFieldEditor(
				IPreferenceConstants.RMI_PORT, 
				Messages.WindupPreferenceRmiPort, 
				getFieldEditorParent());
		portEditor.setEmptyStringAllowed(false);
		addField(portEditor);
		
		startTimeoutDurationEditor = new IntegerFieldEditor(
				IPreferenceConstants.START_TIMEOUT, 
				Messages.WindupPreferenceStartTimeoutDuration, 
				getFieldEditorParent());
		startTimeoutDurationEditor.setEmptyStringAllowed(false);
		addField(startTimeoutDurationEditor);
		
		stopTimeoutDurationEditor = new IntegerFieldEditor(
				IPreferenceConstants.STOP_TIMEOUT, 
				Messages.WindupPreferenceStopTimeoutDuration, 
				getFieldEditorParent());
		stopTimeoutDurationEditor.setEmptyStringAllowed(false);
		addField(stopTimeoutDurationEditor);
	}
	
	@Override
	public boolean performOk() {
		boolean result = super.performOk();
		preferences.put(IPreferenceConstants.WINDUP_HOME, homeEditor.getStringValue());
		preferences.put(IPreferenceConstants.RMI_PORT, portEditor.getStringValue());
		preferences.put(IPreferenceConstants.START_TIMEOUT, startTimeoutDurationEditor.getStringValue());
		preferences.put(IPreferenceConstants.STOP_TIMEOUT, stopTimeoutDurationEditor.getStringValue());
		try {
			preferences.flush();
		} catch (BackingStoreException e) {
			WindupUIPlugin.log(e);
		}
		return result;
	}
	
	@Override
	protected Point doComputeSize() {
		return new Point(300, 400);
	}
	
	@Override
	public void init(IWorkbench workbench) {
	}
}
