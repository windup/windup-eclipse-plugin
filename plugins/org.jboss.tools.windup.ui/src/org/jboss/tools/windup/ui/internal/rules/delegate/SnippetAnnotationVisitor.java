package org.jboss.tools.windup.ui.internal.rules.delegate;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.jboss.tools.windup.ui.internal.rules.delegate.AnnotationUtil.DOMEmitter;
import org.jboss.tools.windup.ui.internal.rules.delegate.AnnotationUtil.EvaluationContext;

import com.google.common.base.Objects;

public class SnippetAnnotationVisitor extends ASTVisitor {
	
	private DOMEmitter emitter;
	private EvaluationContext evaluationContext;
	
	public SnippetAnnotationVisitor(DOMEmitter emitter, EvaluationContext evaluationContext) {
		this.emitter = emitter;
		this.evaluationContext = evaluationContext;
	}
	
	public static class AnnotationVisitor extends ASTVisitor {
		
		protected Annotation thisAnnotation;
		protected EvaluationContext evaluationContext;
		protected DOMEmitter emitter;
		
		public AnnotationVisitor(Annotation thisAnnotation, EvaluationContext evaluationContext, DOMEmitter emitter) {
			this.thisAnnotation = thisAnnotation;
			this.evaluationContext = evaluationContext;
			this.emitter = emitter;
		}
	}
	
	private static class NormalAnnotationVisitor extends AnnotationVisitor {
		
		public NormalAnnotationVisitor(NormalAnnotation thisAnnotation, EvaluationContext evaluationContext, DOMEmitter emitter) {
			super(thisAnnotation, evaluationContext, emitter);
		}
		@Override
		public boolean visit(MemberValuePair node) {
			System.out.println("NormalAnnotationVisitor#visit(MemberValuePair node) " + node.getName() + " = " + node.getValue());
			return true;
		}
		
		@Override
		public boolean visit(SimpleName node) {
			System.out.println("NormalAnnotationVisitor#visit(SimpleName node) " + node.getIdentifier());
			return true;
		}
		
		@Override
		public boolean visit(QualifiedName node) {
			System.out.println("NormalAnnotationVisitor#visit(QualifiedName node) " + node.getName());
			return true;
		}
		
		@Override
		public boolean visit(ArrayInitializer node) {
			System.out.println("NormalAnnotationVisitor#visit(ArrayInitializer node) " + node.toString());
			return true;
		}
		
		@Override
		public boolean visit(ConditionalExpression node) {
			System.out.println("NormalAnnotationVisitor#visit(ConditionalExpression node) " + node.getExpression().toString());
			return true;
		}
		
		@Override
		public boolean visit(StringLiteral node) {
			System.out.println("NormalAnnotationVisitor#visit(StringLiteral node) " + node.getEscapedValue());
			return true;
		}
		
		@Override
		public boolean visit(NormalAnnotation node) {
			if (!Objects.equal(thisAnnotation, node)) {
				System.out.println("NormalAnnotationVisitor#visit(NormalAnnotation node) " + node.getTypeName());
				node.accept(new NormalAnnotationVisitor(node, new EvaluationContext(evaluationContext), emitter));
				return false;
			}
			return true;
		}
		
		@Override
		public boolean visit(MarkerAnnotation node) {
			System.out.println("NormalAnnotationVisitor#visit(MarkerAnnotation node) " + node.getTypeName());
			node.accept(new MarkerAnnotationVisitor(node, new EvaluationContext(evaluationContext), emitter));
			return false;
		}
		
		@Override
		public boolean visit(SingleMemberAnnotation node) {
			System.out.println("NormalAnnotationVisitor#visit(SingleMemberAnnotation node) " + node.getTypeName());
			node.accept(new SingleMemberAnnotationVisitor(node, new EvaluationContext(evaluationContext), emitter));
			return false;
		}
	}
	
	private static class MarkerAnnotationVisitor extends AnnotationVisitor {
		
		public MarkerAnnotationVisitor(MarkerAnnotation thisAnnotation, EvaluationContext evaluationContext, DOMEmitter emitter) {
			super(thisAnnotation, evaluationContext, emitter);
		}
		
		@Override
		public boolean visit(SimpleName node) {
			System.out.println("MarkerAnnotationVisitor#visit(SimpleName node) " + node.getIdentifier());
			
			return false;
		}
		
		@Override
		public boolean visit(QualifiedName node) {
			System.out.println("MarkerAnnotationVisitor#visit(QualifiedName node) " + node.getName());
			
			return false;
		}
	}
	
	private static class SingleMemberAnnotationVisitor extends AnnotationVisitor {
		
		public SingleMemberAnnotationVisitor(SingleMemberAnnotation thisAnnotation, EvaluationContext evaluationContext, DOMEmitter emitter) {
			super(thisAnnotation, evaluationContext, emitter);
		}
		
		@Override
		public boolean visit(MemberValuePair node) {
			System.out.println("SingleMemberAnnotationVisitor#visit(MemberValuePair node) " + node.getName() + " = " + node.getValue());
			return true;
		}
		
		@Override
		public boolean visit(SimpleName node) {
			System.out.println("SingleMemberAnnotationVisitor#visit(SimpleName node) " + node.getIdentifier());
			return false;
		}
		
		@Override
		public boolean visit(QualifiedName node) {
			System.out.println("SingleMemberAnnotationVisitor#visit(QualifiedName node) " + node.getName());
			return false;
		}
		
		@Override
		public boolean visit(StringLiteral node) {
			System.out.println("SingleMemberAnnotationVisitor#visit(StringLiteral node) " + node.getLiteralValue());
			return false;
		}
		
		@Override
		public boolean visit(SingleMemberAnnotation node) {
			if (!Objects.equal(thisAnnotation, node)) {
				node.accept(new SingleMemberAnnotationVisitor(node, new EvaluationContext(evaluationContext), emitter));
				return false;
			}
			return true;
		}
	}

	@Override
	public boolean visit(NormalAnnotation node) {
		System.out.println("SnippetAnnotationVisitor#visit(NormalAnnotation node) " + node.getTypeName());
		node.accept(new NormalAnnotationVisitor(node, new EvaluationContext(evaluationContext), emitter));
		return false;
	}
	
	@Override
	public boolean visit(MarkerAnnotation node) {
		System.out.println("SnippetAnnotationVisitor#visit(MarkerAnnotation node) " + node.getTypeName());
		node.accept(new MarkerAnnotationVisitor(node, new EvaluationContext(evaluationContext), emitter));
		return false;
	}
	
	@Override
	public boolean visit(SingleMemberAnnotation node) {
		System.out.println("SnippetAnnotationVisitor#visit(SingleMemberAnnotation node) " + node.getTypeName());
		node.accept(new SingleMemberAnnotationVisitor(node, new EvaluationContext(evaluationContext),emitter));
		return false;
	}
	
	@Override
	public boolean visit(MemberValuePair node) {
		System.out.println("SnippetAnnotationVisitor#visit(MemberValuePair node) " + node.getName() + " = " + node.getValue());
		return true;
	}
	
	@Override
	public boolean visit(SimpleName node) {
		System.out.println("SnippetAnnotationVisitor#visit(SimpleName node) " + node.getIdentifier());
		return true;
	}
	
	@Override
	public boolean visit(QualifiedName node) {
		System.out.println("SnippetAnnotationVisitor#visit(QualifiedName node) " + node.getName());
		return true;
	}
	
	@Override
	public boolean visit(ConditionalExpression node) {
		System.out.println("SnippetAnnotationVisitor#visit(ConditionalExpression node) " + node.getExpression().toString());
		return true;
	}
	
	@Override
	public boolean visit(StringLiteral node) {
		System.out.println("SnippetAnnotationVisitor#visit(StringLiteral node) " + node.getEscapedValue());
		return true;
	}
	
	@Override
	public boolean visit(ArrayInitializer node) {
		System.out.println("SnippetAnnotationVisitor#visit(ArrayInitializer node) " + node.toString());
		return true;
	}
}
