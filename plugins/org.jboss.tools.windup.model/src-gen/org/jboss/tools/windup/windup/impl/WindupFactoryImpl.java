/**
 */
package org.jboss.tools.windup.windup.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.jboss.tools.windup.windup.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class WindupFactoryImpl extends EFactoryImpl implements WindupFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static WindupFactory init() {
		try {
			WindupFactory theWindupFactory = (WindupFactory)EPackage.Registry.INSTANCE.getEFactory(WindupPackage.eNS_URI);
			if (theWindupFactory != null) {
				return theWindupFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new WindupFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WindupFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case WindupPackage.NAMED_ELEMENT: return createNamedElement();
			case WindupPackage.CONFIGURATION_ELEMENT: return createConfigurationElement();
			case WindupPackage.PARAMETER: return createParameter();
			case WindupPackage.PARAMETERIZED: return createParameterized();
			case WindupPackage.WINDUP_MODEL: return createWindupModel();
			case WindupPackage.INPUT: return createInput();
			case WindupPackage.WINDUP_RESULT: return createWindupResult();
			case WindupPackage.ISSUE: return createIssue();
			case WindupPackage.HINT: return createHint();
			case WindupPackage.CLASSIFICATION: return createClassification();
			case WindupPackage.LINK: return createLink();
			case WindupPackage.QUICK_FIX: return createQuickFix();
			case WindupPackage.MIGRATION_PATH: return createMigrationPath();
			case WindupPackage.TECHNOLOGY: return createTechnology();
			case WindupPackage.PAIR: return createPair();
			case WindupPackage.CUSTOM_RULE_PROVIDER: return createCustomRuleProvider();
			case WindupPackage.MARKER_ELEMENT: return createMarkerElement();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NamedElement createNamedElement() {
		NamedElementImpl namedElement = new NamedElementImpl();
		return namedElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConfigurationElement createConfigurationElement() {
		ConfigurationElementImpl configurationElement = new ConfigurationElementImpl();
		return configurationElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Parameter createParameter() {
		ParameterImpl parameter = new ParameterImpl();
		return parameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Parameterized createParameterized() {
		ParameterizedImpl parameterized = new ParameterizedImpl();
		return parameterized;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WindupModel createWindupModel() {
		WindupModelImpl windupModel = new WindupModelImpl();
		return windupModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Input createInput() {
		InputImpl input = new InputImpl();
		return input;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WindupResult createWindupResult() {
		WindupResultImpl windupResult = new WindupResultImpl();
		return windupResult;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Issue createIssue() {
		IssueImpl issue = new IssueImpl();
		return issue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Hint createHint() {
		HintImpl hint = new HintImpl();
		return hint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Classification createClassification() {
		ClassificationImpl classification = new ClassificationImpl();
		return classification;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Link createLink() {
		LinkImpl link = new LinkImpl();
		return link;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public QuickFix createQuickFix() {
		QuickFixImpl quickFix = new QuickFixImpl();
		return quickFix;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MigrationPath createMigrationPath() {
		MigrationPathImpl migrationPath = new MigrationPathImpl();
		return migrationPath;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Technology createTechnology() {
		TechnologyImpl technology = new TechnologyImpl();
		return technology;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Pair createPair() {
		PairImpl pair = new PairImpl();
		return pair;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CustomRuleProvider createCustomRuleProvider() {
		CustomRuleProviderImpl customRuleProvider = new CustomRuleProviderImpl();
		return customRuleProvider;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MarkerElement createMarkerElement() {
		MarkerElementImpl markerElement = new MarkerElementImpl();
		return markerElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WindupPackage getWindupPackage() {
		return (WindupPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static WindupPackage getPackage() {
		return WindupPackage.eINSTANCE;
	}

} //WindupFactoryImpl
