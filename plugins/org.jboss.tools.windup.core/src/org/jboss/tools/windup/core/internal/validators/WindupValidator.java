/*******************************************************************************
* Copyright (c) 2011 Red Hat, Inc.
* Distributed under license by Red Hat, Inc. All rights reserved.
* This program is made available under the terms of the
* Eclipse Public License v1.0 which accompanies this distribution,
* and is available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   Red Hat, Inc. - initial API and implementation
******************************************************************************/
package org.jboss.tools.windup.core.internal.validators;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.wst.validation.AbstractValidator;
import org.eclipse.wst.validation.ValidationResult;
import org.eclipse.wst.validation.ValidationState;
import org.eclipse.wst.validation.ValidatorMessage;
import org.jboss.tools.windup.core.WindupCorePlugin;
import org.jboss.windup.WindupEngine;
import org.jboss.windup.metadata.decoration.AbstractDecoration;
import org.jboss.windup.metadata.decoration.AbstractDecoration.NotificationLevel;
import org.jboss.windup.metadata.decoration.Line;
import org.jboss.windup.metadata.decoration.hint.Hint;
import org.jboss.windup.metadata.decoration.hint.MarkdownHint;
import org.jboss.windup.metadata.type.FileMetadata;

/**
 * <p>
 * {@link AbstractValidator} which uses the {@link WindupEngine} to add {@link ValidatorMessage}s
 * to resources based on the decorations and hits found by the {@link WindupEngine}.
 * </p>
 */
public class WindupValidator extends AbstractValidator {	
	private static final String WINDUP_DECORATION_MARKER_ID = "org.jboss.tools.windup.core.decorationMarker"; //$NON-NLS-1$
	private static final String WINDUP_HINT_MARKER_ID = "org.jboss.tools.windup.core.hintMarker"; //$NON-NLS-1$
	
	/**
	 * @see org.eclipse.wst.validation.AbstractValidator#clean(org.eclipse.core.resources.IProject, org.eclipse.wst.validation.ValidationState, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void clean(IProject project, ValidationState state, IProgressMonitor monitor) {
		cleanUpWindUpMarkers(project);
	}
	
	/**
	 * @see org.eclipse.wst.validation.AbstractValidator#validationStarting(org.eclipse.core.resources.IProject, org.eclipse.wst.validation.ValidationState, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void validationStarting(IProject project,
			org.eclipse.wst.validation.ValidationState state,
			IProgressMonitor monitor) {
		
		super.validationStarting(project, state, monitor);
	}
	
	/**
	 * @see org.eclipse.wst.validation.AbstractValidator#validationFinishing(org.eclipse.core.resources.IProject, org.eclipse.wst.validation.ValidationState, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void validationFinishing(IProject project,
			org.eclipse.wst.validation.ValidationState state,
			IProgressMonitor monitor) {
		
		super.validationFinishing(project, state, monitor);
	}
	
	/**
	 * @see org.eclipse.wst.validation.AbstractValidator#validate(org.eclipse.core.resources.IResource, int, org.eclipse.wst.validation.ValidationState, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public ValidationResult validate(IResource resource, int kind,
			org.eclipse.wst.validation.ValidationState state,
			IProgressMonitor monitor) {
		
		//before generating new results clean out existing ones
		cleanUpWindUpMarkers(resource);
		
		//create a new result
		ValidationResult result = new ValidationResult();
		try {
			//process the file with WindUp
			FileMetadata meta = this.getEngine().processFile(resource.getLocation().toFile());
			
			//if meta then WindUp matched on something in the file
			if(meta != null) {
				//for each decoration found on the file
				Collection<AbstractDecoration> decorations = meta.getDecorations();
				for(AbstractDecoration decoration : decorations) {
					
					//determine line number to report issue on
					int lineNumber = 1;
					if(decoration instanceof Line) {
						lineNumber = ((Line) decoration).getLineNumber();
					}
					
					//create validation message for the decoration
					ValidatorMessage decorationMessage = ValidatorMessage.create(decoration.getDescription(), resource);
					decorationMessage.setAttribute(IMarker.SEVERITY, levelToSeverity(decoration.getLevel()));
					decorationMessage.setType(WINDUP_DECORATION_MARKER_ID);
					decorationMessage.setAttribute(IMarker.LINE_NUMBER, lineNumber);
					
					//create validation messages for the hints
					Set<Hint> hints = decoration.getHints();
					if(!hints.isEmpty()) {
						for(Hint hint : hints) {
							String hintMessage = null;
							if(hint instanceof MarkdownHint) {
								hintMessage = ((MarkdownHint) hint).getMarkdown();
							} else {
								hintMessage = hint.toString();
							}
							
							ValidatorMessage hintValidatorMessage = ValidatorMessage.create(hintMessage, resource);
							hintValidatorMessage.setAttribute(IMarker.SEVERITY, levelToSeverity(decoration.getLevel()));
							hintValidatorMessage.setType(WINDUP_HINT_MARKER_ID);
							hintValidatorMessage.setAttribute(IMarker.LINE_NUMBER, lineNumber);
							result.add(hintValidatorMessage);
						}
					}
					
					result.add(decorationMessage);
				}
			}
		} catch (IOException e) {
			WindupCorePlugin.getDefault().logError("Error running WindUp: " + resource, e); //$NON-NLS-1$
		}
		
		return result;
	}
	
	/**
	 * TODO: IAN: doc me
	 * 
	 * @return
	 */
	private WindupEngine getEngine() {
		return WindupCorePlugin.getDefault().getEngine();
	}
	
	/**
	 * <p>Convert a {@link NotificationLevel} to an {@link IMarker} severity</p>
	 * 
	 * @param level {@link NotificationLevel} to convert to an {@link IMarker} severity
	 * 
	 * @return {@link IMarker} severity equivalent for the given {@link NotificationLevel}
	 */
	private static int levelToSeverity(NotificationLevel level) {
		int severity;
		switch(level) {
			case CRITICAL:
			case SEVERE: {
				severity = IMarker.SEVERITY_ERROR;
				break;
			}
		
			case INFO: {
				severity = IMarker.SEVERITY_INFO;
				break;
			}
			
			case WARNING: 
			default: {
				severity = IMarker.SEVERITY_WARNING;
				break;
			}
		}
		
		//NOTE: windup currently does not leverage notification levels well so make everything a warning
		
		return severity;
	}
	
	/**
	 * <p>
	 * Removes all of the WindUp markers from the given {@link IResource}.
	 * </p>
	 * 
	 * @param resource to cleanup the WindUp markers from
	 */
	private static void cleanUpWindUpMarkers(IResource resource) {
		if(resource != null) {
			try {
				resource.deleteMarkers(WINDUP_DECORATION_MARKER_ID, true, IResource.DEPTH_INFINITE);
				resource.deleteMarkers(WINDUP_HINT_MARKER_ID, true, IResource.DEPTH_INFINITE);
			} catch (CoreException e) {
				WindupCorePlugin.getDefault().logError("Error cleaning up markers from: " + resource, e); //$NON-NLS-1$
			}
		}
	}
}