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
package org.jboss.tools.windup.ui.internal.explorer;

import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;

/**
 * Represents an image descriptor for decorated images.
 */
public class ImageDecoratorDescriptor extends CompositeImageDescriptor {
	
	private ImageData baseImage;
	private ImageData decorator;
	private Point size;

	public ImageDecoratorDescriptor(ImageData baseImage, Point size, ImageData decorator) {
		this.baseImage = baseImage;
		this.decorator = decorator;
		this.size = size;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !ImageDecoratorDescriptor.class.equals(obj.getClass())) {
			return false;
		}
		ImageDecoratorDescriptor other = (ImageDecoratorDescriptor)obj;
		return (baseImage.equals(other.baseImage) && size.equals(other.size) && decorator.equals(other.decorator));
	}
	
	@Override
	public int hashCode() {
		return baseImage.hashCode() | size.hashCode() | decorator.hashCode();
	}
	
	@Override
	protected void drawCompositeImage(int width, int height) {
		drawImage(baseImage, 0, 0);
		drawBottomLeft();
	}

	private void drawBottomLeft() {
		drawImage(decorator, 0, size.y - decorator.height);
	}
	
	@Override
	protected Point getSize() {
		return size;
	}
}
