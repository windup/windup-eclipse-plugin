package org.jboss.tools.windup.ui.markers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.text.source.IVerticalRulerInfo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.jboss.tools.windup.core.WindupCorePlugin;
import org.jboss.tools.windup.ui.WindupUIPlugin;

public class MarkerMenuContribution extends ContributionItem
{

    private EditorPart editor;
    private IVerticalRulerInfo rulerInfo;
    private List<IMarker> markers;

    public MarkerMenuContribution(EditorPart editor)
    {
        this.editor = editor;
        this.rulerInfo = getRulerInfo();
        this.markers = getMarkers();
    }

    private IVerticalRulerInfo getRulerInfo()
    {
        return (IVerticalRulerInfo) editor.getAdapter(IVerticalRulerInfo.class);
    }

    private List<IMarker> getMarkers()
    {
        List<IMarker> clickedOnMarkers = new ArrayList<IMarker>();
        for (IMarker marker : getAllMarkers())
        {
            if (markerHasBeenClicked(marker))
            {
                clickedOnMarkers.add(marker);
            }
        }

        return clickedOnMarkers;
    }

    // Determine whether the marker has been clicked using the ruler's mouse listener
    private boolean markerHasBeenClicked(IMarker marker)
    {
        return (marker.getAttribute(IMarker.LINE_NUMBER, 0)) == (rulerInfo.getLineOfLastMouseButtonActivity() + 1);
    }

    // Get all My Markers for this source file
    private IMarker[] getAllMarkers()
    {
        try
        {
            IMarker[] classificationMarkers = ((FileEditorInput) editor.getEditorInput()).getFile()
                        .findMarkers(WindupCorePlugin.WINDUP_CLASSIFICATION_MARKER_ID, true, IResource.DEPTH_ZERO);
            IMarker[] hintMarkers = ((FileEditorInput) editor.getEditorInput()).getFile()
                        .findMarkers(WindupCorePlugin.WINDUP_HINT_MARKER_ID, true, IResource.DEPTH_ZERO);
            Set<IMarker> all = new HashSet<>(Arrays.asList(classificationMarkers));
            all.addAll(Arrays.asList(hintMarkers));
            return all.toArray(new IMarker[all.size()]);
        }
        catch (CoreException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    // Create a menu item for each marker on the line clicked on
    public void fill(Menu menu, int index)
    {
        for (final IMarker marker : markers)
        {
            MenuItem menuItem = new MenuItem(menu, SWT.PUSH, index);
            menuItem.setText("Remove Migration Task: '" + marker.getAttribute(IMarker.MESSAGE, "") + "'");
            menuItem.addSelectionListener(createDynamicSelectionListener(marker));
        }
    }

    // Action to be performed when clicking on the menu item is defined here
    private SelectionAdapter createDynamicSelectionListener(final IMarker marker)
    {
        return new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent event)
            {
                String message = marker.getAttribute(IMarker.MESSAGE, "");
                try
                {
                    marker.delete();
                }
                catch (CoreException e)
                {
                    WindupUIPlugin.logError("Failed to remove marker: " + message + " due to: " + e, e);
                }
            }
        };
    }
}
