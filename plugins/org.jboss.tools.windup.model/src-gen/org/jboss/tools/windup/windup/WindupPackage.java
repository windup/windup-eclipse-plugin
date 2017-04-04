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
	 * The feature id for the '<em><b>Timestamp</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_ELEMENT__TIMESTAMP = PARAMETERIZED_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Generate Report</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_ELEMENT__GENERATE_REPORT = PARAMETERIZED_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Migration Path</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_ELEMENT__MIGRATION_PATH = PARAMETERIZED_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>User Rules Directories</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_ELEMENT__USER_RULES_DIRECTORIES = PARAMETERIZED_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Options</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_ELEMENT__OPTIONS = PARAMETERIZED_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Report Directory</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_ELEMENT__REPORT_DIRECTORY = PARAMETERIZED_FEATURE_COUNT + 10;

	/**
	 * The number of structural features of the '<em>Configuration Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_ELEMENT_FEATURE_COUNT = PARAMETERIZED_FEATURE_COUNT + 11;

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
	 * The feature id for the '<em><b>Migration Paths</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WINDUP_MODEL__MIGRATION_PATHS = 1;

	/**
	 * The feature id for the '<em><b>Custom Rule Repositories</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WINDUP_MODEL__CUSTOM_RULE_REPOSITORIES = 2;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WINDUP_MODEL_FEATURE_COUNT = 3;

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
	 * The number of structural features of the '<em>Input</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 2;

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
	 * The meta object id for the '{@link org.jboss.tools.windup.windup.impl.HintImpl <em>Hint</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.windup.windup.impl.HintImpl
	 * @see org.jboss.tools.windup.windup.impl.WindupPackageImpl#getHint()
	 * @generated
	 */
	int HINT = 8;

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
	 * The meta object id for the '{@link org.jboss.tools.windup.windup.impl.LinkImpl <em>Link</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.windup.windup.impl.LinkImpl
	 * @see org.jboss.tools.windup.windup.impl.WindupPackageImpl#getLink()
	 * @generated
	 */
	int LINK = 10;

	/**
	 * The meta object id for the '{@link org.jboss.tools.windup.windup.impl.QuickFixImpl <em>Quick Fix</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.windup.windup.impl.QuickFixImpl
	 * @see org.jboss.tools.windup.windup.impl.WindupPackageImpl#getQuickFix()
	 * @generated
	 */
	int QUICK_FIX = 11;

	/**
	 * The meta object id for the '{@link org.jboss.tools.windup.windup.impl.MigrationPathImpl <em>Migration Path</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.windup.windup.impl.MigrationPathImpl
	 * @see org.jboss.tools.windup.windup.impl.WindupPackageImpl#getMigrationPath()
	 * @generated
	 */
	int MIGRATION_PATH = 12;

	/**
	 * The meta object id for the '{@link org.jboss.tools.windup.windup.impl.TechnologyImpl <em>Technology</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.windup.windup.impl.TechnologyImpl
	 * @see org.jboss.tools.windup.windup.impl.WindupPackageImpl#getTechnology()
	 * @generated
	 */
	int TECHNOLOGY = 13;

	/**
	 * The meta object id for the '{@link org.jboss.tools.windup.windup.impl.PairImpl <em>Pair</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.windup.windup.impl.PairImpl
	 * @see org.jboss.tools.windup.windup.impl.WindupPackageImpl#getPair()
	 * @generated
	 */
	int PAIR = 14;

	/**
	 * The meta object id for the '{@link org.jboss.tools.windup.windup.impl.CustomRuleProviderImpl <em>Custom Rule Provider</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.windup.windup.impl.CustomRuleProviderImpl
	 * @see org.jboss.tools.windup.windup.impl.WindupPackageImpl#getCustomRuleProvider()
	 * @generated
	 */
	int CUSTOM_RULE_PROVIDER = 15;

	/**
	 * The meta object id for the '{@link org.jboss.tools.windup.windup.impl.MarkerElementImpl <em>Marker Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.windup.windup.impl.MarkerElementImpl
	 * @see org.jboss.tools.windup.windup.impl.WindupPackageImpl#getMarkerElement()
	 * @generated
	 */
	int MARKER_ELEMENT = 16;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKER_ELEMENT__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Marker</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKER_ELEMENT__MARKER = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Marker Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKER_ELEMENT_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Validate</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKER_ELEMENT___VALIDATE__DIAGNOSTICCHAIN_MAP = NAMED_ELEMENT___VALIDATE__DIAGNOSTICCHAIN_MAP;

	/**
	 * The number of operations of the '<em>Marker Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MARKER_ELEMENT_OPERATION_COUNT = NAMED_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISSUE__NAME = MARKER_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Marker</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISSUE__MARKER = MARKER_ELEMENT__MARKER;

	/**
	 * The feature id for the '<em><b>Element Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISSUE__ELEMENT_ID = MARKER_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Links</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISSUE__LINKS = MARKER_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>File Absolute Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISSUE__FILE_ABSOLUTE_PATH = MARKER_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Severity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISSUE__SEVERITY = MARKER_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Rule Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISSUE__RULE_ID = MARKER_ELEMENT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Effort</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISSUE__EFFORT = MARKER_ELEMENT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Fixed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISSUE__FIXED = MARKER_ELEMENT_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Generated Report Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISSUE__GENERATED_REPORT_LOCATION = MARKER_ELEMENT_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Quick Fixes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISSUE__QUICK_FIXES = MARKER_ELEMENT_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Original Line Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISSUE__ORIGINAL_LINE_SOURCE = MARKER_ELEMENT_FEATURE_COUNT + 9;

	/**
	 * The feature id for the '<em><b>Stale</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISSUE__STALE = MARKER_ELEMENT_FEATURE_COUNT + 10;

	/**
	 * The number of structural features of the '<em>Issue</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISSUE_FEATURE_COUNT = MARKER_ELEMENT_FEATURE_COUNT + 11;

	/**
	 * The operation id for the '<em>Validate</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISSUE___VALIDATE__DIAGNOSTICCHAIN_MAP = MARKER_ELEMENT___VALIDATE__DIAGNOSTICCHAIN_MAP;

	/**
	 * The number of operations of the '<em>Issue</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISSUE_OPERATION_COUNT = MARKER_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HINT__NAME = ISSUE__NAME;

	/**
	 * The feature id for the '<em><b>Marker</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HINT__MARKER = ISSUE__MARKER;

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
	 * The feature id for the '<em><b>Generated Report Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HINT__GENERATED_REPORT_LOCATION = ISSUE__GENERATED_REPORT_LOCATION;

	/**
	 * The feature id for the '<em><b>Quick Fixes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HINT__QUICK_FIXES = ISSUE__QUICK_FIXES;

	/**
	 * The feature id for the '<em><b>Original Line Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HINT__ORIGINAL_LINE_SOURCE = ISSUE__ORIGINAL_LINE_SOURCE;

	/**
	 * The feature id for the '<em><b>Stale</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HINT__STALE = ISSUE__STALE;

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
	 * The operation id for the '<em>Validate</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HINT___VALIDATE__DIAGNOSTICCHAIN_MAP = ISSUE___VALIDATE__DIAGNOSTICCHAIN_MAP;

	/**
	 * The number of operations of the '<em>Hint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HINT_OPERATION_COUNT = ISSUE_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASSIFICATION__NAME = ISSUE__NAME;

	/**
	 * The feature id for the '<em><b>Marker</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASSIFICATION__MARKER = ISSUE__MARKER;

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
	 * The feature id for the '<em><b>Generated Report Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASSIFICATION__GENERATED_REPORT_LOCATION = ISSUE__GENERATED_REPORT_LOCATION;

	/**
	 * The feature id for the '<em><b>Quick Fixes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASSIFICATION__QUICK_FIXES = ISSUE__QUICK_FIXES;

	/**
	 * The feature id for the '<em><b>Original Line Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASSIFICATION__ORIGINAL_LINE_SOURCE = ISSUE__ORIGINAL_LINE_SOURCE;

	/**
	 * The feature id for the '<em><b>Stale</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASSIFICATION__STALE = ISSUE__STALE;

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
	 * The operation id for the '<em>Validate</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASSIFICATION___VALIDATE__DIAGNOSTICCHAIN_MAP = ISSUE___VALIDATE__DIAGNOSTICCHAIN_MAP;

	/**
	 * The number of operations of the '<em>Classification</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASSIFICATION_OPERATION_COUNT = ISSUE_OPERATION_COUNT + 0;

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
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUICK_FIX__NAME = MARKER_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Marker</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUICK_FIX__MARKER = MARKER_ELEMENT__MARKER;

	/**
	 * The feature id for the '<em><b>New Line</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUICK_FIX__NEW_LINE = MARKER_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Replacement String</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUICK_FIX__REPLACEMENT_STRING = MARKER_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Search String</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUICK_FIX__SEARCH_STRING = MARKER_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Quick Fix Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUICK_FIX__QUICK_FIX_TYPE = MARKER_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUICK_FIX__ID = MARKER_ELEMENT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Transformation Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUICK_FIX__TRANSFORMATION_ID = MARKER_ELEMENT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>File</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUICK_FIX__FILE = MARKER_ELEMENT_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>Quick Fix</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUICK_FIX_FEATURE_COUNT = MARKER_ELEMENT_FEATURE_COUNT + 7;

	/**
	 * The operation id for the '<em>Validate</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUICK_FIX___VALIDATE__DIAGNOSTICCHAIN_MAP = MARKER_ELEMENT___VALIDATE__DIAGNOSTICCHAIN_MAP;

	/**
	 * The number of operations of the '<em>Quick Fix</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUICK_FIX_OPERATION_COUNT = MARKER_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MIGRATION_PATH__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MIGRATION_PATH__ID = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Source</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MIGRATION_PATH__SOURCE = NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Target</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MIGRATION_PATH__TARGET = NAMED_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Migration Path</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MIGRATION_PATH_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The operation id for the '<em>Validate</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MIGRATION_PATH___VALIDATE__DIAGNOSTICCHAIN_MAP = NAMED_ELEMENT___VALIDATE__DIAGNOSTICCHAIN_MAP;

	/**
	 * The number of operations of the '<em>Migration Path</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MIGRATION_PATH_OPERATION_COUNT = NAMED_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TECHNOLOGY__ID = 0;

	/**
	 * The feature id for the '<em><b>Version Range</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TECHNOLOGY__VERSION_RANGE = 1;

	/**
	 * The number of structural features of the '<em>Technology</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TECHNOLOGY_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Technology</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TECHNOLOGY_OPERATION_COUNT = 0;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAIR__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAIR__VALUE = 1;

	/**
	 * The number of structural features of the '<em>Pair</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAIR_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Pair</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAIR_OPERATION_COUNT = 0;

	/**
	 * The feature id for the '<em><b>Location URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CUSTOM_RULE_PROVIDER__LOCATION_URI = 0;

	/**
	 * The feature id for the '<em><b>External</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CUSTOM_RULE_PROVIDER__EXTERNAL = 1;

	/**
	 * The number of structural features of the '<em>Custom Rule Provider</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CUSTOM_RULE_PROVIDER_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Custom Rule Provider</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CUSTOM_RULE_PROVIDER_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '<em>Execution Results</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.windup.tooling.ExecutionResults
	 * @see org.jboss.tools.windup.windup.impl.WindupPackageImpl#getWindupExecutionResults()
	 * @generated
	 */
	int WINDUP_EXECUTION_RESULTS = 17;


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
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.ConfigurationElement#getTimestamp <em>Timestamp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Timestamp</em>'.
	 * @see org.jboss.tools.windup.windup.ConfigurationElement#getTimestamp()
	 * @see #getConfigurationElement()
	 * @generated
	 */
	EAttribute getConfigurationElement_Timestamp();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.ConfigurationElement#isGenerateReport <em>Generate Report</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Generate Report</em>'.
	 * @see org.jboss.tools.windup.windup.ConfigurationElement#isGenerateReport()
	 * @see #getConfigurationElement()
	 * @generated
	 */
	EAttribute getConfigurationElement_GenerateReport();

	/**
	 * Returns the meta object for the reference '{@link org.jboss.tools.windup.windup.ConfigurationElement#getMigrationPath <em>Migration Path</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Migration Path</em>'.
	 * @see org.jboss.tools.windup.windup.ConfigurationElement#getMigrationPath()
	 * @see #getConfigurationElement()
	 * @generated
	 */
	EReference getConfigurationElement_MigrationPath();

	/**
	 * Returns the meta object for the attribute list '{@link org.jboss.tools.windup.windup.ConfigurationElement#getUserRulesDirectories <em>User Rules Directories</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>User Rules Directories</em>'.
	 * @see org.jboss.tools.windup.windup.ConfigurationElement#getUserRulesDirectories()
	 * @see #getConfigurationElement()
	 * @generated
	 */
	EAttribute getConfigurationElement_UserRulesDirectories();

	/**
	 * Returns the meta object for the containment reference list '{@link org.jboss.tools.windup.windup.ConfigurationElement#getOptions <em>Options</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Options</em>'.
	 * @see org.jboss.tools.windup.windup.ConfigurationElement#getOptions()
	 * @see #getConfigurationElement()
	 * @generated
	 */
	EReference getConfigurationElement_Options();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.ConfigurationElement#getReportDirectory <em>Report Directory</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Report Directory</em>'.
	 * @see org.jboss.tools.windup.windup.ConfigurationElement#getReportDirectory()
	 * @see #getConfigurationElement()
	 * @generated
	 */
	EAttribute getConfigurationElement_ReportDirectory();

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
	 * Returns the meta object for the containment reference list '{@link org.jboss.tools.windup.windup.WindupModel#getMigrationPaths <em>Migration Paths</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Migration Paths</em>'.
	 * @see org.jboss.tools.windup.windup.WindupModel#getMigrationPaths()
	 * @see #getWindupModel()
	 * @generated
	 */
	EReference getWindupModel_MigrationPaths();

	/**
	 * Returns the meta object for the containment reference list '{@link org.jboss.tools.windup.windup.WindupModel#getCustomRuleRepositories <em>Custom Rule Repositories</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Custom Rule Repositories</em>'.
	 * @see org.jboss.tools.windup.windup.WindupModel#getCustomRuleRepositories()
	 * @see #getWindupModel()
	 * @generated
	 */
	EReference getWindupModel_CustomRuleRepositories();

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
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.Issue#getGeneratedReportLocation <em>Generated Report Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Generated Report Location</em>'.
	 * @see org.jboss.tools.windup.windup.Issue#getGeneratedReportLocation()
	 * @see #getIssue()
	 * @generated
	 */
	EAttribute getIssue_GeneratedReportLocation();

	/**
	 * Returns the meta object for the containment reference list '{@link org.jboss.tools.windup.windup.Issue#getQuickFixes <em>Quick Fixes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Quick Fixes</em>'.
	 * @see org.jboss.tools.windup.windup.Issue#getQuickFixes()
	 * @see #getIssue()
	 * @generated
	 */
	EReference getIssue_QuickFixes();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.Issue#getOriginalLineSource <em>Original Line Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Original Line Source</em>'.
	 * @see org.jboss.tools.windup.windup.Issue#getOriginalLineSource()
	 * @see #getIssue()
	 * @generated
	 */
	EAttribute getIssue_OriginalLineSource();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.Issue#isStale <em>Stale</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Stale</em>'.
	 * @see org.jboss.tools.windup.windup.Issue#isStale()
	 * @see #getIssue()
	 * @generated
	 */
	EAttribute getIssue_Stale();

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
	 * Returns the meta object for class '{@link org.jboss.tools.windup.windup.QuickFix <em>Quick Fix</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Quick Fix</em>'.
	 * @see org.jboss.tools.windup.windup.QuickFix
	 * @generated
	 */
	EClass getQuickFix();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.QuickFix#getNewLine <em>New Line</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>New Line</em>'.
	 * @see org.jboss.tools.windup.windup.QuickFix#getNewLine()
	 * @see #getQuickFix()
	 * @generated
	 */
	EAttribute getQuickFix_NewLine();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.QuickFix#getReplacementString <em>Replacement String</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Replacement String</em>'.
	 * @see org.jboss.tools.windup.windup.QuickFix#getReplacementString()
	 * @see #getQuickFix()
	 * @generated
	 */
	EAttribute getQuickFix_ReplacementString();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.QuickFix#getSearchString <em>Search String</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Search String</em>'.
	 * @see org.jboss.tools.windup.windup.QuickFix#getSearchString()
	 * @see #getQuickFix()
	 * @generated
	 */
	EAttribute getQuickFix_SearchString();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.QuickFix#getQuickFixType <em>Quick Fix Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Quick Fix Type</em>'.
	 * @see org.jboss.tools.windup.windup.QuickFix#getQuickFixType()
	 * @see #getQuickFix()
	 * @generated
	 */
	EAttribute getQuickFix_QuickFixType();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.QuickFix#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.jboss.tools.windup.windup.QuickFix#getId()
	 * @see #getQuickFix()
	 * @generated
	 */
	EAttribute getQuickFix_Id();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.QuickFix#getTransformationId <em>Transformation Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Transformation Id</em>'.
	 * @see org.jboss.tools.windup.windup.QuickFix#getTransformationId()
	 * @see #getQuickFix()
	 * @generated
	 */
	EAttribute getQuickFix_TransformationId();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.QuickFix#getFile <em>File</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>File</em>'.
	 * @see org.jboss.tools.windup.windup.QuickFix#getFile()
	 * @see #getQuickFix()
	 * @generated
	 */
	EAttribute getQuickFix_File();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.windup.windup.MigrationPath <em>Migration Path</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Migration Path</em>'.
	 * @see org.jboss.tools.windup.windup.MigrationPath
	 * @generated
	 */
	EClass getMigrationPath();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.MigrationPath#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.jboss.tools.windup.windup.MigrationPath#getId()
	 * @see #getMigrationPath()
	 * @generated
	 */
	EAttribute getMigrationPath_Id();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.windup.windup.MigrationPath#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Source</em>'.
	 * @see org.jboss.tools.windup.windup.MigrationPath#getSource()
	 * @see #getMigrationPath()
	 * @generated
	 */
	EReference getMigrationPath_Source();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.windup.windup.MigrationPath#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Target</em>'.
	 * @see org.jboss.tools.windup.windup.MigrationPath#getTarget()
	 * @see #getMigrationPath()
	 * @generated
	 */
	EReference getMigrationPath_Target();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.windup.windup.Technology <em>Technology</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Technology</em>'.
	 * @see org.jboss.tools.windup.windup.Technology
	 * @generated
	 */
	EClass getTechnology();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.Technology#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.jboss.tools.windup.windup.Technology#getId()
	 * @see #getTechnology()
	 * @generated
	 */
	EAttribute getTechnology_Id();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.Technology#getVersionRange <em>Version Range</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version Range</em>'.
	 * @see org.jboss.tools.windup.windup.Technology#getVersionRange()
	 * @see #getTechnology()
	 * @generated
	 */
	EAttribute getTechnology_VersionRange();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.windup.windup.Pair <em>Pair</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Pair</em>'.
	 * @see org.jboss.tools.windup.windup.Pair
	 * @generated
	 */
	EClass getPair();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.Pair#getKey <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see org.jboss.tools.windup.windup.Pair#getKey()
	 * @see #getPair()
	 * @generated
	 */
	EAttribute getPair_Key();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.Pair#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.jboss.tools.windup.windup.Pair#getValue()
	 * @see #getPair()
	 * @generated
	 */
	EAttribute getPair_Value();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.windup.windup.CustomRuleProvider <em>Custom Rule Provider</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Custom Rule Provider</em>'.
	 * @see org.jboss.tools.windup.windup.CustomRuleProvider
	 * @generated
	 */
	EClass getCustomRuleProvider();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.CustomRuleProvider#getLocationURI <em>Location URI</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Location URI</em>'.
	 * @see org.jboss.tools.windup.windup.CustomRuleProvider#getLocationURI()
	 * @see #getCustomRuleProvider()
	 * @generated
	 */
	EAttribute getCustomRuleProvider_LocationURI();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.CustomRuleProvider#isExternal <em>External</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>External</em>'.
	 * @see org.jboss.tools.windup.windup.CustomRuleProvider#isExternal()
	 * @see #getCustomRuleProvider()
	 * @generated
	 */
	EAttribute getCustomRuleProvider_External();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.windup.windup.MarkerElement <em>Marker Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Marker Element</em>'.
	 * @see org.jboss.tools.windup.windup.MarkerElement
	 * @generated
	 */
	EClass getMarkerElement();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.windup.windup.MarkerElement#getMarker <em>Marker</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Marker</em>'.
	 * @see org.jboss.tools.windup.windup.MarkerElement#getMarker()
	 * @see #getMarkerElement()
	 * @generated
	 */
	EAttribute getMarkerElement_Marker();

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
