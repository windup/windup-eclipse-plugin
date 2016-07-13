/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.model.edit.domain;

import org.eclipse.emf.common.notify.Adapter;
import org.jboss.tools.windup.windup.provider.WindupItemProviderAdapterFactory;

/**
 * Windup's item provider.
 */
public class WindupItemProvider extends WindupItemProviderAdapterFactory {

	@Override
	public Adapter createConfigurationElementAdapter() {
		if (configurationElementItemProvider == null) {
			configurationElementItemProvider = new ConfigurationElementItemProviderImpl(this);
		}
		return configurationElementItemProvider;
	}
}
