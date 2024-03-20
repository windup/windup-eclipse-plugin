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
package org.jboss.tools.windup.ui.internal.services;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.jboss.tools.common.xml.XMLUtilities;
import org.jboss.tools.windup.runtime.options.TypeReferenceLocation;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.RulesetConstants;
import org.jboss.tools.windup.ui.internal.rules.delegate.AnnotationUtil.EvaluationContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.common.collect.Lists;

@Creatable
@Singleton
public class RulesetSelectionCreationService {

	@Inject private RulesetDOMService domService;
	
	public Element createRuleFromXPath(Document document, String xpath) {
		Element rulesetElement = domService.findOrCreateRulesetElement(document);
		Element rulesElement = domService.findOrCreateRulesElement(rulesetElement);
		Element ruleElement = domService.createRuleElement(rulesElement);
		Element whenElement = domService.createWhenElement(document);
		ruleElement.appendChild(whenElement);
		Element xmlfileElement = domService.createXMLFileElement(document);
		whenElement.appendChild(xmlfileElement);
		xmlfileElement.setAttribute(RulesetConstants.XPATH, xpath);
		Element performElement = domService.createPerformElement(ruleElement);
		Element hintElement = domService.createHintElement(performElement);
		domService.populateDefaultHintElement(hintElement);
		return xmlfileElement;
	}

	public List<Element> createRuleFromJavaEditorSelection(Document document, ASTNode[] nodes) {
		try {
			List<Element> javaClassElements = Arrays.stream(nodes).filter(node -> {
				WindupUIPlugin.logInfo("Attempting to match AST node to Annotation."); //$NON-NLS-1$
				if (node instanceof Annotation || (node instanceof Name && node.getParent() instanceof Annotation)) {
					WindupUIPlugin.logInfo("Annotation found."); //$NON-NLS-1$
					return true;
				}
				WindupUIPlugin.logInfo("Unable to match AST node to Annotation."); //$NON-NLS-1$
				return false;
			}).map(node -> {
				if (node instanceof Name && node.getParent() instanceof Annotation) {
					node = node.getParent();
				}
				WindupUIPlugin.logInfo("Attempting to create rule for annotation."); //$NON-NLS-1$
				return createJavaClassElementForAnnotations(document, Lists.newArrayList((Annotation)node));
			}).filter(Objects::nonNull).collect(Collectors.toList());
			
			if (!javaClassElements.isEmpty()) {
				return javaClassElements;
			}
			
			if (nodes != null && nodes.length == 1) {
				
				ASTNode node = nodes[0];
				
				if (node instanceof SimpleName && node.getParent() instanceof QualifiedName) {
					QualifiedName qualifiedName = (QualifiedName)node.getParent();
					if (qualifiedName.getParent() instanceof Type) {
						node = qualifiedName.getParent();
					}
					else if (qualifiedName.getParent() instanceof ImportDeclaration) {
						node = qualifiedName.getParent();
					}
				}
				
				// <javaclass references="..." /> IMPORT
				if (node instanceof ImportDeclaration) {
					String importName = ((ImportDeclaration)node).getName().getFullyQualifiedName();
					WindupUIPlugin.logInfo("Attempting to create rule for import."); //$NON-NLS-1$
					return Lists.newArrayList(createJavaClassReferenceElement(document, TypeReferenceLocation.IMPORT.toString(), importName));
				}
				// <javaclass references="..." /> IMPORT
				if (node instanceof QualifiedName && node.getParent() instanceof ImportDeclaration) {
					String importName = ((ImportDeclaration)node.getParent()).getName().getFullyQualifiedName();
					WindupUIPlugin.logInfo("Attempting to create rule for import.."); //$NON-NLS-1$
					return Lists.newArrayList(createJavaClassReferenceElement(document, TypeReferenceLocation.IMPORT.toString(), importName));
				}
				
				if (node instanceof SimpleName && node.getParent() instanceof Type) {
					node = node.getParent();
				}
				
				// <javaclass references="..." /> IMPLEMENTS_TYPE
				if (node instanceof SimpleType && node.getParent() != null &&
						node.getParent() instanceof TypeDeclaration && ((TypeDeclaration)node.getParent()).superInterfaceTypes().contains(node)) {
					String referenceName = ((SimpleType)node).getName().getFullyQualifiedName();
					ITypeBinding binding = ((SimpleType)node).resolveBinding();
					if (binding != null) {
						referenceName = binding.getQualifiedName();
					}
					WindupUIPlugin.logInfo("Attempting to create rule for implements type."); //$NON-NLS-1$
					return Lists.newArrayList(createJavaClassReferenceElement(document, TypeReferenceLocation.IMPLEMENTS_TYPE.toString(), referenceName));
				}
				WindupUIPlugin.logInfo("The selected AST node is not supported by the current rule generation facilities."); //$NON-NLS-1$
			}
			else {
				WindupUIPlugin.logInfo("Unable to create rule from selection. Only one AST node is supported."); //$NON-NLS-1$
			}
		}
		catch (Exception e) {
			WindupUIPlugin.log(e);
			return null;
		}
		return null;
	}
	
	private Element createJavaClassElementForAnnotations(Document document, List<Annotation> annotations) {
		
		Element rulesetElement = domService.findOrCreateRulesetElement(document);
		Element rulesElement = domService.findOrCreateRulesElement(rulesetElement);
		Element ruleElement = domService.createRuleElement(rulesElement);
		Element whenElement = domService.createWhenElement(document);
		ruleElement.appendChild(whenElement);
		
		Element javaClassElement = null;
		
		Annotation annotation = annotations.get(0);
		
		ASTNode parent = annotation.getParent();
		String location = "";
		
		EvaluationContext evaluationContext = new EvaluationContext(null);
		if (parent instanceof TypeDeclaration || parent instanceof MethodDeclaration) {
			javaClassElement = domService.createJavaClassElement(rulesetElement.getOwnerDocument());
		}
		else if (parent instanceof FieldDeclaration) {
			location = TypeReferenceLocation.FIELD_DECLARATION.toString();
			
			// The type the annotation is attached to.
			Type parameterType = ((FieldDeclaration)parent).getType();
			
			String referenceName = "";
			
			// If primitive, we don't really a way to specify it in the javaclass reference attribute, so 
			// we set it up to match a javaclass containing the annotation, instead of isolating it down to
			// a method parameter.
			if (parameterType.isPrimitiveType()) {
				location = "";
				referenceName = annotation.getTypeName().getFullyQualifiedName();
				ITypeBinding typeBinding = annotation.resolveTypeBinding();
				if (typeBinding != null) {
					referenceName = typeBinding.getQualifiedName();
				}
				javaClassElement = domService.createJavaClassReferencesImportElement(referenceName, rulesetElement);
				evaluationContext.setElement(javaClassElement);
			}
			else if (!(referenceName = findParamterType(parameterType)).isEmpty()) {
				javaClassElement = domService.createJavaClassReferencesImportElement(referenceName, rulesetElement);
			}
			else {
				return null;
			}
		}
		else if (parent instanceof SingleVariableDeclaration && parent.getParent() instanceof MethodDeclaration) {
			location = TypeReferenceLocation.METHOD_PARAMETER.toString();
			
			// The type the annotation is attached to.
			Type parameterType = ((SingleVariableDeclaration)parent).getType();
			
			String referenceName = "";
			
			// If primitive, we don't really a way to specify it in the javaclass reference attribute, so 
			// we set it up to match a javaclass containing the annotation, instead of isolating it down to
			// a method parameter.
			if (parameterType.isPrimitiveType()) {
				location = "";
				referenceName = annotation.getTypeName().getFullyQualifiedName();
				ITypeBinding typeBinding = annotation.resolveTypeBinding();
				if (typeBinding != null) {
					referenceName = typeBinding.getQualifiedName();
				}
				javaClassElement = domService.createJavaClassReferencesImportElement(referenceName, rulesetElement);
				evaluationContext.setElement(javaClassElement);
			}
			else if (!(referenceName = findParamterType(parameterType)).isEmpty()) {
				javaClassElement = domService.createJavaClassReferencesImportElement(referenceName, rulesetElement);
			}
			else {
				return null;
			}
		}
		
		whenElement.appendChild(javaClassElement);
		
		if (!location.isEmpty()) {
			Element locationElement = domService.createJavaClassLocation(javaClassElement);
			XMLUtilities.setText(locationElement, location);
		}
				
		AnnotationService annotationService = new AnnotationService();
		
		Element performElement = domService.createPerformElement(ruleElement);
		Element hintElement = domService.createHintElement(performElement);
		domService.populateDefaultHintElement(hintElement);
		
		annotationService.generateAnnotationElements(javaClassElement, annotation, evaluationContext);
		
		return javaClassElement;
	}
	
	private String findParamterType(Type type) {
		ITypeBinding binding = type.resolveBinding();
		if (binding != null && binding.isArray()) {
			return binding.getElementType().getQualifiedName();
		}
		if (binding != null) {
			return binding.getTypeDeclaration().getQualifiedName();
		}
		WindupUIPlugin.logErrorMessage(type.toString() + "Parameter type not resolved using type binding.");
		return "";
	}
	
	private Element createJavaClassReferenceElement(Document document, String locationType, String referenceFullyQualifiedName) {
		Element rulesetElement = domService.findOrCreateRulesetElement(document);
		Element rulesElement = domService.findOrCreateRulesElement(rulesetElement);
		Element ruleElement = domService.createRuleElement(rulesElement);
		Element whenElement = domService.createWhenElement(document);
		ruleElement.appendChild(whenElement);
		Element javaClassElement = domService.createJavaClassReferencesImportElement(referenceFullyQualifiedName, rulesetElement);
		whenElement.appendChild(javaClassElement);
		Element referenceLocation = domService.createJavaClassLocation(javaClassElement);
		XMLUtilities.setText(referenceLocation, locationType);
		Element performElement = domService.createPerformElement(ruleElement);
		Element hintElement = domService.createHintElement(performElement);
		domService.populateDefaultHintElement(hintElement);
		return javaClassElement;
	}
}
