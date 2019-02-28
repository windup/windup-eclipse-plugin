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
package org.jboss.tools.windup.ui.internal.rules;

import java.rmi.RemoteException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;

public class RefreshRulesetsHandler extends AbstractHandler {
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		RuleRepositoryView view = (RuleRepositoryView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(RuleRepositoryView.VIEW_ID);
		if (view != null) {
			try {
				view.refresh();
			} catch (RemoteException e) {
				// ignore. it's just a refresh
			}
		}
		return null;
	}
}
