/**
 */
package org.jboss.tools.windup.windup.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.jboss.tools.windup.windup.Hint;
import org.jboss.tools.windup.windup.WindupPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Hint</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.jboss.tools.windup.windup.impl.HintImpl#getLineNumber <em>Line Number</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.HintImpl#getColumn <em>Column</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.HintImpl#getLength <em>Length</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.HintImpl#getSourceSnippet <em>Source Snippet</em>}</li>
 * </ul>
 *
 * @generated
 */
public class HintImpl extends IssueImpl implements Hint {
	/**
	 * The default value of the '{@link #getLineNumber() <em>Line Number</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLineNumber()
	 * @generated
	 * @ordered
	 */
	protected static final int LINE_NUMBER_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getLineNumber() <em>Line Number</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLineNumber()
	 * @generated
	 * @ordered
	 */
	protected int lineNumber = LINE_NUMBER_EDEFAULT;

	/**
	 * The default value of the '{@link #getColumn() <em>Column</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getColumn()
	 * @generated
	 * @ordered
	 */
	protected static final int COLUMN_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getColumn() <em>Column</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getColumn()
	 * @generated
	 * @ordered
	 */
	protected int column = COLUMN_EDEFAULT;

	/**
	 * The default value of the '{@link #getLength() <em>Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLength()
	 * @generated
	 * @ordered
	 */
	protected static final int LENGTH_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getLength() <em>Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLength()
	 * @generated
	 * @ordered
	 */
	protected int length = LENGTH_EDEFAULT;

	/**
	 * The default value of the '{@link #getSourceSnippet() <em>Source Snippet</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSourceSnippet()
	 * @generated
	 * @ordered
	 */
	protected static final String SOURCE_SNIPPET_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSourceSnippet() <em>Source Snippet</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSourceSnippet()
	 * @generated
	 * @ordered
	 */
	protected String sourceSnippet = SOURCE_SNIPPET_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected HintImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return WindupPackage.eINSTANCE.getHint();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getLineNumber() {
		return lineNumber;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLineNumber(int newLineNumber) {
		int oldLineNumber = lineNumber;
		lineNumber = newLineNumber;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.HINT__LINE_NUMBER, oldLineNumber, lineNumber));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setColumn(int newColumn) {
		int oldColumn = column;
		column = newColumn;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.HINT__COLUMN, oldColumn, column));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getLength() {
		return length;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLength(int newLength) {
		int oldLength = length;
		length = newLength;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.HINT__LENGTH, oldLength, length));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSourceSnippet() {
		return sourceSnippet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSourceSnippet(String newSourceSnippet) {
		String oldSourceSnippet = sourceSnippet;
		sourceSnippet = newSourceSnippet;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.HINT__SOURCE_SNIPPET, oldSourceSnippet, sourceSnippet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case WindupPackage.HINT__LINE_NUMBER:
				return getLineNumber();
			case WindupPackage.HINT__COLUMN:
				return getColumn();
			case WindupPackage.HINT__LENGTH:
				return getLength();
			case WindupPackage.HINT__SOURCE_SNIPPET:
				return getSourceSnippet();
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
			case WindupPackage.HINT__LINE_NUMBER:
				setLineNumber((Integer)newValue);
				return;
			case WindupPackage.HINT__COLUMN:
				setColumn((Integer)newValue);
				return;
			case WindupPackage.HINT__LENGTH:
				setLength((Integer)newValue);
				return;
			case WindupPackage.HINT__SOURCE_SNIPPET:
				setSourceSnippet((String)newValue);
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
			case WindupPackage.HINT__LINE_NUMBER:
				setLineNumber(LINE_NUMBER_EDEFAULT);
				return;
			case WindupPackage.HINT__COLUMN:
				setColumn(COLUMN_EDEFAULT);
				return;
			case WindupPackage.HINT__LENGTH:
				setLength(LENGTH_EDEFAULT);
				return;
			case WindupPackage.HINT__SOURCE_SNIPPET:
				setSourceSnippet(SOURCE_SNIPPET_EDEFAULT);
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
			case WindupPackage.HINT__LINE_NUMBER:
				return lineNumber != LINE_NUMBER_EDEFAULT;
			case WindupPackage.HINT__COLUMN:
				return column != COLUMN_EDEFAULT;
			case WindupPackage.HINT__LENGTH:
				return length != LENGTH_EDEFAULT;
			case WindupPackage.HINT__SOURCE_SNIPPET:
				return SOURCE_SNIPPET_EDEFAULT == null ? sourceSnippet != null : !SOURCE_SNIPPET_EDEFAULT.equals(sourceSnippet);
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
		result.append(" (lineNumber: ");
		result.append(lineNumber);
		result.append(", column: ");
		result.append(column);
		result.append(", length: ");
		result.append(length);
		result.append(", sourceSnippet: ");
		result.append(sourceSnippet);
		result.append(')');
		return result.toString();
	}

} //HintImpl
