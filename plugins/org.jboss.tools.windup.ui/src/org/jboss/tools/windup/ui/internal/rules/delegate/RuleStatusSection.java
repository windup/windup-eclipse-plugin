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
import org.eclipse.mylyn.internal.tasks.ui.editors.EditorUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jboss.tools.windup.ui.internal.RuleMessages;
import org.jboss.tools.windup.ui.internal.editor.ElementAttributesContainer;

@SuppressWarnings({"restriction"})
public class RuleStatusSection extends ElementAttributesContainer {

	private Button statusCompleteButton;
	private Button statusIncompleteButton;
	private Text creationDateText;
	private Text completionDateText;
	
	private Composite container;
	
	public void setEnabled(boolean enabled) {
		container.setEnabled(enabled);
	}
	
	@PostConstruct
	private void createControls(Composite parent) {
		this.container = toolkit.createComposite(parent);
		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		container.setLayout(layout);

		createLabel(container, toolkit, RuleMessages.TaskPlanning_Status, 0);
		statusIncompleteButton = toolkit.createButton(container, RuleMessages.TaskPlanning_Incomplete,
				SWT.RADIO);
		statusIncompleteButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (statusIncompleteButton.getSelection()) {
					statusCompleteButton.setSelection(false);
					//markDirty(statusCompleteButton);
				}
			}
		});
		statusCompleteButton = toolkit.createButton(container, RuleMessages.TaskPlanning_Complete, SWT.RADIO);
		statusCompleteButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (statusCompleteButton.getSelection()) {
					statusIncompleteButton.setSelection(false);
					//markDirty(statusCompleteButton);
				}
			}
		});

		createLabel(container, toolkit, RuleMessages.TaskPlanning_Created, EditorUtil.HEADER_COLUMN_MARGIN);
		// do not use toolkit.createText() to avoid border on Windows
		creationDateText = new Text(container, SWT.FLAT | SWT.READ_ONLY);
		toolkit.adapt(creationDateText, false, false);
		creationDateText.setData(FormToolkit.KEY_DRAW_BORDER, Boolean.FALSE);

		createLabel(container, toolkit, RuleMessages.TaskPlanning_Completed, EditorUtil.HEADER_COLUMN_MARGIN);
		// do not use toolkit.createText() to avoid border on Windows
		completionDateText = new Text(container, SWT.FLAT | SWT.READ_ONLY);
		toolkit.adapt(completionDateText, false, false);
		completionDateText.setData(FormToolkit.KEY_DRAW_BORDER, Boolean.FALSE);

		// ensure layout does not wrap
		layout.numColumns = container.getChildren().length;
		toolkit.paintBordersFor(container);
	}
	
	private Label createLabel(Composite composite, FormToolkit toolkit, String label, int indent) {
		Label labelControl = toolkit.createLabel(composite, label);
		labelControl.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
		GridDataFactory.defaultsFor(labelControl).indent(indent, 0).applyTo(labelControl);
		return labelControl;
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
