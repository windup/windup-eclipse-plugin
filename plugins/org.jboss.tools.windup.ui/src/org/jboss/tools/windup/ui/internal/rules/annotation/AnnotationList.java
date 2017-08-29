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
import java.util.Map;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.xml.core.internal.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.RulesetConstants;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@SuppressWarnings("restriction")
public class AnnotationList extends AnnotationElement {
	
	private Map<Node, AnnotationElement> elementMap = Maps.newHashMap();

	public AnnotationList(AnnotationElement parent, Element element) {
		super(parent, element);
	}
	
	@Override
	public AnnotationElement[] getChildElements() {
		return buildModel();
	}
	
	private AnnotationElement[] buildModel() {
		List<CMAttributeDeclaration> attributes = getAttributes();
		AnnotationListNameElement nameElement = new AnnotationListNameElement(this, attributes.get(1));
		AnnotationListIndexElement indexElement = new AnnotationListIndexElement(this, attributes.get(0));
		List<AnnotationElement> children = Lists.newArrayList();
		children.add(nameElement);
		children.add(indexElement);
		children.addAll(collectChildren());
		return children.toArray(new AnnotationElement[children.size()]);
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
	
	private List<AnnotationElement> collectChildren() {
		List<AnnotationElement> literalElements = Lists.newArrayList();
		List<AnnotationElement> typeElements = Lists.newArrayList();
		List<AnnotationElement> listElements = Lists.newArrayList();
		for (Object child : contentHelper.getChildren(getElement())) {
			if (child instanceof Element) {
				Element element = (Element)child;
				if (Objects.equal(RulesetConstants.JAVA_CLASS_ANNOTATION_LITERAL, element.getNodeName())) {
					AnnotationElement model = elementMap.get(element);
					if (model == null) {
						model = new AnnotationLiteral(this, element);
						elementMap.put(element, model);
					}
					literalElements.add(model);
				}
				else if (Objects.equal(RulesetConstants.JAVA_CLASS_ANNOTATION_TYPE, element.getNodeName())) {
					AnnotationElement model = elementMap.get(element);
					if (model == null) {
						model = new AnnotationType(this, element);
						elementMap.put(element, model);
					}
					typeElements.add(model);
				}
				else if (Objects.equal(RulesetConstants.JAVA_CLASS_ANNOTATION_LIST, element.getNodeName())) {
					AnnotationElement model = elementMap.get(element);
					if (model == null) {
						model = new AnnotationList(this, element);
						elementMap.put(element, model);
					}
					listElements.add(model);
				}
			}
		}
		List<AnnotationElement> result = Lists.newArrayList();
		result.addAll(literalElements);
		result.addAll(typeElements);
		result.addAll(listElements);
		return result;
	}
}
