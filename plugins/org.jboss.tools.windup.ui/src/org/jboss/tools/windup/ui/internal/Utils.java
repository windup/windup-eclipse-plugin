/*******************************************************************************
* Copyright (c) 2013 Red Hat, Inc.
* Distributed under license by Red Hat, Inc. All rights reserved.
* This program is made available under the terms of the
* Eclipse Public License v1.0 which accompanies this distribution,
* and is available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   Red Hat, Inc. - initial API and implementation
******************************************************************************/
package org.jboss.tools.windup.ui.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ide.ResourceUtil;

/**
 * <p>
 * Useful utilities.
 * </p>
 */
public class Utils {
	/**
	 * <p>
	 * Return the selected resource from the given selection.
	 * </p>
	 * 
	 * @param selection
	 *            selection to get the selected {@link IResource} from
	 * 
	 * @return selected {@link IResource} from the given
	 *         {@link ISelection} or <code>null</code> if nothing is
	 *         selected or there is more then one selection
	 */
	public static IResource getSelectedResource(ISelection selection) {
		IResource selectedResource = null;

		if(selection instanceof IStructuredSelection) {
			IStructuredSelection structedSelection = (IStructuredSelection)selection;
			
			if (structedSelection.size() == 1) {
				Object firstElement = structedSelection.getFirstElement();
	
				selectedResource = ResourceUtil.getResource(firstElement);
			}
		}

		return selectedResource;
	}
	
	/**
	 * <p>
	 * Return all of the containing projects for all of the selected resources.
	 * If more then one resource is selected in the same project that project is
	 * still only included in the returned list once.
	 * </p>
	 * 
	 * @param selection
	 *            selection to get the selected {@link IProject}s from
	 * 
	 * @return {@link IProject}s containing the resources selected in the given
	 *         selection
	 */
	public static List<IProject> getSelectedProjects(ISelection selection) {
		List<IProject> selectedProjects = new ArrayList<IProject>();

		if(selection instanceof IStructuredSelection) {
			IStructuredSelection structedSelection = (IStructuredSelection)selection;
				Object[] selectedElements = structedSelection.toArray();
	
				for(Object selectedElement : selectedElements) {
					IResource selectedResource = ResourceUtil.getResource(selectedElement);
					if(selectedResource != null) {
						IProject selectedProject = selectedResource.getProject();
						
						if(!selectedProjects.contains(selectedProject)) {
							selectedProjects.add(selectedProject);
						}
					}
				}
		}

		return selectedProjects;
	}
}