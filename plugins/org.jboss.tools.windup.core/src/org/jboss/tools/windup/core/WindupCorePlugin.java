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
package org.jboss.tools.windup.core;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

public class WindupCorePlugin extends Plugin
{
    /**
     * The id for windup classification markers
     */
    public static final String WINDUP_CLASSIFICATION_MARKER_ID = "org.jboss.tools.windup.core.classificationMarker"; //$NON-NLS-1$
    /**
     * The id for windup hint markers
     */
    public static final String WINDUP_HINT_MARKER_ID = "org.jboss.tools.windup.core.hintMarker"; //$NON-NLS-1$

    /**
     * <p>
     * The plugin ID.
     * </p>
     */
    public static final String PLUGIN_ID = "org.jboss.tools.windup.core"; //$NON-NLS-1$

    /**
     * <p>
     * The singleton instance of the plugin.
     * </p>
     */
    private static WindupCorePlugin plugin;

    /**
     * @return singleton instance of the plugin
     */
    public static WindupCorePlugin getDefault()
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
        WindupCorePlugin.getDefault().getLog().log(
                    new Status(IStatus.ERROR, WindupCorePlugin.PLUGIN_ID, message, exception));
    }
}