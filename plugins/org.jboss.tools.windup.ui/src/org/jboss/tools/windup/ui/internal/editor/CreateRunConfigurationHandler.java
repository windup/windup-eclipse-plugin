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
package org.jboss.tools.windup.ui.internal.editor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.debug.internal.ui.launchConfigurations.LaunchConfigurationManager;
import org.eclipse.debug.internal.ui.launchConfigurations.LaunchConfigurationsDialog;
import org.eclipse.debug.internal.ui.launchConfigurations.LaunchGroupExtension;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.windup.model.domain.WindupConstants;
import org.jboss.tools.windup.model.util.NameUtil;
import org.jboss.tools.windup.ui.WindupUIPlugin;

/**
 * Handler for creating a Windup run configuration, and opening it in the
 * run configuration dialog.
 */
@SuppressWarnings("restriction")
public class CreateRunConfigurationHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		LaunchConfigurationManager lcManager = DebugUIPlugin.getDefault().getLaunchConfigurationManager();
		LaunchGroupExtension group = lcManager.getLaunchGroup(WindupConstants.LAUNCH_GROUP);
		LaunchConfigurationsDialog dialog = new LaunchConfigurationsDialog(Display.getDefault().getActiveShell(), group);
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType type = manager.getLaunchConfigurationType(WindupConstants.LAUNCH_TYPE);
		try {
			ILaunchConfigurationWorkingCopy wc = type.newInstance(null, manager.generateLaunchConfigurationName(NameUtil.CONFIGURATION_ELEMENT_PREFIX));
			ILaunchConfiguration config = wc.doSave();
			IStructuredSelection ss = new StructuredSelection(config);
			dialog.setInitialSelection(ss);
			dialog.setOpenMode(LaunchConfigurationsDialog.LAUNCH_CONFIGURATION_DIALOG_OPEN_ON_SELECTION);
			dialog.open();
		} catch (CoreException e) {
			displayLaunchError(Display.getDefault().getActiveShell(), e);
		}
		return IStatus.OK;
	}
	
	public static void displayLaunchError(Shell shell, Exception e) {
		WindupUIPlugin.log(e);
		String title = "Problem Occured"; //$NON-NLS-1$
		String message = "Opening the configuration dialog has encoutered a problem.\n\n" + e.getLocalizedMessage(); //$NON-NLS-1$
		MessageDialog.openError(shell, title, message);
	}
}
