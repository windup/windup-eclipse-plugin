/*******************************************************************************
 * Copyright (c) 2019 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.rules;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.action.ContributionManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.menus.IMenuService;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.editor.RulesetEditorRulesSection;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.IElementUiDelegate;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateRegistry;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class RulesetEditor {
	
	public static final String ID = "org.jboss.tools.windup.ui.rulesetEditor"; //$NON-NLS-1$
	private static final String TOOLBAR_ID = "toolbar:org.jboss.tools.windup.toolbar"; //$NON-NLS-1$
	
	private static final String SASH_LEFT = "weightLeft"; //$NON-NLS-1$
	private static final String SASH_RIGHT = "weightRight"; //$NON-NLS-1$
	
	private static final int SASH_LEFT_DEFAULT = 238;
	private static final int SASH_RIGHT_DEFAULT = 685;
	
	@Inject private Composite container;
	@Inject private IEclipseContext context;
	@Inject private IMenuService menuService;
	
	@Inject	private RulesetElementUiDelegateRegistry widgetRegistry;
	
	@Inject private IFile file;
	
	private Composite stackComposite;
	private Composite gettingStartedComposite;
	
	private Form form;
	private FormToolkit toolkit;
	
	private SashForm sash;
	
	private IEclipsePreferences preferences = InstanceScope.INSTANCE.getNode(WindupUIPlugin.PLUGIN_ID);
	
	private DataBindingContext bindingContext = new DataBindingContext();
	
	private RulesetEditorRulesSection elementsSection;
	
	private IElementUiDelegate activeElement;
	
	public void setDocument(Document document) {
		elementsSection.setDocument(document);
		if (activeElement != null) {
			activeElement.update();
		}
	}
	
	public ISelectionProvider getSelectionProvider() {
		return elementsSection.getSelectionProvider();
	}
	
	public Control getControl() {
		return form;
	}
	
	public void setFocus() {
		if (activeElement != null) {
			activeElement.setFocus();
		}
	}
	
	public void selectAndReveal(Element element) {
		elementsSection.selectAndReveal(element);
	}
	
	@PostConstruct
	private void createParent(Composite parent) {
		this.toolkit = new FormToolkit(container.getDisplay());
		this.form = toolkit.createForm(parent);
		//form.setText(rulesEditor_tabTitle);
		//form.setImage(WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_WINDUP));
		
		menuService.populateContributionManager((ContributionManager)form.getToolBarManager(), TOOLBAR_ID);
		
		Composite comp = form.getBody();
		comp.setBackground(parent.getBackground());
		GridLayoutFactory.fillDefaults().applyTo(comp);

		context.set(Form.class, form);
		context.set(FormToolkit.class, toolkit);
		context.set(DataBindingContext.class, bindingContext);
		context.set(IEclipsePreferences.class, preferences);
		
		this.sash = new SashForm(comp, SWT.SMOOTH|SWT.VERTICAL);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(sash);
		sash.setOrientation(SWT.HORIZONTAL);
		sash.setFont(comp.getFont());
		sash.setVisible(true);
		
		String leftPref = file.getFullPath()+SASH_LEFT;
		String rightPref = file.getFullPath()+SASH_RIGHT;
		
		sash.addDisposeListener((e) -> {
			preferences.put(leftPref, String.valueOf(sash.getWeights()[0]));
			preferences.put(rightPref, String.valueOf(sash.getWeights()[1]));
		});
		
		elementsSection = createLeftSide(sash);
		createRightSide(sash);
		
		int left = preferences.getInt(leftPref, SASH_LEFT_DEFAULT);
		int right = preferences.getInt(rightPref, SASH_RIGHT_DEFAULT);
		
		sash.setWeights(new int[]{left, right});
				
		//toolkit.decorateFormHeading(form.getForm());
		toolkit.paintBordersFor(form.getBody());
		
		updateDetails(null);
	}
	
	private RulesetEditorRulesSection createLeftSide(Composite parent) {
		IEclipseContext child = context.createChild();
		child.set(RulesetElementUiDelegateRegistry.class, widgetRegistry);
		RulesetEditorRulesSection section = createChild(RulesetEditorRulesSection.class, parent, child);
		section.getSelectionProvider().addSelectionChangedListener((e) -> {
			Object object = ((StructuredSelection)e.getSelection()).getFirstElement();
			updateDetails((Element)object);
		});
		return section;
	}
	
	private <T> T createChild(Class<T> clazz, Composite parent, IEclipseContext child) {
		child.set(Composite.class, parent);
		child.set(RulesetElementUiDelegateFactory.class, widgetRegistry.getUIDelegateFactory());
		return ContextInjectionFactory.make(clazz, child);
	}
	
	private void createRightSide(Composite parent) {
		this.stackComposite = new Composite(parent, SWT.NONE);
		stackComposite.setLayout(new StackLayout());
		GridDataFactory.fillDefaults().grab(true, true).applyTo(stackComposite);
		fillRightSide(stackComposite);
	}
	
	private void fillRightSide(Composite parent) {
		this.gettingStartedComposite = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().applyTo(gettingStartedComposite);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(gettingStartedComposite);
		createGettingStarted(gettingStartedComposite);
	}
	
	protected void createGettingStarted(Composite parent) {
		//parent.setBackground(toolkit.getColors().getBackground());
		new Label(parent, SWT.NONE);
	}
	
	public IElementUiDelegate getUiDelegate(Element element) {
		return widgetRegistry.getOrCreateUiDelegate(element, createUiDelegateContext(element));
	}
	
	private IEclipseContext createUiDelegateContext(Element element) {
		IEclipseContext child = context.createChild();
		child.set(Element.class, element);
		child.set(Composite.class, stackComposite);
		child.set(RulesetEditor.class, this);
		return child;
	}
	
	public IElementUiDelegate getDelegate(Element element) {
		return widgetRegistry.getOrCreateUiDelegate(element, createUiDelegateContext(element));
	}
	
	public void updateDetails(Element element) {
		if (form.isDisposed()) {
			return;
		}
		int left = sash.getWeights()[0];
		int right = sash.getWeights()[1];
		Control top = gettingStartedComposite;
		if (element != null) {
			IElementUiDelegate uiDelegate = getDelegate(element);
			if (uiDelegate != null) { 
				top = uiDelegate.getControl();
				uiDelegate.update();
				this.activeElement = uiDelegate;
			}
		}
		((StackLayout)stackComposite.getLayout()).topControl = top;

		stackComposite.layout(true, true);
		sash.setWeights(new int[]{left, right});
	}
}

