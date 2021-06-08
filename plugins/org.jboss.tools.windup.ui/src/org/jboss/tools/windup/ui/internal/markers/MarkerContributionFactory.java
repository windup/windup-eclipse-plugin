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
package org.jboss.tools.windup.ui.internal.markers;

import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.ExtensionContributionFactory;
import org.eclipse.ui.menus.IContributionRoot;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.services.IServiceLocator;

public class MarkerContributionFactory extends ExtensionContributionFactory
{
    @Override
    public void createContributionItems(IServiceLocator serviceLocator, IContributionRoot additions)
    {
        IWorkbenchPart activePart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
        if (activePart instanceof EditorPart)
        {
            EditorPart editor = (EditorPart) activePart;
            additions.addContributionItem(new MarkerMenuContribution(editor), null);
        }
    }
}
