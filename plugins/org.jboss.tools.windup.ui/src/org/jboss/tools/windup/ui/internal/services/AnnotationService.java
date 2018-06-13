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
package org.jboss.tools.windup.ui.internal.services;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.wst.xml.ui.internal.tabletree.TreeContentHelper;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.RulesetConstants;
import org.jboss.tools.windup.ui.internal.rules.delegate.AnnotationUtil.EvaluationContext;
import org.jboss.tools.windup.ui.internal.rules.delegate.AnnotationUtil.IAnnotationEmitter;
import org.jboss.tools.windup.ui.internal.rules.delegate.JavaClassDelegate;
import org.jboss.tools.windup.ui.internal.rules.delegate.SnippetAnnotationVisitor;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

@SuppressWarnings("restriction")
public class AnnotationService {
	
	private TreeContentHelper contentHelper = new TreeContentHelper();
	
	// List
	public Element createAnnotationList(String name, Element parent) {
		Element annotationList = createElement(parent, RulesetConstants.JAVA_CLASS_ANNOTATION_LIST);
		if (name != null) {
			annotationList.setAttribute(RulesetConstants.LIST_NAME, name);
		}
		return annotationList;
	}
	
	// Type
	public Element createAnnotationTypeWithPattern(String pattern, Element parent) {
		Element annotationType = createElement(parent, RulesetConstants.JAVA_CLASS_ANNOTATION_TYPE);
		if (pattern != null) {
			annotationType.setAttribute(RulesetConstants.TYPE_PATTERN, pattern);
		}
		return annotationType;
	}
	
	// Literal
	public Element createAnnotationLiteralWithValue(String name, String value, Element parent) {
		Element literalElement = createElement(parent, RulesetConstants.JAVA_CLASS_ANNOTATION_LITERAL);
		if (name != null) {
			literalElement.setAttribute(RulesetConstants.LITERAL_NAME, name);
		}
		if (value != null) {
			literalElement.setAttribute(RulesetConstants.LITERAL_PATTERN, value);
		}
		return literalElement;
	}
	
	// Common
	private Element createElement(Element parent, String name) {
		Element element = parent.getOwnerDocument().createElement(name);
		parent.appendChild(element);
		RulesetDOMService.format(parent, true);
		return element;
	}
	
	// Annotation Generation
	
	public void generateAnnotationElements(Element javaclassElement, Annotation annotation, EvaluationContext evaluationContext) {
		
		IAnnotationEmitter emitter = new IAnnotationEmitter() {
			@Override
			public void emitSingleValue(String value, EvaluationContext evaluationContext) {
				createAnnotationLiteralWithValue(null, value, (Element)evaluationContext.getElement());
			}
			@Override
			public void emitMemberValuePair(String name, String value, EvaluationContext evaluationContext) {
				createAnnotationLiteralWithValue(name, value, (Element)evaluationContext.getElement());
			}
			
			@Override
			public void emitBeginMemberValuePairArrayInitializer(String name, EvaluationContext evaluationContext) {
				Node annotationList = createAnnotationList(name, (Element)evaluationContext.getElement());
				evaluationContext.setElement(annotationList);
			}
			
			@Override
			public void emitEndMemberValuePairArrayInitializer(EvaluationContext evaluationContext) {
				// popup to the current context's element's parent.
				Element element = (Element)evaluationContext.getElement();
				element = (Element)element.getParentNode();
				// not sure if this is right, we might need to get the parent context (witch might already have this parent element as its elemnt)
				evaluationContext.setElement(element);
			}
			
			@Override
			public void emitBeginArrayInitializer(EvaluationContext evaluationContext) {
				// Assuming we're handling arrays with no name (ie., not a MemberValuePair) as nameless annotation-list
				Node annotationList = createAnnotationList(null, (Element)evaluationContext.getElement());
				evaluationContext.setElement(annotationList);
			}
			
			@Override
			public void emitEndArrayInitializer(EvaluationContext evaluationContext) {
				// popup to the current context's element's parent.
				Element element = (Element)evaluationContext.getElement();
				element = (Element)element.getParentNode();
				// not sure if this is right, we might need to get the parent context (witch might already have this parent element as its elemnt)
				evaluationContext.setElement(element);
			}
			
			@Override
			public void emitAnnotation(Annotation annotation, EvaluationContext evaluationContext) {
				String annotationName = annotation.getTypeName().getFullyQualifiedName();
				ITypeBinding typeBinding= annotation.resolveTypeBinding();
				if (typeBinding != null) {
					annotationName = typeBinding.getQualifiedName();
				}
				if (evaluationContext.isTopLevelContext()) {
					boolean initialized = isJavaclassInitialized(javaclassElement);
					if (!initialized) {
						javaclassElement.setAttribute(RulesetConstants.JAVA_CLASS_REFERENCES, annotationName);
						createLocationWithName(javaclassElement, JavaClassDelegate.JAVA_CLASS_REFERENCE_LOCATION.ANNOTATION.getLabel());
						evaluationContext.setElement(javaclassElement);
					}
					else if (!evaluationContext.isInitialized()){
						Node anntotationTypeNode = createAnnotationTypeWithPattern(annotationName, javaclassElement);
						evaluationContext.setElement(anntotationTypeNode);
					}
				}
				else {
					Element parent = (Element)evaluationContext.getElement();
					Node anntotationTypeNode = createAnnotationTypeWithPattern(annotationName, parent);
					evaluationContext.setElement(anntotationTypeNode);
				}
			}
		};
		annotation.accept(new SnippetAnnotationVisitor(emitter, evaluationContext));
	}
	
	public boolean isJavaclassInitialized(Element element) {
		if (element.getAttribute(RulesetConstants.JAVA_CLASS_REFERENCES).isEmpty() && 
				element.getElementsByTagName(RulesetConstants.JAVA_CLASS_LOCATION).getLength() == 0) {
			return false;
		}
		return true;
	}
	
	// Location
	public void createLocationWithName(Element javaClassElement, String name) {
		Element locationElement = javaClassElement.getOwnerDocument().createElement(RulesetConstants.JAVA_CLASS_LOCATION);
		javaClassElement.appendChild(locationElement);
		if (name != null) {
			contentHelper.setNodeValue(locationElement, name);
		}
		RulesetDOMService.format(javaClassElement, true);
	}
}
