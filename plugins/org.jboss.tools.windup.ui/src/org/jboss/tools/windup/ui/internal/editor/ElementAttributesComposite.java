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

import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.pde.internal.ui.editor.FormLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQueryAction;
import org.eclipse.wst.xml.core.internal.modelquery.ModelQueryUtil;
import org.eclipse.wst.xml.ui.internal.actions.MenuBuilder;
import org.eclipse.wst.xml.ui.internal.actions.BaseNodeActionManager.MyMenuManager;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.IElementUiDelegate;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.RulesetConstants;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.common.collect.Lists;

@SuppressWarnings({"restriction"})
public abstract class ElementAttributesComposite implements IElementUiDelegate {
	
	@Inject protected FormToolkit toolkit;
	@Inject protected Composite parent;
	@Inject protected Element element;
	@Inject protected IEclipseContext context;
	
	protected MenuBuilder menuBuilder = new MenuBuilder();
	
	protected Section section;
	protected Control control;
	
	public Control getControl() {
		if (control == null) {
			createControls(createClient());
		}
		update();
		return control;
	}
	
	private Composite createClient() {
		Composite container = toolkit.createComposite(parent);
		GridLayoutFactory.fillDefaults().margins(0, 10).applyTo(container);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(container);
		
		parent = container;
		
		this.section = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR | Section.DESCRIPTION);
		section.clientVerticalSpacing = FormLayoutFactory.SECTION_HEADER_VERTICAL_SPACING;
		section.setText("Details"); //$NON-NLS-1$
		section.setDescription("Set the properties of '" + element.getNodeName() + "'. Required fields are denoted by '*'."); //$NON-NLS-1$
		
		section.setLayout(FormLayoutFactory.createClearGridLayout(false, 1));
		section.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.VERTICAL_ALIGN_BEGINNING));
		
		Composite client = toolkit.createComposite(section);
		int span = computeColumns();
		GridLayout glayout = FormLayoutFactory.createSectionClientGridLayout(false, span);
		client.setLayout(glayout);
		client.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		toolkit.paintBordersFor(client);
		section.setClient(client);
		
		this.control = container;
		return client;
	}
	
	protected int computeColumns() {
		return 2;
	}
	
	@Override
	public void setFocus() {
		section.getClient().setFocus();
		section.getClient().forceFocus();
	}
	
	protected void createLabel(Composite parent, String text) {
		Label label = toolkit.createLabel(parent, text, SWT.NULL);
		label.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
	}
	
	protected abstract void createControls(Composite parent);
	
	public abstract void update();
	
	@Override
	public boolean isEditable() {
		return true;
	}
	
	@Override
	public void fillContextMenu(IMenuManager manager, IStructuredModel model, TreeViewer treeViewer) {
		IMenuManager addChildMenu = new MyMenuManager(Messages.rulesMenuNew);
		manager.add(addChildMenu);
		ModelQuery modelQuery = ModelQueryUtil.getModelQuery(model);
		CMElementDeclaration ed = modelQuery.getCMElementDeclaration(element);
		if (ed != null) {
			List<ModelQueryAction> insertActionList = Lists.newArrayList();
			modelQuery.getInsertActions(element, ed, -1, ModelQuery.INCLUDE_CHILD_NODES,  ModelQuery.VALIDITY_STRICT, insertActionList);
			addActionHelper(model, addChildMenu, insertActionList, treeViewer);	
		}
	}
	
	protected void addActionHelper(IStructuredModel model, IMenuManager menu, List<ModelQueryAction> modelQueryActionList, TreeViewer treeViewer) {
		List<Action> actionList = Lists.newArrayList();
		for (ModelQueryAction action : modelQueryActionList) {
			if (action.getCMNode() != null) {
				int cmNodeType = action.getCMNode().getNodeType();
				if (action.getKind() == ModelQueryAction.INSERT) {
					switch (cmNodeType) {
						case CMNode.ELEMENT_DECLARATION : {
							if (!shouldFilterElementInsertAction(action)) {
								actionList.add(ElementAttributesComposite.createAddElementAction(model, action.getParent(), (CMElementDeclaration) action.getCMNode(), action.getStartIndex(), treeViewer));
							}
							break;
						}
					}
				}
			}
		}
		menuBuilder.populateMenu(menu, actionList, false);
	}
	
	protected boolean shouldFilterElementInsertAction(ModelQueryAction action) {
		boolean filter = false;
		Element element = (Element)action.getParent();
		if (element.getTagName().equals(RulesetConstants.JAVACLASS_NAME)) {
			filter = true;
		}
		return filter;
	}
	
	public static Action createAddElementAction(IStructuredModel model, Node parent, CMElementDeclaration ed, int index, TreeViewer treeViewer) {
		Action action = null;
		if (ed != null) {
			action = new AddNodeAction(model, ed, parent, index) {
				@Override
				public void run() {
					super.run();
					if (!result.isEmpty()) {
						Object element = result.get(0);
						treeViewer.expandToLevel(element, TreeViewer.ALL_LEVELS);
						treeViewer.setSelection(new StructuredSelection(element), true);
					}
				}
			};
		}
		return action;
	}
}
