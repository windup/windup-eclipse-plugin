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
package org.jboss.tools.windup.ui.internal.rules.delegate;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.text.Document;

public class AnnotationUtil {
	
	public static class EvaluationContext {
		private EvaluationContext parentContext;
		private Object element;
		
		public EvaluationContext(EvaluationContext parentContext) {
			this.parentContext = parentContext;
		}
		
		public Object getElement() {
			if (element == null) {
				return parentContext.getElement();
			}
			return element;
		}
		
		public void setElement(Object element) {
			this.element = element;
		}
		
		public EvaluationContext getParentEvaluationContext() {
			return parentContext;
		}
		
		public boolean isTopLevelContext() {
			return parentContext == null;
		}
	}
	
	public static interface IAnnotationEmitter {
		// if evaluationContext parent is null, we're at the top-level.
		// if at top level:
		// 		* if reference is empty, set reference to the annotation, and
		// 	    * if location is empty, set location to annotation.
		
		// 		* Otherwise, if reference is not empty, 
		// 		* create <annotation-type pattern=annotationName></annotation-type>
		void emitAnnotation(Annotation annotation, EvaluationContext evaluationContext);
		
		// results in <annotation-literal pattern=value /> 
		// this should cover @MyAnnotation("myValue") and
		// a single value in an array initializer @MyAnnotation(mylist={"myValue", 1+2==2?true:false, "myValue2"})
		void emitSingleValue(String value, EvaluationContext evaluationContext);
		
		// results in <annotation-literal name=name pattern=value />
		// this covers conditionExpression and Annotation
		void emitMemberValuePair(String name, String value, EvaluationContext evaluationContext);
		
		// results in <annotation-type pattern=name></annotation-type>
		void emitBeginMemberValuePairArrayInitializer(String name, EvaluationContext evaluationContext);
		
		void emitEndMemberValuePairArrayInitializer(EvaluationContext evaluationContext);
		
		void emitBeginArrayInitializer(EvaluationContext evaluationContext);
		void emitEndArrayInitializer(EvaluationContext evaluationContext);
	}
	
	public static Annotation getAnnotationElement(String annotation) {
		JavaProject javaProject = new JavaProject();
		Document doc = new Document("package org.jboss.toos.windup.rules.java.tmp;" + System.lineSeparator() + 
				annotation + System.lineSeparator() + "\npublic class X {}"+System.lineSeparator()); //$NON-NLS-1$ //$NON-NLS-2$
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setProject(javaProject.getProject());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(doc.get().toCharArray());
		CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		TypeDeclaration type = (TypeDeclaration)cu.types().get(0);
		return (Annotation)type.modifiers().get(0);
	}
}
