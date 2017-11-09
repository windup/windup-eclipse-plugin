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

import static org.jboss.tools.windup.ui.internal.Messages.Rules;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.internal.ui.SWTFactory;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.windup.core.services.WindupOptionsService;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.ui.FilteredListDialog;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.rules.xml.XMLRulesetModelUtil;
import org.jboss.tools.windup.windup.ConfigurationElement;
import org.jboss.tools.windup.windup.CustomRuleProvider;

import com.google.common.collect.Lists;

@SuppressWarnings("restriction")
public class RulesTab extends AbstractLaunchConfigurationTab {
	
	private static final String ID = "org.jboss.tools.windup.ui.launch.RulesTab"; //$NON-NLS-1$

	private ModelService modelService;
	private ConfigurationElement configuration;
	
	private TableViewer rulesRepositoryViewer;
	private Button removeButton;
	
	// TODO: We probably want to use this once we start using an external Windup launcher.
	@SuppressWarnings("unused")
	private WindupOptionsService optionsService;
	
	public RulesTab(ModelService modelService, WindupOptionsService optionsService) {
		this.modelService = modelService;
		this.optionsService = optionsService;
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().margins(5, 5).applyTo(container);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(container);
		createCustomRulesGroup(container);
		super.setControl(container);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(container, ID);
	}
	
	private void createCustomRulesGroup(Composite parent) {
		Group group = SWTFactory.createGroup(parent, Messages.windupCustomRules+":", 2, 1, GridData.FILL_BOTH);
		GridDataFactory.fillDefaults().grab(true, true).hint(70, 100).applyTo(group);
		rulesRepositoryViewer = new TableViewer(group, SWT.MULTI|SWT.BORDER|SWT.FULL_SELECTION|SWT.H_SCROLL|SWT.V_SCROLL);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(rulesRepositoryViewer.getTable());
		rulesRepositoryViewer.setContentProvider(ArrayContentProvider.getInstance());
		rulesRepositoryViewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				CustomRuleProvider provider = (CustomRuleProvider)element;
				StringBuilder builder = new StringBuilder();
				String parsedRulesetId = XMLRulesetModelUtil.getRulesetId(provider.getLocationURI());
				if (parsedRulesetId != null && !parsedRulesetId.isEmpty()) {
					builder.append(parsedRulesetId);
					builder.append(" - "); //$NON-NLS-1$
				}
				builder.append(provider.getLocationURI());
				return builder.toString();				
			}
			@Override
			public Image getImage(Object element) {
				return WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_RULE_SET);
			}
		});
		createCustomRulesButtonBar(group);
	}
	
	private void createCustomRulesButtonBar(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayoutFactory.fillDefaults().margins(0, 0).spacing(0, 0).applyTo(container);
		GridDataFactory.fillDefaults().grab(false, true).applyTo(container);
		
		Button addButton = new Button(container, SWT.PUSH);
		addButton.setText("Add..."); //$NON-NLS-1$
		GridDataFactory.fillDefaults().grab(true, false).applyTo(addButton);
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FilteredListDialog dialog = new FilteredListDialog(parent.getShell(), new LabelProvider() {
					@Override
					public String getText(Object element) {
						CustomRuleProvider provider = (CustomRuleProvider)element;
						StringBuilder builder = new StringBuilder();
						String parsedRulesetId = XMLRulesetModelUtil.getRulesetId(provider.getLocationURI());
						if (parsedRulesetId != null && !parsedRulesetId.isEmpty()) {
							builder.append(parsedRulesetId);
							builder.append(" - "); //$NON-NLS-1$
						}
						builder.append(provider.getLocationURI());
						return builder.toString();
					}
					@Override
					public Image getImage(Object element) {
						return WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_RULE_SET);
					}
				});
				dialog.setMultipleSelection(true);
				dialog.setMessage(Messages.selectExistingRepositories);
				
				modelService.cleanCustomRuleRepositories(configuration);
				
				List<CustomRuleProvider> providers = Lists.newArrayList(modelService.getModel().getCustomRuleRepositories());
				providers = providers.stream().filter(p -> !configuration.getUserRulesDirectories().contains(p.getLocationURI())).collect(Collectors.toList());
				dialog.setElements(providers.stream().toArray(CustomRuleProvider[]::new));
				dialog.setTitle(Messages.selectRepositories);
				dialog.setHelpAvailable(false);
				dialog.create();
				if (dialog.open() == Window.OK) {
					Object[] selected = (Object[])dialog.getResult();
					if (selected.length > 0) {
						List<String> rulesets = Lists.newArrayList();
						
						// TODO: Temporary - see https://tree.taiga.io/project/rdruss-jboss-migration-windup-v3/task/884
						selected = new Object[] {selected[0]};
						modelService.write(() -> configuration.getUserRulesDirectories().clear());
						// 
						
						Arrays.stream(selected).forEach(p -> rulesets.add(((CustomRuleProvider)p).getLocationURI()));
						modelService.write(() -> {
							configuration.getUserRulesDirectories().addAll(rulesets);
						});
						reload();
					}
				}
			}
		});
		
		removeButton= new Button(container, SWT.PUSH);
		removeButton.setText(Messages.windupRemove);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(removeButton);
		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				StructuredSelection ss = (StructuredSelection)rulesRepositoryViewer.getSelection();
				if (!ss.isEmpty()) {
					@SuppressWarnings("unchecked")
					List<CustomRuleProvider> providers = (List<CustomRuleProvider>)ss.toList();
					List<String> paths = providers.stream().map(p -> p.getLocationURI()).collect(Collectors.toList());
					modelService.write(() -> {
						configuration.getUserRulesDirectories().removeAll(paths);
					});
					reloadCustomRules();
				}
			}
		});
		
		rulesRepositoryViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				removeButton.setEnabled(!event.getSelection().isEmpty());
			}
		});
		removeButton.setEnabled(false);
	}
	
	private void reload() {
		reloadCustomRules();
	}
	
	private void reloadCustomRules() {
		if (rulesRepositoryViewer != null) {
			modelService.cleanCustomRuleRepositories(configuration);
			
			List<CustomRuleProvider> providers = Lists.newArrayList();
			configuration.getUserRulesDirectories().forEach(directory -> {
				modelService.getModel().getCustomRuleRepositories().forEach(provider -> {
					if (directory.contains(provider.getLocationURI())) {
						providers.add(provider);
					}
				});
			});			
			
			rulesRepositoryViewer.setInput(providers);
		}
	}
	
	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy launchConfig) {
		initializeConfiguration(launchConfig);
	}
	
	@Override
	public void performApply(ILaunchConfigurationWorkingCopy launchConfig) {
		modelService.write(() -> {
			configuration.setName(launchConfig.getName());
		});
	}

	@Override
	public String getName() {
		return Rules;
	}
	
	@Override
	public Image getImage() {
		return WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_RULES);
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
