/*******************************************************************************
 * Copyright (c) 2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.core;

import java.util.EventListener;

import org.eclipse.core.resources.IProject;

/**
 * <p>
 * Interface for listening to Windup report changes.
 * </p>
 */
public interface IWindupListener extends EventListener
{

    /**
     * <p>
     * Notifiers the listener when a Windup {@link GraphContext} is generated for the given {@link IProject}
     * </p>
     * 
     * @param project {@link IProject} that a Windup {@link GraphContext} was generated for
     */
    public void graphGenerated(IProject project);
}
