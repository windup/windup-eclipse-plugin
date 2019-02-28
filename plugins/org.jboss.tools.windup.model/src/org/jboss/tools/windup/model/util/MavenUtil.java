/*******************************************************************************
 * Copyright (c) 2019 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.model.util;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.m2e.core.internal.MavenPluginActivator;
import org.eclipse.m2e.core.internal.project.registry.ProjectRegistryManager;
import org.eclipse.m2e.core.project.IMavenProjectFacade;

import com.google.common.collect.Lists;

@SuppressWarnings("restriction")
public class MavenUtil {
	
	public static class ProjectInfo {

		private String name;
		private IProject project;
		private ProjectInfo parent;
		private List<ProjectInfo> projects;
		private List<ProjectInfo> missingProjects;
		
		public ProjectInfo (String name, IProject project) {
			this.name = name;
			this.project = project;
			this.projects = Lists.newArrayList();
			this.missingProjects = Lists.newArrayList();
		}
		
		public ProjectInfo(String name, IProject project, ProjectInfo parent) {
			this(name, project);
			this.parent = parent;
		}
		
		public String getName() {
			return name;
		}

		public IProject getProject() {
			return project;
		}
		
		public ProjectInfo getParent() {
			return parent;
		}
		
		public List<ProjectInfo> getProjects() {
			return projects;
		}
		
		public void addProject(IProject moduleProject) {
			projects.add(new ProjectInfo(moduleProject.getName(), moduleProject, this));
		}
		
		public List<ProjectInfo> getMissingProjects() {
			return missingProjects;
		}
		
		public void addMissingProject(String missingProject) {
			missingProjects.add(new ProjectInfo(missingProject, null, this));
		}
		
		public boolean hasChildren() {
			return !projects.isEmpty() || !missingProjects.isEmpty();
		}
		
		public boolean projectExists() {
			return project != null && project.exists();
		}
		
		public ProjectInfo getTopProject() {
			if (parent != null) {
				return parent.getTopProject();
			}
			return this;
		}
		
		public boolean isParentProject() {
			return parent == null;
		}
	}

	public static ProjectInfo computeProjectInfo(IProject project) {
		
		ProjectInfo mavenModuleResult = new ProjectInfo(project.getName(), project);
		
		ProjectRegistryManager mavenProjectManager = MavenPluginActivator.getDefault().getMavenProjectManagerImpl();
		
		IMavenProjectFacade mavenFacade = mavenProjectManager.create(project, new NullProgressMonitor());
		if (mavenFacade != null) {
			List<String> modules = mavenFacade.getMavenProjectModules();
			for (String module : modules) {
				IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(module);
				if (resource != null && resource instanceof IProject) {
					IProject moduleProject = (IProject) resource;
					if (moduleProject.exists() && moduleProject.isAccessible()) {
						mavenModuleResult.addProject(moduleProject);
					}
					else {
						mavenModuleResult.addMissingProject(module);
					}
				}
				else {
					mavenModuleResult.addMissingProject(module);
				}
			}
		}
		
		return mavenModuleResult;
	}
}
