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

import static org.jboss.tools.windup.model.domain.WindupConstants.EVENT_ISSUE_MARKER;
import static org.jboss.tools.windup.model.domain.WindupConstants.EVENT_ISSUE_MARKER_UPDATE;
import static org.jboss.tools.windup.model.domain.WindupConstants.GROUPS_CHANGED;
import static org.jboss.tools.windup.model.domain.WindupConstants.MARKERS_CHANGED;
import static org.jboss.tools.windup.model.domain.WindupConstants.MARKER_CHANGED;
import static org.jboss.tools.windup.model.domain.WindupConstants.MARKER_DELETED;

import java.io.File;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.progress.UIJob;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.model.domain.WindupConstants;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.explorer.IssueExplorerContentProvider.ReportNode;
import org.jboss.tools.windup.ui.internal.explorer.IssueExplorerContentProvider.TreeNode;
import org.jboss.tools.windup.ui.internal.intro.ShowGettingStartedAction;
import org.jboss.tools.windup.ui.internal.services.IssueGroupService;
import org.jboss.tools.windup.ui.internal.views.WindupReportView;
import org.jboss.tools.windup.windup.Issue;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import com.google.common.collect.Lists;

/**
 * Explorer view for displaying and navigating Windup issues, classifications, etc. 
 */
public class IssueExplorer extends CommonNavigator {
	
	public static final String VIEW_ID = "org.jboss.tools.windup.ui.explorer"; //$NON-NLS-1$
	
	@Inject private IEclipseContext context;
	@Inject private IEventBroker broker;
	@Inject private ModelService modelService;
	@Inject private IssueGroupService groupService;
	@Inject private EPartService partService;
	@Inject private IssueExplorerContentService contentService;
	
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
		super.createPartControl(aParent);
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
					updateReportView((ReportNode)selection, false);
				}
			}
		});
		getCommonViewer().setComparator(new IssueExplorerComparator());
		getServiceContext().set(IssueExplorerService.class, explorerSerivce);
		broker.subscribe(MARKERS_CHANGED, markersChangedHandler);
		broker.subscribe(MARKER_DELETED, markerDeletedHandler);
		broker.subscribe(MARKER_CHANGED, markerChangedHandler);
		broker.subscribe(GROUPS_CHANGED, groupsChangedHandler);
		initGettingStarted();
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
	
	private EventHandler markersChangedHandler = new EventHandler() {
		@Override
		public void handleEvent(Event event) {
			refresh();
			// TODO: restore previously expanded nodes, selection, scrolls, etc.
			getCommonViewer().collapseAll();
		}
	};
	
	private EventHandler markerDeletedHandler = new EventHandler() {
		@Override
		public void handleEvent(Event event) {
			MarkerNode node = (MarkerNode)event.getProperty(EVENT_ISSUE_MARKER);
			TreeNode parent = node.getParent();
			Object segment = node.getSegment();
			while (parent != null) {
				parent.removeChild(segment);
				getCommonViewer().remove(node);
				getCommonViewer().refresh(parent, true);
				if (parent.getChildren().isEmpty()) {
					segment = parent.getSegment();
					parent = parent.getParent();
				}
				else {
					break;
				}
			}
		}
	};
	
	private EventHandler markerChangedHandler = new EventHandler() {
		@Override
		public void handleEvent(Event event) {
			IMarker marker = (IMarker)event.getProperty(EVENT_ISSUE_MARKER);
			IMarker updatedMarker = (IMarker)event.getProperty(EVENT_ISSUE_MARKER_UPDATE);
			Object node = findIssueNode(marker);
			contentService.updateNodeMapping(marker, updatedMarker);
			if (node != null) {
				MarkerNode markerNode = (MarkerNode)node;
				markerNode.setMarker(updatedMarker);
				Display.getDefault().syncExec(() -> {
					getCommonViewer().refresh(node, true);
				});
			}
		}
	};
	
	private EventHandler groupsChangedHandler = new EventHandler() {
		@Override
		public void handleEvent(Event event) {
			refresh();
			// TODO: restore previously expanded nodes, selection, scrolls, etc.
			getCommonViewer().collapseAll();
		}
	};
	
	private Object findIssueNode(IMarker marker) {
 		List<TreeItem> treeItems = Lists.newArrayList();
		collectTreeItems(getCommonViewer().getTree(), treeItems);
		for (TreeItem item : treeItems) {
			Object data = item.getData();
			if (data instanceof MarkerNode) {
				MarkerNode node = (MarkerNode)data;
				if (Objects.equals(marker, node.getMarker())) {
					return node;
				}
			}
		}
		return null;
	}
	
	private static void collectTreeItems(Tree tree, List<TreeItem> treeItems) {
	    for(TreeItem item : tree.getItems()) {
	    	treeItems.add(item);
	    	collectTreeItems(item, treeItems);
	    }
	}
	
	private static void collectTreeItems(TreeItem currentItem, List<TreeItem> treeItems) {
	    TreeItem[] children = currentItem.getItems();
	    for(int i = 0; i < children.length; i++) {
	        treeItems.add(children[i]);
	        collectTreeItems(children[i], treeItems);
	    }
	}
	
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
					updateReportView(node, true);
				}
			}
		}
	}
	
	private void updateReportView(ReportNode node, boolean open) {
		Issue issue = modelService.findIssue(node.getMarker());
		if (issue.getGeneratedReportLocation() != null) {
			File file = new File(issue.getGeneratedReportLocation());
			MPart part = partService.findPart(WindupReportView.ID);
			WindupReportView view = (WindupReportView)part.getObject();
			if (file.exists()) {
				if (open) {
					view = (WindupReportView)partService.showPart(part, PartState.ACTIVATE).getObject();
				}
				if (view != null) {
					view.showReport(issue.getGeneratedReportLocation(), true);
				}
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
		broker.unsubscribe(markersChangedHandler);
		broker.unsubscribe(markerDeletedHandler);
		broker.unsubscribe(markerChangedHandler);
		broker.unsubscribe(groupsChangedHandler);
		modelService.save();
		groupService.save();
	}
}
