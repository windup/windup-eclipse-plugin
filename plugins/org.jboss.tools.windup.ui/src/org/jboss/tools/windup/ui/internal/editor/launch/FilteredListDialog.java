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

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.dialogs.FilteredList;
import org.eclipse.ui.dialogs.FilteredList.FilterMatcher;
import org.eclipse.ui.internal.misc.StringMatcher;

/**
 * Dialog providing a filtered list.
 */
@SuppressWarnings("restriction")
public class FilteredListDialog extends ElementListSelectionDialog {
	
	public FilteredListDialog(Shell parent, ILabelProvider renderer) {
		super(parent, renderer);
	}

	@Override
	protected FilteredList createFilteredList(org.eclipse.swt.widgets.Composite parent) {
		FilteredList list = super.createFilteredList(parent);
		list.setFilterMatcher(new FilterMatcher() {
			private StringMatcher fMatcher;
			@Override
			public void setFilter(String pattern, boolean ignoreCase, boolean ignoreWildCards) {
				fMatcher = new StringMatcher('*' + pattern + '*', ignoreCase, ignoreWildCards);
			}
			@Override
			public boolean match(Object element) {
				return fMatcher.match(list.getLabelProvider().getText(element));
			}
		});
		return list;
	}
}
