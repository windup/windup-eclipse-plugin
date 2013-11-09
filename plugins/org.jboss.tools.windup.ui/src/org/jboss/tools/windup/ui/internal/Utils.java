/*******************************************************************************
* Copyright (c) 2011 Red Hat, Inc.
* Distributed under license by Red Hat, Inc. All rights reserved.
* This program is made available under the terms of the
* Eclipse Public License v1.0 which accompanies this distribution,
* and is available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   Red Hat, Inc. - initial API and implementation
******************************************************************************/
package org.jboss.tools.windup.ui.internal;

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
			
			// if only one selection is made update the displayed report
			if (structedSelection.size() == 1) {
				Object firstElement = structedSelection.getFirstElement();
	
				selectedResource = ResourceUtil.getResource(firstElement);
			}
		}

		return selectedResource;
	}	
}