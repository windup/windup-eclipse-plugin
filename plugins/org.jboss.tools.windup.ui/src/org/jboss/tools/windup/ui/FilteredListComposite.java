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
package org.jboss.tools.windup.ui;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.FilteredList;

public class FilteredListComposite {
	
    private ILabelProvider renderer;
    private boolean ignoreCase = true;
    private boolean isMultipleSelection = true;
    private boolean matchEmptyString = true;
    private boolean allowDuplicates = true;
    private String filter = null;
    
    protected FilteredList filteredList;
    
    private Text filterText;
    
    public FilteredListComposite(Composite parent, ILabelProvider renderer) {
    	this.renderer = renderer;
    	createFilterText(parent);
    	createFilteredList(parent);
    }
    
    /**
     * Specifies if sorting and filtering is case sensitive.
     */
    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }
    
    /**
     * Specifies whether everything or nothing should be filtered on
     * empty filter string.
     */
    public void setMatchEmptyString(boolean matchEmptyString) {
        this.matchEmptyString = matchEmptyString;
    }

    /**
     * Specifies if multiple selection is allowed.
     */
    public void setMultipleSelection(boolean multipleSelection) {
        this.isMultipleSelection = multipleSelection;
    }

    /**
     * Specifies whether duplicate entries are displayed or not.
     */
    public void setAllowDuplicates(boolean allowDuplicates) {
        this.allowDuplicates = allowDuplicates;
    }
    
    public void setListElements(Object[] elements) {
        filteredList.setElements(elements);
		handleElementsChanged();
    }
    
	protected void handleElementsChanged() {
		boolean enabled = !filteredList.isEmpty();
		filteredList.setEnabled(enabled);
		filterText.setEnabled(enabled);
		filteredList.setEnabled(enabled);
	}

	public void setFilter(String filter) {
        if (filterText == null) {
			this.filter = filter;
		} else {
			this.filterText.setText(filter);
		}
    }
	
    protected void createFilteredList(Composite parent) {
        int flags = SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL
                | (isMultipleSelection ? SWT.MULTI : SWT.SINGLE);

        filteredList = new FilteredList(parent, flags, renderer,
                ignoreCase, allowDuplicates, matchEmptyString);

        GridDataFactory.fillDefaults().grab(true, true).applyTo(filteredList);
        filteredList.setFont(parent.getFont());
        filteredList.setFilter((filter == null ? "" : filter)); //$NON-NLS-1$

        filteredList.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		handleWidgetSelected();
        	}
		});
    }
    
    protected void createFilterText(Composite parent) {
        filterText = new Text(parent, SWT.BORDER);
        GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, GridData.BEGINNING).applyTo(filterText);
        filterText.setFont(parent.getFont());
        filterText.setText((filter == null ? "" : filter)); //$NON-NLS-1$

        filterText.addListener(SWT.Modify, new Listener() {
            @Override
			public void handleEvent(Event e) {
                filterChanged();
            }
        });

        filterText.addKeyListener(new KeyListener() {
            @Override
			public void keyPressed(KeyEvent e) {
                if (e.keyCode == SWT.ARROW_DOWN) {
					filteredList.setFocus();
				}
            }
            @Override
			public void keyReleased(KeyEvent e) {}
        });
    }
    
    protected void filterChanged() {
    	filteredList.setFilter(filterText.getText());
    }
    
    protected void handleWidgetSelected() {}
    
    public Object[] getSelection() {
    	return filteredList.getSelection();
    }
}
