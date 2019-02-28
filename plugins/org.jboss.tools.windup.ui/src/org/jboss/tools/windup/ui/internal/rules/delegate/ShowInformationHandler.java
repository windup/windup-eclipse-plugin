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
package org.jboss.tools.windup.ui.internal.rules.delegate;

import javax.inject.Inject;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.e4.core.di.annotations.Optional;
import org.jboss.tools.windup.ui.internal.rules.delegate.ControlInformationSupport.HoverInfoControlManager;

public class ShowInformationHandler extends AbstractHandler {
	
	@Inject @Optional private HoverInfoControlManager hoverManager;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		hoverManager.setFocus();
		return null;
	}
}
