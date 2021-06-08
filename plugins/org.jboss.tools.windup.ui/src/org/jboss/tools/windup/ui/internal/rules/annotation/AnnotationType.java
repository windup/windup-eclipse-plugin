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
public class AnnotationType extends AnnotationElement {
	
	private AnnotationTypePatternElement patternElement;
	private Map<Node, AnnotationElement> elementMap = Maps.newHashMap();

	public AnnotationType(AnnotationElement parent, Element element) {
		super(parent, element);
	}
	
	@Override
	public AnnotationElement[] getChildElements() {
		return buildModel();
	}
	
	private AnnotationElement[] buildModel() {
		List<CMAttributeDeclaration> attributes = getAttributes();
		AnnotationTypeNameElement nameELement = new AnnotationTypeNameElement(this, attributes.get(0));
		this.patternElement = new AnnotationTypePatternElement(this, attributes.get(1));
		List<AnnotationElement> children = Lists.newArrayList();
		children.add(nameELement);
		children.add(patternElement);
		children.addAll(collectChildren());
		return children.toArray(new AnnotationElement[children.size()]);
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
	
	public static class AnnotationTypeNameElement extends AttributeElement {
		public AnnotationTypeNameElement(AnnotationElement parent, CMNode node) {
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
