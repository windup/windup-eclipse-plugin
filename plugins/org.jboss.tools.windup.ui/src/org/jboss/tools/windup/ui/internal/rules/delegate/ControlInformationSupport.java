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

import java.net.URL;

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
import org.eclipse.jface.internal.text.html.BrowserInformationControl;
import org.eclipse.jface.internal.text.html.BrowserInformationControlInput;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.AbstractReusableInformationControlCreator;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IInformationControlExtension4;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
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
		IInformationControlCreator creator = new IInformationControlCreator() {
			@Override
			public IInformationControl createInformationControl(Shell parent) {
				return new DefaultInformationControl(parent, false);
			}
		};
		HoverInfoControlManager manager = new HoverInfoControlManager(creator, control);
		manager.install(control, control.getShell());
	}
	
	public static class HoverInfoControlManager extends AbstractHoverInformationControlManager {
		
		private IContextService contextService = (IContextService)PlatformUI.getWorkbench()
				.getService(IContextService.class);
		
		private IContextActivation contextActivation;
		 
		public HoverInfoControlManager(IInformationControlCreator creator, Control control) {
			super(creator);
			setSizeConstraints(TEXT_HOVER_WIDTH_CHARS, TEXT_HOVER_HEIGHT_CHARS, false, true);
			getInternalAccessor().setInformationControlReplacer(new StickyHoverManager(control));
			setAnchor(ANCHOR_BOTTOM);
			setFallbackAnchors(new Anchor[] {ANCHOR_BOTTOM, ANCHOR_RIGHT, ANCHOR_LEFT} );
		}
		
		private BrowserInformationControlInput createInput(Label label) {
			Object data = label.getData(INFORMATION);
			if (data != null) {
				return new BrowserInformationControlInput(null) {
					@Override
					public String getInputName() {
						return label.getText();
					}
					@Override
					public Object getInputElement() {
						return label;
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
				Label label = (Label)widget;
				super.setCustomInformationControlCreator(getHoverControlCreator());
				Rectangle bounds = label.getBounds();
				Rectangle area = new Rectangle(0, 0, bounds.width, bounds.height);
	    			setInformation(createInput(label), area);
			}
		}
		
		public void hide() {
			
		}
		
		public void setFocus() {
			IInformationControl iControl= getCurrentInformationControl();
			if (canReplace(iControl)) {
				if (cancelReplacingDelay()) {
					replaceInformationControl(true);
				}
			}
			deactivateContext();
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
				ToolBarManager tbm= new ToolBarManager(SWT.FLAT);
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