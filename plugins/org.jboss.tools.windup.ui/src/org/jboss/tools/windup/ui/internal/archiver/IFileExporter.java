/*******************************************************************************
 * Copyright (c) 2019 Red Hat, Inc.
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

/**
 * <p>
 * Interface for file exporters of different file formats. Used by the zip and tar.gz exporters.
 * </p>
 * 
 * *
 * <p>
 * This class is based off of the {@link org.eclipse.ui.internal.wizards.datatransfer.IFileExporter} class used to archive
 * {@link org.eclipse.core.resources.IResource}s, this modified version of the operation is used to export {@link File}s.
 * </p>
 * 
 * @see org.eclipse.ui.internal.wizards.datatransfer.IFileExporter
 */
@SuppressWarnings("restriction")
public interface IFileExporter
{

    /**
     * <p>
     * Do all required cleanup now that we are finished with the currently-open file.
     * </p>
     * 
     * @throws IOException Can happen when performing file IO
     */
    public void finished() throws IOException;

    /**
     * <p>
     * Write the passed file to the current archive.
     * </p>
     * 
     * <p>
     * If the given file is a {@link File} write the file to the archive, else if the {@link File} is a directory write an entry for the directory's
     * name to the archive.
     * </p>
     * 
     * @param fileToArchive {@link File} to write to the archive
     * @param destinationPath destination within the archive to write the given {@link File} to
     * 
     * @throws IOException Can happen when performing file IO
     */
    public void write(File fileToArchive, String destinationPath) throws IOException;
}