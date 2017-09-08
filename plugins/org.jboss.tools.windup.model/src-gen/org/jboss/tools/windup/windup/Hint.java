/**
 */
package org.jboss.tools.windup.windup;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Hint</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.jboss.tools.windup.windup.Hint#getLineNumber <em>Line Number</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.Hint#getColumn <em>Column</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.Hint#getLength <em>Length</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.Hint#getSourceSnippet <em>Source Snippet</em>}</li>
 * </ul>
 *
 * @see org.jboss.tools.windup.windup.WindupPackage#getHint()
 * @model
 * @generated
 */
public interface Hint extends Issue {
	/**
	 * Returns the value of the '<em><b>Line Number</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Line Number</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Line Number</em>' attribute.
	 * @see #setLineNumber(int)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getHint_LineNumber()
	 * @model
	 * @generated
	 */
	int getLineNumber();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.Hint#getLineNumber <em>Line Number</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Line Number</em>' attribute.
	 * @see #getLineNumber()
	 * @generated
	 */
	void setLineNumber(int value);

	/**
	 * Returns the value of the '<em><b>Column</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Column</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Column</em>' attribute.
	 * @see #setColumn(int)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getHint_Column()
	 * @model
	 * @generated
	 */
	int getColumn();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.Hint#getColumn <em>Column</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Column</em>' attribute.
	 * @see #getColumn()
	 * @generated
	 */
	void setColumn(int value);

	/**
	 * Returns the value of the '<em><b>Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Length</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Length</em>' attribute.
	 * @see #setLength(int)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getHint_Length()
	 * @model
	 * @generated
	 */
	int getLength();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.Hint#getLength <em>Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Length</em>' attribute.
	 * @see #getLength()
	 * @generated
	 */
	void setLength(int value);

	/**
	 * Returns the value of the '<em><b>Source Snippet</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source Snippet</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source Snippet</em>' attribute.
	 * @see #setSourceSnippet(String)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getHint_SourceSnippet()
	 * @model
	 * @generated
	 */
	String getSourceSnippet();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.Hint#getSourceSnippet <em>Source Snippet</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source Snippet</em>' attribute.
	 * @see #getSourceSnippet()
	 * @generated
	 */
	void setSourceSnippet(String value);

} // Hint
