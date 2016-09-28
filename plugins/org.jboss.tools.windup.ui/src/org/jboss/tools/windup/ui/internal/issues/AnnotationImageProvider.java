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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.texteditor.IAnnotationImageProvider;
import org.jboss.tools.windup.ui.WindupUIPlugin;

/**
 * Image provider for Windup annotations.
 */
public class AnnotationImageProvider implements IAnnotationImageProvider {

	@Override
	public Image getManagedImage(Annotation annotation) {
		return WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_WINDUP);
	}

	@Override
	public String getImageDescriptorId(Annotation annotation) {
		return WindupUIPlugin.IMG_WINDUP;
	}

	@Override
	public ImageDescriptor getImageDescriptor(String imageDescritporId) {
		return WindupUIPlugin.getImageDescriptor(WindupUIPlugin.IMG_WINDUP);
	}
}
