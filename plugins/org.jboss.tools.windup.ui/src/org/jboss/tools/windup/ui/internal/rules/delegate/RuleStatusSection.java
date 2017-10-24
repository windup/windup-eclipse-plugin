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
package org.jboss.tools.windup.ui.internal.rules.delegate;

import javax.annotation.PostConstruct;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.mylyn.commons.workbench.forms.DatePicker;
import org.eclipse.mylyn.internal.tasks.ui.editors.EditorUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.jboss.tools.windup.ui.internal.RuleMessages;
import org.jboss.tools.windup.ui.internal.editor.ElementAttributesContainer;

@SuppressWarnings({"restriction"})
public class RuleStatusSection extends ElementAttributesContainer {
	
	private static final int CONTROL_WIDTH = 120;

	private Button statusCompleteButton;
	private Button statusIncompleteButton;
	
	private DatePicker createdDate;
	private DatePicker dueDate;
	private DatePicker completedDate;
	private Text assignedTo;
	
	@Override
	protected void bind() {
		
	}
	
	@PostConstruct
	private void createControls(Composite parent) {
		Composite container = super.createSection(parent, 2, toolkit, element, ExpandableComposite.TITLE_BAR|Section.EXPANDED, RuleMessages.TaskPlanning_TaskDetails, "");
		
		createLabel(container, toolkit, RuleMessages.TaskPlanning_Status, 0);
		Composite statusDetails = toolkit.createComposite(container);
		GridLayoutFactory.fillDefaults().numColumns(2).applyTo(statusDetails);		
		
		statusIncompleteButton = toolkit.createButton(statusDetails, RuleMessages.TaskPlanning_Incomplete, SWT.RADIO);
		statusIncompleteButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (statusIncompleteButton.getSelection()) {
					statusCompleteButton.setSelection(false);
				}
			}
		});
		statusCompleteButton = toolkit.createButton(statusDetails, RuleMessages.TaskPlanning_Complete, SWT.RADIO);
		statusCompleteButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (statusCompleteButton.getSelection()) {
					statusIncompleteButton.setSelection(false);
				}
			}
		});

		createdDate = createDatePicker(toolkit, container, RuleMessages.TaskPlanning_Created);
		createdDate.addPickerSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
			}
		});

		dueDate = createDatePicker(toolkit, container, RuleMessages.TaskPlanning_Due);
		dueDate.addPickerSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
			}
		});
		
		completedDate = createDatePicker(toolkit, container, RuleMessages.TaskPlanning_Completed);
		completedDate.addPickerSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
			}
		});
		
		Label label = toolkit.createLabel(container, RuleMessages.TaskPlanning_AssgnedTo);
		label.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
		assignedTo = toolkit.createText(container, "");
		GridDataFactory.fillDefaults().hint(150, SWT.DEFAULT).applyTo(assignedTo);
		assignedTo.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
			}
		});

		toolkit.paintBordersFor(container);
	}
	
	private Label createLabel(Composite composite, FormToolkit toolkit, String label, int indent) {
		Label labelControl = toolkit.createLabel(composite, label);
		labelControl.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
		GridDataFactory.defaultsFor(labelControl).indent(indent, 0).applyTo(labelControl);
		return labelControl;
	}
	
	private static DatePicker createDatePicker(FormToolkit toolkit, Composite parent, String textLabel) {
		Label label = toolkit.createLabel(parent, textLabel);
		label.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));

		Composite composite = createComposite(parent, 1, toolkit);

		DatePicker datePicker = new DatePicker(composite, SWT.FLAT, DatePicker.LABEL_CHOOSE, true, 0);
		GridDataFactory.fillDefaults().hint(CONTROL_WIDTH, SWT.DEFAULT).applyTo(datePicker);
		datePicker.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		datePicker.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TREE_BORDER);
		/*if (getTask().getDueDate() != null) {
			Calendar calendar = TaskActivityUtil.getCalendar();
			calendar.setTime(getTask().getDueDate());
			createdDate.setDate(calendar);
		}*/
		toolkit.adapt(datePicker, false, false);
		toolkit.paintBordersFor(composite);
		return datePicker;
	}
	
	private static Composite createComposite(Composite parent, int col, FormToolkit toolkit) {
		Composite nameValueComp = toolkit.createComposite(parent);
		GridLayout layout = new GridLayout(3, false);
		layout.marginHeight = 3;
		nameValueComp.setLayout(layout);
		return nameValueComp;
	}
	
	private static Composite createBorder(Composite composite, final FormToolkit toolkit, boolean paintBorder) {
		// create composite to hold rounded border
		final Composite roundedBorder = toolkit.createComposite(composite);
		if (paintBorder) {
			roundedBorder.addPaintListener(new PaintListener() {
				public void paintControl(PaintEvent e) {
					e.gc.setForeground(toolkit.getColors().getBorderColor());
					Point size = roundedBorder.getSize();
					e.gc.drawRoundRectangle(0, 2, size.x - 1, size.y - 5, 5, 5);
				}
			});
			roundedBorder.setLayout(GridLayoutFactory.fillDefaults().margins(4, 6).create());
		} else {
			roundedBorder.setLayout(GridLayoutFactory.fillDefaults().margins(0, 6).create());
		}
		GridDataFactory.fillDefaults()
				.align(SWT.FILL, SWT.BEGINNING)
				.hint(EditorUtil.MAXIMUM_WIDTH, SWT.DEFAULT)
				.grab(true, false)
				.applyTo(roundedBorder);
		return roundedBorder;
	}
}
