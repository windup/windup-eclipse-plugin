
package org.jboss.tools.windup.ui.internal.rules.delegate;

import org.eclipse.jface.text.AbstractReusableInformationControlCreator;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlExtension3;
import org.eclipse.jface.util.Geometry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class StickyHoverManager extends InformationControlReplacer {

	/**
	 * Internal information control closer. Listens to several events issued by its subject control
	 * and closes the information control when necessary.
	 */
	class Closer implements IInformationControlCloser, ControlListener, MouseListener, KeyListener, FocusListener, Listener {
		//TODO: Catch 'Esc' key in fInformationControlToClose: Don't dispose, just hideInformationControl().
		// This would allow to reuse the information control also when the user explicitly closes it.

		//TODO: if subject control is a Scrollable, should add selection listeners to both scroll bars
		// (and remove the ViewPortListener, which only listens to vertical scrolling)

		/** The subject control. */
		private Control fSubjectControl;
		/** Indicates whether this closer is active. */
		private boolean fIsActive= false;
		/** The display. */
		private Display fDisplay;

		@Override
		public void setSubjectControl(Control control) {
			fSubjectControl= control;
		}

		@Override
		public void setInformationControl(IInformationControl control) {
			// NOTE: we use getCurrentInformationControl2() from the outer class
		}

		@Override
		public void start(Rectangle informationArea) {

			if (fIsActive)
				return;
			fIsActive= true;

			if (fSubjectControl != null && !fSubjectControl.isDisposed()) {
				fSubjectControl.addControlListener(this);
				fSubjectControl.addMouseListener(this);
				fSubjectControl.addKeyListener(this);
			}

			//fTextViewer.addViewportListener(this);

			IInformationControl fInformationControlToClose= getCurrentInformationControl2();
			if (fInformationControlToClose != null)
				fInformationControlToClose.addFocusListener(this);

			fDisplay= fSubjectControl.getDisplay();
			if (!fDisplay.isDisposed()) {
				fDisplay.addFilter(SWT.MouseMove, this);
				fDisplay.addFilter(SWT.FocusOut, this);
			}
		}

		@Override
		public void stop() {

			if (!fIsActive)
				return;
			fIsActive= false;

			if (fSubjectControl != null && !fSubjectControl.isDisposed()) {
				fSubjectControl.removeControlListener(this);
				fSubjectControl.removeMouseListener(this);
				fSubjectControl.removeKeyListener(this);
			}

			IInformationControl fInformationControlToClose= getCurrentInformationControl2();
			if (fInformationControlToClose != null)
				fInformationControlToClose.removeFocusListener(this);

			if (fDisplay != null && !fDisplay.isDisposed()) {
				fDisplay.removeFilter(SWT.MouseMove, this);
				fDisplay.removeFilter(SWT.FocusOut, this);
			}

			fDisplay= null;
		}

		 @Override
		public void controlResized(ControlEvent e) {
			 hideInformationControl();
		}

		 @Override
		public void controlMoved(ControlEvent e) {
			 hideInformationControl();
		}

		 @Override
		public void mouseDown(MouseEvent e) {
			 hideInformationControl();
		}

		@Override
		public void mouseUp(MouseEvent e) {
		}

		@Override
		public void mouseDoubleClick(MouseEvent e) {
			hideInformationControl();
		}

		@Override
		public void keyPressed(KeyEvent e) {
			hideInformationControl();
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}

		@Override
		public void focusGained(FocusEvent e) {
		}

		@Override
		public void focusLost(FocusEvent e) {
			if (DEBUG) System.out.println("StickyHoverManager.Closer.focusLost(): " + e); //$NON-NLS-1$
			Display d= fSubjectControl.getDisplay();
			d.asyncExec(new Runnable() {
				// Without the asyncExec, mouse clicks to the workbench window are swallowed.
				@Override
				public void run() {
					hideInformationControl();
				}
			});
		}

		@Override
		public void handleEvent(Event event) {
			if (event.type == SWT.MouseMove) {
				if (!(event.widget instanceof Control) || event.widget.isDisposed())
					return;

				IInformationControl infoControl= getCurrentInformationControl2();
				if (infoControl != null && !infoControl.isFocusControl() && infoControl instanceof IInformationControlExtension3) {
					IInformationControlExtension3 iControl3= (IInformationControlExtension3) infoControl;
					Rectangle controlBounds= iControl3.getBounds();
					if (controlBounds != null) {
						Point mouseLoc= event.display.map((Control) event.widget, null, event.x, event.y);
						int margin= getKeepUpMargin();
						Geometry.expand(controlBounds, margin, margin, margin, margin);
						if (!controlBounds.contains(mouseLoc)) {
							hideInformationControl();
						}
					}

				} else {
					/*
					 * TODO: need better understanding of why/if this is needed.
					 * Looks like the same panic code we have in org.eclipse.jface.text.AbstractHoverInformationControlManager.Closer.handleMouseMove(Event)
					 */
					if (fDisplay != null && !fDisplay.isDisposed())
						fDisplay.removeFilter(SWT.MouseMove, this);
				}

			} else if (event.type == SWT.FocusOut) {
				if (DEBUG) System.out.println("StickyHoverManager.Closer.handleEvent(): focusOut: " + event); //$NON-NLS-1$
				IInformationControl iControl= getCurrentInformationControl2();
				if (iControl != null && ! iControl.isFocusControl())
					hideInformationControl();
			}
		}
	}

	protected static class DefaultInformationControlCreator extends AbstractReusableInformationControlCreator {
		@Override
		public IInformationControl doCreateInformationControl(Shell shell) {
			return new DefaultInformationControl(shell, true);
		}
	}

	public StickyHoverManager(Control control) {
		super(new DefaultInformationControlCreator());
		setCloser(new Closer());
		install(control);
	}
}

