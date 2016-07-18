/**
 */
package org.jboss.tools.windup.windup.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.jboss.tools.windup.windup.WindupPackage;
import org.jboss.tools.windup.windup.WindupResult;

import org.jboss.windup.tooling.ExecutionResults;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Result</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.jboss.tools.windup.windup.impl.WindupResultImpl#getExecutionResults <em>Execution Results</em>}</li>
 * </ul>
 *
 * @generated
 */
public class WindupResultImpl extends MinimalEObjectImpl.Container implements WindupResult {
	/**
	 * The default value of the '{@link #getExecutionResults() <em>Execution Results</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExecutionResults()
	 * @generated
	 * @ordered
	 */
	protected static final ExecutionResults EXECUTION_RESULTS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getExecutionResults() <em>Execution Results</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExecutionResults()
	 * @generated
	 * @ordered
	 */
	protected ExecutionResults executionResults = EXECUTION_RESULTS_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected WindupResultImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return WindupPackage.eINSTANCE.getWindupResult();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExecutionResults getExecutionResults() {
		return executionResults;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExecutionResults(ExecutionResults newExecutionResults) {
		ExecutionResults oldExecutionResults = executionResults;
		executionResults = newExecutionResults;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.WINDUP_RESULT__EXECUTION_RESULTS, oldExecutionResults, executionResults));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case WindupPackage.WINDUP_RESULT__EXECUTION_RESULTS:
				return getExecutionResults();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case WindupPackage.WINDUP_RESULT__EXECUTION_RESULTS:
				setExecutionResults((ExecutionResults)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case WindupPackage.WINDUP_RESULT__EXECUTION_RESULTS:
				setExecutionResults(EXECUTION_RESULTS_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case WindupPackage.WINDUP_RESULT__EXECUTION_RESULTS:
				return EXECUTION_RESULTS_EDEFAULT == null ? executionResults != null : !EXECUTION_RESULTS_EDEFAULT.equals(executionResults);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (executionResults: ");
		result.append(executionResults);
		result.append(')');
		return result.toString();
	}

} //WindupResultImpl
