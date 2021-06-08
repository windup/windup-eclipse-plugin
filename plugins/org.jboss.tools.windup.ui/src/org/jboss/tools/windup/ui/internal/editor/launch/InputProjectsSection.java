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
package org.jboss.tools.windup.ui.internal.editor.launch;

import static org.jboss.tools.windup.model.domain.ConfigurationResourceUtil.getCurrentProjects;
import static org.jboss.tools.windup.model.domain.WindupConstants.INPUT_CHANGED;
import static org.jboss.tools.windup.ui.internal.Messages.inputProjects;
import static org.jboss.tools.windup.ui.internal.Messages.inputProjectsDescription;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PreDestroy;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.internal.ui.viewsupport.JavaUILabelProvider;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.ListSelectionDialog;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.editor.AbstractSection;

import com.google.common.collect.Lists;

/**
 * Section for configuring Windup's project input option.
 */
@SuppressWarnings("restriction")
public class InputProjectsSection extends AbstractSection {
	
	private TableViewer table;
	
	private IResourceChangeListener listener = new IResourceChangeListener() {
		// TODO: This needs to be optimized. Wait until we're done prototyping.
		@Override
		public void resourceChanged(IResourceChangeEvent event) {
			if (table != null && !table.getTable().isDisposed()) {
				broker.post(INPUT_CHANGED, true);
				focus();
			}
		}
	};

	@Override
	protected void fillSection(Composite parent) {
		section.setDescription(inputProjectsDescription);
		createTable(parent);
		createButtonBar(parent);
		ResourcesPlugin.getWorkspace().addResourceChangeListener(listener);
	}
	
	private void createTable(Composite parent) {
		table = new TableViewer(parent, SWT.BORDER|SWT.MULTI);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(table.getTable());
		table.setLabelProvider(new WorkbenchLabelProvider());
		table.setContentProvider(new ResourceContentProvider());
		reloadTable();
	}
	
	private void reloadTable() {
		IProject[] projects = getCurrentProjects(configuration);
		table.setInput(projects);
		String label = projects.length > 0 ? inputProjects + " (" + projects.length + ")" : inputProjects;
		section.setText(label);
	}
	
	private void createButtonBar(Composite parent) {
		Composite container = toolkit.createComposite(parent);
		GridLayoutFactory.fillDefaults().applyTo(container);
		GridDataFactory.fillDefaults().grab(false, true).applyTo(container);
		
		Button addButton = toolkit.createButton(container, Messages.windupAdd, SWT.PUSH);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(addButton);
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				List<IProject> projects = Lists.newArrayList(ResourcesPlugin.getWorkspace().getRoot().getProjects());
				projects.removeAll(Lists.newArrayList(getCurrentProjects(configuration)));
				ListSelectionDialog dialog = new ListSelectionDialog(parent.getShell(), projects, new ArrayContentProvider(), 
						new JavaUILabelProvider(), Messages.windupProjects);
				dialog.setTitle(Messages.windupProjects);
				dialog.setHelpAvailable(false);
				if (dialog.open() == Window.OK) {
					Object[] selected = (Object[])dialog.getResult();
					if (selected.length > 0) {
						List<String> newProjects = Lists.newArrayList();
						Arrays.stream(selected).forEach(p -> newProjects.add(((IProject)p).getLocation().toString()));
						modelService.createInput(configuration, newProjects);
						reloadTable();
					}
				}
			}
		});
		
		Button removeButton = toolkit.createButton(container, Messages.windupRemove, SWT.PUSH);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(removeButton);
		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ISelection selection = table.getSelection();
				if (!selection.isEmpty()) {
					//StructuredSelection ss = (StructuredSelection)selection;
					//List<IProject> projects = (List<IProject>)ss.toList();
					//modelService.deleteInput(configuration, projects);
					broker.post(INPUT_CHANGED, true);
					reloadTable();
				}
			}
		});
	}
	
	public void focus() {
		modelService.synch(configuration);
		reloadTable();
	}
	
	@PreDestroy
	private void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(listener);
	}
}
