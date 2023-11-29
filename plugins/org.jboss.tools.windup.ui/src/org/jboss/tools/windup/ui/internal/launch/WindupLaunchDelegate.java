/*******************************************************************************
 * Copyright (c) 2021 Red Hat, Inc.

 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.launch;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.runtime.kantra.KantraRunner;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.services.MarkerService;
import org.jboss.tools.windup.ui.internal.services.ViewService;
import org.jboss.tools.windup.windup.ConfigurationElement;
import org.jboss.tools.windup.windup.Pair;

import com.google.common.collect.Lists;


/**
 * The launch delegate for Windup.
 */
public class WindupLaunchDelegate implements ILaunchConfigurationDelegate {
	
	
	@Inject private ModelService modelService;
	@Inject private MarkerService markerService;
	@Inject private ViewService viewService;
	
	@Inject @Named (IServiceConstants.ACTIVE_SHELL) Shell shell;
	
	public static KantraRunner activeRunner = null;
	private static Job kantraJob = null;
	private static IProgressMonitor kantraMonitor;
	
	public void launch(ILaunchConfiguration config, String mode, ILaunch launch, IProgressMonitor monitor) {
		ConfigurationElement configuration = modelService.findConfiguration(config.getName());
		if (configuration == null || configuration.getInputs().isEmpty()) {
			Display.getDefault().asyncExec(() -> {
				MessageDialog.openInformation(Display.getDefault().getActiveShell(), 
						Messages.launchErrorTitle, Messages.launchErrorMessage);
				WindupUIPlugin.logErrorMessage("WindupLaunchDelegate:: unable to launch MTR. Input is empty."); //$NON-NLS-1$
			});
		}
		else {
			markerService.clear();
			this.runKantra(configuration);
		}
	}
	
	 private MessageConsole findConsole(String name) {
	      ConsolePlugin plugin = ConsolePlugin.getDefault();
	      IConsoleManager conMan = plugin.getConsoleManager();
	      IConsole[] existing = conMan.getConsoles();
	      for (int i = 0; i < existing.length; i++)
	         if (name.equals(existing[i].getName()))
	            return (MessageConsole) existing[i];
	      MessageConsole myConsole = new MessageConsole(name, null);
	      conMan.addConsoles(new IConsole[]{myConsole});
	      return myConsole;
	   }
	
	private void runKantra(ConfigurationElement configuration) {
		if (WindupLaunchDelegate.activeRunner != null) {
			WindupLaunchDelegate.activeRunner.kill();
		}
		if (kantraJob != null) {
			kantraJob.cancel();
		}
		
		MessageConsole myConsole = findConsole("kantra");
		MessageConsoleStream out = myConsole.newMessageStream();		
		
		Display.getDefault().syncExec(() -> {
			try {
				IConsoleView view = (IConsoleView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().
						getActivePage().showView(IConsoleConstants.ID_CONSOLE_VIEW);
				view.display(myConsole);
			} catch (PartInitException e) {
				WindupUIPlugin.log(e);
			}
		});
		
		
		kantraJob = new Job(NLS.bind(Messages.generate_windup_report_for, configuration.getName())) {
	      @Override
	      protected IStatus run(IProgressMonitor monitor) {
	    	  	kantraMonitor = monitor;
	      		while (!monitor.isCanceled()) {}    	  	
	          	return Status.OK_STATUS;
	      }
		};
		kantraJob.addJobChangeListener(new JobChangeAdapter() {
	    	@Override
	    	public void done(IJobChangeEvent event) {
	    		super.done(event);
	    		System.out.println("kantra job done");
	    		WindupLaunchDelegate.activeRunner.kill();
	    		kantraMonitor.done();
	    	}
	    });
		kantraJob.setUser(false);
		kantraJob.schedule();
	    		
		
		WindupLaunchDelegate.activeRunner = new KantraRunner();

        
        Set<String> inputs = configuration.getInputs().stream().map(i -> i.getLocation()).collect(Collectors.toSet());
		List<String> sources = Lists.newArrayList();
		List<String> targets = Lists.newArrayList();
		String output = configuration.getOutputLocation();
	            	
    	for (Pair pair : configuration.getOptions()) {
			String name = pair.getKey();
			String value = pair.getValue();
			if (name.equals("input")) {
				inputs.add(value);
			}
			if (name.equals("source")) {
				sources.add(value);
			}
			if (name.equals("target")) {
				targets.add(value);
			}
        }
    	if (targets.isEmpty()) {
    		targets.add("quarkus");
    	}
    	if (sources.isEmpty()) {
    		sources.add("springboot");
    	}
	
    	viewService.launchStarting();
    	Consumer<String> onMessage = (msg) -> { 
    		System.out.println("onMessage: " + msg);
    		Display.getDefault().asyncExec(() -> {
    			out.println(msg.toString());
    		});
    	};
    	Consumer<Boolean> onComplete = (msg) -> { 
    		System.out.println("onComplete: " + msg);
        	viewService.renderReport(configuration);
        	markerService.generateMarkersForConfiguration(configuration);
        	kantraJob.cancel();
    	};
    	Consumer<String> onFailed = (msg) -> { 
    		System.out.println("onFailed: " + msg);
    		System.out.println(msg.toString());
    		kantraJob.cancel();
    	};
    	WindupLaunchDelegate.activeRunner.runKantra(inputs, output, sources, targets, onMessage, onComplete, onFailed);
	}
}
