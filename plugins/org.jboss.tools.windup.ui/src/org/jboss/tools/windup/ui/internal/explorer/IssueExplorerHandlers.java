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

import java.lang.reflect.InvocationTargetException;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.inject.Inject;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.State;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.model.domain.WindupConstants;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.explorer.IssueExplorer.IssueExplorerService;
import org.jboss.tools.windup.ui.internal.issues.IssueDetailsView;
import org.jboss.tools.windup.ui.internal.services.IssueGroupService;
import org.jboss.tools.windup.ui.internal.services.MarkerService;
import org.jboss.tools.windup.windup.Hint;
import org.jboss.tools.windup.windup.Issue;
import org.jboss.tools.windup.windup.QuickFix;

/**
 * Handlers used by the Issue Explorer.
 */
public class IssueExplorerHandlers {
	
	public abstract static class GroupBy extends AbstractHandler {
		@Inject protected IssueGroupService groupService;
		@Override
		public Object execute(ExecutionEvent event) throws ExecutionException {
			update(!HandlerUtil.toggleCommandState(event.getCommand()));
			return null;
		}
		protected abstract void update(boolean enabled);
	}
	
	public static class GroupBySeverity extends GroupBy {
		@Override
		protected void update(boolean enabled) {
			groupService.setGroupBySeverity(enabled);
		}
	}
	
	public static class GroupByRule extends GroupBy {
		@Override
		protected void update(boolean enabled) {
			groupService.setGroupByRule(enabled);
		}
	}

	public static class GroupByProjectHierarchy extends GroupBy {
		@Inject private ICommandService commandService;
		@Override
		protected void update(boolean enabled) {
			groupService.setGroupByHierachy(enabled);
			if (enabled) {
				Command c = commandService.getCommand(IssueConstants.GROUP_BY_FILE_CMD);
				State s = c.getState(IssueConstants.TOGGLE_STATE_ID); 
				s.setValue(true);
				commandService.refreshElements(c.getId(), null);
				groupService.setGroupByFile(true, false);
			}
		}
	}
	
	public static class GroupByFile extends GroupBy {
		@Override
		protected void update(boolean enabled) {
			groupService.setGroupByFile(enabled, true);
		}
	}
	
	public static class ExpandIssuesHandler extends AbstractHandler {
		@Inject private IEclipseContext context;
		@Override
		public Object execute(ExecutionEvent event) throws ExecutionException {
			context.get(IssueExplorerService.class).expandAll();
			return null;
		}
	}
	
	public static class DeleteAllIssuesHandler extends AbstractHandler {
		@Inject private MarkerService markerService;
		@Inject private IEventBroker broker;
		@Override
		public Object execute(ExecutionEvent event) throws ExecutionException {
			markerService.deleteAllWindupMarkers();
			broker.post(WindupConstants.MARKERS_CHANGED, true);
			return null;
		}
	}
	
	private abstract static class AbstractIssueHanlder extends AbstractHandler {
		@Inject protected IEventBroker broker;
		@Inject protected MarkerService markerService;
		protected MarkerNode getMarkerNode (ExecutionEvent event) {
			TreeSelection selection = (TreeSelection) HandlerUtil.getCurrentSelection(event);
			return (MarkerNode)selection.getFirstElement();
		}
	}
	
	public static class MarkIssueFixedHandler extends AbstractIssueHanlder {
		@Override
		public Object execute(ExecutionEvent event) throws ExecutionException {
			TreeSelection selection = (TreeSelection) HandlerUtil.getCurrentSelection(event);
			for (Object selected : ((StructuredSelection)selection).toList()) {
				MarkerNode node = (MarkerNode)selected;
				Issue issue = node.getIssue();
				IMarker marker = node.getMarker();
				QuickFixUtil.setFixed(issue, marker, broker, markerService);
			}
			return null;
		}
	}
	
	public static class ViewIssueDetailsHandler extends AbstractIssueHanlder {
		@Inject private EPartService partService;
		@Override
		public Object execute(ExecutionEvent event) throws ExecutionException {
			MarkerNode node = getMarkerNode(event);
			MPart part = partService.showPart(IssueDetailsView.ID, PartState.ACTIVATE);
			IssueDetailsView view = (IssueDetailsView)part.getObject();
			view.showIssueDetails(node.getMarker());
			return null;
		}
	}
	
	public static class RefreshIssuesHandler extends AbstractIssueHanlder {
		@Inject private ModelService modelService;
		@Inject private MarkerService markerService;
		@Override
		public Object execute(ExecutionEvent event) throws ExecutionException {
			markerService.deleteAllWindupMarkers();
			markerService.updateMarkers(modelService.getRecentConfiguration());
			return null;
		}
	}
	
	public static class PreviewQuickFixHandler extends AbstractIssueHanlder {
		@Override
		public Object execute(ExecutionEvent event) throws ExecutionException {
			MarkerNode node = getMarkerNode(event);
			Hint hint = (Hint)node.getIssue();
			QuickFixUtil.previewQuickFix(hint, node.getMarker(), broker, markerService);
			return null;
		}
	}

	public static class IssueQuickFixHandler extends AbstractIssueHanlder {
		@Override
		public Object execute(ExecutionEvent event) throws ExecutionException {
			TreeSelection selection = (TreeSelection) HandlerUtil.getCurrentSelection(event);
			WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
				@Override
				protected void execute(IProgressMonitor monitor)
						throws CoreException, InvocationTargetException, InterruptedException {
					for (Object selected : ((StructuredSelection)selection).toList()) {
						MarkerNode node = (MarkerNode)selected;
						QuickFix quickFix = node.getIssue().getQuickFixes().get(0);
						IMarker marker = node.getMarker();
						QuickFixUtil.applyQuickFix(quickFix, (Hint)node.getIssue(), marker, broker, markerService);
					}
				}
			};
			try {
				new ProgressMonitorDialog(Display.getDefault().getActiveShell()).run(false, false, op);
			} catch (InvocationTargetException | InterruptedException e) {
				WindupUIPlugin.log(e);
			}
			return null;
		}
	}
	
	public static class DeleteIssueHandler extends AbstractIssueHanlder {
		@Override
		public Object execute(ExecutionEvent event) throws ExecutionException {
			TreeSelection selection = (TreeSelection) HandlerUtil.getCurrentSelection(event);
			for (Object selected : ((StructuredSelection)selection).toList()) {
				((MarkerNode)selected).delete();
				Dictionary<String, Object> props = new Hashtable<String, Object>();
				props.put(WindupConstants.EVENT_ISSUE_MARKER, selected);
				broker.send(WindupConstants.MARKER_DELETED, props);
			}
			return null;
		}
	}
}
