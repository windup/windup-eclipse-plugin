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
package org.jboss.tools.windup.ui.internal.services;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.IntersectionType;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NameQualifiedType;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.eclipse.jdt.core.dom.UnionType;
import org.eclipse.jdt.core.dom.WildcardType;
import org.eclipse.jdt.internal.ui.actions.SelectionConverter;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jdt.internal.ui.javaeditor.JavaTextSelection;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.ui.texteditor.ITextEditor;
import org.jboss.tools.common.xml.XMLUtilities;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.rules.RulesetEditorWrapper;
import org.jboss.tools.windup.ui.internal.rules.delegate.JavaClassDelegate;
import org.jboss.tools.windup.ui.internal.rules.delegate.AnnotationUtil.EvaluationContext;
import org.jboss.windup.ast.java.data.TypeReferenceLocation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.common.collect.Lists;

@Creatable
@Singleton
@SuppressWarnings("restriction")
public class RulesetSelectionCreationService {

	@Inject private RulesetDOMService domService;

	public Element createRuleFromEditorSelection(ITextEditor editor, RulesetEditorWrapper wrapper) {
		if (editor instanceof JavaEditor) {
			return createRuleFromJavaEditorSelection((JavaEditor)editor, wrapper);
		}
		return null;
	}
	
	private Element createRuleFromJavaEditorSelection(JavaEditor editor, RulesetEditorWrapper wrapper) {
		Document document = wrapper.getDocument();
		if (SelectionConverter.getInputAsCompilationUnit(editor) != null) {
			ITextSelection textSelection = (ITextSelection)editor.getSelectionProvider().getSelection();
			JavaTextSelection javaSelection= new JavaTextSelection(domService.getEditorInput(editor), domService.getDocument(editor), textSelection.getOffset(), textSelection.getLength());
			try {
				ASTNode[] nodes = javaSelection.resolveSelectedNodes();
				if (nodes != null && nodes.length == 1) {
					
					ASTNode node = nodes[0];
					
					if (node instanceof Annotation) {
						return createJavaClassElementForAnnotations(wrapper, Lists.newArrayList((Annotation)node));
					}
					
					else if (node instanceof Name && node.getParent() instanceof Annotation) {
						return createJavaClassElementForAnnotations(wrapper, Lists.newArrayList((Annotation)node.getParent()));
					}
					
					// <javaclass references="..." /> IMPORT
					if (node instanceof ImportDeclaration) {
						String importName = ((ImportDeclaration)node).getName().getFullyQualifiedName();
						return createJavaClassReferenceElement(document, TypeReferenceLocation.IMPORT.toString(), importName);
					}
					// <javaclass references="..." /> IMPORT
					if (node instanceof QualifiedName && node.getParent() instanceof ImportDeclaration) {
						String importName = ((ImportDeclaration)node.getParent()).getName().getFullyQualifiedName();
						return createJavaClassReferenceElement(document, TypeReferenceLocation.IMPORT.toString(), importName);
					}
					// <javaclass references="..." /> IMPLEMENTS_TYPE
					if (node instanceof SimpleType && node.getParent() != null &&
							node.getParent() instanceof TypeDeclaration && ((TypeDeclaration)node.getParent()).superInterfaceTypes().contains(node)) {
						String referenceName = ((SimpleType)node).getName().getFullyQualifiedName();
						ITypeBinding binding = ((SimpleType)node).resolveBinding();
						if (binding != null) {
							referenceName = binding.getQualifiedName();
						}
						return createJavaClassReferenceElement(document, TypeReferenceLocation.IMPLEMENTS_TYPE.toString(), referenceName);
					}
				}
			}
			catch (Exception e) {
				WindupUIPlugin.log(e);
				return null;
			}
		}
		return null;
	}
	
	private Element createJavaClassElementForAnnotations(RulesetEditorWrapper wrapper, List<Annotation> annotations) {
		Document document = wrapper.getDocument();
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
		if (parent instanceof TypeDeclaration) {
			javaClassElement = domService.createJavaClassElement(rulesetElement.getOwnerDocument());
		}
		else if (parent instanceof FieldDeclaration) {
			location = TypeReferenceLocation.FIELD_DECLARATION.toString();
			javaClassElement = domService.createJavaClassReferencesImportElement(((SimpleType)((FieldDeclaration)parent).getType()).getName().getFullyQualifiedName(), rulesetElement);
		}
		else if (parent instanceof MethodDeclaration) {
			// location METHOD
			location = TypeReferenceLocation.METHOD.toString();
			String annotationName = annotation.getTypeName().getFullyQualifiedName();
			ITypeBinding typeBinding= annotation.resolveTypeBinding();
			if (typeBinding != null) {
				annotationName = typeBinding.getQualifiedName();
			}
			javaClassElement = domService.createJavaClassReferencesImportElement(annotationName, rulesetElement);
			evaluationContext.setElement(javaClassElement);
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
			Element referenceLocation = domService.createJavaClassReferenceLocation(javaClassElement);
			XMLUtilities.setText(referenceLocation, location);
		}
		
		Element performElement = domService.createPerformElement(ruleElement);
		Element hintElement = domService.createHintElement(performElement);
		domService.populateDefaultHintElement(hintElement);

		wrapper.selectAndReveal(javaClassElement);
		
		JavaClassDelegate uiDelegate = (JavaClassDelegate)wrapper.getUiDelegate(javaClassElement);
		uiDelegate.generateAnnotationElements(annotation, evaluationContext);
		
		return null;
	}
	
	private String findParamterType(Type type) {
		ITypeBinding binding = type.resolveBinding();
		if (binding != null) {
			return binding.getQualifiedName();
		}
		WindupUIPlugin.logErrorMessage("Parameter type not resolved using type binding.");
		return "";
	}
	
	private String getTypeBinding(Expression expression) {
		ITypeBinding typeBinding = expression.resolveTypeBinding();
		if (typeBinding != null) {
			return typeBinding.getQualifiedName();
		}
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
		Element referenceLocation = domService.createJavaClassReferenceLocation(javaClassElement);
		XMLUtilities.setText(referenceLocation, locationType);
		Element performElement = domService.createPerformElement(ruleElement);
		Element hintElement = domService.createHintElement(performElement);
		domService.populateDefaultHintElement(hintElement);
		return javaClassElement;
	}
}
