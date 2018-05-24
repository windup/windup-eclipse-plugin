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
import org.jboss.tools.windup.windup.IgnorePattern;
import org.jboss.tools.windup.windup.Input;
import org.jboss.tools.windup.windup.MigrationPath;
import org.jboss.tools.windup.windup.Pair;
import org.jboss.tools.windup.windup.Report;
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
 *   <li>{@link org.jboss.tools.windup.windup.impl.ConfigurationElementImpl#getWindupHome <em>Windup Home</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.ConfigurationElementImpl#isSourceMode <em>Source Mode</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.ConfigurationElementImpl#getOutputLocation <em>Output Location</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.ConfigurationElementImpl#getPackages <em>Packages</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.ConfigurationElementImpl#getTimestamp <em>Timestamp</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.ConfigurationElementImpl#getMigrationPath <em>Migration Path</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.ConfigurationElementImpl#getUserRulesDirectories <em>User Rules Directories</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.ConfigurationElementImpl#getOptions <em>Options</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.ConfigurationElementImpl#getJreHome <em>Jre Home</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.ConfigurationElementImpl#isGenerateReport <em>Generate Report</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.ConfigurationElementImpl#getWindupResult <em>Windup Result</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.ConfigurationElementImpl#getReports <em>Reports</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.ConfigurationElementImpl#getIgnorePatterns <em>Ignore Patterns</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.ConfigurationElementImpl#getIgnoreFile <em>Ignore File</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.impl.ConfigurationElementImpl#getIgnoreFileTimestamp <em>Ignore File Timestamp</em>}</li>
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
	 * The default value of the '{@link #getOutputLocation() <em>Output Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOutputLocation()
	 * @generated
	 * @ordered
	 */
	protected static final String OUTPUT_LOCATION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOutputLocation() <em>Output Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOutputLocation()
	 * @generated
	 * @ordered
	 */
	protected String outputLocation = OUTPUT_LOCATION_EDEFAULT;

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
	 * The default value of the '{@link #getJreHome() <em>Jre Home</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getJreHome()
	 * @generated
	 * @ordered
	 */
	protected static final String JRE_HOME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getJreHome() <em>Jre Home</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getJreHome()
	 * @generated
	 * @ordered
	 */
	protected String jreHome = JRE_HOME_EDEFAULT;

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
	 * The cached value of the '{@link #getWindupResult() <em>Windup Result</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindupResult()
	 * @generated
	 * @ordered
	 */
	protected WindupResult windupResult;

	/**
	 * The cached value of the '{@link #getReports() <em>Reports</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReports()
	 * @generated
	 * @ordered
	 */
	protected EList<Report> reports;

	/**
	 * The cached value of the '{@link #getIgnorePatterns() <em>Ignore Patterns</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIgnorePatterns()
	 * @generated
	 * @ordered
	 */
	protected EList<IgnorePattern> ignorePatterns;

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
	public String getOutputLocation() {
		return outputLocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOutputLocation(String newOutputLocation) {
		String oldOutputLocation = outputLocation;
		outputLocation = newOutputLocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.CONFIGURATION_ELEMENT__OUTPUT_LOCATION, oldOutputLocation, outputLocation));
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
	public String getJreHome() {
		return jreHome;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setJreHome(String newJreHome) {
		String oldJreHome = jreHome;
		jreHome = newJreHome;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.CONFIGURATION_ELEMENT__JRE_HOME, oldJreHome, jreHome));
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
	public EList<Report> getReports() {
		if (reports == null) {
			reports = new EObjectContainmentEList.Resolving<Report>(Report.class, this, WindupPackage.CONFIGURATION_ELEMENT__REPORTS);
		}
		return reports;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<IgnorePattern> getIgnorePatterns() {
		if (ignorePatterns == null) {
			ignorePatterns = new EObjectContainmentEList.Resolving<IgnorePattern>(IgnorePattern.class, this, WindupPackage.CONFIGURATION_ELEMENT__IGNORE_PATTERNS);
		}
		return ignorePatterns;
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
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.CONFIGURATION_ELEMENT__IGNORE_FILE, oldIgnoreFile, ignoreFile));
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
			eNotify(new ENotificationImpl(this, Notification.SET, WindupPackage.CONFIGURATION_ELEMENT__IGNORE_FILE_TIMESTAMP, oldIgnoreFileTimestamp, ignoreFileTimestamp));
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
			case WindupPackage.CONFIGURATION_ELEMENT__WINDUP_RESULT:
				return basicSetWindupResult(null, msgs);
			case WindupPackage.CONFIGURATION_ELEMENT__REPORTS:
				return ((InternalEList<?>)getReports()).basicRemove(otherEnd, msgs);
			case WindupPackage.CONFIGURATION_ELEMENT__IGNORE_PATTERNS:
				return ((InternalEList<?>)getIgnorePatterns()).basicRemove(otherEnd, msgs);
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
			case WindupPackage.CONFIGURATION_ELEMENT__OUTPUT_LOCATION:
				return getOutputLocation();
			case WindupPackage.CONFIGURATION_ELEMENT__PACKAGES:
				return getPackages();
			case WindupPackage.CONFIGURATION_ELEMENT__TIMESTAMP:
				return getTimestamp();
			case WindupPackage.CONFIGURATION_ELEMENT__MIGRATION_PATH:
				if (resolve) return getMigrationPath();
				return basicGetMigrationPath();
			case WindupPackage.CONFIGURATION_ELEMENT__USER_RULES_DIRECTORIES:
				return getUserRulesDirectories();
			case WindupPackage.CONFIGURATION_ELEMENT__OPTIONS:
				return getOptions();
			case WindupPackage.CONFIGURATION_ELEMENT__JRE_HOME:
				return getJreHome();
			case WindupPackage.CONFIGURATION_ELEMENT__GENERATE_REPORT:
				return isGenerateReport();
			case WindupPackage.CONFIGURATION_ELEMENT__WINDUP_RESULT:
				if (resolve) return getWindupResult();
				return basicGetWindupResult();
			case WindupPackage.CONFIGURATION_ELEMENT__REPORTS:
				return getReports();
			case WindupPackage.CONFIGURATION_ELEMENT__IGNORE_PATTERNS:
				return getIgnorePatterns();
			case WindupPackage.CONFIGURATION_ELEMENT__IGNORE_FILE:
				return getIgnoreFile();
			case WindupPackage.CONFIGURATION_ELEMENT__IGNORE_FILE_TIMESTAMP:
				return getIgnoreFileTimestamp();
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
			case WindupPackage.CONFIGURATION_ELEMENT__OUTPUT_LOCATION:
				setOutputLocation((String)newValue);
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__PACKAGES:
				getPackages().clear();
				getPackages().addAll((Collection<? extends String>)newValue);
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__TIMESTAMP:
				setTimestamp((String)newValue);
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
			case WindupPackage.CONFIGURATION_ELEMENT__JRE_HOME:
				setJreHome((String)newValue);
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__GENERATE_REPORT:
				setGenerateReport((Boolean)newValue);
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__WINDUP_RESULT:
				setWindupResult((WindupResult)newValue);
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__REPORTS:
				getReports().clear();
				getReports().addAll((Collection<? extends Report>)newValue);
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__IGNORE_PATTERNS:
				getIgnorePatterns().clear();
				getIgnorePatterns().addAll((Collection<? extends IgnorePattern>)newValue);
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__IGNORE_FILE:
				setIgnoreFile((String)newValue);
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__IGNORE_FILE_TIMESTAMP:
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
			case WindupPackage.CONFIGURATION_ELEMENT__INPUTS:
				getInputs().clear();
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__WINDUP_HOME:
				setWindupHome(WINDUP_HOME_EDEFAULT);
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__SOURCE_MODE:
				setSourceMode(SOURCE_MODE_EDEFAULT);
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__OUTPUT_LOCATION:
				setOutputLocation(OUTPUT_LOCATION_EDEFAULT);
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__PACKAGES:
				getPackages().clear();
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__TIMESTAMP:
				setTimestamp(TIMESTAMP_EDEFAULT);
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
			case WindupPackage.CONFIGURATION_ELEMENT__JRE_HOME:
				setJreHome(JRE_HOME_EDEFAULT);
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__GENERATE_REPORT:
				setGenerateReport(GENERATE_REPORT_EDEFAULT);
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__WINDUP_RESULT:
				setWindupResult((WindupResult)null);
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__REPORTS:
				getReports().clear();
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__IGNORE_PATTERNS:
				getIgnorePatterns().clear();
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__IGNORE_FILE:
				setIgnoreFile(IGNORE_FILE_EDEFAULT);
				return;
			case WindupPackage.CONFIGURATION_ELEMENT__IGNORE_FILE_TIMESTAMP:
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
			case WindupPackage.CONFIGURATION_ELEMENT__INPUTS:
				return inputs != null && !inputs.isEmpty();
			case WindupPackage.CONFIGURATION_ELEMENT__WINDUP_HOME:
				return WINDUP_HOME_EDEFAULT == null ? windupHome != null : !WINDUP_HOME_EDEFAULT.equals(windupHome);
			case WindupPackage.CONFIGURATION_ELEMENT__SOURCE_MODE:
				return sourceMode != SOURCE_MODE_EDEFAULT;
			case WindupPackage.CONFIGURATION_ELEMENT__OUTPUT_LOCATION:
				return OUTPUT_LOCATION_EDEFAULT == null ? outputLocation != null : !OUTPUT_LOCATION_EDEFAULT.equals(outputLocation);
			case WindupPackage.CONFIGURATION_ELEMENT__PACKAGES:
				return packages != null && !packages.isEmpty();
			case WindupPackage.CONFIGURATION_ELEMENT__TIMESTAMP:
				return TIMESTAMP_EDEFAULT == null ? timestamp != null : !TIMESTAMP_EDEFAULT.equals(timestamp);
			case WindupPackage.CONFIGURATION_ELEMENT__MIGRATION_PATH:
				return migrationPath != null;
			case WindupPackage.CONFIGURATION_ELEMENT__USER_RULES_DIRECTORIES:
				return userRulesDirectories != null && !userRulesDirectories.isEmpty();
			case WindupPackage.CONFIGURATION_ELEMENT__OPTIONS:
				return options != null && !options.isEmpty();
			case WindupPackage.CONFIGURATION_ELEMENT__JRE_HOME:
				return JRE_HOME_EDEFAULT == null ? jreHome != null : !JRE_HOME_EDEFAULT.equals(jreHome);
			case WindupPackage.CONFIGURATION_ELEMENT__GENERATE_REPORT:
				return generateReport != GENERATE_REPORT_EDEFAULT;
			case WindupPackage.CONFIGURATION_ELEMENT__WINDUP_RESULT:
				return windupResult != null;
			case WindupPackage.CONFIGURATION_ELEMENT__REPORTS:
				return reports != null && !reports.isEmpty();
			case WindupPackage.CONFIGURATION_ELEMENT__IGNORE_PATTERNS:
				return ignorePatterns != null && !ignorePatterns.isEmpty();
			case WindupPackage.CONFIGURATION_ELEMENT__IGNORE_FILE:
				return IGNORE_FILE_EDEFAULT == null ? ignoreFile != null : !IGNORE_FILE_EDEFAULT.equals(ignoreFile);
			case WindupPackage.CONFIGURATION_ELEMENT__IGNORE_FILE_TIMESTAMP:
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
		result.append(" (windupHome: ");
		result.append(windupHome);
		result.append(", sourceMode: ");
		result.append(sourceMode);
		result.append(", outputLocation: ");
		result.append(outputLocation);
		result.append(", packages: ");
		result.append(packages);
		result.append(", timestamp: ");
		result.append(timestamp);
		result.append(", userRulesDirectories: ");
		result.append(userRulesDirectories);
		result.append(", jreHome: ");
		result.append(jreHome);
		result.append(", generateReport: ");
		result.append(generateReport);
		result.append(", ignoreFile: ");
		result.append(ignoreFile);
		result.append(", ignoreFileTimestamp: ");
		result.append(ignoreFileTimestamp);
		result.append(')');
		return result.toString();
	}

} //ConfigurationElementImpl
