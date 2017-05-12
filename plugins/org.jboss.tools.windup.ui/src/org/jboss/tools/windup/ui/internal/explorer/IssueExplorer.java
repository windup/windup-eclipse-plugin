/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.explorer;

import static org.jboss.tools.windup.model.domain.WindupConstants.GROUPS_CHANGED;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.JFacePreferences;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.misc.StringMatcher;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;
import org.eclipse.ui.progress.UIJob;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.model.domain.WindupConstants;
import org.jboss.tools.windup.runtime.WindupRmiClient;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.explorer.IssueExplorerContentProvider.ReportNode;
import org.jboss.tools.windup.ui.internal.explorer.IssueExplorerContentProvider.RootReportNode;
import org.jboss.tools.windup.ui.internal.explorer.IssueExplorerContentProvider.RuleGroupNode;
import org.jboss.tools.windup.ui.internal.explorer.IssueExplorerContentProvider.SeverityNode;
import org.jboss.tools.windup.ui.internal.explorer.IssueExplorerContentProvider.TreeNode;
import org.jboss.tools.windup.ui.internal.intro.ShowGettingStartedAction;
import org.jboss.tools.windup.ui.internal.services.IssueGroupService;
import org.jboss.tools.windup.ui.internal.services.MarkerService;
import org.jboss.tools.windup.ui.internal.views.WindupReportView;
import org.jboss.tools.windup.ui.util.WindupLauncher;
import org.jboss.tools.windup.ui.util.WindupServerCallbackAdapter;
import org.jboss.tools.windup.windup.Issue;
import org.jboss.windup.tooling.ExecutionBuilder;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

/**
 * Explorer view for displaying and navigating Windup issues, classifications, etc. 
 */
@SuppressWarnings("restriction")
public class IssueExplorer extends CommonNavigator {
	
	public static final String VIEW_ID = "org.jboss.tools.windup.ui.explorer"; //$NON-NLS-1$
	
	@Inject private IEclipseContext context;
	@Inject private IEventBroker broker;
	@Inject private ModelService modelService;
	@Inject private IssueGroupService groupService;
	@Inject private EPartService partService;
	private IssueExplorerContentService contentService;
	
	@Inject private WindupLauncher windupLauncher;
	@Inject private WindupRmiClient windupClient;
	@Inject private MarkerService markerService;
	
	private Text searchText;
	
	@Inject
	public IssueExplorer(IssueExplorerContentService contentService) {
		this.contentService = contentService;
		contentService.setIssuExplorer(this);
	}
	
	@Override
	protected CommonViewer createCommonViewerObject(Composite aParent) {
		// See: https://issues.jboss.org/browse/WINDUP-1290
		// Newly imported projects automatically get added to the tree via PackageExplorer#postAdd
		// The primary issue with calling super.refresh is it will cause IssueExplorer's tree nodes
		// to collapse. 
		CommonViewer viewer = new CommonViewer(getViewSite().getId(), aParent,
				SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL) {
			@Override
			public void add(Object parentElement, Object[] childElements) {
				super.add(parentElement, childElements);
				super.refresh(parentElement);
			}
		};
		viewer.setUseHashlookup(false);
		return viewer;
	}
	
	@SuppressWarnings("unchecked")
	public TreeNode computeNode(IResource resource) {
		TreeNode node = contentService.findResourceNode(resource);
		ISelection selection = getCommonViewer().getSelection();
		if (!selection.isEmpty() && selection instanceof IStructuredSelection) {
			List<Object> elements = ((IStructuredSelection)selection).toList();
			for (Object element : elements) {
				if (element instanceof TreeNode) {
					TreeNode currentNode = (TreeNode)element;
					while (currentNode != null) {
						if (currentNode.getSegment() != null && Objects.equal(currentNode.getSegment(), resource)) {
							return null;
						}
						currentNode = currentNode.getParent();
					}
				}	
			}
		}
		return node;
	}
	
	private IssueExplorerService explorerSerivce = new IssueExplorerService() {
		@Override
		public void expandAll() {
			getCommonViewer().expandAll();
		}
		public void refresh() {
			getCommonViewer().refresh();
		}
	};
	
	public void showIssue(IMarker marker) {
		MarkerNode node = contentService.findMarkerNode(marker);
		if (node != null) {
			List<Object> segments = Lists.newArrayList();
			TreeNode parent = node.getParent();
			while (parent != null) {
				if (parent.getSegment() != null) {
					segments.add(0, parent);
				}
				parent = parent.getParent();
			}
			segments.add(node);
			TreePath path = new TreePath(segments.toArray(new TreeNode[segments.size()]));
			getCommonViewer().setSelection(new StructuredSelection(path), true);
		}
	}
	
	@Override
	public void createPartControl(Composite aParent) {
		createServerArea(aParent);
		FormToolkit toolkit = new FormToolkit(aParent.getDisplay());
		
		createSearchArea(toolkit, aParent);
		super.createPartControl(aParent);
		
		GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).indent(0, 5).applyTo(getCommonViewer().getControl());
		
		getCommonViewer().addFilter(getRuleFilter());
		
		getCommonViewer().addDoubleClickListener(new OpenIssueListener());
		getCommonViewer().addDoubleClickListener(new OpenReportListener());
		getCommonViewer().addSelectionChangedListener((e) -> {
			StructuredSelection ss = (StructuredSelection)e.getSelection();
			if (ss.size() == 1) {
				Object selection = ss.getFirstElement();
				IMarker type = null;
				if (selection instanceof MarkerNode) {
					type = ((MarkerNode)selection).getMarker();
				}
				context.set(IMarker.class, type);
				if (selection instanceof ReportNode) {
					ReportNode reportNode = (ReportNode)selection;
					IMarker marker = reportNode.getMarker();
					Issue issue = markerService.find(marker);
					if (issue != null) {
						String reportLocation = issue.getGeneratedReportLocation();
						if (reportLocation != null) {
							updateReportView(reportLocation, false, partService);
						}
					}
				}
			}
		});
		getCommonViewer().setComparator(new IssueExplorerComparator());
		getServiceContext().set(IssueExplorerService.class, explorerSerivce);
		broker.subscribe(GROUPS_CHANGED, groupsChangedHandler);
		initGettingStarted();
		buildTree();
	}
	
	private Label serverImage;
	private Label textLabel;
	private Label statusLabel;
	private CButton startStopButton;
	
	@Inject
	@Optional
	private void updateServer(@UIEventTopic(WindupRmiClient.WINDUP_SERVER_STATUS) ExecutionBuilder executionBuilder) {
		if (textLabel != null && !textLabel.isDisposed()) {
			updateServerGroup();
		}
	}
	
	private void createServerArea(final Composite parent) {
		GridLayoutFactory.fillDefaults().spacing(0, 0).applyTo(parent);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(parent);
		
		Composite top = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().spacing(0, 0).numColumns(2).applyTo(top);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(top);
		
		Composite container = new Composite(top, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(4).margins(0, 5).spacing(0, 0).applyTo(container);
		GridDataFactory.fillDefaults().indent(0, 0).grab(true, false).applyTo(container);
		
		serverImage = new Label(container, SWT.NONE);
		serverImage.setImage(WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_SERVER));
		
		textLabel = new Label(container, SWT.NONE);
		textLabel.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
		textLabel.setText(Messages.WindupServerLabel); //$NON-NLS-1$
		
		statusLabel = new Label(container, SWT.NONE);
		statusLabel.setForeground(JFaceResources.getColorRegistry().get(JFacePreferences.DECORATIONS_COLOR));
		
		Composite buttonBar = new Composite(top, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(3).margins(0, 5).spacing(12, 0).applyTo(buttonBar);
		GridDataFactory.fillDefaults().indent(0, 0).grab(false, false).applyTo(buttonBar);
		
		startStopButton = new CButton(buttonBar, SWT.NONE);
		startStopButton.setHotImage(WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_START));

		updateServerGroup();
		
		final Shell shell = parent.getShell();
		startStopButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!windupClient.isWindupServerRunning()) {
					windupLauncher.shutdown(new WindupServerCallbackAdapter(shell) {
						@Override
						public void serverShutdown(IStatus status) {
							Boolean shutdown;
							if (status.isOK()) {
								shutdown = Boolean.TRUE;
							}
							else {
								shutdown = Boolean.FALSE;
								MessageDialog.openError(parent.getShell(), 
										Messages.WindupShuttingDownError, 
										status.getMessage());
							}
							if (shutdown) {
								windupLauncher.start(new WindupServerCallbackAdapter(shell) {
									@Override
									public void windupNotExecutable() {
										MessageDialog.openError(parent.getShell(), 
												Messages.WindupNotExecutableTitle, 
												Messages.WindupNotExecutableInfo);
									}
									@Override
									public void serverStart(IStatus status) {
										if (status.getSeverity() == IStatus.ERROR) {
											MessageDialog.openError(parent.getShell(), 
													Messages.WindupStartingError, 
													status.getMessage());
										}
										updateServerGroup();
									}
								});
							}
						}
					});
				}
				else {
					windupLauncher.shutdown(new WindupServerCallbackAdapter(shell) {
						@Override
						public void serverShutdown(IStatus status) {
							Display.getDefault().asyncExec(() -> {
								if (status.getSeverity() == Status.ERROR || !status.isOK()) {
									MessageDialog.openError(parent.getShell(), 
											Messages.WindupShuttingDownError, 
											status.getMessage());
								}
								updateServerGroup();
							});
						}
					});
				}
			}
		});
		
		CButton preferenceButton = new CButton(buttonBar, SWT.NONE);
		preferenceButton.setHotImage(WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_CONFIG_HOT));
		preferenceButton.setColdImage(WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_CONFIG_COLD));
		preferenceButton.setToolTipText("Configure Windup"); //$NON-NLS-1$
		preferenceButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String id = "org.jboss.tools.windup.ui.preferences.WindupPreferencePage"; //$NON-NLS-1$
				PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(parent.getShell(), 
						id, new String[]{id}, null);
				dialog.open();
			}
		});
		
		Composite separatorContainer = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().applyTo(separatorContainer);
		GridDataFactory.fillDefaults().indent(0, 3).grab(true, false).applyTo(separatorContainer);
	 }
	
	private void updateServerGroup() {
		if (windupClient.isWindupServerRunning()) {
			//serverImage
			//statusImage.setImage(WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_SERVER_RUNNING_STATUS));
			statusLabel.setText("[Running - " + windupClient.getWindupVersion() + "]"); //$NON-NLS-1$
			startStopButton.setHotImage(WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_STOP));
			startStopButton.setToolTipText("Stop Windup Server"); //$NON-NLS-1$
		}
		else {
			//serverImage
			//statusImage.setImage(WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_SERVER_NOT_RUNNING_STATUS));
			statusLabel.setText("[Not Running]"); //$NON-NLS-1$
			startStopButton.setHotImage(WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_START));	
			startStopButton.setToolTipText("Start Windup Server"); //$NON-NLS-1$
		}
		startStopButton.redraw();
		startStopButton.update();
		statusLabel.getParent().layout(true);
	}
	
	private void initGettingStarted() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (WindupUIPlugin.getDefault().getPreferenceStore()
				.getBoolean(WindupConstants.SHOW_GETTING_STARTED)) {
			if (window.getWorkbench().getIntroManager().getIntro() != null) {
				return;
			}
			Job gettingStartedJob = new UIJob(WindupConstants.INIT_GETTING_STARTED) {
				public IStatus runInUIThread(IProgressMonitor monitor) {
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							ShowGettingStartedAction action = new ShowGettingStartedAction();
							action.init(window);
							action.run(null);
							return;
						}
					});
					return Status.OK_STATUS;
				}
			};
			gettingStartedJob.setSystem(true);
			gettingStartedJob.schedule();
		}
	}
	
	private void createSearchArea(FormToolkit toolkit, Composite parent) {
		Form form = toolkit.createForm(parent);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(form);
		Composite container = form.getBody();
		GridLayoutFactory.fillDefaults().numColumns(1).margins(2, 0).applyTo(container);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(container);
		searchText = new Text(container, SWT.SEARCH | SWT.ICON_SEARCH);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, false).applyTo(searchText);
		searchText.addModifyListener(onSearch());
	}
	
	public String getFilterText() {
		return searchText.getText().trim();
	}

	private ModifyListener onSearch() {
		return e -> {
			getCommonViewer().refresh();
			getCommonViewer().expandAll();
		};
	}
	
	// Creating local copy here b/c we cannot get a reference to the one. 
	private IssueExplorerLabelProvider filterLabelProvider = new IssueExplorerLabelProvider();
	
	private ViewerFilter getRuleFilter() {
		return new ViewerFilter() {
			@Override
			public boolean select(Viewer viewer, Object parentElement,
					Object element) {
				if (element instanceof TreeNode) {
					return containsFilterNode((TreeNode)element, parentElement);
				}
				return false; 
			}
		};
	}
	
	private boolean doesParentMatchFilter(TreeNode treeNode) {
		if (isFilterMatch(treeNode)) {
			return true;
		}
		if (treeNode.getParent() != null) {
			TreeNode parentNode = treeNode.getParent();
			if ((parentNode instanceof RuleGroupNode || parentNode.getSegment() instanceof IResource ||
					parentNode instanceof SeverityNode || parentNode.getSegment() instanceof IOpenable || parentNode.getSegment() instanceof IContainer) &&
					!(parentNode.getSegment() instanceof IProject)) {
				return doesParentMatchFilter(treeNode.getParent());
			}
		}
		return false;
	}
	
	private boolean containsFilterNode(TreeNode treeNode, Object parentElement) {
		
		if (isFilterMatch(treeNode)) {
			return true;
		}
		
		if (treeNode instanceof MarkerNode) {
			if (doesParentMatchFilter(treeNode)) {
				return true;
			}
		}
		
		if (treeNode instanceof ReportNode || treeNode instanceof RootReportNode) {
			TreeNode parentNode = treeNode.getParent();
			if (parentNode.getSegment() instanceof IResource) {
				IResource resource = (IResource)parentNode.getSegment();
				TreeNode resourceNode = contentService.findResourceNode(resource);
				List<TreeNode> children = resourceNode.getChildren().stream().filter(node -> {
					return !Objects.equal(node, treeNode);
				}).collect(Collectors.toList());
				for (TreeNode childNode : children) {
					if (containsFilterNode(childNode, parentElement)) {
						return true;
					}
				}
			}
			return false;
		}
		
		for (TreeNode child : treeNode.getChildren()) {
			if (containsFilterNode(child, parentElement)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static StringMatcher getFilterMatcher(String text) {
		String pattern = text + "*";
		// Include leading wild cards.
		if (!(text.charAt(0) == '*')) {
			pattern = "*" + pattern;
		}
		return new StringMatcher(pattern, true, false);
	}
	
	private boolean isFilterMatch(TreeNode node) {
		String text = searchText.getText();
		if (text.trim().isEmpty()) {
			return true;
		}
		
		String nodeText = ((StyledString)filterLabelProvider.getStyledText(node)).getString();
		return getFilterMatcher(text).match(nodeText);
	}
	
	public void clear() {
		getCommonViewer().getTree().removeAll();
		getCommonViewer().setSelection(StructuredSelection.EMPTY);
		
		boolean visible = getCommonViewer().getTree().getItemCount() > 0;
		searchText.setVisible(visible);
	}
	
	public void buildTree()	{
		refresh();
		boolean visible = getCommonViewer().getTree().getItemCount() > 0;
		searchText.setVisible(visible);
	}
	
	public void delete(Issue issue) {
		IMarker marker = (IMarker)issue.getMarker();
		MarkerNode markerNode = contentService.findMarkerNode(marker);
		if (markerNode != null) {
			getCommonViewer().remove(markerNode);
			TreeNode parent = markerNode.getParent();
			Object segment = markerNode.getSegment();
			while (parent != null) {
				TreeNode childNode = parent.getChildPath(segment);
				getCommonViewer().remove(childNode);
				parent.removeChild(segment);
				getCommonViewer().refresh(parent, true);
				if (isEmptyParent(parent)) {
					segment = parent.getSegment();
					parent = parent.getParent();
				}
				else if (parent.getChildren().isEmpty()) {
					segment = parent.getSegment();
					parent = parent.getParent();
				}
				else {
					break;
				}
			}
		}
		boolean visible = getCommonViewer().getTree().getItemCount() > 0;
		searchText.setVisible(visible);
	}
	
	private boolean isEmptyParent(TreeNode node) {
		return (!node.getChildren().isEmpty() && node.getChildren().size() == 1 &&
			(node.getChildren().get(0) instanceof ReportNode || node.getChildren().get(0) instanceof RootReportNode));
	}
	
	public void update(Issue issue, IMarker oldMarker) {
		IMarker newMarker = (IMarker)issue.getMarker();
		MarkerNode markerNode = contentService.findMarkerNode(oldMarker);
		markerNode.setMarker(newMarker);
		contentService.updateNodeMapping(oldMarker, newMarker);
		getCommonViewer().refresh(markerNode, true);
	}
	
	private EventHandler groupsChangedHandler = new EventHandler() {
		@Override
		public void handleEvent(Event event) {
			refresh();
			// TODO: restore previously expanded nodes, selection, scrolls, etc.
			getCommonViewer().collapseAll();
		}
	};
	
	private void refresh() {
		if (getCommonViewer() != null && !getCommonViewer().getTree().isDisposed()) {
			getCommonViewer().refresh(true);
		}
		// TODO: after memento is integrated, re-set to current selection.
		context.set(IMarker.class, null);
	}

	private class OpenIssueListener implements IDoubleClickListener {
		@Override
		public void doubleClick(DoubleClickEvent event) {
			StructuredSelection ss = (StructuredSelection)event.getSelection();
			if (ss.size() == 1) {
				Object node = ss.getFirstElement();
				if (node instanceof MarkerNode) {
					MarkerNode issue = (MarkerNode)node;
                    try {
						IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(),
								issue.getMarker(), true);
					} catch (PartInitException e) {
						WindupUIPlugin.log(e);
					}
				}
			}
		}
	}
	
	private class OpenReportListener implements IDoubleClickListener {
		@Override
		public void doubleClick(DoubleClickEvent event) {
			StructuredSelection ss = (StructuredSelection)event.getSelection();
			if (ss.size() == 1) {
				Object element = ss.getFirstElement();
				if (element instanceof ReportNode) {
					ReportNode node = (ReportNode)element;
					IMarker marker = node.getMarker();
					Issue issue = markerService.find(marker);
					String reportLocation = issue.getGeneratedReportLocation();
					if (reportLocation != null) {
						updateReportView(reportLocation, true, partService);
					}
				}
				else if (element instanceof RootReportNode) {
					String reportLocation = ((RootReportNode)element).getReportLocation();
					updateReportView(reportLocation, true, partService);
				}
			}
		}
	}
	
	public static void updateReportView(String reportLocation, boolean open, EPartService partService) {
		File file = new File(reportLocation);
		MPart part = partService.findPart(WindupReportView.ID);
		WindupReportView view = (WindupReportView)part.getObject();
		if (file.exists()) {
			if (open) {
				view = (WindupReportView)partService.showPart(part, PartState.ACTIVATE).getObject();
			}
			if (view != null) {
				view.showReport(reportLocation, true);
			}
		}
	}
	
	private IEclipseContext getServiceContext() {
		return context.get(MApplication.class).getContext();
	}
	
	public static interface IssueExplorerService {
		void expandAll();
		void refresh();
	}
	
	@Override
	public void dispose() {
		super.dispose();
		broker.unsubscribe(groupsChangedHandler);
		modelService.save();
		groupService.save();
	}
}
