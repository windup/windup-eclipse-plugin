/**
 */
package org.jboss.tools.windup.windup;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Report</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.jboss.tools.windup.windup.Report#getLocation <em>Location</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.Report#getInputFile <em>Input File</em>}</li>
 * </ul>
 *
 * @see org.jboss.tools.windup.windup.WindupPackage#getReport()
 * @model
 * @generated
 */
public interface Report extends EObject {
	/**
	 * Returns the value of the '<em><b>Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Location</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Location</em>' attribute.
	 * @see #setLocation(String)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getReport_Location()
	 * @model
	 * @generated
	 */
	String getLocation();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.Report#getLocation <em>Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Location</em>' attribute.
	 * @see #getLocation()
	 * @generated
	 */
	void setLocation(String value);

	/**
	 * Returns the value of the '<em><b>Input File</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Input File</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Input File</em>' attribute.
	 * @see #setInputFile(String)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getReport_InputFile()
	 * @model
	 * @generated
	 */
	String getInputFile();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.Report#getInputFile <em>Input File</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Input File</em>' attribute.
	 * @see #getInputFile()
	 * @generated
	 */
	void setInputFile(String value);

} // Report
