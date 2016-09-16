/*******************************************************************************
 * Copyright (c) 2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.tests;

import javax.inject.Inject;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.ui.internal.e4.compatibility.CompatibilityView;
import org.jboss.tools.test.util.TestProjectProvider;
import org.jboss.tools.windup.core.test.WindupTest;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.model.util.NameUtil;
import org.jboss.tools.windup.ui.WindupPerspective;
import org.jboss.tools.windup.ui.internal.explorer.IssueExplorer;
import org.jboss.tools.windup.ui.tests.swtbot.WorkbenchBot;
import org.jboss.tools.windup.windup.ConfigurationElement;
import org.junit.After;
import org.junit.Before;

import com.google.common.collect.Lists;

/**
 * Base class for Windup UI tests.
 */
@SuppressWarnings("restriction")
public class WindupUiTest extends WindupTest {
	
	private static final String PROJECT = "demo";
	
	protected TestProjectProvider projectProvider;

	@Inject protected MApplication application;
	@Inject protected EModelService eModelService;
	@Inject protected EPartService partService;
	
	@Inject protected WorkbenchBot workbenchBot;
	
	@Inject protected ModelService modelService;
	
	@Before
	public void init() throws CoreException {
		projectProvider = workbenchBot.importProject(Activator.PLUGIN_ID, null, PROJECT, false);
		openWindupPerspective();
	}
	
	@After
	public void clean() {
		projectProvider.dispose();
	}
	
	protected IssueExplorer getIssueExplorer() {
		return (IssueExplorer)((CompatibilityView)partService.findPart(
				IssueExplorer.VIEW_ID).getObject()).getView();
	}
	
	protected void openWindupPerspective() {
		workbenchBot.perspectiveById(WindupPerspective.ID).activate();
	}
	
	protected void runWindup(ConfigurationElement configuration) {
	}
	
	protected ConfigurationElement createRunConfiguration() {
		// TODO: We should do this through SWTBot and the Run Configuration dialog.
		ConfigurationElement configuration = modelService.createConfiguration(
				NameUtil.generateUniqueConfigurationElementName(modelService.getModel()));
		IProject project = projectProvider.getProject();
		modelService.createInput(configuration, Lists.newArrayList(project));
		return configuration;
	}
}
