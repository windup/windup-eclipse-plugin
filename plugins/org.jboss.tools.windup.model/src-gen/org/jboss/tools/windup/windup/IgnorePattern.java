/**
 */
package org.jboss.tools.windup.windup;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Ignore Pattern</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.jboss.tools.windup.windup.IgnorePattern#getPattern <em>Pattern</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.IgnorePattern#isEnabled <em>Enabled</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.IgnorePattern#isReadFromFile <em>Read From File</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.IgnorePattern#isRemoved <em>Removed</em>}</li>
 * </ul>
 *
 * @see org.jboss.tools.windup.windup.WindupPackage#getIgnorePattern()
 * @model
 * @generated
 */
public interface IgnorePattern extends EObject {
	/**
	 * Returns the value of the '<em><b>Pattern</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Pattern</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pattern</em>' attribute.
	 * @see #setPattern(String)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getIgnorePattern_Pattern()
	 * @model
	 * @generated
	 */
	String getPattern();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.IgnorePattern#getPattern <em>Pattern</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pattern</em>' attribute.
	 * @see #getPattern()
	 * @generated
	 */
	void setPattern(String value);

	/**
	 * Returns the value of the '<em><b>Enabled</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Enabled</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Enabled</em>' attribute.
	 * @see #setEnabled(boolean)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getIgnorePattern_Enabled()
	 * @model
	 * @generated
	 */
	boolean isEnabled();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.IgnorePattern#isEnabled <em>Enabled</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Enabled</em>' attribute.
	 * @see #isEnabled()
	 * @generated
	 */
	void setEnabled(boolean value);

	/**
	 * Returns the value of the '<em><b>Read From File</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Read From File</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Read From File</em>' attribute.
	 * @see #setReadFromFile(boolean)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getIgnorePattern_ReadFromFile()
	 * @model
	 * @generated
	 */
	boolean isReadFromFile();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.IgnorePattern#isReadFromFile <em>Read From File</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Read From File</em>' attribute.
	 * @see #isReadFromFile()
	 * @generated
	 */
	void setReadFromFile(boolean value);

	/**
	 * Returns the value of the '<em><b>Removed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Removed</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Removed</em>' attribute.
	 * @see #setRemoved(boolean)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getIgnorePattern_Removed()
	 * @model
	 * @generated
	 */
	boolean isRemoved();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.IgnorePattern#isRemoved <em>Removed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Removed</em>' attribute.
	 * @see #isRemoved()
	 * @generated
	 */
	void setRemoved(boolean value);

} // IgnorePattern
