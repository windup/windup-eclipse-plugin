/**
 */
package org.jboss.tools.windup.windup;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Technology</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.jboss.tools.windup.windup.Technology#getId <em>Id</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.Technology#getVersionRange <em>Version Range</em>}</li>
 * </ul>
 *
 * @see org.jboss.tools.windup.windup.WindupPackage#getTechnology()
 * @model
 * @generated
 */
public interface Technology extends EObject {
	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getTechnology_Id()
	 * @model
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.Technology#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

	/**
	 * Returns the value of the '<em><b>Version Range</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Version Range</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Version Range</em>' attribute.
	 * @see #setVersionRange(String)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getTechnology_VersionRange()
	 * @model
	 * @generated
	 */
	String getVersionRange();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.Technology#getVersionRange <em>Version Range</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Version Range</em>' attribute.
	 * @see #getVersionRange()
	 * @generated
	 */
	void setVersionRange(String value);

} // Technology
