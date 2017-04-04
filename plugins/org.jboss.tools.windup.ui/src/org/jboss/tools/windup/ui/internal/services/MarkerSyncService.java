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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.jboss.tools.windup.model.util.DocumentUtils;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.windup.Hint;
import org.jboss.tools.windup.windup.MarkerElement;

/**
 * Service for synchronizing Windup markers with resources changes.
 */
public class MarkerSyncService implements IResourceChangeListener, IResourceDeltaVisitor {
	
	@Inject private MarkerLookupService markerService;
	
	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		try {
			event.getDelta().accept(this);
		} catch (CoreException e) {
			WindupUIPlugin.log(e);
		}
	}
	
	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {
		IResource resource = delta.getResource();
		if (resource instanceof IFile) {
			switch (delta.getKind()) {
				case IResourceDelta.CHANGED: {
					if ((delta.getFlags() & IResourceDelta.CONTENT) != 0) {
						for (MarkerElement element : markerService.find(resource)) {
							if (element instanceof Hint) {
								update((Hint)element);
							}
						}
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * Marks issues as stale if the issue's original line of code differs from the current. 
	 */
	private void update(Hint hint) {
		if (!hint.isStale() && !hint.isFixed()) {
			IMarker marker = (IMarker)hint.getMarker();
			
			int lineNumber = marker.getAttribute(IMarker.LINE_NUMBER, hint.getLineNumber()) - 1;
			int lineNumbers = DocumentUtils.getLineNumbers(marker.getResource());
			
			if (lineNumber < lineNumbers || lineNumber > lineNumbers) {
				markerService.delete(marker, hint);
			}
			
			else if (DocumentUtils.differs(marker.getResource(), lineNumber, hint.getOriginalLineSource())) {
				markerService.setStale(hint);
			}
		}
	}
	
	@PostConstruct
	private void init() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.PRE_BUILD);
		
	}
	
	@PreDestroy
	private void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
	}
}
