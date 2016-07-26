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
package org.jboss.tools.windup.ui.internal.explorer;

import javax.inject.Inject;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonContentProvider;

/**
 * Content provider for the Windup explorer.
 */
public class IssueExplorerContentProvider implements ICommonContentProvider {
	
	@Inject private IssueExplorerContentService contentService;

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parent) {
		return contentService.getChildren(parent);
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}
	
	@Override
	public Object getParent(Object element) {
		return contentService.getParent(element);
	}

	@Override
	public boolean hasChildren(Object element) {
		return contentService.hasChildren(element);
	}

	@Override
	public void restoreState(IMemento aMemento) {}
	@Override
	public void saveState(IMemento aMemento) {}
	@Override
	public void init(ICommonContentExtensionSite aConfig) {}
}
