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

import static org.jboss.tools.windup.model.domain.WindupConstants.EVENT_ISSUE;
import static org.jboss.tools.windup.model.domain.WindupConstants.GROUPS_CHANGED;
import static org.jboss.tools.windup.model.domain.WindupConstants.ISSUE_CHANGED;
import static org.jboss.tools.windup.model.domain.WindupConstants.ISSUE_DELETED;
import static org.jboss.tools.windup.model.domain.WindupConstants.MARKERS_CHANGED;

import javax.inject.Inject;

import org.eclipse.core.resources.IMarker;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.navigator.CommonNavigator;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

/**
 * Explorer view for displaying and navigating Windup issues, classifications, etc. 
 */
public class IssueExplorer extends CommonNavigator {
	
	@Inject private IEclipseContext context;
	@Inject private IEventBroker broker;
	
	private IssueExplorerService explorerSerivce = new IssueExplorerService() {
		@Override
		public void expandAll() {
			getCommonViewer().expandAll();
		}
		public void refresh() {
			getCommonViewer().refresh();
		}
	};

	@Override
	public void createPartControl(Composite aParent) {
		super.createPartControl(aParent);
		getCommonViewer().addDoubleClickListener(new OpenIssueListener());
		getCommonViewer().addSelectionChangedListener((e) -> {
			StructuredSelection ss = (StructuredSelection)e.getSelection();
			if (ss.size() == 1) {
				Object selection = ss.getFirstElement();
				IMarker type = null;
				if (selection instanceof IssueNode) {
					type = ((IssueNode)selection).getType();
				}
				context.set(IMarker.class, type);
			}
		});
		getServiceContext().set(IssueExplorerService.class, explorerSerivce);
		broker.subscribe(MARKERS_CHANGED, markersChangedHandler);
		broker.subscribe(ISSUE_CHANGED, issueChangedHandler);
		broker.subscribe(ISSUE_DELETED, issueDeletedHandler);
		broker.subscribe(GROUPS_CHANGED, groupsChangedHandler);
	}
	
	private EventHandler markersChangedHandler = new EventHandler() {
		@Override
		public void handleEvent(Event event) {
			refresh();
		}
	};
	
	private EventHandler issueChangedHandler = new EventHandler() {
		@Override
		public void handleEvent(Event event) {
			Display.getDefault().asyncExec(() -> {
				getCommonViewer().refresh(event.getProperty(EVENT_ISSUE), true);
			});
		}
	};
	
	private EventHandler issueDeletedHandler = new EventHandler() {
		@Override
		public void handleEvent(Event event) {
			getCommonViewer().remove(event.getProperty(EVENT_ISSUE));
		}
	};
	
	private EventHandler groupsChangedHandler = new EventHandler() {
		@Override
		public void handleEvent(Event event) {
			refresh();
			// TODO: restore previously expanded nodes, selection, scrolls, etc.
			getCommonViewer().expandAll();
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
				if (node instanceof IssueNode) {
					IssueNode issue = (IssueNode)node;
                    try {
						IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(),
								issue.getType(), true);
					} catch (PartInitException e) {
						WindupUIPlugin.log(e);
					}
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
		broker.unsubscribe(issueChangedHandler);
		broker.unsubscribe(issueDeletedHandler);
		broker.unsubscribe(groupsChangedHandler);
	}
}
