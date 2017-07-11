package org.jboss.tools.windup.ui.internal.rules;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.pde.internal.ui.editor.FormLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.rules.ElementUiDelegate.IElementDetailsContainer;
import org.w3c.dom.Element;

@SuppressWarnings({"restriction"})
public abstract class ElementDetailsSection implements IElementDetailsContainer {
	
	@Inject protected Element element;
	@Inject protected ModelQuery modelQuery;
	@Inject protected FormToolkit toolkit;
	@Inject protected IEclipseContext context;
	
	@Override
	public abstract void update();
	
	protected Composite createSection(Composite parent, int columns) {
		Composite container = toolkit.createComposite(parent);
		GridLayoutFactory.fillDefaults().margins(0, 0).applyTo(container);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(container);
		
		Section section = toolkit.createSection(container, ExpandableComposite.TITLE_BAR | Section.DESCRIPTION);
		section.clientVerticalSpacing = FormLayoutFactory.SECTION_HEADER_VERTICAL_SPACING;
		section.setText(Messages.ruleElementDetails); //$NON-NLS-1$
		section.setDescription("Set the properties of '" + element.getNodeName() + "'. Required fields are denoted by '*'."); //$NON-NLS-1$
		
		section.setLayout(FormLayoutFactory.createClearGridLayout(false, 1));
		section.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
		
		Composite client = toolkit.createComposite(section);
		//int span = computeColumns();
		GridLayout glayout = FormLayoutFactory.createSectionClientGridLayout(false, /*span*/ columns);
		client.setLayout(glayout);
		client.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		toolkit.paintBordersFor(client);
		section.setClient(client);
		
		return client;
	}
	
	protected Section createSection(Composite parent, String title, int style) {
		Section section = toolkit.createSection(parent, style);
		section.clientVerticalSpacing = FormLayoutFactory.SECTION_HEADER_VERTICAL_SPACING;
		section.setText(title);
		
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
