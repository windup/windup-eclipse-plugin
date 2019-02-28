/*******************************************************************************
 * Copyright (c) 2019 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui;

import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.mylyn.tasks.ui.ITasksUiConstants;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.progress.IProgressConstants;
import org.jboss.tools.windup.ui.internal.explorer.IssueExplorer;
import org.jboss.tools.windup.ui.internal.issues.IssueDetailsView;
import org.jboss.tools.windup.ui.internal.rules.RuleRepositoryView;
import org.jboss.tools.windup.ui.internal.views.SubmitRulesetInfoView;

/**
 * The Windup perspective.
 */
public class WindupPerspectiveFactory implements IPerspectiveFactory {
	
	public static final String ID = "org.jboss.tools.windup.ui.perspective";
	
	private static final String ID_CONSOLE_VIEW = "org.eclipse.ui.console.ConsoleView"; //$NON-NLS-1$
	private static final String ID_SEARCH_VIEW = "org.eclipse.search.ui.views.SearchView"; //$NON-NLS-1$
	
	private static final String TASK_LIST_VIEW_ID = "org.eclipse.mylyn.tasks.ui.views.tasks";

	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.addActionSet(IDebugUIConstants.LAUNCH_ACTION_SET);
		layout.addShowViewShortcut(IssueExplorer.VIEW_ID);
		layout.addShowViewShortcut(RuleRepositoryView.VIEW_ID);
		layout.addShowViewShortcut(IssueDetailsView.ID);
		//layout.addShowViewShortcut(WindupReportView.ID);
		layout.addShowViewShortcut(TASK_LIST_VIEW_ID);
		layout.addShowViewShortcut(ITasksUiConstants.ID_VIEW_REPOSITORIES);
		layout.addShowViewShortcut(SubmitRulesetInfoView.ID);
		
		String editorArea = layout.getEditorArea();

		// Top left.
		IFolderLayout topLeft = layout.createFolder("topLeft", IPageLayout.LEFT, 0.26f, editorArea);//$NON-NLS-1$
		topLeft.addView(IssueExplorer.VIEW_ID);
		topLeft.addPlaceholder(JavaUI.ID_TYPE_HIERARCHY);
		topLeft.addPlaceholder(JavaUI.ID_PACKAGES_VIEW);
		
		// Bottom right.
		IFolderLayout bottomRight = layout.createFolder("bottomRight", IPageLayout.BOTTOM, 0.7f, editorArea);//$NON-NLS-1$
		bottomRight.addView(IssueDetailsView.ID);
		//bottomRight.addView(WindupReportView.ID);
		bottomRight.addView(SubmitRulesetInfoView.ID);
		bottomRight.addPlaceholder(ID_CONSOLE_VIEW);
		
		bottomRight.addPlaceholder(IPageLayout.ID_TASK_LIST);
		bottomRight.addPlaceholder(ID_CONSOLE_VIEW);
		bottomRight.addPlaceholder(IPageLayout.ID_BOOKMARKS);
		bottomRight.addPlaceholder(IProgressConstants.PROGRESS_VIEW_ID);
		bottomRight.addPlaceholder(ID_SEARCH_VIEW);
		bottomRight.addView(RuleRepositoryView.VIEW_ID);
		bottomRight.addView(ITasksUiConstants.ID_VIEW_REPOSITORIES);
		
		// Bottom far-right.
		//IFolderLayout bottomFarRight = layout.createFolder("bottomFarRight", IPageLayout.RIGHT, 0.5f, "bottomRight");//$NON-NLS-1$
		//bottomFarRight.addView(WindupServerView.ID);
	}
}
