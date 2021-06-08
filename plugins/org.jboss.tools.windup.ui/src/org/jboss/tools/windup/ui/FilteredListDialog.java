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
package org.jboss.tools.windup.ui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.dialogs.FilteredList;
import org.eclipse.ui.dialogs.FilteredList.FilterMatcher;
import org.eclipse.ui.dialogs.SearchPattern;

/**
 * Dialog providing a filtered list.
 */
@SuppressWarnings("restriction")
public class FilteredListDialog extends ElementListSelectionDialog {

    private String filterText;
    private boolean editable;
    
    public FilteredListDialog(Shell parent, ILabelProvider renderer) {
        super(parent, renderer);
    }
    
    public FilteredListDialog(Shell parent, ILabelProvider renderer, boolean editable) {
        this(parent, renderer);
        this.editable = editable;
    }
    
    protected void handleEmptyList() {
        if (!editable) {
            super.handleEmptyList();
        }
    }
    
    @Override
    protected void updateOkState() {
        if (!editable) {
            super.updateOkState();
        }
        else {
            Button okButton = getOkButton();
            if (okButton != null) {
                okButton.setEnabled(true);
            }
        }
    }
    
     protected Text createFilterText(Composite parent) {
         Text text = super.createFilterText(parent);
         text.addListener(SWT.Modify, e -> filterText = text.getText().trim());
         return text;
     }
     
     public String getText() {
         return filterText;
     }
     
     @Override
    protected void updateButtonsEnableState(IStatus status) {
    } 

    @Override
    protected FilteredList createFilteredList(org.eclipse.swt.widgets.Composite parent) {
        FilteredList list = super.createFilteredList(parent);
        list.setFilterMatcher(new FilterMatcher() {
            private SearchPattern fMatcher;
            @Override
            public void setFilter(String pattern, boolean ignoreCase, boolean ignoreWildCards) {
                fMatcher = new SearchPattern();
                fMatcher.setPattern('*' + pattern + '*');
            }
            @Override
            public boolean match(Object element) {
                return fMatcher.matches(list.getLabelProvider().getText(element));
            }
        });
        return list;
    }
}
