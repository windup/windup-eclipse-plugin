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
package org.jboss.tools.windup.ui.internal.explorer;

import java.io.File;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.windup.Issue;
import org.jboss.tools.windup.windup.QuickFix;

/**
 * Diff dialog for previewing Windup quick fixes.
 */
public class QuickFixDiffDialog extends DiffDialog {
	
	private TableViewer table;
	private Issue issue;
	private QuickFix quickfix;
	
	private QuickfixService quickfixService;
	
	private IResource left;
	private IMarker marker;
	
	public QuickFixDiffDialog(Shell shell, Issue issue, QuickfixService quickfixService) {
		super(shell);
		this.quickfix =  issue.getQuickFixes().get(0);
		this.issue = issue;
		this.quickfixService = quickfixService;
	}
	
	@Override
	protected IResource computeLeft() {
		marker = (IMarker)quickfix.getMarker();
		if (marker != null && marker.exists()) {
			left = marker.getResource();
			return left;
		}
		return null;
	}
	
	@Override
	protected IResource computeRight() {
		if (left != null) {
			return quickfixService.getQuickFixedResource(null, quickfix, marker);
		}
		return null;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		if (issue.getQuickFixes().size() == 1) {
			return super.createDialogArea(parent);
		}
		Composite container = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().margins(0, 0).applyTo(container);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(container);
		table = new TableViewer(container, SWT.BORDER|SWT.FULL_SELECTION|SWT.SINGLE|SWT.H_SCROLL|SWT.V_SCROLL);
		buildColumns();
		table.getTable().setHeaderVisible(true);
		table.getTable().setLinesVisible(true);
		table.setContentProvider(ArrayContentProvider.getInstance());
		table.setInput(issue.getQuickFixes());
		GridDataFactory.fillDefaults().grab(true, false).hint(SWT.DEFAULT, 100).applyTo(table.getTable());
		table.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				if (!event.getSelection().isEmpty()) {
					QuickFixDiffDialog.this.quickfix = (QuickFix)((StructuredSelection)event.getSelection()).getFirstElement();
					QuickFixDiffDialog.this.loadPreview();
				}
			}
		});
		Control control = super.doCreateDialogArea(container);
		table.setSelection(new StructuredSelection(issue.getQuickFixes().get(0)));			
		return control;
	}
	
	public List<QuickFix> getQuickfixes() {
		return issue.getQuickFixes();
	}
	
	private void buildColumns() {
		// Type Column
		TableViewerColumn column = new TableViewerColumn(table, SWT.NONE);
		column.getColumn().setWidth(200);
		column.getColumn().setText(Messages.ComparePreviewer_quickFixFile);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				QuickFix fix = (QuickFix)element;
				int start = fix.getFile().lastIndexOf(File.separator);
				return fix.getFile().substring(start+1, fix.getFile().length());
			}
		});
		// Text Column
		column = new TableViewerColumn(table, SWT.NONE);
		column.getColumn().setWidth(500);
		column.getColumn().setText(Messages.ComparePreviewer_quickFixText);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				QuickFix fix = (QuickFix)element;
				return fix.getName();
			}
		});
	}
}
