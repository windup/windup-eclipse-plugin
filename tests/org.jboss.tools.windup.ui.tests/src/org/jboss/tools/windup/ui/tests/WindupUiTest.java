/*******************************************************************************
 * Copyright (c) 2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.tests;

import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.jboss.tools.windup.core.test.WindupTest;
import org.jboss.tools.windup.ui.WindupPerspective;
import org.jboss.tools.windup.ui.tests.swtbot.WorkbenchBot;

/**
 * Base class for Windup UI tests.
 */
public class WindupUiTest extends WindupTest {

	@Inject protected MApplication application;
	@Inject protected EModelService modelService;
	
	@Inject protected WorkbenchBot workbenchBot;
	
	protected void openWindupPerspective() {
		workbenchBot.perspectiveById(WindupPerspective.ID).activate();
	}
}
