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
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.xtext.builder.standalone.StandaloneBuilderModule;
import org.jboss.tools.windup.core.WindupCorePlugin;
import org.jboss.tools.windup.model.domain.WindupConstants;
import org.osgi.framework.BundleContext;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;

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
    public static final String IMG_JRE = "icons/module_view.png"; //$NON-NLS-1$
    public static final String IMG_SEARCH = "icons/search.png"; //$NON-NLS-1$
    
    public static final String IMG_ERROR = "icons/error.png"; //$NON-NLS-1$
    public static final String IMG_WARNING = "icons/warning.png"; //$NON-NLS-1$
    public static final String IMG_INFO = "icons/info.gif"; //$NON-NLS-1$
    public static final String IMG_QUICKFIX_ERROR = "icons/quickfix_error.png"; //$NON-NLS-1$
    public static final String IMG_QUICKFIX_WARNING = "icons/quickfix_warning.png"; //$NON-NLS-1$
    public static final String IMG_QUICKFIX_INFO = "icons/quickfix_info.png"; //$NON-NLS-1$
    public static final String IMG_RULES = "icons/rules/repositories.gif"; //$NON-NLS-1$
    public static final String IMG_RULE = "icons/rule.png"; //$NON-NLS-1$
    public static final String IMG_ARGS = "icons/variable_tab.gif"; //$NON_NLS-1$
    public static final String IMG_SEVERITY = "icons/severity.png"; //$NON-NLS-1$
    public static final String IMG_EXPANDALL = "icons/expandall.png"; //$NON-NLS-1$
    public static final String IMG_FIXED = "icons/fixedIssue.gif"; //$NON-NLS-1$
    public static final String IMG_DELETE_ALL = "icons/deleteAllMarkers.png"; //$NON-NLS-1$
    public static final String IMG_REPORT = "icons/report.gif"; //$NON-NLS-1$
    public static final String IMG_STALE_ISSUE = "icons/stale_issue.gif"; //$NON-NLS-1$
    public static final String IMG_SERVER = "icons/server.gif"; //$NON-NLS-1$
    public static final String IMG_CONFIG_HOT = "icons/config_hot.png"; //$NON-NLS-1$
    public static final String IMG_CONFIG_COLD = "icons/config_cold.png"; //$NON-NLS-1$
    public static final String IMG_LAUNCH_BG = "icons/launch_button_bg.png"; //$NON-NLS-1$
    public static final String IMG_SERVER_NOT_STARTE = "icons/server_not_started.gif"; //$NON-NLS-1$
    public static final String IMG_START = "icons/start.gif"; //$NON-NLS-1$
    public static final String IMG_STOP = "icons/stop.gif"; //$NON-NLS-1$
    public static final String IMG_START_DISABLED = "icons/start_disabled.gif"; //$NON-NLS-1$
    public static final String IMG_STOP_DISABLED = "icons/stop_disabled.gif"; //$NON-NLS-1$
    public static final String IMG_OPTIONS_TAB = "icons/options_tab.gif"; //$NON-NLS-1$
    
    public static final String IMG_XML_RULE = "icons/xml_rule.gif"; //$NON-NLS-1$
    public static final String IMG_GROOVY_RULE = "icons/groovy_rule.gif"; //$NON-NLS-1$
    
    public static final String IMG_RULE_REPO = "icons/rules/repository-middle.gif"; //$NON-NLS-1$
    public static final String IMG_RULE_SET = "icons/rules/repository-middle.gif"; //$NON-NLS-1$
    
    public static final String IMG_XML_WIZ = "icons/generatexml_wiz.png"; //$NON-NLS-1$
    public static final String IMG_JAVA_WIZ = "icons/newclass_wiz.png"; //$NON-NLS-1$
    
    public static final String IMG_REMOVE_RULESET = "icons/rules/remove_ruleset.gif"; //$NON-NLS-1$
    public static final String IMG_NEW_XML_RULE = "icons/rules/new_xml_rule.png"; //$NON-NLS-1$
    
    // The shared instance
    private static WindupUIPlugin plugin;
    
    private Injector injector;
    
    /**
     * The constructor
     */
    public WindupUIPlugin() {
    }

    /**
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        getInjector();
    }
    
    @Override
    protected void initializeDefaultPreferences(IPreferenceStore store) {
    	store.setDefault(WindupConstants.SHOW_GETTING_STARTED, true);
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
		reg.put(IMG_JRE, createImageDescriptor(IMG_JRE));
		reg.put(IMG_SEARCH, createImageDescriptor(IMG_SEARCH));
		reg.put(IMG_ERROR, createImageDescriptor(IMG_ERROR));
		reg.put(IMG_WARNING, createImageDescriptor(IMG_WARNING));
		reg.put(IMG_INFO, createImageDescriptor(IMG_INFO));
		reg.put(IMG_QUICKFIX_ERROR, createImageDescriptor(IMG_QUICKFIX_ERROR));
		reg.put(IMG_QUICKFIX_WARNING, createImageDescriptor(IMG_QUICKFIX_WARNING));
		reg.put(IMG_QUICKFIX_INFO, createImageDescriptor(IMG_QUICKFIX_INFO));
		reg.put(IMG_SEVERITY, createImageDescriptor(IMG_SEVERITY));
		reg.put(IMG_EXPANDALL, createImageDescriptor(IMG_EXPANDALL));
		reg.put(IMG_FIXED, createImageDescriptor(IMG_FIXED));
		reg.put(IMG_DELETE_ALL, createImageDescriptor(IMG_DELETE_ALL));
		reg.put(IMG_REPORT, createImageDescriptor(IMG_REPORT));
		reg.put(IMG_STALE_ISSUE, createImageDescriptor(IMG_STALE_ISSUE));
		reg.put(IMG_ARGS, createImageDescriptor(IMG_ARGS));
		reg.put(IMG_SERVER, createImageDescriptor(IMG_SERVER));
		reg.put(IMG_CONFIG_HOT, createImageDescriptor(IMG_CONFIG_HOT));
		reg.put(IMG_CONFIG_COLD, createImageDescriptor(IMG_CONFIG_COLD));
		reg.put(IMG_LAUNCH_BG, createImageDescriptor(IMG_LAUNCH_BG));
		reg.put(IMG_START, createImageDescriptor(IMG_START));
		reg.put(IMG_STOP, createImageDescriptor(IMG_STOP));
		reg.put(IMG_START_DISABLED, createImageDescriptor(IMG_START_DISABLED));
		reg.put(IMG_STOP_DISABLED, createImageDescriptor(IMG_STOP_DISABLED));
		reg.put(IMG_OPTIONS_TAB, createImageDescriptor(IMG_OPTIONS_TAB));
		reg.put(IMG_RULES, createImageDescriptor(IMG_RULES));
		reg.put(IMG_RULE, createImageDescriptor(IMG_RULE));
		reg.put(IMG_XML_RULE, createImageDescriptor(IMG_XML_RULE));
		reg.put(IMG_GROOVY_RULE, createImageDescriptor(IMG_GROOVY_RULE));
		reg.put(IMG_RULE_SET, createImageDescriptor(IMG_RULE_SET));
		reg.put(IMG_XML_WIZ, createImageDescriptor(IMG_XML_WIZ));
		reg.put(IMG_JAVA_WIZ, createImageDescriptor(IMG_JAVA_WIZ));
		reg.put(IMG_RULE_REPO, createImageDescriptor(IMG_RULE_REPO));
		reg.put(IMG_REMOVE_RULESET, createImageDescriptor(IMG_REMOVE_RULESET));
		reg.put(IMG_NEW_XML_RULE, createImageDescriptor(IMG_NEW_XML_RULE));
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
    
    public Injector getInjector() {
    	if (injector == null) {
    		injector = createInjector();
    	}
    	return injector;
    }
    
    private Injector createInjector() {
    	try {
    		return Guice.createInjector(Lists.newArrayList(new WindupUiModule(), new StandaloneBuilderModule()));
    	} catch (Exception e) {
    		logErrorMessage("Failed to create injector");
    		logError(e.getMessage(), e);
    		throw new RuntimeException("Failed to create injector.", e);
    	}
    }
    
    public IEclipseContext getContext() {
		return ((MApplication)PlatformUI.getWorkbench().getService(MApplication.class)).getContext();
	}
}
