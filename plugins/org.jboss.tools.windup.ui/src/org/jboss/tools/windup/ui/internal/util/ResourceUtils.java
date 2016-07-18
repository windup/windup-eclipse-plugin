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
package org.jboss.tools.windup.ui.internal.util;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;

/**
 * Utility for working with the Eclipse workspace resources.
 */
public class ResourceUtils {

	/**
	 * Returns the {@link IProject} associated with the given editor's input.
	 */
	public static IProject getProjectFromEditor(IEditorPart editor) {
		IEditorInput input = editor.getEditorInput();
		if (!(input instanceof IFileEditorInput)) {
			return null;
		}
		return ((IFileEditorInput)input).getFile().getProject();
	}
	
	/**
	 * Returns a list of {@link IProject}s associated with the given selection.
	 */
	public static List<IProject> getProjectsFromSelection(ISelection selection) {
		List<IProject> projects = new ArrayList<IProject>();
		if (selection instanceof IStructuredSelection) {
			for (Object element : ((IStructuredSelection)selection).toList()) {
				if (element instanceof IAdaptable) {
					IAdaptable resource = (IAdaptable) element;
					IFile file = (IFile) resource.getAdapter(IFile.class);
					if (file != null) {
						projects.add(file.getProject());
					}
					else {
						IProject project = (IProject)resource.getAdapter(IProject.class);
						if (project != null) {
							projects.add(project);
						}
					}
				}
			}
		}
		return projects;
	}
	
	/**
	 * @return the platform URI path.
	 */
	public static String getProjectURI(IResource resource) {
		return String.format("platform:/plugin/%s/%s", 
				resource.getProject().getName(), 
				resource.getProjectRelativePath().toString());
	}
	
	/**
	 * @return the project in the workspace matching the specified name.
	 */
	public static IProject findProject(String projectName) {
		return Arrays.stream(ResourcesPlugin.getWorkspace().getRoot().getProjects()).filter(proj -> {
			return Collator.getInstance().equals(proj.getName(), projectName);
		}).findFirst().get();
	}
}
