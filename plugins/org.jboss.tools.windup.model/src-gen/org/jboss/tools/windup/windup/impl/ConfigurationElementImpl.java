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

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.jboss.tools.windup.windup.ConfigurationElement;
import org.jboss.tools.windup.windup.Input;
import org.jboss.tools.windup.windup.WindupPackage;
import org.jboss.tools.windup.windup.WindupResult;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Configuration Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.jboss.tools.windup.windup.impl.ConfigurationElementImpl#getInputs <em>Inputs</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.ConfigurationElementImpl#getWindupResult <em>Windup Result</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.ConfigurationElementImpl#getWindupHome <em>Windup Home</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.ConfigurationElementImpl#getGeneratedReportLocation <em>Generated Report Location</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.ConfigurationElementImpl#isSourceMode <em>Source Mode</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ConfigurationElementImpl extends ParameterizedImpl implements ConfigurationElement {
	/**
	 * The cached value of the '{@link #getInputs() <em>Inputs</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInputs()
	 * @generated
	 * @ordered
	 */
	protected EList<Input> inputs;

	/**
	 * The cached value of the '{@link #getWindupResult() <em>Windup Result</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindupResult()
	 * @generated
	 * @ordered
	 */
	protected WindupResult windupResult;

	/**
	 * The default value of the '{@link #getWindupHome() <em>Windup Home</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindupHome()
	 * @generated
	 * @ordered
	 */
	protected static final String WINDUP_HOME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getWindupHome() <em>Windup Home</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindupHome()
	 * @generated
	 * @ordered
	 */
	protected String windupHome = WINDUP_HOME_EDEFAULT;

	/**
	 * The default value of the '{@link #getGeneratedReportLocation() <em>Generated Report Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGeneratedReportLocation()
	 * @generated
	 * @ordered
	 */
	protected static final String GENERATED_REPORT_LOCATION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getGeneratedReportLocation() <em>Generated Report Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGeneratedReportLocation()
	 * @generated
	 * @ordered
	 */
	protected String generatedReportLocation = GENERATED_REPORT_LOCATION_EDEFAULT;

	/**
	 * The default value of the '{@link #isSourceMode() <em>Source Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSourceMode()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SOURCE_MODE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isSourceMode() <em>Source Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSourceMode()
	 * @generated
	 * @ordered
	 */
	protected boolean sourceMode = SOURCE_MODE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ConfigurationElementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return WindupPackage.eINSTANCE.getConfigurationElement();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Input> getInputs() {
		if (inputs == null) {
			inputs = new EObjectContainmentEList.Resolving<Input>(Input.class, this, WindupPackage.CONFIGURATION_ELEMENT__INPUTS);
		}
		return inputs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WindupResult getWindupResult() {
		if (windupResult != null && windupResult.eIsProxy()) {
			InternalEObject oldWindupResult = (InternalEObject)windupResult;
			windupResult = (WindupResult)eResolveProxy(oldWindupResult);
			if (windupResult != oldWindupResult) {
				InternalEObject newWindupResult = (InternalEObject)windupResult;
				NotificationChain msgs = oldWindupResult.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - WindupPackage.CONFIGURATION_ELEMENT__WINDUP_RESULT, null, null);
				if (newWindupResult.eInternalContainer() == null) {
					msgs = newWindupResult.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - WindupPackage.CONFIGURATION_ELEMENT__WINDUP_RESULT, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, WindupPackage.CONFIGURATION_ELEMENT__WINDUP_RESULT, oldWindupResult, windupResult));
			}
		}
		return windupResult;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WindupResult basicGetWindupResult() {
		return windupResult;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetWindupResult(WindupResult newWindupResult, NotificationChain msgs) {
		WindupResult oldWindupResult = windupResult;
		windupResult = newWindupResult;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, WindupPackage.CONFIGURATION_ELEMENT__WINDUP_RESULT, oldWindupResult, newWindupResult);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWindupResult(WindupResult newWindupResult) {
		if (newWindupResult != windupResult) {
			NotificationChain msgs = null;
			if (windupResult != null)
				msgs = ((InternalEObject)windupResult).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - WindupPackage.CONFIGURATION_ELEMENT__WINDUP_RESULT, null, msgs);
			if (newWindupResult != null)
				msgs = ((InternalEObject)newWindupResult).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - WindupPackage.CONFIGURATION_ELEMENT__WINDUP_RESULT, null, msgs);
			msgs = basicSetWindupResult(newWindupResult, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.CONFIGURATION_ELEMENT__WINDUP_RESULT, newWindupResult, newWindupResult));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getWindupHome() {
		return windupHome;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWindupHome(String newWindupHome) {
		String oldWindupHome = windupHome;
		windupHome = newWindupHome;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.CONFIGURATION_ELEMENT__WINDUP_HOME, oldWindupHome, windupHome));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getGeneratedReportLocation() {
		return generatedReportLocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setGeneratedReportLocation(String newGeneratedReportLocation) {
		String oldGeneratedReportLocation = generatedReportLocation;
		generatedReportLocation = newGeneratedReportLocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.CONFIGURATION_ELEMENT__GENERATED_REPORT_LOCATION, oldGeneratedReportLocation, generatedReportLocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSourceMode() {
		return sourceMode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSourceMode(boolean newSourceMode) {
		boolean oldSourceMode = sourceMode;
		sourceMode = newSourceMode;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.CONFIGURATION_ELEMENT__SOURCE_MODE, oldSourceMode, sourceMode));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case WindupPackage.CONFIGURATION_ELEMENT__INPUTS:
				return ((InternalEList<?>)getInputs()).basicRemove(otherEnd, msgs);
			case WindupPackage.CONFIGURATION_ELEMENT__WINDUP_RESULT:
				return basicSetWindupResult(null, msgs);
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
			case WindupPackage.CONFIGURATION_ELEMENT__INPUTS:
				return getInputs();
			case WindupPackage.CONFIGURATION_ELEMENT__WINDUP_RESULT:
				if (resolve) return getWindupResult();
				return basicGetWindupResult();
			case WindupPackage.CONFIGURATION_ELEMENT__WINDUP_HOME:
				return getWindupHome();
			case WindupPackage.CONFIGURATION_ELEMENT__GENERATED_REPORT_LOCATION:
				return getGeneratedReportLocation();
			case WindupPackage.CONFIGURATION_ELEMENT__SOURCE_MODE:
				return isSourceMode();
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
			case WindupPackage.CONFIGURATION_ELEMENT__INPUTS:
				getInputs().clear();
				getInputs().addAll((Collection<? extends Input>)newValue);
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__WINDUP_RESULT:
				setWindupResult((WindupResult)newValue);
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__WINDUP_HOME:
				setWindupHome((String)newValue);
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__GENERATED_REPORT_LOCATION:
				setGeneratedReportLocation((String)newValue);
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__SOURCE_MODE:
				setSourceMode((Boolean)newValue);
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
			case WindupPackage.CONFIGURATION_ELEMENT__INPUTS:
				getInputs().clear();
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__WINDUP_RESULT:
				setWindupResult((WindupResult)null);
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__WINDUP_HOME:
				setWindupHome(WINDUP_HOME_EDEFAULT);
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__GENERATED_REPORT_LOCATION:
				setGeneratedReportLocation(GENERATED_REPORT_LOCATION_EDEFAULT);
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__SOURCE_MODE:
				setSourceMode(SOURCE_MODE_EDEFAULT);
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
			case WindupPackage.CONFIGURATION_ELEMENT__INPUTS:
				return inputs != null && !inputs.isEmpty();
			case WindupPackage.CONFIGURATION_ELEMENT__WINDUP_RESULT:
				return windupResult != null;
			case WindupPackage.CONFIGURATION_ELEMENT__WINDUP_HOME:
				return WINDUP_HOME_EDEFAULT == null ? windupHome != null : !WINDUP_HOME_EDEFAULT.equals(windupHome);
			case WindupPackage.CONFIGURATION_ELEMENT__GENERATED_REPORT_LOCATION:
				return GENERATED_REPORT_LOCATION_EDEFAULT == null ? generatedReportLocation != null : !GENERATED_REPORT_LOCATION_EDEFAULT.equals(generatedReportLocation);
			case WindupPackage.CONFIGURATION_ELEMENT__SOURCE_MODE:
				return sourceMode != SOURCE_MODE_EDEFAULT;
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
		result.append(" (windupHome: ");
		result.append(windupHome);
		result.append(", generatedReportLocation: ");
		result.append(generatedReportLocation);
		result.append(", sourceMode: ");
		result.append(sourceMode);
		result.append(')');
		return result.toString();
	}

} //ConfigurationElementImpl
