/**
 */
package org.jboss.tools.windup.windup.util;

import java.util.Map;

import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.EObjectValidator;

import org.jboss.tools.windup.windup.*;

import org.jboss.windup.tooling.ExecutionResults;

/**
 * <!-- begin-user-doc -->
 * The <b>Validator</b> for the model.
 * <!-- end-user-doc -->
 * @see org.jboss.tools.windup.windup.WindupPackage
 * @generated
 */
public class WindupValidator extends EObjectValidator {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final WindupValidator INSTANCE = new WindupValidator();

	/**
	 * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource() source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode() codes} from this package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.common.util.Diagnostic#getSource()
	 * @see org.eclipse.emf.common.util.Diagnostic#getCode()
	 * @generated
	 */
	public static final String DIAGNOSTIC_SOURCE = "org.jboss.tools.windup.windup";

	/**
	 * The {@link org.eclipse.emf.common.util.Diagnostic#getCode() code} for constraint 'Validate' of 'Named Element'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final int NAMED_ELEMENT__VALIDATE = 1;

	/**
	 * A constant with a fixed name that can be used as the base value for additional hand written constants.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final int GENERATED_DIAGNOSTIC_CODE_COUNT = 1;

	/**
	 * A constant with a fixed name that can be used as the base value for additional hand written constants in a derived class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static final int DIAGNOSTIC_CODE_COUNT = GENERATED_DIAGNOSTIC_CODE_COUNT;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WindupValidator() {
		super();
	}

	/**
	 * Returns the package of this validator switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EPackage getEPackage() {
	  return WindupPackage.eINSTANCE;
	}

	/**
	 * Calls <code>validateXXX</code> for the corresponding classifier of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected boolean validate(int classifierID, Object value, DiagnosticChain diagnostics, Map<Object, Object> context) {
		switch (classifierID) {
			case WindupPackage.NAMED_ELEMENT:
				return validateNamedElement((NamedElement)value, diagnostics, context);
			case WindupPackage.CONFIGURATION_ELEMENT:
				return validateConfigurationElement((ConfigurationElement)value, diagnostics, context);
			case WindupPackage.PARAMETER:
				return validateParameter((Parameter)value, diagnostics, context);
			case WindupPackage.PARAMETERIZED:
				return validateParameterized((Parameterized)value, diagnostics, context);
			case WindupPackage.WINDUP_MODEL:
				return validateWindupModel((WindupModel)value, diagnostics, context);
			case WindupPackage.INPUT:
				return validateInput((Input)value, diagnostics, context);
			case WindupPackage.WINDUP_RESULT:
				return validateWindupResult((WindupResult)value, diagnostics, context);
			case WindupPackage.ISSUE:
				return validateIssue((Issue)value, diagnostics, context);
			case WindupPackage.HINT:
				return validateHint((Hint)value, diagnostics, context);
			case WindupPackage.CLASSIFICATION:
				return validateClassification((Classification)value, diagnostics, context);
			case WindupPackage.LINK:
				return validateLink((Link)value, diagnostics, context);
			case WindupPackage.QUICK_FIX:
				return validateQuickFix((QuickFix)value, diagnostics, context);
			case WindupPackage.MIGRATION_PATH:
				return validateMigrationPath((MigrationPath)value, diagnostics, context);
			case WindupPackage.TECHNOLOGY:
				return validateTechnology((Technology)value, diagnostics, context);
			case WindupPackage.WINDUP_EXECUTION_RESULTS:
				return validateWindupExecutionResults((ExecutionResults)value, diagnostics, context);
			default:
				return true;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateNamedElement(NamedElement namedElement, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(namedElement, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(namedElement, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(namedElement, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(namedElement, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(namedElement, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(namedElement, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(namedElement, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(namedElement, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(namedElement, diagnostics, context);
		if (result || diagnostics != null) result &= validateNamedElement_validate(namedElement, diagnostics, context);
		return result;
	}

	/**
	 * Validates the validate constraint of '<em>Named Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateNamedElement_validate(NamedElement namedElement, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return namedElement.validate(diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateConfigurationElement(ConfigurationElement configurationElement, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(configurationElement, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(configurationElement, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(configurationElement, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(configurationElement, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(configurationElement, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(configurationElement, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(configurationElement, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(configurationElement, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(configurationElement, diagnostics, context);
		if (result || diagnostics != null) result &= validateNamedElement_validate(configurationElement, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateParameter(Parameter parameter, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(parameter, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(parameter, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(parameter, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(parameter, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(parameter, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(parameter, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(parameter, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(parameter, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(parameter, diagnostics, context);
		if (result || diagnostics != null) result &= validateNamedElement_validate(parameter, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateParameterized(Parameterized parameterized, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(parameterized, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(parameterized, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(parameterized, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(parameterized, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(parameterized, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(parameterized, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(parameterized, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(parameterized, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(parameterized, diagnostics, context);
		if (result || diagnostics != null) result &= validateNamedElement_validate(parameterized, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateWindupModel(WindupModel windupModel, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(windupModel, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateInput(Input input, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(input, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(input, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(input, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(input, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(input, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(input, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(input, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(input, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(input, diagnostics, context);
		if (result || diagnostics != null) result &= validateNamedElement_validate(input, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateWindupResult(WindupResult windupResult, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(windupResult, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateIssue(Issue issue, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(issue, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateHint(Hint hint, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(hint, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateClassification(Classification classification, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(classification, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateLink(Link link, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(link, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateQuickFix(QuickFix quickFix, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(quickFix, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(quickFix, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(quickFix, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(quickFix, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(quickFix, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(quickFix, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(quickFix, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(quickFix, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(quickFix, diagnostics, context);
		if (result || diagnostics != null) result &= validateNamedElement_validate(quickFix, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateMigrationPath(MigrationPath migrationPath, DiagnosticChain diagnostics, Map<Object, Object> context) {
		if (!validate_NoCircularContainment(migrationPath, diagnostics, context)) return false;
		boolean result = validate_EveryMultiplicityConforms(migrationPath, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryDataValueConforms(migrationPath, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryReferenceIsContained(migrationPath, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryBidirectionalReferenceIsPaired(migrationPath, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryProxyResolves(migrationPath, diagnostics, context);
		if (result || diagnostics != null) result &= validate_UniqueID(migrationPath, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryKeyUnique(migrationPath, diagnostics, context);
		if (result || diagnostics != null) result &= validate_EveryMapEntryUnique(migrationPath, diagnostics, context);
		if (result || diagnostics != null) result &= validateNamedElement_validate(migrationPath, diagnostics, context);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateTechnology(Technology technology, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate_EveryDefaultConstraint(technology, diagnostics, context);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean validateWindupExecutionResults(ExecutionResults windupExecutionResults, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	/**
	 * Returns the resource locator that will be used to fetch messages for this validator's diagnostics.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		// TODO
		// Specialize this to return a resource locator for messages specific to this validator.
		// Ensure that you remove @generated or mark it @generated NOT
		return super.getResourceLocator();
	}

} //WindupValidator
