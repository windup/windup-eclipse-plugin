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
package org.jboss.tools.windup.ui.internal.services;

import static org.jboss.tools.windup.model.domain.WindupConstants.LAUNCH_COMPLETED;
import static org.jboss.tools.windup.model.domain.WindupConstants.LAUNCH_STARTING;

import java.io.File;

import javax.inject.Inject;

import org.eclipse.core.runtime.IPath;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPlaceholder;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.ui.internal.views.WindupReportView;
import org.jboss.tools.windup.windup.ConfigurationElement;
import org.jboss.tools.windup.windup.Input;

/**
 * Service for view related functionality.
 */
public class ViewService {

	@Inject private EPartService partService;
	@Inject private MApplication application;
	@Inject private ModelService modelService;
	
	/**
	 * @return the activated {@link WindupReportView}.
	 */
	public WindupReportView activateWindupReportView() {
		application.getChildren().get(0).getContext().activate();
		MPlaceholder holder = partService.createSharedPart(WindupReportView.ID, false);
		MPart part = (MPart)holder.getRef();
		partService.showPart(part, PartState.ACTIVATE);
		return (WindupReportView)part.getObject();
	}
	
    @Inject
	@Optional
	public void renderReport(@UIEventTopic(LAUNCH_COMPLETED) ConfigurationElement configuration) {
    	if (configuration.isGenerateReport() && !configuration.getInputs().isEmpty()) {
    		Input input = configuration.getInputs().get(0);
    		IPath path = modelService.getGeneratedReport(configuration, input);
    		File file = new File(path.toString());
    		if (file.exists()) {
    			activateWindupReportView().showReport(path, true);
    		}
    	}
    }
    
    @Inject
	@Optional
	public void launchStarting(@UIEventTopic(LAUNCH_STARTING) ConfigurationElement configuration) {
    	WindupReportView view = activateWindupReportView();
    	if (view != null) {
    		view.showMessage("No report available.", true);
    	}
    }
}
