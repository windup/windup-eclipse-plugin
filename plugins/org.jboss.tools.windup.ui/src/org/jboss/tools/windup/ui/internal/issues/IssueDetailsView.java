/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.issues;

import static org.jboss.tools.windup.model.domain.WindupMarker.EFFORT;
import static org.jboss.tools.windup.model.domain.WindupMarker.HINT;
import static org.jboss.tools.windup.model.domain.WindupMarker.RULE_ID;
import static org.jboss.tools.windup.model.domain.WindupMarker.SEVERITY;
import static org.jboss.tools.windup.model.domain.WindupMarker.SOURCE_SNIPPET;
import static org.jboss.tools.windup.model.domain.WindupMarker.TITLE;
import static org.jboss.tools.windup.ui.internal.Messages.issueLabelEffort;
import static org.jboss.tools.windup.ui.internal.Messages.issueLabelHint;
import static org.jboss.tools.windup.ui.internal.Messages.issueLabelInfo;
import static org.jboss.tools.windup.ui.internal.Messages.issueLabelRuleId;
import static org.jboss.tools.windup.ui.internal.Messages.issueLabelSeverity;
import static org.jboss.tools.windup.ui.internal.Messages.issueLabelSource;
import static org.jboss.tools.windup.ui.internal.Messages.issueLabelTitle;
import static org.jboss.tools.windup.ui.internal.Messages.noIssueDetails;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.resources.IMarker;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.windup.Issue;

import com.google.common.collect.Lists;

/**
 * View for displaying the details of an Issue.
 */
public class IssueDetailsView {
	
	public static final String ID = "org.jboss.tools.windup.ui.issue.details";
	
	private FormToolkit toolkit;
	private DetailsComposite detailsComposite;
	private Composite placeholder;
	private Composite stack;
	
	@Inject private ModelService modelService;
	
	private static Color DETAILS_BACKGROUND_COLOR = Display.getDefault().getSystemColor(SWT.COLOR_INFO_BACKGROUND);
	
	private ScrolledForm form;

	@PostConstruct
	private void create(Composite parent, IEclipseContext context) {
		toolkit = new FormToolkit(Display.getDefault());
		form = toolkit.createScrolledForm(parent);
		GridLayoutFactory.fillDefaults().applyTo(form);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(form);
		GridLayoutFactory.fillDefaults().applyTo(form.getBody());
		stack = toolkit.createComposite(form.getBody());
		toolkit.paintBordersFor(stack);
		stack.setLayout(new StackLayout());
		GridDataFactory.fillDefaults().grab(true, true).applyTo(stack);
		IEclipseContext child = context.createChild();
		child.set(FormToolkit.class, toolkit);
		child.set(Composite.class, stack);
		detailsComposite = ContextInjectionFactory.make(DetailsComposite.class, child);
		placeholder = toolkit.createComposite(stack);
		toolkit.createLabel(placeholder, noIssueDetails);
		GridLayoutFactory.fillDefaults().applyTo(placeholder);
		setTop(placeholder);
	}
	
	@PreDestroy
	private void dispose() {
		toolkit.dispose();
	}
	
	@Inject
	public void showIssueDetails(@Optional IMarker marker) {
		if (detailsComposite != null && !detailsComposite.isDisposed()) {
			Composite top = placeholder;
			if (marker != null) {
				Issue issue = modelService.findIssue(marker);
				if (issue != null) {
					detailsComposite.setIssue(marker, issue);
					top = detailsComposite;
				}
			}
			setTop(top);
		}
	}
	
	private void setTop(Control top) {
		((StackLayout)stack.getLayout()).topControl = top;
		stack.layout(true, true);
		form.reflow(true);
	}

	private static class DetailsComposite extends Composite {

		private IMarker marker;
		private FormToolkit toolkit;
		
		private Label titleText;
		private Label hintText;
		private Label severityText;
		private Label effortText;
		private Label ruleIdText;
		private Label sourceText;
		
		private InfoSection infoSection;
		
		@Inject
		public DetailsComposite(Composite parent, FormToolkit toolkit) {
			super(parent, SWT.NONE);
			this.toolkit = toolkit;
			GridLayoutFactory.fillDefaults().applyTo(this);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(this);
			createControls(this);
		}
		
		private void createControls(Composite parent) {
			Composite container = toolkit.createComposite(parent);
			GridLayoutFactory.fillDefaults().applyTo(container);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(container);
			
			this.titleText = createSection(container, issueLabelTitle);
			this.hintText = createSection(container, issueLabelHint);
			this.severityText = createSection(container, issueLabelSeverity);
			this.effortText = createSection(container, issueLabelEffort);
			this.ruleIdText = createSection(container, issueLabelRuleId);
			
			DetailsComposite.createHeaderLabel(toolkit, container, issueLabelInfo);
			infoSection = new InfoSection(container, toolkit);
			
			this.sourceText = createSection(container, issueLabelSource);
		}
		
		private Label createSection(Composite parent, String header) {
			DetailsComposite.createHeaderLabel(toolkit, parent, header);
			return DetailsComposite.createDeailsLabel(toolkit, parent);
		}
		
		private static Label createHeaderLabel(FormToolkit toolkit, Composite parent, String text) {
			Label label = toolkit.createLabel(parent, text);
			label.setFont(JFaceResources.getFontRegistry().get(JFaceResources.HEADER_FONT));
			return label;
		}
		
		private static Label createDeailsLabel(FormToolkit toolkit, Composite parent) {
			Label label = toolkit.createLabel(parent, "", SWT.WRAP);
			label.setBackground(DETAILS_BACKGROUND_COLOR);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(label);
			return label;
		}
		
		public void setIssue(IMarker marker, Issue issue) {
			this.marker = marker;
			refresh(issue);
		}
		
		private void refresh(Issue issue) {
			titleText.setText(marker.getAttribute(TITLE, noIssueDetails));
			hintText.setText(marker.getAttribute(HINT, noIssueDetails));
			severityText.setText(marker.getAttribute(SEVERITY, noIssueDetails));
			effortText.setText(marker.getAttribute(EFFORT, noIssueDetails));
			ruleIdText.setText(marker.getAttribute(RULE_ID, noIssueDetails));
			sourceText.setText(marker.getAttribute(SOURCE_SNIPPET, noIssueDetails));
			infoSection.update(issue);
		}
	}
	
	private static class InfoSection extends Composite {
		
		private FormToolkit toolkit;
		private List<Composite> sections = Lists.newArrayList();
		private Label noDetailsLabel;
		
		public InfoSection(Composite parent, FormToolkit toolkit) {
			super(parent, SWT.NONE);
			this.toolkit = toolkit;
			GridLayoutFactory.fillDefaults().applyTo(this);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(this);
		}
		
		private void disposePlaceholder() {
			if (noDetailsLabel != null && !noDetailsLabel.isDisposed()) {
				noDetailsLabel.dispose();
				noDetailsLabel = null;
			}
		}
		
		private void createPlaceholder() {
			if (noDetailsLabel == null || noDetailsLabel.isDisposed()) {
				noDetailsLabel = DetailsComposite.createDeailsLabel(toolkit, this);
				noDetailsLabel.setText(noIssueDetails);
			}
		}
		
		public void update(Issue issue) {
			sections.forEach(section -> section.dispose());
			if (issue.getLinks().isEmpty()) {
				createPlaceholder();
			}
			else {
				disposePlaceholder();
				for (final org.jboss.tools.windup.windup.Link link : issue.getLinks()) {
					Composite group = toolkit.createComposite(this);
					group.setBackground(DETAILS_BACKGROUND_COLOR);
					sections.add(group);
					GridLayoutFactory.fillDefaults().spacing(0, 3).numColumns(1).applyTo(group);
					GridDataFactory.fillDefaults().grab(true, false).applyTo(group);;
					Label label = new Label(group, SWT.NONE);
					label.setText(link.getDescription());
					GridDataFactory.fillDefaults().indent(5, 0).applyTo(label);
					Hyperlink hyperLink = toolkit.createHyperlink(group, link.getUrl(), SWT.NO_FOCUS);
					hyperLink.setBackground(DETAILS_BACKGROUND_COLOR);
					hyperLink.addHyperlinkListener(new HyperlinkAdapter() {
						@Override
						public void linkActivated(HyperlinkEvent e) {
							Program.launch(link.getUrl());
						}
					});
					GridDataFactory.fillDefaults().indent(15, 0).applyTo(hyperLink);
				}
			}
		}
	}
}
