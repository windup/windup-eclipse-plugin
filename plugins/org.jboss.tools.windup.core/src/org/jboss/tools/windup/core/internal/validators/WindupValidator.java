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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

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
import org.jboss.tools.windup.core.services.WindupService;
import org.jboss.tools.windup.model.domain.WindupMarker;
import org.jboss.tools.windup.runtime.WindupRuntimePlugin;
import org.jboss.windup.reporting.model.Severity;
import org.jboss.windup.tooling.data.Classification;
import org.jboss.windup.tooling.data.Hint;

/**
 * <p>
 * {@link AbstractValidator} which uses the {@link WindupService} to add {@link ValidatorMessage}s to resources based on
 * the decorations and hits found by the {@link WindupService}.
 * </p>
 */
public class WindupValidator extends AbstractValidator
{
	@Inject private WindupService windup;
	
    public WindupValidator()
    {
    }

    /**
     * @see org.eclipse.wst.validation.AbstractValidator#clean(org.eclipse.core.resources.IProject,
     *      org.eclipse.wst.validation.ValidationState, org.eclipse.core.runtime.IProgressMonitor)
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
    	if (windup == null) {
    		return new ValidationResult();
    	}
        cleanUpWindUpMarkers(resource);

        ValidationResult result = new ValidationResult();

        Iterable<Hint> hints = windup.getHints(resource);
        for (Hint hint : hints)
        {
            if (matches(hint.getFile(), resource))
            {

                String message = hint.getHint();
                if (message != null)
                    message = message.trim();

                ValidatorMessage hintMessage = ValidatorMessage.create(message, resource);
                hintMessage.setAttribute(IMarker.SEVERITY, convertSeverity(hint.getSeverity()));
                hintMessage.setType(WindupMarker.WINDUP_HINT_MARKER_ID);
                hintMessage.setAttribute(IMarker.LINE_NUMBER, hint.getLineNumber());

                try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(hint.getFile())))
                {
                    int currentLine = 1;
                    int pos = 0;
                    int currentByte = 0;
                    int lastByte = 0;

                    int startPos = -1;
                    int endPos = -1;
                    while ((currentByte = bis.read()) != -1)
                    {
                        pos++;
                        if (currentByte == '\n' && lastByte != '\r')
                        {
                            currentLine++;
                            if (startPos != -1)
                            {
                                endPos = pos;
                                break;
                            }
                        }

                        if (currentLine == hint.getLineNumber() && startPos == -1)
                            startPos = pos;

                        lastByte = currentByte;
                    }
                    if (endPos == -1)
                        endPos = pos;

                    hintMessage.setAttribute(IMarker.CHAR_START, startPos);
                    hintMessage.setAttribute(IMarker.CHAR_END, endPos);
                }
                catch (IOException e)
                {
                    WindupRuntimePlugin.logError(e.getMessage(), e);
                }
                hintMessage.setAttribute(IMarker.USER_EDITABLE, true);

                result.add(hintMessage);
            }
        }

        Set<String> seen = new HashSet<>();
        Iterable<Classification> classifications = windup.getClassifications(resource, monitor);
        for (Classification classification : classifications)
        {
            if (matches(classification.getFile(), resource))
            {
                String title = classification.getClassification();
                if (!seen.contains(title))
                {
                    seen.add(title);

                    ValidatorMessage message = ValidatorMessage.create(title, resource);
                    message.setAttribute(IMarker.SEVERITY, convertSeverity(classification.getSeverity()));
                    message.setType(WindupMarker.WINDUP_CLASSIFICATION_MARKER_ID);
                    message.setAttribute(IMarker.LINE_NUMBER, 1);
                    message.setAttribute(IMarker.CHAR_START, 0);
                    message.setAttribute(IMarker.CHAR_END, 0);

                    result.add(message);
                }
            }
        }

        return result;
    }

    private boolean matches(File file, IResource resource)
    {
        return file.getAbsolutePath().contains(resource.getFullPath().toString());
    }

    private int convertSeverity(Severity severity)
    {
        if (severity == null)
            return IMarker.SEVERITY_WARNING;

        switch (severity)
        {
        case MANDATORY:
            return IMarker.SEVERITY_ERROR;
        case OPTIONAL:
            return IMarker.SEVERITY_WARNING;
        default:
            return IMarker.SEVERITY_ERROR;
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
                resource.deleteMarkers(WindupMarker.WINDUP_CLASSIFICATION_MARKER_ID, true, IResource.DEPTH_INFINITE);
                resource.deleteMarkers(WindupMarker.WINDUP_HINT_MARKER_ID, true, IResource.DEPTH_INFINITE);
            }
            catch (CoreException e)
            {
                WindupCorePlugin.logError("Error cleaning up markers from: " + resource, e); //$NON-NLS-1$
            }
        }
    }
}