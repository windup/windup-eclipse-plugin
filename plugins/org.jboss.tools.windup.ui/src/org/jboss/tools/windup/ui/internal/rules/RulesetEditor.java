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
package org.jboss.tools.windup.ui.internal.rules;

import static org.jboss.tools.windup.model.domain.WindupConstants.ACTIVE_ELEMENT;
import static org.jboss.tools.windup.ui.internal.Messages.rulesEditor_tabTitle;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.jface.action.ContributionManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.menus.IMenuService;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.editor.RulesetEditorRulesSection;
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
	@Inject private ModelService modelService;
	
	@Inject	private RulesetElementUiDelegateRegistry widgetRegistry;
	
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
		form.setText(rulesEditor_tabTitle);
		form.setImage(WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_WINDUP));
		
		menuService.populateContributionManager((ContributionManager)form.getToolBarManager(), TOOLBAR_ID);
		
		Composite comp = form.getBody();
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
		
		sash.addDisposeListener((e) -> {
			preferences.put(SASH_LEFT, String.valueOf(sash.getWeights()[0]));
			preferences.put(SASH_RIGHT, String.valueOf(sash.getWeights()[1]));
		});
		
		elementsSection = createLeftSide(sash);
		createRightSide(sash);
		
		int left = preferences.getInt(SASH_LEFT, SASH_LEFT_DEFAULT);
		int right = preferences.getInt(SASH_RIGHT, SASH_RIGHT_DEFAULT);
		
		sash.setWeights(new int[]{left, right});
				
		toolkit.decorateFormHeading(form);
		toolkit.paintBordersFor(form.getBody());
	}
	
	private RulesetEditorRulesSection createLeftSide(Composite parent) {
		IEclipseContext child = context.createChild();
		child.set(RulesetElementUiDelegateRegistry.class, widgetRegistry);
		return createChild(RulesetEditorRulesSection.class, parent, child);
	}
	
	private <T> T createChild(Class<T> clazz, Composite parent, IEclipseContext child) {
		child.set(Composite.class, parent);
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
		new Label(parent, SWT.NONE).setText("");
	}
	
	@Inject
	@Optional
	private void updateDetails(@UIEventTopic(ACTIVE_ELEMENT) Element element) {
		if (form.isDisposed()) {
			return;
		}
		Control top = gettingStartedComposite;
		if (element != null) {
			IElementUiDelegate uiDelegate = widgetRegistry.getOrCreateUiDelegate(element, stackComposite, context);
			if (uiDelegate != null && uiDelegate.isEditable()) {
				top = uiDelegate.getControl();
				this.activeElement = uiDelegate;
			}
		}
		((StackLayout)stackComposite.getLayout()).topControl = top;
		stackComposite.layout(true, true);
	}
}

