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
package org.jboss.tools.windup.ui.internal.editor;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.custom.BusyIndicator;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;

public class RulesetElementsCollapseAllAction extends Action {
	
	private Viewer viewer;

	public RulesetElementsCollapseAllAction(Viewer viewer) {
		super(Messages.collapseAll);
		this.viewer = viewer;
	}
	
	@Override
	public void run() {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				performAction();
			}
		};
		BusyIndicator.showWhile(viewer.getControl().getShell().getDisplay(), r);
	}
	
	private void performAction() {
		((TreeViewer)viewer).collapseAll();
	}
	
	@Override
	public ImageDescriptor getImageDescriptor() {
		return WindupUIPlugin.getImageDescriptor(WindupUIPlugin.IMG_COLLAPSE_ALL);
	}
	
	@Override
	public ImageDescriptor getDisabledImageDescriptor() {
		return WindupUIPlugin.getImageDescriptor(WindupUIPlugin.IMG_COLLAPSE_ALL_DISABLED);
	}


	@Override
	public String getToolTipText() {
		return Messages.collapseAll;
	}
}
