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
package org.jboss.tools.windup.ui.internal.editor.launch;

import static org.jboss.tools.windup.model.domain.WindupConstants.ACTIVE_CONFIG;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.jboss.tools.windup.core.services.WindupService;
import org.jboss.tools.windup.windup.ConfigurationElement;

/**
 * Handler for running Windup using the active {@link ConfigurationElement}.
 */
public class RunWindupHanlder {

	private ConfigurationElement configuration;
	
	@Inject
	@Optional
	public void updateConfiguration(@UIEventTopic(ACTIVE_CONFIG) ConfigurationElement configuration) {
		this.configuration = configuration;
	}
	
	@Execute
	public void run(WindupService windup) {
	}
	
	@CanExecute
	public boolean canExecute() {
		return configuration != null;
	}
}
