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

import static org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME;
import static org.jboss.tools.windup.model.domain.WindupConstants.DEFAULT;
import static org.jboss.tools.windup.model.domain.WindupConstants.LAUNCH_COMPLETED;
import static org.jboss.tools.windup.ui.internal.util.ResourceUtils.findProject;

import javax.inject.Inject;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.jboss.tools.windup.core.services.WindupService;

/**
 * Service for annotating eclipse {@link IResource}s with Windup's hints and classifications.
 */
public class MarkerService {
	
	@Inject private WindupService windup;

	@Inject
	@Optional
	private void activeWindupReportView(@UIEventTopic(LAUNCH_COMPLETED) ILaunchConfiguration configuration) 
			throws CoreException {
		IProject project = findProject(configuration.getAttribute(ATTR_PROJECT_NAME, DEFAULT));
		// TODO:
		/*for (Hint hint : windup.getHints(project)) {
			File file = hint.getFile();
			IFile resource = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(file.getAbsolutePath()));
			IMarker marker = resource.createMarker(WindupCorePlugin.WINDUP_HINT_MARKER_ID);
			marker.setAttribute(IMarker.MESSAGE, hint.getHint());
			marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		}*/
	}
}
