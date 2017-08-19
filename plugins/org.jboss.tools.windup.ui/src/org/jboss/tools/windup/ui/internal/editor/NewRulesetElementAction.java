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
package org.jboss.tools.windup.ui.internal.editor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.actions.BaseSelectionListenerAction;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;

public class NewRulesetElementAction extends BaseSelectionListenerAction {

	private Viewer viewer;
	
	public NewRulesetElementAction(Viewer viewer) {
		super(Messages.newElement);
		this.viewer = viewer;
		viewer.addSelectionChangedListener(this);
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
		
	}
	
	@Override
	public ImageDescriptor getImageDescriptor() {
		return WindupUIPlugin.getImageDescriptor(WindupUIPlugin.IMG_ADD);
	}
	
	@Override
	public String getToolTipText() {
		return Messages.newElement;
	}
}
