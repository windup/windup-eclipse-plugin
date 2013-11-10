/*******************************************************************************
* Copyright (c) 2011 Red Hat, Inc.
* Distributed under license by Red Hat, Inc. All rights reserved.
* This program is made available under the terms of the
* Eclipse Public License v1.0 which accompanies this distribution,
* and is available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   Red Hat, Inc. - initial API and implementation
******************************************************************************/
package org.jboss.tools.windup.ui.internal.views;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.preference.BooleanPropertyAction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.IShowInTarget;
import org.eclipse.ui.part.ShowInContext;
import org.eclipse.ui.part.ViewPart;
import org.jboss.tools.windup.core.WindupService;
import org.jboss.tools.windup.ui.Preferences;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.Utils;

/**
 * <p>
 * A view to display Windup Reports.
 * </p>
 */
public class WindupReportView extends ViewPart implements IShowInTarget{

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.jboss.tools.windup.ui.views.WindupReportView"; //$NON-NLS-1$

	private Browser browser = null;
	
	private ISelectionListener selectionChangedListener;
	
	private boolean reactOnSelection = true;
	
	/**
	 * <p>
	 * The current resource who's report is being displayed.
	 * </p>
	 */
	private IResource currentSelection = null;

	@Override
	public void createPartControl(Composite parent) {
		this.browser = new Browser(parent, SWT.NONE);

		//react to selection changes
		this.selectionChangedListener = new ISelectionListener() {
			public void selectionChanged(IWorkbenchPart part, ISelection selection) {
				
				/* if update on selection and the current selection is not
				 * the same as the containing workbench part */
				if (WindupReportView.this.reactOnSelection && part != getSite().getPart()) {
					
					/* if editor selection
					 * else if some other sort of selection */
					if (part instanceof IEditorPart) {
						IEditorInput input = ((IEditorPart) part).getEditorInput();
						if (input instanceof IFileEditorInput) {
							updateSelection(new StructuredSelection(
									((IFileEditorInput) input).getFile()));
						}
					} else {
						updateSelection(selection);
					}
				}
			}
		};
		IWorkbenchPartSite site = getSite();
		ISelectionService srv = (ISelectionService) site.getService(ISelectionService.class);
		srv.addPostSelectionListener(selectionChangedListener);

		//store view preferences
		IPreferenceStore preferenceStore = getPreferenceStore();
		if (preferenceStore.contains(Preferences.REPORTVIEW_SYNC_SELECTION)) {
			this.reactOnSelection = preferenceStore.getBoolean(Preferences.REPORTVIEW_SYNC_SELECTION);
		} else {
			preferenceStore.setDefault(Preferences.REPORTVIEW_SYNC_SELECTION, true);
		}
		
		// get the views toolbar
		IActionBars actionBars = getViewSite().getActionBars();
		IToolBarManager toolbar = actionBars.getToolBarManager();
		
		// create and add link with selection action to the toolbar
		Action linkSelectionAction = new BooleanPropertyAction(
				Messages.link_with_editor_and_selection,
				this.getPreferenceStore(),
				Preferences.REPORTVIEW_SYNC_SELECTION) {
			
			@Override
			public void run() {
				super.run();
				
				WindupReportView.this.reactOnSelection = this.isChecked();
			}
		};
		linkSelectionAction.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(
						ISharedImages.IMG_ELCL_SYNCED));
		toolbar.add(linkSelectionAction);
	}

	@Override
	public void setFocus() {
		this.browser.setFocus();
	}
	
	@Override
	public void dispose() {
		super.dispose();

		//remove selection listener
		ISelectionService srv = (ISelectionService) getSite().getService(ISelectionService.class);
		srv.removePostSelectionListener(this.selectionChangedListener);
	}

	/**
	 * <p>
	 * React to a selection change.
	 * </p>
	 * 
	 * @param selection
	 *            selection change to react to
	 * 
	 * @return <code>true</code> if the view can react to the given selection,
	 *         </code>false</code> otherwise
	 */
	public synchronized boolean updateSelection(ISelection selection) {
		boolean canReact = false;
		
		IResource selectedResource = Utils.getSelectedResource(selection);
		if(selectedResource != null) {
			canReact = true;
			this.displayReport(selectedResource);
		}
		
		return canReact;
	}
	
	/**
	 * <p>
	 * Displays the Windup report for the given {@link IResource}.
	 * </p>
	 * 
	 * @param resource
	 *            {@link IResource} to display the Windup report for
	 */
	private synchronized void displayReport(IResource resource) {
		//don't change displayed report if current selection is same as newly selected resource
		if(this.currentSelection != resource) {
			this.currentSelection = resource;
		
			IPath reportPath = WindupService.getDefault().getReportLocation(resource);
			
			if(reportPath != null) {
				this.browser.setUrl(reportPath.toString());
			} else {
				//TODO: IAN: something better here
				this.browser.setText("can't find report"); //$NON-NLS-1$
			}
			
			this.browser.getBrowserType();
		}
	}
	
	/**
	 * @return the plugins {@link IPreferenceStore} instance
	 */
	private IPreferenceStore getPreferenceStore() {
		return WindupUIPlugin.getDefault().getPreferenceStore();
	}

	/**
	 * @see org.eclipse.ui.part.IShowInTarget#show(org.eclipse.ui.part.ShowInContext)
	 */
	@Override
	public boolean show(ShowInContext context) {
		return this.updateSelection(context.getSelection());
	}
}