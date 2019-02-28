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
package org.jboss.tools.windup.model;

import java.util.List;

/**
 * Container for facades.
 */
public class Facades {

	public static interface IFacade {
	}
	
	public static interface FacadeOwner {
		List<? extends IFacade> getFacades();
		<T extends IFacade> T getFacade(Class<T> type);
	}
}
