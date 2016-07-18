/**
 */
package org.jboss.tools.windup.windup;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.jboss.tools.windup.windup.WindupFactory
 * @model kind="package"
 * @generated
 */
public interface WindupPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "windup";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "org.jboss.tools.windup";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "windup";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	WindupPackage eINSTANCE = org.jboss.tools.windup.windup.impl.WindupPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.jboss.tools.windup.windup.impl.NamedElementImpl <em>Named Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.windup.windup.impl.NamedElementImpl
	 * @see org.jboss.tools.windup.windup.impl.WindupPackageImpl#getNamedElement()
	 * @generated
	 */
	int NAMED_ELEMENT = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT__NAME = 0;

	/**
	 * The number of structural features of the '<em>Named Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT_FEATURE_COUNT = 1;

	/**
	 * The operation id for the '<em>Validate</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT___VALIDATE__DIAGNOSTICCHAIN_MAP = 0;

	/**
	 * The number of operations of the '<em>Named Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT_OPERATION_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.jboss.tools.windup.windup.impl.ParameterizedImpl <em>Parameterized</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.windup.windup.impl.ParameterizedImpl
	 * @see org.jboss.tools.windup.windup.impl.WindupPackageImpl#getParameterized()
	 * @generated
	 */
	int PARAMETERIZED = 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETERIZED__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETERIZED__PARAMETERS = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Parameterized</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETERIZED_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Validate</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETERIZED___VALIDATE__DIAGNOSTICCHAIN_MAP = NAMED_ELEMENT___VALIDATE__DIAGNOSTICCHAIN_MAP;

	/**
	 * The number of operations of the '<em>Parameterized</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETERIZED_OPERATION_COUNT = NAMED_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.jboss.tools.windup.windup.impl.ConfigurationElementImpl <em>Configuration Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.windup.windup.impl.ConfigurationElementImpl
	 * @see org.jboss.tools.windup.windup.impl.WindupPackageImpl#getConfigurationElement()
	 * @generated
	 */
	int CONFIGURATION_ELEMENT = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_ELEMENT__NAME = PARAMETERIZED__NAME;

	/**
	 * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_ELEMENT__PARAMETERS = PARAMETERIZED__PARAMETERS;

	/**
	 * The feature id for the '<em><b>Inputs</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_ELEMENT__INPUTS = PARAMETERIZED_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Windup Result</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_ELEMENT__WINDUP_RESULT = PARAMETERIZED_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Windup Home</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_ELEMENT__WINDUP_HOME = PARAMETERIZED_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Generated Report Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_ELEMENT__GENERATED_REPORT_LOCATION = PARAMETERIZED_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Source Mode</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_ELEMENT__SOURCE_MODE = PARAMETERIZED_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Configuration Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_ELEMENT_FEATURE_COUNT = PARAMETERIZED_FEATURE_COUNT + 5;

	/**
	 * The operation id for the '<em>Validate</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_ELEMENT___VALIDATE__DIAGNOSTICCHAIN_MAP = PARAMETERIZED___VALIDATE__DIAGNOSTICCHAIN_MAP;

	/**
	 * The number of operations of the '<em>Configuration Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_ELEMENT_OPERATION_COUNT = PARAMETERIZED_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.jboss.tools.windup.windup.impl.ParameterImpl <em>Parameter</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.windup.windup.impl.ParameterImpl
	 * @see org.jboss.tools.windup.windup.impl.WindupPackageImpl#getParameter()
	 * @generated
	 */
	int PARAMETER = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__VALUE = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Validate</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER___VALIDATE__DIAGNOSTICCHAIN_MAP = NAMED_ELEMENT___VALIDATE__DIAGNOSTICCHAIN_MAP;

	/**
	 * The number of operations of the '<em>Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_OPERATION_COUNT = NAMED_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.jboss.tools.windup.windup.impl.WindupModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.windup.windup.impl.WindupModelImpl
	 * @see org.jboss.tools.windup.windup.impl.WindupPackageImpl#getWindupModel()
	 * @generated
	 */
	int WINDUP_MODEL = 4;

	/**
	 * The feature id for the '<em><b>Configuration Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WINDUP_MODEL__CONFIGURATION_ELEMENTS = 0;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WINDUP_MODEL_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WINDUP_MODEL_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.jboss.tools.windup.windup.impl.InputImpl <em>Input</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.windup.windup.impl.InputImpl
	 * @see org.jboss.tools.windup.windup.impl.WindupPackageImpl#getInput()
	 * @generated
	 */
	int INPUT = 5;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Uri</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT__URI = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Input</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Validate</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT___VALIDATE__DIAGNOSTICCHAIN_MAP = NAMED_ELEMENT___VALIDATE__DIAGNOSTICCHAIN_MAP;

	/**
	 * The number of operations of the '<em>Input</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_OPERATION_COUNT = NAMED_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.jboss.tools.windup.windup.impl.WindupResultImpl <em>Result</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.windup.windup.impl.WindupResultImpl
	 * @see org.jboss.tools.windup.windup.impl.WindupPackageImpl#getWindupResult()
	 * @generated
	 */
	int WINDUP_RESULT = 6;

	/**
	 * The feature id for the '<em><b>Execution Results</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WINDUP_RESULT__EXECUTION_RESULTS = 0;

	/**
	 * The number of structural features of the '<em>Result</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WINDUP_RESULT_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Result</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WINDUP_RESULT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '<em>Execution Results</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.windup.tooling.ExecutionResults
	 * @see org.jboss.tools.windup.windup.impl.WindupPackageImpl#getWindupExecutionResults()
	 * @generated
	 */
	int WINDUP_EXECUTION_RESULTS = 7;


	/**
	 * Returns the meta object for class '{@link org.jboss.tools.windup.windup.NamedElement <em>Named Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Named Element</em>'.
	 * @see org.jboss.tools.windup.windup.NamedElement
	 * @generated
	 */
	EClass getNamedElement();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.NamedElement#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.jboss.tools.windup.windup.NamedElement#getName()
	 * @see #getNamedElement()
	 * @generated
	 */
	EAttribute getNamedElement_Name();

	/**
	 * Returns the meta object for the '{@link org.jboss.tools.windup.windup.NamedElement#validate(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map) <em>Validate</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Validate</em>' operation.
	 * @see org.jboss.tools.windup.windup.NamedElement#validate(org.eclipse.emf.common.util.DiagnosticChain, java.util.Map)
	 * @generated
	 */
	EOperation getNamedElement__Validate__DiagnosticChain_Map();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.windup.windup.ConfigurationElement <em>Configuration Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Configuration Element</em>'.
	 * @see org.jboss.tools.windup.windup.ConfigurationElement
	 * @generated
	 */
	EClass getConfigurationElement();

	/**
	 * Returns the meta object for the containment reference list '{@link org.jboss.tools.windup.windup.ConfigurationElement#getInputs <em>Inputs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Inputs</em>'.
	 * @see org.jboss.tools.windup.windup.ConfigurationElement#getInputs()
	 * @see #getConfigurationElement()
	 * @generated
	 */
	EReference getConfigurationElement_Inputs();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.windup.windup.ConfigurationElement#getWindupResult <em>Windup Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Windup Result</em>'.
	 * @see org.jboss.tools.windup.windup.ConfigurationElement#getWindupResult()
	 * @see #getConfigurationElement()
	 * @generated
	 */
	EReference getConfigurationElement_WindupResult();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.ConfigurationElement#getWindupHome <em>Windup Home</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Windup Home</em>'.
	 * @see org.jboss.tools.windup.windup.ConfigurationElement#getWindupHome()
	 * @see #getConfigurationElement()
	 * @generated
	 */
	EAttribute getConfigurationElement_WindupHome();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.ConfigurationElement#getGeneratedReportLocation <em>Generated Report Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Generated Report Location</em>'.
	 * @see org.jboss.tools.windup.windup.ConfigurationElement#getGeneratedReportLocation()
	 * @see #getConfigurationElement()
	 * @generated
	 */
	EAttribute getConfigurationElement_GeneratedReportLocation();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.ConfigurationElement#isSourceMode <em>Source Mode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Source Mode</em>'.
	 * @see org.jboss.tools.windup.windup.ConfigurationElement#isSourceMode()
	 * @see #getConfigurationElement()
	 * @generated
	 */
	EAttribute getConfigurationElement_SourceMode();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.windup.windup.Parameter <em>Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Parameter</em>'.
	 * @see org.jboss.tools.windup.windup.Parameter
	 * @generated
	 */
	EClass getParameter();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.Parameter#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.jboss.tools.windup.windup.Parameter#getValue()
	 * @see #getParameter()
	 * @generated
	 */
	EAttribute getParameter_Value();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.windup.windup.Parameterized <em>Parameterized</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Parameterized</em>'.
	 * @see org.jboss.tools.windup.windup.Parameterized
	 * @generated
	 */
	EClass getParameterized();

	/**
	 * Returns the meta object for the containment reference list '{@link org.jboss.tools.windup.windup.Parameterized#getParameters <em>Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Parameters</em>'.
	 * @see org.jboss.tools.windup.windup.Parameterized#getParameters()
	 * @see #getParameterized()
	 * @generated
	 */
	EReference getParameterized_Parameters();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.windup.windup.WindupModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see org.jboss.tools.windup.windup.WindupModel
	 * @generated
	 */
	EClass getWindupModel();

	/**
	 * Returns the meta object for the containment reference list '{@link org.jboss.tools.windup.windup.WindupModel#getConfigurationElements <em>Configuration Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Configuration Elements</em>'.
	 * @see org.jboss.tools.windup.windup.WindupModel#getConfigurationElements()
	 * @see #getWindupModel()
	 * @generated
	 */
	EReference getWindupModel_ConfigurationElements();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.windup.windup.Input <em>Input</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Input</em>'.
	 * @see org.jboss.tools.windup.windup.Input
	 * @generated
	 */
	EClass getInput();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.Input#getUri <em>Uri</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Uri</em>'.
	 * @see org.jboss.tools.windup.windup.Input#getUri()
	 * @see #getInput()
	 * @generated
	 */
	EAttribute getInput_Uri();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.windup.windup.WindupResult <em>Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Result</em>'.
	 * @see org.jboss.tools.windup.windup.WindupResult
	 * @generated
	 */
	EClass getWindupResult();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.WindupResult#getExecutionResults <em>Execution Results</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Execution Results</em>'.
	 * @see org.jboss.tools.windup.windup.WindupResult#getExecutionResults()
	 * @see #getWindupResult()
	 * @generated
	 */
	EAttribute getWindupResult_ExecutionResults();

	/**
	 * Returns the meta object for data type '{@link org.jboss.windup.tooling.ExecutionResults <em>Execution Results</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Execution Results</em>'.
	 * @see org.jboss.windup.tooling.ExecutionResults
	 * @model instanceClass="org.jboss.windup.tooling.ExecutionResults" serializeable="false"
	 * @generated
	 */
	EDataType getWindupExecutionResults();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	WindupFactory getWindupFactory();

} //WindupPackage
