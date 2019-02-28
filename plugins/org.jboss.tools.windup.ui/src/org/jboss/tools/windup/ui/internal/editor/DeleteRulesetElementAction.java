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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.actions.BaseSelectionListenerAction;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;

public class DeleteRulesetElementAction extends BaseSelectionListenerAction {

	private Viewer viewer;
	private RulesetEditorRulesSection section;
	
	public DeleteRulesetElementAction(RulesetEditorRulesSection section, Viewer viewer) {
		super(Messages.deleteElement);
		this.section = section;
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
	
	@Override
	protected boolean updateSelection(IStructuredSelection selection) {
		return !selection.isEmpty();
	}
	
	@SuppressWarnings("unchecked")
	private void performAction() {
		section.removeNodes(((IStructuredSelection)viewer.getSelection()).toList());
	}
	
	@Override
	public ImageDescriptor getDisabledImageDescriptor() {
		return WindupUIPlugin.getImageDescriptor(WindupUIPlugin.IMG_DELETE_CONFIG_DISABLED);
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return WindupUIPlugin.getImageDescriptor(WindupUIPlugin.IMG_DELETE_CONFIG);
	}

	@Override
	public String getToolTipText() {
		return Messages.deleteElement;
	}
}