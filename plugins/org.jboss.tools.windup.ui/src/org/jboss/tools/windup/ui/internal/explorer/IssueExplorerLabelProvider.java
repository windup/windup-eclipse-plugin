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

import static org.eclipse.core.resources.IMarker.SEVERITY_ERROR;
import static org.eclipse.core.resources.IMarker.SEVERITY_WARNING;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonLabelProvider;

/**
 * The label provider for the Windup explorer.
 */
public class IssueExplorerLabelProvider implements ICommonLabelProvider, IColorProvider {

	private WorkbenchLabelProvider workbenchProvider = new WorkbenchLabelProvider();
	
	@Override
	public Image getImage(Object element) {
		if (element instanceof ProjectGroupNode ||
				element instanceof PackageGroupNode ||
					element instanceof ClassGroupNode) {
			IssueGroupNode<?> node = (IssueGroupNode<?>)element;
			return workbenchProvider.getImage(node.getType());
		}
		if (element instanceof IssueNode) {
			switch (((IssueNode)element).getSeverity()) {
				case SEVERITY_ERROR: return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEC_FIELD_ERROR);
				case SEVERITY_WARNING: return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEC_FIELD_WARNING);
			}
		}
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof IssueGroupNode) {
			IssueGroupNode<?> node = (IssueGroupNode<?>)element;
			String label = node.getLabel();
			if (!(element instanceof IssueNode)) {
				label += " (" + node.getChildren().size() + ")";
			}
			return label;
		}
		return null;
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
	}

	@Override
	public void restoreState(IMemento aMemento) {
	}

	@Override
	public void saveState(IMemento aMemento) {
	}

	@Override
	public String getDescription(Object anElement) {
		return null;
	}

	@Override
	public Color getForeground(Object element) {
		return null;
	}

	@Override
	public Color getBackground(Object element) {
		return null;
	}

	@Override
	public void init(ICommonContentExtensionSite aConfig) {
	}
}
