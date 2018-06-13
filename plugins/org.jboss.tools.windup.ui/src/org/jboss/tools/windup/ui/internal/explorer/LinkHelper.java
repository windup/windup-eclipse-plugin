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
package org.jboss.tools.windup.ui.internal.explorer;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.ResourceUtil;
import org.eclipse.ui.navigator.ILinkHelper;

public class LinkHelper implements ILinkHelper {
	
	@Override
	public IStructuredSelection findSelection(IEditorInput input) {
		Object element = null;
		IFile resource = ResourceUtil.getFile(input);
		if (resource != null) {
			IssueExplorer explorer = (IssueExplorer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(IssueExplorer.VIEW_ID);
			if (explorer != null) {
				element = explorer.computeNode(resource);
			}
		}
		return (element != null) ? new StructuredSelection(element) : StructuredSelection.EMPTY;
	}

	@Override
	public void activateEditor(IWorkbenchPage aPage, IStructuredSelection aSelection) {
	}
}
