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
	 * The feature id for the '<em><b>Windup Home</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_ELEMENT__WINDUP_HOME = PARAMETERIZED_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Source Mode</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_ELEMENT__SOURCE_MODE = PARAMETERIZED_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Generated Reports Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_ELEMENT__GENERATED_REPORTS_LOCATION = PARAMETERIZED_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Packages</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_ELEMENT__PACKAGES = PARAMETERIZED_FEATURE_COUNT + 4;

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
	 * The feature id for the '<em><b>Windup Result</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT__WINDUP_RESULT = NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Generated Report Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT__GENERATED_REPORT_LOCATION = NAMED_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Input</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 3;

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
	 * The feature id for the '<em><b>Issues</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WINDUP_RESULT__ISSUES = 1;

	/**
	 * The number of structural features of the '<em>Result</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WINDUP_RESULT_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Result</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WINDUP_RESULT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.jboss.tools.windup.windup.impl.IssueImpl <em>Issue</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.windup.windup.impl.IssueImpl
	 * @see org.jboss.tools.windup.windup.impl.WindupPackageImpl#getIssue()
	 * @generated
	 */
	int ISSUE = 7;

	/**
	 * The feature id for the '<em><b>Element Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISSUE__ELEMENT_ID = 0;

	/**
	 * The feature id for the '<em><b>Links</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISSUE__LINKS = 1;

	/**
	 * The feature id for the '<em><b>File Absolute Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISSUE__FILE_ABSOLUTE_PATH = 2;

	/**
	 * The feature id for the '<em><b>Severity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISSUE__SEVERITY = 3;

	/**
	 * The feature id for the '<em><b>Rule Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISSUE__RULE_ID = 4;

	/**
	 * The feature id for the '<em><b>Effort</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISSUE__EFFORT = 5;

	/**
	 * The feature id for the '<em><b>Fixed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISSUE__FIXED = 6;

	/**
	 * The number of structural features of the '<em>Issue</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISSUE_FEATURE_COUNT = 7;

	/**
	 * The number of operations of the '<em>Issue</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISSUE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.jboss.tools.windup.windup.impl.HintImpl <em>Hint</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.windup.windup.impl.HintImpl
	 * @see org.jboss.tools.windup.windup.impl.WindupPackageImpl#getHint()
	 * @generated
	 */
	int HINT = 8;

	/**
	 * The feature id for the '<em><b>Element Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HINT__ELEMENT_ID = ISSUE__ELEMENT_ID;

	/**
	 * The feature id for the '<em><b>Links</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HINT__LINKS = ISSUE__LINKS;

	/**
	 * The feature id for the '<em><b>File Absolute Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HINT__FILE_ABSOLUTE_PATH = ISSUE__FILE_ABSOLUTE_PATH;

	/**
	 * The feature id for the '<em><b>Severity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HINT__SEVERITY = ISSUE__SEVERITY;

	/**
	 * The feature id for the '<em><b>Rule Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HINT__RULE_ID = ISSUE__RULE_ID;

	/**
	 * The feature id for the '<em><b>Effort</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HINT__EFFORT = ISSUE__EFFORT;

	/**
	 * The feature id for the '<em><b>Fixed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HINT__FIXED = ISSUE__FIXED;

	/**
	 * The feature id for the '<em><b>Title</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HINT__TITLE = ISSUE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Hint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HINT__HINT = ISSUE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Line Number</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HINT__LINE_NUMBER = ISSUE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Column</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HINT__COLUMN = ISSUE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HINT__LENGTH = ISSUE_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Source Snippet</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HINT__SOURCE_SNIPPET = ISSUE_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Hint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HINT_FEATURE_COUNT = ISSUE_FEATURE_COUNT + 6;

	/**
	 * The number of operations of the '<em>Hint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HINT_OPERATION_COUNT = ISSUE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.jboss.tools.windup.windup.impl.ClassificationImpl <em>Classification</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.windup.windup.impl.ClassificationImpl
	 * @see org.jboss.tools.windup.windup.impl.WindupPackageImpl#getClassification()
	 * @generated
	 */
	int CLASSIFICATION = 9;

	/**
	 * The feature id for the '<em><b>Element Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASSIFICATION__ELEMENT_ID = ISSUE__ELEMENT_ID;

	/**
	 * The feature id for the '<em><b>Links</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASSIFICATION__LINKS = ISSUE__LINKS;

	/**
	 * The feature id for the '<em><b>File Absolute Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASSIFICATION__FILE_ABSOLUTE_PATH = ISSUE__FILE_ABSOLUTE_PATH;

	/**
	 * The feature id for the '<em><b>Severity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASSIFICATION__SEVERITY = ISSUE__SEVERITY;

	/**
	 * The feature id for the '<em><b>Rule Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASSIFICATION__RULE_ID = ISSUE__RULE_ID;

	/**
	 * The feature id for the '<em><b>Effort</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASSIFICATION__EFFORT = ISSUE__EFFORT;

	/**
	 * The feature id for the '<em><b>Fixed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASSIFICATION__FIXED = ISSUE__FIXED;

	/**
	 * The feature id for the '<em><b>Classification</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASSIFICATION__CLASSIFICATION = ISSUE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASSIFICATION__DESCRIPTION = ISSUE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Classification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASSIFICATION_FEATURE_COUNT = ISSUE_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Classification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASSIFICATION_OPERATION_COUNT = ISSUE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.jboss.tools.windup.windup.impl.LinkImpl <em>Link</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.windup.windup.impl.LinkImpl
	 * @see org.jboss.tools.windup.windup.impl.WindupPackageImpl#getLink()
	 * @generated
	 */
	int LINK = 10;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINK__DESCRIPTION = 0;

	/**
	 * The feature id for the '<em><b>Url</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINK__URL = 1;

	/**
	 * The number of structural features of the '<em>Link</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINK_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Link</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LINK_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '<em>Execution Results</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.windup.tooling.ExecutionResults
	 * @see org.jboss.tools.windup.windup.impl.WindupPackageImpl#getWindupExecutionResults()
	 * @generated
	 */
	int WINDUP_EXECUTION_RESULTS = 11;


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
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.ConfigurationElement#getGeneratedReportsLocation <em>Generated Reports Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Generated Reports Location</em>'.
	 * @see org.jboss.tools.windup.windup.ConfigurationElement#getGeneratedReportsLocation()
	 * @see #getConfigurationElement()
	 * @generated
	 */
	EAttribute getConfigurationElement_GeneratedReportsLocation();

	/**
	 * Returns the meta object for the attribute list '{@link org.jboss.tools.windup.windup.ConfigurationElement#getPackages <em>Packages</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Packages</em>'.
	 * @see org.jboss.tools.windup.windup.ConfigurationElement#getPackages()
	 * @see #getConfigurationElement()
	 * @generated
	 */
	EAttribute getConfigurationElement_Packages();

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
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.windup.windup.Input#getWindupResult <em>Windup Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Windup Result</em>'.
	 * @see org.jboss.tools.windup.windup.Input#getWindupResult()
	 * @see #getInput()
	 * @generated
	 */
	EReference getInput_WindupResult();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.Input#getGeneratedReportLocation <em>Generated Report Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Generated Report Location</em>'.
	 * @see org.jboss.tools.windup.windup.Input#getGeneratedReportLocation()
	 * @see #getInput()
	 * @generated
	 */
	EAttribute getInput_GeneratedReportLocation();

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
	 * Returns the meta object for the containment reference list '{@link org.jboss.tools.windup.windup.WindupResult#getIssues <em>Issues</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Issues</em>'.
	 * @see org.jboss.tools.windup.windup.WindupResult#getIssues()
	 * @see #getWindupResult()
	 * @generated
	 */
	EReference getWindupResult_Issues();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.windup.windup.Issue <em>Issue</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Issue</em>'.
	 * @see org.jboss.tools.windup.windup.Issue
	 * @generated
	 */
	EClass getIssue();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.Issue#getElementId <em>Element Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Element Id</em>'.
	 * @see org.jboss.tools.windup.windup.Issue#getElementId()
	 * @see #getIssue()
	 * @generated
	 */
	EAttribute getIssue_ElementId();

	/**
	 * Returns the meta object for the containment reference list '{@link org.jboss.tools.windup.windup.Issue#getLinks <em>Links</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Links</em>'.
	 * @see org.jboss.tools.windup.windup.Issue#getLinks()
	 * @see #getIssue()
	 * @generated
	 */
	EReference getIssue_Links();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.Issue#getFileAbsolutePath <em>File Absolute Path</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>File Absolute Path</em>'.
	 * @see org.jboss.tools.windup.windup.Issue#getFileAbsolutePath()
	 * @see #getIssue()
	 * @generated
	 */
	EAttribute getIssue_FileAbsolutePath();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.Issue#getSeverity <em>Severity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Severity</em>'.
	 * @see org.jboss.tools.windup.windup.Issue#getSeverity()
	 * @see #getIssue()
	 * @generated
	 */
	EAttribute getIssue_Severity();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.Issue#getRuleId <em>Rule Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Rule Id</em>'.
	 * @see org.jboss.tools.windup.windup.Issue#getRuleId()
	 * @see #getIssue()
	 * @generated
	 */
	EAttribute getIssue_RuleId();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.Issue#getEffort <em>Effort</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Effort</em>'.
	 * @see org.jboss.tools.windup.windup.Issue#getEffort()
	 * @see #getIssue()
	 * @generated
	 */
	EAttribute getIssue_Effort();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.Issue#isFixed <em>Fixed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fixed</em>'.
	 * @see org.jboss.tools.windup.windup.Issue#isFixed()
	 * @see #getIssue()
	 * @generated
	 */
	EAttribute getIssue_Fixed();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.windup.windup.Hint <em>Hint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Hint</em>'.
	 * @see org.jboss.tools.windup.windup.Hint
	 * @generated
	 */
	EClass getHint();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.Hint#getTitle <em>Title</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Title</em>'.
	 * @see org.jboss.tools.windup.windup.Hint#getTitle()
	 * @see #getHint()
	 * @generated
	 */
	EAttribute getHint_Title();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.Hint#getHint <em>Hint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Hint</em>'.
	 * @see org.jboss.tools.windup.windup.Hint#getHint()
	 * @see #getHint()
	 * @generated
	 */
	EAttribute getHint_Hint();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.Hint#getLineNumber <em>Line Number</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Line Number</em>'.
	 * @see org.jboss.tools.windup.windup.Hint#getLineNumber()
	 * @see #getHint()
	 * @generated
	 */
	EAttribute getHint_LineNumber();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.Hint#getColumn <em>Column</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Column</em>'.
	 * @see org.jboss.tools.windup.windup.Hint#getColumn()
	 * @see #getHint()
	 * @generated
	 */
	EAttribute getHint_Column();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.Hint#getLength <em>Length</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Length</em>'.
	 * @see org.jboss.tools.windup.windup.Hint#getLength()
	 * @see #getHint()
	 * @generated
	 */
	EAttribute getHint_Length();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.Hint#getSourceSnippet <em>Source Snippet</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Source Snippet</em>'.
	 * @see org.jboss.tools.windup.windup.Hint#getSourceSnippet()
	 * @see #getHint()
	 * @generated
	 */
	EAttribute getHint_SourceSnippet();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.windup.windup.Classification <em>Classification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Classification</em>'.
	 * @see org.jboss.tools.windup.windup.Classification
	 * @generated
	 */
	EClass getClassification();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.Classification#getClassification <em>Classification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Classification</em>'.
	 * @see org.jboss.tools.windup.windup.Classification#getClassification()
	 * @see #getClassification()
	 * @generated
	 */
	EAttribute getClassification_Classification();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.Classification#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see org.jboss.tools.windup.windup.Classification#getDescription()
	 * @see #getClassification()
	 * @generated
	 */
	EAttribute getClassification_Description();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.windup.windup.Link <em>Link</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Link</em>'.
	 * @see org.jboss.tools.windup.windup.Link
	 * @generated
	 */
	EClass getLink();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.Link#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see org.jboss.tools.windup.windup.Link#getDescription()
	 * @see #getLink()
	 * @generated
	 */
	EAttribute getLink_Description();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.Link#getUrl <em>Url</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Url</em>'.
	 * @see org.jboss.tools.windup.windup.Link#getUrl()
	 * @see #getLink()
	 * @generated
	 */
	EAttribute getLink_Url();

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
