/*******************************************************************************
 * Copyright (c) 2021 Red Hat, Inc.

 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.preferences;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMInstallType;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.VMStandin;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.pde.internal.ui.IHelpContextIds;
import org.eclipse.pde.internal.ui.PDEUIMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.jboss.tools.windup.runtime.IPreferenceConstants;
import org.jboss.tools.windup.runtime.WindupRmiClient;
import org.jboss.tools.windup.runtime.WindupRuntimePlugin;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.osgi.service.prefs.BackingStoreException;

@SuppressWarnings("restriction")
public class WindupPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private IEclipsePreferences preferences = InstanceScope.INSTANCE.getNode(WindupRuntimePlugin.PLUGIN_ID);
	
	private DirectoryFieldEditor homeEditor;
	private IntegerFieldEditor portEditor;
	
	private IntegerFieldEditor startTimeoutDurationEditor;
	private IntegerFieldEditor stopTimeoutDurationEditor;
	
	private FileFieldEditor jreEditor;
	
	@Inject private WindupRmiClient windupClient;
	
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
		homeEditor = new DirectoryFieldEditor(
				IPreferenceConstants.WINDUP_HOME, 
				Messages.WindupPreferenceHome, 
				getFieldEditorParent());
		homeEditor.setEmptyStringAllowed(false);
		addField(homeEditor);
//		
//		portEditor = new IntegerFieldEditor(
//				IPreferenceConstants.RMI_PORT, 
//				Messages.WindupPreferenceRmiPort, 
//				getFieldEditorParent());
//		portEditor.setEmptyStringAllowed(false);
//		addField(portEditor);
//		
//		startTimeoutDurationEditor = new IntegerFieldEditor(
//				IPreferenceConstants.START_TIMEOUT, 
//				Messages.WindupPreferenceStartTimeoutDuration, 
//				getFieldEditorParent());
//		startTimeoutDurationEditor.setEmptyStringAllowed(false);
//		addField(startTimeoutDurationEditor);
//		
//		stopTimeoutDurationEditor = new IntegerFieldEditor(
//				IPreferenceConstants.STOP_TIMEOUT, 
//				Messages.WindupPreferenceStopTimeoutDuration, 
//				getFieldEditorParent());
//		stopTimeoutDurationEditor.setEmptyStringAllowed(false);
//		addField(stopTimeoutDurationEditor);
//		
//		jreEditor = new FileFieldEditor(
//				IPreferenceConstants.WINDUP_JRE_HOME, 
//				Messages.WindupPreferenceJRE, 
//				true, 
//				StringFieldEditor.VALIDATE_ON_KEY_STROKE,
//				getFieldEditorParent()) {
//			
//			@Override
//			protected boolean checkState() {
//		        String msg = null;
//		        String path = getTextControl().getText();
//        			clearErrorMessage();
//		        if (path != null) {
//					path = path.trim();
//				} else {
//					path = "";//$NON-NLS-1$
//				}
//		        if (path.length() == 0) {
//		        		return true; 
//		        } else {
//		            File file = new File(path);
//		            if (file.isDirectory()) {
//		                if (!file.isAbsolute()) {
//							msg = Messages.JRENotAbsolute;
//						}
//		            } else {
//		                msg = Messages.InvalidJRELocation;
//		            }
//		        }
//		        if (msg != null) { // error
//		            showErrorMessage(msg);
//		            return false;
//		        }
//		        return true;
//			}
//			
//			@Override
//			protected String changePressed() {
//		        File f = new File(getTextControl().getText());
//		        if (!f.exists()) {
//					f = null;
//				}
//		        
//		        File d = handleAdd();
//		        if (d == null) {
//					return null;
//				}
//
//		        return d.getAbsolutePath();
//		    }
//		};
//		jreEditor.setEmptyStringAllowed(false);
//		addField(jreEditor);
	
//		if (windupClient.isWindupServerRunning()) {
//			Composite parent = new Composite(getFieldEditorParent(), SWT.NONE);
//			GridLayoutFactory.fillDefaults().numColumns(2).applyTo(parent);
//			GridDataFactory.fillDefaults().grab(true, false).span(2, 1).applyTo(parent);
//			new Label(parent, SWT.LEFT).setText(Messages.currentJavaHome);
//			Text text = new Text(parent, SWT.BORDER);
//			GridDataFactory.fillDefaults().grab(true, false).applyTo(text);
//			text.setEnabled(false);
//			text.setText(windupClient.getJavaHome());
//		}
	}
	
	private File handleAdd() {
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(Display.getDefault().getActiveShell(), new LabelProvider() {
			public Image getImage(Object element) {
				return JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_LIBRARY);
			}
			@Override
			public String getText(Object element) {
				IVMInstall install = (IVMInstall)element;
				return install.getName();
			}
		});
		dialog.setElements(getEnvironments());
		dialog.setAllowDuplicates(false);
		dialog.setMultipleSelection(true);
		dialog.setTitle(PDEUIMessages.RequiredExecutionEnvironmentSection_dialog_title);
		dialog.setMessage(PDEUIMessages.RequiredExecutionEnvironmentSection_dialogMessage);
		dialog.create();
		PlatformUI.getWorkbench().getHelpSystem().setHelp(dialog.getShell(), IHelpContextIds.EXECUTION_ENVIRONMENT_SELECTION);
		if (dialog.open() == Window.OK && dialog.getResult() != null && dialog.getResult().length == 1) {
			IVMInstall install = (IVMInstall)dialog.getResult()[0];
			return install.getInstallLocation();
			//addExecutionEnvironments(dialog.getResult());
		}
		return null;
	}
	
	public static Object[] getEnvironments() {
		// fill with JREs
		List<VMStandin> standins = new ArrayList<>();
		IVMInstallType[] types = JavaRuntime.getVMInstallTypes();
		for (int i = 0; i < types.length; i++) {
			IVMInstallType type = types[i];
			IVMInstall[] installs = type.getVMInstalls();
			for (int j = 0; j < installs.length; j++) {
				IVMInstall install = installs[j];
				standins.add(new VMStandin(install));
			}
		}
		return standins.toArray(new IVMInstall[standins.size()]);
	}
	
	@Override
	public boolean performOk() {
		boolean result = super.performOk();
		preferences.put(IPreferenceConstants.WINDUP_HOME, homeEditor.getStringValue());
//		preferences.put(IPreferenceConstants.RMI_PORT, portEditor.getStringValue());
//		preferences.put(IPreferenceConstants.START_TIMEOUT, startTimeoutDurationEditor.getStringValue());
//		preferences.put(IPreferenceConstants.STOP_TIMEOUT, stopTimeoutDurationEditor.getStringValue());
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
