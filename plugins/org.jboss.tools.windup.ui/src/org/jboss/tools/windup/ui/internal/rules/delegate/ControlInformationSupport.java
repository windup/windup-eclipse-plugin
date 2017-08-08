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
import org.eclipse.jdt.internal.ui.actions.SimpleSelectionProvider;
import org.eclipse.jdt.internal.ui.infoviews.JavadocView;
import org.eclipse.jdt.internal.ui.text.java.hover.JavadocBrowserInformationControlInput;
import org.eclipse.jdt.internal.ui.text.java.hover.JavadocHover;
import org.eclipse.jdt.internal.ui.text.java.hover.JavadocHover.FallbackInformationPresenter;
import org.eclipse.jdt.internal.ui.viewsupport.JavaElementLinks;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jdt.ui.PreferenceConstants;
import org.eclipse.jdt.ui.actions.OpenAttachedJavadocAction;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.internal.text.html.BrowserInformationControl;
import org.eclipse.jface.internal.text.html.BrowserInformationControlInput;
import org.eclipse.jface.internal.text.html.BrowserInput;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.AbstractReusableInformationControlCreator;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IInformationControlExtension4;
import org.eclipse.jface.text.IInputChangedListener;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.EditorsUI;

@SuppressWarnings("restriction")
public class ControlInformationSupport {
	
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
	
	public class HoverInfoControlManager extends AbstractHoverInformationControlManager {
		 
		public HoverInfoControlManager(IInformationControlCreator creator, Control control) {
			super(creator);
			getInternalAccessor().setInformationControlReplacer(new StickyHoverManager(control));
			//replacer.takesFocusWhenVisible(true);
			//getInternalAccessor().setInformationControlReplacer(replacer);
			//InformationControlReplacer replacer = new InformationControlReplacer(replacementInformationControlCreator);
			//replacer.takesFocusWhenVisible(true);
			//getInternalAccessor().setInformationControlReplacer(replacer);
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
	    			setInformation(createInput(label), label.getBounds());
			}
		}
	}
	
	/**
	 * Action to go back to the previous input in the hover control.
	 *
	 * @since 3.4
	 */
	private static final class BackAction extends Action {
		private final BrowserInformationControl fInfoControl;

		public BackAction(BrowserInformationControl infoControl) {
			fInfoControl= infoControl;
			setText("Back");
			ISharedImages images= PlatformUI.getWorkbench().getSharedImages();
			setImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_BACK));
			setDisabledImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_BACK_DISABLED));

			update();
		}

		@Override
		public void run() {
			BrowserInformationControlInput previous= (BrowserInformationControlInput) fInfoControl.getInput().getPrevious();
			if (previous != null) {
				fInfoControl.setInput(previous);
			}
		}

		public void update() {
			BrowserInformationControlInput current= fInfoControl.getInput();

			if (current != null && current.getPrevious() != null) {
				BrowserInput previous= current.getPrevious();
				setToolTipText("Back");
				setEnabled(true);
			} else {
				setToolTipText("Back");
				setEnabled(false);
			}
		}
	}

	/**
	 * Action to go forward to the next input in the hover control.
	 *
	 * @since 3.4
	 */
	private static final class ForwardAction extends Action {
		private final BrowserInformationControl fInfoControl;

		public ForwardAction(BrowserInformationControl infoControl) {
			fInfoControl= infoControl;
			setText("Forward");
			ISharedImages images= PlatformUI.getWorkbench().getSharedImages();
			setImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_FORWARD));
			setDisabledImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_FORWARD_DISABLED));

			update();
		}

		@Override
		public void run() {
			BrowserInformationControlInput next= (BrowserInformationControlInput) fInfoControl.getInput().getNext();
			if (next != null) {
				fInfoControl.setInput(next);
			}
		}

		public void update() {
			BrowserInformationControlInput current= fInfoControl.getInput();

			if (current != null && current.getNext() != null) {
				setToolTipText("Next");
				setEnabled(true);
			} else {
				setToolTipText("Forward");
				setEnabled(false);
			}
		}
	}

	/**
	 * Action that shows the current hover contents in the Javadoc view.
	 *
	 * @since 3.4
	 */
	private static final class ShowInJavadocViewAction extends Action {
		private final BrowserInformationControl fInfoControl;

		public ShowInJavadocViewAction(BrowserInformationControl infoControl) {
			fInfoControl= infoControl;
			setText("Show in JavaDoc");
			setImageDescriptor(JavaPluginImages.DESC_OBJS_JAVADOCTAG); //TODO: better image
		}

		/*
		 * @see org.eclipse.jface.action.Action#run()
		 */
		@Override
		public void run() {
			JavadocBrowserInformationControlInput infoInput= (JavadocBrowserInformationControlInput) fInfoControl.getInput(); //TODO: check cast
			fInfoControl.notifyDelayedInputChange(null);
			fInfoControl.dispose(); //FIXME: should have protocol to hide, rather than dispose
			try {
				JavadocView view= (JavadocView) JavaPlugin.getActivePage().showView(JavaUI.ID_JAVADOC_VIEW);
				view.setInput(infoInput);
			} catch (PartInitException e) {
				JavaPlugin.log(e);
			}
		}
	}

	/**
	 * Action that opens the current hover input element.
	 *
	 * @since 3.4
	 */
	private static final class OpenDeclarationAction extends Action {
		private final BrowserInformationControl fInfoControl;

		public OpenDeclarationAction(BrowserInformationControl infoControl) {
			fInfoControl= infoControl;
			setText("Open Declaration");
			JavaPluginImages.setLocalImageDescriptors(this, "goto_input.png"); //$NON-NLS-1$ //TODO: better images
		}

		/*
		 * @see org.eclipse.jface.action.Action#run()
		 */
		@Override
		public void run() {
			JavadocBrowserInformationControlInput infoInput= (JavadocBrowserInformationControlInput) fInfoControl.getInput(); //TODO: check cast
			fInfoControl.notifyDelayedInputChange(null);
			fInfoControl.dispose(); //FIXME: should have protocol to hide, rather than dispose

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
	
	
	/**
	 * Presenter control creator.
	 *
	 * @since 3.3
	 */
	public static final class PresenterControlCreator extends AbstractReusableInformationControlCreator {

		private final IWorkbenchSite fSite;

		/**
		 * Creates a new PresenterControlCreator.
		 * 
		 * @param site the site or <code>null</code> if none
		 * @since 3.6
		 */
		public PresenterControlCreator(IWorkbenchSite site) {
			fSite= site;
		}

		/*
		 * @see org.eclipse.jdt.internal.ui.text.java.hover.AbstractReusableInformationControlCreator#doCreateInformationControl(org.eclipse.swt.widgets.Shell)
		 */
		@Override
		public IInformationControl doCreateInformationControl(Shell parent) {
			if (BrowserInformationControl.isAvailable(parent)) {
				ToolBarManager tbm= new ToolBarManager(SWT.FLAT);
				String font= PreferenceConstants.APPEARANCE_JAVADOC_FONT;
				BrowserInformationControl iControl= new BrowserInformationControl(parent, font, tbm) {
					@Override
					public boolean containsControl(Control control) {
						// TODO Auto-generated method stub
						return super.containsControl(control);
					}
				};

				final BackAction backAction= new BackAction(iControl);
				backAction.setEnabled(false);
				tbm.add(backAction);
				final ForwardAction forwardAction= new ForwardAction(iControl);
				tbm.add(forwardAction);
				forwardAction.setEnabled(false);

				final ShowInJavadocViewAction showInJavadocViewAction= new ShowInJavadocViewAction(iControl);
				tbm.add(showInJavadocViewAction);
				final OpenDeclarationAction openDeclarationAction= new OpenDeclarationAction(iControl);
				tbm.add(openDeclarationAction);

				final SimpleSelectionProvider selectionProvider= new SimpleSelectionProvider();
				if (fSite != null) {
					OpenAttachedJavadocAction openAttachedJavadocAction= new OpenAttachedJavadocAction(fSite);
					openAttachedJavadocAction.setSpecialSelectionProvider(selectionProvider);
					openAttachedJavadocAction.setImageDescriptor(JavaPluginImages.DESC_ELCL_OPEN_BROWSER);
					openAttachedJavadocAction.setDisabledImageDescriptor(JavaPluginImages.DESC_DLCL_OPEN_BROWSER);
					selectionProvider.addSelectionChangedListener(openAttachedJavadocAction);
					selectionProvider.setSelection(new StructuredSelection());
					tbm.add(openAttachedJavadocAction);
				}

				IInputChangedListener inputChangeListener= new IInputChangedListener() {
					@Override
					public void inputChanged(Object newInput) {
						backAction.update();
						forwardAction.update();
						if (newInput == null) {
							selectionProvider.setSelection(new StructuredSelection());
						} else if (newInput instanceof BrowserInformationControlInput) {
							BrowserInformationControlInput input= (BrowserInformationControlInput) newInput;
							Object inputElement= input.getInputElement();
							selectionProvider.setSelection(new StructuredSelection(inputElement));
							boolean isJavaElementInput= inputElement instanceof IJavaElement;
							showInJavadocViewAction.setEnabled(isJavaElementInput);
							openDeclarationAction.setEnabled(isJavaElementInput);
						}
					}
				};
				iControl.addInputChangeListener(inputChangeListener);

				tbm.update(true);

				addLinkListener(iControl);
				return iControl;

			} else {
				return new DefaultInformationControl(parent, true);
			}
		}
	}
	
	private PresenterControlCreator fPresenterControlCreator;
	
	public IInformationControlCreator getInformationPresenterControlCreator() {
		if (fPresenterControlCreator == null)
			fPresenterControlCreator= new PresenterControlCreator(null);
		return fPresenterControlCreator;
	}
	
	private HoverControlCreator fHoverControlCreator;
	
	public IInformationControlCreator getHoverControlCreator() {
		if (fHoverControlCreator == null)
			fHoverControlCreator= new HoverControlCreator(getInformationPresenterControlCreator());
		return fHoverControlCreator;
	}

	/**
	 * Hover control creator.
	 *
	 * @since 3.3
	 */
	public static final class HoverControlCreator extends AbstractReusableInformationControlCreator implements IPropertyChangeListener {
		/**
		 * The information presenter control creator.
		 * @since 3.4
		 */
		private final IInformationControlCreator fInformationPresenterControlCreator;
		/**
		 * <code>true</code> to use the additional info affordance, <code>false</code> to use the hover affordance.
		 */
		private final boolean fAdditionalInfoAffordance;

		/**
		 * @param informationPresenterControlCreator control creator for enriched hover
		 * @since 3.4
		 */
		public HoverControlCreator(IInformationControlCreator informationPresenterControlCreator) {
			this(informationPresenterControlCreator, false);
		}

		/**
		 * @param informationPresenterControlCreator control creator for enriched hover
		 * @param additionalInfoAffordance <code>true</code> to use the additional info affordance, <code>false</code> to use the hover affordance
		 * @since 3.4
		 */
		public HoverControlCreator(IInformationControlCreator informationPresenterControlCreator, boolean additionalInfoAffordance) {
			fInformationPresenterControlCreator= informationPresenterControlCreator;
			fAdditionalInfoAffordance= additionalInfoAffordance;
		}

		private BrowserInformationControl iControl;
		
		@Override
		public boolean canReplace(IInformationControlCreator creator) {
			return super.canReplace(creator);
		}
		
		/*
		 * @see org.eclipse.jdt.internal.ui.text.java.hover.AbstractReusableInformationControlCreator#doCreateInformationControl(org.eclipse.swt.widgets.Shell)
		 */
		@Override
		public IInformationControl doCreateInformationControl(Shell parent) {
			String tooltipAffordanceString= fAdditionalInfoAffordance ? JavaPlugin.getAdditionalInfoAffordanceString() : EditorsUI.getTooltipAffordanceString();
			if (BrowserInformationControl.isAvailable(parent)) {
				String font= PreferenceConstants.APPEARANCE_JAVADOC_FONT;
				iControl= new BrowserInformationControl(parent, font, tooltipAffordanceString) {
					/*
					 * @see org.eclipse.jface.text.IInformationControlExtension5#getInformationPresenterControlCreator()
					 */
					@Override
					public IInformationControlCreator getInformationPresenterControlCreator() {
						return fInformationPresenterControlCreator;
					}
				};
				
				JFaceResources.getColorRegistry().addListener(this); // So propertyChange() method is triggered in context of IPropertyChangeListener
				setHoverColors();
				
				addLinkListener(iControl);
				return iControl;
			} else {
				return new DefaultInformationControl(parent, tooltipAffordanceString) {
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

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			String property= event.getProperty();
			if (iControl != null &&
					(property.equals("org.eclipse.jdt.ui.Javadoc.foregroundColor") //$NON-NLS-1$
							|| property.equals("org.eclipse.jdt.ui.Javadoc.backgroundColor"))) { //$NON-NLS-1$
				setHoverColors();
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
		public void widgetDisposed(DisposeEvent e) {
			super.widgetDisposed(e);
			//Called when active editor is closed.
			JFaceResources.getColorRegistry().removeListener(this);
		}

		/*
		 * @see org.eclipse.jdt.internal.ui.text.java.hover.AbstractReusableInformationControlCreator#canReuse(org.eclipse.jface.text.IInformationControl)
		 */
		@Override
		public boolean canReuse(IInformationControl control) {
			if (!super.canReuse(control))
				return false;

			if (control instanceof IInformationControlExtension4) {
				String tooltipAffordanceString= fAdditionalInfoAffordance ? JavaPlugin.getAdditionalInfoAffordanceString() : EditorsUI.getTooltipAffordanceString();
				((IInformationControlExtension4)control).setStatusText(tooltipAffordanceString);
			}

			return true;
		}
	}
	
	private static void addLinkListener(final BrowserInformationControl control) {
		control.addLocationListener(JavaElementLinks.createLocationListener(new JavaElementLinks.ILinkHandler() {
			@Override
			public void handleJavadocViewLink(IJavaElement linkTarget) {
				control.notifyDelayedInputChange(null);
				control.setVisible(false);
				control.dispose(); //FIXME: should have protocol to hide, rather than dispose
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
				control.dispose(); //FIXME: should have protocol to hide, rather than dispose
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
				control.dispose(); //FIXME: should have protocol to hide, rather than dispose

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