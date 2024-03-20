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

import static org.jboss.tools.windup.model.domain.ConfigurationResourceUtil.computePackages;
import static org.jboss.tools.windup.model.domain.ConfigurationResourceUtil.getCurrentPackages;
import static org.jboss.tools.windup.model.domain.WindupConstants.INPUT_CHANGED;
import static org.jboss.tools.windup.ui.internal.Messages.inputPackages;
import static org.jboss.tools.windup.ui.internal.Messages.inputPackagesDescription;

import java.util.Arrays;
import java.util.List;

import jakarta.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.jboss.tools.windup.ui.FilteredListDialog;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.editor.AbstractSection;

import com.google.common.collect.Lists;

/**
 * Section for configuring Windup's packages input option.
 */
public class InputPackagesSection extends AbstractSection {
	
	private TableViewer table;

	@Override
	protected void fillSection(Composite parent) {
		section.setDescription(inputPackagesDescription);
		createTable(parent);
		createButtonBar(parent);
	}
	
	private void createTable(Composite parent) {
		table = new TableViewer(parent, SWT.BORDER|SWT.MULTI);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(table.getTable());
		table.setLabelProvider(new WorkbenchLabelProvider());
		table.setContentProvider(new ResourceContentProvider());
		reloadTable();
	}
	
	private void reloadTable() {
		IPackageFragment[] packages = getCurrentPackages(configuration);
		table.setInput(packages);
		String label = packages.length > 0 ? inputPackages + " (" + packages.length + ")" : inputPackages;
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
						reloadTable();
					}
				}
			}
		});
		
		Button removeButton = toolkit.createButton(container, Messages.windupRemove, SWT.PUSH);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(removeButton);
		removeButton.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				ISelection selection = table.getSelection();
				if (!selection.isEmpty()) {
					StructuredSelection ss = (StructuredSelection)selection;
					modelService.removePackages(configuration, (List<IPackageFragment>)ss.toList());
					reloadTable();
				}
			}
		});
	}
	
	@Inject
	@Optional
	private void updateDetails(@UIEventTopic(INPUT_CHANGED) Boolean changed) {
		reloadTable();
	}
	
	public void focus() {
		modelService.synch(configuration);
		reloadTable();
	}
}
