/*******************************************************************************
 * Copyright (c) 2021 Red Hat, Inc.
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
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.PageBook;
import org.jboss.tools.windup.ui.internal.services.RulesetDOMService;
import org.w3c.dom.Element;

public class RulesetElementsView extends AbstractElementView {
	
	private RulesetElementsTree elementsTree;
	
	private DeleteRulesetElementAction deleteElementAction;
	private RulesetElementsExpandAllAction expandAllAction;
	private RulesetElementsCollapseAllAction collapseAllAction;
	
	private RulesetEditorRulesSection section;
	private RulesetDOMService domService;
	
	public void init(RulesetEditorRulesSection section, RulesetDOMService domService) {
		this.section = section;
		this.domService = domService;
	}
	
	@Override
	protected Viewer createViewer(Composite parent) {
		elementsTree = new RulesetElementsTree(parent, createPatterFilter());
		elementsTree.createViewControl();
		addActionEnablementListeners();
		getFilteringTextControl().addModifyListener(onSearch());
		return elementsTree.getViewer();
	}
	
	private ModifyListener onSearch() {
		return e -> {
			((TreeViewer)getViewer()).refresh();
			((TreeViewer)getViewer()).expandAll();
		};
	}
	
	private PatternFilter createPatterFilter() {
		return new PatternFilter() {
			@Override
			protected boolean isLeafMatch(Viewer viewer, Object element){
		        String labelText = ((ILabelProvider)((DelegatingStyledCellLabelProvider) ((StructuredViewer) viewer)
		                .getLabelProvider()).getStyledStringProvider()).getText(element);

		        if(labelText == null) {
					return false;
				}
		        return wordMatches(labelText);
		    }
			@Override
			public boolean isElementVisible(Viewer viewer, Object element) {
				boolean visible = super.isElementVisible(viewer, element);
				if (!visible && element instanceof Element) {
					visible = isVisible(viewer, (Element)element);
				}
				return visible;
			}
			
			private boolean isVisible(Viewer viewer, Element element) {
				if (isLeafMatch(viewer, element)) {
					return true;
				}
				if (element.getParentNode() != null && element.getParentNode() instanceof Element) {
					return isVisible(viewer, (Element)element.getParentNode());
				}
				return false;
			}
		};
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
		manager.add(deleteElementAction = new DeleteRulesetElementAction(section, getViewer()));
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
