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
package org.jboss.tools.windup.ui.internal.explorer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;

/**
 * A temporary project for working with temporary resources.
 */
@SuppressWarnings("restriction")
public class TempProject {
	
	public static final String TMP_PROJECT_NAME = ".org.jboss.toos.windup.compare.tmp"; //$NON-NLS-1$
	
	private final static String TMP_PROJECT_FILE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" //$NON-NLS-1$
			+ "<projectDescription>\n" //$NON-NLS-1$
			+ "\t<name>" + TMP_PROJECT_NAME + "\t</name>\n" //$NON-NLS-1$ //$NON-NLS-2$
			+ "\t<comment></comment>\n" //$NON-NLS-1$
			+ "\t<projects>\n" //$NON-NLS-1$
			+ "\t</projects>\n" //$NON-NLS-1$
			+ "\t<buildSpec>\n" //$NON-NLS-1$
			+ "\t</buildSpec>\n" //$NON-NLS-1$
			+ "\t<natures>\n" + "\t</natures>\n" //$NON-NLS-1$//$NON-NLS-2$
			+ "</projectDescription>"; //$NON-NLS-1$
	
	private final static String TMP_FOLDER_NAME = "tmpFolder"; //$NON-NLS-1$
	private final static String TMP_FILE_NAME = "tmpFile"; //$NON-NLS-1$
	
	private TempProject() {
	}
	
	public static IProject getTmpProject() {
		IProject project = findTmpProject();
		if (!project.isAccessible()) {
			try {
				IPath stateLocation = WindupUIPlugin.getDefault().getStateLocation();
				if (!project.exists()) {
					IProjectDescription desc = project.getWorkspace()
							.newProjectDescription(project.getName());
					desc.setLocation(stateLocation.append(TMP_PROJECT_NAME));
					project.create(desc, null);
				}
				try {
					project.open(null);
				} catch (CoreException e) { // in case .project file or folder has been deleted
					IPath projectPath = stateLocation.append(TMP_PROJECT_NAME);
					projectPath.toFile().mkdirs();
					FileOutputStream output = new FileOutputStream(
							projectPath.append(".project").toOSString()); //$NON-NLS-1$
					try {
						output.write(TMP_PROJECT_FILE.getBytes());
					} finally {
						output.close();
					}
					project.open(null);
				}
				getTmpFolder(project);
			} catch (IOException ioe) {
				return project;
			} catch (CoreException e) {
				WindupUIPlugin.log(e);
			}
		}
		try {
			if (!project.isOpen()) {
				project.open(null);
			}
			project.setHidden(true);
		} catch (Exception e) {
			WindupUIPlugin.log(e);
		}
		return project;
	}
	
	private static IProject findTmpProject() {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(TMP_PROJECT_NAME);
	}
	
	public static IFile getFile(String location) {
		IProject project = TempProject.getTmpProject();
		IPath path = project.getFullPath().append(new File(location).getAbsolutePath());
		return ResourcesPlugin.getWorkspace().getRoot().getFile(path);
	}
	
	private static IFolder getTmpFolder(IProject project) throws CoreException {
		IFolder folder = project.getFolder(TMP_FOLDER_NAME);
		if (!folder.exists())
			folder.create(IResource.NONE, true, null);
		return folder;
	}
	
	public static IResource createResource(String contents) {
		try {
			IProject project = TempProject.getTmpProject();
			IFolder folder = TempProject.getTmpFolder(project);
			IResource resource = folder.getFile(TMP_FILE_NAME);
			InputStream input = new ByteArrayInputStream(contents.getBytes(StandardCharsets.UTF_8));
			if (!resource.exists()) {
				((IFile)resource).create(input, true, new NullProgressMonitor());
			}
			else {
				((IFile)resource).setContents(input, IResource.FORCE, new NullProgressMonitor());
			}
			return resource;
		} catch (CoreException e) {
			WindupUIPlugin.log(e);
			MessageDialog.openError(Display.getDefault().getActiveShell(),
							Messages.ComparePreviewer_errorTitle,
							Messages.ComparePreviewer_errorMessage);
		}
		return null;
	}
}
