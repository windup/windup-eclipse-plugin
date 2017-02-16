/**
 */
package org.jboss.tools.windup.windup;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.jboss.tools.windup.windup.WindupModel#getConfigurationElements <em>Configuration Elements</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.WindupModel#getMigrationPaths <em>Migration Paths</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.WindupModel#getCustomRuleRepositories <em>Custom Rule Repositories</em>}</li>
 * </ul>
 *
 * @see org.jboss.tools.windup.windup.WindupPackage#getWindupModel()
 * @model
 * @generated
 */
public interface WindupModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Configuration Elements</b></em>' containment reference list.
	 * The list contents are of type {@link org.jboss.tools.windup.windup.ConfigurationElement}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Configuration Elements</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Configuration Elements</em>' containment reference list.
	 * @see org.jboss.tools.windup.windup.WindupPackage#getWindupModel_ConfigurationElements()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<ConfigurationElement> getConfigurationElements();

	/**
	 * Returns the value of the '<em><b>Migration Paths</b></em>' containment reference list.
	 * The list contents are of type {@link org.jboss.tools.windup.windup.MigrationPath}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Migration Paths</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Migration Paths</em>' containment reference list.
	 * @see org.jboss.tools.windup.windup.WindupPackage#getWindupModel_MigrationPaths()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<MigrationPath> getMigrationPaths();

	/**
	 * Returns the value of the '<em><b>Custom Rule Repositories</b></em>' containment reference list.
	 * The list contents are of type {@link org.jboss.tools.windup.windup.CustomRuleProvider}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Custom Rule Repositories</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Custom Rule Repositories</em>' containment reference list.
	 * @see org.jboss.tools.windup.windup.WindupPackage#getWindupModel_CustomRuleRepositories()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<CustomRuleProvider> getCustomRuleRepositories();

} // WindupModel
