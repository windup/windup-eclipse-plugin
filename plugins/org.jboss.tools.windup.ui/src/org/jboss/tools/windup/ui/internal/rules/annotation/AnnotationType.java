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
package org.jboss.tools.windup.ui.internal.rules.annotation;

import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.xml.core.internal.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.w3c.dom.Element;

@SuppressWarnings("restriction")
public class AnnotationType extends AnnotationElement {
	
	private AnnotationTypePatternElement patternElement;
	
	public AnnotationType(AnnotationElement parent, Element element) {
		super(parent, element);
		setChildren(buildModel());
	}
	
	private AnnotationElement[] buildModel() {
		List<CMAttributeDeclaration> attributes = getAttributes();
		this.patternElement = new AnnotationTypePatternElement(this, attributes.get(0));
		return new AnnotationElement[] {patternElement};
	}
	
	@Override
	public Image getImage() {
		return WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_ANNOTATION);
	}
	
	@Override
	public String getText() {
		StringBuffer buff = new StringBuffer();
		buff.append("[Annotation Type]"); //$NON-NLS-1$
		return buff.toString();
	}
	
	public static class AnnotationTypePatternElement extends AttributeElement {
		public AnnotationTypePatternElement(AnnotationElement parent, CMNode node) {
			super(parent, node);
		}
	}
}
