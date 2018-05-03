/*******************************************************************************
 * Copyright (c) 2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.editor;

import static org.jboss.tools.windup.model.domain.WindupConstants.CONFIG_CREATED;
import static org.jboss.tools.windup.model.domain.WindupConstants.CONFIG_DELETED;

import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ContributionManager;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.pde.internal.ui.PDEPluginImages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.menus.IMenuService;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.IElementUiDelegate;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.RulesetConstants;
import org.jboss.tools.windup.ui.internal.rules.xml.XMLRulesetModelUtil;
import org.jboss.tools.windup.ui.internal.services.RulesetDOMService;
import org.jboss.tools.windup.windup.ConfigurationElement;
import org.jboss.tools.windup.windup.CustomRuleProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.common.collect.Lists;

/**
 * The composite containing Windup's configurations.
 */
@SuppressWarnings("restriction")
public class RulesetEditorRulesSection {
	
	private static final String TOOLBAR_ID = "toolbar:org.jboss.tools.windup.toolbar.configurations.table"; //$NON-NLS-1$
	
	private static final String SELECTED_CONFIGURATION = "selectedConfiguration";
 
	@Inject private IEventBroker broker;
	@Inject private IMenuService menuService;
	@Inject private ModelService modelService;
	@Inject private FormToolkit toolkit;
	@Inject private IEclipsePreferences preferences;
	
	@Inject private RulesetDOMService domService;

	@Inject private RulesetElementUiDelegateRegistry elementUiDelegateRegistry;
	
	@Inject RulesSectionContentProvider provider;
	
	private ToolBarManager toolBarManager;
	private TreeViewer treeViewer;
	
	private Button removeButton;
	private Button upButton;
	private Button downButton;
	
	private RulesetElementsView elementsView;
	
	public ISelectionProvider getSelectionProvider() {
		return treeViewer;
	}
	
	public TreeViewer getTableViewer() {
		return treeViewer;
	}
	
	public void selectAndReveal(Element element) {
		treeViewer.expandToLevel(element, TreeViewer.ALL_LEVELS);
		treeViewer.setSelection(new StructuredSelection(element), true);
	}
	
	public void setDocument(Document document) {
		if (document == treeViewer.getInput()) {
			treeViewer.refresh(document, true);
		}
		else {
			treeViewer.setInput(document);
		}
	}
	
	public void init(CustomRuleProvider ruleProvider) {
		List<Node> ruleNodes = XMLRulesetModelUtil.getRules(ruleProvider.getLocationURI());
		treeViewer.setInput(ruleNodes.toArray(new Node[ruleNodes.size()]));
	}
	
	@PostConstruct
	private void createRulesetElementArea(Composite parent) {
        Composite comp = new Composite(parent, SWT.FLAT);
        GridLayout gridLayout = new GridLayout(1, false);
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        comp.setLayout(gridLayout);
        comp.setLayoutData(new GridData(GridData.FILL_BOTH));

        ViewForm viewForm = new ViewForm(comp, SWT.FLAT | SWT.BORDER);
        ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT);
        ToolBar toolBar = toolBarManager.createControl(viewForm);
        toolBar.setBackground(parent.getBackground());
        viewForm.setTopLeft(toolBar);
        viewForm.setLayoutData(new GridData(GridData.FILL_BOTH));

        Composite viewFormContents = new Composite(viewForm, SWT.FLAT);
        gridLayout = new GridLayout();
        gridLayout.marginHeight = 5;
        gridLayout.marginWidth = 5;
        viewFormContents.setLayout(gridLayout);
        viewFormContents.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        elementsView = new RulesetElementsView();
        elementsView.init(this, domService);
        elementsView.createControls(viewFormContents, toolBarManager);
		Text filterText = elementsView.getFilteringTextControl();
		if (filterText != null){
			filterText.setFocus();
		}

		this.treeViewer = (TreeViewer)elementsView.getViewer();
		
		DelegatingStyledCellLabelProvider styleProvider = new DelegatingStyledCellLabelProvider(provider);
		provider.setFilterText(elementsView.getFilteringTextControl());
		treeViewer.setContentProvider(provider);
		treeViewer.setLabelProvider(styleProvider);
		createViewerContextMenu();

		Control control = treeViewer.getControl();
		GridData gd = new GridData(GridData.FILL_BOTH);
		control.setLayoutData(gd);
        viewForm.setContent(viewFormContents);
	}
	
	//@PostConstruct
	private void create(Composite parent) {
		//parent.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		
		Composite container = toolkit.createComposite(parent);
		GridLayoutFactory.fillDefaults().margins(0, 0).applyTo(container);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(container);
		
//		Section section = toolkit.createSection(container, Section.TITLE_BAR);
//		section.setText(rulesSectionTitle);
//		GridDataFactory.fillDefaults().grab(true, true).applyTo(section);

		createToolbar(container);
		
		Composite client = toolkit.createComposite(/*section*/container);
		toolkit.paintBordersFor(client);
		GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).applyTo(client);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(client);
		//section.setClient(client);
		
		createControls(client);
		
		focus();
	}
	
	private void createControls(Composite parent/*, Section section*/) {
		//createToolbar(section);
		this.treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		treeViewer.addSelectionChangedListener((e) -> {
			Element selectedElement = (Element)((StructuredSelection)e.getSelection()).getFirstElement();
			update();
			boolean enabled = selectedElement != null;
			removeButton.setEnabled(enabled);
			if (enabled) {
				enabled = RulesetConstants.RULE_NAME.equals(selectedElement.getNodeName()) ? true : false;
			}
			upButton.setEnabled(enabled);
			downButton.setEnabled(enabled);
		});
		
		GridDataFactory.fillDefaults().grab(true, true).applyTo(treeViewer.getTree());
		
		treeViewer.setContentProvider(provider);
		treeViewer.setLabelProvider(provider);
		createViewerContextMenu();
		
		createButtons(parent);
	}
	
	private void createButtons(Composite parent) {
		Composite container = toolkit.createComposite(parent);
		toolkit.paintBordersFor(container);
		GridLayoutFactory.fillDefaults().applyTo(container);
		GridDataFactory.fillDefaults().grab(false, true).applyTo(container);
		
		Button addButton = createButton(container, Messages.RulesetEditor_AddRule);
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Document document = (Document)treeViewer.getInput();
				Element rulesetElement = domService.findOrCreateRulesetElement(document);
				Element rulesElement = domService.findOrCreateRulesElement(rulesetElement);
				//createRule(rulesElement);
			}
		});
		this.removeButton = createButton(container, Messages.RulesetEditor_remove);
		removeButton.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				removeNodes(((IStructuredSelection)treeViewer.getSelection()).toList());
			}
		});
		
		createPlaceholder(container);
		createPlaceholder(container);
		createPlaceholder(container);
		
		this.upButton = createButton(container, Messages.RulesetEditor_Rules_up);
		this.downButton = createButton(container, Messages.RulesetEditor_Rules_down);
		
		upButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				StructuredSelection ss = (StructuredSelection)treeViewer.getSelection();
				if (!ss.isEmpty() && ss.size() == 1) {
					Node node = (Node)ss.getFirstElement();
					if (node instanceof Element && node.getParentNode() instanceof Element) {
						ISelection selection = treeViewer.getSelection();
						domService.insertBeforePreviousSibling(node);
						treeViewer.setSelection(selection);
					}
				}
			}
		});
		
		downButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				StructuredSelection ss = (StructuredSelection)treeViewer.getSelection();
				if (!ss.isEmpty() && ss.size() == 1) {
					Node node = (Node)ss.getFirstElement();
					if (node instanceof Element && node.getParentNode() instanceof Element) {
						ISelection selection = treeViewer.getSelection();
						domService.insertAfterNextSibling(node);
						treeViewer.setSelection(selection);
					}
				}
			}
		});
	}
	
	private void createPlaceholder(Composite parent) {
		Label label = toolkit.createLabel(parent, null);
		GridData gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd.horizontalSpan = 1;
		gd.widthHint = 0;
		gd.heightHint = 0;
		label.setLayoutData(gd);
	}
	
	private Button createButton(Composite parent, String label) {
		Button button = toolkit.createButton(parent, label, SWT.PUSH);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		button.setLayoutData(gd);
		button.setFont(JFaceResources.getDialogFont());
		PixelConverter converter = new PixelConverter(button);
		int widthHint = converter.convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
		gd.widthHint = Math.max(widthHint, button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		return button;
	}
	
	private void createToolbar(Composite parent) {
		this.toolBarManager = new ToolBarManager(SWT.FLAT);
		ToolBar toolBar = this.toolBarManager.createControl(parent);
//		section.setTextClient(toolBar);
		menuService.populateContributionManager((ContributionManager)toolBarManager, TOOLBAR_ID);
	}
	
	@Inject
	@Optional
	private void configCreated(@UIEventTopic(CONFIG_CREATED) ConfigurationElement configuration) {
		treeViewer.setSelection(new StructuredSelection(configuration), true);
	}
	
	@Inject
	@Optional
	private void configDeleted(@UIEventTopic(CONFIG_DELETED) ConfigurationElement configuration) {
	}
	
	private void update() {
		broker.post(UIEvents.REQUEST_ENABLEMENT_UPDATE_TOPIC, UIEvents.ALL_ELEMENT_ID);
	}
	
	private void focus() {
		String previouslySelected = preferences.get(SELECTED_CONFIGURATION, null);
		if (previouslySelected != null) {
			ConfigurationElement configuration = modelService.findConfiguration(previouslySelected);
			if (configuration != null) {
				treeViewer.setSelection(new StructuredSelection(configuration), true);
			}
		}
		treeViewer.getTree().setFocus();
	}
	
	private void createViewerContextMenu() {
		MenuManager popupMenuManager = new MenuManager();
		IMenuListener listener = new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager mng) {
				fillContextMenu(mng);
			}
		};
		popupMenuManager.addMenuListener(listener);
		popupMenuManager.setRemoveAllWhenShown(true);
		Control control = treeViewer.getControl();
		Menu menu = popupMenuManager.createContextMenu(control);
		control.setMenu(menu);
	}
	
	@SuppressWarnings("unchecked")
	private void fillContextMenu(IMenuManager manager) {
		ISelection selection = treeViewer.getSelection();
		IStructuredSelection ssel = (IStructuredSelection) selection;
		if (!ssel.isEmpty()) {
			if (ssel.toList().size() == 1) {
				Element element = (Element)ssel.getFirstElement();
				IElementUiDelegate delegate = elementUiDelegateRegistry.getUiDelegate(element);
				if (delegate != null) {
					delegate.fillContextMenu(manager, treeViewer);
				}
			}
			Action deleteAction = new Action() {
				@Override
				public ImageDescriptor getImageDescriptor() {
					return PDEPluginImages.DESC_DELETE;
				}

				@Override
				public ImageDescriptor getDisabledImageDescriptor() {
					return PDEPluginImages.DESC_REMOVE_ATT_DISABLED;
				}

				@Override
				public void run() {
					removeNodes(ssel.toList());
				}
			};
			deleteAction.setText(Messages.RulesetEditor_RemoveElement);
			manager.add(deleteAction);
		}
		this.treeViewer.getControl().update();
	}
	
	public void removeNodes(List<Element> elements) {
		if (elements.isEmpty()) {
			return;
		}
		IStructuredModel model = ((IDOMDocument)treeViewer.getInput()).getModel();
		try {
			model.aboutToChangeModel();

			filterChildElements(elements);
		
			Node nextSelection = findElementForSelection(elements);
			
			new DeleteNodeAction(model, elements).run();
			
			if (nextSelection != null) {
				treeViewer.setSelection(new StructuredSelection(nextSelection));
			}
		}
		finally {
			model.changedModel();
		}
	}
	
	private Node findElementForSelection(List<Element> toBeDeleted) {
		if (toBeDeleted.size() > 1) {
			return null;
		}
		Element firstElement = toBeDeleted.get(0);
		Element parent = (Element)firstElement.getParentNode();
		
		Node nextSelection = domService.findNextSibling(toBeDeleted.get(toBeDeleted.size()-1), 1);
		if (nextSelection == null || toBeDeleted.contains(nextSelection)) {
			// no next node, use previous node
			nextSelection = domService.findPreviousSibling(firstElement);
		}

		if (nextSelection == null || toBeDeleted.contains(nextSelection)) {
			// next or previous null, use parent
			nextSelection = parent;
		}
		return nextSelection;
	}
	
	private void filterChildElements(List<Element> elements) {
		for (Iterator<Element> iter = Lists.newArrayList(elements).iterator(); iter.hasNext();) {
			Element element = iter.next();
			// climb parent hierarchy, and remove element if one of parents is in list.
			while (true) {
				Node parent = element.getParentNode();
				if (parent == null) {
					break;
				}
				if (elements.contains(parent)) {
					iter.remove();
					break;
				}
				parent = element.getParentNode();
				if (!(parent instanceof Element)) {
					break;
				}
				element = (Element)parent; 
			}
		}
	}
}
