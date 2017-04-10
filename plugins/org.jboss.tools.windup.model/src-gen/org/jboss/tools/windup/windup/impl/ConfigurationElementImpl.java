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

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.jboss.tools.windup.windup.ConfigurationElement;
import org.jboss.tools.windup.windup.Input;
import org.jboss.tools.windup.windup.MigrationPath;
import org.jboss.tools.windup.windup.Pair;
import org.jboss.tools.windup.windup.WindupPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Configuration Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.jboss.tools.windup.windup.impl.ConfigurationElementImpl#getInputs <em>Inputs</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.ConfigurationElementImpl#getWindupHome <em>Windup Home</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.ConfigurationElementImpl#isSourceMode <em>Source Mode</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.ConfigurationElementImpl#getGeneratedReportsLocation <em>Generated Reports Location</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.ConfigurationElementImpl#getPackages <em>Packages</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.ConfigurationElementImpl#getTimestamp <em>Timestamp</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.ConfigurationElementImpl#isGenerateReport <em>Generate Report</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.ConfigurationElementImpl#getMigrationPath <em>Migration Path</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.ConfigurationElementImpl#getUserRulesDirectories <em>User Rules Directories</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.ConfigurationElementImpl#getOptions <em>Options</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.ConfigurationElementImpl#getReportDirectory <em>Report Directory</em>}</li>
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
	 * The default value of the '{@link #getGeneratedReportsLocation() <em>Generated Reports Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGeneratedReportsLocation()
	 * @generated
	 * @ordered
	 */
	protected static final String GENERATED_REPORTS_LOCATION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getGeneratedReportsLocation() <em>Generated Reports Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGeneratedReportsLocation()
	 * @generated
	 * @ordered
	 */
	protected String generatedReportsLocation = GENERATED_REPORTS_LOCATION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getPackages() <em>Packages</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPackages()
	 * @generated
	 * @ordered
	 */
	protected EList<String> packages;

	/**
	 * The default value of the '{@link #getTimestamp() <em>Timestamp</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTimestamp()
	 * @generated
	 * @ordered
	 */
	protected static final String TIMESTAMP_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTimestamp() <em>Timestamp</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTimestamp()
	 * @generated
	 * @ordered
	 */
	protected String timestamp = TIMESTAMP_EDEFAULT;

	/**
	 * The default value of the '{@link #isGenerateReport() <em>Generate Report</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isGenerateReport()
	 * @generated
	 * @ordered
	 */
	protected static final boolean GENERATE_REPORT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isGenerateReport() <em>Generate Report</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isGenerateReport()
	 * @generated
	 * @ordered
	 */
	protected boolean generateReport = GENERATE_REPORT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getMigrationPath() <em>Migration Path</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMigrationPath()
	 * @generated
	 * @ordered
	 */
	protected MigrationPath migrationPath;

	/**
	 * The cached value of the '{@link #getUserRulesDirectories() <em>User Rules Directories</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUserRulesDirectories()
	 * @generated
	 * @ordered
	 */
	protected EList<String> userRulesDirectories;

	/**
	 * The cached value of the '{@link #getOptions() <em>Options</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOptions()
	 * @generated
	 * @ordered
	 */
	protected EList<Pair> options;

	/**
	 * The default value of the '{@link #getReportDirectory() <em>Report Directory</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReportDirectory()
	 * @generated
	 * @ordered
	 */
	protected static final String REPORT_DIRECTORY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getReportDirectory() <em>Report Directory</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReportDirectory()
	 * @generated
	 * @ordered
	 */
	protected String reportDirectory = REPORT_DIRECTORY_EDEFAULT;

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
	public String getGeneratedReportsLocation() {
		return generatedReportsLocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setGeneratedReportsLocation(String newGeneratedReportsLocation) {
		String oldGeneratedReportsLocation = generatedReportsLocation;
		generatedReportsLocation = newGeneratedReportsLocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.CONFIGURATION_ELEMENT__GENERATED_REPORTS_LOCATION, oldGeneratedReportsLocation, generatedReportsLocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getPackages() {
		if (packages == null) {
			packages = new EDataTypeUniqueEList<String>(String.class, this, WindupPackage.CONFIGURATION_ELEMENT__PACKAGES);
		}
		return packages;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getTimestamp() {
		return timestamp;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTimestamp(String newTimestamp) {
		String oldTimestamp = timestamp;
		timestamp = newTimestamp;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.CONFIGURATION_ELEMENT__TIMESTAMP, oldTimestamp, timestamp));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isGenerateReport() {
		return generateReport;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setGenerateReport(boolean newGenerateReport) {
		boolean oldGenerateReport = generateReport;
		generateReport = newGenerateReport;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.CONFIGURATION_ELEMENT__GENERATE_REPORT, oldGenerateReport, generateReport));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MigrationPath getMigrationPath() {
		if (migrationPath != null && migrationPath.eIsProxy()) {
			InternalEObject oldMigrationPath = (InternalEObject)migrationPath;
			migrationPath = (MigrationPath)eResolveProxy(oldMigrationPath);
			if (migrationPath != oldMigrationPath) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, WindupPackage.CONFIGURATION_ELEMENT__MIGRATION_PATH, oldMigrationPath, migrationPath));
			}
		}
		return migrationPath;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MigrationPath basicGetMigrationPath() {
		return migrationPath;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMigrationPath(MigrationPath newMigrationPath) {
		MigrationPath oldMigrationPath = migrationPath;
		migrationPath = newMigrationPath;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.CONFIGURATION_ELEMENT__MIGRATION_PATH, oldMigrationPath, migrationPath));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getUserRulesDirectories() {
		if (userRulesDirectories == null) {
			userRulesDirectories = new EDataTypeUniqueEList<String>(String.class, this, WindupPackage.CONFIGURATION_ELEMENT__USER_RULES_DIRECTORIES);
		}
		return userRulesDirectories;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Pair> getOptions() {
		if (options == null) {
			options = new EObjectContainmentEList.Resolving<Pair>(Pair.class, this, WindupPackage.CONFIGURATION_ELEMENT__OPTIONS);
		}
		return options;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getReportDirectory() {
		return reportDirectory;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setReportDirectory(String newReportDirectory) {
		String oldReportDirectory = reportDirectory;
		reportDirectory = newReportDirectory;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.CONFIGURATION_ELEMENT__REPORT_DIRECTORY, oldReportDirectory, reportDirectory));
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
			case WindupPackage.CONFIGURATION_ELEMENT__OPTIONS:
				return ((InternalEList<?>)getOptions()).basicRemove(otherEnd, msgs);
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
			case WindupPackage.CONFIGURATION_ELEMENT__WINDUP_HOME:
				return getWindupHome();
			case WindupPackage.CONFIGURATION_ELEMENT__SOURCE_MODE:
				return isSourceMode();
			case WindupPackage.CONFIGURATION_ELEMENT__GENERATED_REPORTS_LOCATION:
				return getGeneratedReportsLocation();
			case WindupPackage.CONFIGURATION_ELEMENT__PACKAGES:
				return getPackages();
			case WindupPackage.CONFIGURATION_ELEMENT__TIMESTAMP:
				return getTimestamp();
			case WindupPackage.CONFIGURATION_ELEMENT__GENERATE_REPORT:
				return isGenerateReport();
			case WindupPackage.CONFIGURATION_ELEMENT__MIGRATION_PATH:
				if (resolve) return getMigrationPath();
				return basicGetMigrationPath();
			case WindupPackage.CONFIGURATION_ELEMENT__USER_RULES_DIRECTORIES:
				return getUserRulesDirectories();
			case WindupPackage.CONFIGURATION_ELEMENT__OPTIONS:
				return getOptions();
			case WindupPackage.CONFIGURATION_ELEMENT__REPORT_DIRECTORY:
				return getReportDirectory();
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
			case WindupPackage.CONFIGURATION_ELEMENT__WINDUP_HOME:
				setWindupHome((String)newValue);
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__SOURCE_MODE:
				setSourceMode((Boolean)newValue);
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__GENERATED_REPORTS_LOCATION:
				setGeneratedReportsLocation((String)newValue);
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__PACKAGES:
				getPackages().clear();
				getPackages().addAll((Collection<? extends String>)newValue);
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__TIMESTAMP:
				setTimestamp((String)newValue);
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__GENERATE_REPORT:
				setGenerateReport((Boolean)newValue);
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__MIGRATION_PATH:
				setMigrationPath((MigrationPath)newValue);
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__USER_RULES_DIRECTORIES:
				getUserRulesDirectories().clear();
				getUserRulesDirectories().addAll((Collection<? extends String>)newValue);
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__OPTIONS:
				getOptions().clear();
				getOptions().addAll((Collection<? extends Pair>)newValue);
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__REPORT_DIRECTORY:
				setReportDirectory((String)newValue);
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
			case WindupPackage.CONFIGURATION_ELEMENT__WINDUP_HOME:
				setWindupHome(WINDUP_HOME_EDEFAULT);
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__SOURCE_MODE:
				setSourceMode(SOURCE_MODE_EDEFAULT);
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__GENERATED_REPORTS_LOCATION:
				setGeneratedReportsLocation(GENERATED_REPORTS_LOCATION_EDEFAULT);
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__PACKAGES:
				getPackages().clear();
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__TIMESTAMP:
				setTimestamp(TIMESTAMP_EDEFAULT);
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__GENERATE_REPORT:
				setGenerateReport(GENERATE_REPORT_EDEFAULT);
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__MIGRATION_PATH:
				setMigrationPath((MigrationPath)null);
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__USER_RULES_DIRECTORIES:
				getUserRulesDirectories().clear();
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__OPTIONS:
				getOptions().clear();
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__REPORT_DIRECTORY:
				setReportDirectory(REPORT_DIRECTORY_EDEFAULT);
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
			case WindupPackage.CONFIGURATION_ELEMENT__WINDUP_HOME:
				return WINDUP_HOME_EDEFAULT == null ? windupHome != null : !WINDUP_HOME_EDEFAULT.equals(windupHome);
			case WindupPackage.CONFIGURATION_ELEMENT__SOURCE_MODE:
				return sourceMode != SOURCE_MODE_EDEFAULT;
			case WindupPackage.CONFIGURATION_ELEMENT__GENERATED_REPORTS_LOCATION:
				return GENERATED_REPORTS_LOCATION_EDEFAULT == null ? generatedReportsLocation != null : !GENERATED_REPORTS_LOCATION_EDEFAULT.equals(generatedReportsLocation);
			case WindupPackage.CONFIGURATION_ELEMENT__PACKAGES:
				return packages != null && !packages.isEmpty();
			case WindupPackage.CONFIGURATION_ELEMENT__TIMESTAMP:
				return TIMESTAMP_EDEFAULT == null ? timestamp != null : !TIMESTAMP_EDEFAULT.equals(timestamp);
			case WindupPackage.CONFIGURATION_ELEMENT__GENERATE_REPORT:
				return generateReport != GENERATE_REPORT_EDEFAULT;
			case WindupPackage.CONFIGURATION_ELEMENT__MIGRATION_PATH:
				return migrationPath != null;
			case WindupPackage.CONFIGURATION_ELEMENT__USER_RULES_DIRECTORIES:
				return userRulesDirectories != null && !userRulesDirectories.isEmpty();
			case WindupPackage.CONFIGURATION_ELEMENT__OPTIONS:
				return options != null && !options.isEmpty();
			case WindupPackage.CONFIGURATION_ELEMENT__REPORT_DIRECTORY:
				return REPORT_DIRECTORY_EDEFAULT == null ? reportDirectory != null : !REPORT_DIRECTORY_EDEFAULT.equals(reportDirectory);
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
		result.append(", sourceMode: ");
		result.append(sourceMode);
		result.append(", generatedReportsLocation: ");
		result.append(generatedReportsLocation);
		result.append(", packages: ");
		result.append(packages);
		result.append(", timestamp: ");
		result.append(timestamp);
		result.append(", generateReport: ");
		result.append(generateReport);
		result.append(", userRulesDirectories: ");
		result.append(userRulesDirectories);
		result.append(", reportDirectory: ");
		result.append(reportDirectory);
		result.append(')');
		return result.toString();
	}

} //ConfigurationElementImpl
