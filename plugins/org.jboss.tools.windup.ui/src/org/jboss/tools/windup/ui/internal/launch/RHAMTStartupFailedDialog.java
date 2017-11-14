/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.

 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.launch;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.jboss.tools.windup.ui.internal.Messages;

public class RHAMTStartupFailedDialog extends Dialog {
	

    public RHAMTStartupFailedDialog(Shell parentShell) {
        super(parentShell);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        new Label(container, SWT.LEFT).setText(Messages.WindupNotStartedDebugInfo);
        
        new Label(container, SWT.LEFT).setText(Messages.WindupStartNotStartingSolution1);
        new Label(container, SWT.LEFT).setText(Messages.WindupStartNotStartingSolution2);
        new Label(container, SWT.LEFT).setText(Messages.WindupStartNotStartingSolution3);
        new Label(container, SWT.LEFT).setText(Messages.WindupStartNotStartingSolution4);

        Link link = new Link(container, SWT.NO_FOCUS);
        link.setText(Messages.WindupStartNotStartingSolution5);
        link.addSelectionListener(new SelectionAdapter() {
	        	@Override
	        	public void widgetSelected(SelectionEvent e) {
	        		RHAMTStartupFailedDialog.this.close();
	        		openWorkspaceLog();
	        	}
        });
        
        return container;
    }
    
    private void openWorkspaceLog() {
    		IPath logPath = new Path(Platform.getLogFileLocation().toFile().getAbsolutePath());
    		IFileStore fileStore = EFS.getLocalFileSystem().getStore(logPath);
    		if (!fileStore.fetchInfo().isDirectory() && fileStore.fetchInfo().exists()) {
    			IWorkbenchWindow ww = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    			IWorkbenchPage page = ww.getActivePage();
    			try {
    				IDE.openEditorOnFileStore(page, fileStore);
    			} catch (PartInitException e) { // do nothing
    			}
    		}
    }
    
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.WindupStartingError);
    }
    
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    }

    @Override
    protected Point getInitialSize() {
        return new Point(425, 260);
    }
}