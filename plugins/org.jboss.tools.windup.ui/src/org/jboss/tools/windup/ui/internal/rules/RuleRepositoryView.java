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

import java.io.File;
import java.rmi.RemoteException;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.part.ViewPart;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.model.domain.WindupConstants;
import org.jboss.tools.windup.model.domain.WindupDomainListener.RulesetChange;
import org.jboss.tools.windup.runtime.WindupRmiClient;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.rules.RulesNode.CustomRulesNode;
import org.jboss.tools.windup.ui.internal.rules.RulesNode.RulesetFileNode;
import org.jboss.tools.windup.ui.internal.rules.xml.XMLRulesetModelUtil;
import org.jboss.tools.windup.windup.CustomRuleProvider;
import org.jboss.windup.tooling.ExecutionBuilder;
import org.jboss.windup.tooling.rules.RuleProviderRegistry;
import org.w3c.dom.Node;

import com.google.common.collect.Lists;
import com.google.common.io.Files;

/**
 * View for displaying Windup rule repositories.
 */
public class RuleRepositoryView extends ViewPart {
	
	public static final String VIEW_ID = "org.jboss.tools.windup.ui.rules.rulesView"; //$NON-NLS-1$
	
	private TreeViewer treeViewer;
	
	private RuleRepositoryContentProvider contentProvider;
	@Inject private WindupRmiClient windupClient;
	
	@Inject private ModelService modelService;
	@Inject private IEclipseContext context;
	
	private MenuManager menuManager;
	
	private Composite stack;
	private Composite placeholder;
	private Composite viewerComposite;
	
	@Override
	public void createPartControl(Composite parent) {
		GridLayoutFactory.fillDefaults().spacing(0, 0).applyTo(parent);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(parent);
		stack = new Composite(parent, SWT.NONE);
		stack.setLayout(new StackLayout());
		GridDataFactory.fillDefaults().grab(true, true).applyTo(stack);
		try {
			createPlaceholder(stack);
			viewerComposite = new Composite(stack, SWT.NONE);
			GridLayoutFactory.fillDefaults().spacing(0, 0).applyTo(viewerComposite);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(viewerComposite);
			createSystemRulesTreeViewer(viewerComposite);
		} catch (RemoteException e1) {
			WindupUIPlugin.log(e1);
			return;
		}
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				if (event.getSelection().isEmpty()) {
					return;
				}
				ISelection selection = event.getSelection();
				if (selection instanceof ITreeSelection) {
					Object element = ((ITreeSelection)selection).getFirstElement();
					if (element instanceof RulesetFileNode) {
						RulesetFileNode node = (RulesetFileNode)element;
						if (node.getRuleProvider() != null) {
							XMLRulesetModelUtil.openRuleInEditor(node.getRuleProvider(), null);
						}
						/*IFileStore fileStore = EFS.getLocalFileSystem().getStore(new Path(node.getFile().getParent()));
						fileStore = fileStore.getChild(node.getName());
						if (!fileStore.fetchInfo().isDirectory() && fileStore.fetchInfo().exists()) {
						    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
						    try {
						    		IDE.openEditorOnFileStore(page, fileStore);
						    } catch (PartInitException e) {
							    	WindupUIPlugin.log(e);
							    	MessageDialog.openError(
											Display.getDefault().getActiveShell(), 
											Messages.openRuleset, 
											Messages.errorOpeningRuleset);
						    }
						}*/
					}
					else if (element instanceof Node) {
						Node node = (Node)element;
						Object provider = contentProvider.getProvider(node);
						if (provider != null) {
							XMLRulesetModelUtil.openRuleInEditor(provider, node);
						}
					}
				}
			}
		});
		super.getSite().setSelectionProvider(treeViewer);
		
		menuManager = new MenuManager();
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(new IMenuListener() {
	        public void menuAboutToShow(IMenuManager manager) {
	            RuleRepositoryView.this.fillContextMenu(manager);
	        }
	    });
	    Menu menu = menuManager.createContextMenu(treeViewer.getControl());
	    treeViewer.getControl().setMenu(menu);
	    //getSite().registerContextMenu(menuManager, treeViewer);
	    
	    treeViewer.addDropSupport(DND.DROP_COPY| DND.DROP_MOVE, 
	    		new Transfer[]{LocalSelectionTransfer.getTransfer(), FileTransfer.getInstance()}, 
	    		new RulesetDropListener(treeViewer));
	}
	
	private void createPlaceholder(Composite parent) {
		placeholder = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().spacing(0, 0).applyTo(placeholder);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(placeholder);
		new Label(placeholder, SWT.NONE).setText("Loading...");
	}
	
	private void loading(boolean loading) {
		Composite top = loading ? placeholder : viewerComposite;
		((StackLayout)stack.getLayout()).topControl = top;
		placeholder.getParent().layout(true);
	}
	
	private class RulesetDropListener extends ViewerDropAdapter {

		protected RulesetDropListener(Viewer viewer) {
			super(viewer);
		}	
		
		private void addRulesets(String[] rulesetLocations, boolean isLocal) {
			WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {
				@Override
				public void execute(IProgressMonitor monitor) {
					for (String rulesetLocation : rulesetLocations) {
						IFile resource = XMLRulesetModelUtil.getRuleset(rulesetLocation);
						if (resource == null) {
							resource = XMLRulesetModelUtil.createLinkedResource(rulesetLocation);
						}
						modelService.addRulesetRepository(rulesetLocation, resource.getFullPath().toString(), true); //$NON-NLS-1$
					}
				}
			};
			try {
				new ProgressMonitorDialog(getSite().getShell()).run(false, false, operation);
			}
			catch (Exception e) {
				WindupUIPlugin.log(e);
			}
		}
		
		@Override
		public boolean performDrop(Object data) {
			Object target = getCurrentTarget();
            if (target instanceof CustomRulesNode) {
            	TransferData transferType = getCurrentEvent().currentDataType;
            	if (LocalSelectionTransfer.getTransfer().isSupportedType(transferType)) {
            		IStructuredSelection selection = (IStructuredSelection) data;
            		List<String> locations = Lists.newArrayList();
            		for (Object selected : selection.toList()) {
            			if (selected instanceof IAdaptable) {
            				IFile file = ((IAdaptable)selected).getAdapter(IFile.class);
            				if (file != null && file.getLocation() != null) {
            					locations.add(file.getLocation().toString());
            				}
            			}
            		}
            		if (!locations.isEmpty()) {
            			addRulesets(locations.stream().toArray(String[]::new), true);
            		}
            	}
            	else if (FileTransfer.getInstance().isSupportedType(transferType)) {
            		List<String> locations = Lists.newArrayList();
            		String[] fileLocations = (String[]) data; 
            		if (fileLocations != null && fileLocations.length > 0) {
            			for (String fileLocation : fileLocations) {
            				File file = new File(fileLocation);
            				if (file.exists() && !file.isDirectory()) {
            					String extension = Files.getFileExtension(fileLocation);
            					if (extension.equals(NewXMLFilePage.EXTENSION)) {
            						locations.add(fileLocation);
            					}
            				}
            			}
            			if (!locations.isEmpty()) {
	            			addRulesets(locations.stream().toArray(String[]::new), false);
	            			return true;
            			}
            		}
            	}
            }
			return false;
		}

		@Override
		public boolean validateDrop(Object target, int operation, TransferData transferType) {
			if (target instanceof CustomRulesNode) {
				switch (getCurrentLocation()) {
					case ViewerDropAdapter.LOCATION_AFTER:
						return false;
					case ViewerDropAdapter.LOCATION_ON: {
						overrideOperation(DND.DROP_COPY);
						return true;
					}
					case ViewerDropAdapter.LOCATION_BEFORE:
						return false;
					case ViewerDropAdapter.LOCATION_NONE:
						return false;
				}
			}
			return false;
		}
	}
	
	private void fillContextMenu(IMenuManager manager) {
		IStructuredSelection selection = (IStructuredSelection)treeViewer.getSelection();
		List<CustomRuleProvider> providers = Lists.newArrayList();
		for (Object selected : selection.toList()) {
			if (!(selected instanceof CustomRuleProvider)) {
				return;
			}
			providers.add((CustomRuleProvider)selected);
		}
		if (!providers.isEmpty()) {
			RemoveRulesetHandler action = ContextInjectionFactory.make(RemoveRulesetHandler.class, context);
			action.setProviders(providers);
			manager.add(action);
		}
		if (providers.size() == 1) {
			manager.add(new Separator());
			NewXMLRuleHandler action = ContextInjectionFactory.make(NewXMLRuleHandler.class, context);
			action.setProvider(providers.get(0));
			manager.add(action);
		}
	}
	
	@Inject
	@Optional
	private void rulesetAdded(@UIEventTopic(WindupConstants.CUSTOM_RULESET_CHANGED) RulesetChange change) throws RemoteException {
		if (treeViewer != null && !treeViewer.getTree().isDisposed()) {
			modelService.cleanPhantomCustomRuleProviders();
			CustomRulesNode root = (CustomRulesNode)((RuleRepositoryInput)treeViewer.getInput()).getChildren()[1];
			if (!treeViewer.isBusy()) {
				treeViewer.refresh(root);
			}
			if (!change.isDelete()) {
				treeViewer.setSelection(new StructuredSelection(change.getProviders()), true);
				treeViewer.expandToLevel(root, 1);
				change.getProviders().forEach(provider -> {
					treeViewer.expandToLevel(provider, AbstractTreeViewer.ALL_LEVELS);
					treeViewer.setSelection(new StructuredSelection(provider), true);
				});
			}
		}
	}
	
	private void createSystemRulesTreeViewer(Composite parent) throws RemoteException {
		treeViewer = new TreeViewer(parent, SWT.FULL_SELECTION|SWT.MULTI|SWT.V_SCROLL|SWT.H_SCROLL);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(treeViewer.getControl());
	    contentProvider = new RuleRepositoryContentProvider(treeViewer);
		treeViewer.setContentProvider(contentProvider);
		treeViewer.setLabelProvider(new DelegatingStyledCellLabelProvider(contentProvider));
		refresh();
	}
	
	@Inject
	@Optional
	private void updateServer(@UIEventTopic(WindupRmiClient.WINDUP_SERVER_STATUS) ExecutionBuilder executionBuilder) throws RemoteException {
		if (treeViewer != null && !treeViewer.getTree().isDisposed()) {
			refresh();
		}
	}
	
	public void refresh() throws RemoteException {
		if (windupClient.isWindupServerRunning()) {
			loading(true);
			Job fetchJob = new Job(Messages.refreshingRules) { //$NON-NLS-1$
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					RuleProviderRegistry systemRuleProviderRegistry = null;
					if (windupClient.isWindupServerRunning()) {
						try {
							systemRuleProviderRegistry = windupClient.getExecutionBuilder().getSystemRuleProviderRegistry();
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
					refreshRulesTree(systemRuleProviderRegistry);
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
	
	private void refreshRulesTree(RuleProviderRegistry systemRuleProviderRegistry) {
		Display.getDefault().asyncExec(() -> {
			RuleRepositoryInput input = RuleRepositoryInput.computeInput(systemRuleProviderRegistry, modelService);
			treeViewer.setInput(input);
			loading(false);
		});
	}

	@Override
	public void setFocus() {
		treeViewer.getTree().setFocus();
	}
}
