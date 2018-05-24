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
 *   <li>{@link org.jboss.tools.windup.windup.ConfigurationElement#getOutputLocation <em>Output Location</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.ConfigurationElement#getPackages <em>Packages</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.ConfigurationElement#getTimestamp <em>Timestamp</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.ConfigurationElement#getMigrationPath <em>Migration Path</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.ConfigurationElement#getUserRulesDirectories <em>User Rules Directories</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.ConfigurationElement#getOptions <em>Options</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.ConfigurationElement#getJreHome <em>Jre Home</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.ConfigurationElement#isGenerateReport <em>Generate Report</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.ConfigurationElement#getWindupResult <em>Windup Result</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.ConfigurationElement#getReports <em>Reports</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.ConfigurationElement#getIgnorePatterns <em>Ignore Patterns</em>}</li>
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
	 * Returns the value of the '<em><b>Output Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Output Location</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Output Location</em>' attribute.
	 * @see #setOutputLocation(String)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getConfigurationElement_OutputLocation()
	 * @model
	 * @generated
	 */
	String getOutputLocation();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.ConfigurationElement#getOutputLocation <em>Output Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Output Location</em>' attribute.
	 * @see #getOutputLocation()
	 * @generated
	 */
	void setOutputLocation(String value);

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
	 * Returns the value of the '<em><b>Migration Path</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Migration Path</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Migration Path</em>' reference.
	 * @see #setMigrationPath(MigrationPath)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getConfigurationElement_MigrationPath()
	 * @model
	 * @generated
	 */
	MigrationPath getMigrationPath();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.ConfigurationElement#getMigrationPath <em>Migration Path</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Migration Path</em>' reference.
	 * @see #getMigrationPath()
	 * @generated
	 */
	void setMigrationPath(MigrationPath value);

	/**
	 * Returns the value of the '<em><b>User Rules Directories</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>User Rules Directories</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>User Rules Directories</em>' attribute list.
	 * @see org.jboss.tools.windup.windup.WindupPackage#getConfigurationElement_UserRulesDirectories()
	 * @model
	 * @generated
	 */
	EList<String> getUserRulesDirectories();

	/**
	 * Returns the value of the '<em><b>Options</b></em>' containment reference list.
	 * The list contents are of type {@link org.jboss.tools.windup.windup.Pair}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Options</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Options</em>' containment reference list.
	 * @see org.jboss.tools.windup.windup.WindupPackage#getConfigurationElement_Options()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<Pair> getOptions();

	/**
	 * Returns the value of the '<em><b>Jre Home</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Jre Home</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Jre Home</em>' attribute.
	 * @see #setJreHome(String)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getConfigurationElement_JreHome()
	 * @model
	 * @generated
	 */
	String getJreHome();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.ConfigurationElement#getJreHome <em>Jre Home</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Jre Home</em>' attribute.
	 * @see #getJreHome()
	 * @generated
	 */
	void setJreHome(String value);

	/**
	 * Returns the value of the '<em><b>Generate Report</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Generate Report</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Generate Report</em>' attribute.
	 * @see #setGenerateReport(boolean)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getConfigurationElement_GenerateReport()
	 * @model
	 * @generated
	 */
	boolean isGenerateReport();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.ConfigurationElement#isGenerateReport <em>Generate Report</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Generate Report</em>' attribute.
	 * @see #isGenerateReport()
	 * @generated
	 */
	void setGenerateReport(boolean value);

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
	 * Returns the value of the '<em><b>Reports</b></em>' containment reference list.
	 * The list contents are of type {@link org.jboss.tools.windup.windup.Report}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Reports</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Reports</em>' containment reference list.
	 * @see org.jboss.tools.windup.windup.WindupPackage#getConfigurationElement_Reports()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<Report> getReports();

	/**
	 * Returns the value of the '<em><b>Ignore Patterns</b></em>' containment reference list.
	 * The list contents are of type {@link org.jboss.tools.windup.windup.IgnorePattern}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ignore Patterns</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ignore Patterns</em>' containment reference list.
	 * @see org.jboss.tools.windup.windup.WindupPackage#getConfigurationElement_IgnorePatterns()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<IgnorePattern> getIgnorePatterns();

} // ConfigurationElement
