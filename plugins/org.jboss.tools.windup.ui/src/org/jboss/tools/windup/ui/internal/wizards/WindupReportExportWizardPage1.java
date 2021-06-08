/*******************************************************************************
 * Copyright (c) 2021 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.wizards;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.jboss.tools.windup.core.services.WindupService;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.Utils;
import org.jboss.tools.windup.ui.internal.archiver.ArchiveFileExportOperation;

/**
 * <p>
 * Page one of the Windup Report Export Wizard.
 * </p>
 * 
 * <p>
 * Implementation based off of {@link org.eclipse.ui.internal.wizards.datatransfer.WizardArchiveFileResourceExportPage1}.
 * </p>
 * 
 * @see org.eclipse.ui.internal.wizards.datatransfer.WizardArchiveFileResourceExportPage1
 */
@SuppressWarnings({ "restriction", "deprecation" })
public class WindupReportExportWizardPage1 extends WizardPage implements Listener, IOverwriteQuery
{

    // dialog store id constants
    private static final String STORE_DESTINATION_NAMES_ID = "WindupReportExportWizardPage1.STORE_DESTINATION_NAMES_ID"; //$NON-NLS-1$
    private static final String STORE_ROOT_DIR_NAMES_ID = "WindupReportExportWizardPage1.STORE_ROOT_DIR_NAMES_ID"; //$NON-NLS-1$
    private final static String STORE_COMPRESS_CONTENTS_ID = "WindupReportExportWizardPage1.STORE_COMPRESS_CONTENTS_ID"; //$NON-NLS-1$
    private final static String STORE_RE_GENERATE_REPORTS_ID = "WindupReportExportWizardPage1.STORE_RE_GENERATE_REPORTS_ID"; //$NON-NLS-1$

    private static final String ZIP_FIRST_FILE_FILTER = "*.zip;*.tar.gz;*.tar;*.tgz"; //$NON-NLS-1$
    private static final String TAR_FIRST_FILE_FILTER = "*.tar;*.zip;*.tar.gz;*.tgz"; //$NON-NLS-1$
    private static final String TARGZ_FIRST_FILE_FILTER = "*.tar.gz;*.tgz;*.tar;*.zip"; //$NON-NLS-1$

    private static final int SIZING_TEXT_FIELD_WIDTH = 250;
    private static final int COMBO_HISTORY_LENGTH = 5;
    private static final int SIZING_SELECTION_WIDGET_HEIGHT = 250;
    private static final int SIZING_SELECTION_WIDGET_WIDTH = 300;

    /**
     * The default root directory name of all exported Windup reports.
     */
    private static final String DEFAULT_ARCHIVE_ROOT_DIRECTORY_NAME = "WindupReports"; //$NON-NLS-1$

    private IStructuredSelection initialResourceSelection;

    // widgets
    private CheckboxTableViewer resourceGroup;
    private Button compressContentsCheckbox;
    private Button reGenerateReportsCheckbox;
    private Button zipFormatButton;
    private Button targzFormatButton;
    private Combo destinationNameField;
    private Combo rootDirNameField;
    private Button destinationBrowseButton;
    
    private WindupService windup;
    
    /**
     * <p>
     * Creates the page using the given default selection.
     * </p>
     * 
     * @param selection Current selection to determine the default projects to export reports for
     */
    public WindupReportExportWizardPage1(WindupService windup, IStructuredSelection selection)
    {
        super("windupReportExportPage1", //$NON-NLS-1$
                    Messages.WindupReportExport_exportReportsTitle,
                    WindupUIPlugin.getImageDescriptor("icons/export_windup_report.png")); //$NON-NLS-1$

        setTitle(Messages.WindupReportExport_page_one_title);
        setDescription(Messages.WindupReportExport_page_one_description);
        this.initialResourceSelection = selection;
        this.windup = windup;
    }

    /**
     * <p>
     * Create all of the UI for this wizard page.
     * </p>
     * 
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent)
    {

        initializeDialogUnits(parent);

        Composite composite = new Composite(parent, SWT.NULL);
        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(
                    GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL));
        composite.setFont(parent.getFont());

        createResourcesGroup(composite);
        createButtonsGroup(composite);

        createDestinationGroup(composite);
        createArchiveRootDirectoryNameGroup(composite);

        createOptionsGroup(composite);

        restoreWidgetValues();

        updateWidgetEnablements();
        setPageComplete(determinePageCompletion());
        setErrorMessage(null); // should not initially have error message

        setControl(composite);
    }

    /**
     * <p>
     * The Finish button was pressed. Try to do the required work now and answer a boolean indicating success. If false is returned then the wizard
     * will not close.
     * </p>
     * 
     * @returns boolean <code>true</code> if successfully finished, <code>false</code> otherwise
     */
    public boolean finish()
    {
        if (!ensureTargetIsValid())
        {
            return false;
        }

        // Save dirty editors if possible but do not stop if not all are saved
        PlatformUI.getWorkbench().saveAllEditors(true);

        // get the list of selected projects
        Object[] checkedObjects = this.resourceGroup.getCheckedElements();
        IProject[] selectedProjects = new IProject[checkedObjects.length];
        System.arraycopy(checkedObjects, 0, selectedProjects, 0, checkedObjects.length);

        /*
         * convert the list of selected projects into a list of Windup reports for those selected projects
         */
        List<IProject> generateReportsForProjects = new ArrayList<IProject>();
        List<File> reportParentDirectories = new ArrayList<File>();
        boolean reGenerateAllReports = this.reGenerateReportsCheckbox.getSelection();
        for (IProject selectedProject : selectedProjects)
        {
        		/*
            IPath reportLocation = windup.getReportParentDirectoryLocation(selectedProject);
            File reportParentDir = reportLocation.toFile();
            reportParentDirectories.add(reportParentDir);
			*/
            /*
             * if regenerating all windup reports or windup report does not exist for project add it to the list of projects to create windup reports
             * for
             */
        		/*
            if (reGenerateAllReports || !windup.reportExists(selectedProject))
            {
                generateReportsForProjects.add(selectedProject);
            }
            */
        }

        // about to invoke the operation so save our state
        saveWidgetValues();

        // execute the export
        return executeExportOperation(reportParentDirectories, generateReportsForProjects);
    }

    /**
     * <p>
     * Handle all events and enablements for widgets in this page
     * </p>
     * 
     * @param e {@link Event} to be handled
     */
    public void handleEvent(Event e)
    {
        Widget source = e.widget;

        if (source == this.destinationBrowseButton)
        {
            handleDestinationBrowseButtonPressed();
        }

        updatePageCompletion();
    }

    /**
     * The <code>WizardDataTransfer</code> implementation of this <code>IOverwriteQuery</code> method asks the user whether the existing resource at
     * the given path should be overwritten.
     * 
     * @param pathString
     * @return the user's reply: one of <code>"YES"</code>, <code>"NO"</code>, <code>"ALL"</code>, or <code>"CANCEL"</code>
     */
    public String queryOverwrite(String pathString)
    {

        Path path = new Path(pathString);

        String messageString;
        // Break the message up if there is a file name and a directory
        // and there are at least 2 segments.
        if (path.getFileExtension() == null || path.segmentCount() < 2)
        {
            messageString = NLS.bind(
                        Messages.WindupReportExport_question_fileExists,
                        pathString);
        }
        else
        {
            messageString = NLS
                        .bind(Messages.WindupReportExport_question_overwriteNameAndPath,
                                    path.lastSegment(), path.removeLastSegments(1)
                                                .toOSString());
        }

        final MessageDialog dialog = new MessageDialog(getContainer().getShell(),
                    Messages.Question, null,
                    messageString, MessageDialog.QUESTION, new String[] {
                                IDialogConstants.YES_LABEL,
                                IDialogConstants.YES_TO_ALL_LABEL,
                                IDialogConstants.NO_LABEL,
                                IDialogConstants.NO_TO_ALL_LABEL,
                                IDialogConstants.CANCEL_LABEL }, 0)
        {
            protected int getShellStyle()
            {
                return super.getShellStyle() | SWT.SHEET;
            }
        };
        String[] response = new String[] { YES, ALL, NO, NO_ALL, CANCEL };
        // run in syncExec because callback is from an operation,
        // which is probably not running in the UI thread.
        getControl().getDisplay().syncExec(new Runnable()
        {
            public void run()
            {
                dialog.open();
            }
        });
        return dialog.getReturnCode() < 0 ? CANCEL : response[dialog.getReturnCode()];
    }

    /**
     * Create the export options specification widgets.
     * 
     */
    private void createOptionsGroupButtons(Group optionsGroup)
    {
        Font font = optionsGroup.getFont();
        optionsGroup.setLayout(new GridLayout(2, true));

        Composite left = new Composite(optionsGroup, SWT.NONE);
        left.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false));
        left.setLayout(new GridLayout(1, true));

        createFileFormatOptions(left, font);

        // compress... checkbox
        this.compressContentsCheckbox = new Button(left, SWT.CHECK | SWT.LEFT);
        this.compressContentsCheckbox.setText(Messages.WindupReportExport_compressContents);
        this.compressContentsCheckbox.setFont(font);

        Composite right = new Composite(optionsGroup, SWT.NONE);
        right.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false));
        right.setLayout(new GridLayout(1, true));

        this.reGenerateReportsCheckbox = new Button(right, SWT.CHECK | SWT.LEFT);
        this.reGenerateReportsCheckbox.setText(Messages.WindupReportExport_reGenerateReportBeforeExport);
        this.reGenerateReportsCheckbox.setFont(font);

        // initial setup
        this.compressContentsCheckbox.setSelection(true);
        this.reGenerateReportsCheckbox.setSelection(true);
    }

    /**
     * Create the buttons for the group that determine if the entire or selected directory structure should be created.
     * 
     * @param optionsGroup
     * @param font
     */
    private void createFileFormatOptions(Composite optionsGroup, Font font)
    {
        // create directory structure radios
        this.zipFormatButton = new Button(optionsGroup, SWT.RADIO | SWT.LEFT);
        this.zipFormatButton.setText(Messages.WindupReportExport_saveInZipFormat);
        this.zipFormatButton.setSelection(true);
        this.zipFormatButton.setFont(font);

        // create directory structure radios
        this.targzFormatButton = new Button(optionsGroup, SWT.RADIO | SWT.LEFT);
        this.targzFormatButton.setText(Messages.WindupReportExport_saveInTarFormat);
        this.targzFormatButton.setSelection(false);
        this.targzFormatButton.setFont(font);
    }

    /**
     * Answer the contents of self's destination specification widget. If this value does not have a suffix then add it first.
     */
    private String getDestinationValue()
    {
        String idealSuffix = getOutputSuffix();
        String destinationText = this.destinationNameField.getText().trim();

        // only append a suffix if the destination doesn't already have a . in
        // its last path segment.
        // Also prevent the user from selecting a directory. Allowing this will
        // create a ".zip" file in the directory
        if (destinationText.length() != 0
                    && !destinationText.endsWith(File.separator))
        {
            int dotIndex = destinationText.lastIndexOf('.');
            if (dotIndex != -1)
            {
                // the last path separator index
                int pathSepIndex = destinationText.lastIndexOf(File.separator);
                if (pathSepIndex != -1 && dotIndex < pathSepIndex)
                {
                    destinationText += idealSuffix;
                }
            }
            else
            {
                destinationText += idealSuffix;
            }
        }

        return destinationText;
    }

    /**
     * <p>
     * Answer the suffix that files exported from this wizard should have.
     * </p>
     */
    private String getOutputSuffix()
    {
        if (this.zipFormatButton.getSelection())
        {
            return ".zip"; //$NON-NLS-1$
        }
        else if (this.compressContentsCheckbox.getSelection())
        {
            return ".tar.gz"; //$NON-NLS-1$
        }
        else
        {
            return ".tar"; //$NON-NLS-1$
        }
    }

    /**
     * Open an appropriate destination browser so that the user can specify a source to import from
     */
    private void handleDestinationBrowseButtonPressed()
    {
        FileDialog dialog = new FileDialog(getContainer().getShell(), SWT.SAVE | SWT.SHEET);

        String extensionFilter;
        if (this.zipFormatButton.getSelection())
        {
            extensionFilter = ZIP_FIRST_FILE_FILTER;
        }
        else if (this.compressContentsCheckbox.getSelection())
        {
            extensionFilter = TARGZ_FIRST_FILE_FILTER;
        }
        else
        {
            extensionFilter = TAR_FIRST_FILE_FILTER;
        }

        dialog.setFilterExtensions(new String[] { extensionFilter, "*.*" }); //$NON-NLS-1$
        dialog.setText(Messages.WindupReportExport_selectDestintationDialogTitle);
        String currentSourceString = getDestinationValue();
        int lastSeparatorIndex = currentSourceString.lastIndexOf(File.separator);
        if (lastSeparatorIndex != -1)
        {
            dialog.setFilterPath(currentSourceString.substring(0, lastSeparatorIndex));
        }
        String selectedFileName = dialog.open();

        if (selectedFileName != null)
        {
            setErrorMessage(null);
            this.destinationNameField.setText(selectedFileName);
        }
    }

    /**
     * Hook method for restoring widget values to the values that they held last time this wizard was used to completion.
     */
    private void restoreWidgetValues()
    {
        IDialogSettings settings = getDialogSettings();
        if (settings != null)
        {
            // load previous destinations
            String[] prevDirectoryNames = settings.getArray(STORE_DESTINATION_NAMES_ID);
            if (prevDirectoryNames != null && prevDirectoryNames.length > 0)
            {
                this.destinationNameField.setText(prevDirectoryNames[0]);
                for (String prevDirecotryName : prevDirectoryNames)
                {
                    this.destinationNameField.add(prevDirecotryName);
                }
            }

            // load previous root dir names
            String[] rootDirNames = settings.getArray(STORE_ROOT_DIR_NAMES_ID);
            if (rootDirNames != null && rootDirNames.length > 0)
            {
                this.rootDirNameField.setText(rootDirNames[0]);
                for (String prevRootDirName : rootDirNames)
                {
                    this.rootDirNameField.add(prevRootDirName);
                }
            }
            else
            {
                this.rootDirNameField.setText(DEFAULT_ARCHIVE_ROOT_DIRECTORY_NAME);
            }

            if (settings.get(STORE_COMPRESS_CONTENTS_ID) != null)
            {
                this.compressContentsCheckbox.setSelection(settings.getBoolean(STORE_COMPRESS_CONTENTS_ID));
            }

            if (settings.get(STORE_RE_GENERATE_REPORTS_ID) != null)
            {
                this.reGenerateReportsCheckbox.setSelection(settings.getBoolean(STORE_RE_GENERATE_REPORTS_ID));
            }
        }
        else
        {
            this.rootDirNameField.setText(DEFAULT_ARCHIVE_ROOT_DIRECTORY_NAME);
        }
    }

    /**
     * <p>
     * Persists resource specification control setting that are to be restored in the next instance of this page.
     * </p>
     */
    private void saveWidgetValues()
    {
        // update directory names history
        IDialogSettings settings = getDialogSettings();
        if (settings != null)
        {
            // save previous directory names
            String[] directoryNames = settings.getArray(STORE_DESTINATION_NAMES_ID);
            if (directoryNames == null)
            {
                directoryNames = new String[0];
            }
            directoryNames = addToHistory(directoryNames, getDestinationValue());
            settings.put(STORE_DESTINATION_NAMES_ID, directoryNames);

            // save previous archive names
            String[] prevRoodDirNames = settings.getArray(STORE_ROOT_DIR_NAMES_ID);
            if (prevRoodDirNames == null)
            {
                prevRoodDirNames = new String[0];
            }
            prevRoodDirNames = addToHistory(prevRoodDirNames, this.rootDirNameField.getText());
            settings.put(STORE_ROOT_DIR_NAMES_ID, prevRoodDirNames);

            // save compress setting
            settings.put(STORE_COMPRESS_CONTENTS_ID,
                        this.compressContentsCheckbox.getSelection());

            // save re-generate reports setting
            settings.put(STORE_RE_GENERATE_REPORTS_ID,
                        this.reGenerateReportsCheckbox.getSelection());
        }
    }

    /**
     * Answer a boolean indicating whether the receivers destination specification widgets currently all contain valid values.
     */
    private boolean validateDestinationGroup()
    {
        String destinationValue = getDestinationValue();
        if (destinationValue.endsWith(".tar")) { //$NON-NLS-1$
            this.compressContentsCheckbox.setSelection(false);
            this.targzFormatButton.setSelection(true);
            this.zipFormatButton.setSelection(false);
        }
        else if (destinationValue.endsWith(".tar.gz") //$NON-NLS-1$
                    || destinationValue.endsWith(".tgz")) { //$NON-NLS-1$
            this.compressContentsCheckbox.setSelection(true);
            this.targzFormatButton.setSelection(true);
            this.zipFormatButton.setSelection(false);
        }
        else if (destinationValue.endsWith(".zip")) { //$NON-NLS-1$
            this.zipFormatButton.setSelection(true);
            this.targzFormatButton.setSelection(false);
        }

        boolean isValid = true;
        if (destinationValue.length() == 0)
        {
            this.setErrorMessage(Messages.WindupReportExport_destinationEmptyError);
            isValid = false;
        }

        return isValid;
    }

    /**
     * Create the export destination specification widgets
     * 
     * @param parent org.eclipse.swt.widgets.Composite
     */
    private void createDestinationGroup(Composite parent)
    {

        Font font = parent.getFont();
        // destination specification group
        Composite destinationSelectionGroup = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        destinationSelectionGroup.setLayout(layout);
        destinationSelectionGroup.setLayoutData(new GridData(
                    GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
        destinationSelectionGroup.setFont(font);

        // create the label
        Label destinationLabel = new Label(destinationSelectionGroup, SWT.NONE);
        destinationLabel.setText(Messages.WindupReportExport_destinationLabel);
        destinationLabel.setFont(font);

        // destination name entry field
        this.destinationNameField = new Combo(destinationSelectionGroup, SWT.SINGLE | SWT.BORDER);
        this.destinationNameField.addListener(SWT.Modify, this);
        this.destinationNameField.addListener(SWT.Selection, this);
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
        data.widthHint = SIZING_TEXT_FIELD_WIDTH;
        this.destinationNameField.setLayoutData(data);
        this.destinationNameField.setFont(font);

        // destination browse button
        this.destinationBrowseButton = new Button(destinationSelectionGroup, SWT.PUSH);
        this.destinationBrowseButton.setText(Messages.WindupReportExport_browse);
        this.destinationBrowseButton.addListener(SWT.Selection, this);
        this.destinationBrowseButton.setFont(font);
        this.setButtonLayoutData(this.destinationBrowseButton);
    }

    /**
     * Create the export destination specification widgets
     * 
     * @param parent org.eclipse.swt.widgets.Composite
     */
    private void createArchiveRootDirectoryNameGroup(Composite parent)
    {

        Font font = parent.getFont();
        // destination specification group
        Composite rootDirNameGroup = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        rootDirNameGroup.setLayout(layout);
        rootDirNameGroup.setLayoutData(new GridData(
                    GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
        rootDirNameGroup.setFont(font);

        // create the label
        Label rootDirNameLabel = new Label(rootDirNameGroup, SWT.NONE);
        rootDirNameLabel.setText(Messages.WindupReportExport_rootDirNameLabel);
        rootDirNameLabel.setFont(font);

        // destination name entry field
        this.rootDirNameField = new Combo(rootDirNameGroup, SWT.SINGLE | SWT.BORDER);
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
        data.widthHint = SIZING_TEXT_FIELD_WIDTH;
        this.rootDirNameField.setLayoutData(data);
        this.rootDirNameField.setFont(font);

        // vertical spacer
        new Label(parent, SWT.NONE);
    }

    /**
     * Attempts to ensure that the specified directory exists on the local file system. Answers a boolean indicating success.
     * 
     * @return boolean
     * @param directory java.io.File
     */
    private boolean ensureDirectoryExists(File directory)
    {
        if (!directory.exists())
        {
            if (!queryYesNoQuestion(Messages.WindupReportExport_question_createTargetDirectory))
            {
                return false;
            }

            if (!directory.mkdirs())
            {
                displayErrorDialog(Messages.WindupReportExport_directoryCreationError);
                giveFocusToDestination();
                return false;
            }
        }

        return true;
    }

    /**
     * If the target for export does not exist then attempt to create it. Answer a boolean indicating whether the target exists (ie.- if it either
     * pre-existed or this method was able to create it)
     * 
     * @return boolean
     */
    private boolean ensureTargetIsValid(File targetDirectory)
    {
        if (targetDirectory.exists() && !targetDirectory.isDirectory())
        {
            displayErrorDialog(Messages.WindupReportExport_directoryExists);
            giveFocusToDestination();
            return false;
        }

        return ensureDirectoryExists(targetDirectory);
    }

    /**
     * Set the current input focus to self's destination entry field
     */
    private void giveFocusToDestination()
    {
        this.destinationNameField.setFocus();
    }

    /**
     * Creates a new button with the given id.
     * <p>
     * The <code>Dialog</code> implementation of this framework method creates a standard push button, registers for selection events including button
     * presses and registers default buttons with its shell. The button id is stored as the buttons client data. Note that the parent's layout is
     * assumed to be a GridLayout and the number of columns in this layout is incremented. Subclasses may override.
     * </p>
     * 
     * @param parent the parent composite
     * @param id the id of the button (see <code>IDialogConstants.*_ID</code> constants for standard dialog button ids)
     * @param label the label from the button
     * @param defaultButton <code>true</code> if the button is to be the default button, and <code>false</code> otherwise
     */
    private Button createButton(Composite parent, int id, String label,
                boolean defaultButton)
    {
        // increment the number of columns in the button bar
        ((GridLayout) parent.getLayout()).numColumns++;

        Button button = new Button(parent, SWT.PUSH);

        GridData buttonData = new GridData(GridData.FILL_HORIZONTAL);
        button.setLayoutData(buttonData);

        button.setData(new Integer(id));
        button.setText(label);
        button.setFont(parent.getFont());

        if (defaultButton)
        {
            Shell shell = parent.getShell();
            if (shell != null)
            {
                shell.setDefaultButton(button);
            }
            button.setFocus();
        }
        button.setFont(parent.getFont());
        setButtonLayoutData(button);
        return button;
    }

    /**
     * Creates the buttons for selecting specific types or selecting all or none of the elements.
     * 
     * @param parent the parent control
     */
    private final void createButtonsGroup(Composite parent)
    {

        Font font = parent.getFont();

        // top level group
        Composite buttonComposite = new Composite(parent, SWT.NONE);
        buttonComposite.setFont(parent.getFont());

        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        layout.makeColumnsEqualWidth = true;
        buttonComposite.setLayout(layout);
        buttonComposite.setLayoutData(new GridData(
                    GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL));

        // add select all button
        Button selectButton = createButton(buttonComposite,
                    IDialogConstants.SELECT_ALL_ID, Messages.WindupReportExport_select_all, false);
        SelectionListener listener = new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                resourceGroup.setAllChecked(true);
                updateWidgetEnablements();
            }
        };
        selectButton.addSelectionListener(listener);
        selectButton.setFont(font);
        setButtonLayoutData(selectButton);

        // add de-select all button
        Button deselectButton = createButton(buttonComposite,
                    IDialogConstants.DESELECT_ALL_ID, Messages.WindupReportExport_deselect_all, false);
        listener = new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                resourceGroup.setAllChecked(false);
                updateWidgetEnablements();
            }
        };
        deselectButton.addSelectionListener(listener);
        deselectButton.setFont(font);
        setButtonLayoutData(deselectButton);

    }

    /**
     * Creates the checkbox tree and list for selecting resources.
     * 
     * @param parent the parent control
     */
    private final void createResourcesGroup(Composite parent)
    {
        this.resourceGroup = CheckboxTableViewer.newCheckList(parent, SWT.BORDER);
        GridData data = new GridData(GridData.FILL_BOTH);
        data.heightHint = SIZING_SELECTION_WIDGET_HEIGHT;
        data.widthHint = SIZING_SELECTION_WIDGET_WIDTH;
        this.resourceGroup.getTable().setLayoutData(data);

        this.resourceGroup.setLabelProvider(new WorkbenchLabelProvider());
        this.resourceGroup.setContentProvider(new BaseWorkbenchContentProvider());
        this.resourceGroup.setInput(ResourcesPlugin.getWorkspace().getRoot());

        ICheckStateListener listener = new ICheckStateListener()
        {
            public void checkStateChanged(CheckStateChangedEvent event)
            {
                updateWidgetEnablements();
            }
        };
        this.resourceGroup.addCheckStateListener(listener);

        // select the selected projects when the wizard was launched
        List<IProject> selectedProjects = Utils.getSelectedProjects(this.initialResourceSelection);
        this.resourceGroup.setCheckedElements(selectedProjects.toArray());
    }

    /**
     * Check if widgets are enabled or disabled by a change in the dialog.
     */
    private void updateWidgetEnablements()
    {

        boolean pageComplete = determinePageCompletion();
        setPageComplete(pageComplete);
        if (pageComplete)
        {
            setMessage(null);
        }
    }

    /**
     * Returns whether this page is complete. This determination is made based upon the current contents of this page's controls. Subclasses wishing
     * to include their controls in this determination should override the hook methods <code>validateSourceGroup</code> and/or
     * <code>validateOptionsGroup</code>.
     * 
     * @return <code>true</code> if this page is complete, and <code>false</code> if incomplete
     * @see #validateSourceGroup
     * @see #validateOptionsGroup
     */
    private boolean determinePageCompletion()
    {
        boolean complete = validateSourceGroup() && validateDestinationGroup();

        // Avoid draw flicker by not clearing the error
        // message unless all is valid.
        if (complete)
        {
            this.setErrorMessage(null);
        }

        return complete;
    }

    /**
     * Displays a Yes/No question to the user with the specified message and returns the user's response.
     * 
     * @param message the question to ask
     * @return <code>true</code> for Yes, and <code>false</code> for No
     */
    private boolean queryYesNoQuestion(String message)
    {
        MessageDialog dialog = new MessageDialog(
                    getContainer().getShell(),
                    Messages.Question,
                    (Image) null,
                    message,
                    MessageDialog.NONE,
                    new String[] { IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL },
                    0)
        {

            protected int getShellStyle()
            {
                return super.getShellStyle() | SWT.SHEET;
            }
        };
        // ensure yes is the default

        return dialog.open() == 0;
    }

    /**
     * Determine if the page is complete and update the page appropriately.
     */
    private void updatePageCompletion()
    {
        boolean pageComplete = determinePageCompletion();
        setPageComplete(pageComplete);
        if (pageComplete)
        {
            setErrorMessage(null);
        }
    }

    /**
     * Create the options specification widgets.
     * 
     * @param parent org.eclipse.swt.widgets.Composite
     */
    private void createOptionsGroup(Composite parent)
    {
        // options group
        Group optionsGroup = new Group(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        optionsGroup.setLayout(layout);
        optionsGroup.setLayoutData(new GridData(
                    GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
        optionsGroup.setText(Messages.Options);
        optionsGroup.setFont(parent.getFont());

        createOptionsGroupButtons(optionsGroup);
    }

    /**
     * Returns a boolean indicating whether the directory portion of the passed pathname is valid and available for use.
     */
    private boolean ensureTargetDirectoryIsValid(String fullPathname)
    {
        int separatorIndex = fullPathname.lastIndexOf(File.separator);

        if (separatorIndex == -1)
        {
            return true;
        }

        return ensureTargetIsValid(new File(fullPathname.substring(0,
                    separatorIndex)));
    }

    /**
     * Returns a boolean indicating whether the passed File handle is is valid and available for use.
     */
    private boolean ensureTargetFileIsValid(File targetFile)
    {
        if (targetFile.exists() && targetFile.isDirectory())
        {
            displayErrorDialog(Messages.WindupReportExport_mustBeFileError);
            giveFocusToDestination();
            return false;
        }

        if (targetFile.exists())
        {
            if (targetFile.canWrite())
            {
                if (!queryYesNoQuestion(Messages.WindupReportExport_question_shouldOverwriteExisting))
                {
                    return false;
                }
            }
            else
            {
                displayErrorDialog(Messages.WindupReportExport_archiveAlreadyExistsError);
                giveFocusToDestination();
                return false;
            }
        }

        return true;
    }

    /**
     * Ensures that the target output file and its containing directory are both valid and able to be used. Answer a boolean indicating validity.
     */
    private boolean ensureTargetIsValid()
    {
        String targetPath = getDestinationValue();

        if (!ensureTargetDirectoryIsValid(targetPath))
        {
            return false;
        }

        if (!ensureTargetFileIsValid(new File(targetPath)))
        {
            return false;
        }

        return true;
    }

    private boolean validateSourceGroup()
    {
        // there must be some resources selected for Export
        boolean isValid = true;
        if (this.resourceGroup.getCheckedElements().length == 0)
        {
            setErrorMessage(Messages.WindupReportExport_noneSelected);
            isValid = false;
        }
        else
        {
            setErrorMessage(null);
        }
        return isValid;
    }

    /**
     * <p>
     * Generates reports for any of the given projects that need reports generated. Then exports all of the given report parent directories.
     * </p>
     * 
     * @param reportParentDirectories parent report directories to export
     * @param projectsWithoutReports projects that need Windup reports generated before the export
     * 
     * @return <code>true</code> if the export is a success, <code>false</code> otherwise
     */
    private boolean executeExportOperation(
                List<File> reportParentDirectories,
                final List<IProject> projectsWithoutReports)
    {

        /*
         * if there are projects that need a windup report generated create an operation to do that
         */
   /*     final GenerateWindupReportsOperation generateReportsOperation =
                    projectsWithoutReports.isEmpty() ?
                                null : new GenerateWindupReportsOperation(windup, projectsWithoutReports);

        // create an operation to export all of the selected windup reports
        final ArchiveFileExportOperation exportOperation = new ArchiveFileExportOperation(
                    reportParentDirectories,
                    getDestinationValue());
        exportOperation.setUseCompression(this.compressContentsCheckbox.getSelection());
        exportOperation.setUseTarFormat(this.targzFormatButton.getSelection());

        String rootDirName = this.rootDirNameField.getText();
        if (rootDirName != null && !rootDirName.trim().isEmpty())
        {
            exportOperation.setRootArchiveDirectoryName(rootDirName);
        }

        try
        {*/
            /*
             * create an operation to optionally generate any required reports, then export all selected reports
             */
            /*getContainer().run(true, true, new IRunnableWithProgress()
            {
                @Override
                public void run(IProgressMonitor monitor)
                            throws InvocationTargetException, InterruptedException
                {

                    // determine the amount of work to do
                    int work = generateReportsOperation != null ? 2 : 1;
                    monitor.beginTask(Messages.WindupReportExport_exportReportsTitle, work);

                    try
                    {
                        // generate reports if necessary
                        if (generateReportsOperation != null)
                        {
                            generateReportsOperation.run(new SubProgressMonitor(monitor, 1));
                        }

                        // export reports
                        exportOperation.run(new SubProgressMonitor(monitor, 1));
                    }
                    finally
                    {
                        monitor.done();
                    }
                }
            });
        }
        catch (InterruptedException e)
        {
            return false;
        }
        catch (InvocationTargetException e)
        {
            displayErrorDialog(e.getTargetException());
            return false;
        }

        // if generate reports operation failed display error
        if (generateReportsOperation != null)
        {
            IStatus generateStatus = generateReportsOperation.getStatus();
            if (!generateStatus.isOK())
            {
                ErrorDialog.openError(getContainer().getShell(),
                            Messages.WindupReportExport_errorGeneratingReports,
                            null, // no special message
                            generateStatus);
                return false;
            }
        }

        // if export operation failed display error
        IStatus exportStatus = exportOperation.getStatus();
        if (!exportStatus.isOK())
        {
            ErrorDialog.openError(getContainer().getShell(),
                        org.jboss.tools.windup.ui.internal.archiver.Messages.ArchiveFileExport_errorExporting,
                        null, // no special message
                        exportStatus);
            return false;
        }
	*/
        return true;
    }

    /**
     * Display an error dialog with the specified message.
     * 
     * @param message the error message
     */
    private void displayErrorDialog(String message)
    {
        MessageDialog.open(MessageDialog.ERROR, getContainer().getShell(),
                    Messages.WindupReportExport_errorDialogTitle, message, SWT.SHEET);
    }

    /**
     * <p>
     * Display an error dialog with the information from the supplied exception.
     * </p>
     * 
     * @param exception {@link Throwable} to display the error dialog for
     */
    private void displayErrorDialog(Throwable exception)
    {
        String message = exception.getMessage();
        // Some system exceptions have no message
        if (message == null)
        {
            message = NLS.bind(
                        org.jboss.tools.windup.ui.internal.archiver.Messages.ArchiveFileExport_errorExporting,
                        exception);
        }
        displayErrorDialog(message);
    }

    /**
     * <p>
     * Adds an entry to a history, while taking care of duplicate history items and excessively long histories. The assumption is made that all
     * histories should be of length {@link #COMBO_HISTORY_LENGTH}.
     * </p>
     * 
     * @param history the current history
     * @param newEntry the entry to add to the history
     */
    private static String[] addToHistory(String[] history, String newEntry)
    {
        ArrayList<String> historyList = new ArrayList<String>(Arrays.asList(history));

        historyList.remove(newEntry);
        historyList.add(0, newEntry);

        // since only one new item was added, we can be over the limit
        // by at most one item
        if (historyList.size() > COMBO_HISTORY_LENGTH)
        {
            historyList.remove(COMBO_HISTORY_LENGTH);
        }

        return historyList.toArray(new String[historyList.size()]);
    }
}