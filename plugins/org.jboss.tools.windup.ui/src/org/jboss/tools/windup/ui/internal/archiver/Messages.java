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

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
    private static final String BUNDLE_NAME = "org.jboss.tools.windup.ui.internal.archiver.messages"; //$NON-NLS-1$

    public static String ArchiveFileExport_cannotOpen;

    public static String ArchiveFileExport_cannotClose;

    public static String ArchiveFileExport_exportingTitle;

    public static String ArchiveFileExport_errorExporting;

    public static String ArchiveFileExport_problemsExporting;

    static
    {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages()
    {
    }
}