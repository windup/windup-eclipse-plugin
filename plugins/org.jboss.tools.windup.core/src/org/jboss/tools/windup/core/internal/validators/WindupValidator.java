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
import org.jboss.windup.reporting.model.Severity;
import org.jboss.windup.tooling.data.Classification;
import org.jboss.windup.tooling.data.Hint;

/**
 * <p>
 * {@link AbstractValidator} which uses the {@link WindupService} to add {@link ValidatorMessage}s to resources based on the decorations and hits
 * found by the {@link WindupService}.
 * </p>
 */
public class WindupValidator extends AbstractValidator
{
    private static final String WINDUP_CLASSIFICATION_MARKER_ID = "org.jboss.tools.windup.core.classificationMarker"; //$NON-NLS-1$
    private static final String WINDUP_HINT_MARKER_ID = "org.jboss.tools.windup.core.hintMarker"; //$NON-NLS-1$

    public WindupValidator()
    {
    }

    /**
     * @see org.eclipse.wst.validation.AbstractValidator#clean(org.eclipse.core.resources.IProject, org.eclipse.wst.validation.ValidationState,
     *      org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void clean(IProject project, ValidationState state, IProgressMonitor monitor)
    {
        cleanUpWindUpMarkers(project);
    }

    /**
     * @see org.eclipse.wst.validation.AbstractValidator#validationStarting(org.eclipse.core.resources.IProject,
     *      org.eclipse.wst.validation.ValidationState, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void validationStarting(IProject project,
                org.eclipse.wst.validation.ValidationState state,
                IProgressMonitor monitor)
    {

        super.validationStarting(project, state, monitor);
    }

    /**
     * @see org.eclipse.wst.validation.AbstractValidator#validationFinishing(org.eclipse.core.resources.IProject,
     *      org.eclipse.wst.validation.ValidationState, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void validationFinishing(IProject project,
                org.eclipse.wst.validation.ValidationState state,
                IProgressMonitor monitor)
    {

        super.validationFinishing(project, state, monitor);
    }

    /**
     * @see org.eclipse.wst.validation.AbstractValidator#validate(org.eclipse.core.resources.IResource, int,
     *      org.eclipse.wst.validation.ValidationState, org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public ValidationResult validate(IResource resource, int kind,
                org.eclipse.wst.validation.ValidationState state,
                IProgressMonitor monitor)
    {
        cleanUpWindUpMarkers(resource);

        ValidationResult result = new ValidationResult();

        Iterable<Hint> hints = WindupService.getDefault().getHints(resource, monitor);
        for (Hint hint : hints)
        {
            ValidatorMessage hintMessage = ValidatorMessage.create(hint.getHint(), resource);
            hintMessage.setAttribute(IMarker.SEVERITY, convertSeverity(hint.getSeverity()));
            hintMessage.setType(WINDUP_HINT_MARKER_ID);
            hintMessage.setAttribute(IMarker.LINE_NUMBER, hint.getLineNumber());
            hintMessage.setAttribute(IMarker.CHAR_START, hint.getColumn());
            hintMessage.setAttribute(IMarker.CHAR_END, hint.getColumn() + hint.getLength());

            result.add(hintMessage);
        }

        Iterable<Classification> classifications = WindupService.getDefault().getClassifications(resource, monitor);
        for (Classification classification : classifications)
        {
            ValidatorMessage message = ValidatorMessage.create(classification.getClassification(), resource);
            message.setAttribute(IMarker.SEVERITY, convertSeverity(classification.getSeverity()));
            message.setType(WINDUP_CLASSIFICATION_MARKER_ID);
            message.setAttribute(IMarker.LINE_NUMBER, 1);
            message.setAttribute(IMarker.CHAR_START, 0);
            message.setAttribute(IMarker.CHAR_END, 0);

            result.add(message);
        }

        return result;
    }

    private int convertSeverity(Severity severity)
    {
        switch (severity)
        {
        case INFO:
            return IMarker.SEVERITY_INFO;
        case WARNING:
            return IMarker.SEVERITY_WARNING;
        case CRITICAL:
            return IMarker.SEVERITY_ERROR;
        case SEVERE:
            return IMarker.SEVERITY_ERROR;
        default:
            return IMarker.SEVERITY_INFO;
        }
    }

    /**
     * <p>
     * Removes all of the WindUp markers from the given {@link IResource}.
     * </p>
     * 
     * @param resource to cleanup the WindUp markers from
     */
    private static void cleanUpWindUpMarkers(IResource resource)
    {
        if (resource != null)
        {
            try
            {
                resource.deleteMarkers(WINDUP_CLASSIFICATION_MARKER_ID, true, IResource.DEPTH_INFINITE);
                resource.deleteMarkers(WINDUP_HINT_MARKER_ID, true, IResource.DEPTH_INFINITE);
            }
            catch (CoreException e)
            {
                WindupCorePlugin.logError("Error cleaning up markers from: " + resource, e); //$NON-NLS-1$
            }
        }
    }
}