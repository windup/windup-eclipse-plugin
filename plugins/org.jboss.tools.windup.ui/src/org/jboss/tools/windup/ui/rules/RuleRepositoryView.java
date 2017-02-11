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
package org.jboss.tools.windup.ui.rules;

import java.io.File;
import java.rmi.RemoteException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.jboss.tools.windup.runtime.WindupRmiClient;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.rules.SystemRulesNode.SystemRulesetFileNode;
import org.jboss.windup.tooling.ExecutionBuilder;
import org.jboss.windup.tooling.rules.RuleProvider;
import org.jboss.windup.tooling.rules.RuleProviderRegistry;

/**
 * View for displaying Windup rule repositories.
 */
public class RuleRepositoryView {
	
	public static final String VIEW_ID = "org.jboss.tools.windup.ui.rules.rulesView"; //$NON-NLS-1$
	
	private TreeViewer treeViewer;
	
	@Inject private RuleRepositoryContentProvider contentProvider;
	@Inject private WindupRmiClient windupClient;

	@PostConstruct
	private void create(Composite parent) throws RemoteException {
		GridLayoutFactory.fillDefaults().spacing(0, 0).applyTo(parent);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(parent);
		createSystemRulesTreeViewer(parent);
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				ISelection selection = event.getSelection();
				if (!selection.isEmpty() && selection instanceof ITreeSelection) {
					Object element = ((ITreeSelection)selection).getFirstElement();
					if (element instanceof SystemRulesetFileNode) {
						SystemRulesetFileNode node = (SystemRulesetFileNode)element;
						RuleProvider ruleProvider = node.getRuleProvider();
						IFileStore fileStore = EFS.getLocalFileSystem().getStore(new Path(new File(ruleProvider.getOrigin()).getParent()));
						fileStore = fileStore.getChild(node.getFileName());
						if (!fileStore.fetchInfo().isDirectory() && fileStore.fetchInfo().exists()) {
						    IWorkbenchPage page=  PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
						    try {
						        //Desktop desktop = Desktop.getDesktop();
						        //desktop.open(new File(node.getRuleProvider().getOrigin()).getParentFile());
						        IDE.openEditorOnFileStore(page, fileStore);
						    } catch (PartInitException e) {
						    	WindupUIPlugin.log(e);
						    	MessageDialog.openError(
										Display.getDefault().getActiveShell(), 
										Messages.openRuleset, 
										Messages.errorOpeningRuleset);
						    }
						}
					}
				}
			}
		});
	}
	
	private void createSystemRulesTreeViewer(Composite parent) throws RemoteException {
		treeViewer = new TreeViewer(parent, SWT.FULL_SELECTION|SWT.V_SCROLL|SWT.H_SCROLL);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(treeViewer.getControl());
		treeViewer.setContentProvider(contentProvider);
		treeViewer.setLabelProvider(new RuleRepositoryLabelProvider());
		computeRuleRepositoryInput();
	}
	
	@Inject
	@Optional
	private void updateServer(@UIEventTopic(WindupRmiClient.WINDUP_SERVER_STATUS) ExecutionBuilder executionBuilder) throws RemoteException {
		if (treeViewer != null && !treeViewer.getTree().isDisposed()) {
			computeRuleRepositoryInput();
		}
	}
	
	private void computeRuleRepositoryInput() throws RemoteException {
		if (windupClient.getExecutionBuilder() != null) {
			Job fetchJob = new Job(Messages.refreshingRules) { //$NON-NLS-1$
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					RuleProviderRegistry ruleProviderRegistry = null;
					if (windupClient.getExecutionBuilder() != null) {
						try {
							ruleProviderRegistry = windupClient.getExecutionBuilder().getSystemRuleProviderRegistry();
						} catch (RemoteException e) {
							WindupUIPlugin.log(e);
							Display.getDefault().asyncExec(() -> {
								MessageDialog.openError(
										Display.getDefault().getActiveShell(), 
										Messages.refreshingRules, 
										Messages.refreshingRulesError);
							});
						}
					}
					refreshRulesTree(ruleProviderRegistry);
					return Status.OK_STATUS;
				}
			};
			fetchJob.setSystem(true);
			fetchJob.schedule();
		}
		else {
			refreshRulesTree(null);
		}
	}
	
	private void refreshRulesTree(RuleProviderRegistry ruleProviderRegistry) {
		Display.getDefault().asyncExec(() -> {
			treeViewer.setInput(RuleRepositoryInput.computeInput(ruleProviderRegistry));
		});
	}
}
