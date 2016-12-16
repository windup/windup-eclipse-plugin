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
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.jboss.tools.common.xml.IMemento;
import org.jboss.tools.common.xml.XMLMemento;
import org.jboss.windup.bootstrap.help.Help;
import org.jboss.windup.bootstrap.help.OptionDescription;
import org.osgi.framework.BundleContext;

import com.google.common.collect.Lists;

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
    
    private static final String HELP_CACHE = "/cache/help/help.xml"; //$NON-NLS-1$

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
     * Returns the cached help file from the embedded Windup installation.
     */
	public static Help findWindupHelpCache() {
		Help result = new Help();
    	File windupHome = WindupRuntimePlugin.findWindupHome();
    	File cacheFile = new File(windupHome, HELP_CACHE);
		try {
		  	URL url = cacheFile.toURI().toURL();
			InputStream input = url.openStream();
        	XMLMemento root = XMLMemento.createReadRoot(input);
            for (IMemento element : root.getChildren("option")) { //$NON-NLS-1$
            	String name = element.getString("name"); //$NON-NLS-1$
            	XMLMemento descriptionChild = (XMLMemento)element.getChild("description"); //$NON-NLS-1$
            	String description = descriptionChild.getTextData();
            	XMLMemento typeChild = (XMLMemento)element.getChild("type"); //$NON-NLS-1$
            	String type = typeChild.getTextData();
            	XMLMemento uiTypeChild = (XMLMemento)element.getChild("ui-type"); //$NON-NLS-1$
            	String uiType = uiTypeChild.getTextData();
            	List<String> availableOptions = Lists.newArrayList();
            	XMLMemento availableOptionsElement = (XMLMemento)element.getChild("available-options"); //$NON-NLS-1$
            	if (availableOptionsElement != null) {
            		for (IMemento optionElement : availableOptionsElement.getChildren("option")) {
            			XMLMemento optionMemento = (XMLMemento)optionElement;
            			String availableOption = optionMemento.getTextData();
            			availableOptions.add(availableOption);
            		}
            	}
            	OptionDescription option = new OptionDescription(name, description, type, uiType, availableOptions);
				result.getOptions().add(option);
            }
        } catch (Exception e) {
			WindupRuntimePlugin.log(e);
		}
		return result;
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
    
	public static void log(IStatus status) {
		if (WindupRuntimePlugin.getDefault() != null) {
			WindupRuntimePlugin.getDefault().getLog().log(status);
		}
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