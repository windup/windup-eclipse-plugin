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
package org.jboss.tools.windup.ui.internal.launch;

import static org.jboss.tools.windup.model.domain.ConfigurationResourceUtil.computePackages;
import static org.jboss.tools.windup.model.domain.ConfigurationResourceUtil.getCurrentPackages;
import static org.jboss.tools.windup.model.domain.ConfigurationResourceUtil.getCurrentProjects;
import static org.jboss.tools.windup.ui.internal.Messages.inputProjectsDescription;
import static org.jboss.tools.windup.ui.internal.Messages.inputTabName;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.internal.ui.SWTFactory;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.editor.launch.FilteredListDialog;
import org.jboss.tools.windup.windup.ConfigurationElement;
import org.jboss.tools.windup.windup.MigrationPath;

import com.google.common.collect.Lists;

/**
 * The tab where the user specifies the input to analyze by Windup.
 */
@SuppressWarnings("restriction")
public class WindupInputTab extends AbstractLaunchConfigurationTab {
	
	private static final String ID = "org.jboss.tools.windup.ui.launch.WindupInputTab"; //$NON-NLS-1$

	private ModelService modelService;
	private ConfigurationElement configuration;
	
	private TableViewer projectsTable;
	private TableViewer packagesTable;
	private Combo migrationPathCombo;
	
	public WindupInputTab(ModelService modelService) {
		this.modelService = modelService;
	}
	
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().margins(5, 5).applyTo(container);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(container);
		createMigrationPathGroup(container);
		createProjectsGroup(container);
		createPackagesGroup(container);
		createVerticalSpacer(container, 1);
		super.setControl(container);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(container, ID);
	}
	
	private void createProjectsGroup(Composite parent) {
		Group group = SWTFactory.createGroup(parent, Messages.windupProjects+":", 2, 1, GridData.FILL_BOTH);
		GridDataFactory.fillDefaults().grab(true, true).hint(70, 100).applyTo(group);
		projectsTable = new TableViewer(group, SWT.BORDER|SWT.MULTI);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(projectsTable.getTable());
		projectsTable.setLabelProvider(new WorkbenchLabelProvider());
		projectsTable.setContentProvider(ArrayContentProvider.getInstance());
		createProjectsButtonBar(group, projectsTable);
	}
	
	private void reload() {
		reloadMigrationPath();
		reloadProjectsTable();
		reloadPackagesTable();
	}
	
	private void reloadProjectsTable() {
		if (projectsTable != null) {
			projectsTable.setInput(getCurrentProjects(configuration));
		}
	}
	
	private void reloadPackagesTable() {
		if (packagesTable != null) {
			packagesTable.setInput(getCurrentPackages(configuration));
		}
	}
	
	private void reloadMigrationPath() {
		if (migrationPathCombo != null) {
			int current = Lists.newArrayList(migrationPathCombo.getItems()).
					indexOf(configuration.getMigrationPath().getName());
			migrationPathCombo.select(current);
		}
	}
	
	private void createProjectsButtonBar(Composite parent, TableViewer table) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().margins(0, 0).spacing(0, 0).applyTo(container);
		GridDataFactory.fillDefaults().grab(false, true).applyTo(container);
		
		Button addButton = new Button(container, SWT.PUSH);
		addButton.setText(Messages.windupAdd);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(addButton);
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				List<IProject> projects = Lists.newArrayList(ResourcesPlugin.getWorkspace().getRoot().getProjects());
				projects.removeAll(Lists.newArrayList(getCurrentProjects(configuration)));
				FilteredListDialog dialog = new FilteredListDialog(parent.getShell(), new WorkbenchLabelProvider());
				dialog.setMultipleSelection(true);
				dialog.setMessage(Messages.windupProjectsSelect);
				dialog.setElements(projects.toArray(new IProject[projects.size()]));
				dialog.setTitle(Messages.windupProjects);
				dialog.setHelpAvailable(false);
				dialog.create();
				if (dialog.open() == Window.OK) {
					Object[] selected = (Object[])dialog.getResult();
					if (selected.length > 0) {
						List<IProject> newProjects = Lists.newArrayList();
						Arrays.stream(selected).forEach(p -> newProjects.add((IProject)p));
						modelService.createInput(configuration, newProjects);
						reloadProjectsTable();
					}
				}
				updateLaunchConfigurationDialog();
			}
		});
		
		Button removeButton = new Button(container, SWT.PUSH);
		removeButton.setText(Messages.windupRemove);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(removeButton);
		removeButton.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				ISelection selection = projectsTable.getSelection();
				if (!selection.isEmpty()) {
					StructuredSelection ss = (StructuredSelection)selection;
					List<IProject> projects = (List<IProject>)ss.toList();
					modelService.deleteInput(configuration, projects);
					modelService.synch(configuration);
					reloadProjectsTable();
					reloadPackagesTable();
				}
			}
		});
	}
	
	private void createPackagesGroup(Composite parent) {
		Group group = SWTFactory.createGroup(parent, Messages.windupPackages+":", 2, 1, GridData.FILL_BOTH);
		GridDataFactory.fillDefaults().grab(true, true).hint(70, 100).applyTo(group);
		packagesTable = new TableViewer(group, SWT.BORDER|SWT.MULTI);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(packagesTable.getTable());
		packagesTable.setLabelProvider(new WorkbenchLabelProvider());
		packagesTable.setContentProvider(ArrayContentProvider.getInstance());
		createPackagesButtonBar(group, packagesTable);
	}
	
	private void createPackagesButtonBar(Composite parent, TableViewer table) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().margins(0, 0).spacing(0, 0).applyTo(container);
		GridDataFactory.fillDefaults().grab(false, true).applyTo(container);
		
		Button addButton = new Button(container, SWT.PUSH);
		addButton.setText(Messages.windupAdd);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(addButton);
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FilteredListDialog dialog = new FilteredListDialog(parent.getShell(), new WorkbenchLabelProvider());
				dialog.setMultipleSelection(true);
				dialog.setMessage(Messages.windupPackagesSelect);
				dialog.setElements(computePackages(configuration));
				dialog.setTitle(Messages.windupPackages);
				dialog.setHelpAvailable(false);
				dialog.create();
				if (dialog.open() == Window.OK) {
					Object[] selected = (Object[])dialog.getResult();
					if (selected.length > 0) {
						List<IPackageFragment> packages = Lists.newArrayList();
						Arrays.stream(selected).forEach(p -> packages.add((IPackageFragment)p));
						modelService.addPackages(configuration, packages);
						reloadPackagesTable();
					}
				}
			}
		});
		
		Button removeButton = new Button(container, SWT.PUSH);
		removeButton.setText(Messages.windupRemove);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(removeButton);
		removeButton.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				ISelection selection = table.getSelection();
				if (!selection.isEmpty()) {
					StructuredSelection ss = (StructuredSelection)selection;
					modelService.removePackages(configuration, (List<IPackageFragment>)ss.toList());
					reloadPackagesTable();
				}
			}
		});
	}
	
	private void createMigrationPathGroup(Composite parent) {
		Group group = SWTFactory.createGroup(parent, Messages.windupMigrationPath+":", 1, 1, GridData.FILL_HORIZONTAL);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(group);
		String[] items = buildMigrationPaths();
		migrationPathCombo = SWTFactory.createCombo(group, SWT.READ_ONLY|SWT.BORDER, GridData.FILL_HORIZONTAL, items);
		migrationPathCombo.select(items.length-1);
		migrationPathCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				configuration.setMigrationPath(findMigrationPath(migrationPathCombo.getText()));
			}
		});
	}
	
	private String[] buildMigrationPaths() {
		List<String> paths = Lists.newArrayList();
		modelService.getModel().getMigrationPaths().stream().forEach(p -> paths.add(p.getName()));
		return paths.toArray(new String[paths.size()]);
	}
	
	private MigrationPath findMigrationPath(String name) {
		return modelService.getModel().getMigrationPaths().stream().filter(p -> { 
			return p.getName().equals(name);
		}).findFirst().get();
	}
	
	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy launchConfig) {
		initializeConfiguration(launchConfig);
	}
	
	@Override
	public void performApply(ILaunchConfigurationWorkingCopy launchConfig) {
		configuration.setName(launchConfig.getName());
	}

	@Override
	public String getName() {
		return inputTabName;
	}
	
	@Override
	public Image getImage() {
		return PlatformUI.getWorkbench().getSharedImages().getImage(IDE.SharedImages.IMG_OBJ_PROJECT);
	}

	@Override
	public boolean isValid(ILaunchConfiguration launchConfig) {
		setErrorMessage(null);
		setMessage(null);
		if (configuration.getInputs().isEmpty()) {
			setErrorMessage(inputProjectsDescription);
			return false;
		}
		return true;
	}
	
	@Override
	public void initializeFrom(ILaunchConfiguration launchConfig) {
		initializeConfiguration(launchConfig);
	}
	
	private void initializeConfiguration(ILaunchConfiguration launchConfig) {
		this.configuration = modelService.findConfiguration(launchConfig.getName());
		if (configuration == null) {
			this.configuration = modelService.createConfiguration(launchConfig.getName());
		}
		reload();
	}
}
