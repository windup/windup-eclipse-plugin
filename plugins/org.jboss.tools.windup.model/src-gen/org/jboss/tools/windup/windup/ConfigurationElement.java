/**
 */
package org.jboss.tools.windup.windup;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Configuration Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.jboss.tools.windup.windup.ConfigurationElement#getInputs <em>Inputs</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.ConfigurationElement#getWindupResult <em>Windup Result</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.ConfigurationElement#getWindupHome <em>Windup Home</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.ConfigurationElement#getGeneratedReportLocation <em>Generated Report Location</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.ConfigurationElement#isSourceMode <em>Source Mode</em>}</li>
 * </ul>
 *
 * @see org.jboss.tools.windup.windup.WindupPackage#getConfigurationElement()
 * @model
 * @generated
 */
public interface ConfigurationElement extends Parameterized {
	/**
	 * Returns the value of the '<em><b>Inputs</b></em>' containment reference list.
	 * The list contents are of type {@link org.jboss.tools.windup.windup.Input}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Inputs</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Inputs</em>' containment reference list.
	 * @see org.jboss.tools.windup.windup.WindupPackage#getConfigurationElement_Inputs()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<Input> getInputs();

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
	 * @see org.jboss.tools.windup.windup.WindupPackage#getConfigurationElement_WindupResult()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	WindupResult getWindupResult();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.ConfigurationElement#getWindupResult <em>Windup Result</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Windup Result</em>' containment reference.
	 * @see #getWindupResult()
	 * @generated
	 */
	void setWindupResult(WindupResult value);

	/**
	 * Returns the value of the '<em><b>Windup Home</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Windup Home</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Windup Home</em>' attribute.
	 * @see #setWindupHome(String)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getConfigurationElement_WindupHome()
	 * @model
	 * @generated
	 */
	String getWindupHome();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.ConfigurationElement#getWindupHome <em>Windup Home</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Windup Home</em>' attribute.
	 * @see #getWindupHome()
	 * @generated
	 */
	void setWindupHome(String value);

	/**
	 * Returns the value of the '<em><b>Generated Report Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Generated Report Location</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Generated Report Location</em>' attribute.
	 * @see #setGeneratedReportLocation(String)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getConfigurationElement_GeneratedReportLocation()
	 * @model
	 * @generated
	 */
	String getGeneratedReportLocation();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.ConfigurationElement#getGeneratedReportLocation <em>Generated Report Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Generated Report Location</em>' attribute.
	 * @see #getGeneratedReportLocation()
	 * @generated
	 */
	void setGeneratedReportLocation(String value);

	/**
	 * Returns the value of the '<em><b>Source Mode</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source Mode</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source Mode</em>' attribute.
	 * @see #setSourceMode(boolean)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getConfigurationElement_SourceMode()
	 * @model
	 * @generated
	 */
	boolean isSourceMode();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.ConfigurationElement#isSourceMode <em>Source Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source Mode</em>' attribute.
	 * @see #isSourceMode()
	 * @generated
	 */
	void setSourceMode(boolean value);

} // ConfigurationElement
