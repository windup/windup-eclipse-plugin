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
package org.jboss.tools.windup.ui.tests;

import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.internal.e4.compatibility.CompatibilityView;
import org.jboss.tools.test.util.TestProjectProvider;
import org.jboss.tools.windup.core.services.WindupService;
import org.jboss.tools.windup.core.test.WindupTest;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.model.util.NameUtil;
import org.jboss.tools.windup.runtime.WindupRmiClient;
import org.jboss.tools.windup.runtime.WindupRuntimePlugin;
import org.jboss.tools.windup.ui.WindupPerspective;
import org.jboss.tools.windup.ui.internal.explorer.IssueExplorer;
import org.jboss.tools.windup.ui.internal.explorer.QuickfixService;
import org.jboss.tools.windup.ui.internal.launch.LaunchUtils;
import org.jboss.tools.windup.ui.internal.services.CreateMigrationIssueService;
import org.jboss.tools.windup.ui.internal.services.MarkerService;
import org.jboss.tools.windup.ui.internal.services.RulesetDOMService;
import org.jboss.tools.windup.ui.internal.services.RulesetSelectionCreationService;
import org.jboss.tools.windup.ui.internal.services.ViewService;
import org.jboss.tools.windup.ui.tests.swtbot.WorkbenchBot;
import org.jboss.tools.windup.ui.util.WindupLauncher;
import org.jboss.tools.windup.ui.util.WindupServerCallbackAdapter;
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
	@Inject protected WindupService windupService;
	
	@Inject protected IEventBroker broker;
	
	@Inject protected WindupRmiClient windupClient;
	@Inject protected WindupLauncher windupLauncher;
	
	@Inject protected MarkerService markerService;
	@Inject protected QuickfixService quickfixService;
	@Inject protected ViewService viewService;
	
	@Inject protected RulesetDOMService domService;
	@Inject protected RulesetSelectionCreationService ruleCreationService;
	@Inject protected CreateMigrationIssueService issueService;
	
	protected IssueExplorer issueExplorer;
	
	@Before
	public void init() throws CoreException {
		projectProvider = workbenchBot.importProject(Activator.PLUGIN_ID, null, PROJECT, false);
		openWindupPerspective();
		this.issueExplorer = (IssueExplorer)((CompatibilityView)partService.findPart(
				IssueExplorer.VIEW_ID).getObject()).getView();
		assertNotNull("Issue Explorer is NULL.", issueExplorer);
		System.out.println("@Before::init:: start RHAMT server.");
		windupLauncher.start(new WindupServerCallbackAdapter(Display.getDefault().getActiveShell()) {
			@Override
			public void serverStart(IStatus status) {
				if (status.isOK() && windupClient.getExecutionBuilder() != null) {
					System.out.println("@Before::init:: RHAMT server started successfully.");
				}
				else {
					System.out.println("@Before::init:: RHAMT server failed to start.");
				}
			}
		}, WindupRuntimePlugin.computeJRELocation());
	}
	
	@After
	public void clean() {
		projectProvider.dispose();
	}
	
	protected void openWindupPerspective() {
		workbenchBot.perspectiveById(WindupPerspective.ID).activate();
	}
	
	protected void runWindup(ConfigurationElement configuration) {
		if (windupClient.isWindupServerRunning()) {
			System.out.println("RHAMT server already running.");
			Display.getDefault().syncExec(() -> {
				markerService.clear();
            	viewService.launchStarting();
				windupService.generateGraph(configuration, new NullProgressMonitor());
				viewService.renderReport(configuration);
            	markerService.generateMarkersForConfiguration(configuration);
			});
		}
		else {
			System.out.println("RHAMT server not running. Attempting to start it.");
			windupLauncher.start(new WindupServerCallbackAdapter(Display.getDefault().getActiveShell()) {
				@Override
				public void serverStart(IStatus status) {
					System.out.println("Finished attempt to start RHAMT server.");
					if (status.isOK() && windupClient.getExecutionBuilder() != null) {
						System.out.println("RHAMT server started successfully.");
						markerService.clear();
		            	viewService.launchStarting();
						windupService.generateGraph(configuration, new NullProgressMonitor());
						viewService.renderReport(configuration);
		            	markerService.generateMarkersForConfiguration(configuration);
					}
				}
			}, WindupRuntimePlugin.computeJRELocation());
		}
	}
	
	protected ConfigurationElement createRunConfiguration() {
		// TODO: We should do this through SWTBot and the Run Configuration dialog.
		ConfigurationElement configuration = LaunchUtils.createConfiguration(
				NameUtil.generateUniqueConfigurationElementName(modelService.getModel()), modelService);
		IProject project = projectProvider.getProject();
		modelService.createInput(configuration, Lists.newArrayList(project.getLocation().toString()));
		return configuration;
	}
}
