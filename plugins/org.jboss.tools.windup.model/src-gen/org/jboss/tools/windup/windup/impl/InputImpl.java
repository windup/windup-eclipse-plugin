/**
 */
package org.jboss.tools.windup.windup.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.jboss.tools.windup.windup.Input;
import org.jboss.tools.windup.windup.WindupPackage;
import org.jboss.tools.windup.windup.WindupResult;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Input</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.jboss.tools.windup.windup.impl.InputImpl#getUri <em>Uri</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.InputImpl#getWindupResult <em>Windup Result</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.InputImpl#getGeneratedReportLocation <em>Generated Report Location</em>}</li>
 * </ul>
 *
 * @generated
 */
public class InputImpl extends NamedElementImpl implements Input {
	/**
	 * The default value of the '{@link #getUri() <em>Uri</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUri()
	 * @generated
	 * @ordered
	 */
	protected static final String URI_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUri() <em>Uri</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUri()
	 * @generated
	 * @ordered
	 */
	protected String uri = URI_EDEFAULT;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InputImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return WindupPackage.eINSTANCE.getInput();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUri(String newUri) {
		String oldUri = uri;
		uri = newUri;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.INPUT__URI, oldUri, uri));
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
				NotificationChain msgs = oldWindupResult.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - WindupPackage.INPUT__WINDUP_RESULT, null, null);
				if (newWindupResult.eInternalContainer() == null) {
					msgs = newWindupResult.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - WindupPackage.INPUT__WINDUP_RESULT, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, WindupPackage.INPUT__WINDUP_RESULT, oldWindupResult, windupResult));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, WindupPackage.INPUT__WINDUP_RESULT, oldWindupResult, newWindupResult);
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
				msgs = ((InternalEObject)windupResult).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - WindupPackage.INPUT__WINDUP_RESULT, null, msgs);
			if (newWindupResult != null)
				msgs = ((InternalEObject)newWindupResult).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - WindupPackage.INPUT__WINDUP_RESULT, null, msgs);
			msgs = basicSetWindupResult(newWindupResult, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.INPUT__WINDUP_RESULT, newWindupResult, newWindupResult));
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
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.INPUT__GENERATED_REPORT_LOCATION, oldGeneratedReportLocation, generatedReportLocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case WindupPackage.INPUT__WINDUP_RESULT:
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
			case WindupPackage.INPUT__URI:
				return getUri();
			case WindupPackage.INPUT__WINDUP_RESULT:
				if (resolve) return getWindupResult();
				return basicGetWindupResult();
			case WindupPackage.INPUT__GENERATED_REPORT_LOCATION:
				return getGeneratedReportLocation();
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
			case WindupPackage.INPUT__URI:
				setUri((String)newValue);
				return;
			case WindupPackage.INPUT__WINDUP_RESULT:
				setWindupResult((WindupResult)newValue);
				return;
			case WindupPackage.INPUT__GENERATED_REPORT_LOCATION:
				setGeneratedReportLocation((String)newValue);
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
			case WindupPackage.INPUT__URI:
				setUri(URI_EDEFAULT);
				return;
			case WindupPackage.INPUT__WINDUP_RESULT:
				setWindupResult((WindupResult)null);
				return;
			case WindupPackage.INPUT__GENERATED_REPORT_LOCATION:
				setGeneratedReportLocation(GENERATED_REPORT_LOCATION_EDEFAULT);
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
			case WindupPackage.INPUT__URI:
				return URI_EDEFAULT == null ? uri != null : !URI_EDEFAULT.equals(uri);
			case WindupPackage.INPUT__WINDUP_RESULT:
				return windupResult != null;
			case WindupPackage.INPUT__GENERATED_REPORT_LOCATION:
				return GENERATED_REPORT_LOCATION_EDEFAULT == null ? generatedReportLocation != null : !GENERATED_REPORT_LOCATION_EDEFAULT.equals(generatedReportLocation);
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
		result.append(" (uri: ");
		result.append(uri);
		result.append(", generatedReportLocation: ");
		result.append(generatedReportLocation);
		result.append(')');
		return result.toString();
	}

} //InputImpl
