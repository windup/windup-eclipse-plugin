/*******************************************************************************
 * Copyright (c) 2021 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.launch;

import static org.jboss.tools.windup.ui.internal.Messages.Options;

import java.io.File;
import java.util.List;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.internal.ui.SWTFactory;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.pde.internal.ui.IHelpContextIds;
import org.eclipse.pde.internal.ui.PDEPluginImages;
import org.eclipse.pde.internal.ui.PDEUIMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.jboss.tools.windup.core.services.WindupOptionsService;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.runtime.IPreferenceConstants;
import org.jboss.tools.windup.runtime.WindupRuntimePlugin;
import org.jboss.tools.windup.runtime.options.IOptionKeys;
import org.jboss.tools.windup.runtime.options.OptionDescription;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.preferences.WindupPreferencePage;
import org.jboss.tools.windup.windup.ConfigurationElement;
import org.jboss.tools.windup.windup.Pair;
import org.jboss.tools.windup.windup.WindupFactory;

import com.google.common.collect.Lists;

/**
 * Tab for configuration Windup options.
 */
@SuppressWarnings("restriction")
public class OptionsTab extends AbstractLaunchConfigurationTab {

	private static final String ID = "org.jboss.tools.windup.ui.launch.OptionsTab"; //$NON-NLS-1$
	
	private ModelService modelService;
	private ConfigurationElement configuration;
	
	private Button generateReportButton;
	private TableViewer optionsViewer;
	
	// TODO: We probably want to use this once we start using an external Windup launcher.
	private WindupOptionsService optionsService;
	
	private FileFieldEditor jreEditor;
	
	public OptionsTab(ModelService modelService, WindupOptionsService optionsService) {
		this.modelService = modelService;
		this.optionsService = optionsService;
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().margins(5, 5).applyTo(container);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(container);
		createReportGroup(container);
		createJREGroup(container);
		createOptionsGroup(container);
		super.setControl(container);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(container, ID);
	}
	
	private void createReportGroup(Composite parent) {
		Group group = SWTFactory.createGroup(parent, Messages.windupReport+":", 1, 1, GridData.FILL_HORIZONTAL);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(group);
		generateReportButton = SWTFactory.createCheckButton(group, Messages.windupGenerateReport, null, true, GridData.GRAB_HORIZONTAL);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(generateReportButton);
		generateReportButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				configuration.setGenerateReport(generateReportButton.getSelection());
			}
		});
	}
	
	private void createJREGroup(Composite parent) {
		
//		parent = new Composite(parent, SWT.NONE);
//		GridLayoutFactory.fillDefaults().numColumns(2).margins(0, 0).spacing(8, LayoutConstants.getSpacing().y).applyTo(parent);

		parent = new Composite(parent, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, false).indent(5, 0).applyTo(parent);
		
		jreEditor = new FileFieldEditor(
				IPreferenceConstants.WINDUP_JRE_HOME, 
				Messages.WindupPreferenceJRE, 
				true, 
				StringFieldEditor.VALIDATE_ON_KEY_STROKE,
				parent) {
			
			private boolean isDefaultJrePresented = false;
			
			public Label getLabelControl(Composite parent) {
				CLabel label = new CLabel(parent, SWT.LEFT);
				Image jreImage = PDEPluginImages.DESC_JAVA_LIB_OBJ.createImage();
	            label.setImage(jreImage);
	            label.setFont(parent.getFont());
	            String text = getLabelText();
	            if (text != null) {
					label.setText(text);
				}
	            label.addDisposeListener(event -> {
	            	 	jreImage.dispose();
	            });
	            return null;
		    }
			
			@Override
			protected void doStore() {
				configuration.setJreHome(getTextControl().getText().trim());
			}
			
			@Override
			public void store() {
				if (isDefaultJrePresented) {
					configuration.setJreHome(WindupRuntimePlugin.computeJRELocation());
		        } else {
		            doStore();
		        }
			}
			
		    protected void valueChanged() {
		        setPresentsDefaultValue(false);
		        boolean oldState = super.isValid();
		        refreshValidState();

		        if (isValid() != oldState) {
					fireStateChanged(IS_VALID, oldState, isValid());
				}

		        String newValue = super.getTextControl().getText();
		        if (!newValue.equals(oldValue)) {
		            fireValueChanged(VALUE, oldValue, newValue);
		            oldValue = newValue;
		            store();
		        }
		    }
		    
			@Override
			public void load() {
				isDefaultJrePresented = false;
				Text textControl = super.getTextControl();
				if (textControl != null) {
					String value = configuration.getJreHome();
					textControl.setText(value != null ? value : "");
					oldValue = value;
				}
				refreshValidState();
			}
		    
			@Override
			public void loadDefault() {
				isDefaultJrePresented = true;
				Text textControl = super.getTextControl();
				if (textControl != null) {
					String value = WindupRuntimePlugin.computeJRELocation();
					textControl.setText(value);
				}
				refreshValidState();
				valueChanged();
			}
			
			@Override
			protected boolean checkState() {
		        String msg = null;
		        String path = getTextControl().getText();
        			clearErrorMessage();
		        if (path != null) {
					path = path.trim();
				} else {
					path = "";//$NON-NLS-1$
				}
		        if (path.length() == 0) {
		        		return true; 
		        } else {
		            File file = new File(path);
		            if (file.isDirectory()) {
		                if (!file.isAbsolute()) {
							msg = Messages.JRENotAbsolute;
						}
		            } else {
		                msg = Messages.InvalidJRELocation;
		            }
		        }
		        if (msg != null) { // error
		            showErrorMessage(msg);
		            return false;
		        }
		        return true;
			}
			
			@Override
			protected String changePressed() {
		        File f = new File(getTextControl().getText());
		        if (!f.exists()) {
					f = null;
				}
		        
		        File d = promptJRE();
		        if (d == null) {
					return null;
				}

		        return d.getAbsolutePath();
		    }
		};
		jreEditor.setEmptyStringAllowed(true);
	}
	
	private File promptJRE() {
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(Display.getDefault().getActiveShell(), new LabelProvider() {
			public Image getImage(Object element) {
				return JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_LIBRARY);
			}
			@Override
			public String getText(Object element) {
				IVMInstall install = (IVMInstall)element;
				return install.getName();
			}
		});
		dialog.setElements(WindupPreferencePage.getEnvironments());
		dialog.setAllowDuplicates(false);
		dialog.setMultipleSelection(true);
		dialog.setTitle(PDEUIMessages.RequiredExecutionEnvironmentSection_dialog_title);
		dialog.setMessage(PDEUIMessages.RequiredExecutionEnvironmentSection_dialogMessage);
		dialog.create();
		PlatformUI.getWorkbench().getHelpSystem().setHelp(dialog.getShell(), IHelpContextIds.EXECUTION_ENVIRONMENT_SELECTION);
		if (dialog.open() == Window.OK && dialog.getResult() != null && dialog.getResult().length == 1) {
			IVMInstall install = (IVMInstall)dialog.getResult()[0];
			return install.getInstallLocation();
		}
		return null;
	}
	
	private void createOptionsGroup(Composite parent) {
		Group group = SWTFactory.createGroup(parent, Messages.Options+":", 2, 1, GridData.FILL_BOTH);
		GridDataFactory.fillDefaults().grab(true, true).hint(70, 100).applyTo(group);
		optionsViewer = new TableViewer(group, SWT.MULTI|SWT.BORDER|SWT.FULL_SELECTION|SWT.H_SCROLL|SWT.V_SCROLL);
		optionsViewer.getTable().setHeaderVisible(true);
		optionsViewer.getTable().setLinesVisible(true);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(optionsViewer.getTable());
		optionsViewer.setContentProvider(ArrayContentProvider.getInstance());
		
		TableViewerColumn column = new TableViewerColumn(optionsViewer, SWT.NONE);
		column.getColumn().setResizable(true);
		column.getColumn().setMoveable(true);
		column.getColumn().setWidth(200);
		column.getColumn().setText(Messages.OPTION);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((Pair)element).getKey();
			}
		});
		
		column = new TableViewerColumn(optionsViewer, SWT.NONE);
		column.getColumn().setResizable(true);
		column.getColumn().setMoveable(true);
		column.getColumn().setWidth(200);
		column.getColumn().setText(Messages.VALUE);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((Pair)element).getValue();
			}
		});
		createOptionsButtonBar(group);
	}
	
	private void createOptionsButtonBar(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().margins(0, 0).spacing(0, 0).applyTo(container);
		GridDataFactory.fillDefaults().grab(false, true).applyTo(container);
		
		Button addButton = new Button(container, SWT.PUSH);
		addButton.setText(Messages.windupAdd);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(addButton);
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				OptionsDialog dialog = new OptionsDialog(parent.getShell(), 
						modelService, optionsService, configuration);
				if (dialog.open() == IDialogConstants.OK_ID) {
					OptionDescription option = dialog.getSelectedOption();
					String value = dialog.getSelectedOptionValue();
					if (!value.isEmpty()) {
						Pair pair = WindupFactory.eINSTANCE.createPair();
						pair.setKey(option.getName());
						pair.setValue(value);
						configuration.getOptions().add(pair);
						if (IOptionKeys.outputOption.equals(option.getName())) {
							configuration.setOutputLocation(value);
						}
					}
					reloadOptions();
				}
			}
		});
		
		Button removeButton = new Button(container, SWT.PUSH);
		removeButton.setText(Messages.windupRemove);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(removeButton);
		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				StructuredSelection ss = (StructuredSelection)optionsViewer.getSelection();
				if (!ss.isEmpty()) {
					@SuppressWarnings("unchecked")
					List<Pair> options = (List<Pair>)ss.toList();
					configuration.getOptions().removeAll(options);
					for (Pair option : options) {
						if (IOptionKeys.outputOption.equals(option.getKey())) {
							configuration.setOutputLocation(modelService.getDefaultOutputLocation(configuration));
						}
					}
					reloadOptions();
				}
			}
		});
	}
	
	private void reload() {
		reloadReportGroup();
		reloadOptions();
		reloadJreHome();
	}

	private void reloadReportGroup() {
		if (generateReportButton != null) {
			generateReportButton.setSelection(configuration.isGenerateReport());
		}
	}
	
	private void reloadOptions() {
		if (optionsViewer != null) {
			optionsViewer.setInput(Lists.newArrayList(configuration.getOptions()));
		}
	}
	
	private void reloadJreHome() {
		if (jreEditor != null) {
			jreEditor.load();
		}
	}
	
	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy launchConfig) {
		initializeConfiguration(launchConfig);
	}
	
	@Override
	public void performApply(ILaunchConfigurationWorkingCopy launchConfig) {
		configuration.setName(launchConfig.getName());
	}

	@Override
	public String getName() {
		return Options;
	}
	
	@Override
	public Image getImage() {
		return WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_OPTIONS_TAB);
	}
	
	@Override
	public boolean isValid(ILaunchConfiguration launchConfig) {
		return true;
	}
	
	@Override
	public void initializeFrom(ILaunchConfiguration launchConfig) {
		initializeConfiguration(launchConfig);
	}
	
	private void initializeConfiguration(ILaunchConfiguration launchConfig) {
		this.configuration = modelService.findConfiguration(launchConfig.getName());
		if (configuration == null) {
			this.configuration = LaunchUtils.createConfiguration(launchConfig.getName(), modelService);
		}
		reload();
	}
}
