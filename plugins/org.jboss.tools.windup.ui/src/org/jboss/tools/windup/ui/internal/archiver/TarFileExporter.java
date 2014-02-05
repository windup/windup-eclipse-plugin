/*******************************************************************************
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.archiver;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

/**
 * <p>
 * Exports {@link File}s to a .tar or .tar.gz file depending on if compression
 * is enabled or not.
 * </p>
 * 
 * <p>
 * This class is based off of the
 * {@link org.eclipse.ui.internal.wizards.datatransfer.TarFileExporter} class
 * used to archive {@link org.eclipse.core.resources.IResource}s, this modified
 * version of the operation is used to export {@link File}s.
 * </p>
 * 
 * @see org.eclipse.ui.internal.wizards.datatransfer.TarFileExporter
 */
public class TarFileExporter extends AbstractArchiveFileExporter {
	private GZIPOutputStream gzipOutputStream;

	/**
	 * @param archiveName
	 *            Name of the archive to create and add {@link File}s too
	 * @param compress
	 *            <code>true</code> to compress the contents of the TAR using
	 *            GZip compression, <code>false</code> to not use compression at
	 *            all
	 * 
	 * @exception java.io.IOException
	 *                can happen when doing file IO
	 */
	public TarFileExporter(String archiveName, boolean compress) throws IOException {
		super(archiveName, compress);
	}

	/**
	 * @see org.jboss.tools.windup.ui.internal.archiver.IFileExporter#finished()
	 */
	@Override
	public void finished() throws IOException {
		super.finished();
		if (this.gzipOutputStream != null) {
			this.gzipOutputStream.close();
		}
	}

	/**
	 * @see org.jboss.tools.windup.ui.internal.archiver.AbstractArchiveFileExporter#createOutputStream(java.lang.String, boolean)
	 */
	@Override
	protected ArchiveOutputStream createOutputStream(String archiveName,
			boolean compress) throws IOException {
		
		ArchiveOutputStream archiveOut;
		//if compression enabled use GZip to do the compression
		if (compress) {
			this.gzipOutputStream = new GZIPOutputStream(
					new FileOutputStream(archiveName));
			archiveOut = new TarArchiveOutputStream(
					new BufferedOutputStream(this.gzipOutputStream));
		} else {
			archiveOut = new TarArchiveOutputStream(
					new BufferedOutputStream(new FileOutputStream(archiveName)));
		}
		
		return archiveOut;
	}

	/**
	 * @see org.jboss.tools.windup.ui.internal.archiver.AbstractArchiveFileExporter#getNewArchiveEntry(java.io.File, java.lang.String)
	 */
	@Override
	protected ArchiveEntry getNewArchiveEntry(File fileToArchive,
			String destinationPath) {
	
		return new TarArchiveEntry(fileToArchive, destinationPath);
	}
}