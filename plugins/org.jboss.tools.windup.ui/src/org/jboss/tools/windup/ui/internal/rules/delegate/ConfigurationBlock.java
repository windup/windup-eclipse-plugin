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
package org.jboss.tools.windup.ui.internal.rules.delegate;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import com.google.common.base.Objects;

public class ConfigurationBlock {

	private static final int HIGHLIGHT_FOCUS = SWT.COLOR_WIDGET_DARK_SHADOW;
	private static final int HIGHLIGHT_MOUSE = SWT.COLOR_WIDGET_NORMAL_SHADOW;
	private static final int HIGHLIGHT_NONE = SWT.NONE;

	//////////////
	// Conrol/Text
	//////////////
	public static void addHighlight(final Composite parent, final Control labelControl, final Text text) {
		text.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				highlight(parent, labelControl, text, HIGHLIGHT_NONE);
			}
			@Override
			public void focusGained(FocusEvent e) {
				highlight(parent, labelControl, text, HIGHLIGHT_FOCUS);
			}
		});

		MouseTrackAdapter labelComboListener= new MouseTrackAdapter() {
			@Override
			public void mouseEnter(MouseEvent e) {
				highlight(parent, labelControl, text, text.isFocusControl() ? HIGHLIGHT_FOCUS : HIGHLIGHT_MOUSE);
			}
			@Override
			public void mouseExit(MouseEvent e) {
				if (! text.isFocusControl())
					highlight(parent, labelControl, text, HIGHLIGHT_NONE);
			}
		};
		text.addMouseTrackListener(labelComboListener);
		labelControl.addMouseTrackListener(labelComboListener);

		class MouseMoveTrackListener extends MouseTrackAdapter implements MouseMoveListener, MouseListener {
			@Override
			public void mouseExit(MouseEvent e) {
				if (! text.isFocusControl())
					highlight(parent, labelControl, text, HIGHLIGHT_NONE);
			}
			@Override
			public void mouseMove(MouseEvent e) {
				int color= text.isFocusControl() ? HIGHLIGHT_FOCUS : isAroundLabel(e) ? HIGHLIGHT_MOUSE : HIGHLIGHT_NONE;
				highlight(parent, labelControl, text, color);
			}
			@Override
			public void mouseDown(MouseEvent e) {
				if (isAroundLabel(e))
					text.setFocus();
			}
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// not used
			}
			@Override
			public void mouseUp(MouseEvent e) {
				// not used
			}
			private boolean isAroundLabel(MouseEvent e) {
				int lx= labelControl.getLocation().x;
				Rectangle c= text.getBounds();
				int x= e.x;
				int y= e.y;
				boolean isAroundLabel= lx - 5 < x && x < c.x && c.y - 2 < y && y < c.y + c.height + 2;
				return isAroundLabel;
			}
		}
		MouseMoveTrackListener parentListener= new MouseMoveTrackListener();
		parent.addMouseMoveListener(parentListener);
		parent.addMouseTrackListener(parentListener);
		parent.addMouseListener(parentListener);

		MouseAdapter labelClickListener= new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				text.setFocus();
			}
		};
		labelControl.addMouseListener(labelClickListener);
	}

	public static void highlight(final Composite parent, final Control labelControl, final Text text, final int color) {

		class HighlightPainter implements PaintListener {

			private int fColor= color;

			@Override
			public void paintControl(PaintEvent e) {
				if (((GridData) labelControl.getLayoutData()).exclude) {
					parent.removePaintListener(this);
					labelControl.setData(null);
					return;
				}

				int GAP= 7;
				int ARROW= 3;
				Rectangle l= labelControl.getBounds();
				Point c= text.getLocation();

				e.gc.setForeground(e.display.getSystemColor(fColor));
				int x2= c.x - GAP;
				int y= l.y + l.height / 2 + 1;

				e.gc.drawLine(l.x + l.width + GAP, y, x2, y);
				e.gc.drawLine(x2 - ARROW, y - ARROW, x2, y);
				e.gc.drawLine(x2 - ARROW, y + ARROW, x2, y);
			}
		}

		Object data= labelControl.getData();
		if (data == null) {
			if (color != HIGHLIGHT_NONE) {
				PaintListener painter= new HighlightPainter();
				parent.addPaintListener(painter);
				labelControl.setData(painter);
			} else {
				return;
			}
		} else {
			if (color == HIGHLIGHT_NONE) {
				parent.removePaintListener((PaintListener) data);
				labelControl.setData(null);
			} else if (color != ((HighlightPainter) data).fColor){
				((HighlightPainter) data).fColor= color;
			} else {
				return;
			}
		}

		parent.redraw();
		parent.update();
	}
	
	public static final String TOOLBAR_CONTROL = "toolbarControl";
	
	public static void addToolbarListener(Control subjectControl) {
		new ToolbarListener(subjectControl);
	}
	
	private static class ToolbarListener implements Listener {
		
		private Control subjectControl;
		
		public ToolbarListener(Control subjectControl) {
			this.subjectControl = subjectControl;
			this.subjectControl.addDisposeListener(new DisposeListener() {
				@Override
				public void widgetDisposed(DisposeEvent e) {
					unregister();
				}
			});
			Display.getDefault().addFilter(SWT.MouseMove, this);
		}
		
		private void unregister() {
			Display.getDefault().removeFilter(SWT.MouseMove, this);
		}
		
		@Override
		public void handleEvent(Event event) {
			switch (event.type) {
				case SWT.MouseMove:
					handleMouseMove(event);
					break;
			}
		}
		
		private void handleMouseMove(Event event) {
			if (!(event.widget instanceof Control))
				return;
	
			if (subjectControl.isDisposed()) {
				unregister();
			}
			
			if (!subjectControl.isVisible()) {
				return;
			}
	
			Control eventControl = (Control) event.widget;
			
			if (Objects.equal(eventControl, subjectControl)) {
				return;
			}
			
			ToolbarContainer currentToolbarContainer = (ToolbarContainer)subjectControl.getData(TOOLBAR_CONTROL);
			
			Object toolbarContainer = eventControl.getData(TOOLBAR_CONTROL);
			if (toolbarContainer != null) {
				if (Objects.equal(currentToolbarContainer, toolbarContainer)) {
					return;
				}
				else if (currentToolbarContainer != null) {
					currentToolbarContainer.update(false, true);
				}
				ToolbarContainer container = (ToolbarContainer)toolbarContainer;
				container.update(true, true);
				subjectControl.setData(TOOLBAR_CONTROL, container);
			}
			
			else if (currentToolbarContainer != null){
				Point mouseLoc = event.display.map(eventControl, subjectControl, event.x, event.y);
				Rectangle bounds = subjectControl.getBounds();
				bounds.add(new Rectangle(0, 0, 10, 10));
				if (!bounds.contains(mouseLoc)) {
					currentToolbarContainer.update(false, true);
					subjectControl.setData(TOOLBAR_CONTROL, null);
				}
			}
		}
	}
	
	public static class ToolbarContainer {

		private Composite layoutContainer;
		private Control container;
		private Control toolbar;
		private boolean toolbarVisible;
		
		public ToolbarContainer (Composite layoutContainer, Control container, Control toolbar) {
			this.layoutContainer = layoutContainer;
			this.container = container;
			this.toolbar = toolbar;
		}
		
		public Control getToolbarControl() {
			return toolbar;
		}
		
		public void update(boolean visible, boolean layout) {
			if (container.isDisposed()) {
				return;
			}
			if (!visible && !toolbarVisible) {
				return;
			}
			if (visible && toolbarVisible) {
				return;
			}
			toolbarVisible = visible;
			FormData data = (FormData)container.getLayoutData();
			
			toolbar.setVisible(visible);
			
			if (visible) {
				data.right = new FormAttachment(toolbar);
			}
			else {
				data.right = new FormAttachment(100);
			}
			if (layout) {
				layoutContainer.layout(true, true);
			}
		}
	}
}
