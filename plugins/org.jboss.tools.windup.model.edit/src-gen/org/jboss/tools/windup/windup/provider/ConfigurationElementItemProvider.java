/**
 */
package org.jboss.tools.windup.windup.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import org.jboss.tools.windup.windup.ConfigurationElement;
import org.jboss.tools.windup.windup.WindupFactory;
import org.jboss.tools.windup.windup.WindupPackage;

/**
 * This is the item provider adapter for a {@link org.jboss.tools.windup.windup.ConfigurationElement} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ConfigurationElementItemProvider extends ParameterizedItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConfigurationElementItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addWindupHomePropertyDescriptor(object);
			addSourceModePropertyDescriptor(object);
			addGeneratedReportsLocationPropertyDescriptor(object);
			addPackagesPropertyDescriptor(object);
			addTimestampPropertyDescriptor(object);
			addGenerateReportPropertyDescriptor(object);
			addMigrationPathPropertyDescriptor(object);
			addUserRulesDirectoriesPropertyDescriptor(object);
			addReportDirectoryPropertyDescriptor(object);
			addJreHomePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Windup Home feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addWindupHomePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ConfigurationElement_windupHome_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ConfigurationElement_windupHome_feature", "_UI_ConfigurationElement_type"),
				 WindupPackage.eINSTANCE.getConfigurationElement_WindupHome(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Source Mode feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSourceModePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ConfigurationElement_sourceMode_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ConfigurationElement_sourceMode_feature", "_UI_ConfigurationElement_type"),
				 WindupPackage.eINSTANCE.getConfigurationElement_SourceMode(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Generated Reports Location feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addGeneratedReportsLocationPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ConfigurationElement_generatedReportsLocation_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ConfigurationElement_generatedReportsLocation_feature", "_UI_ConfigurationElement_type"),
				 WindupPackage.eINSTANCE.getConfigurationElement_GeneratedReportsLocation(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Packages feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPackagesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ConfigurationElement_packages_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ConfigurationElement_packages_feature", "_UI_ConfigurationElement_type"),
				 WindupPackage.eINSTANCE.getConfigurationElement_Packages(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Timestamp feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addTimestampPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ConfigurationElement_timestamp_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ConfigurationElement_timestamp_feature", "_UI_ConfigurationElement_type"),
				 WindupPackage.eINSTANCE.getConfigurationElement_Timestamp(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Generate Report feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addGenerateReportPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ConfigurationElement_generateReport_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ConfigurationElement_generateReport_feature", "_UI_ConfigurationElement_type"),
				 WindupPackage.eINSTANCE.getConfigurationElement_GenerateReport(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Migration Path feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMigrationPathPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ConfigurationElement_migrationPath_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ConfigurationElement_migrationPath_feature", "_UI_ConfigurationElement_type"),
				 WindupPackage.eINSTANCE.getConfigurationElement_MigrationPath(),
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the User Rules Directories feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addUserRulesDirectoriesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ConfigurationElement_userRulesDirectories_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ConfigurationElement_userRulesDirectories_feature", "_UI_ConfigurationElement_type"),
				 WindupPackage.eINSTANCE.getConfigurationElement_UserRulesDirectories(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Report Directory feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addReportDirectoryPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ConfigurationElement_reportDirectory_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ConfigurationElement_reportDirectory_feature", "_UI_ConfigurationElement_type"),
				 WindupPackage.eINSTANCE.getConfigurationElement_ReportDirectory(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Jre Home feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addJreHomePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ConfigurationElement_jreHome_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ConfigurationElement_jreHome_feature", "_UI_ConfigurationElement_type"),
				 WindupPackage.eINSTANCE.getConfigurationElement_JreHome(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(WindupPackage.eINSTANCE.getConfigurationElement_Inputs());
			childrenFeatures.add(WindupPackage.eINSTANCE.getConfigurationElement_Options());
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This returns ConfigurationElement.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/ConfigurationElement"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((ConfigurationElement)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_ConfigurationElement_type") :
			getString("_UI_ConfigurationElement_type") + " " + label;
	}
	

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(ConfigurationElement.class)) {
			case WindupPackage.CONFIGURATION_ELEMENT__WINDUP_HOME:
			case WindupPackage.CONFIGURATION_ELEMENT__SOURCE_MODE:
			case WindupPackage.CONFIGURATION_ELEMENT__GENERATED_REPORTS_LOCATION:
			case WindupPackage.CONFIGURATION_ELEMENT__PACKAGES:
			case WindupPackage.CONFIGURATION_ELEMENT__TIMESTAMP:
			case WindupPackage.CONFIGURATION_ELEMENT__GENERATE_REPORT:
			case WindupPackage.CONFIGURATION_ELEMENT__USER_RULES_DIRECTORIES:
			case WindupPackage.CONFIGURATION_ELEMENT__REPORT_DIRECTORY:
			case WindupPackage.CONFIGURATION_ELEMENT__JRE_HOME:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__INPUTS:
			case WindupPackage.CONFIGURATION_ELEMENT__OPTIONS:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add
			(createChildParameter
				(WindupPackage.eINSTANCE.getConfigurationElement_Inputs(),
				 WindupFactory.eINSTANCE.createInput()));

		newChildDescriptors.add
			(createChildParameter
				(WindupPackage.eINSTANCE.getConfigurationElement_Options(),
				 WindupFactory.eINSTANCE.createPair()));
	}

}
