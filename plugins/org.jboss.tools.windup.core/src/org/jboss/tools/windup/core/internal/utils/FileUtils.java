package org.jboss.tools.windup.core.internal.utils;

import java.io.File;

/**
 * <p>
 * Useful file utilities.
 * </p>
 */
public class FileUtils
{
    /**
     * Delete the file or directory, recursively if specified.
     */
    public static boolean delete(File file, final boolean recursive)
    {
        boolean result = false;
        if (recursive)
        {
            result = deleteRecursive(file, true);
        }
        else
        {
            if ((file.listFiles() != null) && (file.listFiles().length != 0))
            {
                throw new RuntimeException("directory not empty"); //$NON-NLS-1$
            }

            if (isWindows())
            {
                System.gc(); // ensure no lingering handles that would prevent
                             // deletion
            }

            result = file.delete();
        }
        return result;
    }

    private static boolean isWindows()
    {
        return System.getProperty("os.name").startsWith("Windows"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    private static boolean deleteRecursive(final File file,
                final boolean collect)
    {
        boolean result = true;
        if (collect && isWindows())
        {
            System.gc(); // ensure no lingering handles that would prevent deletion
        }

        File[] children = file.listFiles();
        if (children != null)
        {
            for (File sf : children)
            {
                if (sf.isDirectory())
                {
                    if (!deleteRecursive(sf, false))
                        result = false;
                }
                else
                {
                    if (!sf.delete())
                        result = false;
                }
            }
        }

        return file.delete() && result;
    }
}
