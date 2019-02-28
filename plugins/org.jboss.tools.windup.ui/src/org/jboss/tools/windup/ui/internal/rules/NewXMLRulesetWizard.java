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
package org.jboss.tools.windup.ui.internal.rules;

import java.lang.reflect.InvocationTargetException;

import javax.inject.Inject;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;

public class NewXMLRulesetWizard extends Wizard implements INewWizard, IExecutableExtension {
	
	private IStructuredSelection selection;

	private IConfigurationElement fConfig;
	
	private NewXMLFilePage startPage;
	
	@Inject private GenerateRulesetOperation generateOperation;
	private CreateRulesetOperation createRulesetOperation;
	
	private String newRulesetLocation;
	
	public NewXMLRulesetWizard() {
		setWindowTitle(Messages.NewXMLRuleset_title);
		setNeedsProgressMonitor(true);
	}
	
	public void init(CreateRulesetOperation createRulesetOperation) {
		this.createRulesetOperation = createRulesetOperation;
	}

	@Override
	public void setInitializationData(IConfigurationElement config, String property, Object data) throws CoreException {
		this.fConfig = config;
	}

	@Override
	public void addPages() {
		startPage = new NewXMLFilePage("startPage", selection);
		addPage(startPage);
	}

	@Override
	public boolean performFinish() {
		try {
			BasicNewProjectResourceWizard.updatePerspective(fConfig);
			IResource container = ResourcesPlugin.getWorkspace().getRoot().findMember(startPage.getContainerFullPath());
			if (container instanceof IContainer) {
			    	String rulesetId = startPage.getRulesetId();
			    	String fileName = startPage.getFileName();
			    	boolean generateQuickstart = startPage.generateQuickStartTemplate();
			    	generateOperation.init(getShell().getDisplay(), (IContainer)container, fileName, rulesetId, generateQuickstart);
				getContainer().run(false, true, generateOperation);
				this.newRulesetLocation = ((IContainer)container).getFile(new Path(fileName)).getLocation().toString();
				createRulesetOperation.init(newRulesetLocation);
				getContainer().run(false, true, createRulesetOperation);
			}
		} catch (InvocationTargetException e) {
			WindupUIPlugin.log(e);
			return false;
		} catch (InterruptedException e) {
			WindupUIPlugin.log(e);
			return false;
		}
		return true;
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
		this.selection = currentSelection;
	}
	
	public String getNewRulestLocation() {
		return newRulesetLocation;
	}
}
