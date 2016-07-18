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

    public static String refresh;

    public static String report_has_no_information_on_resource;

    public static String windup_report_has_not_been_generated;

    public static String select_projects_to_generate_windup_reports_for;

    public static String Question;

    public static String Options;

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
    
    // Launch
    public static String inputTabName;
    public static String applicationsToMigrate;
    public static String launchMode;
    public static String launchTab;
    public static String launchTabTitle;
    public static String launchName;
    
    public static String windupInput;
    public static String inputDescription;
    
    public static String generate_windup_report_for;
    
    public static String selectLaunchConfiguration;
    public static String selectExistinConfiguration;
    public static String errorConfiguringWindup;
    
    // General Info
    public static String generalInfoTitle;
    public static String generalInfoDescription;
    
    public static String windupReport;
    public static String windupHomeLocation;
    
    public static String sourceMode;
    
    public static String javaRuntimeEnvironment;
    public static String searchJRE;
    
    // Windup Options
    public static String windupOptions;
    public static String optionsDescription;
    
    public static String generatedReportLocation;
    public static String browseLabel;
    public static String generatedReportLocationSearch;
    
    // Issues
    public static String issuesTab;
    public static String issuesTabTitle;
    
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