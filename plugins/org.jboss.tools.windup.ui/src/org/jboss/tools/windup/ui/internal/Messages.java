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

    public static String refresh;

    public static String report_has_no_information_on_resource;

    public static String windup_report_has_not_been_generated;

    public static String select_projects_to_generate_windup_reports_for;

    public static String Question;

    public static String Options;
    public static String windupOption;
    public static String KEY;
    public static String VALUE;

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
    public static String launchTabTitle;
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
    public static String ComparePreviewer_quickFixType;
    public static String ComparePreviewer_quickFixText;
    public static String ComparePreviewer_quickFixNoText;
    
    // Editor
    public static String windupEditorTitle;
    
    
    
    static
    {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages()
    {
    }
}