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

import static org.jboss.tools.windup.model.domain.WindupConstants.MARKERS_CHANGED;

import javax.inject.Inject;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jboss.tools.windup.ui.internal.explorer.IssueExplorer.IssueExplorerService;
import org.jboss.tools.windup.ui.internal.issues.IssueDetailsView;
import org.jboss.tools.windup.ui.internal.services.IssueGroupService;
import org.jboss.tools.windup.ui.internal.services.MarkerService;

/**
 * Handlers used by the Issue Explorer.
 */
public class IssueExplorerHandlers {
	
	public abstract static class GroubyBy extends AbstractHandler {
		@Inject protected IssueGroupService groupService;
		@Override
		public Object execute(ExecutionEvent event) throws ExecutionException {
			update(!HandlerUtil.toggleCommandState(event.getCommand()));
			return null;
		}
		protected abstract void update(boolean enabled);
	}
	
	public static class GroupBySeverity extends GroubyBy {
		@Override
		protected void update(boolean enabled) {
			groupService.setGroupBySeverity(enabled);
		}
	}
	
	public static class GroupByRule extends GroubyBy {
		@Override
		protected void update(boolean enabled) {
			groupService.setGroupByRule(enabled);
		}
	}
	
	public static class GroupByProject extends GroubyBy {
		@Override
		protected void update(boolean enabled) {
			groupService.setGroupByProject(enabled);
		}
	}
	
	public static class GroupByPackage extends GroubyBy {
		@Override
		protected void update(boolean enabled) {
			groupService.setGroupByPackage(enabled);
		}
	}
	
	public static class GroupByFile extends GroubyBy {
		@Override
		protected void update(boolean enabled) {
			groupService.setGroupByFile(enabled);
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
			markerService.deleteWindpuMarkers();
			broker.post(MARKERS_CHANGED, true);
			return null;
		}
	}
	
	private abstract static class AbstractIssueHanlder extends AbstractHandler {
		protected IssueNode getIssueNode (ExecutionEvent event) {
			TreeSelection selection = (TreeSelection) HandlerUtil.getCurrentSelection(event);
			return (IssueNode)selection.getFirstElement();
		}
	}
	
	public static class MarkIssueFixedHandler extends AbstractIssueHanlder {
		@Override
		public Object execute(ExecutionEvent event) throws ExecutionException {
			getIssueNode(event).markAsFixed();
			return null;
		}
	}
	
	public static class ViewIssueDetailsHandler extends AbstractIssueHanlder {
		@Inject private EPartService partService;
		@Override
		public Object execute(ExecutionEvent event) throws ExecutionException {
			IssueNode node = getIssueNode(event);
			MPart part = partService.showPart(IssueDetailsView.ID, PartState.ACTIVATE);
			IssueDetailsView view = (IssueDetailsView)part.getObject();
			view.showIssueDetails(node.getType());
			return null;
		}
	}
	
	private static void applyFix(IResource resource) {
	}
	
	public static class PreviewQuickFixHandler extends AbstractIssueHanlder {
		@Override
		public Object execute(ExecutionEvent event) throws ExecutionException {
			return null;
		}
	}
	
	public static class IssueQuickFixHandler extends AbstractIssueHanlder {
		@Override
		public Object execute(ExecutionEvent event) throws ExecutionException {
			IssueNode node = getIssueNode(event);
			applyFix(node.getType().getResource());
			node.markAsFixed();
			return null;
		}
	}
	
	public static class DeleteIssueHandler extends AbstractIssueHanlder {
		@Override
		public Object execute(ExecutionEvent event) throws ExecutionException {
			getIssueNode(event).delete();
			return null;
		}
	}
}
