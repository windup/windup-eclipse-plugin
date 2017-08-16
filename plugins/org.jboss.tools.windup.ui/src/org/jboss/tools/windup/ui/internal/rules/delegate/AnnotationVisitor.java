package org.jboss.tools.windup.ui.internal.rules.delegate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IMemberValuePairBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

/**
 * A visitor for a single annotation on a java member (can be a method or a type).
 */
public class AnnotationVisitor extends ASTVisitor {

    /** the annotated member name. */
    private final String memberName;

    /** the annotated member type. */
    private final int memberType;

    /** the annotated member start position, to distinguish between overloaded methods. */
    private final int memberStartPosition;

    /** the annotated member end position, to distinguish between overloaded methods. */
    private final int memberEndPosition;

    /** the name of the annotation. */
    private final List<String> annotationNames = new ArrayList<String>();

    /** the bindings for the matching annotation. */
    private final List<JavaAnnotation> annotations = new ArrayList<JavaAnnotation>();

    /**
     * Full Constructor to resolve a single annotation from its fully qualified name.
     * 
     * @param name
     *            the member name
     * @param memberType
     *            the member type
     * @param name
     *            the annotation name
     * @throws JavaModelException
     */
    public AnnotationVisitor(final IMember member, final String annotationName) throws JavaModelException {
        super();
        this.memberName = member.getElementName();
        this.memberType = member.getElementType();
        this.memberStartPosition = member.getSourceRange().getOffset();
        this.memberEndPosition = member.getSourceRange().getOffset() + member.getSourceRange().getLength();
        this.annotationNames.add(annotationName);
    }

    /**
     * Full Constructor to resolve a multiple annotations from their fully qualified name.
     * 
     * @param name
     *            the member name
     * @param memberType
     *            the member type
     * @param name
     *            the annotation name
     * @throws JavaModelException
     */
    public AnnotationVisitor(final IMember member, final List<String> annotationNames) throws JavaModelException {
        super();
        this.memberName = member.getElementName();
        this.memberType = member.getElementType();
        this.memberStartPosition = member.getSourceRange().getOffset();
        this.memberEndPosition = member.getSourceRange().getOffset() + member.getSourceRange().getLength();
        this.annotationNames.addAll(annotationNames);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom.AnnotationTypeDeclaration)
     */
    @Override
    public final boolean visit(final AnnotationTypeDeclaration node) {
        if (memberType == IJavaElement.TYPE && node.getName().getFullyQualifiedName().equals(memberName)
                && matchesLocation(node)) {
            visitExtendedModifiers((List<?>) node.getStructuralProperty(AnnotationTypeDeclaration.MODIFIERS2_PROPERTY));
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom. TypeDeclaration)
     */
    @Override
    public final boolean visit(final TypeDeclaration node) {
        if (memberType == IJavaElement.TYPE && node.getName().getFullyQualifiedName().equals(memberName)
                && matchesLocation(node)) {
            visitExtendedModifiers((List<?>) node.getStructuralProperty(TypeDeclaration.MODIFIERS2_PROPERTY));
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom. MethodDeclaration)
     */
    @Override
    public final boolean visit(final MethodDeclaration node) {
        if (memberType == IJavaElement.METHOD && node.getName().getFullyQualifiedName().equals(memberName)
                && matchesLocation(node)) {
            visitExtendedModifiers((List<?>) node.getStructuralProperty(MethodDeclaration.MODIFIERS2_PROPERTY));
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse.jdt.core.dom. MethodDeclaration)
     */
    @Override
    public final boolean visit(final FieldDeclaration node) {
        if (memberType == IJavaElement.FIELD) {
            VariableDeclarationFragment fragment = (VariableDeclarationFragment) (node.fragments().get(0));
            if (fragment.getName().toString().equals(memberName) && matchesLocation(node)) {
                visitExtendedModifiers((List<?>) node.getStructuralProperty(FieldDeclaration.MODIFIERS2_PROPERTY));
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the given node matches the expected member location by comparing start and end positions.
     * 
     * @param node
     * @return
     */
    private boolean matchesLocation(final ASTNode node) {
        return node.getStartPosition() >= this.memberStartPosition
                && (node.getStartPosition() + node.getLength()) <= this.memberEndPosition;
    }

    /**
     * Visits the modifiers.
     * 
     * @param modifiers
     *            the modifiers
     */
    private void visitExtendedModifiers(final List<?> modifiers) {
        for (Object modifier : modifiers) {
            if (modifier instanceof org.eclipse.jdt.core.dom.Annotation) {
                IAnnotationBinding annotationBinding = ((org.eclipse.jdt.core.dom.Annotation) modifier)
                        .resolveAnnotationBinding();
                if (annotationBinding != null) {
                    final String qualifiedName = annotationBinding.getAnnotationType().getQualifiedName();
                    final String name = annotationBinding.getAnnotationType().getName();
                    if (annotationNames.contains(qualifiedName) || annotationNames.contains(name)) {
                        annotations.add(toAnnotation(annotationBinding));
                    }
                }
            }
        }
    }

    /**
     * Returns the Annotation element matching the annotation name given in the visitor constructor. This method should
     * only be called when the constructor with a single annotation name was used.
     * 
     * @return the annotation found on the target java element
     * @throws JavaModelException
     *             in case of underlying exception
     */
    public final JavaAnnotation getResolvedAnnotation() throws JavaModelException {
        assert annotationNames.size() == 1;
        if (annotations.size() == 0) {
            return null;
        }
        return annotations.get(0);
    }

    /**
     * Returns the Annotation elements matching the annotations name given in the visitor constructor. The matching
     * annotations are indexed by their associated Java type's fully qualified names. This method should only be called
     * when the constructor with multiple annotation names was used.
     * 
     * @return the annotation found on the target java element
     * @throws JavaModelException
     *             in case of underlying exception
     */
    public final Map<String, JavaAnnotation> getResolvedAnnotations() throws JavaModelException {
        final Map<String, JavaAnnotation> resolvedJavaAnnotations = new HashMap<String, JavaAnnotation>();
        for (JavaAnnotation annotation : annotations) {
            resolvedJavaAnnotations.put(annotation.getFullyQualifiedName(), annotation);
        }
        return resolvedJavaAnnotations;
    }

    /**
     * Creates a instance of {@link JavaAnnotation} from the given annotation binding.
     * @param annotationBinding
     * @return
     */
    public static JavaAnnotation toAnnotation(final IAnnotationBinding annotationBinding) {
        return toAnnotation(annotationBinding, (IAnnotation) annotationBinding.getJavaElement());
    }
    
    /**
     * Creates a instance of {@link Annotation} from the given annotation binding, specifically using the given javaAnnotation instead of the one that could be retrieved from the binding.
     * @param annotationBinding
     * @param javaAnnotation
     * @return
     */
    public static JavaAnnotation toAnnotation(IAnnotationBinding annotationBinding, IAnnotation javaAnnotation) {
        final String annotationName = annotationBinding.getAnnotationType().getQualifiedName();
        final Map<String, List<String>> annotationElements = resolveAnnotationElements(annotationBinding);
        return new JavaAnnotation(javaAnnotation, annotationName, annotationElements);
    }
    
    public static Map<String, List<String>> resolveAnnotationElements(IAnnotationBinding annotationBinding) {
        final Map<String, List<String>> annotationElements = new HashMap<String, List<String>>();
        try {
            for (IMemberValuePairBinding binding : annotationBinding.getAllMemberValuePairs()) {
                final List<String> values = new ArrayList<String>();
                if(binding.getValue() != null) {
                    if (binding.getValue() instanceof Object[]) {
                    for (Object v : (Object[]) binding.getValue()) {
                        values.add(toString(v));
                    }
                } else {
                    values.add(toString(binding.getValue()));
                }
                }
                annotationElements.put(binding.getName(), values);
            }
            // if the code is not valid, the underlying DefaultValuePairBinding
            // may throw a NPE:
            // at
            // org.eclipse.jdt.core.dom.DefaultValuePairBinding.<init>(DefaultValuePairBinding.java:31)
            // at
            // org.eclipse.jdt.core.dom.AnnotationBinding.getAllMemberValuePairs(AnnotationBinding.java:98)
        } catch (Throwable e) {
            // silently ignore
        }
        return annotationElements;
    }
    
    /**
     * Converts the given value into String. The actual types that are supported are:
     * java.lang.Class - the ITypeBinding for the class object
     * java.lang.String - the string value itself
     * enum type - the IVariableBinding for the enum constant
     * annotation type - an IAnnotationBinding
     * for other types, the <code>java.lang.Object{@link #toString()}</code> method is used.
     * @param value
     * @return litteral value
     */
    public static String toString(Object value) {
        if(value instanceof ITypeBinding) {
            return ((ITypeBinding)value).getQualifiedName();
        } else if(value instanceof IVariableBinding) {
            return ((IVariableBinding)value).getName();
        } else if(value instanceof IAnnotationBinding) {
            return ((IAnnotationBinding)value).getName();
        } 
        return value.toString();
    }
}