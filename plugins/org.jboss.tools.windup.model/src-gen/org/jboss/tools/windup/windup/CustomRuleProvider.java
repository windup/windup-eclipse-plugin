/**
 */
package org.jboss.tools.windup.windup;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Custom Rule Provider</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.jboss.tools.windup.windup.CustomRuleProvider#getLocationURI <em>Location URI</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.CustomRuleProvider#getRulesetId <em>Ruleset Id</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.CustomRuleProvider#isExternal <em>External</em>}</li>
 * </ul>
 *
 * @see org.jboss.tools.windup.windup.WindupPackage#getCustomRuleProvider()
 * @model
 * @generated
 */
public interface CustomRuleProvider extends EObject {
	/**
	 * Returns the value of the '<em><b>Location URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Location URI</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Location URI</em>' attribute.
	 * @see #setLocationURI(String)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getCustomRuleProvider_LocationURI()
	 * @model
	 * @generated
	 */
	String getLocationURI();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.CustomRuleProvider#getLocationURI <em>Location URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Location URI</em>' attribute.
	 * @see #getLocationURI()
	 * @generated
	 */
	void setLocationURI(String value);

	/**
	 * Returns the value of the '<em><b>Ruleset Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ruleset Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ruleset Id</em>' attribute.
	 * @see #setRulesetId(String)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getCustomRuleProvider_RulesetId()
	 * @model
	 * @generated
	 */
	String getRulesetId();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.CustomRuleProvider#getRulesetId <em>Ruleset Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ruleset Id</em>' attribute.
	 * @see #getRulesetId()
	 * @generated
	 */
	void setRulesetId(String value);

	/**
	 * Returns the value of the '<em><b>External</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>External</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>External</em>' attribute.
	 * @see #setExternal(boolean)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getCustomRuleProvider_External()
	 * @model
	 * @generated
	 */
	boolean isExternal();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.CustomRuleProvider#isExternal <em>External</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>External</em>' attribute.
	 * @see #isExternal()
	 * @generated
	 */
	void setExternal(boolean value);

} // CustomRuleProvider
