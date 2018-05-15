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
package org.jboss.tools.windup.ui.internal.editor.launch;

import static org.jboss.tools.windup.ui.internal.Messages.generatedReportLocation;
import static org.jboss.tools.windup.ui.internal.Messages.optionsDescription;
import static org.jboss.tools.windup.ui.internal.Messages.sourceMode;
import static org.jboss.tools.windup.ui.internal.Messages.windupOptions;

import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IFormColors;
import org.jboss.tools.windup.ui.internal.editor.AbstractSection;
import org.jboss.tools.windup.windup.WindupPackage;

/**
 * Section for specifying Windup options.
 */
public class OptionsSections extends AbstractSection {
	
	@SuppressWarnings("unchecked")
	@Override
	protected void fillSection(Composite parent) {
		section.setText(windupOptions);
		section.setDescription(optionsDescription); 
		
		Composite container = toolkit.createComposite(parent);
		GridLayoutFactory.fillDefaults().applyTo(container);
		GridDataFactory.fillDefaults().span(2, 1).applyTo(container);
		
		Button sourceButton = toolkit.createButton(container, sourceMode, SWT.CHECK);
		sourceButton.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
		bindingContext.bindValue(WidgetProperties.selection().observe(sourceButton),
				EMFProperties.value(WindupPackage.eINSTANCE.getConfigurationElement_SourceMode()).
					observe(configuration));
		sourceButton.setEnabled(false);
		
		
		container = toolkit.createComposite(parent);
		GridLayoutFactory.fillDefaults().numColumns(3).applyTo(container);
		GridDataFactory.fillDefaults().grab(true, false).span(2, 1).applyTo(container);
		createLabel(container, generatedReportLocation);
		
		/*Text outputLocationText = toolkit.createText(container, configuration.getGeneratedReportsLocation());
		outputLocationText.setEditable(false);
		outputLocationText.setBackground(Display.getDefault().getActiveShell().getBackground());
		GridDataFactory.fillDefaults().grab(true, false).hint(400, SWT.DEFAULT).applyTo(outputLocationText);
		
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(outputLocationText),
				EMFProperties.value(WindupPackage.eINSTANCE.getConfigurationElement_GeneratedReportsLocation()).
					observe(configuration));
		
		Button reportLocationButton = toolkit.createButton(container, browseLabel, SWT.PUSH);
		reportLocationButton.setImage(WindupUIPlugin.getDefault().getImageRegistry().get(IMG_SEARCH));
		reportLocationButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dd = new DirectoryDialog(WindupUIPlugin.getActiveWorkbenchShell());
				dd.setText(generatedReportLocationSearch);
				String filename = dd.open();
				if(filename != null) {
					IPath path = new Path(filename);
					if (path != null) {
						configuration.setGeneratedReportsLocation(path.toString());
					}
				}
			}
		});
		reportLocationButton.setEnabled(false);
		*/
	}
}
