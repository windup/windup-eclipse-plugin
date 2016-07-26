/**
 */
package org.jboss.tools.windup.windup.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import org.jboss.tools.windup.windup.Issue;
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
 *   <li>{@link org.jboss.tools.windup.windup.impl.WindupResultImpl#getIssues <em>Issues</em>}</li>
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
	 * The cached value of the '{@link #getIssues() <em>Issues</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIssues()
	 * @generated
	 * @ordered
	 */
	protected EList<Issue> issues;

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
	public EList<Issue> getIssues() {
		if (issues == null) {
			issues = new EObjectResolvingEList<Issue>(Issue.class, this, WindupPackage.WINDUP_RESULT__ISSUES);
		}
		return issues;
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
			case WindupPackage.WINDUP_RESULT__ISSUES:
				return getIssues();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case WindupPackage.WINDUP_RESULT__EXECUTION_RESULTS:
				setExecutionResults((ExecutionResults)newValue);
				return;
			case WindupPackage.WINDUP_RESULT__ISSUES:
				getIssues().clear();
				getIssues().addAll((Collection<? extends Issue>)newValue);
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
			case WindupPackage.WINDUP_RESULT__ISSUES:
				getIssues().clear();
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
			case WindupPackage.WINDUP_RESULT__ISSUES:
				return issues != null && !issues.isEmpty();
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
