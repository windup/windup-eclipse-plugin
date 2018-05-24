/**
 */
package org.jboss.tools.windup.windup.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.jboss.tools.windup.windup.IgnorePattern;
import org.jboss.tools.windup.windup.WindupPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Ignore Pattern</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.jboss.tools.windup.windup.impl.IgnorePatternImpl#getPattern <em>Pattern</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.IgnorePatternImpl#isEnabled <em>Enabled</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.IgnorePatternImpl#isReadFromFile <em>Read From File</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.IgnorePatternImpl#getRemoved <em>Removed</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.IgnorePatternImpl#getIgnoreFile <em>Ignore File</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.IgnorePatternImpl#getIgnoreFileTimestamp <em>Ignore File Timestamp</em>}</li>
 * </ul>
 *
 * @generated
 */
public class IgnorePatternImpl extends MinimalEObjectImpl.Container implements IgnorePattern {
	/**
	 * The default value of the '{@link #getPattern() <em>Pattern</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPattern()
	 * @generated
	 * @ordered
	 */
	protected static final String PATTERN_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPattern() <em>Pattern</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPattern()
	 * @generated
	 * @ordered
	 */
	protected String pattern = PATTERN_EDEFAULT;

	/**
	 * The default value of the '{@link #isEnabled() <em>Enabled</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isEnabled()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ENABLED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isEnabled() <em>Enabled</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isEnabled()
	 * @generated
	 * @ordered
	 */
	protected boolean enabled = ENABLED_EDEFAULT;

	/**
	 * The default value of the '{@link #isReadFromFile() <em>Read From File</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isReadFromFile()
	 * @generated
	 * @ordered
	 */
	protected static final boolean READ_FROM_FILE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isReadFromFile() <em>Read From File</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isReadFromFile()
	 * @generated
	 * @ordered
	 */
	protected boolean readFromFile = READ_FROM_FILE_EDEFAULT;

	/**
	 * The default value of the '{@link #getRemoved() <em>Removed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRemoved()
	 * @generated
	 * @ordered
	 */
	protected static final String REMOVED_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRemoved() <em>Removed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRemoved()
	 * @generated
	 * @ordered
	 */
	protected String removed = REMOVED_EDEFAULT;

	/**
	 * The default value of the '{@link #getIgnoreFile() <em>Ignore File</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIgnoreFile()
	 * @generated
	 * @ordered
	 */
	protected static final String IGNORE_FILE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getIgnoreFile() <em>Ignore File</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIgnoreFile()
	 * @generated
	 * @ordered
	 */
	protected String ignoreFile = IGNORE_FILE_EDEFAULT;

	/**
	 * The default value of the '{@link #getIgnoreFileTimestamp() <em>Ignore File Timestamp</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIgnoreFileTimestamp()
	 * @generated
	 * @ordered
	 */
	protected static final String IGNORE_FILE_TIMESTAMP_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getIgnoreFileTimestamp() <em>Ignore File Timestamp</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIgnoreFileTimestamp()
	 * @generated
	 * @ordered
	 */
	protected String ignoreFileTimestamp = IGNORE_FILE_TIMESTAMP_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IgnorePatternImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return WindupPackage.eINSTANCE.getIgnorePattern();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPattern(String newPattern) {
		String oldPattern = pattern;
		pattern = newPattern;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.IGNORE_PATTERN__PATTERN, oldPattern, pattern));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEnabled(boolean newEnabled) {
		boolean oldEnabled = enabled;
		enabled = newEnabled;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.IGNORE_PATTERN__ENABLED, oldEnabled, enabled));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isReadFromFile() {
		return readFromFile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setReadFromFile(boolean newReadFromFile) {
		boolean oldReadFromFile = readFromFile;
		readFromFile = newReadFromFile;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.IGNORE_PATTERN__READ_FROM_FILE, oldReadFromFile, readFromFile));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getRemoved() {
		return removed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRemoved(String newRemoved) {
		String oldRemoved = removed;
		removed = newRemoved;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.IGNORE_PATTERN__REMOVED, oldRemoved, removed));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getIgnoreFile() {
		return ignoreFile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIgnoreFile(String newIgnoreFile) {
		String oldIgnoreFile = ignoreFile;
		ignoreFile = newIgnoreFile;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.IGNORE_PATTERN__IGNORE_FILE, oldIgnoreFile, ignoreFile));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getIgnoreFileTimestamp() {
		return ignoreFileTimestamp;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIgnoreFileTimestamp(String newIgnoreFileTimestamp) {
		String oldIgnoreFileTimestamp = ignoreFileTimestamp;
		ignoreFileTimestamp = newIgnoreFileTimestamp;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.IGNORE_PATTERN__IGNORE_FILE_TIMESTAMP, oldIgnoreFileTimestamp, ignoreFileTimestamp));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case WindupPackage.IGNORE_PATTERN__PATTERN:
				return getPattern();
			case WindupPackage.IGNORE_PATTERN__ENABLED:
				return isEnabled();
			case WindupPackage.IGNORE_PATTERN__READ_FROM_FILE:
				return isReadFromFile();
			case WindupPackage.IGNORE_PATTERN__REMOVED:
				return getRemoved();
			case WindupPackage.IGNORE_PATTERN__IGNORE_FILE:
				return getIgnoreFile();
			case WindupPackage.IGNORE_PATTERN__IGNORE_FILE_TIMESTAMP:
				return getIgnoreFileTimestamp();
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
			case WindupPackage.IGNORE_PATTERN__PATTERN:
				setPattern((String)newValue);
				return;
			case WindupPackage.IGNORE_PATTERN__ENABLED:
				setEnabled((Boolean)newValue);
				return;
			case WindupPackage.IGNORE_PATTERN__READ_FROM_FILE:
				setReadFromFile((Boolean)newValue);
				return;
			case WindupPackage.IGNORE_PATTERN__REMOVED:
				setRemoved((String)newValue);
				return;
			case WindupPackage.IGNORE_PATTERN__IGNORE_FILE:
				setIgnoreFile((String)newValue);
				return;
			case WindupPackage.IGNORE_PATTERN__IGNORE_FILE_TIMESTAMP:
				setIgnoreFileTimestamp((String)newValue);
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
			case WindupPackage.IGNORE_PATTERN__PATTERN:
				setPattern(PATTERN_EDEFAULT);
				return;
			case WindupPackage.IGNORE_PATTERN__ENABLED:
				setEnabled(ENABLED_EDEFAULT);
				return;
			case WindupPackage.IGNORE_PATTERN__READ_FROM_FILE:
				setReadFromFile(READ_FROM_FILE_EDEFAULT);
				return;
			case WindupPackage.IGNORE_PATTERN__REMOVED:
				setRemoved(REMOVED_EDEFAULT);
				return;
			case WindupPackage.IGNORE_PATTERN__IGNORE_FILE:
				setIgnoreFile(IGNORE_FILE_EDEFAULT);
				return;
			case WindupPackage.IGNORE_PATTERN__IGNORE_FILE_TIMESTAMP:
				setIgnoreFileTimestamp(IGNORE_FILE_TIMESTAMP_EDEFAULT);
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
			case WindupPackage.IGNORE_PATTERN__PATTERN:
				return PATTERN_EDEFAULT == null ? pattern != null : !PATTERN_EDEFAULT.equals(pattern);
			case WindupPackage.IGNORE_PATTERN__ENABLED:
				return enabled != ENABLED_EDEFAULT;
			case WindupPackage.IGNORE_PATTERN__READ_FROM_FILE:
				return readFromFile != READ_FROM_FILE_EDEFAULT;
			case WindupPackage.IGNORE_PATTERN__REMOVED:
				return REMOVED_EDEFAULT == null ? removed != null : !REMOVED_EDEFAULT.equals(removed);
			case WindupPackage.IGNORE_PATTERN__IGNORE_FILE:
				return IGNORE_FILE_EDEFAULT == null ? ignoreFile != null : !IGNORE_FILE_EDEFAULT.equals(ignoreFile);
			case WindupPackage.IGNORE_PATTERN__IGNORE_FILE_TIMESTAMP:
				return IGNORE_FILE_TIMESTAMP_EDEFAULT == null ? ignoreFileTimestamp != null : !IGNORE_FILE_TIMESTAMP_EDEFAULT.equals(ignoreFileTimestamp);
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
		result.append(" (pattern: ");
		result.append(pattern);
		result.append(", enabled: ");
		result.append(enabled);
		result.append(", readFromFile: ");
		result.append(readFromFile);
		result.append(", removed: ");
		result.append(removed);
		result.append(", ignoreFile: ");
		result.append(ignoreFile);
		result.append(", ignoreFileTimestamp: ");
		result.append(ignoreFileTimestamp);
		result.append(')');
		return result.toString();
	}

} //IgnorePatternImpl
