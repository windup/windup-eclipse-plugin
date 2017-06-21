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

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.internal.ui.actions.SelectionConverter;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jdt.internal.ui.javaeditor.JavaTextSelection;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.ui.texteditor.ITextEditor;
import org.jboss.tools.common.xml.XMLUtilities;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.windup.ast.java.data.TypeReferenceLocation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Creatable
@Singleton
@SuppressWarnings("restriction")
public class RulesetSelectionCreationService {

	@Inject private RulesetDOMService domService;

	public Element createRuleFromEditorSelection(ITextEditor editor, Document document) {
		if (editor instanceof JavaEditor) {
			return createRuleFromJavaEditorSelection((JavaEditor)editor, document);
		}
		return null;
	}
	
	private Element createRuleFromJavaEditorSelection(JavaEditor editor, Document document) {
		if (SelectionConverter.getInputAsCompilationUnit(editor) != null) {
			ITextSelection textSelection = (ITextSelection)editor.getSelectionProvider().getSelection();
			JavaTextSelection javaSelection= new JavaTextSelection(domService.getEditorInput(editor), domService.getDocument(editor), textSelection.getOffset(), textSelection.getLength());
			try {
				ASTNode[] nodes = javaSelection.resolveSelectedNodes();
				if (nodes != null && nodes.length == 1) {
					
					// <javaclass references="..." /> IMPORT
					if (nodes[0] instanceof ImportDeclaration) {
						String importName = ((ImportDeclaration)nodes[0]).getName().getFullyQualifiedName();
						return createJavaClassReferenceElement(document, TypeReferenceLocation.IMPORT.toString(), importName);
					}
					// <javaclass references="..." /> IMPORT
					if (nodes[0] instanceof QualifiedName && nodes[0].getParent() instanceof ImportDeclaration) {
						String importName = ((ImportDeclaration)nodes[0].getParent()).getName().getFullyQualifiedName();
						return createJavaClassReferenceElement(document, TypeReferenceLocation.IMPORT.toString(), importName);
					}
					// <javaclass references="..." /> IMPLEMENTS_TYPE
					if (nodes[0] instanceof SimpleType && nodes[0].getParent() != null &&
							nodes[0].getParent() instanceof TypeDeclaration && ((TypeDeclaration)nodes[0].getParent()).superInterfaceTypes().contains(nodes[0])) {
						String referenceName = ((SimpleType)nodes[0]).getName().getFullyQualifiedName();
						ITypeBinding binding = ((SimpleType)nodes[0]).resolveBinding();
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
		domService.createMessageElement(hintElement);
		return javaClassElement;
	}
}
