/**
 */
package org.jboss.tools.windup.windup;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Input</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.jboss.tools.windup.windup.Input#getUri <em>Uri</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.Input#getWindupResult <em>Windup Result</em>}</li>
 * </ul>
 *
 * @see org.jboss.tools.windup.windup.WindupPackage#getInput()
 * @model
 * @generated
 */
public interface Input extends NamedElement {
	/**
	 * Returns the value of the '<em><b>Uri</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Uri</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Uri</em>' attribute.
	 * @see #setUri(String)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getInput_Uri()
	 * @model
	 * @generated
	 */
	String getUri();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.Input#getUri <em>Uri</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Uri</em>' attribute.
	 * @see #getUri()
	 * @generated
	 */
	void setUri(String value);

	/**
	 * Returns the value of the '<em><b>Windup Result</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Windup Result</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Windup Result</em>' containment reference.
	 * @see #setWindupResult(WindupResult)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getInput_WindupResult()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	WindupResult getWindupResult();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.Input#getWindupResult <em>Windup Result</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Windup Result</em>' containment reference.
	 * @see #getWindupResult()
	 * @generated
	 */
	void setWindupResult(WindupResult value);

} // Input
