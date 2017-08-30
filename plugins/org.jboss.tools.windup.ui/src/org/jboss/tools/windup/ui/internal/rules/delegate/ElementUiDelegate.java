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
package org.jboss.tools.windup.ui.internal.rules.delegate;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.core.resources.IFile;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQueryAction;
import org.eclipse.wst.xml.core.internal.contentmodel.util.DOMNamespaceHelper;
import org.eclipse.wst.xml.core.internal.modelquery.ModelQueryUtil;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.ui.internal.actions.BaseNodeActionManager.MyMenuManager;
import org.eclipse.wst.xml.ui.internal.actions.MenuBuilder;
import org.eclipse.wst.xml.ui.internal.tabletree.TreeContentHelper;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.editor.AddNodeAction;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.IElementUiDelegate;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.NodeRow;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

/**
 * Represents a stack of tabs.
 */
@SuppressWarnings({"restriction"})
public abstract class ElementUiDelegate extends BaseTabStack implements IElementUiDelegate {
	
	@Inject @Optional protected FormToolkit toolkit;
	@Inject @Optional protected IEclipseContext context;
	@Inject @Optional protected Form form;
	@Inject @Optional protected Composite parent;
	@Inject @Optional protected IFile file;
	@Inject @Optional protected RulesetElementUiDelegateFactory uiDelegateFactory;
	
	protected MenuBuilder menuBuilder = new MenuBuilder();
	protected TreeContentHelper contentHelper = new TreeContentHelper();
	
	protected Element element;
	protected IStructuredModel model;
	protected ModelQuery modelQuery;
	protected CMElementDeclaration elementDeclaration;
	
	@Inject
	private void setElement(Element element) {
		this.element = element;
		this.model = ((IDOMNode) element).getModel();
		this.modelQuery = ModelQueryUtil.getModelQuery(model);
		this.elementDeclaration = modelQuery.getCMElementDeclaration(element);
	}
	
	@Override
	public void createControls(Composite parent, Element element, CMElementDeclaration ed, List<NodeRow> rows) {
	}
	
	@Override
	protected void tabItemSelected(CTabItem item) {
	}
	
	@Override
	public void update() {
		for (TabWrapper wrapper : tabs.values()) {
			IElementDetailsContainer container = (IElementDetailsContainer)wrapper.getObject();
			container.update();
		}
	}
	
	@Override
	public void setFocus() {
	}
	
	@Override
	public void fillContextMenu(IMenuManager manager, TreeViewer treeViewer) {
		IMenuManager addChildMenu = new MyMenuManager(Messages.rulesMenuNew);
		manager.add(addChildMenu);
		List<ModelQueryAction> insertActionList = Lists.newArrayList();
		modelQuery.getInsertActions(element, elementDeclaration, -1, ModelQuery.INCLUDE_CHILD_NODES,  ModelQuery.VALIDITY_STRICT, insertActionList);
		addActionHelper(model, addChildMenu, insertActionList, treeViewer);	
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
								actionList.add(createAddElementAction(model, action.getParent(), (CMElementDeclaration) action.getCMNode(), action.getStartIndex(), this, treeViewer));
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
	
	protected Control control;
	
	@Override
	public Control getControl() {
		if (folder == null) {
			super.createFolder(parent);
			createTabs();
			folder.setSelection(0);
		}
		update();
		return folder;
	}
	
	protected <T> TabWrapper addTab(Class<T> clazz) {
		CTabItem item = new CTabItem(folder, SWT.NONE);
		item.setImage(WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_MAIN_TAB));
		Composite parent = toolkit.createComposite(folder);
		GridLayoutFactory.fillDefaults().margins(0, 0).applyTo(parent);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(parent);
		
		item.setControl(parent);
		IEclipseContext child = createTabContext(parent);
		child.set(CTabItem.class, item);
		T object = create(clazz, child);
		TabWrapper wrapper = new TabWrapper(object, child, item);
		tabs.put(item, wrapper);
		return wrapper;
	}
	
	protected abstract void createTabs();
	
	@Override
	protected IEclipseContext createTabContext(Composite parent) {
		IEclipseContext context = super.createTabContext(parent);
		context.set(Element.class, element);
		context.set(IStructuredModel.class, model);
		context.set(ModelQuery.class, modelQuery);
		context.set(CMElementDeclaration.class, elementDeclaration);
		context.set(TreeContentHelper.class, contentHelper);
		context.set(IElementUiDelegate.class, this);
		return context;
	}
	
	public static Action createAddElementAction(IStructuredModel model, Node parent, CMElementDeclaration ed, int index, IElementUiDelegate delegate, TreeViewer treeViewer) {
		Action action = null;
		if (ed != null) {
			action = new AddNodeAction(model, ed, parent, index) {
				@Override
				public void run() {
					super.run();
					if (!result.isEmpty()) {
						Object element = result.get(0);
						Object[] children = null;
						if (delegate != null) {
							children = delegate.getChildren();
						}
						else if (treeViewer != null) {
							ITreeContentProvider provider = (ITreeContentProvider)treeViewer.getContentProvider();
							children = provider.getChildren(element);
						}
						if (children != null) {
							java.util.Optional<Object> optional = Arrays.stream(children).filter(e -> Objects.equal(element, e)).findFirst();
							if (optional.isPresent()) {
								treeViewer.expandToLevel(element, TreeViewer.ALL_LEVELS);
								treeViewer.setSelection(new StructuredSelection(element), true);
							}
						}
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
	
	@Override
	public List<Element> collectTreeChildren(Element parent) {
		return Lists.newArrayList();
	}
	
	@Override
	public void createTreeItemControls(Composite parent, Element element) {
	}
	
	@Override
	public void createChildTreeItemControls(Composite parent, Element element) {
	}
	
	public static Node findNode(Element parent, CMElementDeclaration ed, CMNode cmNode) {
		Node node = null;
		switch (cmNode.getNodeType()) {
			case CMNode.ATTRIBUTE_DECLARATION: {
				String attributeName = DOMNamespaceHelper.computeName(cmNode, parent, null);
				node = parent.getAttributeNode(attributeName);
				break;
			}
		}
		return node;
	}
	
	public static interface IElementDetailsContainer {
		void update();
	}
}
