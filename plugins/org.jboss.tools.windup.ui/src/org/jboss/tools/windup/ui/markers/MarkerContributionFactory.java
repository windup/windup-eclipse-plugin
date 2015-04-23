package org.jboss.tools.windup.ui.markers;

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
        EditorPart editor = (EditorPart) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();

        additions.addContributionItem(new MarkerMenuContribution(editor), null);
    }
}
