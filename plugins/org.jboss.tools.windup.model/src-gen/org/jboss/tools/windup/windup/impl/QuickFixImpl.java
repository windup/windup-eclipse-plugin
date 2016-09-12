/**
 */
package org.jboss.tools.windup.windup.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.jboss.tools.windup.windup.QuickFix;
import org.jboss.tools.windup.windup.WindupPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Quick Fix</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.jboss.tools.windup.windup.impl.QuickFixImpl#getNewLine <em>New Line</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.QuickFixImpl#getReplacementString <em>Replacement String</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.QuickFixImpl#getSearchString <em>Search String</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.QuickFixImpl#getQuickFixType <em>Quick Fix Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class QuickFixImpl extends NamedElementImpl implements QuickFix {
	/**
	 * The default value of the '{@link #getNewLine() <em>New Line</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewLine()
	 * @generated
	 * @ordered
	 */
	protected static final String NEW_LINE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getNewLine() <em>New Line</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewLine()
	 * @generated
	 * @ordered
	 */
	protected String newLine = NEW_LINE_EDEFAULT;

	/**
	 * The default value of the '{@link #getReplacementString() <em>Replacement String</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReplacementString()
	 * @generated
	 * @ordered
	 */
	protected static final String REPLACEMENT_STRING_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getReplacementString() <em>Replacement String</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReplacementString()
	 * @generated
	 * @ordered
	 */
	protected String replacementString = REPLACEMENT_STRING_EDEFAULT;

	/**
	 * The default value of the '{@link #getSearchString() <em>Search String</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSearchString()
	 * @generated
	 * @ordered
	 */
	protected static final String SEARCH_STRING_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSearchString() <em>Search String</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSearchString()
	 * @generated
	 * @ordered
	 */
	protected String searchString = SEARCH_STRING_EDEFAULT;

	/**
	 * The default value of the '{@link #getQuickFixType() <em>Quick Fix Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQuickFixType()
	 * @generated
	 * @ordered
	 */
	protected static final String QUICK_FIX_TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getQuickFixType() <em>Quick Fix Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQuickFixType()
	 * @generated
	 * @ordered
	 */
	protected String quickFixType = QUICK_FIX_TYPE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected QuickFixImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return WindupPackage.eINSTANCE.getQuickFix();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getNewLine() {
		return newLine;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNewLine(String newNewLine) {
		String oldNewLine = newLine;
		newLine = newNewLine;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.QUICK_FIX__NEW_LINE, oldNewLine, newLine));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getReplacementString() {
		return replacementString;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setReplacementString(String newReplacementString) {
		String oldReplacementString = replacementString;
		replacementString = newReplacementString;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.QUICK_FIX__REPLACEMENT_STRING, oldReplacementString, replacementString));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSearchString() {
		return searchString;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSearchString(String newSearchString) {
		String oldSearchString = searchString;
		searchString = newSearchString;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.QUICK_FIX__SEARCH_STRING, oldSearchString, searchString));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getQuickFixType() {
		return quickFixType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setQuickFixType(String newQuickFixType) {
		String oldQuickFixType = quickFixType;
		quickFixType = newQuickFixType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.QUICK_FIX__QUICK_FIX_TYPE, oldQuickFixType, quickFixType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case WindupPackage.QUICK_FIX__NEW_LINE:
				return getNewLine();
			case WindupPackage.QUICK_FIX__REPLACEMENT_STRING:
				return getReplacementString();
			case WindupPackage.QUICK_FIX__SEARCH_STRING:
				return getSearchString();
			case WindupPackage.QUICK_FIX__QUICK_FIX_TYPE:
				return getQuickFixType();
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
			case WindupPackage.QUICK_FIX__NEW_LINE:
				setNewLine((String)newValue);
				return;
			case WindupPackage.QUICK_FIX__REPLACEMENT_STRING:
				setReplacementString((String)newValue);
				return;
			case WindupPackage.QUICK_FIX__SEARCH_STRING:
				setSearchString((String)newValue);
				return;
			case WindupPackage.QUICK_FIX__QUICK_FIX_TYPE:
				setQuickFixType((String)newValue);
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
			case WindupPackage.QUICK_FIX__NEW_LINE:
				setNewLine(NEW_LINE_EDEFAULT);
				return;
			case WindupPackage.QUICK_FIX__REPLACEMENT_STRING:
				setReplacementString(REPLACEMENT_STRING_EDEFAULT);
				return;
			case WindupPackage.QUICK_FIX__SEARCH_STRING:
				setSearchString(SEARCH_STRING_EDEFAULT);
				return;
			case WindupPackage.QUICK_FIX__QUICK_FIX_TYPE:
				setQuickFixType(QUICK_FIX_TYPE_EDEFAULT);
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
			case WindupPackage.QUICK_FIX__NEW_LINE:
				return NEW_LINE_EDEFAULT == null ? newLine != null : !NEW_LINE_EDEFAULT.equals(newLine);
			case WindupPackage.QUICK_FIX__REPLACEMENT_STRING:
				return REPLACEMENT_STRING_EDEFAULT == null ? replacementString != null : !REPLACEMENT_STRING_EDEFAULT.equals(replacementString);
			case WindupPackage.QUICK_FIX__SEARCH_STRING:
				return SEARCH_STRING_EDEFAULT == null ? searchString != null : !SEARCH_STRING_EDEFAULT.equals(searchString);
			case WindupPackage.QUICK_FIX__QUICK_FIX_TYPE:
				return QUICK_FIX_TYPE_EDEFAULT == null ? quickFixType != null : !QUICK_FIX_TYPE_EDEFAULT.equals(quickFixType);
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
		result.append(" (newLine: ");
		result.append(newLine);
		result.append(", replacementString: ");
		result.append(replacementString);
		result.append(", searchString: ");
		result.append(searchString);
		result.append(", quickFixType: ");
		result.append(quickFixType);
		result.append(')');
		return result.toString();
	}

} //QuickFixImpl
