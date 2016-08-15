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
package org.jboss.tools.windup.core.test;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.windup.core.services.WindupService;
import org.junit.Before;

import org.junit.Assert;

public class WindupTest {
	
	@Inject	protected WindupService windupService;
	
	@Before
	public void setup() {
		if ( getContext() == null ) {
			Assert.fail("Could not get Eclipse workbench as a context for running tests!");
		}
		ContextInjectionFactory.inject(this, getContext());
	}
	
	private IEclipseContext getContext() {
		if (!PlatformUI.isWorkbenchRunning()) {
			return null;
		}
		return PlatformUI.getWorkbench().getService(MApplication.class).getContext().getActiveLeaf();
	}
}
