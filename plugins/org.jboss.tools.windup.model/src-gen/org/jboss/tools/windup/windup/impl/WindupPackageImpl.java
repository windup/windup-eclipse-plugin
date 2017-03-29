/**
 */
package org.jboss.tools.windup.windup.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EValidator;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.jboss.tools.windup.windup.Classification;
import org.jboss.tools.windup.windup.ConfigurationElement;
import org.jboss.tools.windup.windup.CustomRuleProvider;
import org.jboss.tools.windup.windup.Hint;
import org.jboss.tools.windup.windup.Input;
import org.jboss.tools.windup.windup.Issue;
import org.jboss.tools.windup.windup.Link;
import org.jboss.tools.windup.windup.MigrationPath;
import org.jboss.tools.windup.windup.NamedElement;
import org.jboss.tools.windup.windup.Pair;
import org.jboss.tools.windup.windup.Parameter;
import org.jboss.tools.windup.windup.Parameterized;
import org.jboss.tools.windup.windup.QuickFix;
import org.jboss.tools.windup.windup.Technology;
import org.jboss.tools.windup.windup.WindupFactory;
import org.jboss.tools.windup.windup.WindupModel;
import org.jboss.tools.windup.windup.WindupPackage;
import org.jboss.tools.windup.windup.WindupResult;

import org.jboss.tools.windup.windup.util.WindupValidator;

import org.jboss.windup.tooling.ExecutionResults;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class WindupPackageImpl extends EPackageImpl implements WindupPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass namedElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass configurationElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass parameterEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass parameterizedEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass windupModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass inputEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass windupResultEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass issueEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass hintEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass classificationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass linkEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass quickFixEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass migrationPathEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass technologyEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass pairEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass customRuleProviderEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType windupExecutionResultsEDataType = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.jboss.tools.windup.windup.WindupPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private WindupPackageImpl() {
		super(eNS_URI, WindupFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link WindupPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static WindupPackage init() {
		if (isInited) return (WindupPackage)EPackage.Registry.INSTANCE.getEPackage(WindupPackage.eNS_URI);

		// Obtain or create and register package
		WindupPackageImpl theWindupPackage = (WindupPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof WindupPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new WindupPackageImpl());

		isInited = true;

		// Create package meta-data objects
		theWindupPackage.createPackageContents();

		// Initialize created meta-data
		theWindupPackage.initializePackageContents();

		// Register package validator
		EValidator.Registry.INSTANCE.put
			(theWindupPackage, 
			 new EValidator.Descriptor() {
				 public EValidator getEValidator() {
					 return WindupValidator.INSTANCE;
				 }
			 });

		// Mark meta-data to indicate it can't be changed
		theWindupPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(WindupPackage.eNS_URI, theWindupPackage);
		return theWindupPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNamedElement() {
		return namedElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getNamedElement_Name() {
		return (EAttribute)namedElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getNamedElement__Validate__DiagnosticChain_Map() {
		return namedElementEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getConfigurationElement() {
		return configurationElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getConfigurationElement_Inputs() {
		return (EReference)configurationElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getConfigurationElement_WindupHome() {
		return (EAttribute)configurationElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getConfigurationElement_SourceMode() {
		return (EAttribute)configurationElementEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getConfigurationElement_GeneratedReportsLocation() {
		return (EAttribute)configurationElementEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getConfigurationElement_Packages() {
		return (EAttribute)configurationElementEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getConfigurationElement_Timestamp() {
		return (EAttribute)configurationElementEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getConfigurationElement_GenerateReport() {
		return (EAttribute)configurationElementEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getConfigurationElement_MigrationPath() {
		return (EReference)configurationElementEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getConfigurationElement_UserRulesDirectories() {
		return (EAttribute)configurationElementEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getConfigurationElement_Options() {
		return (EReference)configurationElementEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getConfigurationElement_ReportDirectory() {
		return (EAttribute)configurationElementEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getParameter() {
		return parameterEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParameter_Value() {
		return (EAttribute)parameterEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getParameterized() {
		return parameterizedEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getParameterized_Parameters() {
		return (EReference)parameterizedEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getWindupModel() {
		return windupModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getWindupModel_ConfigurationElements() {
		return (EReference)windupModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getWindupModel_MigrationPaths() {
		return (EReference)windupModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getWindupModel_CustomRuleRepositories() {
		return (EReference)windupModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getInput() {
		return inputEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInput_Uri() {
		return (EAttribute)inputEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getInput_WindupResult() {
		return (EReference)inputEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getWindupResult() {
		return windupResultEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getWindupResult_ExecutionResults() {
		return (EAttribute)windupResultEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getWindupResult_Issues() {
		return (EReference)windupResultEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIssue() {
		return issueEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIssue_ElementId() {
		return (EAttribute)issueEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIssue_Links() {
		return (EReference)issueEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIssue_FileAbsolutePath() {
		return (EAttribute)issueEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIssue_Severity() {
		return (EAttribute)issueEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIssue_RuleId() {
		return (EAttribute)issueEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIssue_Effort() {
		return (EAttribute)issueEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIssue_Fixed() {
		return (EAttribute)issueEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIssue_GeneratedReportLocation() {
		return (EAttribute)issueEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIssue_QuickFixes() {
		return (EReference)issueEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIssue_OriginalLineSource() {
		return (EAttribute)issueEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIssue_Stale() {
		return (EAttribute)issueEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getHint() {
		return hintEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getHint_Title() {
		return (EAttribute)hintEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getHint_Hint() {
		return (EAttribute)hintEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getHint_LineNumber() {
		return (EAttribute)hintEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getHint_Column() {
		return (EAttribute)hintEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getHint_Length() {
		return (EAttribute)hintEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getHint_SourceSnippet() {
		return (EAttribute)hintEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getClassification() {
		return classificationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getClassification_Classification() {
		return (EAttribute)classificationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getClassification_Description() {
		return (EAttribute)classificationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLink() {
		return linkEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLink_Description() {
		return (EAttribute)linkEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLink_Url() {
		return (EAttribute)linkEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getQuickFix() {
		return quickFixEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getQuickFix_NewLine() {
		return (EAttribute)quickFixEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getQuickFix_ReplacementString() {
		return (EAttribute)quickFixEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getQuickFix_SearchString() {
		return (EAttribute)quickFixEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getQuickFix_QuickFixType() {
		return (EAttribute)quickFixEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getQuickFix_Id() {
		return (EAttribute)quickFixEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getQuickFix_TransformationId() {
		return (EAttribute)quickFixEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getQuickFix_File() {
		return (EAttribute)quickFixEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMigrationPath() {
		return migrationPathEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMigrationPath_Id() {
		return (EAttribute)migrationPathEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMigrationPath_Source() {
		return (EReference)migrationPathEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMigrationPath_Target() {
		return (EReference)migrationPathEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTechnology() {
		return technologyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTechnology_Id() {
		return (EAttribute)technologyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTechnology_VersionRange() {
		return (EAttribute)technologyEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPair() {
		return pairEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPair_Key() {
		return (EAttribute)pairEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPair_Value() {
		return (EAttribute)pairEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCustomRuleProvider() {
		return customRuleProviderEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCustomRuleProvider_LocationURI() {
		return (EAttribute)customRuleProviderEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCustomRuleProvider_External() {
		return (EAttribute)customRuleProviderEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getWindupExecutionResults() {
		return windupExecutionResultsEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WindupFactory getWindupFactory() {
		return (WindupFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		namedElementEClass = createEClass(NAMED_ELEMENT);
		createEAttribute(namedElementEClass, NAMED_ELEMENT__NAME);
		createEOperation(namedElementEClass, NAMED_ELEMENT___VALIDATE__DIAGNOSTICCHAIN_MAP);

		configurationElementEClass = createEClass(CONFIGURATION_ELEMENT);
		createEReference(configurationElementEClass, CONFIGURATION_ELEMENT__INPUTS);
		createEAttribute(configurationElementEClass, CONFIGURATION_ELEMENT__WINDUP_HOME);
		createEAttribute(configurationElementEClass, CONFIGURATION_ELEMENT__SOURCE_MODE);
		createEAttribute(configurationElementEClass, CONFIGURATION_ELEMENT__GENERATED_REPORTS_LOCATION);
		createEAttribute(configurationElementEClass, CONFIGURATION_ELEMENT__PACKAGES);
		createEAttribute(configurationElementEClass, CONFIGURATION_ELEMENT__TIMESTAMP);
		createEAttribute(configurationElementEClass, CONFIGURATION_ELEMENT__GENERATE_REPORT);
		createEReference(configurationElementEClass, CONFIGURATION_ELEMENT__MIGRATION_PATH);
		createEAttribute(configurationElementEClass, CONFIGURATION_ELEMENT__USER_RULES_DIRECTORIES);
		createEReference(configurationElementEClass, CONFIGURATION_ELEMENT__OPTIONS);
		createEAttribute(configurationElementEClass, CONFIGURATION_ELEMENT__REPORT_DIRECTORY);

		parameterEClass = createEClass(PARAMETER);
		createEAttribute(parameterEClass, PARAMETER__VALUE);

		parameterizedEClass = createEClass(PARAMETERIZED);
		createEReference(parameterizedEClass, PARAMETERIZED__PARAMETERS);

		windupModelEClass = createEClass(WINDUP_MODEL);
		createEReference(windupModelEClass, WINDUP_MODEL__CONFIGURATION_ELEMENTS);
		createEReference(windupModelEClass, WINDUP_MODEL__MIGRATION_PATHS);
		createEReference(windupModelEClass, WINDUP_MODEL__CUSTOM_RULE_REPOSITORIES);

		inputEClass = createEClass(INPUT);
		createEAttribute(inputEClass, INPUT__URI);
		createEReference(inputEClass, INPUT__WINDUP_RESULT);

		windupResultEClass = createEClass(WINDUP_RESULT);
		createEAttribute(windupResultEClass, WINDUP_RESULT__EXECUTION_RESULTS);
		createEReference(windupResultEClass, WINDUP_RESULT__ISSUES);

		issueEClass = createEClass(ISSUE);
		createEAttribute(issueEClass, ISSUE__ELEMENT_ID);
		createEReference(issueEClass, ISSUE__LINKS);
		createEAttribute(issueEClass, ISSUE__FILE_ABSOLUTE_PATH);
		createEAttribute(issueEClass, ISSUE__SEVERITY);
		createEAttribute(issueEClass, ISSUE__RULE_ID);
		createEAttribute(issueEClass, ISSUE__EFFORT);
		createEAttribute(issueEClass, ISSUE__FIXED);
		createEAttribute(issueEClass, ISSUE__GENERATED_REPORT_LOCATION);
		createEReference(issueEClass, ISSUE__QUICK_FIXES);
		createEAttribute(issueEClass, ISSUE__ORIGINAL_LINE_SOURCE);
		createEAttribute(issueEClass, ISSUE__STALE);

		hintEClass = createEClass(HINT);
		createEAttribute(hintEClass, HINT__TITLE);
		createEAttribute(hintEClass, HINT__HINT);
		createEAttribute(hintEClass, HINT__LINE_NUMBER);
		createEAttribute(hintEClass, HINT__COLUMN);
		createEAttribute(hintEClass, HINT__LENGTH);
		createEAttribute(hintEClass, HINT__SOURCE_SNIPPET);

		classificationEClass = createEClass(CLASSIFICATION);
		createEAttribute(classificationEClass, CLASSIFICATION__CLASSIFICATION);
		createEAttribute(classificationEClass, CLASSIFICATION__DESCRIPTION);

		linkEClass = createEClass(LINK);
		createEAttribute(linkEClass, LINK__DESCRIPTION);
		createEAttribute(linkEClass, LINK__URL);

		quickFixEClass = createEClass(QUICK_FIX);
		createEAttribute(quickFixEClass, QUICK_FIX__NEW_LINE);
		createEAttribute(quickFixEClass, QUICK_FIX__REPLACEMENT_STRING);
		createEAttribute(quickFixEClass, QUICK_FIX__SEARCH_STRING);
		createEAttribute(quickFixEClass, QUICK_FIX__QUICK_FIX_TYPE);
		createEAttribute(quickFixEClass, QUICK_FIX__ID);
		createEAttribute(quickFixEClass, QUICK_FIX__TRANSFORMATION_ID);
		createEAttribute(quickFixEClass, QUICK_FIX__FILE);

		migrationPathEClass = createEClass(MIGRATION_PATH);
		createEAttribute(migrationPathEClass, MIGRATION_PATH__ID);
		createEReference(migrationPathEClass, MIGRATION_PATH__SOURCE);
		createEReference(migrationPathEClass, MIGRATION_PATH__TARGET);

		technologyEClass = createEClass(TECHNOLOGY);
		createEAttribute(technologyEClass, TECHNOLOGY__ID);
		createEAttribute(technologyEClass, TECHNOLOGY__VERSION_RANGE);

		pairEClass = createEClass(PAIR);
		createEAttribute(pairEClass, PAIR__KEY);
		createEAttribute(pairEClass, PAIR__VALUE);

		customRuleProviderEClass = createEClass(CUSTOM_RULE_PROVIDER);
		createEAttribute(customRuleProviderEClass, CUSTOM_RULE_PROVIDER__LOCATION_URI);
		createEAttribute(customRuleProviderEClass, CUSTOM_RULE_PROVIDER__EXTERNAL);

		// Create data types
		windupExecutionResultsEDataType = createEDataType(WINDUP_EXECUTION_RESULTS);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		configurationElementEClass.getESuperTypes().add(this.getParameterized());
		parameterEClass.getESuperTypes().add(this.getNamedElement());
		parameterizedEClass.getESuperTypes().add(this.getNamedElement());
		inputEClass.getESuperTypes().add(this.getNamedElement());
		hintEClass.getESuperTypes().add(this.getIssue());
		classificationEClass.getESuperTypes().add(this.getIssue());
		quickFixEClass.getESuperTypes().add(this.getNamedElement());
		migrationPathEClass.getESuperTypes().add(this.getNamedElement());

		// Initialize classes, features, and operations; add parameters
		initEClass(namedElementEClass, NamedElement.class, "NamedElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getNamedElement_Name(), ecorePackage.getEString(), "name", null, 0, 1, NamedElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		EOperation op = initEOperation(getNamedElement__Validate__DiagnosticChain_Map(), ecorePackage.getEBoolean(), "validate", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEDiagnosticChain(), "diagnostic", 0, 1, !IS_UNIQUE, IS_ORDERED);
		EGenericType g1 = createEGenericType(ecorePackage.getEMap());
		EGenericType g2 = createEGenericType(ecorePackage.getEJavaObject());
		g1.getETypeArguments().add(g2);
		g2 = createEGenericType(ecorePackage.getEJavaObject());
		g1.getETypeArguments().add(g2);
		addEParameter(op, g1, "context", 0, 1, !IS_UNIQUE, IS_ORDERED);

		initEClass(configurationElementEClass, ConfigurationElement.class, "ConfigurationElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getConfigurationElement_Inputs(), this.getInput(), null, "inputs", null, 0, -1, ConfigurationElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getConfigurationElement_WindupHome(), ecorePackage.getEString(), "windupHome", null, 0, 1, ConfigurationElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getConfigurationElement_SourceMode(), ecorePackage.getEBoolean(), "sourceMode", null, 0, 1, ConfigurationElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getConfigurationElement_GeneratedReportsLocation(), ecorePackage.getEString(), "generatedReportsLocation", null, 0, 1, ConfigurationElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getConfigurationElement_Packages(), ecorePackage.getEString(), "packages", null, 0, -1, ConfigurationElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getConfigurationElement_Timestamp(), ecorePackage.getEString(), "timestamp", null, 0, 1, ConfigurationElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getConfigurationElement_GenerateReport(), ecorePackage.getEBoolean(), "generateReport", null, 0, 1, ConfigurationElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getConfigurationElement_MigrationPath(), this.getMigrationPath(), null, "migrationPath", null, 0, 1, ConfigurationElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getConfigurationElement_UserRulesDirectories(), ecorePackage.getEString(), "userRulesDirectories", null, 0, -1, ConfigurationElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getConfigurationElement_Options(), this.getPair(), null, "options", null, 0, -1, ConfigurationElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getConfigurationElement_ReportDirectory(), ecorePackage.getEString(), "reportDirectory", null, 0, 1, ConfigurationElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(parameterEClass, Parameter.class, "Parameter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getParameter_Value(), ecorePackage.getEJavaObject(), "value", null, 0, 1, Parameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(parameterizedEClass, Parameterized.class, "Parameterized", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getParameterized_Parameters(), this.getParameter(), null, "parameters", null, 0, -1, Parameterized.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(windupModelEClass, WindupModel.class, "WindupModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getWindupModel_ConfigurationElements(), this.getConfigurationElement(), null, "configurationElements", null, 0, -1, WindupModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getWindupModel_MigrationPaths(), this.getMigrationPath(), null, "migrationPaths", null, 0, -1, WindupModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getWindupModel_CustomRuleRepositories(), this.getCustomRuleProvider(), null, "customRuleRepositories", null, 0, -1, WindupModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(inputEClass, Input.class, "Input", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getInput_Uri(), ecorePackage.getEString(), "uri", null, 0, 1, Input.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getInput_WindupResult(), this.getWindupResult(), null, "windupResult", null, 0, 1, Input.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(windupResultEClass, WindupResult.class, "WindupResult", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getWindupResult_ExecutionResults(), this.getWindupExecutionResults(), "executionResults", null, 0, 1, WindupResult.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getWindupResult_Issues(), this.getIssue(), null, "issues", null, 0, -1, WindupResult.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(issueEClass, Issue.class, "Issue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIssue_ElementId(), ecorePackage.getEString(), "elementId", null, 0, 1, Issue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIssue_Links(), this.getLink(), null, "links", null, 0, -1, Issue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIssue_FileAbsolutePath(), ecorePackage.getEString(), "fileAbsolutePath", null, 0, 1, Issue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIssue_Severity(), ecorePackage.getEString(), "severity", null, 0, 1, Issue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIssue_RuleId(), ecorePackage.getEString(), "ruleId", null, 0, 1, Issue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIssue_Effort(), ecorePackage.getEInt(), "effort", null, 0, 1, Issue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIssue_Fixed(), ecorePackage.getEBoolean(), "fixed", null, 0, 1, Issue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIssue_GeneratedReportLocation(), ecorePackage.getEString(), "generatedReportLocation", null, 0, 1, Issue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getIssue_QuickFixes(), this.getQuickFix(), null, "quickFixes", null, 0, -1, Issue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIssue_OriginalLineSource(), ecorePackage.getEString(), "originalLineSource", null, 0, 1, Issue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getIssue_Stale(), ecorePackage.getEBoolean(), "stale", null, 0, 1, Issue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(hintEClass, Hint.class, "Hint", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getHint_Title(), ecorePackage.getEString(), "title", null, 0, 1, Hint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getHint_Hint(), ecorePackage.getEString(), "hint", null, 0, 1, Hint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getHint_LineNumber(), ecorePackage.getEInt(), "lineNumber", null, 0, 1, Hint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getHint_Column(), ecorePackage.getEInt(), "column", null, 0, 1, Hint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getHint_Length(), ecorePackage.getEInt(), "length", null, 0, 1, Hint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getHint_SourceSnippet(), ecorePackage.getEString(), "sourceSnippet", null, 0, 1, Hint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(classificationEClass, Classification.class, "Classification", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getClassification_Classification(), ecorePackage.getEString(), "classification", null, 0, 1, Classification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getClassification_Description(), ecorePackage.getEString(), "description", null, 0, 1, Classification.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(linkEClass, Link.class, "Link", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getLink_Description(), ecorePackage.getEString(), "description", null, 0, 1, Link.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLink_Url(), ecorePackage.getEString(), "url", null, 0, 1, Link.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(quickFixEClass, QuickFix.class, "QuickFix", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getQuickFix_NewLine(), ecorePackage.getEString(), "newLine", null, 0, 1, QuickFix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getQuickFix_ReplacementString(), ecorePackage.getEString(), "replacementString", null, 0, 1, QuickFix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getQuickFix_SearchString(), ecorePackage.getEString(), "searchString", null, 0, 1, QuickFix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getQuickFix_QuickFixType(), ecorePackage.getEString(), "quickFixType", null, 0, 1, QuickFix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getQuickFix_Id(), ecorePackage.getEString(), "id", null, 0, 1, QuickFix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getQuickFix_TransformationId(), ecorePackage.getEString(), "transformationId", null, 0, 1, QuickFix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getQuickFix_File(), ecorePackage.getEString(), "file", null, 0, 1, QuickFix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(migrationPathEClass, MigrationPath.class, "MigrationPath", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMigrationPath_Id(), ecorePackage.getEString(), "id", null, 0, 1, MigrationPath.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMigrationPath_Source(), this.getTechnology(), null, "source", null, 0, 1, MigrationPath.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMigrationPath_Target(), this.getTechnology(), null, "target", null, 0, 1, MigrationPath.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(technologyEClass, Technology.class, "Technology", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTechnology_Id(), ecorePackage.getEString(), "id", null, 0, 1, Technology.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTechnology_VersionRange(), ecorePackage.getEString(), "versionRange", null, 0, 1, Technology.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(pairEClass, Pair.class, "Pair", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPair_Key(), ecorePackage.getEString(), "key", null, 0, 1, Pair.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPair_Value(), ecorePackage.getEString(), "value", null, 0, 1, Pair.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(customRuleProviderEClass, CustomRuleProvider.class, "CustomRuleProvider", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCustomRuleProvider_LocationURI(), ecorePackage.getEString(), "locationURI", null, 0, 1, CustomRuleProvider.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCustomRuleProvider_External(), ecorePackage.getEBoolean(), "external", null, 0, 1, CustomRuleProvider.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize data types
		initEDataType(windupExecutionResultsEDataType, ExecutionResults.class, "WindupExecutionResults", !IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} //WindupPackageImpl
