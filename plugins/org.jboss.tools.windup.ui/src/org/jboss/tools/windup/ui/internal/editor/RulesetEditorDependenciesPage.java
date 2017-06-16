package org.jboss.tools.windup.ui.internal.editor;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class RulesetEditorDependenciesPage {
	
	private Browser browser;

	public RulesetEditorDependenciesPage(Composite parent) {
		createControls(parent);
	}
	
	private void createControls(Composite parent) {
		this.browser = new Browser(parent, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(browser);
		browser.setUrl("http://www.google.com");
	}
	
	public Control getControl() {
		return browser;
	}
}