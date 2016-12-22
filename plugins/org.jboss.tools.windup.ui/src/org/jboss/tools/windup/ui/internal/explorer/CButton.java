package org.jboss.tools.windup.ui.internal.explorer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TypedListener;

public class CButton extends Canvas {
	
	private boolean inButton;
	private Image hotImage;
	private Image coldImage;

	public CButton(Composite parent, int style) {
		super(parent, style);
		addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				if (CButton.this.inButton) {
					if (CButton.this.hotImage != null) {
						e.gc.drawImage(CButton.this.hotImage, 0, 0);
					} else if (CButton.this.coldImage != null) {
						e.gc.drawImage(CButton.this.coldImage, 0, 0);
					}
				} else if (CButton.this.coldImage != null) {
					e.gc.drawImage(CButton.this.coldImage, 0, 0);
				} else if (CButton.this.hotImage != null) {
					e.gc.drawImage(CButton.this.hotImage, 0, 0);
				}
			}
		});
		addMouseTrackListener(new MouseTrackAdapter() {
			public void mouseEnter(MouseEvent e) {
				CButton.this.setSelected(true);
			}

			public void mouseExit(MouseEvent e) {
				CButton.this.setSelected(false);
			}
		});
		addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				CButton.this.setSelected(true);
				CButton.this.handleSelection(CButton.this.inButton);
			}

			public void mouseDoubleClick(MouseEvent e) {
				CButton.this.setSelected(true);
				CButton.this.handleDefaultSelection(CButton.this.inButton);
			}
		});
	}

	public void dispose() {
		super.dispose();
		if (this.hotImage != null) {
			this.hotImage.dispose();
		}
		if (this.coldImage != null) {
			this.coldImage.dispose();
		}
	}

	public Point computeSize(int wHint, int hHint, boolean changed) {
		int width = 0;
		int height = 0;
		if (this.hotImage != null) {
			Rectangle bounds = this.hotImage.getBounds();
			width = bounds.width;
			height = bounds.height;
		}
		if (this.coldImage != null) {
			Rectangle bounds = this.coldImage.getBounds();
			if (bounds.width > width) {
				width = bounds.width;
			}
			if (bounds.height > height) {
				height = bounds.height;
			}
		}
		return new Point(width, height);
	}

	public void setHotImage(Image image) {
		this.hotImage = image;
	}

	public void setColdImage(Image image) {
		this.coldImage = image;
	}

	protected void handleSelection(boolean selection) {
		notifyListeners(SWT.Selection, null);
	}

	protected void handleDefaultSelection(boolean selection) {
		notifyListeners(SWT.DefaultSelection, null);
	}

	public void addSelectionListener(SelectionListener listener) {
		checkWidget();
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Selection, typedListener);
		addListener(SWT.DefaultSelection, typedListener);
	}

	public void setSelected(boolean sel) {
		this.inButton = sel;
		redraw();
	}

	public boolean isSelected() {
		return this.inButton;
	}
}
