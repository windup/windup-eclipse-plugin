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
package org.jboss.tools.windup.ui.internal.launch;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.windup.core.services.WindupService;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.runtime.WindupRmiClient;
import org.jboss.tools.windup.runtime.WindupRuntimePlugin;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.services.MarkerService;
import org.jboss.tools.windup.ui.internal.services.ViewService;
import org.jboss.tools.windup.ui.util.WindupLauncher;
import org.jboss.tools.windup.ui.util.WindupServerCallbackAdapter;
import org.jboss.tools.windup.windup.ConfigurationElement;


/**
 * The launch delegate for Windup.
 */
public class WindupLaunchDelegate implements ILaunchConfigurationDelegate {
	
	@Inject private WindupLauncher launcher;
	
	@Inject private WindupService windupService;
	@Inject private ModelService modelService;
	@Inject private WindupRmiClient windupClient;
	@Inject private MarkerService markerService;
	@Inject private ViewService viewService;
	
	@Inject @Named (IServiceConstants.ACTIVE_SHELL) Shell shell;
	
	public void launch(ILaunchConfiguration config, String mode, ILaunch launch, IProgressMonitor monitor) {
		ConfigurationElement configuration = modelService.findConfiguration(config.getName());
		if (configuration == null || configuration.getInputs().isEmpty()) {
			Display.getDefault().asyncExec(() -> {
				MessageDialog.openInformation(Display.getDefault().getActiveShell(), 
						Messages.launchErrorTitle, Messages.launchErrorMessage);
				WindupUIPlugin.logErrorMessage("WindupLaunchDelegate:: unable to launch MTA. Input is empty."); //$NON-NLS-1$
			});
		}
		else {
			markerService.clear();
			
			String jreHome = configuration.getJreHome();
			if (jreHome == null || jreHome.trim().isEmpty()) {
				jreHome = WindupRuntimePlugin.computeJRELocation();
			}
			
			if (windupClient.isWindupServerRunning() && !windupClient.isJreRunning(jreHome)) {
				StringBuilder buff = new StringBuilder();
				buff.append("Current JAVA_HOME: " + windupClient.getJavaHome()); //$NON-NLS-1$
				buff.append(System.lineSeparator());
				buff.append("Restarting using JAVA_HOME: " + jreHome); //$NON-NLS-1$
				WindupUIPlugin.getDefault().getLog().log(
	                    new Status(IStatus.INFO, WindupUIPlugin.PLUGIN_ID, buff.toString()));
				restartAndRun(configuration, jreHome);
			}
				
			else if (!windupClient.isWindupServerRunning()) {
				if (jreHome == null || jreHome.trim().isEmpty()) {
					jreHome = WindupRuntimePlugin.computeJRELocation();
				}
				launcher.start(new WindupServerCallbackAdapter(shell) {
					@Override
					public void windupNotExecutable() {
						WindupUIPlugin.logErrorMessage("WindupLaunchDelegate:: unable to start the MTA server."); //$NON-NLS-1$
						MessageDialog.openError(shell, 
								Messages.WindupNotExecutableTitle, 
								Messages.WindupNotExecutableInfo);
					}
					@Override
					public void serverStart(IStatus status) {
						if (status.getSeverity() == Status.ERROR) {
							MessageDialog.openError(shell,
									Messages.WindupStartingError, 
									status.getMessage());
							WindupUIPlugin.logErrorMessage("WindupLaunchDelegate:: unable to start the MTA server. Message: " + status.getMessage()); //$NON-NLS-1$
						}
						if (status.isOK()) {
							runWindup(configuration);
						}
					}
					@Override
					public void serverShutdown(IStatus status) {}
				}, jreHome);
			}
			else {
				runWindup(configuration);
			}
		}
	}
	
	private void restartAndRun(ConfigurationElement configuration, String jreHome) {
		launcher.shutdown(new WindupServerCallbackAdapter(shell) {
			@Override
			public void serverShutdown(IStatus status) {
				Boolean shutdown;
				if (status.isOK()) {
					shutdown = Boolean.TRUE;
				}
				else {
					shutdown = Boolean.FALSE;
					MessageDialog.openError(shell, 
							Messages.WindupShuttingDownError, 
							status.getMessage());
					WindupUIPlugin.logErrorMessage("WindupLaunchDelegate:: unable to shutdown the MTA server. " + status.getMessage()); //$NON-NLS-1$
				}
				if (shutdown) {
					launcher.start(new WindupServerCallbackAdapter(shell) {
						@Override
						public void windupNotExecutable() {
							MessageDialog.openError(shell, 
									Messages.WindupNotExecutableTitle, 
									Messages.WindupNotExecutableInfo);
							WindupUIPlugin.logErrorMessage("WindupLaunchDelegate:: unable to start the MTA server. Script not executable."); //$NON-NLS-1$
						}
						@Override
						public void serverStart(IStatus status) {
							if (status.getSeverity() == IStatus.ERROR || !windupClient.isWindupServerRunning()) {
								StringBuilder builder = new StringBuilder();
								builder.append(Messages.WindupNotStartedMessage);
								builder.append(status.getMessage());
								builder.append(System.lineSeparator());
								builder.append(System.lineSeparator());
								builder.append(Messages.WindupNotStartedDebugInfo);
								builder.append(System.lineSeparator());
								builder.append(Messages.WindupStartNotStartingSolution1);
								builder.append(System.lineSeparator());
								builder.append(Messages.WindupStartNotStartingSolution2);
								builder.append(System.lineSeparator());
								builder.append(Messages.WindupStartNotStartingSolution3);
								builder.append(System.lineSeparator());
								builder.append(Messages.WindupStartNotStartingSolution4);
								builder.append(System.lineSeparator());
								builder.append(Messages.WindupStartNotStartingSolution5);
								RHAMTStartupFailedDialog dialog = new RHAMTStartupFailedDialog(Display.getDefault().getActiveShell());
								dialog.open();
								WindupUIPlugin.logErrorMessage("WindupLaunchDelegate:: Failed to start the MTA server. Message:" + status.getMessage()); //$NON-NLS-1$
							}
							if(windupClient.isWindupServerRunning()) {
								runWindup(configuration);
							}
						}
					}, jreHome);
				}
			}
		});
	}
	
	private void runWindup(ConfigurationElement configuration) {
		Job job = new Job(NLS.bind(Messages.generate_windup_report_for, configuration.getName())) {
            @Override
            protected IStatus run(IProgressMonitor monitor) {
	            	viewService.launchStarting();
	            	IStatus status = windupService.generateGraph(configuration, monitor);
	            	viewService.renderReport(configuration);
	            	markerService.generateMarkersForConfiguration(configuration);
	            	return status;
            }
        };
        job.setUser(true);
        job.schedule();
	}
}
