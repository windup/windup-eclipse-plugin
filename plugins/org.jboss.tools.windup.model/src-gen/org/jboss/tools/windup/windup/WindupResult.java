/**
 */
package org.jboss.tools.windup.windup;

import org.eclipse.emf.ecore.EObject;

import org.jboss.windup.tooling.ExecutionResults;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Result</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.jboss.tools.windup.windup.WindupResult#getExecutionResults <em>Execution Results</em>}</li>
 * </ul>
 *
 * @see org.jboss.tools.windup.windup.WindupPackage#getWindupResult()
 * @model
 * @generated
 */
public interface WindupResult extends EObject {
	/**
	 * Returns the value of the '<em><b>Execution Results</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Execution Results</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Execution Results</em>' attribute.
	 * @see #setExecutionResults(ExecutionResults)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getWindupResult_ExecutionResults()
	 * @model dataType="org.jboss.tools.windup.windup.WindupExecutionResults" transient="true"
	 * @generated
	 */
	ExecutionResults getExecutionResults();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.WindupResult#getExecutionResults <em>Execution Results</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Execution Results</em>' attribute.
	 * @see #getExecutionResults()
	 * @generated
	 */
	void setExecutionResults(ExecutionResults value);

} // WindupResult
