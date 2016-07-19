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
package org.jboss.tools.windup.ui.internal.editor.launch;

import static org.jboss.tools.windup.ui.internal.Messages.inputDescription;
import static org.jboss.tools.windup.ui.internal.Messages.windupInput;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.internal.navigator.resources.ResourceToItemsMapper;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.navigator.CommonViewer;
import org.eclipse.ui.views.navigator.ResourceComparator;
import org.jboss.tools.windup.ui.internal.editor.AbstractSection;
import org.jboss.tools.windup.windup.Input;
import org.jboss.tools.windup.windup.WindupFactory;

import com.google.common.base.Objects;

/**
 * Section for configuring Windup's input options.
 */
public class InputSection extends AbstractSection {
	
	private CheckboxTreeViewer treeViewer;
	
	// this is temp code used for demo/prototyping purposes.
	protected void fillSection_(Composite parent) {
		section.setText(windupInput);
		section.setDescription(inputDescription);
		CommonViewer viewer = new CommonViewer("org.eclipse.ui.navigator.ProjectExplorer", 
				parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setMapper(new ResourceToItemsMapper(viewer));
		viewer.setInput(ResourcesPlugin.getWorkspace().getRoot());
		GridDataFactory.fillDefaults().grab(true, true).hint(SWT.DEFAULT, 200).applyTo(viewer.getTree());
	}

	@Override
	protected void fillSection(Composite parent) {
		section.setText(windupInput);
		section.setDescription(inputDescription);
		treeViewer = new CheckboxTreeViewer(toolkit.createTree(parent, SWT.CHECK));
		GridDataFactory.fillDefaults().grab(true, true).hint(SWT.DEFAULT, 200).applyTo(treeViewer.getTree());
		treeViewer.setComparator(new ResourceComparator(ResourceComparator.NAME));
		treeViewer.setContentProvider(new WorkbenchContentProvider());
		treeViewer.setLabelProvider( new WorkbenchLabelProvider());
		treeViewer.setInput(ResourcesPlugin.getWorkspace().getRoot());
		treeViewer.setAutoExpandLevel(0);
		treeViewer.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(final CheckStateChangedEvent event) {
				treeViewer.setSubtreeChecked(event.getElement(), event.getChecked());
				if (!event.getChecked()) {
					uncheckParent((IResource)event.getElement());
				}
				updateConfiguration();
				broker.post(UIEvents.REQUEST_ENABLEMENT_UPDATE_TOPIC, UIEvents.ALL_ELEMENT_ID);
			}
		});
		initCurrentSelection();
	}
	
	/**
	 * Selects the resources in the current configuration.
	 */
	private void initCurrentSelection() {
		for (Input input : configuration.getInputs()) {
			URI uri = URI.createURI(input.getUri());
			Path path = new Path(uri.toPlatformString(false));
			IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(path);
			treeViewer.setSubtreeChecked(resource, true);
		}
	}
	
	/**
	 * Unchecks the highest checked parent in the tree.
	 */
	private void uncheckParent(IResource resource) {
		IResource parent = getCheckedParent(resource);
		if (parent != null) {
			treeViewer.setSubtreeChecked(parent, false);
		}
	}
	
	/**
	 * @return the top-level checked parent of the specified resource.
	 */
	private IResource getCheckedParent(IResource resource) {
		IResource parent = null;
		while (resource.getParent() != null) {
			if (treeViewer.getChecked(resource.getParent())) {
				parent = resource.getParent();
			}
			resource = resource.getParent();
		}
		return parent;
	}
	
	/**
	 * Saves the selected resources within the current configuration.
	 */
	private void updateConfiguration() {
		configuration.getInputs().clear();
		for (Object selection : treeViewer.getCheckedElements()) {
			IResource resource = (IResource)selection;
			IResource checkedParent = getCheckedParent(resource);
			resource = checkedParent != null ? checkedParent : resource;
			URI uri = URI.createPlatformPluginURI(resource.getFullPath().toString(), false);
			boolean exists = configuration.getInputs().stream().anyMatch(input -> {
				return Objects.equal(input.getUri(), uri.toString());
			});
			if (!exists) {
				Input input = WindupFactory.eINSTANCE.createInput();
				input.setUri(uri.toString());
				configuration.getInputs().add(input);
			}
		}
	}
}
