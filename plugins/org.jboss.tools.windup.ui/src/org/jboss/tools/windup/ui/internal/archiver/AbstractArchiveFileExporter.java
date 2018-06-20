/*******************************************************************************
 * Copyright (c) 2018 Red Hat, Inc.
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
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.io.IOUtils;

/**
 * <p>
 * Defines an abstract {@link IFileExporter} which uses the Apache commons archivers API to perform archive export operations.
 * </p>
 */
public abstract class AbstractArchiveFileExporter implements IFileExporter
{
    private ArchiveOutputStream outputStream;

    /**
     * Create an instance of this class.
     * 
     * @param archiveName Name of the archive to create and add {@link File}s too
     * @param compress <code>true</code> to use compression when creating the ZIP archive, <code>false</code> otherwise
     * 
     * @exception java.io.IOException can happen when doing file IO
     */
    public AbstractArchiveFileExporter(String archiveName, boolean compress) throws IOException
    {
        this.outputStream = this.createOutputStream(archiveName, compress);
    }

    /**
     * @see org.jboss.tools.windup.ui.internal.archiver.IFileExporter#write(java.io.File, java.lang.String)
     */
    @Override
    public final void write(File fileToArchive, String destinationPath)
                throws IOException
    {

        // create and add a new zip entry
        ArchiveEntry newEntry = this.getNewArchiveEntry(fileToArchive, destinationPath);
        this.outputStream.putArchiveEntry(newEntry);

        /*
         * if file, add it's contents to the archive else if directory close the new entry
         */
        if (fileToArchive.isFile())
        {
            FileInputStream fInputStream = null;
            try
            {
                fInputStream = new FileInputStream(fileToArchive);
                IOUtils.copy(fInputStream, this.outputStream);
                this.outputStream.closeArchiveEntry();
            }
            finally
            {
                IOUtils.closeQuietly(fInputStream);
            }
        }
        else
        {
            // close the directory entry
            this.outputStream.closeArchiveEntry();
        }
    }

    /**
     * <p>
     * Closes the output stream returned by {@link #createOutputStream(String, boolean)}.
     * </p>
     * 
     * <p>
     * If subclasses overrides this method then it needs to be sure to call the super implimentation.
     * </p>
     * 
     * @see org.jboss.tools.windup.ui.internal.archiver.IFileExporter#finished()
     */
    @Override
    public void finished() throws IOException
    {
        this.outputStream.close();
    }

    /**
     * <p>
     * Creates a new output stream for this file exporter to use.
     * </p>
     * 
     * @param archiveName Name of the archive to create and add {@link File}s too
     * @param compress <code>true</code> to use compression when creating the ZIP archive, <code>false</code> otherwise
     * 
     * @return creates the {@link ArchiveOutputStream} to use for this exporter.
     * 
     * @throws IOException this can happen during file IO
     */
    protected abstract ArchiveOutputStream createOutputStream(
                String archiveName, boolean compress) throws IOException;

    /**
     * <p>
     * Creates and returns a new {@link ArchiveEntry} based on the given information.
     * </p>
     * 
     * @param fileToArchive {@link File} to archive to the given destination in the archive
     * @param destinationPath destination to archive the given {@link File} in the archive
     * 
     * @return a new {@link ArchiveEntry} for the given file to archive to the given destination
     */
    protected abstract ArchiveEntry getNewArchiveEntry(
                File fileToArchive, String destinationPath);
}