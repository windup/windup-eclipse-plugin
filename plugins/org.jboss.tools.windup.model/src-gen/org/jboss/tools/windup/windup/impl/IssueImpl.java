/**
 */
package org.jboss.tools.windup.windup.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.jboss.tools.windup.windup.Issue;
import org.jboss.tools.windup.windup.Link;
import org.jboss.tools.windup.windup.WindupPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Issue</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.jboss.tools.windup.windup.impl.IssueImpl#getElementId <em>Element Id</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.IssueImpl#getLinks <em>Links</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.IssueImpl#getFileAbsolutePath <em>File Absolute Path</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.IssueImpl#getSeverity <em>Severity</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.IssueImpl#getRuleId <em>Rule Id</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.IssueImpl#getEffort <em>Effort</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.IssueImpl#isFixed <em>Fixed</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.IssueImpl#getQuickFix <em>Quick Fix</em>}</li>
 * </ul>
 *
 * @generated
 */
public class IssueImpl extends MinimalEObjectImpl.Container implements Issue {
	/**
	 * The default value of the '{@link #getElementId() <em>Element Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getElementId()
	 * @generated
	 * @ordered
	 */
	protected static final String ELEMENT_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getElementId() <em>Element Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getElementId()
	 * @generated
	 * @ordered
	 */
	protected String elementId = ELEMENT_ID_EDEFAULT;

	/**
	 * The cached value of the '{@link #getLinks() <em>Links</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLinks()
	 * @generated
	 * @ordered
	 */
	protected EList<Link> links;

	/**
	 * The default value of the '{@link #getFileAbsolutePath() <em>File Absolute Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFileAbsolutePath()
	 * @generated
	 * @ordered
	 */
	protected static final String FILE_ABSOLUTE_PATH_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFileAbsolutePath() <em>File Absolute Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFileAbsolutePath()
	 * @generated
	 * @ordered
	 */
	protected String fileAbsolutePath = FILE_ABSOLUTE_PATH_EDEFAULT;

	/**
	 * The default value of the '{@link #getSeverity() <em>Severity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSeverity()
	 * @generated
	 * @ordered
	 */
	protected static final String SEVERITY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSeverity() <em>Severity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSeverity()
	 * @generated
	 * @ordered
	 */
	protected String severity = SEVERITY_EDEFAULT;

	/**
	 * The default value of the '{@link #getRuleId() <em>Rule Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRuleId()
	 * @generated
	 * @ordered
	 */
	protected static final String RULE_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRuleId() <em>Rule Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRuleId()
	 * @generated
	 * @ordered
	 */
	protected String ruleId = RULE_ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getEffort() <em>Effort</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEffort()
	 * @generated
	 * @ordered
	 */
	protected static final int EFFORT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getEffort() <em>Effort</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEffort()
	 * @generated
	 * @ordered
	 */
	protected int effort = EFFORT_EDEFAULT;

	/**
	 * The default value of the '{@link #isFixed() <em>Fixed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFixed()
	 * @generated
	 * @ordered
	 */
	protected static final boolean FIXED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isFixed() <em>Fixed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFixed()
	 * @generated
	 * @ordered
	 */
	protected boolean fixed = FIXED_EDEFAULT;

	/**
	 * The default value of the '{@link #getQuickFix() <em>Quick Fix</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQuickFix()
	 * @generated
	 * @ordered
	 */
	protected static final String QUICK_FIX_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getQuickFix() <em>Quick Fix</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQuickFix()
	 * @generated
	 * @ordered
	 */
	protected String quickFix = QUICK_FIX_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IssueImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return WindupPackage.eINSTANCE.getIssue();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getElementId() {
		return elementId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setElementId(String newElementId) {
		String oldElementId = elementId;
		elementId = newElementId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.ISSUE__ELEMENT_ID, oldElementId, elementId));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Link> getLinks() {
		if (links == null) {
			links = new EObjectContainmentEList.Resolving<Link>(Link.class, this, WindupPackage.ISSUE__LINKS);
		}
		return links;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getFileAbsolutePath() {
		return fileAbsolutePath;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFileAbsolutePath(String newFileAbsolutePath) {
		String oldFileAbsolutePath = fileAbsolutePath;
		fileAbsolutePath = newFileAbsolutePath;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.ISSUE__FILE_ABSOLUTE_PATH, oldFileAbsolutePath, fileAbsolutePath));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSeverity() {
		return severity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSeverity(String newSeverity) {
		String oldSeverity = severity;
		severity = newSeverity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.ISSUE__SEVERITY, oldSeverity, severity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getRuleId() {
		return ruleId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRuleId(String newRuleId) {
		String oldRuleId = ruleId;
		ruleId = newRuleId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.ISSUE__RULE_ID, oldRuleId, ruleId));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getEffort() {
		return effort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEffort(int newEffort) {
		int oldEffort = effort;
		effort = newEffort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.ISSUE__EFFORT, oldEffort, effort));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isFixed() {
		return fixed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFixed(boolean newFixed) {
		boolean oldFixed = fixed;
		fixed = newFixed;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.ISSUE__FIXED, oldFixed, fixed));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getQuickFix() {
		return quickFix;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setQuickFix(String newQuickFix) {
		String oldQuickFix = quickFix;
		quickFix = newQuickFix;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.ISSUE__QUICK_FIX, oldQuickFix, quickFix));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case WindupPackage.ISSUE__LINKS:
				return ((InternalEList<?>)getLinks()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case WindupPackage.ISSUE__ELEMENT_ID:
				return getElementId();
			case WindupPackage.ISSUE__LINKS:
				return getLinks();
			case WindupPackage.ISSUE__FILE_ABSOLUTE_PATH:
				return getFileAbsolutePath();
			case WindupPackage.ISSUE__SEVERITY:
				return getSeverity();
			case WindupPackage.ISSUE__RULE_ID:
				return getRuleId();
			case WindupPackage.ISSUE__EFFORT:
				return getEffort();
			case WindupPackage.ISSUE__FIXED:
				return isFixed();
			case WindupPackage.ISSUE__QUICK_FIX:
				return getQuickFix();
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
			case WindupPackage.ISSUE__ELEMENT_ID:
				setElementId((String)newValue);
				return;
			case WindupPackage.ISSUE__LINKS:
				getLinks().clear();
				getLinks().addAll((Collection<? extends Link>)newValue);
				return;
			case WindupPackage.ISSUE__FILE_ABSOLUTE_PATH:
				setFileAbsolutePath((String)newValue);
				return;
			case WindupPackage.ISSUE__SEVERITY:
				setSeverity((String)newValue);
				return;
			case WindupPackage.ISSUE__RULE_ID:
				setRuleId((String)newValue);
				return;
			case WindupPackage.ISSUE__EFFORT:
				setEffort((Integer)newValue);
				return;
			case WindupPackage.ISSUE__FIXED:
				setFixed((Boolean)newValue);
				return;
			case WindupPackage.ISSUE__QUICK_FIX:
				setQuickFix((String)newValue);
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
			case WindupPackage.ISSUE__ELEMENT_ID:
				setElementId(ELEMENT_ID_EDEFAULT);
				return;
			case WindupPackage.ISSUE__LINKS:
				getLinks().clear();
				return;
			case WindupPackage.ISSUE__FILE_ABSOLUTE_PATH:
				setFileAbsolutePath(FILE_ABSOLUTE_PATH_EDEFAULT);
				return;
			case WindupPackage.ISSUE__SEVERITY:
				setSeverity(SEVERITY_EDEFAULT);
				return;
			case WindupPackage.ISSUE__RULE_ID:
				setRuleId(RULE_ID_EDEFAULT);
				return;
			case WindupPackage.ISSUE__EFFORT:
				setEffort(EFFORT_EDEFAULT);
				return;
			case WindupPackage.ISSUE__FIXED:
				setFixed(FIXED_EDEFAULT);
				return;
			case WindupPackage.ISSUE__QUICK_FIX:
				setQuickFix(QUICK_FIX_EDEFAULT);
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
			case WindupPackage.ISSUE__ELEMENT_ID:
				return ELEMENT_ID_EDEFAULT == null ? elementId != null : !ELEMENT_ID_EDEFAULT.equals(elementId);
			case WindupPackage.ISSUE__LINKS:
				return links != null && !links.isEmpty();
			case WindupPackage.ISSUE__FILE_ABSOLUTE_PATH:
				return FILE_ABSOLUTE_PATH_EDEFAULT == null ? fileAbsolutePath != null : !FILE_ABSOLUTE_PATH_EDEFAULT.equals(fileAbsolutePath);
			case WindupPackage.ISSUE__SEVERITY:
				return SEVERITY_EDEFAULT == null ? severity != null : !SEVERITY_EDEFAULT.equals(severity);
			case WindupPackage.ISSUE__RULE_ID:
				return RULE_ID_EDEFAULT == null ? ruleId != null : !RULE_ID_EDEFAULT.equals(ruleId);
			case WindupPackage.ISSUE__EFFORT:
				return effort != EFFORT_EDEFAULT;
			case WindupPackage.ISSUE__FIXED:
				return fixed != FIXED_EDEFAULT;
			case WindupPackage.ISSUE__QUICK_FIX:
				return QUICK_FIX_EDEFAULT == null ? quickFix != null : !QUICK_FIX_EDEFAULT.equals(quickFix);
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
		result.append(" (elementId: ");
		result.append(elementId);
		result.append(", fileAbsolutePath: ");
		result.append(fileAbsolutePath);
		result.append(", severity: ");
		result.append(severity);
		result.append(", ruleId: ");
		result.append(ruleId);
		result.append(", effort: ");
		result.append(effort);
		result.append(", fixed: ");
		result.append(fixed);
		result.append(", quickFix: ");
		result.append(quickFix);
		result.append(')');
		return result.toString();
	}

} //IssueImpl
