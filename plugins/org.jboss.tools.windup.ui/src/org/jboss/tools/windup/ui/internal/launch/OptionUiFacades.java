/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.launch;

import java.io.File;
import java.nio.file.Path;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.windup.model.Facades.IFacade;
import org.jboss.tools.windup.model.OptionFacades.OptionTypeFacade;
import org.jboss.tools.windup.model.OptionFacades.OptionsFacadeManager;
import org.jboss.tools.windup.model.OptionFacades.Type;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.windup.bootstrap.help.OptionDescription;

/**
 * GUI facades for options. 
 */
public class OptionUiFacades {
	
	public static interface OptionUiFacade extends IFacade {
		void createControls(Composite parent);
		boolean isValid();
		Control getControl();
		void setFocus();
		OptionDescription getOptionDescription();
		String getValue();
	}
	
	private static abstract class AbstractOptionUiFacade<T> implements OptionUiFacade {
		
		@SuppressWarnings("unused")
		protected OptionTypeFacade<T> optionTypeFacade;
		protected OptionDescription option;
		
		protected Control control;
		protected Runnable optionChangedCallback;
		
		private String value = "";
		
		
		// For Alpha, just a simple text field.
		protected Text textWidget;
		
		public AbstractOptionUiFacade(OptionTypeFacade<T> optionTypeFacade, Runnable optionChangedCallback) {
			this.optionTypeFacade = optionTypeFacade;
			this.optionChangedCallback = optionChangedCallback;
			this.option = optionTypeFacade.getOptionDescription();
		}
		
		@Override
		public void createControls(Composite parent) {
			Composite control = new Composite(parent, SWT.NONE);
			GridLayoutFactory.fillDefaults().numColumns(2).applyTo(control);
			Label valueLabel = new Label(control, SWT.NONE);
			valueLabel.setText(Messages.VALUE+":");
			valueLabel.setFont(parent.getFont());
			GridDataFactory.fillDefaults().hint(50, SWT.DEFAULT).align(SWT.RIGHT, SWT.CENTER).applyTo(valueLabel);
			createInputControl(control);
			this.control = control;
		}
		
		@Override
		public void setFocus() {
			value = "";
			textWidget.setText("");
			textWidget.setFocus();
		}
		
		protected void createInputControl(Composite parent) {
			textWidget = new Text(parent, SWT.BORDER | SWT.SINGLE);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(textWidget);
			
			textWidget.setFont(parent.getFont());
			textWidget.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					AbstractOptionUiFacade.this.value = textWidget.getText().trim();
					optionChangedCallback.run();
				}
			});
		}
		
		@Override
		public boolean isValid() {
			return !value.isEmpty();
		}
		
		public String getValue() {
			return value;
		}
		
		@Override
		public OptionDescription getOptionDescription() {
			return this.option;
		}
		
		@Override
		public Control getControl() {
			return this.control;
		}
	}

	public static class StringControlFacade extends AbstractOptionUiFacade<String> {
		public StringControlFacade(OptionTypeFacade<String> optionTypeFacade, Runnable optionChangedCallback) {
			super (optionTypeFacade, optionChangedCallback);
		}
	}
	
	public static class BooleanControlFacade extends AbstractOptionUiFacade<Boolean> {
		private Button button;
		private boolean enabled = false;
		
		public BooleanControlFacade(OptionTypeFacade<Boolean> optionTypeFacade, Runnable optionChangedCallback) {
			super(optionTypeFacade, optionChangedCallback);
		}
		
		@Override
		public void createControls(Composite parent) {
			Composite control = new Composite(parent, SWT.NONE);
			GridLayoutFactory.fillDefaults().numColumns(1).applyTo(control);
			button = new Button(control, SWT.CHECK);
			button.setText("Include the --" + option.getName() + " argument."); //$NON-NLS-1$
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					BooleanControlFacade.this.enabled = button.getSelection();
				}
			});
			Text text = new Text(control, SWT.BORDER|SWT.V_SCROLL|SWT.H_SCROLL|SWT.WRAP);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(text);
			text.setEditable(false);
			text.setText(option.getDescription());
			this.control = control;
		}
		
		@Override
		public boolean isValid() {
			return true;
		}
		
		@Override
		public String getValue() {
			return String.valueOf(enabled);
		}
		
		@Override
		public void setFocus() {
			button.setFocus();
		}
	}
	
	public static class FileControlFacade extends AbstractOptionUiFacade<File> {
		public FileControlFacade(OptionTypeFacade<File> optionTypeFacade, Runnable optionChangedCallback) {
			super(optionTypeFacade, optionChangedCallback);
		}
	}
	
	public static class PathControlFacade extends AbstractOptionUiFacade<Path> {
		public PathControlFacade(OptionTypeFacade<Path> optionTypeFacade, Runnable optionChangedCallback) {
			super(optionTypeFacade, optionChangedCallback);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static OptionUiFacade createOptionUiFacade(OptionDescription option, OptionsFacadeManager mgr, 
			Runnable optionChangedCallback) {
		Type type = Type.valueOf(option.getType().toUpperCase());
		OptionTypeFacade<?> typeFacade = mgr.getFacade(option, OptionTypeFacade.class);
		OptionUiFacade uiFacade = null;
		switch(type) {
			case STRING: {
				uiFacade = new StringControlFacade((OptionTypeFacade<String>) typeFacade, optionChangedCallback);
				break;
			}
			case BOOLEAN: {
				uiFacade = new BooleanControlFacade((OptionTypeFacade<Boolean>) typeFacade, optionChangedCallback);
				break;
			}
			case FILE: {
				uiFacade = new FileControlFacade((OptionTypeFacade<File>) typeFacade, optionChangedCallback);
				break;
			}
			case PATH: {
				uiFacade = new PathControlFacade((OptionTypeFacade<Path>) typeFacade, optionChangedCallback);
				break;
			}
		}
		return uiFacade;
	}
}
