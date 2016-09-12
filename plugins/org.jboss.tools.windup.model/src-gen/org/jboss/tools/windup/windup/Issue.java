/**
 */
package org.jboss.tools.windup.windup;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Issue</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.jboss.tools.windup.windup.Issue#getElementId <em>Element Id</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.Issue#getLinks <em>Links</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.Issue#getFileAbsolutePath <em>File Absolute Path</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.Issue#getSeverity <em>Severity</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.Issue#getRuleId <em>Rule Id</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.Issue#getEffort <em>Effort</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.Issue#isFixed <em>Fixed</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.Issue#getGeneratedReportLocation <em>Generated Report Location</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.Issue#getQuickFixes <em>Quick Fixes</em>}</li>
 * </ul>
 *
 * @see org.jboss.tools.windup.windup.WindupPackage#getIssue()
 * @model
 * @generated
 */
public interface Issue extends EObject {
	/**
	 * Returns the value of the '<em><b>Element Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Element Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Element Id</em>' attribute.
	 * @see #setElementId(String)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getIssue_ElementId()
	 * @model
	 * @generated
	 */
	String getElementId();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.Issue#getElementId <em>Element Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Element Id</em>' attribute.
	 * @see #getElementId()
	 * @generated
	 */
	void setElementId(String value);

	/**
	 * Returns the value of the '<em><b>Links</b></em>' containment reference list.
	 * The list contents are of type {@link org.jboss.tools.windup.windup.Link}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Links</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Links</em>' containment reference list.
	 * @see org.jboss.tools.windup.windup.WindupPackage#getIssue_Links()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<Link> getLinks();

	/**
	 * Returns the value of the '<em><b>File Absolute Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>File Absolute Path</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>File Absolute Path</em>' attribute.
	 * @see #setFileAbsolutePath(String)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getIssue_FileAbsolutePath()
	 * @model
	 * @generated
	 */
	String getFileAbsolutePath();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.Issue#getFileAbsolutePath <em>File Absolute Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>File Absolute Path</em>' attribute.
	 * @see #getFileAbsolutePath()
	 * @generated
	 */
	void setFileAbsolutePath(String value);

	/**
	 * Returns the value of the '<em><b>Severity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Severity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Severity</em>' attribute.
	 * @see #setSeverity(String)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getIssue_Severity()
	 * @model
	 * @generated
	 */
	String getSeverity();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.Issue#getSeverity <em>Severity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Severity</em>' attribute.
	 * @see #getSeverity()
	 * @generated
	 */
	void setSeverity(String value);

	/**
	 * Returns the value of the '<em><b>Rule Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rule Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rule Id</em>' attribute.
	 * @see #setRuleId(String)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getIssue_RuleId()
	 * @model
	 * @generated
	 */
	String getRuleId();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.Issue#getRuleId <em>Rule Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Rule Id</em>' attribute.
	 * @see #getRuleId()
	 * @generated
	 */
	void setRuleId(String value);

	/**
	 * Returns the value of the '<em><b>Effort</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Effort</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Effort</em>' attribute.
	 * @see #setEffort(int)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getIssue_Effort()
	 * @model
	 * @generated
	 */
	int getEffort();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.Issue#getEffort <em>Effort</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Effort</em>' attribute.
	 * @see #getEffort()
	 * @generated
	 */
	void setEffort(int value);

	/**
	 * Returns the value of the '<em><b>Fixed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fixed</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fixed</em>' attribute.
	 * @see #setFixed(boolean)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getIssue_Fixed()
	 * @model
	 * @generated
	 */
	boolean isFixed();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.Issue#isFixed <em>Fixed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fixed</em>' attribute.
	 * @see #isFixed()
	 * @generated
	 */
	void setFixed(boolean value);

	/**
	 * Returns the value of the '<em><b>Generated Report Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Generated Report Location</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Generated Report Location</em>' attribute.
	 * @see #setGeneratedReportLocation(String)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getIssue_GeneratedReportLocation()
	 * @model
	 * @generated
	 */
	String getGeneratedReportLocation();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.Issue#getGeneratedReportLocation <em>Generated Report Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Generated Report Location</em>' attribute.
	 * @see #getGeneratedReportLocation()
	 * @generated
	 */
	void setGeneratedReportLocation(String value);

	/**
	 * Returns the value of the '<em><b>Quick Fixes</b></em>' containment reference list.
	 * The list contents are of type {@link org.jboss.tools.windup.windup.QuickFix}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Quick Fixes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Quick Fixes</em>' containment reference list.
	 * @see org.jboss.tools.windup.windup.WindupPackage#getIssue_QuickFixes()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<QuickFix> getQuickFixes();

} // Issue
