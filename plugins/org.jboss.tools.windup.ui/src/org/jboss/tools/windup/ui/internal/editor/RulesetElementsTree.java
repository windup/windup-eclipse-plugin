/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.editor;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;

public class RulesetElementsTree extends FilteredTree {
	
	private static final int STYLE = SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL;

	private PatternFilter filter = null;

	public RulesetElementsTree(Composite parent, PatternFilter filter) {
		super(parent, STYLE, filter, true);
		this.filter = filter;
	}

	@Override
	protected TreeViewer doCreateTreeViewer(Composite cparent, int style) {
		treeViewer = new TreeViewer(cparent, style);
		treeViewer.setUseHashlookup(true); 
		createFilters();
		return treeViewer;
	}
	
	private void createFilters() {
		//treeViewer.addFilter(...);
	}

	@Override
	protected void createControl(Composite cparent, int treeStyle) {
		super.createControl(cparent, treeStyle);
		setBackground(cparent.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
	}

	@Override
	protected void init(int treeStyle, PatternFilter filter) {}

	public void createViewControl() {
		super.init(STYLE, filter);
	}

	@Override
	protected void textChanged() {
		String text = getFilterString();
		if(text.equals("")) { //$NON-NLS-1$
			getPatternFilter().setPattern(null);
			getViewer().refresh();
			return;
		}
		else if(text.equals(getInitialText())) {
			return;
		}
		super.textChanged();
	}

	@Override
	protected void updateToolbar(boolean visible) {
		super.updateToolbar(visible);
		// update filter count
	}
}

