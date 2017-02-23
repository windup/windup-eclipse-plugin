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
package org.jboss.tools.windup.ui.internal.rules;

import java.lang.reflect.InvocationTargetException;

import javax.inject.Inject;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ISetSelectionTarget;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.rules.RulesetGenerator;

public class GenerateRulesetOperation extends WorkspaceModifyOperation {

	private Display display;
	private IContainer container;
	private String fileName;
	private String rulesetId;
	private boolean generateQuickstartTemplate;
	
	@Inject private RulesetGenerator generator; 
	
	public void init (Display display, IContainer container, String fileName, String rulesetId, 
			boolean generateQuickstartTemplate) {
		this.display = display;
		this.container = container;
		this.fileName = fileName;
		this.rulesetId = rulesetId;
		this.generateQuickstartTemplate = generateQuickstartTemplate;
	}
	
	@Override
	protected void execute(IProgressMonitor monitor)
			throws CoreException, InvocationTargetException, InterruptedException {
		monitor.beginTask(Messages.NewRulesetWizard_generatingRuleset, 3);
		if (generateQuickstartTemplate) {
			generator.generateXmlRulesetQuickstartTemplate(fileName, rulesetId, container.getLocation().toString());
		}
		else {
			generator.generateXmlRulesetStub(fileName, rulesetId, container.getLocation().toString());
		}
		monitor.worked(1);
		container.refreshLocal(IResource.DEPTH_ONE, monitor);
		monitor.worked(1);
		openFile(container.getFile(new Path(fileName)));
		monitor.worked(1);
	}
	
	private void openFile(final IFile file) {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				IWorkbenchWindow ww = WindupUIPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow();
				if (ww == null) {
					return;
				}
				IWorkbenchPage page = ww.getActivePage();
				if (page == null || !file.exists())
					return;
				IWorkbenchPart focusPart = page.getActivePart();
				if (focusPart instanceof ISetSelectionTarget) {
					ISelection selection = new StructuredSelection(file);
					((ISetSelectionTarget) focusPart).selectReveal(selection);
				}
				try {
					IDE.openEditor(page, file);
				} catch (PartInitException e) {
				}
			}
		});
	}
}
