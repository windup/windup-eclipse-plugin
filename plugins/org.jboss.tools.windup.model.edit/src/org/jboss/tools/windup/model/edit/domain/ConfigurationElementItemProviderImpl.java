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

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.windup.windup.ConfigurationElement;
import org.jboss.tools.windup.windup.provider.ConfigurationElementItemProvider;

/**
 * ConfigurationElement customized item provider.
 */
public class ConfigurationElementItemProviderImpl extends ConfigurationElementItemProvider {

	public ConfigurationElementItemProviderImpl(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	public String getText(Object object) {
		return ((ConfigurationElement)object).getName();
	}
	
	/* (non-Javadoc)
	 * @see org.jboss.tools.windup.windup.provider.ConfigurationElementItemProvider#getImage(java.lang.Object)
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/WindupModel"));
	}
}
