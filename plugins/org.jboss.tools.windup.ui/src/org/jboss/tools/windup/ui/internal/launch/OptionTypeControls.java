package org.jboss.tools.windup.ui.internal.launch;

import java.io.File;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.windup.bootstrap.help.OptionDescription;

/**
 * GUI controls for each option type. 
 */
public class OptionTypeControls {
	
	static enum Type {
		STRING, BOOLEAN, FILE, PATH
	}
	
	static enum UiType {
		SINGLE, MANY, SELECT_MANY, FILE_OR_DIRECTORY, DIRECTORY
	}
	
	public static interface OptionTypeControl {
		void createControls(Composite parent);
		boolean isValid();
		Object getValue();
		Control getControl();
		void setFocus();
	}
	
	private static abstract class AbstractOptionTypeControl implements OptionTypeControl {
		
		protected OptionDescription option;
		protected UiType uiType;
		private Control control;
		private Runnable optionChangedCallback;
		
		// For Alpha, just a simple text field.
		protected Text textWidget;
		
		public AbstractOptionTypeControl(OptionDescription option, Runnable optionChangedCallback) {
			this.option = option;
			this.optionChangedCallback = optionChangedCallback;
			this.uiType = UiType.valueOf(option.getUiType().toUpperCase());
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
			textWidget.setFocus();
		}
		
		protected void createInputControl(Composite parent) {
			textWidget = new Text(parent, SWT.BORDER | SWT.SINGLE);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(textWidget);
			
			textWidget.setFont(parent.getFont());
			textWidget.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					optionChangedCallback.run();
				}
			});
		}
		
		@Override
		public boolean isValid() {
			return !this.textWidget.getText().isEmpty();
		}
		
		@Override
		public abstract Object getValue();
		
		@Override
		public Control getControl() {
			return this.control;
		}
	}

	public static class StringControl extends AbstractOptionTypeControl {
		public StringControl(OptionDescription option, Runnable optionChangedCallback) {
			super (option, optionChangedCallback);
		}
		
		@Override
		public Object getValue() {
			return this.textWidget.getText();
		}
	}
	
	public static class BooleanControl extends AbstractOptionTypeControl {
		public BooleanControl(OptionDescription option, Runnable optionChangedCallback) {
			super(option, optionChangedCallback);
		}
		@Override
		public Object getValue() {
			return new Boolean(this.textWidget.getText());
		}
	}
	
	public static class FileControl extends AbstractOptionTypeControl {
		public FileControl(OptionDescription option, Runnable optionChangedCallback) {
			super(option, optionChangedCallback);
		}
		@Override
		public Object getValue() {
			return new File(this.textWidget.getText());
		}
	}
	
	public static class PathControl extends AbstractOptionTypeControl {
		public PathControl(OptionDescription option, Runnable optionChangedCallback) {
			super(option, optionChangedCallback);
		}
		@Override
		public Object getValue() {
			File file = new File(this.textWidget.getText());
			if (file.exists()) {
				return file.toPath();
			}
			return null;
		}
	}
	
	public static OptionTypeControl createControl(OptionDescription option, 
			Runnable optionChangedCallback) {
		Type type = Type.valueOf(option.getType().toUpperCase());
		OptionTypeControl control = null;
		switch(type) {
			case STRING: {
				control = new StringControl(option, optionChangedCallback);
				break;
			}
			case BOOLEAN: {
				control = new BooleanControl(option, optionChangedCallback);
				break;
			}
			case FILE: {
				control = new FileControl(option, optionChangedCallback);
				break;
			}
			case PATH: {
				control = new PathControl(option, optionChangedCallback);
				break;
			}
		}
		return control;
	}
}
