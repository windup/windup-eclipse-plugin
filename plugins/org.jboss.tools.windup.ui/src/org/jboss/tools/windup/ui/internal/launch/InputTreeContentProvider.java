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
package org.jboss.tools.windup.ui.internal.launch;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.jboss.tools.windup.model.util.MavenUtil.ProjectInfo;

import com.google.common.collect.Lists;

public class InputTreeContentProvider implements ITreeContentProvider {
	
	public static class ProjectInfoRoot {
		
		private List<ProjectInfo> children;
		
		public ProjectInfoRoot (List<ProjectInfo> children) {
			this.children = children;
		}
		public List<ProjectInfo> getChildren() {
			return children;
		}
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof ProjectInfoRoot) {
			ProjectInfoRoot root = (ProjectInfoRoot)parentElement;
			return root.getChildren().stream().toArray(ProjectInfo[]::new);
		}
		else if (parentElement instanceof ProjectInfo) {
			ProjectInfo project = (ProjectInfo)parentElement;
			List<Object> children = Lists.newArrayList();
			children.addAll(project.getProjects());
			children.addAll(project.getMissingProjects());
			return children.toArray(new Object[children.size()]);
		}
		return new Object[0];
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof ProjectInfo) {
			return ((ProjectInfo)element).getParent();
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof ProjectInfo) {
			return ((ProjectInfo)element).hasChildren();
		}
		return element instanceof ProjectInfoRoot;
	}
}
