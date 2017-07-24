package org.jboss.tools.windup.ui.internal.rules;

import javax.inject.Inject;

import org.eclipse.core.resources.IFile;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.pde.internal.ui.editor.FormLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.ui.internal.tabletree.TreeContentHelper;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.rules.ElementUiDelegate.IElementDetailsContainer;
import org.w3c.dom.Element;

@SuppressWarnings({"restriction"})
public abstract class ElementDetailsSection implements IElementDetailsContainer {
	
	@Inject protected Element element;
	@Inject protected IStructuredModel model;
	@Inject protected ModelQuery modelQuery;
	@Inject protected CMElementDeclaration elementDeclaration;
	@Inject protected TreeContentHelper contentHelper;
	@Inject protected FormToolkit toolkit;
	@Inject protected IEclipseContext context;
	@Inject protected Form form;
	@Inject protected IFile file;
	
	@Override
	public abstract void update();
	
	protected abstract void bind();
	
	protected Composite createSection(Composite parent, int columns) {
		Section section = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR | Section.DESCRIPTION | Section.NO_TITLE_FOCUS_BOX);
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
		
		section.setExpanded(true);
		return client;
	}
	
	protected Section createScrolledSection(Composite parent, int columns) {
		Section section = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR | Section.DESCRIPTION | Section.NO_TITLE_FOCUS_BOX);
		section.clientVerticalSpacing = FormLayoutFactory.SECTION_HEADER_VERTICAL_SPACING;
		section.setText(Messages.ruleElementDetails); //$NON-NLS-1$
		section.setDescription("Set the properties of '" + element.getNodeName() + "'. Required fields are denoted by '*'."); //$NON-NLS-1$
		
		section.setLayout(FormLayoutFactory.createClearGridLayout(false, 1));
		GridDataFactory.fillDefaults().grab(true, true).applyTo(section);
		
		ScrolledComposite scroll = new ScrolledComposite(section, SWT.H_SCROLL|SWT.V_SCROLL);
		scroll.setExpandHorizontal(true);
		scroll.setExpandVertical(true);
		section.setClient(scroll);
		
		section.setExpanded(true);
		return section;
	}
	
	protected Section createSection(Composite parent, String title, int style) {
		Section section = toolkit.createSection(parent, style);
		section.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
			}
		});
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
