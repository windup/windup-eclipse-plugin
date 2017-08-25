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

import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wst.xml.core.internal.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.editor.ElementAttributesContainer;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.ClassAttributeRow;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.RulesetConstants;
import org.jboss.tools.windup.ui.internal.rules.delegate.AnnotationUtil.EvaluationContext;
import org.jboss.tools.windup.ui.internal.rules.delegate.AnnotationUtil.IAnnotationEmitter;
import org.jboss.windup.ast.java.data.TypeReferenceLocation;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.common.base.Objects;

@SuppressWarnings({"restriction"})
public class JavaClassDelegate extends ElementUiDelegate {
	
	enum JAVA_CLASS_REFERENCE_LOCATION {
		
		ANNOTATION(TypeReferenceLocation.ANNOTATION.toString(), "A Java class references the annotation."),
		CATCH_EXCEPTION_STATEMENT(TypeReferenceLocation.CATCH_EXCEPTION_STATEMENT.toString(), "A Java class method catches the specified type."),
		CONSTRUCTOR_CALL(TypeReferenceLocation.CONSTRUCTOR_CALL.toString(), "A Java class constructs the specified type."),
		ENUM_CONSTANT(TypeReferenceLocation.ENUM_CONSTANT.toString(), "A Java class declares the enumeration."),
		FIELD_DECLARATION(TypeReferenceLocation.FIELD_DECLARATION.toString(), "A Java class declares a field of the specified type."),
		IMPLEMENTS_TYPE(TypeReferenceLocation.IMPLEMENTS_TYPE.toString(), "A Java class implements the specified type; works transitively."),
		IMPORT(TypeReferenceLocation.IMPORT.toString(), "A Java class imports the type."), 
		INHERITANCE(TypeReferenceLocation.INHERITANCE.toString(), "A Java class inherits the specified type; works transitively."),
		INSTANCE_OF(TypeReferenceLocation.INSTANCE_OF.toString(), "A Java class of the specified type is used in an instanceof statement."),
		METHOD(TypeReferenceLocation.METHOD.toString(), "A Java class declares the referenced method."),
		METHOD_CALL(TypeReferenceLocation.METHOD_CALL.toString(), "A Java class calls the specified method; works transitively for interfaces."),
		METHOD_PARAMETER(TypeReferenceLocation.METHOD_PARAMETER.toString(), "A Java class declares the referenced method parameter."),
		RETURN_TYPE(TypeReferenceLocation.RETURN_TYPE.toString(), "A Java class returns the specified type."),
		TAGLIB_IMPORT(TypeReferenceLocation.TAGLIB_IMPORT.toString(), "This is only relevant for JSP sources and represents the import of a taglib into the JSP source file."),
		THROW_STATEMENT(TypeReferenceLocation.THROW_STATEMENT.toString(), "A method in the Java class throws the an instance of the specified type."),
		THROWS_METHOD_DECLARATION(TypeReferenceLocation.THROWS_METHOD_DECLARATION.toString(), "A Java class declares that it may throw the specified type."),
		TYPE(TypeReferenceLocation.TYPE.toString(), "A Java class declares the type."),
		VARIABLE_DECLARATION(TypeReferenceLocation.VARIABLE_DECLARATION.toString(), "A Java class declares a variable of the specified type."),
		VARIABLE_INITIALIZER(TypeReferenceLocation.VARIABLE_INITIALIZER.toString(), "A variable initalization expression value.");
		
		private String label;
		private String description;
		
		JAVA_CLASS_REFERENCE_LOCATION(String label, String description) {
			this.label = label;
			this.description = description;
		}
		
		public String getLabel() {
			return label;
		}
		
		public String getDescription() {
			return description;
		}
	}
	
	public void generateAnnotationElements(Annotation annotation, EvaluationContext evaluationContext) {
		IAnnotationEmitter emitter = new IAnnotationEmitter() {
			@Override
			public void emitSingleValue(String value, EvaluationContext evaluationContext) {
				detailsTab.createAnnotationLiteral(value, (Element)evaluationContext.getElement());
			}
			@Override
			public void emitMemberValuePair(String name, String value, EvaluationContext evaluationContext) {
				detailsTab.createAnnotationLiteral(name, value, (Element)evaluationContext.getElement());
			}
			
			@Override
			public void emitBeginMemberValuePairArrayInitializer(String name, EvaluationContext evaluationContext) {
				Node annotationList = detailsTab.createAnnotationList(name, (Element)evaluationContext.getElement());
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
				Node annotationList = detailsTab.createAnnotationList(null, (Element)evaluationContext.getElement());
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
					boolean initialized = isJavaclassInitialized(element);
					if (!initialized) {
						detailsTab.initialize(annotationName, element);
						evaluationContext.setElement(element);
					}
					else if (!evaluationContext.isInitialized()){
						Node anntotationTypeNode = detailsTab.createAnnotationType(annotationName, element);
						evaluationContext.setElement(anntotationTypeNode);
					}
				}
				else {
					Element parent = (Element)evaluationContext.getElement();
					Node anntotationTypeNode = detailsTab.createAnnotationType(annotationName, parent);
					evaluationContext.setElement(anntotationTypeNode);
				}
			}
		};
		annotation.accept(new SnippetAnnotationVisitor(emitter, evaluationContext));
	}
	
	private boolean isJavaclassInitialized(Element element) {
		if (element.getAttribute(RulesetConstants.JAVA_CLASS_REFERENCES).isEmpty() && 
				element.getElementsByTagName(RulesetConstants.JAVA_CLASS_LOCATION).getLength() == 0) {
			return false;
		}
		return true;
	}
	
	private ScrolledForm topContainer;
	private DetailsTab detailsTab;
	
	@Override
	public void update() {
		detailsTab.update();
		topContainer.reflow(true);
	}
	
	@Override
	public Control getControl() {
		if (topContainer == null) {
			topContainer = toolkit.createScrolledForm(parent);
			GridLayoutFactory.fillDefaults().spacing(0, 0).applyTo(topContainer.getForm().getBody());
			createTabs();
		}
		return topContainer;
	}
	
	@Override
	protected <T> TabWrapper addTab(Class<T> clazz) {
		IEclipseContext child = createTabContext(topContainer.getBody());
		T obj = create(clazz, child);
		return new TabWrapper(obj, child, null);
	}
	
	protected void createTabs() {
		this.detailsTab = (DetailsTab)addTab(DetailsTab.class).getObject();
	}

	@Override
	public Object[] getChildren() {
		return super.getChildren();
	}
	
	public static class DetailsTab extends ElementAttributesContainer {
		
		private JavaClassLocationContainer locationContainer;
		private JavaClassAnnotationLiteralContainer annotationLiteralContainer;
		private JavaClassAnnotationListContainer annotationListContainer;
		private JavaClassAnnotationTypeContainer annotationTypeContainer;
		
		private ClassAttributeRow javaClassReferenceRow;
		
		public void initialize(String annotationName, Element parent) {
			javaClassReferenceRow.setText(annotationName);
			locationContainer.createLocationWithAnnotationType(parent);
		}
		
		private Node createAnnotationType(String pattern, Element parent) {
			return annotationTypeContainer.createAnnotationTypeWithPattern(pattern, parent);
		}
		
		private void createAnnotationLiteral(String value, Element parent) {
			annotationLiteralContainer.createAnnotationLiteralWithValue(null, value, parent);
		}
		
		private void createAnnotationLiteral(String name, String value, Element parent) {
			annotationLiteralContainer.createAnnotationLiteralWithValue(name, value, parent);
		}
		
		private Node createAnnotationList(String name, Element parent) {
			return annotationListContainer.createAnnotationList(name, parent);
		}
		
		@PostConstruct
		@SuppressWarnings("unchecked")
		public void createControls(Composite parent) {
			Composite client = super.createSection(parent, 3, toolkit, element, ExpandableComposite.TITLE_BAR |Section.NO_TITLE_FOCUS_BOX|Section.TWISTIE, Messages.ruleElementDetails, null);
			Section section = (Section)client.getParent();
			section.setExpanded(true);

			CMElementDeclaration ed = modelQuery.getCMElementDeclaration(element);
			if (ed != null) {
				List<CMAttributeDeclaration> availableAttributeList = modelQuery.getAvailableContent(element, ed, ModelQuery.INCLUDE_ATTRIBUTES);
			    for (CMAttributeDeclaration declaration : availableAttributeList) {
				    	if (Objects.equal(declaration.getAttrName(), RulesetConstants.JAVA_CLASS_REFERENCES)) {
				    		IFile file = context.get(IFile.class);
				    		IProject project = null;
				    		if (file != null) {
				    			project = file.getProject();
				    		}
				    		javaClassReferenceRow = new ClassAttributeRow(element, declaration, project) {
				    			@Override
				    			protected Node getNode() {
				    				return findNode(element, ed, declaration);
				    			}
				    		};
						rows.add(javaClassReferenceRow);
						javaClassReferenceRow.createContents(client, toolkit, 2);
				    	}
				    	else {
				    		rows.add(ElementAttributesContainer.createTextAttributeRow(element, toolkit, declaration, client, 3));
				    	}
			    }
			    createSections(parent, section);
			}
		}
		
		private void createSections(Composite parent, Composite top) {
			
			Composite container = toolkit.createComposite(parent);
			GridLayout layout = new GridLayout();
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			container.setLayout(layout);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(container);

			locationContainer = new JavaClassLocationContainer(element, model, modelQuery, elementDeclaration, toolkit, uiDelegateFactory, context, contentHelper);
			locationContainer.createControls(container);
			
			container = toolkit.createComposite(parent);
			layout = new GridLayout();
			layout.marginHeight = 0;
			layout.marginBottom = 0;
			layout.marginWidth = 0;
			container.setLayout(layout);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(container);
			
			
			annotationLiteralContainer = new JavaClassAnnotationLiteralContainer(element, model, modelQuery, elementDeclaration, toolkit, uiDelegateFactory, context, contentHelper);
			annotationLiteralContainer.createControls(container);
			
			container = toolkit.createComposite(parent);
			layout = new GridLayout();
			layout.marginHeight = 0;
			layout.marginBottom = 0;
			layout.marginWidth = 0;
			container.setLayout(layout);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(container);
			
			annotationListContainer = new JavaClassAnnotationListContainer(element, model, modelQuery, elementDeclaration, toolkit, uiDelegateFactory, context, contentHelper);
			annotationListContainer.createControls(container);
			
			container = toolkit.createComposite(parent);
			layout = new GridLayout();
			layout.marginHeight = 0;
			layout.marginBottom = 0;
			layout.marginWidth = 0;
			container.setLayout(layout);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(container);
			
			annotationTypeContainer = new JavaClassAnnotationTypeContainer(element, model, modelQuery, elementDeclaration, toolkit, uiDelegateFactory, context, contentHelper);
			annotationTypeContainer.createControls(container);
		}
		
		@Override
		protected void bind() {
			super.bind();
			locationContainer.bind();
			annotationLiteralContainer.bind();
			annotationListContainer.bind();
			annotationTypeContainer.bind();
		}
	}
}