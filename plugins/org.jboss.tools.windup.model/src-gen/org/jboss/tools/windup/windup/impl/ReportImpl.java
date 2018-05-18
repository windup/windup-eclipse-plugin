/**
 */
package org.jboss.tools.windup.windup.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.jboss.tools.windup.windup.Report;
import org.jboss.tools.windup.windup.WindupPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Report</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.jboss.tools.windup.windup.impl.ReportImpl#getLocation <em>Location</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.ReportImpl#getInputFile <em>Input File</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ReportImpl extends MinimalEObjectImpl.Container implements Report {
	/**
	 * The default value of the '{@link #getLocation() <em>Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLocation()
	 * @generated
	 * @ordered
	 */
	protected static final String LOCATION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLocation() <em>Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLocation()
	 * @generated
	 * @ordered
	 */
	protected String location = LOCATION_EDEFAULT;

	/**
	 * The default value of the '{@link #getInputFile() <em>Input File</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInputFile()
	 * @generated
	 * @ordered
	 */
	protected static final String INPUT_FILE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getInputFile() <em>Input File</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInputFile()
	 * @generated
	 * @ordered
	 */
	protected String inputFile = INPUT_FILE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ReportImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return WindupPackage.eINSTANCE.getReport();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLocation(String newLocation) {
		String oldLocation = location;
		location = newLocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.REPORT__LOCATION, oldLocation, location));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getInputFile() {
		return inputFile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInputFile(String newInputFile) {
		String oldInputFile = inputFile;
		inputFile = newInputFile;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.REPORT__INPUT_FILE, oldInputFile, inputFile));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case WindupPackage.REPORT__LOCATION:
				return getLocation();
			case WindupPackage.REPORT__INPUT_FILE:
				return getInputFile();
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
			case WindupPackage.REPORT__LOCATION:
				setLocation((String)newValue);
				return;
			case WindupPackage.REPORT__INPUT_FILE:
				setInputFile((String)newValue);
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
			case WindupPackage.REPORT__LOCATION:
				setLocation(LOCATION_EDEFAULT);
				return;
			case WindupPackage.REPORT__INPUT_FILE:
				setInputFile(INPUT_FILE_EDEFAULT);
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
			case WindupPackage.REPORT__LOCATION:
				return LOCATION_EDEFAULT == null ? location != null : !LOCATION_EDEFAULT.equals(location);
			case WindupPackage.REPORT__INPUT_FILE:
				return INPUT_FILE_EDEFAULT == null ? inputFile != null : !INPUT_FILE_EDEFAULT.equals(inputFile);
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
		result.append(" (location: ");
		result.append(location);
		result.append(", inputFile: ");
		result.append(inputFile);
		result.append(')');
		return result.toString();
	}

} //ReportImpl
