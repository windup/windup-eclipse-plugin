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
package org.jboss.tools.windup.core.test;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class WindupCoreTestPlugin implements BundleActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.jboss.tools.windup.core.test"; //$NON-NLS-1$
	
	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/**
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		WindupCoreTestPlugin.context = bundleContext;
	}

	/**
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		WindupCoreTestPlugin.context = null;
	}
}