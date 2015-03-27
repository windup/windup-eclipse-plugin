/*******************************************************************************
* Copyright (c) 2014 Red Hat, Inc.
* Distributed under license by Red Hat, Inc. All rights reserved.
* This program is made available under the terms of the
* Eclipse Public License v1.0 which accompanies this distribution,
* and is available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   Red Hat, Inc. - initial API and implementation
******************************************************************************/
package org.jboss.tools.windup.runtime.internal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.jboss.tools.forge.core.furnace.repository.FurnaceRepository;
import org.jboss.tools.forge.core.furnace.repository.IFurnaceRepository;
import org.jboss.tools.forge.core.furnace.repository.IFurnaceRepositoryProvider;
import org.jboss.tools.windup.runtime.internal.WindupRuntimePlugin;

/**
 * <p>
 * Windup Furnace add on repository provider.
 * </p>
 */
public class FurnaceRepositoryProvider implements IFurnaceRepositoryProvider {
	/**
	 * <p>
	 * Location of the Windup Furnace add on repository.
	 * </p>
	 */
	private static final String FURNACE_ADDON_REPOSITORY = "furnace-addon-repository"; //$NON-NLS-1$

	@Override
	public List<IFurnaceRepository> getRepositories() {
		List<IFurnaceRepository> windupRepos = new ArrayList<IFurnaceRepository>();
		try {
			File bundleFile = FileLocator.getBundleFile(WindupRuntimePlugin.getDefault().getBundle());
			windupRepos.add(new FurnaceRepository(new File(bundleFile, FURNACE_ADDON_REPOSITORY), true));
		} catch (IOException e) {
			WindupRuntimePlugin.logError("Error getting Windup Furnace add on repository location.", e); //$NON-NLS-1$
		}		
		
		return windupRepos;
	}

	@Override
	public ClassLoader getClassLoader() {
		return WindupRuntimePlugin.getDefault().getClass().getClassLoader();
	}
}