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

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.JavaProject;
import org.jboss.tools.windup.model.Activator;
import org.jboss.tools.windup.model.util.MavenUtil;
import org.jboss.tools.windup.windup.ConfigurationElement;
import org.jboss.tools.windup.windup.Input;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Utility for working with ConfigurationElement's resources (projects and packages).
 */
@SuppressWarnings("restriction")
public class ConfigurationResourceUtil {

	public static IProject[] getCurrentProjects(ConfigurationElement configuration) {
		List<IProject> projects = Lists.newArrayList();
		for (int i = 0; i < configuration.getInputs().size(); i++) {
			Input input = configuration.getInputs().get(i);
			IProject project = (IProject)WorkspaceResourceUtils.findResource(input.getUri());
			if (project != null && project.exists() && project.isAccessible()) {
				projects.add(project);
			}
		}
		return projects.toArray(new IProject[projects.size()]);
	}
	
	public static IPackageFragment[] getCurrentPackages(ConfigurationElement configuration) {
		IPackageFragment[] packages = new IPackageFragment[configuration.getPackages().size()];
		for (int i = 0; i < configuration.getPackages().size(); i++) {
			String aPackage = configuration.getPackages().get(i);
			IResource resource = (IResource)WorkspaceResourceUtils.findResource(aPackage);
			packages[i] = (IPackageFragment)JavaCore.create(resource);
		}
		return packages;
	}
	
	public static IPackageFragment[] computePackages(ConfigurationElement configuration) {
		List<IPackageFragment> packages = Lists.newArrayList();
		List<IPackageFragment> currentFragments = Lists.newArrayList(getCurrentPackages(configuration));
		
		Set<IProject> projects = Sets.newHashSet(getCurrentProjects(configuration));

		// Add all implicitly analyzed projects
		for (IProject project : Lists.newArrayList(projects)) {
			projects.addAll(MavenUtil.computeProjectInfo(project).getProjects().stream().map(info -> { 
				return info.getProject();	
			}).collect(Collectors.toList()));
		}
		
		for (IProject project : projects) {
			JavaProject javaProject = (JavaProject)JavaCore.create(project);
			try {
				for (IPackageFragmentRoot root : javaProject.getPackageFragmentRoots()) {
					if (root.getKind() == IPackageFragmentRoot.K_SOURCE) {
						for (Object obj : root.getChildren()) {
							if (obj instanceof IPackageFragment) {
								IPackageFragment fragment = (IPackageFragment)obj;
								if (fragment.hasChildren() && !currentFragments.contains(fragment)) {
									packages.add(fragment);
								}
							}
						}
					}
				}
			} catch (JavaModelException e) {
				Activator.log(e);
			}
		}
		return packages.stream().toArray(IPackageFragment[]::new);
	}
}
