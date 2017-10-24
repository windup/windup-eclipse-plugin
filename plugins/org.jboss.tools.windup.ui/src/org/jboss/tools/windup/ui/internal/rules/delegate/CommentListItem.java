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

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.mylyn.internal.tasks.ui.editors.EditorUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.ui.internal.tabletree.TreeContentHelper;
import org.eclipse.xtext.util.Pair;
import org.eclipse.xtext.util.Tuples;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory;
import org.w3c.dom.Element;

@SuppressWarnings({"restriction"})
public class CommentListItem extends ListItem {

	public CommentListItem(Composite parent, FormToolkit toolkit, Element itemElement, TreeContentHelper contentHelper,
			ModelQuery modelQuery, IStructuredModel model, RulesetElementUiDelegateFactory uiDelegateFactory,
			IEclipseContext context) {
		super(parent, toolkit, itemElement, contentHelper, modelQuery, model, uiDelegateFactory, context);
	}

	@Override
	protected Pair<Composite, Composite> createListItemContainers(Composite parent) {
		Composite group = createBorder(parent, toolkit, true);
		group.setBackground(toolkit.getColors().getBackground());
		group.setForeground(toolkit.getColors().getBackground());
		GridDataFactory.fillDefaults().grab(true, false).applyTo(group);
		FormLayout layout = new FormLayout();
		layout.marginWidth = 4;
		layout.marginHeight = 6;
		group.setLayout(layout);
		
		Composite left = new Composite(group, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(2).applyTo(left);
		FormData leftData = new FormData();
		leftData.left = new FormAttachment(0);
		left.setLayoutData(leftData);
		
		Composite right = new Composite(group, SWT.NONE);
		GridLayoutFactory.fillDefaults().numColumns(1).applyTo(right);
		FormData rightData = new FormData();
		rightData.right = new FormAttachment(100);
		rightData.bottom = new FormAttachment(73);
		right.setLayoutData(rightData);
		
		leftData.right = new FormAttachment(right);
		
		return Tuples.create(left, right);
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
