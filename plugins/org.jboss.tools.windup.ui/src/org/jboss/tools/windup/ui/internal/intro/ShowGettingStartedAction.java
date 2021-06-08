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
package org.jboss.tools.windup.ui.internal.intro;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.intro.IIntroManager;
import org.eclipse.ui.intro.IIntroPart;
import org.eclipse.ui.progress.UIJob;
import org.jboss.tools.windup.ui.WindupPerspective;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.intro.GettingStartedEditor.EditorInput;

/**
 * Action for opening the {@link GettingStartedEditor}.
 */
public class ShowGettingStartedAction implements IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;
	
	@Override
	public void run(IAction action) {
		ShowGettingStartedAction.doRun(window);
	}

	/**
	 * Closes the intro view, opens the Windup perspective, and opens the 'getting started' editor.
	 */
	public static void doRun(IWorkbenchWindow window) {
		UIJob job = new UIJob(Messages.showWindupGettingStarted) {
			public IStatus runInUIThread(IProgressMonitor monitor) {
				monitor.beginTask(Messages.showWindupGettingStarted, 1);
				try {
					try {
						IIntroManager introMgr = window.getWorkbench().getIntroManager();
						IIntroPart intro = introMgr.getIntro();
						if (intro != null) {
							introMgr.closeIntro(intro);
						}
						PlatformUI.getWorkbench().showPerspective(WindupPerspective.ID, window);
						monitor.worked(1);
						window.getActivePage().openEditor(EditorInput.INSTANCE, GettingStartedEditor.VIEW_ID);
						return Status.OK_STATUS;
					} catch (Throwable e) {
						WindupUIPlugin.log(e);
					}
				} finally {
					monitor.done();
				}
				return Status.OK_STATUS;
			}
		};
		job.schedule();
	}
	
	@Override
	public void selectionChanged(IAction action, ISelection selection) {}
	@Override
	public void dispose() {}
	@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
}
