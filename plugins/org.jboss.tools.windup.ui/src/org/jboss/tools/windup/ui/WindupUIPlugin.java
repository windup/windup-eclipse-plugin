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
package org.jboss.tools.windup.ui;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.jboss.tools.windup.core.WindupCorePlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle for the UI components of the Windup Eclipse plugin.
 */
public class WindupUIPlugin extends AbstractUIPlugin
{

    // The plug-in ID
    public static final String PLUGIN_ID = "org.jboss.tools.windup.ui"; //$NON-NLS-1$
    
    public static final String IMG_RUN_EXC = "icons/run_exc.png"; //$NON-NLS-1$
    public static final String IMG_HELP = "icons/help.png"; //$NON-NLS-1$
    public static final String IMG_NEW_CONFIG = "icons/new_con.png"; //$NON-NLS-1$
    public static final String IMG_DELETE_CONFIG = "icons/delete_config.png"; //$NON-NLS-1$
    public static final String IMG_WINDUP = "icons/windup.png"; //$NON-NLS-1$
    
    // The shared instance
    private static WindupUIPlugin plugin;
    
    /**
     * The constructor
     */
    public WindupUIPlugin()
    {
    }

    /**
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception
    {
        super.start(context);
        plugin = this;
    }

    /**
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception
    {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    public static WindupUIPlugin getDefault()
    {
        return plugin;
    }

    /**
     * Returns an image descriptor for the image file at the given plug-in relative path
     *
     * @param path the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path)
    {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
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
        WindupUIPlugin.getDefault().getLog().log(
                    new Status(IStatus.ERROR, WindupUIPlugin.PLUGIN_ID, message, exception));
    }
    
	public static void log(IStatus status) {
		WindupCorePlugin.getDefault().getLog().log(status);
	}

	public static void logErrorMessage(final String message) {
		log(new Status(IStatus.ERROR, PLUGIN_ID, IStatus.ERROR, message, null));
	}

	public static void logErrorMessage(final String message, final Throwable e) {
		log(new Status(IStatus.ERROR, PLUGIN_ID, IStatus.ERROR, message, e));
	}

	public static void log(Throwable e) {
		if (e instanceof InvocationTargetException)
			e = ((InvocationTargetException) e).getTargetException();
		IStatus status = null;
		if (e instanceof CoreException) {
			status = ((CoreException) e).getStatus();
		} else {
			status = new Status(IStatus.ERROR, PLUGIN_ID, IStatus.OK, e.getMessage(), e);
		}
		log(status);
	}
	
	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		reg.put(IMG_RUN_EXC, createImageDescriptor(IMG_RUN_EXC));
		reg.put(IMG_HELP, createImageDescriptor(IMG_HELP));
		reg.put(IMG_NEW_CONFIG, createImageDescriptor(IMG_NEW_CONFIG));
		reg.put(IMG_DELETE_CONFIG, createImageDescriptor(IMG_DELETE_CONFIG));
		reg.put(IMG_WINDUP, createImageDescriptor(IMG_WINDUP));
	}
    
    private ImageDescriptor createImageDescriptor(String path) {
    	URL url = FileLocator.find(getBundle(), new Path(path), null);
        return ImageDescriptor.createFromURL(url);
    }
    
    public static Shell getActiveWorkbenchShell() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window != null) {
			return window.getShell();
		}
		return null;
	}
}
