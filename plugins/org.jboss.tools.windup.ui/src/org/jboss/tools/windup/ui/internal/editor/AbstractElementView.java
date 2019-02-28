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
package org.jboss.tools.windup.ui.internal.editor;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.Page;
import org.eclipse.ui.part.PageBookView;

public abstract class AbstractElementView extends PageBookView {

	private Viewer viewer;
	
	private void setViewer(Viewer viewer) {
		this.viewer = viewer;
	}
	
	public Viewer getViewer() {
		return viewer;
	}
	
	protected Control getDefaultControl() {
		Viewer viewer = getViewer();
		if (viewer != null) {
			return viewer.getControl();
		}
		return null;
	}
	
	protected abstract Viewer createViewer(Composite parent);
	
	class ViewerPage extends Page {
		@Override
		public void createControl(Composite parent) {
			Viewer viewer = createViewer(parent);
			setViewer(viewer);
		}

		@Override
		public Control getControl() {
			return getDefaultControl();
		}

		@Override
		public void setFocus() {
			Viewer viewer= getViewer();
			if (viewer != null) {
				Control c = viewer.getControl();
				if (!c.isFocusControl()) {
					c.setFocus();
				}
			}
		}
	}
	
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
	}
	
	@Override
	protected boolean isImportant(IWorkbenchPart part) {
		return false;
	}

	@Override
	protected PageRec doCreatePage(IWorkbenchPart part) {
		return null;
	}

	@Override
	protected void doDestroyPage(IWorkbenchPart part, PageRec pageRecord) {
	}

	@Override
	protected IWorkbenchPart getBootstrapPart() {
		return null;
	}
}
