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
package org.jboss.tools.windup.ui;

import org.jboss.tools.foundation.ui.ext.AbstractUiExtensionFactory;
import org.osgi.framework.Bundle;

/**
 * Windup e4 extension factory.
 */
public class WindupExtensionFactory extends AbstractUiExtensionFactory {
	protected Bundle getBundle() {
		return WindupUIPlugin.getDefault().getBundle();
	}
}
