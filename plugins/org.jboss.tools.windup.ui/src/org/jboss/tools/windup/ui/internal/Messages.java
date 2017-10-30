/*******************************************************************************
 * Copyright (c) 2013, 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.jboss.tools.windup.ui.internal.messages"; //$NON-NLS-1$

    public static String link_with_editor_and_selection;

    public static String generate_windup_report;
    public static String generateIssues;
    public static String windupGenerateReport;
    public static String noWindupReport;
    
    public static String WindupStartingTitle;
	public static String WindupStartingDetail;
	public static String WindupStartingError;
	public static String WindupServerError;
	
	public static String WindupNotStartedMessage;
	public static String WindupNotStartedDebugInfo;
	public static String WindupStartNotStartingSolution1;
	public static String WindupStartNotStartingSolution2;
	public static String WindupStartNotStartingSolution3;
	public static String WindupStartNotStartingSolution4;
	public static String WindupStartNotStartingSolution5;
	
	public static String WindupShuttingDownError;
	
	public static String WindupShutdowCheck;
	public static String WindupShuttingDown;
	public static String WindupRunStartScipt;
	
	public static String WindupServerLabel;
	
	public static String WindupNotExecutableTitle;
	public static String WindupNotExecutableInfo;
	
	public static String WindupPreferenceHome;
	public static String WindupPreferenceRmiPort;
	public static String WindupPreferenceJRE;
	
	public static String WindupPreferenceStartTimeoutDuration;
	public static String WindupPreferenceStopTimeoutDuration;
	
	public static String selectExistingRepositories;
	public static String selectRepositories;

    public static String refresh;
    
    // Rules
    public static String refreshingRules;
    public static String refreshingRulesError;
    public static String systemRulesets;
    public static String customRulesets;
    public static String removeRuleset;
    
    public static String newXMLRule_title;
    public static String newXmlRule;
    public static String _UI_WIZARD_NEW_XML_RULE_HEADING;
    public static String _UI_WIZARD_NEW_XML_RULE_EXPL;
    public static String _UI_WIZARD_NEW_XML_RULE_FILTER;
    
    // Rule wizards
    public static String NewXMLRuleset_title;
    public static String ImportRuleset_title;
    public static String NewJavaRuleset_title;
    public static String _ERROR_BAD_FILENAME_EXTENSION;
    public static String _UI_WIZARD_CREATE_XML_RULESET_FILE_HEADING;
    public static String _UI_WIZARD_IMPORT_XML_RULESET_FILE_HEADING;
    public static String _UI_WIZARD_CREATE_JAVA_RULESET_FILE_HEADING;
    public static String _UI_WIZARD_CREATE_XML_FILE_EXPL;
    public static String _UI_WIZARD_IMPORT_XML_FILE_EXPL;
    public static String _UI_WIZARD_CREATE_JAVA_FILE_EXPL;
    public static String _ERROR_FILE_ALREADY_EXISTS;
    
    public static String _UI_WIZARD_IMPORT_XML_RULESET_FILE;
    
    public static String NewWizard_title;
    public static String ImportRuleset_browse;
    public static String NewRulesetWizard_generatingRuleset;
    public static String NewRulesetWizard_generateTemplate;
    public static String NewRulesetWizard_rulesetId;
    public static String NewRulesetWizard_rulesetIdRequired;
    
    // Rules Editor
    public static String rulesEditor_dependencies;
    public static String rulesEditor_tabTitle;
    public static String rulesSectionTitle;
    public static String rulesOverview;
    public static String documentationTitle;
    public static String examplesTitle;
    
    public static String collapseAll;
    public static String expandAll;
    public static String deleteElement;
    public static String newElement;
    
    public static String ElementAttributeRow_AttrLabelReq;
    public static String ElementAttributeRow_AttrLabel;
    
    public static String RulesetEditor_AddRule;
    public static String RulesetEditor_remove;
    
    public static String RulesetEditor_Rules_up;
    public static String RulesetEditor_Rules_down;
    
    public static String RulesetEditor_RemoveElement;
    
    public static String RulesetEditor_ReferenceAttributeRow_browse;
    
    public static String RulesetEditor_tagsSection;
    public static String RulesetEditor_tagsSectionDescription;
    public static String RulesetEditor_tagsSectionName;
    
    public static String RulesetEditor_tagsSectionAddDialogTitle;
    public static String RulesetEditor_tagsSectionAddDialog;
    
    public static String RulesetEditor_previewSection;
    
    public static String RulesetEditor_messageSection;
    public static String RulesetEditor_messageSectionDescription;
    public static String RulesetEditor_messageContentAssist;
    
    public static String RulesetEditor_descriptionSection;
    public static String RulesetEditor_descriptionPreviewSection;
        
    public static String openRuleset;
    public static String errorOpeningRuleset;
    public static String rulesMenuNew;
    
    public static String ruleElementDetails;
    public static String ruleElementMainTab;

    public static String tagsTab;
    public static String messageTab;
    
    public static String taskRule;

    public static String report_has_no_information_on_resource;

    public static String windup_report_has_not_been_generated;

    public static String select_projects_to_generate_windup_reports_for;

    public static String Question;

    public static String Options;
    public static String windupOption;
    public static String OPTION;
    public static String VALUE;
    public static String Rules;
    
    public static String showInIssueExplorer;
    public static String showIssueDetails;
    
    public static String createMigrationIssue;
    public static String createRuleFromSelection;

    public static String createRuleFromXPath;
    
    public static String showWindupGettingStarted;

    public static String PreviewQuickFix;
    public static String ApplyQuickFix;
    public static String MarkAsFixed;
    
    public static String windupGettingStartedName;
    
    public static String WindupReportExport_page_one_title;
    public static String WindupReportExport_page_one_description;
    public static String WindupReportExport_exportReportsTitle;
    public static String WindupReportExport_select_all;
    public static String WindupReportExport_deselect_all;
    public static String WindupReportExport_compressContents;
    public static String WindupReportExport_saveInZipFormat;
    public static String WindupReportExport_saveInTarFormat;
    public static String WindupReportExport_selectDestintationDialogTitle;
    public static String WindupReportExport_destinationLabel;
    public static String WindupReportExport_rootDirNameLabel;
    public static String WindupReportExport_browse;
    public static String WindupReportExport_mustBeFileError;
    public static String WindupReportExport_errorDialogTitle;
    public static String WindupReportExport_noneSelected;
    public static String WindupReportExport_directoryCreationError;
    public static String WindupReportExport_archiveAlreadyExistsError;
    public static String WindupReportExport_directoryExists;
    public static String WindupReportExport_destinationEmptyError;
    public static String WindupReportExport_question_shouldOverwriteExisting;
    public static String WindupReportExport_question_createTargetDirectory;
    public static String WindupReportExport_question_fileExists;
    public static String WindupReportExport_question_overwriteNameAndPath;
    public static String WindupReportExport_operationExceptionMessage;
    public static String WindupReportExport_errorGeneratingReports;
    public static String WindupReportExport_reGenerateReportBeforeExport;
    
    public static String markerError_findingMarkers;
    public static String markerError_findingJavaId;
    
    // Launch
    public static String inputTabName;
    public static String applicationsToMigrate;
    public static String launchMode;
    public static String launchTab;
    public static String launchName;
    
    public static String inputProjects;
    public static String inputProjectsDescription;
    public static String inputPackages;
    public static String inputPackagesDescription;
    
    public static String generate_windup_report_for;
    public static String generatedReport;
    
    public static String selectLaunchConfiguration;
    public static String selectExistinConfiguration;
    public static String errorConfiguringWindup;
    
    // General Info
    public static String generalInfoTitle;
    public static String generalInfoDescription;
    
    public static String windupReport;
    public static String windupHomeLocation;
    
    public static String windupMigrationPath;
    
    // Rules
    public static String windupCustomRules;
    
    public static String sourceMode;
    
    public static String javaRuntimeEnvironment;
    public static String searchJRE;
    
    // Windup Options
    public static String windupOptions;
    public static String optionsDescription;
    
    public static String windupProjects;
    public static String windupPackages;
    public static String windupAdd;
    public static String windupRemove;
    public static String windupProjectsSelect;
    public static String windupPackagesSelect;
    
    public static String newRuleFromSelectionWizard_ruleset;
    public static String newRuleFromSelectionWizard_newRuleset;
    public static String newRuleFromSelectionWizard_heading;
    public static String newRuleFromSelectionWizard_message;
    
    public static String generatedReportLocation;
    public static String browseLabel;
    public static String generatedReportLocationSearch;
    
    // Issues
    public static String issuesTab;
    public static String issuesTabTitle;
    
    public static String issueLabelTitle;
    public static String issueLabelHint;
    public static String issueLabelSeverity;
    public static String issueLabelEffort;
    public static String issueLabelRuleId;
    public static String issueLabelInfo;
    public static String issueLabelSource;
    
    public static String ComparePreviewer_original_source;
    public static String ComparePreviewer_migrated_source;
    public static String ComparePreviewer_donePreviewFix;
    public static String ComparePreviewer_applyFix;
    
    public static String ComparePreviewer_errorTitle;
    public static String ComparePreviewer_errorMessage;
    
    public static String launchErrorTitle;
    public static String launchErrorMessage;
    
    public static String markersCreateError;
    public static String applyQuickFixError;
    
    // Issue Details
    public static String noIssueDetails;
    
    public static String severityError;
    public static String severityWarning;

    public static String operationError;
    public static String issueDeleteError;
    
    // Compare
    public static String resourceCompareTwoWayTooltip;
    public static String ComparePreviewer_quickFixFile;
    public static String ComparePreviewer_quickFixText;
    public static String ComparePreviewer_quickFixNoText;
    
    public static String InvalidJRELocation;
    public static String JRENotAbsolute;
    
    static
    {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages()
    {
    }
}