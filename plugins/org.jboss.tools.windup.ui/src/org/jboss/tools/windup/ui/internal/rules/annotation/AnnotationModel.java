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
package org.jboss.tools.windup.ui.internal.rules.annotation;

import java.util.List;
import java.util.Map;

import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.RulesetConstants;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@SuppressWarnings("restriction")
public class AnnotationModel extends AnnotationElement {
	
	private Map<Node, AnnotationElement> elementMap = Maps.newHashMap();
	
	public AnnotationModel(Element javaclassElement, ModelQuery modelQuery, IStructuredModel model) {
		super(javaclassElement, modelQuery, model);
	}
	
	@Override
	public AnnotationElement[] getChildElements() {
		return buildModel();
	}
	
	private AnnotationElement[] buildModel() {
		List<AnnotationElement> elements = collectElements();
		return elements.toArray(new AnnotationElement[elements.size()]);
	}
	
	private List<AnnotationElement> collectElements() {
		List<AnnotationElement> literalElements = Lists.newArrayList();
		List<AnnotationElement> typeElements = Lists.newArrayList();
		List<AnnotationElement> listElements = Lists.newArrayList();
		for (Object child : contentHelper.getChildren(element)) {
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
