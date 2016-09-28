/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.issues;

import org.eclipse.core.resources.IMarker;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.markers.WorkbenchMarkerResolution;
import org.jboss.tools.windup.ui.WindupUIPlugin;

/**
 * Windup's quick fix resolutions.
 */
public class IssueResolutions {
	
	public static DeleteLineResolution DELETE_RESOLUTION = 
			new DeleteLineResolution();
	
	public static ReplaceStringResolution REPLACE_RESOLUTION = 
			new ReplaceStringResolution();
	
	public static InsertLineResolution INSERT_RESOLUTION = 
			new InsertLineResolution();
	
	/**
	 * Resolution for deleting line.
	 */
	public static class DeleteLineResolution extends WorkbenchMarkerResolution {
		
		private static String DESCRIPTION = "Delete line containing migration issue.";
		private static String LABEL = "Delete line.";
		
		@Override
		public String getDescription() {
			return DESCRIPTION;
		}

		@Override
		public Image getImage() {
			return WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_WINDUP);
		}

		@Override
		public String getLabel() {
			return LABEL;
		}

		@Override
		public void run(IMarker marker) {
		}

		@Override
		public IMarker[] findOtherMarkers(IMarker[] markers) {
			return new IMarker[0];
		}
	}
	
	/**
	 * Resolution for inserting line.
	 */
	public static class InsertLineResolution extends WorkbenchMarkerResolution {
		
		private static String DESCRIPTION = "Insert line containing quick fix.";
		private static String LABEL = "Insert line.";
		
		@Override
		public String getDescription() {
			return DESCRIPTION;
		}

		@Override
		public Image getImage() {
			return WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_WINDUP);
		}

		@Override
		public String getLabel() {
			return LABEL;
		}

		@Override
		public void run(IMarker marker) {
		}

		@Override
		public IMarker[] findOtherMarkers(IMarker[] markers) {
			return new IMarker[0];
		}
	}
	
	/**
	 * Resolution for string replacement.
	 */
	public static class ReplaceStringResolution extends WorkbenchMarkerResolution {
		
		private static String DESCRIPTION = "Replace quick fix.";
		private static String LABEL = "Replace string.";
		
		@Override
		public String getDescription() {
			return DESCRIPTION;
		}

		@Override
		public Image getImage() {
			return WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_WINDUP);
		}

		@Override
		public String getLabel() {
			return LABEL;
		}

		@Override
		public void run(IMarker marker) {
		}

		@Override
		public IMarker[] findOtherMarkers(IMarker[] markers) {
			return new IMarker[0];
		}
	}
}
