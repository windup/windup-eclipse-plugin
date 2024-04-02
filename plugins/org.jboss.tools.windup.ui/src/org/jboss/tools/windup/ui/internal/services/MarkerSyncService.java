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


import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
//import jakarta.inject.Inject;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Display;
import org.jboss.tools.windup.model.util.DocumentUtils;
import org.jboss.tools.windup.ui.WindupExtensionFactory;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.windup.Hint;
import org.jboss.tools.windup.windup.MarkerElement;
import org.osgi.framework.Bundle;

import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.google.inject.Provider;

/**
 * Service for synchronizing Windup markers with resources changes.
 */
public class MarkerSyncService implements IResourceChangeListener, IResourceDeltaVisitor {
	
	static WindupExtensionFactory factory = new WindupExtensionFactory();
	
	private static Bundle getBundle() {
		return WindupUIPlugin.getDefault().getBundle();
	}
	
	public static <T> T getInstance(String className) {
		try {
//			factory.setInitializationData(null, className, "");
//			T result = (T) factory.getInstance();
//			return result;
			Injector injector = WindupUIPlugin.getDefault().getInjector();
			Class<?> type = getBundle().loadClass(className);
			System.out.println("Guice :: getInstance");
			System.out.println(type);
			try {
				injector.getBinding(type);
				return (T) injector.getInstance(type);
			} catch (ConfigurationException e) {
				try {
					Provider<?> provider = injector.getProvider(type);
					return (T) provider.get();
				} catch (ConfigurationException e2) {
					System.out.println("Error creating @Inject instance.");
					e.printStackTrace();
				}
			} 
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return null;
	}
	
//	@Inject private MarkerService markerService;
	private MarkerService markerService = getInstance("org.jboss.tools.windup.ui.internal.services.MarkerService");
	
	
	
	@PostConstruct
	private void init() {
		System.out.println("@PostConstruct :: MarkerSyncService");
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.PRE_BUILD);
		
	}
	
	@PreDestroy
	private void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
	}
	
	
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
			
			if (marker == null) {
				Display.getDefault().syncExec(() -> {
					markerService.delete(marker, hint);
				});
			}
			
			int lineNumber = marker.getAttribute(IMarker.LINE_NUMBER, hint.getLineNumber()) - 1;
			int lineNumbers = DocumentUtils.getLineNumbers(marker.getResource());
			
			if (lineNumber > lineNumbers || lineNumbers == 0) {
				Display.getDefault().syncExec(() -> {
					markerService.delete(marker, hint);
				});
			}
			
			else if (DocumentUtils.differs(marker.getResource(), lineNumber, hint.getOriginalLineSource())) {
				Display.getDefault().syncExec(() -> {
					markerService.setStale(hint);
				});
			}
		}
	}
	

}
