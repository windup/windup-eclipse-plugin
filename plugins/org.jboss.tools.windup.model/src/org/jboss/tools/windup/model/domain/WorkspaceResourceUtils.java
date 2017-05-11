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

import java.io.File;
import java.net.URL;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.jboss.tools.windup.model.Activator;

/**
 * Utility for working with the Eclipse workspace resources.
 */
public class WorkspaceResourceUtils {

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
	 * @return the project in the workspace matching the specified name.
	 */
	public static IProject findProject(String projectName) {
		return Arrays.stream(ResourcesPlugin.getWorkspace().getRoot().getProjects()).filter(proj -> {
			return Collator.getInstance().equals(proj.getName(), projectName);
		}).findFirst().get();
	}
	
	public static IResource findResource(String uriString) {
		URI uri = URI.createURI(uriString);
		Path path = new Path(uri.toPlatformString(false));
		return ResourcesPlugin.getWorkspace().getRoot().findMember(path);
	}
	
	public static URI createPlatformPluginURI(IPath path) {
		return URI.createPlatformPluginURI(path.toString(), false);
	}
	
	public static java.nio.file.Path computePath(String platformPluginUri) {
		try {
			URL resUrl = new URL(platformPluginUri.replace("platform:/plugin", "platform:/resource"));
			System.out.println("URL of resource is: " + resUrl);
			File tempFile = null;
			try {
				tempFile = new File(FileLocator.resolve(resUrl).toURI());
			} catch (IllegalArgumentException e) {
				tempFile = new File(resUrl.toExternalForm());
				if (!tempFile.exists())
				{
					IProject project = findProject(platformPluginUri.replace("platform:/plugin", ""));
					tempFile = new File(project.getLocationURI().getPath());
				}
			}
			System.out.println("File resource is: " + tempFile);
			return tempFile.toPath();
		} catch (Exception e) {
			Activator.log(e);
		}
		return null;
	}
	
	public static IFile getFile(String locationURI) {
		IFile file = null;
		java.net.URI location = URIUtil.toURI(locationURI);
		IFile[] files = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocationURI(location);
		if (files.length > 0) {
			file = files[0];
		}
		return file;
	}
	
	public static IFile getResource(String absolutePath) {
		return ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(new Path(absolutePath));
	}
}
