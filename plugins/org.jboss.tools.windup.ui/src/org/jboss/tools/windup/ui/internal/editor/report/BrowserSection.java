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
package org.jboss.tools.windup.ui.internal.editor.report;

import static org.jboss.tools.windup.model.domain.WindupConstants.WINDUP_RUN_COMPLETED;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.windup.ui.internal.editor.AbstractSection;
import org.jboss.tools.windup.windup.ConfigurationElement;

/**
 * Section containing an embedded browser.
 */
public class BrowserSection extends AbstractSection {

	private Browser browser;
	
	@PostConstruct
	@Override
	protected void createSection(Composite parent) {
		browser = new Browser(parent, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(browser);
		loadReport();
	}
	
	@Inject
	@Optional
	private void updateDynamicTabs(@UIEventTopic(WINDUP_RUN_COMPLETED) ConfigurationElement configuration) {
		/*WindupResult result = configuration.getWindupResult();
		if (Objects.equal(this.configuration, configuration) && result != null) {
			loadReport();
		}*/
	}
	
	private void loadReport() {
		//browser.setUrl(modelService.getReportPath(configuration).toString());
	}
}
