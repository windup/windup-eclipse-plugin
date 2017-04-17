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
package org.jboss.tools.windup.ui.internal.views;

import static org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME;
import static org.jboss.tools.windup.model.domain.WindupConstants.DEFAULT;
import static org.jboss.tools.windup.model.domain.WorkspaceResourceUtils.findProject;

import java.io.File;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.workbench.modeling.ISelectionListener;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.part.IShowInTarget;
import org.eclipse.ui.part.ShowInContext;
import org.jboss.tools.windup.core.IWindupListener;
import org.jboss.tools.windup.core.services.WindupService;
import org.jboss.tools.windup.ui.Preferences;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.Utils;

/**
 * <p>
 * A view to display Windup Reports.
 * </p>
 */
public class WindupReportView implements IShowInTarget
{
	@Inject private MPart part;
	@Inject private WindupService windupService;
	@Inject private ESelectionService selectionService;
	
    /**
     * The ID of the view as specified by the extension.
     */
    public static final String ID = "org.jboss.tools.windup.ui.views.WindupReportView"; //$NON-NLS-1$
    
    private static final String PREVIOUS_REPORT_PREF = "previousReport"; //$NON-NLS-1$

    /**
     * <p>
     * The parent {@link Composite} for this view.
     * </p>
     */
    private Composite composite;

    /**
     * <p>
     * Widget used to display the HTML Windup report.
     * </p>
     */
    private Browser browser;

    /**
     * <p>
     * Used to display messages to the user in the view.
     * </p>
     */
    private StyledText message;

    /**
     * <p>
     * Listens for selection changes so that the view can be synchronized with the current selection if so specified by the user.
     * </p>
     * 
     * @see #syncronizeViewWithCurrentSelection
     */
    private ISelectionListener selectionChangedListener;

    /**
     * <p>
     * Listener used to listen to changes in Windup reports, such as a new one being generated.
     * </p>
     */
    private IWindupListener reportListener;

    /**
     * <p>
     * <code>true</code> if the view should sync with the current workbench selection, <code>false</code> otherwise.
     * </p>
     */
    private boolean syncronizeViewWithCurrentSelection;

    /**
     * <p>
     * The current resource for which a Windup report is currently being displayed.
     * </p>
     */
    private IResource currentSelection;
    
    private String currentReportPath;

    /**
     * <p>
     * Required default constructor for this view since it is created via extension point.
     * </p>
     */
    public WindupReportView()
    {
        this.browser = null;
        this.selectionChangedListener = null;
        this.currentSelection = null;
        this.syncronizeViewWithCurrentSelection = false;
        this.currentReportPath = "";
    }

    @Inject
    public void createPartControl(Composite composite)
    {
    	this.composite = composite;
        //this.composite = new Composite(parent, SWT.NONE);
//        GridLayout layout = new GridLayout(1, false);
//        this.composite.setLayout(layout);
        GridLayoutFactory.fillDefaults().margins(0, 0).applyTo(composite);;
        GridDataFactory.fillDefaults().grab(true, true).applyTo(composite);

        // create the message label
        this.message = new StyledText(this.composite, SWT.WRAP);
        this.message.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        this.message.setVisible(false);

        this.browser = new Browser(this.composite, SWT.NONE);
        this.browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        // react to selection changes
        this.selectionChangedListener = new ISelectionListener() {
			@Override
			public void selectionChanged(MPart part, Object selection) {
				/*
                 * if update on selection and the current selection is not the same as the containing workbench part
                 */
                if (WindupReportView.this.syncronizeViewWithCurrentSelection && part != WindupReportView.this.part)
                {
                    /*
                     * if editor selection else if some other sort of selection
                     */
                    if (part instanceof IEditorPart)
                    {
                        IEditorInput input = ((IEditorPart) part).getEditorInput();
                        if (input instanceof IFileEditorInput)
                        {
                            updateSelection(new StructuredSelection(
                                        ((IFileEditorInput) input).getFile()));
                        }
                    }
                    else
                    {
                    	if (!(selection instanceof ISelection)) {
                    		selection = new StructuredSelection(selection);
                    	}
                    	else {
                    		updateSelection((ISelection)selection);
                    	}
                    }
                }			
			}
		};
		selectionService.addPostSelectionListener(selectionChangedListener);
		
        // react to Windup report generations
        this.reportListener = new IWindupListener()
        {
            @Override
            public void graphGenerated(IProject project)
            {
                /*
                 * if the current selection is in the project that just had a report generated, refresh the view
                 */
                if (WindupReportView.this.currentSelection != null
                            && WindupReportView.this.currentSelection.getProject().equals(project))
                {

                    // refresh has to take place in the display thread
                    Display.getDefault().syncExec(new Runnable()
                    {
                        public void run()
                        {
                            WindupReportView.this.refresh();
                        }
                    });
                }
            }
        };
        windupService.addWindupListener(reportListener);

        // store view preferences
        IPreferenceStore preferenceStore = getPreferenceStore();
        if (preferenceStore.contains(Preferences.REPORTVIEW_SYNC_SELECTION))
        {
            this.syncronizeViewWithCurrentSelection = preferenceStore.getBoolean(Preferences.REPORTVIEW_SYNC_SELECTION);
        }
        else
        {
            preferenceStore.setDefault(Preferences.REPORTVIEW_SYNC_SELECTION, this.syncronizeViewWithCurrentSelection);
        }
        String previousReport = preferenceStore.getString(PREVIOUS_REPORT_PREF);
        if (!previousReport.isEmpty()) {
        	File file = new File(previousReport);
    		if (file.exists()) {
    			showReport(previousReport, true);
    		}
        }
    }
    
    public void setSynchronizeSelection(boolean sync) {
    	this.syncronizeViewWithCurrentSelection = sync;
    }

    @Focus
    public void setFocus()
    {
        this.browser.setFocus();
    }

    @PreDestroy
    public void dispose()
    {
        // remove listeners
        //ISelectionService srv = (ISelectionService) site.getService(ISelectionService.class);
    	selectionService.removePostSelectionListener(this.selectionChangedListener);
        windupService.removeWindupListener(this.reportListener);
        getPreferenceStore().setValue(PREVIOUS_REPORT_PREF, currentReportPath);
    }
    
    public void updateConfiguration(ILaunchConfiguration configuration) {
		try {
			String projectName = configuration.getAttribute(ATTR_PROJECT_NAME, DEFAULT);
			IProject project = findProject(projectName);
	    	displayReport(project, false);
		} catch (CoreException e) {
			WindupUIPlugin.log(e);
		}
    }

    /**
     * <p>
     * React to a selection change.
     * </p>
     * 
     * @param selection selection change to react to
     * 
     * @return <code>true</code> if the view can react to the given selection, </code>false</code> otherwise
     */
    public boolean updateSelection(ISelection selection)
    {
        boolean canReact = false;

        IResource selectedResource = Utils.getSelectedResource(selection);
        if (selectedResource != null)
        {
            canReact = true;
            this.displayReport(selectedResource, false);
        }

        return canReact;
    }

    /**
     * @see org.eclipse.ui.part.IShowInTarget#show(org.eclipse.ui.part.ShowInContext)
     */
    @Override
    public boolean show(ShowInContext context)
    {
        return this.updateSelection(context.getSelection());
    }

    /**
     * <p>
     * Forces the view to update the report.
     * </p>
     */
    public void refresh()
    {
    	if (currentSelection != null) {
    		this.displayReport(this.currentSelection, true);
    	}
    }

    /**
     * <p>
     * Displays the Windup report for the given {@link IResource}.
     * </p>
     * 
     * @param resource {@link IResource} to display the Windup report for
     * @param force if <code>true</code> then refresh the view even if the the view is already displaying the report for the given resource, if
     *            <code>false</code> and the view is already displaying the report for the given resource then do nothing
     */
    private synchronized void displayReport(IResource resource, boolean force)
    {
        // don't change displayed report if current selection is same as newly selected resource
        if (this.currentSelection != resource || force)
        {
            this.currentSelection = resource;

            if (windupService.reportExists(resource))
            {
                IPath reportPath = windupService.getReportLocation(resource);

                if (reportPath != null)
                {
                    this.showReport(reportPath, true);
                }
                else
                {
                    this.showMessage(Messages.report_has_no_information_on_resource, true);
                }
            }
            else
            {
                this.showMessage(Messages.windup_report_has_not_been_generated, true);
            }
        }
    }

    /**
     * <p>
     * Displays the report in the view and optionally hides the current message.
     * </p>
     * 
     * @param reportPath {@link IPath} to the report to show
     * @param hideMessage <code>true</code> to hide the current message, <code>false</code> to show the message with the report
     */
    public void showReport(IPath reportPath, boolean hideMessage) {
        showReport(reportPath.toString(), hideMessage);
    }
    
    public void showReport(String path, boolean hideMessage) {
    	this.currentReportPath = path;
        this.browser.setUrl(path);
        this.browser.setVisible(true);
        ((GridData) this.browser.getLayoutData()).exclude = false;

        this.message.setVisible(!hideMessage);
        ((GridData) this.message.getLayoutData()).exclude = hideMessage;

        this.composite.layout(true);
    }

    /**
     * <p>
     * Displays the given message in the view and optionally hides the report.
     * </p>
     * 
     * @param message text message to display to user in the view
     * @param hideReport <code>true</code> to hide the report, <code>false</code> to show the message with the report
     */
    public void showMessage(String message, boolean hideReport)
    {
        this.message.setText(message);
        this.message.setVisible(true);
        ((GridData) this.message.getLayoutData()).exclude = false;

        this.browser.setVisible(!hideReport);
        if (hideReport) {
        	currentReportPath = "";
        }
        
        ((GridData) this.browser.getLayoutData()).exclude = hideReport;

        this.composite.layout(true);
    }

    /**
     * @return the plugins {@link IPreferenceStore} instance
     */
    private IPreferenceStore getPreferenceStore()
    {
        return WindupUIPlugin.getDefault().getPreferenceStore();
    }
}