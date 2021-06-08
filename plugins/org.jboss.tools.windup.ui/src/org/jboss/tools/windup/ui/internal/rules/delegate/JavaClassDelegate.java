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
package org.jboss.tools.windup.ui.internal.rules.delegate;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationListener;
import org.eclipse.jface.viewers.ColumnViewerEditorDeactivationEvent;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.pde.internal.ui.PDEPluginImages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQueryAction;
import org.jboss.tools.windup.runtime.options.TypeReferenceLocation;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.RuleMessages;
import org.jboss.tools.windup.ui.internal.editor.DeleteNodeAction;
import org.jboss.tools.windup.ui.internal.editor.ElementAttributesContainer;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.ClassAttributeRow;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.IElementUiDelegate;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.RulesetConstants;
import org.jboss.tools.windup.ui.internal.rules.RulesetEditor;
import org.jboss.tools.windup.ui.internal.rules.annotation.AnnotationContentProvider;
import org.jboss.tools.windup.ui.internal.rules.annotation.AnnotationElement;
import org.jboss.tools.windup.ui.internal.rules.annotation.AnnotationElement.AttributeElement;
import org.jboss.tools.windup.ui.internal.rules.annotation.AnnotationModel;
import org.jboss.tools.windup.ui.internal.services.AnnotationService;
import org.jboss.tools.windup.ui.internal.services.RulesetDOMService;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

@SuppressWarnings({"restriction"})
public class JavaClassDelegate extends ElementUiDelegate {
	
	public static enum JAVA_CLASS_REFERENCE_LOCATION {
		
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
	
	@Override
	protected boolean shouldFilterElementInsertAction(ModelQueryAction action) {
		return true;
	}
	
	protected void createTabs() {
		addTab(DetailsTab.class);
	}

	@Override
	public Object[] getChildren() {
		return new Object[] {};
	}
	
	public static class DetailsTab extends ElementAttributesContainer {
		
		@Inject private RulesetDOMService domService;
		@Inject private RulesetEditor editor;
		
		private AnnotationModel annotationModel;
		private TreeViewer annotationTree;
		
		private JavaClassLocationContainer locationContainer;
		
		private ClassAttributeRow javaClassReferenceRow;
		
		@PostConstruct
		@SuppressWarnings("unchecked")
		public void createControls(Composite parent) {
			Composite client = super.createSection(parent, 3, toolkit, element, ExpandableComposite.TITLE_BAR |Section.NO_TITLE_FOCUS_BOX, Messages.ruleElementDetails, null);
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
			    createLocationSection(parent);
			    createAnnotationModelTree(parent, section);
			}
		}
		
		private void createLocationSection(Composite parent) {
			Composite container = toolkit.createComposite(parent);
			GridLayout layout = new GridLayout();
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			container.setLayout(layout);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(container);

			locationContainer = new JavaClassLocationContainer(element, model, modelQuery, elementDeclaration, toolkit, uiDelegateFactory, context, contentHelper);
			locationContainer.createControls(container);
		}
		
		private void createAnnotationModelTree(Composite parent, Composite top) {
			
			Composite client = super.createSection(parent, 1, toolkit, element, ExpandableComposite.TITLE_BAR |Section.NO_TITLE_FOCUS_BOX | Section.TWISTIE, "Annotation", 
					RuleMessages.annotationDescription);
			Section section = (Section)client.getParent();
			GridDataFactory.fillDefaults().grab(true, true).applyTo(section);
			section.setExpanded(true);
			
			GridLayoutFactory.fillDefaults().numColumns(2).applyTo(client);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(client);
			
			Composite left = toolkit.createComposite(client);
			GridLayoutFactory.fillDefaults().extendedMargins(0, 0, 0, 5).applyTo(left);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(left);
			annotationTree = new TreeViewer(left);
			ElementDetailsSection.addScrollListener(annotationTree.getTree());
			
			Composite right = toolkit.createComposite(client);
			GridLayoutFactory.fillDefaults().applyTo(right);
			GridDataFactory.fillDefaults().grab(false, true).applyTo(right);
			
			createToolbar(right);
			
			this.annotationModel = new AnnotationModel(element, modelQuery, model);
			AnnotationContentProvider provider = new AnnotationContentProvider();
			DelegatingStyledCellLabelProvider styleProvider = new DelegatingStyledCellLabelProvider(provider);
			annotationTree.setContentProvider(provider);
			annotationTree.setLabelProvider(styleProvider);
			annotationTree.setInput(annotationModel);
			
			FontDescriptor descriptor = FontDescriptor.createFrom(JFaceResources.getDialogFont());
			descriptor = descriptor.increaseHeight(0);
			final Font customFont = descriptor.createFont(Display.getDefault());
			descriptor = descriptor.setStyle(SWT.ITALIC);
			
			Control control = annotationTree.getControl();
			control.setFont(customFont);
			
			GridDataFactory.fillDefaults().grab(true, true).applyTo(control);
			
			createViewerContextMenu();
			
			annotationTree.expandAll();
			
			annotationTree.setColumnProperties(new String[]{"col1"});
			
			annotationTree.setCellEditors(new CellEditor[]{new TextCellEditor(annotationTree.getTree())});
			annotationTree.setCellModifier(new XMLCMCellModifier());
		}
		
		private void createToolbar(Composite parent) {
			AnnotationService annotationService = new AnnotationService();
			Button newLiteralButton = toolkit.createButton(parent, RuleMessages.javaclass_annotation_literal_sectionTitle, SWT.PUSH);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(newLiteralButton);
			newLiteralButton.setImage(WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_ADD));
			newLiteralButton.setToolTipText(RuleMessages.javaclass_annotation_literal_description);
			newLiteralButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					Element newElement = annotationService.createAnnotationLiteralWithValue(null, null, element);
					annotationTree.expandToLevel(newElement, TreeViewer.ALL_LEVELS);
					annotationTree.setSelection(new StructuredSelection(newElement), true);
				}
			});
			
			Button newTypeButton = toolkit.createButton(parent, RuleMessages.javaclass_annotation_type_sectionTitle, SWT.PUSH);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(newTypeButton);
			newTypeButton.setImage(WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_ADD));
			newTypeButton.setToolTipText(RuleMessages.javaclass_annotation_type_description);
			newTypeButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					Element newElement = annotationService.createAnnotationTypeWithPattern(null, element);
					annotationTree.expandToLevel(newElement, TreeViewer.ALL_LEVELS);
					annotationTree.setSelection(new StructuredSelection(newElement), true);
				}
			});
			
			Button newListButton = toolkit.createButton(parent, RuleMessages.javaclass_annotation_list_sectionTitle, SWT.PUSH);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(newListButton);
			newListButton.setImage(WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_ADD));
			newListButton.setToolTipText(RuleMessages.javaclass_annotation_list_description);
			newListButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					Element newElement = annotationService.createAnnotationList(null, element);
					annotationTree.expandToLevel(newElement, TreeViewer.ALL_LEVELS);
					annotationTree.setSelection(new StructuredSelection(newElement), true);
				}
			});
		}
		
		private void createViewerContextMenu() {
			MenuManager popupMenuManager = new MenuManager();
			IMenuListener listener = new IMenuListener() {
				@Override
				public void menuAboutToShow(IMenuManager mng) {
					fillContextMenu(mng);
				}
			};
			popupMenuManager.addMenuListener(listener);
			popupMenuManager.setRemoveAllWhenShown(true);
			Control control = annotationTree.getControl();
			Menu menu = popupMenuManager.createContextMenu(control);
			control.setMenu(menu);
		}
		
		@SuppressWarnings("unchecked")
		private void fillContextMenu(IMenuManager manager) {
			ISelection selection = annotationTree.getSelection();
			IStructuredSelection ssel = (IStructuredSelection) selection;
			if (!ssel.isEmpty()) {
				if (ssel.toList().size() == 1) {
					AnnotationElement annotationElement = (AnnotationElement)ssel.getFirstElement();
					Element element = annotationElement.getElement();
					IElementUiDelegate delegate = editor.getDelegate(element);
					if (delegate != null) {
						delegate.fillContextMenu(manager, annotationTree);
					}
				}
				Action deleteAction = new Action() {
					@Override
					public ImageDescriptor getImageDescriptor() {
						return PDEPluginImages.DESC_DELETE;
					}

					@Override
					public ImageDescriptor getDisabledImageDescriptor() {
						return PDEPluginImages.DESC_REMOVE_ATT_DISABLED;
					}

					@Override
					public void run() {
						List<AnnotationElement> annotationElements = ssel.toList();
						List<Element> elements = annotationElements.stream().map(e -> e.getElement()).distinct().collect(Collectors.toList());
						removeNodes(elements);
					}
				};
				deleteAction.setText(Messages.RulesetEditor_RemoveElement);
				manager.add(deleteAction);
			}
			this.annotationTree.getControl().update();
		}
		
		public void removeNodes(List<Element> elements) {
			if (elements.isEmpty()) {
				return;
			}
			IStructuredModel model = super.model;
			try {
				model.aboutToChangeModel();

				filterChildElements(elements);
			
				Node nextSelection = findElementForSelection(elements);
				
				new DeleteNodeAction(model, elements).run();
				
				if (nextSelection != null) {
					annotationTree.setSelection(new StructuredSelection(nextSelection));
				}
			}
			finally {
				model.changedModel();
			}
		}
		
		private Node findElementForSelection(List<Element> toBeDeleted) {
			if (toBeDeleted.size() > 1) {
				return null;
			}
			Element firstElement = toBeDeleted.get(0);
			Element parent = (Element)firstElement.getParentNode();
			
			Node nextSelection = domService.findNextSibling(toBeDeleted.get(toBeDeleted.size()-1), 1);
			if (nextSelection == null || toBeDeleted.contains(nextSelection)) {
				// no next node, use previous node
				nextSelection = domService.findPreviousSibling(firstElement);
			}

			if (nextSelection == null || toBeDeleted.contains(nextSelection)) {
				// next or previous null, use parent
				nextSelection = parent;
			}
			return nextSelection;
		}
		
		private void filterChildElements(List<Element> elements) {
			for (Iterator<Element> iter = Lists.newArrayList(elements).iterator(); iter.hasNext();) {
				Element element = iter.next();
				// climb parent hierarchy, and remove element if one of parents is in list.
				while (true) {
					Node parent = element.getParentNode();
					if (parent == null) {
						break;
					}
					if (elements.contains(parent)) {
						iter.remove();
						break;
					}
					parent = element.getParentNode();
					if (!(parent instanceof Element)) {
						break;
					}
					element = (Element)parent; 
				}
			}
		}
		
		@Override
		protected void bind() {
			super.bind();
			annotationTree.refresh(annotationModel, true);
			locationContainer.bind();
		}
		
		private class CellListener extends ColumnViewerEditorActivationListener implements ICellEditorListener {

			private AttributeElement attributeElement;
			private CellEditor editor;
			private String originalValue;

			CellListener(AttributeElement attributeElement, CellEditor editor) {
				this.attributeElement = attributeElement;
				this.editor = editor;
				originalValue = attributeElement.getValue();
				attributeElement.getStructuredModel().aboutToChangeModel();
			}

			public void applyEditorValue() {
				editor.removeListener(this);
			}

			public void cancelEditor() {
				final Object value = editor.getValue();
				if (value != null && !value.equals(originalValue)) {
					attributeElement.setValue(String.valueOf(originalValue));
				}
				editor.removeListener(this);
			}

			public void editorValueChanged(boolean oldValidState, boolean newValidState) {
				if (newValidState) {
					attributeElement.setValue(editor.getValue().toString());
				}
			}

			public void beforeEditorActivated(ColumnViewerEditorActivationEvent event) {
			}

			public void afterEditorActivated(ColumnViewerEditorActivationEvent event) {
			}

			public void beforeEditorDeactivated(ColumnViewerEditorDeactivationEvent event) {
			}

			public void afterEditorDeactivated(ColumnViewerEditorDeactivationEvent event) {
				attributeElement.getStructuredModel().changedModel();
				annotationTree.getColumnViewerEditor().removeEditorActivationListener(this);
			}
		}

		public class XMLCMCellModifier implements ICellModifier {
			
			public boolean canModify(Object element, String property) {
				boolean result = false;
				if (element instanceof AttributeElement) {
					/* Set up the cell editor based on the element */
					CellEditor[] editors = annotationTree.getCellEditors();
					if (editors.length > 0) {
						if (editors[0] != null)
							editors[0].dispose();
						editors[0] = createDefaultPropertyDescriptor((AttributeElement)element);
						if (editors[0] instanceof TextCellEditor) {
							final CellListener listener = new CellListener((AttributeElement)element, editors[0]);
							annotationTree.getColumnViewerEditor().addEditorActivationListener(listener);
							editors[0].addListener(listener);
							result = true;
						}
					}
				}
				return result;
			}

			public Object getValue(Object object, String property) {
				String result = null;
				if (object instanceof Node) {
					result = contentHelper.getNodeValue((Node) object);
				}
				return (result != null) ? result : ""; //$NON-NLS-1$
			}

			public void modify(Object element, String property, Object value) {
				Item item = (Item) element;
				if (item != null) {
					AttributeElement attributeElement = (AttributeElement)item.getData();
					String newValue = value.toString();
					if (newValue != null) {
						attributeElement.setValue(newValue);
					}
				}
			}

			protected CellEditor createDefaultPropertyDescriptor(AttributeElement element) {
				String attributeName = element.getCmNode().getNodeName();
				TextPropertyDescriptor descriptor = new TextPropertyDescriptor(attributeName, attributeName);
				return descriptor.createPropertyEditor(annotationTree.getTree());
			}
		}
	}
}