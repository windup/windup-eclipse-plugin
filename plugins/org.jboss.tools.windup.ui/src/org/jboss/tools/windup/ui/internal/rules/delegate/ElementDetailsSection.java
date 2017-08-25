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

import javax.inject.Inject;

import org.eclipse.core.resources.IFile;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.pde.internal.ui.editor.FormLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.ui.internal.tabletree.TreeContentHelper;
import org.eclipse.xtext.util.Pair;
import org.eclipse.xtext.util.Tuples;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.IElementUiDelegate;
import org.jboss.tools.windup.ui.internal.rules.delegate.ElementUiDelegate.IElementDetailsContainer;
import org.w3c.dom.Element;

@SuppressWarnings({"restriction"})
public abstract class ElementDetailsSection implements IElementDetailsContainer {
	
	public static final int DEFAULT_SCROLL_SECTION_MAX_HEGHT = 200;
	
	@Inject protected Element element;
	@Inject protected IStructuredModel model;
	@Inject protected ModelQuery modelQuery;
	@Inject protected CMElementDeclaration elementDeclaration;
	@Inject protected TreeContentHelper contentHelper;
	@Inject protected FormToolkit toolkit;
	@Inject protected IEclipseContext context;
	@Inject protected Form form;
	@Inject protected IFile file;
	
	@Inject protected RulesetElementUiDelegateFactory uiDelegateFactory;
	@Inject protected IElementUiDelegate uiDelegate;
	
	@Override
	public abstract void update();
	
	protected abstract void bind();
	
	protected Composite createSection(Composite parent, int columns) {
		return createSection(parent, columns, Messages.ruleElementDetails, null);
	}
	
	protected Composite createSection(Composite parent, int columns, String text, String description) {
		return createSection(parent, columns, toolkit, element, text, description);
	}
	
	public static Composite createSection(Composite parent, int columns, FormToolkit toolkit, Element element, String text, String description) {
		return createSection(parent, columns, toolkit, element, ExpandableComposite.TITLE_BAR, text, description);
	}
	
	public static Composite createSection(Composite parent, int columns, FormToolkit toolkit, Element element, int style, String text, String description) {
		Section section = toolkit.createSection(parent, style);
		//section.clientVerticalSpacing = FormLayoutFactory.SECTION_HEADER_VERTICAL_SPACING;
		//section.setText(Messages.ruleElementDetails); //$NON-NLS-1$
		
		text = text != null ? text : Messages.ruleElementDetails;
		section.setText(text);
		
		if (description != null) {
			Label descriptionLabel = new Label(section, SWT.NONE);
			descriptionLabel.setText(description);
			section.setDescriptionControl(descriptionLabel);
		}
		
		else if (element != null) {
			Label descriptionLabel = new Label(section, SWT.NONE);
			descriptionLabel.setText("Set the properties of '" + element.getNodeName() + "'. Required fields are denoted by '*'."); //$NON-NLS-1$ //$NON-NLS-2$
			section.setDescriptionControl(descriptionLabel);
		}
		
		//section.setLayout(FormLayoutFactory.createClearGridLayout(false, 1));
		GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.marginTop = 0;
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        section.setLayout(layout);
		
        GridDataFactory.fillDefaults().grab(true, false).applyTo(section);
		//section.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
		
		Composite client = toolkit.createComposite(section);
		//int span = computeColumns();
		GridLayout glayout = FormLayoutFactory.createSectionClientGridLayout(false, /*span*/ columns);
		glayout.marginTop = 0;
		glayout.marginRight = 0;
		glayout.marginLeft = 0;
		glayout.marginHeight = 0;
		client.setLayout(glayout);
		client.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		toolkit.paintBordersFor(client);
		section.setClient(client);
		
		//section.setExpanded(true);
		return client;
	}
	
	public static Pair<Section, Composite> createScrolledSection(FormToolkit toolkit, Composite parent, String text, String description, int style, int maxHeight) {
		Section section = toolkit.createSection(parent, style);
		section.setText(text);
		Label descriptionLabel = new Label(section, SWT.NONE);
		descriptionLabel.setText(description);
		section.setDescriptionControl(descriptionLabel);
		section.clientVerticalSpacing = FormLayoutFactory.SECTION_HEADER_VERTICAL_SPACING;
		
		section.setLayout(FormLayoutFactory.createClearGridLayout(false, 1));
		GridDataFactory.fillDefaults().grab(true, true).applyTo(section);
		
		ScrolledComposite scroll = new ScrolledComposite(section, SWT.H_SCROLL|SWT.V_SCROLL) {
			public Point computeSize(int wHint, int hHint, boolean changed) {
				Point size = super.computeSize(wHint, hHint, changed);
				if (size.y > maxHeight) {
					size.y = maxHeight;
				}
				return size;
			};
		};
		scroll.setExpandHorizontal(true);
		scroll.setExpandVertical(true);
		section.setClient(scroll);
		
		Composite client = toolkit.createComposite(scroll, SWT.BORDER);
		client.setLayout(new FormLayout());
		GridDataFactory.fillDefaults().grab(true, true).hint(SWT.DEFAULT, maxHeight).applyTo(client);
		scroll.setContent(client);
		
		toolkit.paintBordersFor(client);
		
		return Tuples.create(section, client);
	}
	
	protected Section createSection(Composite parent, String title, int style, String description) {
		Section section = toolkit.createSection(parent, style);
		section.clientVerticalSpacing = FormLayoutFactory.SECTION_HEADER_VERTICAL_SPACING;
		section.setText(title);
		
		if (description != null) {
			Label descriptionLabel = new Label(section, SWT.NONE);
			descriptionLabel.setText(description);
			section.setDescriptionControl(descriptionLabel);
		}
		
		section.setLayout(FormLayoutFactory.createClearGridLayout(false, 1));
		GridDataFactory.fillDefaults().grab(true, false).applyTo(section);
		
		Composite client = toolkit.createComposite(section);
		GridLayoutFactory.fillDefaults().applyTo(client);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(client);
		
		toolkit.paintBordersFor(client);
		section.setClient(client);
		return section;
	}
	
	protected Label createLabel(Composite parent, FormToolkit toolkit, String text) {
		Label label = toolkit.createLabel(parent, text, SWT.NULL);
		label.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
		return label;
	}
}
