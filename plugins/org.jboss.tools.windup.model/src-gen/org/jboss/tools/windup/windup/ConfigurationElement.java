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
 *   <li>{@link org.jboss.tools.windup.windup.ConfigurationElement#getWindupHome <em>Windup Home</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.ConfigurationElement#isSourceMode <em>Source Mode</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.ConfigurationElement#getGeneratedReportsLocation <em>Generated Reports Location</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.ConfigurationElement#getPackages <em>Packages</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.ConfigurationElement#getTimestamp <em>Timestamp</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.ConfigurationElement#isGenerateGraph <em>Generate Graph</em>}</li>
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

	/**
	 * Returns the value of the '<em><b>Generated Reports Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Generated Reports Location</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Generated Reports Location</em>' attribute.
	 * @see #setGeneratedReportsLocation(String)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getConfigurationElement_GeneratedReportsLocation()
	 * @model
	 * @generated
	 */
	String getGeneratedReportsLocation();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.ConfigurationElement#getGeneratedReportsLocation <em>Generated Reports Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Generated Reports Location</em>' attribute.
	 * @see #getGeneratedReportsLocation()
	 * @generated
	 */
	void setGeneratedReportsLocation(String value);

	/**
	 * Returns the value of the '<em><b>Packages</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Packages</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Packages</em>' attribute list.
	 * @see org.jboss.tools.windup.windup.WindupPackage#getConfigurationElement_Packages()
	 * @model
	 * @generated
	 */
	EList<String> getPackages();

	/**
	 * Returns the value of the '<em><b>Timestamp</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Timestamp</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Timestamp</em>' attribute.
	 * @see #setTimestamp(String)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getConfigurationElement_Timestamp()
	 * @model
	 * @generated
	 */
	String getTimestamp();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.ConfigurationElement#getTimestamp <em>Timestamp</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Timestamp</em>' attribute.
	 * @see #getTimestamp()
	 * @generated
	 */
	void setTimestamp(String value);

	/**
	 * Returns the value of the '<em><b>Generate Graph</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Generate Graph</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Generate Graph</em>' attribute.
	 * @see #setGenerateGraph(boolean)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getConfigurationElement_GenerateGraph()
	 * @model
	 * @generated
	 */
	boolean isGenerateGraph();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.ConfigurationElement#isGenerateGraph <em>Generate Graph</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Generate Graph</em>' attribute.
	 * @see #isGenerateGraph()
	 * @generated
	 */
	void setGenerateGraph(boolean value);

} // ConfigurationElement
