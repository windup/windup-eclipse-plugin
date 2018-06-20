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
package org.jboss.tools.windup.ui.internal.editor.launch;

import static org.jboss.tools.windup.ui.WindupUIPlugin.IMG_JRE;
import static org.jboss.tools.windup.ui.WindupUIPlugin.IMG_SEARCH;
import static org.jboss.tools.windup.ui.internal.Messages.generalInfoDescription;
import static org.jboss.tools.windup.ui.internal.Messages.generalInfoTitle;
import static org.jboss.tools.windup.ui.internal.Messages.javaRuntimeEnvironment;
import static org.jboss.tools.windup.ui.internal.Messages.launchName;
import static org.jboss.tools.windup.ui.internal.Messages.searchJRE;
import static org.jboss.tools.windup.ui.internal.Messages.windupHomeLocation;

import java.util.Arrays;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.jboss.tools.windup.runtime.WindupRuntimePlugin;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.editor.AbstractSection;
import org.jboss.tools.windup.windup.WindupPackage;

/**
 * Section for configuring general Windup launch configuration information.
 */
public class GeneralInfoSection extends AbstractSection {
	
	private static final String JRE_PREF_PAGE = "org.eclipse.jdt.debug.ui.preferences.VMPreferencePage";  //$NON-NLS-1$

	@SuppressWarnings("unchecked")
	@Override
	protected void fillSection(Composite parent) {
		section.setText(generalInfoTitle);
		section.setDescription(generalInfoDescription);
		createLabel(parent, launchName);
		Text nameText = toolkit.createText(parent, configuration.getName());
		GridDataFactory.fillDefaults().grab(true, false).applyTo(nameText);
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(nameText),
				EMFProperties.value(WindupPackage.eINSTANCE.getNamedElement_Name()).
					observe(configuration));
		Hyperlink windupHomeLink = toolkit.createHyperlink(parent, windupHomeLocation, SWT.NONE);
		String home = WindupRuntimePlugin.computeWindupHome().toString();
		Text windupLocationText = toolkit.createText(parent, home);
		windupLocationText.setBackground(Display.getDefault().getActiveShell().getBackground());
		windupLocationText.setEditable(false);
		GridDataFactory.fillDefaults().grab(true, false).hint(400, SWT.DEFAULT).applyTo(windupLocationText);
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(windupLocationText),
				EMFProperties.value(WindupPackage.eINSTANCE.getConfigurationElement_WindupHome()).
					observe(configuration));
		windupHomeLink.addHyperlinkListener(new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				DirectoryDialog dd = new DirectoryDialog(WindupUIPlugin.getActiveWorkbenchShell());
				dd.setText(windupHomeLocation);
				String filename = dd.open();
				if(filename != null) {
					IPath path = new Path(filename);
					if (path != null) {
						configuration.setWindupHome(path.toString());
					}
				}
			}
		});
		
		Composite container = toolkit.createComposite(parent);
		toolkit.paintBordersFor(container);
		GridLayoutFactory.fillDefaults().numColumns(4).equalWidth(false).applyTo(container);
		GridDataFactory.fillDefaults().grab(true, true).span(2, 1).applyTo(container);
		
		createLabel(container, "").setImage(WindupUIPlugin.getDefault().getImageRegistry().get(IMG_JRE));
		createLabel(container, javaRuntimeEnvironment);
		
		Combo jreCombo = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(jreCombo);
		jreCombo.setVisibleItemCount(30);
		jreCombo.setEnabled(false);
		
		Button jreButton = toolkit.createButton(container, searchJRE, SWT.PUSH);
		jreButton.setImage(WindupUIPlugin.getDefault().getImageRegistry().get(IMG_SEARCH));
		jreButton.pack(true);
		jreButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String currentVM = jreCombo.getText();
				String defaultInstalledJRE = JavaRuntime.getDefaultVMInstall().getName();
				boolean useDefault = defaultInstalledJRE.equals(currentVM);
				int result = PreferencesUtil.createPreferenceDialogOn(WindupUIPlugin.getActiveWorkbenchShell(), 
						JRE_PREF_PAGE, new String[] {JRE_PREF_PAGE}, null).open();
				if (result == Window.OK) {
					populateJREs(jreCombo);
					if (useDefault || jreCombo.indexOf(currentVM) == -1) {
						jreCombo.setText(defaultInstalledJRE);
					}
					else {
						jreCombo.setText(currentVM);
					}
				}
			}
		});
		jreButton.setEnabled(false);
		populateJREs(jreCombo);
		jreCombo.select(0);
	}
	
	private void populateJREs(Combo combo) {
		combo.setItems(getSortedVMNames());
	}
	
	public static String[] getSortedVMNames() {
		return Arrays.stream(JavaRuntime.getVMInstallTypes()).flatMap(i -> { 
			return Arrays.stream(i.getVMInstalls());
		}).map(vm -> vm.getName()).sorted((arg1, arg2) -> {
			return arg1.toString().compareTo(arg2.toString());
		}).toArray(String[]::new);
	}
}
