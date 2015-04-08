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
public class FurnaceRepositoryProvider implements IFurnaceRepositoryProvider
{
    /**
     * <p>
     * Location of the Windup Furnace add on repository.
     * </p>
     */
    private static final String WINDUP_DIRECTORY = "windup"; //$NON-NLS-1$

    @Override
    public List<IFurnaceRepository> getRepositories()
    {
        List<IFurnaceRepository> windupRepos = new ArrayList<IFurnaceRepository>();
        File windupHome = findWindupHome();
        File windupAddonsDir = new File(windupHome, "addons");

        windupRepos.add(new FurnaceRepository(windupAddonsDir, true));

        return windupRepos;
    }

    public static File findWindupHome()
    {
        try
        {
            File bundleFile = FileLocator.getBundleFile(WindupRuntimePlugin.getDefault().getBundle());
            File windupDirectory = new File(bundleFile, WINDUP_DIRECTORY);
            for (File file : windupDirectory.listFiles())
            {
                if (file.isDirectory() && new File(file, "rules").exists())
                {
                    return file;
                }
            }
            return null;
        }
        catch (IOException e)
        {
            WindupRuntimePlugin.logError("Error getting Windup Furnace add on repository location.", e); //$NON-NLS-1$
            return null;
        }
    }

    @Override
    public ClassLoader getClassLoader()
    {
        return WindupRuntimePlugin.getDefault().getClass().getClassLoader();
    }
}