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
package org.jboss.tools.windup.ui.internal.rules.delegate;

import java.io.ByteArrayInputStream;
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
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.jboss.tools.windup.ui.WindupUIPlugin;

public class JavaProject {
	
	private static final String PROJECT_NAME = ".org.jboss.toos.windup.rules.java.tmp"; //$NON-NLS-1$
	
	private static final String PROJECT_FILE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + System.lineSeparator() + //$NON-NLS-1$
			"<projectDescription>" + System.lineSeparator() + 
			"	<name>" + PROJECT_NAME + "</name>" + System.lineSeparator() + //$NON-NLS-1$ //$NON-NLS-2$ 
			"	<comment></comment>" + System.lineSeparator() + //$NON-NLS-1$
			"	<projects>" + System.lineSeparator() + //$NON-NLS-1$
			"	</projects>" + System.lineSeparator() + //$NON-NLS-1$
			"	<buildSpec>" + System.lineSeparator() + //$NON-NLS-1$
			"		<buildCommand>" + System.lineSeparator() + //$NON-NLS-1$
			"			<name>org.eclipse.jdt.core.javabuilder</name>" + System.lineSeparator() + //$NON-NLS-1$ 
			"			<arguments>" + System.lineSeparator() + //$NON-NLS-1$
			"			</arguments>" + System.lineSeparator() + //$NON-NLS-1$
			"		</buildCommand>" + System.lineSeparator() + //$NON-NLS-1$
			"	</buildSpec>" + System.lineSeparator() + //$NON-NLS-1$
			"	<natures>" + System.lineSeparator() + //$NON-NLS-1$
			"		<nature>org.eclipse.jdt.core.javanature</nature>" + System.lineSeparator() + //$NON-NLS-1$ 
			"	</natures>" + System.lineSeparator() + //$NON-NLS-1$
			"</projectDescription>"; //$NON-NLS-1$
	
	private static final String CLASSPATH_FILE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + System.lineSeparator() + //$NON-NLS-1$ 
			"<classpath>" + System.lineSeparator() + //$NON-NLS-1$
			"	<classpathentry kind=\"con\" path=\"org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/Java SE 8 [1.8.0_92]\"/>" + System.lineSeparator() + //$NON-NLS-1$
			"	<classpathentry kind=\"src\" path=\"src\"/>" + System.lineSeparator() + //$NON-NLS-1$ 
			"	<classpathentry kind=\"output\" path=\"bin\"/>" + System.lineSeparator() + //$NON-NLS-1$
			"</classpath>"; //$NON-NLS-1$
	
	private static final String SETTINGS_FILE = "eclipse.preferences.version=1" + System.lineSeparator() + //$NON-NLS-1$ 
			"org.eclipse.jdt.core.compiler.codegen.inlineJsrBytecode=enabled" + System.lineSeparator() + //$NON-NLS-1$ 
			"org.eclipse.jdt.core.compiler.codegen.targetPlatform=1.8" + System.lineSeparator() + //$NON-NLS-1$ 
			"org.eclipse.jdt.core.compiler.codegen.unusedLocal=preserve" + System.lineSeparator() + //$NON-NLS-1$ 
			"org.eclipse.jdt.core.compiler.compliance=1.8" + System.lineSeparator() + //$NON-NLS-1$ 
			"org.eclipse.jdt.core.compiler.debug.lineNumber=generate" + System.lineSeparator() + //$NON-NLS-1$ 
			"org.eclipse.jdt.core.compiler.debug.localVariable=generate" + System.lineSeparator() + //$NON-NLS-1$ 
			"org.eclipse.jdt.core.compiler.debug.sourceFile=generate" + System.lineSeparator() + //$NON-NLS-1$ 
			"org.eclipse.jdt.core.compiler.problem.assertIdentifier=error" + System.lineSeparator() + //$NON-NLS-1$ 
			"org.eclipse.jdt.core.compiler.problem.enumIdentifier=error" + System.lineSeparator() + //$NON-NLS-1$ 
			"org.eclipse.jdt.core.compiler.source=1.8"; //$NON-NLS-1$
	
	public IJavaProject getProject() {
		IProject project = findProject();
		if (!project.isAccessible()) {
			try {
				IPath stateLocation = WindupUIPlugin.getDefault().getStateLocation();
				if (!project.exists()) {
					IProjectDescription desc = project.getWorkspace()
							.newProjectDescription(project.getName());
					desc.setLocation(stateLocation.append(PROJECT_NAME));
					project.create(desc, null);
					project.open(null);
					init(stateLocation, project);
				}
			} catch (Exception e) {
				WindupUIPlugin.log(e);
			}
		}
		return getJavaProject();
	}
	
	private void init(IPath stateLocation, IProject project) throws Exception {
		IFile projectFile = project.getFile(".project"); //$NON-NLS-1$
		write(projectFile, PROJECT_FILE);
		
		IFile classpathFile = project.getFile(".classpath"); //$NON-NLS-1$
		write(classpathFile, CLASSPATH_FILE);
		
		IFolder settingsFolder = getSettingsFolder(project);
		IFile settingsFile = settingsFolder.getFile(".org.eclipse.jdt.core.prefs"); //$NON-NLS-1$
		write(settingsFile, SETTINGS_FILE);
		
		project.getFolder("src");
	}
	
	private IJavaProject getJavaProject() {
		return JavaCore.create(ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECT_NAME));
	}
	
	private IProject findProject() {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECT_NAME);
	}
	
	private void write(IFile file, String contents) throws Exception {
		InputStream input = new ByteArrayInputStream(contents.getBytes(StandardCharsets.UTF_8));
		if (!file.exists()) {
			file.create(input, true, new NullProgressMonitor());
		}
		else {
			file.setContents(input, IResource.FORCE, new NullProgressMonitor());
		}
	}
	
	private IFolder getSettingsFolder(IProject project) throws CoreException {
		IFolder folder = project.getFolder(".settings"); //$NON-NLS-1$
		if (!folder.exists())
			folder.create(IResource.NONE, true, null);
		return folder;
	}
}