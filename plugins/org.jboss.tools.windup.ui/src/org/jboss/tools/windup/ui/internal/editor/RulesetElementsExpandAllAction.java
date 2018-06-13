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

public class RulesetElementsExpandAllAction extends Action {
	
	private Viewer viewer;

	public RulesetElementsExpandAllAction(Viewer viewer) {
		super(Messages.expandAll);
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
		((TreeViewer)viewer).expandAll();
	}
	
	@Override
	public ImageDescriptor getImageDescriptor() {
		return WindupUIPlugin.getImageDescriptor(WindupUIPlugin.IMG_EXPANDALL);
	}
	
	@Override
	public ImageDescriptor getDisabledImageDescriptor() {
		return WindupUIPlugin.getImageDescriptor(WindupUIPlugin.IMG_EXPAND_ALL_DISABLED);
	}

	@Override
	public String getToolTipText() {
		return Messages.expandAll;
	}
}
