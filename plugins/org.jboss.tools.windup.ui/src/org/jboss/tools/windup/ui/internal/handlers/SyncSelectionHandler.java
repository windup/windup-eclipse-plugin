/*******************************************************************************
 * Copyright (c) 2021 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MToolItem;
import org.jboss.tools.windup.ui.internal.views.WindupReportView;

/**
 * Synchronizes the the Windup view with the active selection.
 */
public class SyncSelectionHandler {
	@Execute
	public void sync(MPart part, MToolItem item) {
		((WindupReportView)part.getObject()).setSynchronizeSelection(item.isSelected());
	}
}
