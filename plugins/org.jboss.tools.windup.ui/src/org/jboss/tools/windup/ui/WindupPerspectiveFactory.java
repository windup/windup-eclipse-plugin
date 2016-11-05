package org.jboss.tools.windup.ui;

import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.ui.progress.IProgressConstants;
import org.jboss.tools.windup.ui.internal.explorer.IssueExplorer;
import org.jboss.tools.windup.ui.internal.issues.IssueDetailsView;
import org.jboss.tools.windup.ui.internal.views.WindupReportView;

/**
 * The Windup perspective.
 */
public class WindupPerspectiveFactory implements IPerspectiveFactory {
	
	private static final String ID_CONSOLE_VIEW = "org.eclipse.ui.console.ConsoleView"; //$NON-NLS-1$
	private static final String ID_SEARCH_VIEW = "org.eclipse.search.ui.views.SearchView"; //$NON-NLS-1$

	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.addActionSet(IDebugUIConstants.LAUNCH_ACTION_SET);
		layout.addShowViewShortcut(IssueExplorer.VIEW_ID);
		layout.addShowViewShortcut(IssueDetailsView.ID);
		layout.addShowViewShortcut(WindupReportView.ID);
		
		String editorArea = layout.getEditorArea();

		// Top left.
		IFolderLayout topLeft = layout.createFolder("topLeft", IPageLayout.LEFT, 0.25f, editorArea);//$NON-NLS-1$
		topLeft.addView(IssueExplorer.VIEW_ID);
		
		topLeft.addPlaceholder(ProjectExplorer.VIEW_ID);
		topLeft.addPlaceholder(JavaUI.ID_TYPE_HIERARCHY);
		topLeft.addPlaceholder(JavaUI.ID_PACKAGES_VIEW);
		
		// Bottom right.
		IFolderLayout bottomRight = layout.createFolder("bottomRight", IPageLayout.BOTTOM, 0.7f, editorArea);//$NON-NLS-1$
		bottomRight.addView(IssueDetailsView.ID);
		bottomRight.addView(WindupReportView.ID);
		bottomRight.addPlaceholder(ID_CONSOLE_VIEW);
		
		bottomRight.addPlaceholder(IPageLayout.ID_TASK_LIST);
		bottomRight.addPlaceholder(ID_CONSOLE_VIEW);
		bottomRight.addPlaceholder(IPageLayout.ID_BOOKMARKS);
		bottomRight.addPlaceholder(IProgressConstants.PROGRESS_VIEW_ID);
		bottomRight.addPlaceholder(ID_SEARCH_VIEW);
	}
}
