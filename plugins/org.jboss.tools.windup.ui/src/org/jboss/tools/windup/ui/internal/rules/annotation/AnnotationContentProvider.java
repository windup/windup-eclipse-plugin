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
package org.jboss.tools.windup.ui.internal.rules.annotation;

import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;

public class AnnotationContentProvider extends StyledCellLabelProvider implements ITreeContentProvider, ILabelProvider, IStyledLabelProvider {

	@Override
	public StyledString getStyledText(Object element) {
		if (element instanceof AnnotationElement) {
			return ((AnnotationElement)element).getStyledText();
		}
		return null;
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof AnnotationElement) {
			return ((AnnotationElement)element).getImage();
		}
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof AnnotationElement) {
			return ((AnnotationElement)element).getText();
		}
		return null;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof AnnotationElement) {
			AnnotationElement element = (AnnotationElement)parentElement;
			return element.getChildElements();
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof AnnotationElement) {
			return ((AnnotationElement)element).getParent();
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof AnnotationElement) {
			return getChildren(element).length > 0;
		}
		return false;
	} 
}
