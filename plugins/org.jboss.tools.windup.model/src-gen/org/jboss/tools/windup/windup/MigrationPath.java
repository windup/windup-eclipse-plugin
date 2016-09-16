/**
 */
package org.jboss.tools.windup.windup;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Migration Path</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.jboss.tools.windup.windup.MigrationPath#getId <em>Id</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.MigrationPath#getSource <em>Source</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.MigrationPath#getTarget <em>Target</em>}</li>
 * </ul>
 *
 * @see org.jboss.tools.windup.windup.WindupPackage#getMigrationPath()
 * @model
 * @generated
 */
public interface MigrationPath extends NamedElement {
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
	 * @see org.jboss.tools.windup.windup.WindupPackage#getMigrationPath_Id()
	 * @model
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.MigrationPath#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

	/**
	 * Returns the value of the '<em><b>Source</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source</em>' containment reference.
	 * @see #setSource(Technology)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getMigrationPath_Source()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	Technology getSource();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.MigrationPath#getSource <em>Source</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source</em>' containment reference.
	 * @see #getSource()
	 * @generated
	 */
	void setSource(Technology value);

	/**
	 * Returns the value of the '<em><b>Target</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target</em>' containment reference.
	 * @see #setTarget(Technology)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getMigrationPath_Target()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	Technology getTarget();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.MigrationPath#getTarget <em>Target</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target</em>' containment reference.
	 * @see #getTarget()
	 * @generated
	 */
	void setTarget(Technology value);

} // MigrationPath
