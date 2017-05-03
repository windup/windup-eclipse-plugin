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
import static org.jboss.tools.windup.ui.internal.Messages.noIssueDetails;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.resources.IMarker;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.internal.browser.BrowserViewer;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.services.MarkerService;
import org.jboss.tools.windup.windup.Issue;
import org.markdown4j.Markdown4jProcessor;

/**
 * View for displaying the details of an Issue.
 */
@SuppressWarnings("restriction")
public class IssueDetailsView {
	
	public static final String ID = "org.jboss.tools.windup.ui.issue.details";
	
	private FormToolkit toolkit;
	private DetailsComposite detailsComposite;
	private Composite placeholder;
	private Composite stack;
	
	@Inject private MarkerService markerService;
	
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
				Issue issue = markerService.find(marker);
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
		private BrowserViewer browserViewer;
		
		@Inject
		public DetailsComposite(Composite parent, FormToolkit toolkit) {
			super(parent, SWT.NONE);
			GridLayoutFactory.fillDefaults().applyTo(this);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(this);
			browserViewer = new BrowserViewer(this, BrowserViewer.LOCATION_BAR|BrowserViewer.BUTTON_BAR);
	        browserViewer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		}
		
		public void setIssue(IMarker marker, Issue issue) {
			this.marker = marker;
			refresh(issue);
		}
		
		private void refresh(Issue issue) {
			String html = buildText(issue);
			try {
				File htmlFile = File.createTempFile("tmp", ".html");
				FileUtils.write(htmlFile, html);
				browserViewer.setURL(htmlFile.getAbsolutePath());;
			} catch (Exception e) {
				WindupUIPlugin.log(e);
			}
		}
		
		private String buildText(Issue issue) {
			StringBuilder builder = new StringBuilder();
			builder.append("<p><b>Title</b></p>");
			builder.append(marker.getAttribute(TITLE, noIssueDetails));
			builder.append("<p><b>Hint</b></p>");
			Markdown4jProcessor markdownProcessor = new Markdown4jProcessor();
			try {
				builder.append(markdownProcessor.process(marker.getAttribute(HINT, noIssueDetails)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			builder.append("<p><b>Severity</b></p>");
			builder.append(marker.getAttribute(SEVERITY, noIssueDetails));
			builder.append("<p><b>Effort</b></p>");
			builder.append(marker.getAttribute(EFFORT, noIssueDetails));
			builder.append("<p><b>Rule ID</b></p>");
			builder.append(marker.getAttribute(RULE_ID, noIssueDetails));
			builder.append("<p><b>More Information</b></p>");
			if (issue.getLinks().isEmpty()) {
				builder.append(noIssueDetails);
			}
			for (final org.jboss.tools.windup.windup.Link link : issue.getLinks()) {
				builder.append("<p>" + link.getDescription() + "<br/>");
				builder.append("<ul><li>");
				builder.append("<a href=\"" + link.getUrl() + "\"" + ">" + link.getUrl() + "</a>");
				builder.append("</ul></li></p>");
			}
			builder.append("<p><b>Source</b></p>");
			builder.append(marker.getAttribute(SOURCE_SNIPPET, noIssueDetails));
			return builder.toString();
		}
	}
}
