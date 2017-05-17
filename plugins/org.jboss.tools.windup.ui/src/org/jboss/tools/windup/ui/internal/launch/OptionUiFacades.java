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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.windup.model.Facades.IFacade;
import org.jboss.tools.windup.model.OptionFacades.OptionTypeFacade;
import org.jboss.tools.windup.model.OptionFacades.OptionsFacadeManager;
import org.jboss.tools.windup.model.OptionFacades.Type;
import org.jboss.tools.windup.model.OptionFacades.UiType;
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
		
		protected Text textWidget;
		
		public AbstractOptionUiFacade(OptionTypeFacade<T> optionTypeFacade, Runnable optionChangedCallback) {
			this.optionTypeFacade = optionTypeFacade;
			this.optionChangedCallback = optionChangedCallback;
			this.option = optionTypeFacade.getOptionDescription();
		}
		
		@Override
		public void createControls(Composite parent) {
			Composite control = new Composite(parent, SWT.NONE);
			GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).applyTo(control);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(control);
			Label valueLabel = new Label(control, SWT.NONE);
			valueLabel.setText(Messages.VALUE+":");
			valueLabel.setFont(parent.getFont());
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
					notifyOptionChanged();
				}
			});
			
			Composite container = new Composite (parent, SWT.NONE);
			GridLayoutFactory.fillDefaults().applyTo(container);
			GridDataFactory.fillDefaults().grab(true, true).span(2, 0).applyTo(container);
			Label text = new Label(container, SWT.WRAP);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(text);
			text.setText("*" + option.getDescription());
		}
		
		protected void notifyOptionChanged() {
			optionChangedCallback.run();
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
			GridDataFactory.fillDefaults().grab(true, false).applyTo(control);
			button = new Button(control, SWT.CHECK);
			button.setText("Include the --" + option.getName() + " argument."); //$NON-NLS-1$
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					BooleanControlFacade.this.enabled = button.getSelection();
					notifyOptionChanged();
				}
			});
			Composite container = new Composite (control, SWT.NONE);
			GridLayoutFactory.fillDefaults().applyTo(container);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(container);
			Label text = new Label(container, SWT.WRAP);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(text);
			text.setText("*" + option.getDescription());
			this.control = control;
		}
		
		@Override
		public boolean isValid() {
			return enabled;
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
		
		private String fileLocation;
		private Text text;
		
		public FileControlFacade(OptionTypeFacade<File> optionTypeFacade, Runnable optionChangedCallback) {
			super(optionTypeFacade, optionChangedCallback);
		}
		
		@Override
		public void createControls(Composite parent) {
			Composite control = new Composite(parent, SWT.NONE);
			GridLayoutFactory.fillDefaults().numColumns(3).applyTo(control);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(control);
			new Label(control, SWT.NONE).setText("Location:");
			text = new Text(control, SWT.BORDER | SWT.SINGLE);
			text.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					fileLocation = text.getText().trim();
					notifyOptionChanged();
				}
			});
			GridDataFactory.fillDefaults().hint(SWT.DEFAULT, SWT.DEFAULT).align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(text);
			Button button = new Button(control, SWT.NONE);
			button.setText("Browse...");
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
			        FileDialog dialog = new FileDialog(FileControlFacade.this.control.getShell(), SWT.OPEN);
			        String location = dialog.open();
			        location = location == null ? "" : location;
			        FileControlFacade.this.fileLocation = location;
			        FileControlFacade.this.text.setText(location);
			        notifyOptionChanged();
				}
			});
			Composite container = new Composite (control, SWT.NONE);
			GridLayoutFactory.fillDefaults().applyTo(container);
			GridDataFactory.fillDefaults().grab(true, true).span(3, 0).applyTo(container);
			Label text = new Label(container, SWT.WRAP);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(text);
			text.setText("*" + option.getDescription());
			this.control = control;
		}
		
		@Override
		public boolean isValid() {
			return fileLocation != null && !fileLocation.isEmpty() && new File(fileLocation).exists();
		}
		
		@Override
		public String getValue() {
			return fileLocation;
		}
		
		@Override
		public void setFocus() {
			text.setFocus();
		}
	}
	
	public static class DirectoryControlFacade extends AbstractOptionUiFacade<File> {
		
		private String directoryLocation;
		private Text text;
		
		public DirectoryControlFacade(OptionTypeFacade<File> optionTypeFacade, Runnable optionChangedCallback) {
			super(optionTypeFacade, optionChangedCallback);
		}
		
		@Override
		public void createControls(Composite parent) {
			Composite control = new Composite(parent, SWT.NONE);
			GridLayoutFactory.fillDefaults().numColumns(3).margins(0, 0).applyTo(control);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(control);
			new Label(control, SWT.NONE).setText("Location:");
			text = new Text(control, SWT.BORDER | SWT.SINGLE);
			text.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					directoryLocation = text.getText().trim();
					notifyOptionChanged();
				}
			});
			GridDataFactory.fillDefaults().grab(true, false).applyTo(text);
			Button button = new Button(control, SWT.NONE);
			GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(text);
			button.setText("Browse...");
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
			        DirectoryDialog dialog = new DirectoryDialog(DirectoryControlFacade.this.control.getShell(), SWT.OPEN);
			        String location = dialog.open();
			        location = location == null ? "" : location;
			        DirectoryControlFacade.this.directoryLocation = location;
			        DirectoryControlFacade.this.text.setText(location);
			        notifyOptionChanged();
				}
			});
			Composite container = new Composite (control, SWT.NONE);
			GridLayoutFactory.fillDefaults().applyTo(container);
			GridDataFactory.fillDefaults().grab(true, true).span(3, 0).applyTo(container);
			Label text = new Label(container, SWT.WRAP);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(text);
			text.setText("*" + option.getDescription());
			this.control = control;
		}
		
		@Override
		public boolean isValid() {
			return directoryLocation != null && !directoryLocation.isEmpty() && new File(directoryLocation).exists();
		}
		
		@Override
		public String getValue() {
			return directoryLocation;
		}
		
		@Override
		public void setFocus() {
			text.setFocus();
		}
	}

	public static class FileOrDirectoryControlFacade extends AbstractOptionUiFacade<Path> {
		
		private String pathLocation;
		private Text text;
		
		public FileOrDirectoryControlFacade(OptionTypeFacade<Path> optionTypeFacade, Runnable optionChangedCallback) {
			super(optionTypeFacade, optionChangedCallback);
		}
		
		@Override
		public void createControls(Composite parent) {
			Composite control = new Composite(parent, SWT.NONE);
			GridLayoutFactory.fillDefaults().numColumns(4).applyTo(control);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(control);
			new Label(control, SWT.NONE).setText("Location:");
			text = new Text(control, SWT.BORDER | SWT.SINGLE);
			text.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					pathLocation = text.getText().trim();
					notifyOptionChanged();
				}
			});
			GridDataFactory.fillDefaults().hint(SWT.DEFAULT, SWT.DEFAULT).align(SWT.FILL, SWT.CENTER).grab(true, false).applyTo(text);
			Button button = new Button(control, SWT.NONE);
			button.setText("Folder...");
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
			        DirectoryDialog dialog = new DirectoryDialog(FileOrDirectoryControlFacade.this.control.getShell(), SWT.OPEN);
			        String location = dialog.open();
			        location = location == null ? "" : location;
			        FileOrDirectoryControlFacade.this.pathLocation = location;
			        FileOrDirectoryControlFacade.this.text.setText(location);
			        notifyOptionChanged();
				}
			});
			button = new Button(control, SWT.NONE);
			button.setText("File...");
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
			        FileDialog dialog = new FileDialog(FileOrDirectoryControlFacade.this.control.getShell(), SWT.OPEN);
			        String location = dialog.open();
			        location = location == null ? "" : location;
			        FileOrDirectoryControlFacade.this.pathLocation = location;
			        FileOrDirectoryControlFacade.this.text.setText(location);
			        notifyOptionChanged();
				}
			});
			Composite container = new Composite (control, SWT.NONE);
			GridLayoutFactory.fillDefaults().applyTo(container);
			GridDataFactory.fillDefaults().grab(true, true).span(4, 0).applyTo(container);
			Label text = new Label(container, SWT.WRAP);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(text);
			text.setText("*" + option.getDescription());
			this.control = control;
		}
		
		@Override
		public boolean isValid() {
			return pathLocation != null && !pathLocation.isEmpty() && new File(pathLocation).exists();
		}
		
		@Override
		public String getValue() {
			return pathLocation;
		}
		
		@Override
		public void setFocus() {
			text.setFocus();
		}
	}
	
	public static class SelectControlFacade extends AbstractOptionUiFacade<String> {
		
		private Combo optionCombo;
		private String selection;
		
		public SelectControlFacade(OptionTypeFacade<String> optionTypeFacade, Runnable optionChangedCallback) {
			super(optionTypeFacade, optionChangedCallback);
		}
		
		@Override
		public void createControls(Composite parent) {
			Composite control = new Composite(parent, SWT.NONE);
			GridLayoutFactory.fillDefaults().numColumns(1).applyTo(control);
			optionCombo = new Combo(control, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(optionCombo);
			optionCombo.setFont(parent.getFont());
			
			optionCombo.setItems(option.getAvailableOptions().toArray(new String[option.getAvailableOptions().size()]));			
			optionCombo.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					SelectControlFacade.this.selection = option.getAvailableOptions().get(optionCombo.getSelectionIndex());
					notifyOptionChanged();
				}
			});
			
			Composite container = new Composite (control, SWT.NONE);
			GridLayoutFactory.fillDefaults().applyTo(container);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(container);
			Label text = new Label(container, SWT.WRAP);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(text);
			text.setText("*" + option.getDescription());
			this.control = control;
		}
		
		@Override
		public boolean isValid() {
			return selection != null && !selection.isEmpty();
		}
		
		@Override
		public String getValue() {
			return selection;
		}
		
		@Override
		public void setFocus() {
			optionCombo.setFocus();
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
				if (UiType.valueOf(option.getUiType()) == UiType.SELECT_MANY) {
					uiFacade = new SelectControlFacade((OptionTypeFacade<String>) typeFacade, optionChangedCallback);	
				}
				else {
					uiFacade = new StringControlFacade((OptionTypeFacade<String>) typeFacade, optionChangedCallback);
				}
				break;
			}
			case BOOLEAN: {
				uiFacade = new BooleanControlFacade((OptionTypeFacade<Boolean>) typeFacade, optionChangedCallback);
				break;
			}
			case FILE: {
				if (UiType.valueOf(option.getUiType()) == UiType.DIRECTORY) {
					uiFacade = new DirectoryControlFacade((OptionTypeFacade<File>) typeFacade, optionChangedCallback);
				}
				else if (UiType.valueOf(option.getUiType()) == UiType.FILE_OR_DIRECTORY) {
					uiFacade = new FileOrDirectoryControlFacade((OptionTypeFacade<Path>) typeFacade, optionChangedCallback);
				}
				else {
					uiFacade = new FileControlFacade((OptionTypeFacade<File>) typeFacade, optionChangedCallback);
				}
				break;
			}
			case PATH: {
				uiFacade = new FileOrDirectoryControlFacade((OptionTypeFacade<Path>) typeFacade, optionChangedCallback);
				break;
			}
		}
		return uiFacade;
	}
}
