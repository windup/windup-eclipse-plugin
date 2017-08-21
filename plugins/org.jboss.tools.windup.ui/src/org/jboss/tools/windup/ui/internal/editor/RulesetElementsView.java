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
package org.jboss.tools.windup.ui.internal.editor;

import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.PageBook;
import org.jboss.tools.windup.ui.internal.services.RulesetDOMService;

public class RulesetElementsView extends AbstractElementView {
	
	private RulesetElementsTree elementsTree;
	
	private DeleteRulesetElementAction deleteElementAction;
	private RulesetElementsExpandAllAction expandAllAction;
	private RulesetElementsCollapseAllAction collapseAllAction;
	
	private RulesetDOMService domService;
	
	public void setDomService(RulesetDOMService domService) {
		this.domService = domService;
	}
	
	@Override
	protected Viewer createViewer(Composite parent) {
		elementsTree = new RulesetElementsTree(parent, new PatternFilter());
		elementsTree.createViewControl();
		addActionEnablementListeners();
		return elementsTree.getViewer();
	}
	
	private void addActionEnablementListeners() {
		((StructuredViewer) getViewer()).addPostSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				deleteElementAction.setEnabled(deleteElementAction.isEnabled());
				expandAllAction.setEnabled(expandAllAction.isEnabled());
				collapseAllAction.setEnabled(collapseAllAction.isEnabled());
			}
		});
	}
	
	protected void createActions(ToolBarManager manager) {
		manager.add(new NewRulesetElementAction(getViewer(), domService));
		manager.add(deleteElementAction = new DeleteRulesetElementAction(getViewer()));
		manager.add(new Separator());
		manager.add(expandAllAction = new RulesetElementsExpandAllAction(getViewer()));
		manager.add(collapseAllAction = new RulesetElementsCollapseAllAction(getViewer()));
		manager.update(true);
	}
	
	public void createControls(Composite parent, ToolBarManager toolBarManager) {
		createViewer(parent);
		createActions(toolBarManager);
	}
	
	public Text getFilteringTextControl() {
		return elementsTree.getFilterControl();
	}
	
	@Override
	public Viewer getViewer() {
		return elementsTree.getViewer();
	}
	
	@Override
	protected IPage createDefaultPage(PageBook book) {
		ViewerPage page = new ViewerPage();
		page.createControl(book);
		initPage(page);
		return page;
	}
	
	public void updateFilterLabel() {
	}
}
