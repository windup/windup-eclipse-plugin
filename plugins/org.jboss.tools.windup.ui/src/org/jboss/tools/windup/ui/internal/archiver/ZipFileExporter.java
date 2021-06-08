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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

/**
 * <p>
 * Exports {@link File}s to a .zip file
 * </p>
 * 
 * <p>
 * This class is based off of the {@link org.eclipse.ui.internal.wizards.datatransfer.ZipFileExporter} class used to archive
 * {@link org.eclipse.core.resources.IResource}s, this modified version of the operation is used to export {@link File}s.
 * </p>
 * 
 * @see org.eclipse.ui.internal.wizards.datatransfer.ZipFileExporter
 */
@SuppressWarnings("restriction")
public class ZipFileExporter extends AbstractArchiveFileExporter
{

    /**
     * Create an instance of this class.
     * 
     * @param archiveName Name of the archive to create and add {@link File}s too
     * @param compress <code>true</code> to use compression when creating the ZIP archive, <code>false</code> otherwise
     * 
     * @exception java.io.IOException can happen when doing file IO
     */
    public ZipFileExporter(String archiveName, boolean compress) throws IOException
    {
        super(archiveName, compress);
    }

    /**
     * @see org.jboss.tools.windup.ui.internal.archiver.AbstractArchiveFileExporter#createOutputStream(java.lang.String, boolean)
     */
    @Override
    protected ArchiveOutputStream createOutputStream(
                String archiveName, boolean compress) throws IOException
    {

        ZipArchiveOutputStream zipOut = new ZipArchiveOutputStream(new FileOutputStream(archiveName));
        zipOut.setMethod(compress ? ZipEntry.DEFLATED : ZipEntry.STORED);
        return zipOut;
    }

    /**
     * @see org.jboss.tools.windup.ui.internal.archiver.AbstractArchiveFileExporter#getNewArchiveEntry(java.io.File, java.lang.String)
     */
    @Override
    protected ArchiveEntry getNewArchiveEntry(File fileToArchive,
                String destinationPath)
    {

        return new ZipArchiveEntry(fileToArchive, destinationPath);
    }
}