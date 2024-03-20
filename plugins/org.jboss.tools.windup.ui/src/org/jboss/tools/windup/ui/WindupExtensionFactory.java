/*******************************************************************************
 * Copyright (c) 2021 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui;

import org.jboss.tools.foundation.ui.ext.AbstractUiExtensionFactory;
import org.osgi.framework.Bundle;

import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.google.inject.Provider;

/**
 * Windup e4 extension factory.
 */
public class WindupExtensionFactory extends AbstractUiExtensionFactory {
	
	protected Bundle getBundle() {
		return WindupUIPlugin.getDefault().getBundle();
	}
	
	@Override
	protected Object getInstance() throws Exception {
		Injector injector = WindupUIPlugin.getDefault().getInjector();
		Class<?> type = getBundle().loadClass(data);
		System.out.println("Guice :: getInstance");
		System.out.println(type);
		try {
			injector.getBinding(type);
			return injector.getInstance(type);
		} catch (ConfigurationException e) {
			try {
				Provider<?> provider = injector.getProvider(type);
				return provider.get();
			} catch (ConfigurationException e2) {
				System.out.println("Error creating @Inject instance.");
				e.printStackTrace();
			}
		} 
		return super.getInstance();
	}
}
