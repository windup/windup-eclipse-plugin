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
package org.jboss.tools.windup.ui.internal.editor;

import static org.jboss.tools.windup.model.domain.WindupConstants.WINDUP_RUN_COMPLETED;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.windup.ui.internal.editor.issues.WindupIssuesTab;
import org.jboss.tools.windup.ui.internal.editor.launch.WindupConfigurationTab;
import org.jboss.tools.windup.ui.internal.editor.report.WindupReportTab;
import org.jboss.tools.windup.ui.internal.rules.delegate.BaseTabStack;
import org.jboss.tools.windup.windup.ConfigurationElement;

import com.google.common.base.Objects;

/**
 * Represents a stack of tabs within the Windup editor.
 */
public class WindupTabStack extends BaseTabStack {
	
	@Inject private ConfigurationElement configuration;
	
	private TabWrapper issuesTab;
	private TabWrapper reportTab;
	
	@PostConstruct
	protected void create(Composite parent) {
		//super.create(parent);
		addTab(WindupConfigurationTab.class);
		updateTabs();
		folder.setSelection(0);
	}
	
	@Inject
	@Optional
	private void updateDynamicTabs(@UIEventTopic(WINDUP_RUN_COMPLETED) ConfigurationElement configuration) {
		if (Objects.equal(this.configuration, configuration)) {
			updateTabs();
			folder.setSelection(1);
		}
	}
	
	private void updateTabs() {
		if (issuesTab == null && reportTab == null) {
			this.reportTab = addTab(WindupReportTab.class);
			this.issuesTab = addTab(WindupIssuesTab.class);
		}
	}
	
	public void focus() {
		for (TabWrapper wrapper : tabs.values()) {
			ContextInjectionFactory.invoke(wrapper.getObject(), 
					Focus.class, wrapper.getContext(), null);
		}
	}
}
