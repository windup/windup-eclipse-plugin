/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.editor;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.windup.ui.WindupUIPlugin;

public class TextViewer {

	public static class TextViewerLabelProvider extends LabelProvider {
		
		@Override
		public String getText(Object element) {
			IContentProposal proposal = (IContentProposal)element;
			return proposal.getLabel() == null ? proposal.getContent()
					: proposal.getLabel();
		}
		@Override
		public Image getImage(Object element) {
			return WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_PARAM);
		}
	}
	
	public static interface TextViewerContentProvider extends IStructuredContentProvider {
		Object fromString(Object parent, String value);
		String toString(Object object);
		Object[] getChildren(Object object, String prefix);
	}
}
