/*******************************************************************************
* Copyright (c) 2011 Red Hat, Inc.
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
import org.jboss.windup.WindupEngine;
import org.jboss.windup.WindupEnvironment;
import org.jboss.windup.reporting.ReportEngine;
import org.osgi.framework.BundleContext;

public class WindupCorePlugin extends Plugin {
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
	 * <p>
	 * {@link WindupEngine} instance used by this plugin.
	 * </p>
	 * 
	 * <p>
	 * This is really expensive to initialize, so it is only done once.
	 * </p>
	 */
	private WindupEngine windupEngine;
	
	/**
	 * <p>
	 * {@link ReportEngine} instance used by this plugin.
	 * </p>
	 * 
	 * <p>
	 * This is really expensive to initialize, so it is only done once.
	 * </p>
	 */
	private ReportEngine reportEngine;
	
	/**
	 * @return singleton instance of the plugin
	 */
	public static WindupCorePlugin getDefault() {
		return plugin;
	}
	
	/**
	 * <p>
	 * The {@link WindupEngine} is expensive to initialize so making sure there
	 * is ever only one helps.
	 * </p>
	 * 
	 * @return the single instance of the {@link WindupEngine} to be used in Eclipse
	 */
	public WindupEngine getWindupEngine() {
		if(this.windupEngine == null) {
			try {
				this.windupEngine = new WindupEngine(this.getWindupEnv());
			} catch(Throwable t) {
				logError("Error getting Windup Engine.", t); //$NON-NLS-1$
			}
		}
		
		return this.windupEngine;
	}
	
	/**
	 * <p>
	 * The {@link ReportEngine} is expensive to initialize so making sure there
	 * is ever only one helps.
	 * </p>
	 * 
	 * @return the single instance of the {@link ReportEngine} to be used in Eclipse
	 */
	public ReportEngine getWindupReportEngine() {
		if(this.reportEngine == null) {
			try {
				this.reportEngine = new ReportEngine(this.getWindupEnv());
			} catch(Throwable t) {
				logError("Error getting Windup Report Engine.", t); //$NON-NLS-1$
			}
		}
		
		return this.reportEngine;
	}
	
	/**
	 * <p>
	 * Single source for the {@link WindupEnvironment} to use within Eclipse.
	 * </p>
	 * 
	 * @return {@link WindupEnvironment} used to initialize Windup
	 */
	private WindupEnvironment getWindupEnv() {
		WindupEnvironment settings = new WindupEnvironment();
		settings.setSource(true);
		
		return settings;
	}

	/**
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		plugin = this;
	}

	/**
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
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
	public static void logError(String message, Throwable exception) {
		WindupCorePlugin.getDefault().getLog().log(
				new Status(IStatus.ERROR, WindupCorePlugin.PLUGIN_ID, message, exception));
	}
}