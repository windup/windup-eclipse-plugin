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
package org.jboss.tools.windup.ui.internal.services;

import java.io.File;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPlaceholder;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.UIEvents.EventTags;
import org.eclipse.e4.ui.workbench.UIEvents.Window;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.foundation.ui.util.BrowserUtility;
import org.jboss.tools.windup.model.Activator;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.ui.WindupPerspectiveFactory;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.explorer.IssueExplorer;
import org.jboss.tools.windup.ui.internal.views.WindupReportView;
import org.jboss.tools.windup.windup.ConfigurationElement;
import org.osgi.service.event.Event;

/**
 * Service for view related functionality.
 */
@Creatable
@Singleton
public class ViewService {

//	@Inject private EPartService partService;
//	@Inject private MApplication application;
//	@Inject private ModelService modelService;
	
	public ViewService() {
	}
	
	public WindupReportView activateWindupReportView() {
		MApplication application = E4Workbench.getServiceContext().get(MApplication.class);
		MTrimmedWindow window = (MTrimmedWindow) application.getChildren().get(0);
//		application.getChildren().get(0).getContext().activate();
		
		EPartService partService = (EPartService)window.getContext().get(EPartService.class);
		
		
		MPlaceholder holder = partService.createSharedPart(WindupReportView.ID, false);
		MPart part = (MPart)holder.getRef();
		
		 partService.showPart(part, PartState.CREATE);
//		partService.showPart(part, PartState.ACTIVATE);
//		part = partService.createPart(WindupReportView.ID);
		 
//		 WindupUIPlugin.getDefault().getvi.showView("org.eclipse.ui.views.PropertySheet");
		 try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(WindupReportView.ID);
		 }
		 catch (Exception e) {
			 e.printStackTrace();
			 System.out.println(e.getMessage());
		 }
		 
		 
		return (WindupReportView)part.getObject();
	}
	
	public void renderReport(ConfigurationElement configuration) {
	    	if (configuration.isGenerateReport()) {
//	    		IPath path = Path.fromOSString(configuration.getOutputLocation());
//	    		IPath report = path.append(ModelService.PROJECT_REPORT_HOME_PAGE);
	    		
	    		String report = configuration.getOutputLocation() + Path.SEPARATOR + "static-report" + Path.SEPARATOR + "index.html";
	    		
	    		File file = new File(report.toString());
	    		if (file.exists()) {
				Display.getDefault().asyncExec(() -> {
					openReport(file);
					/*final WindupReportView view = activateWindupReportView();
					if (view != null) {
						view.showReport(report, true);
					}*/
				});
	    		}
	    	}
    }
	
	public static void openReport(File report) {
		String url = report.toURI().toString();
		new BrowserUtility().openExtenalBrowser(url);
	}
    
	public void launchStarting() {
//		Display.getDefault().asyncExec(() -> {
//		    	WindupReportView view = activateWindupReportView();
//			if (view != null) {
//				view.showMessage("No report available.", true);
//			}
//		});
    }
    
//    @Inject
//    @Optional
//    public void showIssueExplorer(@UIEventTopic(UIEvents.UILifeCycle.PERSPECTIVE_OPENED) Event event) {
//		Object element = event.getProperty(EventTags.ELEMENT);
//		if (element instanceof MPerspective) {
//			MPerspective perspective = (MPerspective) element;
//			if (perspective.getElementId().equals(WindupPerspectiveFactory.ID)) {
//				MApplication application = E4Workbench.getServiceContext().get(MApplication.class);
//				MTrimmedWindow window = (MTrimmedWindow) application.getChildren().get(0);
//				EPartService partService = (EPartService)window.getContext().get(EPartService.class);
//				MPart part = partService.findPart(IssueExplorer.VIEW_ID);
//				if (part != null) {
//					partService.activate(part);
//				}
//			}
//		}
//    } 
}
