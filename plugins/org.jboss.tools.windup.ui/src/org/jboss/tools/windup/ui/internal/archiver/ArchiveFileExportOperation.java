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
package org.jboss.tools.windup.ui.internal.archiver;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.operation.ModalContext;
import org.eclipse.osgi.util.NLS;
import org.jboss.tools.windup.core.WindupCorePlugin;

/**
 * <p>
 * Operation for exporting a {@link File} and its children to a new .zip or .tar.gz file.
 * </p>
 * 
 * <p>
 * This class is based off of the {@link org.jboss.tools.windup.core.internal.archiver.ui.internal.wizards.datatransfer.ArchiveFileExportOperation}
 * class used to archive {@link org.eclipse.core.resources.IResource}s, this modified version of the operation is used to export {@link File}s.
 * </p>
 * 
 * @see org.jboss.tools.windup.core.internal.archiver.ui.internal.wizards.datatransfer.ArchiveFileExportOperation
 */
public class ArchiveFileExportOperation implements IRunnableWithProgress
{
    private IFileExporter exporter;

    private String destinationArchiveName;

    private IProgressMonitor monitor;

    private List<File> filesToExport;

    private File rootResource;

    private IPath rootArchiveDirectory;

    private List<IStatus> errorTable = new ArrayList<IStatus>(1); // IStatus

    private boolean useCompression = true;

    private boolean useTarFormat = false;

    /**
     * <p>
     * Create an instance of this class. Use this constructor if you wish to export specific resources without a common parent resource.
     * </p>
     * 
     * @param files Files to export
     * @param archiveName Name of archive to export the files too
     */
    public ArchiveFileExportOperation(List<File> files, String archiveName)
    {
        super();

        // Eliminate redundancies in list of resources being exported
        Iterator<File> filesIter = files.iterator();
        while (filesIter.hasNext())
        {
            File file = filesIter.next();
            if (isDescendent(files, file))
            {
                filesIter.remove(); // Removes currentResource;
            }
        }

        this.filesToExport = files;
        this.destinationArchiveName = archiveName;
        this.rootArchiveDirectory = null;
    }

    /**
     * <p>
     * Create an instance of this class. Use this constructor if you wish to recursively export a single resource and all of its descendants.
     * </p>
     * 
     * @param file The root file to recursively export to an archive.
     * @param archiveName Name of archive to export the file and its descendants to
     */
    public ArchiveFileExportOperation(File file, String archiveName)
    {
        super();
        this.rootResource = file;
        this.destinationArchiveName = archiveName;
        this.rootArchiveDirectory = null;
    }

    /**
     * Create an instance of this class. Use this constructor if you wish to export specific resources with a common parent resource (affects
     * container directory creation)
     * 
     * @param parentFile Parent file of all of the files to export to the archive
     * @param filesToExport {@link List} of files to export to the archive using the given parent file as the root file
     * @param archiveName Name of the archive to export the files to
     */
    public ArchiveFileExportOperation(File parentFile, List<File> filesToExport,
                String archiveName)
    {
        this(parentFile, archiveName);
        this.filesToExport = filesToExport;
    }

    /**
     * Set this boolean indicating whether exported resources should be compressed (as opposed to simply being stored)
     * 
     * @param value boolean
     */
    public void setUseCompression(boolean value)
    {
        this.useCompression = value;
    }

    /**
     * <p>
     * Set this boolean indicating whether the file should be output in tar.gz format rather than .zip format.
     * </p>
     * 
     * @param value boolean
     */
    public void setUseTarFormat(boolean value)
    {
        this.useTarFormat = value;
    }

    /**
     * <p>
     * Used to set an optional root directory for all archived files to be placed under in the archive. If specified the archive will have one root
     * directory with this name and all added {@link File}s to the archive will go under this one root directory.
     * </p>
     * 
     * @param rootArchiveDirectoryName name of the directory to put at the root of the archive
     */
    public void setRootArchiveDirectoryName(String rootArchiveDirectoryName)
    {
        this.rootArchiveDirectory = new Path(rootArchiveDirectoryName);
    }

    /**
     * Export the resources that were previously specified for export (or if a single resource was specified then export it recursively)
     */
    public void run(IProgressMonitor progressMonitor)
                throws InvocationTargetException, InterruptedException
    {

        this.monitor = progressMonitor;

        try
        {
            initialize();
        }
        catch (IOException e)
        {
            throw new InvocationTargetException(e,
                        NLS.bind(Messages.ArchiveFileExport_cannotOpen, e.getMessage()));
        }

        try
        {
            // determine the amount of work to be done
            int totalWork = IProgressMonitor.UNKNOWN;
            if (this.filesToExport == null)
            {
                totalWork = countChildrenOf(this.rootResource);
            }
            else
            {
                totalWork = countSelectedFilesToExport();
            }

            this.monitor.beginTask(Messages.ArchiveFileExport_exportingTitle, totalWork);

            if (this.filesToExport == null)
            {
                exportResource(this.rootResource);
            }
            else
            {
                // ie.- a list of specific resources to export was specified
                exportSpecifiedResources();
            }

            try
            {
                this.exporter.finished();
            }
            catch (IOException e)
            {
                throw new InvocationTargetException(e, NLS.bind(
                            Messages.ArchiveFileExport_cannotClose,
                            e.getMessage()));
            }
        }
        finally
        {
            this.monitor.done();
        }
    }

    /**
     * <p>
     * Returns the status of the operation. If there were any errors, the result is a status object containing individual status objects for each
     * error. If there were no errors, the result is a status object with error code <code>OK</code>.
     * </p>
     * 
     * @return the status of the operation.
     */
    public IStatus getStatus()
    {
        IStatus[] errors = new IStatus[this.errorTable.size()];
        this.errorTable.toArray(errors);
        return new MultiStatus(
                    WindupCorePlugin.PLUGIN_ID,
                    IStatus.OK,
                    errors,
                    Messages.ArchiveFileExport_problemsExporting,
                    null);
    }

    /**
     * <p>
     * Initialize this operation.
     * </p>
     * 
     * @throws java.io.IOException this can happen when performing file IO
     */
    private void initialize() throws IOException
    {
        if (this.useTarFormat)
        {
            this.exporter = new TarFileExporter(this.destinationArchiveName, this.useCompression);
        }
        else
        {
            this.exporter = new ZipFileExporter(this.destinationArchiveName, this.useCompression);
        }
    }

    /**
     * Add a new entry to the error table with the passed information
     */
    private void addError(String message, Throwable e)
    {
        this.errorTable.add(new Status(IStatus.ERROR,
                    WindupCorePlugin.PLUGIN_ID, 0, message, e));
    }

    /**
     * Answer the total number of file resources that exist at or below self in the resources hierarchy.
     * 
     * @return int
     * @param checkResource org.eclipse.core.resources.IResource
     */
    private int countChildrenOf(File parentFile)
    {
        int count = 0;

        // count all of the files to archive without using recursion
        LinkedList<File> filesToCount = new LinkedList<File>();
        filesToCount.add(parentFile);
        while (!filesToCount.isEmpty())
        {
            File fileToCount = filesToCount.pop();

            if (fileToCount.isFile())
            {
                ++count;
            }
            else
            {
                filesToCount.addAll(Arrays.asList(fileToCount.listFiles()));
            }

        }

        return count;
    }

    /**
     * Answer a boolean indicating the number of file resources that were specified for export
     * 
     * @return number of files to export
     */
    private int countSelectedFilesToExport()
    {
        int result = 0;
        Iterator<File> resources = filesToExport.iterator();
        while (resources.hasNext())
        {
            result += countChildrenOf(resources.next());
        }

        return result;
    }

    /**
     * Creates and returns the string that should be used as the name of the entry in the archive.
     * 
     * @param fileToExport {@link File} to to export
     * @param leadupDepth the number of resource levels to be included in the path including the resource itself.
     */
    private String createDestinationName(File fileToExport, IPath relativeTo)
    {
        IPath fullPath = new Path(fileToExport.getAbsolutePath());
        IPath archiveRelativePath = fullPath.makeRelativeTo(relativeTo);

        // if a root directory is specified then all files should be nested under it
        if (this.rootArchiveDirectory != null)
        {
            archiveRelativePath = this.rootArchiveDirectory.append(archiveRelativePath);
        }

        return archiveRelativePath.toString();
    }

    /**
     * <p>
     * Export the passed resource to the destination .zip. Export with no path leadup
     * </p>
     * 
     * @param fileToExport {@link File} to export.
     */
    private void exportResource(File fileToExport)
                throws InterruptedException
    {

        exportResource(fileToExport, new Path(fileToExport.getParent()));
    }

    /**
     * <p>
     * Export the passed resource to the destination archive
     * </p>
     * 
     * @param fileToExport {@link File} to export
     * @param leadupDepth the number of directory levels to be included in the path including the {@link File} itself.
     */
    private void exportResource(File fileToExport, IPath relativeTo)
                throws InterruptedException
    {

        if (fileToExport.exists())
        {
            /*
             * if the File to export is a file export it else if File is a directory recursivly export all of it's childre
             */
            if (fileToExport.isFile())
            {
                String destinationName = createDestinationName(fileToExport, relativeTo);
                this.monitor.subTask(destinationName);

                try
                {
                    this.exporter.write(fileToExport, destinationName);
                }
                catch (IOException e)
                {
                    addError(NLS.bind(
                                Messages.ArchiveFileExport_errorExporting,
                                fileToExport.getPath(),
                                e.getMessage()), e);
                }

                monitor.worked(1);
                ModalContext.checkCanceled(monitor);
            }
            else
            {

                // create an entry for empty containers
                File[] children = fileToExport.listFiles();
                if (children.length == 0)
                {
                    String destinationName = createDestinationName(fileToExport, relativeTo);
                    try
                    {
                        this.exporter.write(fileToExport, destinationName + IPath.SEPARATOR);
                    }
                    catch (IOException e)
                    {
                        addError(NLS.bind(
                                    Messages.ArchiveFileExport_errorExporting,
                                    fileToExport.getPath(),
                                    e.getMessage()), e);
                    }
                }

                for (int i = 0; i < children.length; i++)
                {
                    exportResource(children[i], relativeTo);
                }
            }
        }
    }

    /**
     * <p>
     * Export the files contained in the previously-defined filesToExport collection
     * </p>
     */
    private void exportSpecifiedResources() throws InterruptedException
    {
        Iterator<File> resources = filesToExport.iterator();

        while (resources.hasNext())
        {
            exportResource(resources.next());
        }
    }

    /**
     * <p>
     * Answer a boolean indicating whether the passed child is a descendant of one or more members of the passed resources collection
     * </p>
     * 
     * @param parentFiles Check if the given child is a child of any of these parent files
     * @param child Check if this child is a child of any of the given parent files
     * 
     * @return <code>true</code> if the given file is a descendant of any of the given parent files, <code>false</code> otherwise
     */
    private boolean isDescendent(List<File> parentFiles, File child)
    {
        if (child != null)
        {
            File parent = child.getParentFile();
            if (parentFiles.contains(parent))
            {
                return true;
            }

            return isDescendent(parentFiles, parent);
        }
        else
        {
            return false;
        }
    }
}
