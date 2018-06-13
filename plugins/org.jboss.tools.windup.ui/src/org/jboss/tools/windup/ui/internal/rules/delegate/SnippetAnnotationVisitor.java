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
package org.jboss.tools.windup.ui.internal.rules.delegate;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.jboss.tools.windup.ui.internal.rules.delegate.AnnotationUtil.EvaluationContext;
import org.jboss.tools.windup.ui.internal.rules.delegate.AnnotationUtil.IAnnotationEmitter;

public class SnippetAnnotationVisitor extends ASTVisitor {
	
	private IAnnotationEmitter emitter;
	private EvaluationContext evaluationContext;
	
	public SnippetAnnotationVisitor(IAnnotationEmitter emitter, EvaluationContext evaluationContext) {
		this.emitter = emitter;
		this.evaluationContext = evaluationContext;
	}
	
	public static class AnnotationVisitor extends ASTVisitor {
		
		protected Annotation thisAnnotation;
		protected EvaluationContext evaluationContext;
		protected IAnnotationEmitter emitter;
		
		public AnnotationVisitor(Annotation thisAnnotation, EvaluationContext evaluationContext, IAnnotationEmitter emitter) {
			this.thisAnnotation = thisAnnotation;
			this.evaluationContext = evaluationContext;
			this.emitter = emitter;
		}
	}
	
	private static class NormalAnnotationVisitor extends AnnotationVisitor {
		
		private boolean visitingMemberPairArrayInitializer;
		
		@SuppressWarnings("unchecked")
		public NormalAnnotationVisitor(NormalAnnotation thisAnnotation, EvaluationContext evaluationContext, IAnnotationEmitter emitter) {
			super(thisAnnotation, evaluationContext, emitter);
			emitter.emitAnnotation(thisAnnotation, evaluationContext);
			if (!thisAnnotation.values().isEmpty()) {
				thisAnnotation.values().forEach(valueObject -> ((ASTNode)valueObject).accept(this));
			}
		}
		@Override
		public boolean visit(MemberValuePair node) {
			//System.out.println("NormalAnnotationVisitor#visit(MemberValuePair node) " + node.getName() + " = " + node.getValue());
			if (node.getValue() instanceof StringLiteral) {
				emitter.emitMemberValuePair(node.getName().getFullyQualifiedName(), ((StringLiteral)node.getValue()).getLiteralValue(), evaluationContext);
				return false;
			}
			else if (node.getValue() instanceof NumberLiteral	) {
				emitter.emitMemberValuePair(node.getName().getFullyQualifiedName(), ((NumberLiteral)node.getValue()).getToken(), evaluationContext);
				return false;
			}
			else if (node.getValue() instanceof QualifiedName	) {
				emitter.emitMemberValuePair(node.getName().getFullyQualifiedName(), ((QualifiedName)node.getValue()).getFullyQualifiedName(), evaluationContext);
				return false;
			}
			else if (node.getValue() instanceof BooleanLiteral) {
				emitter.emitMemberValuePair(node.getName().getFullyQualifiedName(), String.valueOf(((BooleanLiteral)node.getValue()).booleanValue()), evaluationContext);
				return false;
			}
			else if (node.getValue() instanceof ArrayInitializer) {
				visitingMemberPairArrayInitializer = true;
				emitter.emitBeginMemberValuePairArrayInitializer(node.getName().getFullyQualifiedName(), evaluationContext);
				node.getValue().accept(this);
				return false;
			}
			return true;
		}
		
		@Override
		public boolean visit(BooleanLiteral node) {
			//System.out.println("NormalAnnotationVisitor#visit(BooleanLiteral node) " + String.valueOf(node.booleanValue()));
			emitter.emitSingleValue(String.valueOf(node.booleanValue()), evaluationContext);
			return false;
		}
		
		@Override
		public boolean visit(SimpleName node) {
			//System.out.println("NormalAnnotationVisitor#visit(SimpleName node) " + node.getIdentifier());
			emitter.emitSingleValue(node.getFullyQualifiedName(), evaluationContext);
			return true;
		}
		
		@Override
		public boolean visit(QualifiedName node) {
			//System.out.println("NormalAnnotationVisitor#visit(QualifiedName node) " + node.getName());
			emitter.emitSingleValue(node.getFullyQualifiedName(), evaluationContext);
			return true;
		}
		
		@Override
		public boolean visit(ArrayInitializer node) {
			//System.out.println("NormalAnnotationVisitor#visit(ArrayInitializer node) " + node.toString());
			return true;
		}
		
		@Override
		public void endVisit(ArrayInitializer node) {
			if (visitingMemberPairArrayInitializer) {
				emitter.emitEndMemberValuePairArrayInitializer(evaluationContext);
			}
			visitingMemberPairArrayInitializer = false;
		}
		
		@Override
		public boolean visit(ConditionalExpression node) {
			//System.out.println("NormalAnnotationVisitor#visit(ConditionalExpression node) " + node.getExpression().toString());
			emitter.emitSingleValue(node.getExpression().toString(), evaluationContext);
			return true;
		}
		
		@Override
		public boolean visit(StringLiteral node) {
			//System.out.println("NormalAnnotationVisitor#visit(StringLiteral node) " + node.getEscapedValue());
			emitter.emitSingleValue(node.getLiteralValue(), evaluationContext);
			return true;
		}
		
		@Override
		public boolean visit(NormalAnnotation node) {
			//System.out.println("NormalAnnotationVisitor#visit(NormalAnnotation node) " + node.getTypeName());
			new NormalAnnotationVisitor(node, new EvaluationContext(evaluationContext), emitter);
			return false;
		}
		
		@Override
		public boolean visit(MarkerAnnotation node) {
			//System.out.println("NormalAnnotationVisitor#visit(MarkerAnnotation node) " + node.getTypeName());
			new MarkerAnnotationVisitor(node, new EvaluationContext(evaluationContext), emitter);
			return false;
		}
		
		@Override
		public boolean visit(SingleMemberAnnotation node) {
			//System.out.println("NormalAnnotationVisitor#visit(SingleMemberAnnotation node) " + node.getTypeName());
			new SingleMemberAnnotationVisitor(node, new EvaluationContext(evaluationContext), emitter);
			return false;
		}
	}
	
	private static class MarkerAnnotationVisitor extends AnnotationVisitor {
		public MarkerAnnotationVisitor(MarkerAnnotation thisAnnotation, EvaluationContext evaluationContext, IAnnotationEmitter emitter) {
			super(thisAnnotation, evaluationContext, emitter);
			emitter.emitAnnotation(thisAnnotation, evaluationContext);
		}
	}
	
	private static class SingleMemberAnnotationVisitor extends AnnotationVisitor {
		
		private boolean visitingMemberPairArrayInitializer;
		
		public SingleMemberAnnotationVisitor(SingleMemberAnnotation thisAnnotation, EvaluationContext evaluationContext, IAnnotationEmitter emitter) {
			super(thisAnnotation, evaluationContext, emitter);
			emitter.emitAnnotation(thisAnnotation, evaluationContext);
			thisAnnotation.getValue().accept(this);
		}
		
		@Override
		public boolean visit(MemberValuePair node) {
			//System.out.println("SingleMemberAnnotationVisitor#visit(MemberValuePair node) " + node.getName() + " = " + node.getValue());
			String name = node.getName().getFullyQualifiedName();
			if (node.getValue() instanceof ArrayInitializer) {
				visitingMemberPairArrayInitializer = true;
				emitter.emitBeginMemberValuePairArrayInitializer(name, evaluationContext);
			}
			return true;
		}
		
		@Override
		public boolean visit(SimpleName node) {
			//System.out.println("SingleMemberAnnotationVisitor#visit(SimpleName node) " + node.getIdentifier());
			emitter.emitSingleValue(node.getFullyQualifiedName(), evaluationContext);
			return false;
		}
		
		@Override
		public boolean visit(BooleanLiteral node) {
			//System.out.println("SingleMemberAnnotationVisitor#visit(BooleanLiteral node) " + String.valueOf(node.booleanValue()));
			emitter.emitSingleValue(String.valueOf(node.booleanValue()), evaluationContext);
			return false;
		}
		
		@Override
		public boolean visit(QualifiedName node) {
			//System.out.println("SingleMemberAnnotationVisitor#visit(QualifiedName node) " + node.getName());
			emitter.emitSingleValue(node.getFullyQualifiedName(), evaluationContext);
			return false;
		}
		
		@Override
		public boolean visit(StringLiteral node) {
			//System.out.println("SingleMemberAnnotationVisitor#visit(StringLiteral node) " + node.getLiteralValue());
			emitter.emitSingleValue(node.getLiteralValue(), evaluationContext);
			return false;
		}

		@Override
		public boolean visit(ArrayInitializer node) {
			//System.out.println("SingleMemberAnnotationVisitor#visit(ArrayInitializer node) " + node.expressions().toString());
			if (!visitingMemberPairArrayInitializer)	 {
				// we may not want to block with the above, and handle it a little different b/c
				// nested arrays might break this code.
				emitter.emitBeginArrayInitializer(evaluationContext);
			}
			return true;
		}
		
		@Override
		public void endVisit(ArrayInitializer node) {
			if (visitingMemberPairArrayInitializer) {
				emitter.emitEndMemberValuePairArrayInitializer(evaluationContext);
			}
			else {
				// we may not want to block with the above, and handle it a little different b/c
				// nested arrays might break this code.
				emitter.emitEndArrayInitializer(evaluationContext);
			}
			visitingMemberPairArrayInitializer = false;
		}
		
		@Override
		public boolean visit(NormalAnnotation node) {
			//System.out.println("SingleMemberAnnotationVisitor#visit(NormalAnnotation node) " + node.getTypeName());
			new NormalAnnotationVisitor(node, new EvaluationContext(evaluationContext), emitter);
			return false;
		}
		
		@Override
		public boolean visit(MarkerAnnotation node) {
			//System.out.println("SingleMemberAnnotationVisitor#visit(MarkerAnnotation node) " + node.getTypeName());
			new MarkerAnnotationVisitor(node, new EvaluationContext(evaluationContext), emitter);
			return false;
		}
		
		@Override
		public boolean visit(SingleMemberAnnotation node) {
			new SingleMemberAnnotationVisitor(node, new EvaluationContext(evaluationContext), emitter);
			return false;
		}
	}

	@Override
	public boolean visit(NormalAnnotation node) {
		//System.out.println("SnippetAnnotationVisitor#visit(NormalAnnotation node) " + node.getTypeName());
		new NormalAnnotationVisitor(node, evaluationContext, emitter);
		return false;
	}
	
	@Override
	public boolean visit(MarkerAnnotation node) {
		//System.out.println("SnippetAnnotationVisitor#visit(MarkerAnnotation node) " + node.getTypeName());
		new MarkerAnnotationVisitor(node, evaluationContext, emitter);
		return false;
	}
	
	@Override
	public boolean visit(SingleMemberAnnotation node) {
		//System.out.println("SnippetAnnotationVisitor#visit(SingleMemberAnnotation node) " + node.getTypeName());
		new SingleMemberAnnotationVisitor(node, evaluationContext, emitter);
		return false;
	}
}
