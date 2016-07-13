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
package org.jboss.tools.windup.ui.internal.launch.input;

import static org.jboss.tools.windup.model.domain.WindupConstants.DEFAULT;
import static org.jboss.tools.windup.ui.internal.Messages.applicationsToMigrate;
import static org.jboss.tools.windup.ui.internal.Messages.inputTabName;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.internal.debug.ui.launcher.AbstractJavaMainTab;
import org.eclipse.jdt.internal.debug.ui.launcher.LauncherMessages;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

/**
 * The tab where the user specifies the input to analyze by Windup.
 */
@SuppressWarnings("restriction")
public class WindupInputTab extends AbstractJavaMainTab {
	
	private static final String ID = "org.jboss.tools.windup.ui.launch.WindupSourceTab";

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().margins(5, 5).applyTo(container);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(container);
		createProjectEditor(container);
		createVerticalSpacer(container, 1);
		super.setControl(container);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(container, ID);
	}
	
	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		IJavaElement javaElement = getContext();
		if (javaElement != null) {
			initializeJavaProject(javaElement, configuration);
		}
		else {
			configuration.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, DEFAULT);
		}
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, fProjText.getText().trim());
	}

	@Override
	public String getName() {
		return inputTabName;
	}
	
	@Override
	public Image getImage() {
		return PlatformUI.getWorkbench().getSharedImages().getImage(IDE.SharedImages.IMG_OBJ_PROJECT);
	}

	@Override
	public boolean isValid(ILaunchConfiguration config) {
		setErrorMessage(null);
		setMessage(null);
		String name = fProjText.getText().trim();
		if (name.length() == 0) {
			setErrorMessage(applicationsToMigrate);
			return false;
		}
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IStatus status = workspace.validateName(name, IResource.PROJECT);
		if (status.isOK()) {
			IProject project= ResourcesPlugin.getWorkspace().getRoot().getProject(name);
			if (!project.exists()) {
				setErrorMessage(NLS.bind(LauncherMessages.JavaMainTab_20, new String[] {name})); 
				return false;
			}
			if (!project.isOpen()) {
				setErrorMessage(NLS.bind(LauncherMessages.JavaMainTab_21, new String[] {name})); 
				return false;
			}
		}
		else {
			setErrorMessage(NLS.bind(LauncherMessages.JavaMainTab_19, new String[]{status.getMessage()})); 
			return false;
		}
		return true;
	}
	
	@Override
	public void initializeFrom(ILaunchConfiguration config) {
	}
}
