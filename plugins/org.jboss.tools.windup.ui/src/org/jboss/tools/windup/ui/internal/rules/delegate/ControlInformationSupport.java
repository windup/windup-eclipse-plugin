/*******************************************************************************
 * Copyright (c) 2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.rules.delegate;

import java.net.URL;
import java.util.Objects;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.internal.ui.actions.OpenBrowserUtil;
import org.eclipse.jdt.internal.ui.infoviews.JavadocView;
import org.eclipse.jdt.internal.ui.text.java.hover.JavadocBrowserInformationControlInput;
import org.eclipse.jdt.internal.ui.text.java.hover.JavadocHover;
import org.eclipse.jdt.internal.ui.text.java.hover.JavadocHover.FallbackInformationPresenter;
import org.eclipse.jdt.internal.ui.viewsupport.JavaElementLinks;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.internal.text.InformationControlReplacer;
import org.eclipse.jface.internal.text.InternalAccessor;
import org.eclipse.jface.internal.text.html.BrowserInformationControl;
import org.eclipse.jface.internal.text.html.BrowserInformationControlInput;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.AbstractHoverInformationControlManager;
import org.eclipse.jface.text.AbstractReusableInformationControlCreator;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IInformationControlExtension4;
import org.eclipse.jface.text.IInformationControlExtension5;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.contexts.IContextActivation;
import org.eclipse.ui.contexts.IContextService;
import org.jboss.tools.windup.model.domain.WindupConstants;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.RuleMessages;

@SuppressWarnings("restriction")
public class ControlInformationSupport {
	
	private static final int TEXT_HOVER_WIDTH_CHARS = 100; 
	private static final int TEXT_HOVER_HEIGHT_CHARS = 12;

	private static final int DEFAULT_WIDTH = 700;
	private static final int DEFAULT_HEIGHT = 209;
	
	public static final String INFORMATION = "informationData";
	
	protected static final String DEFAULT_FONT = JFaceResources.getFontRegistry().hasValueFor("org.eclipse.jdt.ui.javadocfont") ? "org.eclipse.jdt.ui.javadocfont" : JFaceResources.DIALOG_FONT;

	public ControlInformationSupport(Control control) {
		new HoverInfoControlManager(control);
	}
	
	public static DisplayEventHandler DISPLAY_EVENT_HANDLER = new DisplayEventHandler();
	
	public static class DisplayEventHandler {
		
		interface DisplayController {
			void keyPressed();
			Control getSubjectControl();
		}
		
		private Control subjectControl;
		private Display display; 
		private DisplayController controller;
		
		private boolean isActive = false;
		
		public void start(DisplayController controller) {
			if (isActive && Objects.equals(this.controller, controller)) {
				return;
			}
			
			Assert.isTrue(!isActive);
			Assert.isTrue(subjectControl == null);
			Assert.isTrue(this.controller == null);
			
			isActive = true;
			this.subjectControl = controller.getSubjectControl();
			this.display = subjectControl.getDisplay();
			this.controller = controller;
			
			activate();
		}
		
		private void activate() {
			subjectControl.addDisposeListener(subjectControlDisposeListener);
			display.addFilter(SWT.KeyDown, displayKeyListener);
		}
		
		public void stop(DisplayController controller) {
			if (!isActive || !(Objects.equals(this.controller, controller))) {
				return;
			}
			deactivate();
		}
		
		private void deactivate() {
			removeListeners();
			isActive = false;
			subjectControl = null;
			display = null;
			controller = null;
		}
		
		
		private void removeListeners() {
			if (!subjectControl.isDisposed()) {
				subjectControl.removeDisposeListener(subjectControlDisposeListener);
			}
			if (!display.isDisposed()) {
				display.removeFilter(SWT.KeyDown, displayKeyListener);
			}
		}
		
		private DisposeListener subjectControlDisposeListener = new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				deactivate();
			}
		};
		
		private Listener displayKeyListener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.type == SWT.KeyDown) {
					if (!(event.widget instanceof Control) || event.widget.isDisposed()) {
						return;
					}
					controller.keyPressed();
				}
			}
		};			
	}
	
	public static class HoverInfoControlManager extends AbstractHoverInformationControlManager implements DisplayEventHandler.DisplayController {
		
		private IContextService contextService = (IContextService)PlatformUI.getWorkbench()
				.getService(IContextService.class);
		
		private IContextActivation contextActivation;
		
		public HoverInfoControlManager(Control control) {
			super(createControlCreator());
			setSizeConstraints(TEXT_HOVER_WIDTH_CHARS, TEXT_HOVER_HEIGHT_CHARS, false, true);
			getInternalAccessor().setInformationControlReplacer(new StickyHoverManager(control, this));
			setAnchor(ANCHOR_BOTTOM);
			setFallbackAnchors(new Anchor[] {ANCHOR_BOTTOM, ANCHOR_RIGHT, ANCHOR_LEFT});
			install(control, control.getShell());
			control.addMouseTrackListener(new MouseTrackListener() {
				@Override
				public void mouseHover(MouseEvent e) {
					// stupid MouseTracker doesn't get deactivated once replacement takes hover and mouse e
					IInformationControl iControl = getInformationControl();
					InternalAccessor accessor = getInternalAccessor();
					InformationControlReplacer replacer = accessor.getInformationControlReplacer();
					if (replacer.getCurrentInformationControl2() != null && !((IInformationControlExtension5)replacer.getCurrentInformationControl2()).isVisible()) {
						if (iControl != null && iControl instanceof IInformationControlExtension5) {
							if (!((IInformationControlExtension5)iControl).isVisible() && Display.getCurrent().getActiveShell() == control.getShell()) {
								showInformation();
							}
						}					
					}
				}
				@Override
				public void mouseExit(MouseEvent e) {
				}
				@Override
				public void mouseEnter(MouseEvent e) {
				}
			});
		}
		
		private static IInformationControlCreator createControlCreator() {
			return new IInformationControlCreator() {
				@Override
				public IInformationControl createInformationControl(Shell parent) {
					return new DefaultInformationControl(parent, false);
				}
			};
		}
		
		/*
		 * (non-Javadoc)
		 * @see org.jboss.tools.windup.ui.internal.rules.delegate.ControlInformationSupport.DisplayEventHandler.DisplayController#getControl()
		 */
		@Override
		public Control getSubjectControl() {
			return super.getSubjectControl();
		}
		
		/*
		 * (non-Javadoc)
		 * @see org.jboss.tools.windup.ui.internal.rules.delegate.ControlInformationSupport.DisplayEventHandler.DisplayController#keyPressed()
		 */
		@Override
		public void keyPressed() {
			hideInformationControl();
		}
		
		private BrowserInformationControlInput createInput(Control control) {
			Object data = control.getData(INFORMATION);
			if (data != null) {
				return new BrowserInformationControlInput(null) {
					@Override
					public String getInputName() {
						return control.toString();
					}
					@Override
					public Object getInputElement() {
						return control;
					}
					@Override
					public String getHtml() {
						return (String)data;
					}
				};
			}
			return null;
		}
		
		private Object getData(Widget widget) {
			return widget.getData(INFORMATION);
		}
		
		@Override
		protected void computeInformation() {
			Widget widget = getHoverEvent().widget;
			if (getData(widget) == null) {
				setInformation(null, null);
			}
			else {
				Control control = (Control)widget;
				super.setCustomInformationControlCreator(getHoverControlCreator());
				Rectangle bounds = control.getBounds();
				Rectangle area = new Rectangle(0, 0, bounds.width, bounds.height);
	    			setInformation(createInput(control), area);
			}
		}
		
		public void setFocus() {
			InternalAccessor accessor = getInternalAccessor();
			IInformationControl iControl = accessor.getCurrentInformationControl();
			if (accessor.canReplace(iControl)) {
				accessor.replaceInformationControl(true);
			}
			deactivateContext();
		}
		
		@Override
		protected void showInformationControl(Rectangle subjectArea) {
			super.showInformationControl(subjectArea);
			InternalAccessor accessor = getInternalAccessor();
			IInformationControl iControl = accessor.getCurrentInformationControl();
			if (iControl != null && fInformationControlCloser != null) {
				DISPLAY_EVENT_HANDLER.start(this);
			}
		}
		
		@Override
		protected void hideInformationControl() {
			super.hideInformationControl();
			InternalAccessor accessor = getInternalAccessor();
			IInformationControl iControl = accessor.getCurrentInformationControl();
			if (iControl != null && fInformationControl != null) {
				DISPLAY_EVENT_HANDLER.stop(this);
			}
		}
		
		@Override
		protected void handleInformationControlDisposed() {
			super.handleInformationControlDisposed();
			DISPLAY_EVENT_HANDLER.stop(this);
		}
		
		private void activateContext() {
			WindupUIPlugin.getDefault().getContext().set(HoverInfoControlManager.class, this);
			contextActivation = contextService.activateContext(WindupConstants.RULESET_EDITOR_CONTEXT);
		}
		
		private void deactivateContext() {
			WindupUIPlugin.getDefault().getContext().set(HoverInfoControlManager.class, null);
			contextService.deactivateContext(contextActivation);
		}
		
		private PresenterControlCreator fPresenterControlCreator;
		
		public IInformationControlCreator getInformationPresenterControlCreator() {
			if (fPresenterControlCreator == null)
				fPresenterControlCreator= new PresenterControlCreator();
			return fPresenterControlCreator;
		}
		
		private HoverControlCreator fHoverControlCreator;
		
		public IInformationControlCreator getHoverControlCreator() {
			if (fHoverControlCreator == null)
				fHoverControlCreator= new HoverControlCreator(getInformationPresenterControlCreator(), this);
			return fHoverControlCreator;
		}
	}
	
	public static final class PresenterControlCreator extends AbstractReusableInformationControlCreator {

		@Override
		public IInformationControl doCreateInformationControl(Shell parent) {
			if (BrowserInformationControl.isAvailable(parent)) {
				ToolBarManager tbm = new ToolBarManager(SWT.FLAT);
				String font= PreferenceConstants.APPEARANCE_JAVADOC_FONT;
				BrowserInformationControl iControl= new BrowserInformationControl(parent, font, tbm);

				final OpenDeclarationAction openDeclarationAction= new OpenDeclarationAction(iControl);
				tbm.add(openDeclarationAction);

				tbm.update(true);

				addLinkListener(iControl);
				return iControl;

			} else {
				return new DefaultInformationControl(parent, true);
			}
		}
	}

	public static final class HoverControlCreator extends AbstractReusableInformationControlCreator {
		
		private final IInformationControlCreator fInformationPresenterControlCreator;
		private HoverInfoControlManager hoverManager;

		public HoverControlCreator(IInformationControlCreator informationPresenterControlCreator, HoverInfoControlManager hoverManager) {
			fInformationPresenterControlCreator= informationPresenterControlCreator;
			this.hoverManager = hoverManager;
		}

		private BrowserInformationControl iControl;
		
		@Override
		public boolean canReplace(IInformationControlCreator creator) {
			return super.canReplace(creator);
		}
		
		private String getTooltipAffordance() {
			return RuleMessages.Editor_toolTip_affordance;
		}
		
		@Override
		public IInformationControl doCreateInformationControl(Shell parent) {
			if (BrowserInformationControl.isAvailable(parent)) {
				String font= PreferenceConstants.APPEARANCE_JAVADOC_FONT;
				iControl= new BrowserInformationControl(parent, font, getTooltipAffordance()) {
					@Override
					public IInformationControlCreator getInformationPresenterControlCreator() {
						return fInformationPresenterControlCreator;
					}
					
					@Override
					public void setVisible(boolean visible) {
						super.setVisible(visible);
						if (visible) {
							hoverManager.activateContext();
						} else {
							hoverManager.deactivateContext();
						}
					}
					
					@Override
					public Point computeSizeHint() {
						return new Point(DEFAULT_WIDTH, DEFAULT_HEIGHT);
					}
				};
				
				setHoverColors();
				addLinkListener(iControl);
				return iControl;
			} else {
				return new DefaultInformationControl(parent, getTooltipAffordance()) {
					@Override
					public IInformationControlCreator getInformationPresenterControlCreator() {
						return new IInformationControlCreator() {
							@Override
							public IInformationControl createInformationControl(Shell parentShell) {
								return new DefaultInformationControl(parentShell, (ToolBarManager) null, new FallbackInformationPresenter());
							}
						};
					}
				};
			}
		}

		private void setHoverColors() {
			ColorRegistry registry = JFaceResources.getColorRegistry();
			Color fgRGB = registry.get("org.eclipse.jdt.ui.Javadoc.foregroundColor"); //$NON-NLS-1$ 
			Color bgRGB = registry.get("org.eclipse.jdt.ui.Javadoc.backgroundColor"); //$NON-NLS-1$ 
			iControl.setForegroundColor(fgRGB);
			iControl.setBackgroundColor(bgRGB);
		}

		@Override
		public boolean canReuse(IInformationControl control) {
			if (!super.canReuse(control))
				return false;
			if (control instanceof IInformationControlExtension4) {
				((IInformationControlExtension4)control).setStatusText(getTooltipAffordance());
			}
			return true;
		}
	}
	
	private static final class OpenDeclarationAction extends Action {
		private final BrowserInformationControl fInfoControl;

		public OpenDeclarationAction(BrowserInformationControl infoControl) {
			fInfoControl= infoControl;
			setText("Open Declaration");
			JavaPluginImages.setLocalImageDescriptors(this, "goto_input.png");
		}

		@Override
		public void run() {
			JavadocBrowserInformationControlInput infoInput= (JavadocBrowserInformationControlInput) fInfoControl.getInput();
			fInfoControl.notifyDelayedInputChange(null);
			fInfoControl.dispose();
			try {
				//FIXME: add hover location to editor navigation history?
				JavadocHover.openDeclaration(infoInput.getElement());
			} catch (PartInitException e) {
				JavaPlugin.log(e);
			} catch (JavaModelException e) {
				JavaPlugin.log(e);
			}
		}
	}
	
	
	private static void addLinkListener(final BrowserInformationControl control) {
		control.addLocationListener(JavaElementLinks.createLocationListener(new JavaElementLinks.ILinkHandler() {
			@Override
			public void handleJavadocViewLink(IJavaElement linkTarget) {
				control.notifyDelayedInputChange(null);
				control.setVisible(false);
				control.dispose();
				try {
					JavadocView view= (JavadocView) JavaPlugin.getActivePage().showView(JavaUI.ID_JAVADOC_VIEW);
					view.setInput(linkTarget);
				} catch (PartInitException e) {
					JavaPlugin.log(e);
				}
			}

			@Override
			public void handleInlineJavadocLink(IJavaElement linkTarget) {
				JavadocBrowserInformationControlInput hoverInfo = JavadocHover.getHoverInfo(new IJavaElement[] { linkTarget }, null, null, (JavadocBrowserInformationControlInput) control.getInput());
				if (control.hasDelayedInputChangeListener())
					control.notifyDelayedInputChange(hoverInfo);
				else
					control.setInput(hoverInfo);
			}

			@Override
			public void handleDeclarationLink(IJavaElement linkTarget) {
				control.notifyDelayedInputChange(null);
				control.dispose();
				// TODO: ME
				/*try {
					//FIXME: add hover location to editor navigation history?
					openDeclaration(linkTarget);
				} catch (PartInitException e) {
					JavaPlugin.log(e);
				} catch (JavaModelException e) {
					JavaPlugin.log(e);
				}*/
			}

			@Override
			public boolean handleExternalLink(URL url, Display display) {
				control.notifyDelayedInputChange(null);
				control.dispose();
				// Open attached Javadoc links
				OpenBrowserUtil.open(url, display);
				return true;
			}

			@Override
			public void handleTextSet() {
			}
		}));
	}
}