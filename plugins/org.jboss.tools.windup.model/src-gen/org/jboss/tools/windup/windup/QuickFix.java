/**
 */
package org.jboss.tools.windup.windup;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Quick Fix</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.jboss.tools.windup.windup.QuickFix#getNewLine <em>New Line</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.QuickFix#getReplacementString <em>Replacement String</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.QuickFix#getSearchString <em>Search String</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.QuickFix#getQuickFixType <em>Quick Fix Type</em>}</li>
 * </ul>
 *
 * @see org.jboss.tools.windup.windup.WindupPackage#getQuickFix()
 * @model
 * @generated
 */
public interface QuickFix extends NamedElement {
	/**
	 * Returns the value of the '<em><b>New Line</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Line</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>New Line</em>' attribute.
	 * @see #setNewLine(String)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getQuickFix_NewLine()
	 * @model
	 * @generated
	 */
	String getNewLine();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.QuickFix#getNewLine <em>New Line</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>New Line</em>' attribute.
	 * @see #getNewLine()
	 * @generated
	 */
	void setNewLine(String value);

	/**
	 * Returns the value of the '<em><b>Replacement String</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Replacement String</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Replacement String</em>' attribute.
	 * @see #setReplacementString(String)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getQuickFix_ReplacementString()
	 * @model
	 * @generated
	 */
	String getReplacementString();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.QuickFix#getReplacementString <em>Replacement String</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Replacement String</em>' attribute.
	 * @see #getReplacementString()
	 * @generated
	 */
	void setReplacementString(String value);

	/**
	 * Returns the value of the '<em><b>Search String</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Search String</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Search String</em>' attribute.
	 * @see #setSearchString(String)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getQuickFix_SearchString()
	 * @model
	 * @generated
	 */
	String getSearchString();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.QuickFix#getSearchString <em>Search String</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Search String</em>' attribute.
	 * @see #getSearchString()
	 * @generated
	 */
	void setSearchString(String value);

	/**
	 * Returns the value of the '<em><b>Quick Fix Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Quick Fix Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Quick Fix Type</em>' attribute.
	 * @see #setQuickFixType(String)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getQuickFix_QuickFixType()
	 * @model
	 * @generated
	 */
	String getQuickFixType();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.QuickFix#getQuickFixType <em>Quick Fix Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Quick Fix Type</em>' attribute.
	 * @see #getQuickFixType()
	 * @generated
	 */
	void setQuickFixType(String value);

} // QuickFix
