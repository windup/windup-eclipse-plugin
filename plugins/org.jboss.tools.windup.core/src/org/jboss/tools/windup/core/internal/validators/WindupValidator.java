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
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.wst.validation.AbstractValidator;
import org.eclipse.wst.validation.ValidationResult;
import org.eclipse.wst.validation.ValidatorMessage;
import org.jboss.tools.windup.core.WindupCorePlugin;
import org.jboss.windup.WindupEngine;
import org.jboss.windup.metadata.decoration.AbstractDecoration;
import org.jboss.windup.metadata.decoration.AbstractDecoration.NotificationLevel;
import org.jboss.windup.metadata.decoration.Line;
import org.jboss.windup.metadata.decoration.hint.Hint;
import org.jboss.windup.metadata.type.FileMetadata;

/**
 * TODO: IAN: doc me
 */
public class WindupValidator extends AbstractValidator {	
	private static final String WINDUP_MARKER_ID = "org.jboss.tools.windup.core.validationMarker"; //$NON-NLS-1$
	
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
		
		ValidationResult result = new ValidationResult();
		
		try {
			FileMetadata meta = this.getEngine().processFile(resource.getLocation().toFile());
			
			//for each decoration found on the file
			Collection<AbstractDecoration> decorations = meta.getDecorations();
			for(AbstractDecoration decoration : decorations) {
				
				//create and set the validation message
				StringBuffer message = new StringBuffer();
				message.append(Messages.Windup);
				message.append(decoration.getDescription());
				message.append("."); //$NON-NLS-1$

				//add hints to the validation message
				Set<Hint> hints = decoration.getHints();
				if(!hints.isEmpty()) {
					message.append(Messages.Hints);
					for(Hint hint : hints) {
						message.append(hint.toString());
						message.append(", "); //$NON-NLS-1$
					}
				}
				ValidatorMessage decorationMessage = ValidatorMessage.create(message.toString(), resource);
				
				//determine the severity
				decorationMessage.setAttribute(IMarker.SEVERITY, levelToSeverity(decoration.getLevel()));
				
				//set the specific WindUp category
				decorationMessage.setType(WINDUP_MARKER_ID);
				
				//determine line number to report issue on
				int lineNumber = 1;
				if(decoration instanceof Line) {
					lineNumber = ((Line) decoration).getLineNumber();
				}
				decorationMessage.setAttribute(IMarker.LINE_NUMBER, lineNumber);
				
				result.add(decorationMessage);
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
}