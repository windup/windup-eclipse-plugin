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
package org.jboss.tools.windup.runtime;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

/**
 * The activator class for the plugin containing
 *
 */
public class WindupRuntimePlugin extends Plugin
{
    /**
     * <p>
     * The plugin ID.
     * </p>
     */
    public static final String PLUGIN_ID = "org.jboss.tools.windup.runtime"; //$NON-NLS-1$

    /**
     * <p>
     * Location of the Windup home directory (this contains a full installation of windup)
     * </p>
     */
    private static final String WINDUP_DIRECTORY = "windup"; //$NON-NLS-1$

    /**
     * <p>
     * The singleton instance of the plugin.
     * </p>
     */
    private static WindupRuntimePlugin plugin;

    /**
     * Returns the root directory of the embedded Windup installation.
     */
    public static File findWindupHome()
    {
        try
        {
            File bundleFile = FileLocator.getBundleFile(WindupRuntimePlugin.getDefault().getBundle());
            File windupDirectory = new File(bundleFile, WINDUP_DIRECTORY);
            for (File file : windupDirectory.listFiles())
            {
                // find the directory with a rules subdirectory... this is the actual unzipped windup installation
                // (eg, PLUGIN_DIR/windup/windup-distribution-2.2.0.Final/)
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

    /**
     * @return singleton instance of the plugin
     */
    public static WindupRuntimePlugin getDefault()
    {
        return plugin;
    }

    /**
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext bundleContext) throws Exception
    {
        super.start(bundleContext);
        plugin = this;
    }

    /**
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext bundleContext) throws Exception
    {
        super.stop(bundleContext);
        plugin = null;
    }

    /**
     * <p>
     * Logs an error message.
     * </p>
     * 
     * @param message Error message to log
     */
    public static void logError(String message, Throwable exception)
    {
        WindupRuntimePlugin.getDefault().getLog().log(
                    new Status(IStatus.ERROR, WindupRuntimePlugin.PLUGIN_ID, message, exception));
    }
}