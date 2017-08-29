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
public class AnnotationList extends AnnotationElement {

	public AnnotationList(AnnotationElement parent, Element element) {
		super(parent, element);
		setChildren(buildModel());
	}
	
	private AnnotationElement[] buildModel() {
		List<CMAttributeDeclaration> attributes = getAttributes();
		AnnotationListNameElement nameElement = new AnnotationListNameElement(this, attributes.get(1));
		AnnotationListIndexElement indexElement = new AnnotationListIndexElement(this, attributes.get(0));
		return new AnnotationElement[] {nameElement, indexElement};
	}
	
	@Override
	public String getText() {
		StringBuffer buff = new StringBuffer();
		buff.append("[Annotation List]"); //$NON-NLS-1$
		return buff.toString();
	}
	
	@Override
	public Image getImage() {
		return WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_ARRAY);
	}
	
	public static class AnnotationListIndexElement extends AttributeElement {
		public AnnotationListIndexElement(AnnotationElement parent, CMNode node) {
			super(parent, node);
		}
		
		@Override
		public Image getImage() {
			return WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_ARRAY_INDEX);
		}
	}
	
	public static class AnnotationListNameElement extends AttributeElement {
		public AnnotationListNameElement(AnnotationElement parent, CMNode node) {
			super(parent, node);
		}
	}
}
