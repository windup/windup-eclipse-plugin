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
import org.eclipse.jdt.internal.debug.ui.JDISourceViewer;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wst.xml.core.internal.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.RuleMessages;
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
	
	private static final int SASH_LEFT_DEFAULT = 550;
	private static final int SASH_RIGHT_DEFAULT = 400;
	
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
	
	private Composite containerControl;
	private SashForm sash;
	
	private DetailsTab detailsTab;
	
	private JavaEmbeddedEditor annotationEditor;
	
	@Override
	public void update() {
		detailsTab.update();
	}
	
	@Override
	public Control getControl() {
		if (containerControl == null) {
			containerControl = createContainerControl(parent);
			createControls(containerControl);
		}
		return containerControl;
	}
	
	private Composite createContainerControl(Composite parent) {
		Composite container = toolkit.createComposite(parent);
		//container.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GREEN));
		GridDataFactory.fillDefaults().grab(true, true).applyTo(container);
//		FormLayout layout = new FormLayout();
//		layout.spacing = 5;
//		container.setLayout(layout);
		GridLayoutFactory.fillDefaults().applyTo(container);
		return container;
	}
	
	private void createControls(Composite parent) {
		
		this.sash = new SashForm(parent, SWT.SMOOTH|SWT.VERTICAL);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(sash);
		sash.setOrientation(SWT.HORIZONTAL);
		sash.setFont(parent.getFont());
		sash.setVisible(true);
		
		Composite leftContainer = toolkit.createComposite(sash);
		GridLayoutFactory.fillDefaults().applyTo(leftContainer);
		//leftContainer.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
		FormData leftData = new FormData();
		leftData.top = new FormAttachment(null);
		leftData.left = new FormAttachment(0);
		leftData.bottom = new FormAttachment(100);
		leftContainer.setLayoutData(leftData);
		
		IEclipseContext context = super.createTabContext(leftContainer);
		detailsTab = super.create(DetailsTab.class, context);
		
		Composite rightContainer = toolkit.createComposite(sash);
		GridLayoutFactory.fillDefaults().applyTo(rightContainer);
		//rightContainer.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_YELLOW));
		FormData rightData = new FormData();
		rightData.top = new FormAttachment(null);
		rightData.bottom = new FormAttachment(100);
		rightData.left = new FormAttachment(leftContainer);
		rightData.right = new FormAttachment(100);
		rightContainer.setLayoutData(rightData);
		
		leftData.right = new FormAttachment(50);
		
		createJavaEditor(rightContainer);
		
		sash.setWeights(new int[]{SASH_LEFT_DEFAULT, SASH_RIGHT_DEFAULT});
	}
	
	private void createJavaEditor(Composite parent) {
		parent.setLayout(new FormLayout());
		Composite client = ElementDetailsSection.createSection(parent, 3, toolkit, null);
		Section section = (Section)client.getParent();
		section.setText("Java Source Code"); //$NON-NLS-1$
		FormData data = new FormData();
		data.top = new FormAttachment(0);
		data.left = new FormAttachment(0);
		data.right = new FormAttachment(100);
		data.bottom = new FormAttachment(100);
		section.setLayoutData(data);
		
		createAnnotationSourceSectionToolbar(section);
		
		this.annotationEditor = new JavaEmbeddedEditor(client);
	}
	
	private void createAnnotationSourceSectionToolbar(Section section) {
		ToolBar toolbar = new ToolBar(section, SWT.FLAT|SWT.HORIZONTAL);
		ToolItem addItem = new ToolItem(toolbar, SWT.PUSH);
		addItem.setImage(WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_NEW_ANNOTATION));
		addItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				generateAnnotation(annotationEditor.getSourceViewer());
			}
		});
		section.setTextClient(toolbar);
	}
	
	private void generateAnnotation(JDISourceViewer sourceViewer) {
		IDocument document = sourceViewer.getDocument();
		String annotationSource = document.get();
		Annotation annotation = AnnotationUtil.getAnnotationElement(annotationSource);
		generateAnnotationElements(annotation, new EvaluationContext(null));
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
	
	protected void createTabs() {
		addTab(DetailsTab.class);
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
		public void createControls(Composite parent/*, CTabItem item*/) {
			//item.setText(Messages.ruleElementDetails);
			parent.setLayout(new FormLayout());
			Composite client = super.createSection(parent, 3);
			Section section = (Section)client.getParent();
			section.setDescription(RuleMessages.javaclass_description);
			FormData data = new FormData();
			data.left = new FormAttachment(0);
			data.right = new FormAttachment(100);
			section.setLayoutData(data);
			
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
		
		private void createSections(Composite parent, Section top) {
			locationContainer = new JavaClassLocationContainer(element, model, modelQuery, elementDeclaration, toolkit, uiDelegateFactory, context, contentHelper);
			Section section = locationContainer.createControls(parent);
			FormData data = new FormData();
			data.top = new FormAttachment(top);
			data.left = new FormAttachment(0);
			data.right = new FormAttachment(100);
			section.setLayoutData(data);
			
			data = new FormData();
			data.top = new FormAttachment(section);
			annotationLiteralContainer = new JavaClassAnnotationLiteralContainer(element, model, modelQuery, elementDeclaration, toolkit, uiDelegateFactory, context, contentHelper);
			section = annotationLiteralContainer.createControls(parent);
			data.left = new FormAttachment(0);
			data.right = new FormAttachment(100);
			section.setLayoutData(data);
			
			data = new FormData();
			data.top = new FormAttachment(section);
			annotationListContainer = new JavaClassAnnotationListContainer(element, model, modelQuery, elementDeclaration, toolkit, uiDelegateFactory, context, contentHelper);
			section = annotationListContainer.createControls(parent);
			data.left = new FormAttachment(0);
			data.right = new FormAttachment(100);
			section.setLayoutData(data);
			
			data = new FormData();
			data.top = new FormAttachment(section);
			annotationTypeContainer = new JavaClassAnnotationTypeContainer(element, model, modelQuery, elementDeclaration, toolkit, uiDelegateFactory, context, contentHelper);
			section = annotationTypeContainer.createControls(parent);
			data.left = new FormAttachment(0);
			data.right = new FormAttachment(100);
			section.setLayoutData(data);
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