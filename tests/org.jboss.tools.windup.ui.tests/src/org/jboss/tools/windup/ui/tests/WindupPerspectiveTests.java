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
package org.jboss.tools.windup.ui.tests;

import static org.junit.Assert.assertTrue;

import java.util.Objects;

import org.jboss.tools.windup.ui.WindupPerspective;
import org.junit.Test;

public class WindupPerspectiveTests extends WindupUiTest {
	
	@Test
	public void testPerspective() {
		openWindupPerspective();
		assertTrue(Objects.equals(WindupPerspective.ID,	
				eModelService.getActivePerspective(application.getSelectedElement()).getElementId()));
	}
}
