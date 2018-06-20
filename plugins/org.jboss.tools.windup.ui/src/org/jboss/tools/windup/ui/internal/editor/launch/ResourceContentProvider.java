/*******************************************************************************
 * Copyright (c) 2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.editor.launch;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.model.WorkbenchContentProvider;

/**
 * Content provider for workspace resources.
 */
public class ResourceContentProvider extends WorkbenchContentProvider {
	
	private Viewer viewer;
	
	public ResourceContentProvider() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this, 
				IResourceChangeEvent.POST_CHANGE);
	}
	
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		super.inputChanged(viewer, oldInput, newInput);
		this.viewer = viewer;
	}

	@Override
	public void dispose() {
		if (viewer != null) {
			ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		}
	}
	
	@Override
	public Object[] getElements(Object element) {
		if (element instanceof Object[]) {
			return (Object[])element;
		}
		return new Object[0];
	}
}
