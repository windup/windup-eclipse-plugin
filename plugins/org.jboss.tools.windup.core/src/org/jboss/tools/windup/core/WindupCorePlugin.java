package org.jboss.tools.windup.core;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.jboss.windup.WindupEngine;
import org.jboss.windup.WindupEnvironment;
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
	private WindupEngine engine;
	
	/**
	 * @return singleton instance of the plugin
	 */
	public static WindupCorePlugin getDefault() {
		return plugin;
	}
	
	public WindupEngine getEngine() {
		if(this.engine == null) {
			try {
				WindupEnvironment settings = new WindupEnvironment();
				settings.setSource(true);
				
				this.engine = new WindupEngine(settings);
			} catch(Throwable t) {
				this.logError("Error getting Windup Engine.", t); //$NON-NLS-1$
			}
		}
		
		return this.engine;
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
	
	public void logError(String message, Throwable t) {
		StringBuffer logMessage = new StringBuffer();
		logMessage.append(message);
		logMessage.append("\n"); //$NON-NLS-1$
		logMessage.append(t.getMessage());
		this.getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, logMessage.toString(), t));
	}
}