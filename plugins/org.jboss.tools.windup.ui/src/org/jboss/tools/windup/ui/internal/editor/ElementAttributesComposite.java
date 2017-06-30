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
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.pde.internal.ui.editor.FormLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
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
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.ui.internal.actions.BaseNodeActionManager.MyMenuManager;
import org.eclipse.wst.xml.ui.internal.actions.MenuBuilder;
import org.eclipse.wst.xml.ui.internal.tabletree.TreeContentHelper;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.IElementUiDelegate;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.common.collect.Lists;

@SuppressWarnings({"restriction"})
public abstract class ElementAttributesComposite implements IElementUiDelegate {
	
	@Inject @Optional protected FormToolkit toolkit;
	@Inject @Optional protected Composite parent;
	@Inject @Optional protected IEclipseContext context;
	protected Element element;
	
	protected MenuBuilder menuBuilder = new MenuBuilder();
	
	protected Section section;
	protected Control control;
	
	protected IStructuredModel model;
	protected ModelQuery modelQuery;
	
	@Inject
	private void setElement(Element element) {
		this.element = element;
		this.model = ((IDOMNode) element).getModel();
		this.modelQuery = ModelQueryUtil.getModelQuery(model);
	}
	
	protected TreeContentHelper contentHelper = new TreeContentHelper();
	
	public Control getControl() {
		if (control == null) {
			createControls(createClient());
		}
		update();
		return control;
	}
	
	protected Composite createClient() {
		Composite container = toolkit.createComposite(parent);

		FormLayout formLayout = new FormLayout();
		formLayout.marginTop = 5;
		container.setLayout(formLayout);
		
		//GridLayoutFactory.fillDefaults().margins(0, 10).applyTo(container);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(container);
		
		parent = container;
		
		this.section = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR | Section.DESCRIPTION);
		section.clientVerticalSpacing = FormLayoutFactory.SECTION_HEADER_VERTICAL_SPACING;
		section.setText("Details"); //$NON-NLS-1$
		section.setDescription("Set the properties of '" + element.getNodeName() + "'. Required fields are denoted by '*'."); //$NON-NLS-1$
		
		//section.setLayout(FormLayoutFactory.createClearGridLayout(false, 1));
		formLayout = new FormLayout();
		section.setLayout(formLayout);
		FormData data = new FormData();
		data.right = new FormAttachment(100);
		data.left = new FormAttachment(0);
		data.top = new FormAttachment(0);
		section.setLayoutData(data);
		//section.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.VERTICAL_ALIGN_BEGINNING));
		
		Composite client = toolkit.createComposite(section);
		int span = computeColumns();
		GridLayout glayout = FormLayoutFactory.createSectionClientGridLayout(false, span);
		client.setLayout(glayout);
		//client.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		toolkit.paintBordersFor(client);
		section.setClient(client);
		
		this.control = container;
		return client;
	}
	
	public static Section createSection(Composite parent, FormToolkit toolkit, String title) {
		Section section = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR | Section.DESCRIPTION);
		section.clientVerticalSpacing = FormLayoutFactory.SECTION_HEADER_VERTICAL_SPACING;
		section.setText(title);
		
		section.setLayout(FormLayoutFactory.createClearGridLayout(false, 1));
		FormData data = new FormData();
		section.setLayoutData(data);
		
		Composite client = toolkit.createComposite(section);
		GridLayoutFactory.fillDefaults().applyTo(client);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(client);
		
		toolkit.paintBordersFor(client);
		section.setClient(client);
		return section;
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
	public void fillContextMenu(IMenuManager manager, TreeViewer treeViewer) {
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
		return false;
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
	
	@Override
	public Object[] getChildren() {
		return contentHelper.getChildren(element);
	}
	
	protected int getSpan() {
		return 2;
	}
}
