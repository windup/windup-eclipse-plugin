/**
 */
package org.jboss.tools.windup.windup.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.jboss.tools.windup.windup.CustomRuleProvider;
import org.jboss.tools.windup.windup.WindupPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Custom Rule Provider</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.jboss.tools.windup.windup.impl.CustomRuleProviderImpl#getLocationURI <em>Location URI</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.CustomRuleProviderImpl#getRulesetId <em>Ruleset Id</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.CustomRuleProviderImpl#isExternal <em>External</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CustomRuleProviderImpl extends MinimalEObjectImpl.Container implements CustomRuleProvider {
	/**
	 * The default value of the '{@link #getLocationURI() <em>Location URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLocationURI()
	 * @generated
	 * @ordered
	 */
	protected static final String LOCATION_URI_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLocationURI() <em>Location URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLocationURI()
	 * @generated
	 * @ordered
	 */
	protected String locationURI = LOCATION_URI_EDEFAULT;

	/**
	 * The default value of the '{@link #getRulesetId() <em>Ruleset Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRulesetId()
	 * @generated
	 * @ordered
	 */
	protected static final String RULESET_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRulesetId() <em>Ruleset Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRulesetId()
	 * @generated
	 * @ordered
	 */
	protected String rulesetId = RULESET_ID_EDEFAULT;

	/**
	 * The default value of the '{@link #isExternal() <em>External</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isExternal()
	 * @generated
	 * @ordered
	 */
	protected static final boolean EXTERNAL_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isExternal() <em>External</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isExternal()
	 * @generated
	 * @ordered
	 */
	protected boolean external = EXTERNAL_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CustomRuleProviderImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return WindupPackage.eINSTANCE.getCustomRuleProvider();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLocationURI() {
		return locationURI;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLocationURI(String newLocationURI) {
		String oldLocationURI = locationURI;
		locationURI = newLocationURI;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.CUSTOM_RULE_PROVIDER__LOCATION_URI, oldLocationURI, locationURI));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getRulesetId() {
		return rulesetId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRulesetId(String newRulesetId) {
		String oldRulesetId = rulesetId;
		rulesetId = newRulesetId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.CUSTOM_RULE_PROVIDER__RULESET_ID, oldRulesetId, rulesetId));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isExternal() {
		return external;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExternal(boolean newExternal) {
		boolean oldExternal = external;
		external = newExternal;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.CUSTOM_RULE_PROVIDER__EXTERNAL, oldExternal, external));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case WindupPackage.CUSTOM_RULE_PROVIDER__LOCATION_URI:
				return getLocationURI();
			case WindupPackage.CUSTOM_RULE_PROVIDER__RULESET_ID:
				return getRulesetId();
			case WindupPackage.CUSTOM_RULE_PROVIDER__EXTERNAL:
				return isExternal();
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
			case WindupPackage.CUSTOM_RULE_PROVIDER__LOCATION_URI:
				setLocationURI((String)newValue);
				return;
			case WindupPackage.CUSTOM_RULE_PROVIDER__RULESET_ID:
				setRulesetId((String)newValue);
				return;
			case WindupPackage.CUSTOM_RULE_PROVIDER__EXTERNAL:
				setExternal((Boolean)newValue);
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
			case WindupPackage.CUSTOM_RULE_PROVIDER__LOCATION_URI:
				setLocationURI(LOCATION_URI_EDEFAULT);
				return;
			case WindupPackage.CUSTOM_RULE_PROVIDER__RULESET_ID:
				setRulesetId(RULESET_ID_EDEFAULT);
				return;
			case WindupPackage.CUSTOM_RULE_PROVIDER__EXTERNAL:
				setExternal(EXTERNAL_EDEFAULT);
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
			case WindupPackage.CUSTOM_RULE_PROVIDER__LOCATION_URI:
				return LOCATION_URI_EDEFAULT == null ? locationURI != null : !LOCATION_URI_EDEFAULT.equals(locationURI);
			case WindupPackage.CUSTOM_RULE_PROVIDER__RULESET_ID:
				return RULESET_ID_EDEFAULT == null ? rulesetId != null : !RULESET_ID_EDEFAULT.equals(rulesetId);
			case WindupPackage.CUSTOM_RULE_PROVIDER__EXTERNAL:
				return external != EXTERNAL_EDEFAULT;
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
		result.append(" (locationURI: ");
		result.append(locationURI);
		result.append(", rulesetId: ");
		result.append(rulesetId);
		result.append(", external: ");
		result.append(external);
		result.append(')');
		return result.toString();
	}

} //CustomRuleProviderImpl
