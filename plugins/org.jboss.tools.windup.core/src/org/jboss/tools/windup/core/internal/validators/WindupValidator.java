/*******************************************************************************
* Copyright (c) 2013 Red Hat, Inc.
* Distributed under license by Red Hat, Inc. All rights reserved.
* This program is made available under the terms of the
* Eclipse Public License v1.0 which accompanies this distribution,
* and is available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   Red Hat, Inc. - initial API and implementation
******************************************************************************/
package org.jboss.tools.windup.core.internal.validators;

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
import org.jboss.tools.windup.core.WindupService;
import org.jboss.windup.reporting.model.InlineHintModel;

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

		//process the file with WindUp
		Iterable<InlineHintModel> inlineHints = WindupService.getDefault().getInlineHints(resource, monitor);
		
		//if meta then WindUp matched on something in the file
		if(inlineHints != null) {
			//for each hint found on the file
			for(InlineHintModel inlineHint : inlineHints) {
				
				//create validation message for the decoration
				ValidatorMessage hintMessage = ValidatorMessage.create(inlineHint.getHint(), resource);
				hintMessage.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
				hintMessage.setType(WINDUP_HINT_MARKER_ID);
				hintMessage.setAttribute(IMarker.LINE_NUMBER, inlineHint.getLineNumber());
				hintMessage.setAttribute(IMarker.CHAR_START, inlineHint.getColumnNumber());
				hintMessage.setAttribute(IMarker.CHAR_END, inlineHint.getColumnNumber() + inlineHint.getLength());
				
				result.add(hintMessage);
			}
		}
		
		return result;
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
				WindupCorePlugin.logError("Error cleaning up markers from: " + resource, e); //$NON-NLS-1$
			}
		}
	}
}