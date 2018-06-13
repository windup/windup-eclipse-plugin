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
package org.jboss.tools.windup.ui.internal.rules.annotation;

import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.xml.core.internal.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.w3c.dom.Element;

@SuppressWarnings("restriction")
public class AnnotationLiteral extends AnnotationElement {

	public AnnotationLiteral(AnnotationElement parent, Element element) {
		super(parent, element);
	}
	
	@Override
	public AnnotationElement[] getChildElements() {
		return buildModel();
	}
	
	@Override
	public String getText() {
		StringBuffer buff = new StringBuffer();
		buff.append("[Annotation Literal]"); //$NON-NLS-1$
		return buff.toString();
	}
	
	@Override
	public Image getImage() {
		return WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_CHARACTER);
	}
	
	public AnnotationElement[] buildModel() {
		List<CMAttributeDeclaration> attributes = getAttributes();
		AnnotationLiteralNameElement nameElement = new AnnotationLiteralNameElement(this, attributes.get(0));
		AnnotationLiteralPatternElement patternElement = new AnnotationLiteralPatternElement(this, attributes.get(1));
		return new AnnotationElement[] {nameElement, patternElement};
	}

	public static class AnnotationLiteralNameElement extends AttributeElement {
		public AnnotationLiteralNameElement(AnnotationElement parent, CMNode node) {
			super(parent, node);
		}
	}
	
	public class AnnotationLiteralPatternElement extends AttributeElement {
		public AnnotationLiteralPatternElement(AnnotationElement parent, CMNode node) {
			super(parent, node);
		}
		
		@Override
		public Image getImage() {
			return WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_EXPRESSION);
		}
	}
}
