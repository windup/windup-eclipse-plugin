/**
 */
package org.jboss.tools.windup.windup.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.jboss.tools.windup.windup.ConfigurationElement;
import org.jboss.tools.windup.windup.MigrationPath;
import org.jboss.tools.windup.windup.WindupModel;
import org.jboss.tools.windup.windup.WindupPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.jboss.tools.windup.windup.impl.WindupModelImpl#getConfigurationElements <em>Configuration Elements</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.WindupModelImpl#getMigrationPaths <em>Migration Paths</em>}</li>
 * </ul>
 *
 * @generated
 */
public class WindupModelImpl extends MinimalEObjectImpl.Container implements WindupModel {
	/**
	 * The cached value of the '{@link #getConfigurationElements() <em>Configuration Elements</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConfigurationElements()
	 * @generated
	 * @ordered
	 */
	protected EList<ConfigurationElement> configurationElements;

	/**
	 * The cached value of the '{@link #getMigrationPaths() <em>Migration Paths</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMigrationPaths()
	 * @generated
	 * @ordered
	 */
	protected EList<MigrationPath> migrationPaths;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected WindupModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return WindupPackage.eINSTANCE.getWindupModel();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ConfigurationElement> getConfigurationElements() {
		if (configurationElements == null) {
			configurationElements = new EObjectContainmentEList.Resolving<ConfigurationElement>(ConfigurationElement.class, this, WindupPackage.WINDUP_MODEL__CONFIGURATION_ELEMENTS);
		}
		return configurationElements;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MigrationPath> getMigrationPaths() {
		if (migrationPaths == null) {
			migrationPaths = new EObjectContainmentEList.Resolving<MigrationPath>(MigrationPath.class, this, WindupPackage.WINDUP_MODEL__MIGRATION_PATHS);
		}
		return migrationPaths;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case WindupPackage.WINDUP_MODEL__CONFIGURATION_ELEMENTS:
				return ((InternalEList<?>)getConfigurationElements()).basicRemove(otherEnd, msgs);
			case WindupPackage.WINDUP_MODEL__MIGRATION_PATHS:
				return ((InternalEList<?>)getMigrationPaths()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case WindupPackage.WINDUP_MODEL__CONFIGURATION_ELEMENTS:
				return getConfigurationElements();
			case WindupPackage.WINDUP_MODEL__MIGRATION_PATHS:
				return getMigrationPaths();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case WindupPackage.WINDUP_MODEL__CONFIGURATION_ELEMENTS:
				getConfigurationElements().clear();
				getConfigurationElements().addAll((Collection<? extends ConfigurationElement>)newValue);
				return;
			case WindupPackage.WINDUP_MODEL__MIGRATION_PATHS:
				getMigrationPaths().clear();
				getMigrationPaths().addAll((Collection<? extends MigrationPath>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case WindupPackage.WINDUP_MODEL__CONFIGURATION_ELEMENTS:
				getConfigurationElements().clear();
				return;
			case WindupPackage.WINDUP_MODEL__MIGRATION_PATHS:
				getMigrationPaths().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case WindupPackage.WINDUP_MODEL__CONFIGURATION_ELEMENTS:
				return configurationElements != null && !configurationElements.isEmpty();
			case WindupPackage.WINDUP_MODEL__MIGRATION_PATHS:
				return migrationPaths != null && !migrationPaths.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //WindupModelImpl
