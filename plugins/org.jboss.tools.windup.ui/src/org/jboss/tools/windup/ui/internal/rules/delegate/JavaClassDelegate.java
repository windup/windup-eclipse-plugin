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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wst.xml.core.internal.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQueryAction;
import org.eclipse.xtext.util.Pair;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.RuleMessages;
import org.jboss.tools.windup.ui.internal.editor.ElementAttributesContainer;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.ChoiceAttributeRow;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.ClassAttributeRow;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.RulesetConstants;
import org.jboss.windup.ast.java.data.TypeReferenceLocation;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
	
	@Override
	protected boolean shouldFilterElementInsertAction(ModelQueryAction action) {
		return false;
	}
	
	@Override
	protected void createTabs() {
		addTab(DetailsTab.class);
	}

	@Override
	public Object[] getChildren() {
		return new Object[] {};
	}
	
	public static class DetailsTab extends ElementAttributesContainer {
		
		@PostConstruct
		@SuppressWarnings("unchecked")
		public void createControls(Composite parent, CTabItem item) {
			item.setText(Messages.ruleElementDetails);
			Composite client = super.createSection(parent, 3);
			Section section = (Section)client.getParent();
			//String oldDescription = section.getDescription();
			section.setDescription(RuleMessages.javaclass_description);
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
				    		ClassAttributeRow row = new ClassAttributeRow(element, declaration, project) {
				    			@Override
				    			protected Node getNode() {
				    				return findNode(element, ed, declaration);
				    			}
				    		};
						rows.add(row);
						row.createContents(client, toolkit, 2);
				    	}
				    	else {
				    		rows.add(ElementAttributesContainer.createTextAttributeRow(element, toolkit, declaration, client, 3));
				    	}
			    }
			    createLocationSection(parent);
			}
		}
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private CMElementDeclaration getLocationCmNode() {
			List candidates = modelQuery.getAvailableContent(element, elementDeclaration, 
					ModelQuery.VALIDITY_STRICT);
			Optional<CMElementDeclaration> found = candidates.stream().filter(candidate -> {
				if (candidate instanceof CMElementDeclaration) {
					return RulesetConstants.JAVA_CLASS_LOCATION.equals(((CMElementDeclaration)candidate).getElementName());
				}
				return false;
			}).findFirst();
			if (found.isPresent()) {
				return found.get();
			}
			return null;
		}
		
		private void createLocationSection(Composite parent) {
			Pair<Section, Composite> result = super.createScrolledSection(parent,RuleMessages.javaclass_locationSectionTitle, RuleMessages.javaclass_locationDescription,
					ExpandableComposite.TITLE_BAR | Section.DESCRIPTION | Section.NO_TITLE_FOCUS_BOX | Section.TWISTIE);
			Section section = result.getFirst();
			Composite client = result.getSecond();
			GridLayoutFactory.fillDefaults().numColumns(2).applyTo(client);
			CMNode cmNode = getLocationCmNode();
			ChoiceAttributeRow row = createLocationRow(cmNode);
			rows.add(row);
			row.createContents(client, toolkit, 2);
		}
		
		private ChoiceAttributeRow createLocationRow(CMNode cmNode) {
			return new ChoiceAttributeRow(element, cmNode, true) {
				
				@Override
				protected Node getNode() {
					NodeList list = element.getElementsByTagName(RulesetConstants.JAVA_CLASS_LOCATION);
					if (list.getLength() > 0) {
						return list.item(0);
					}
					return null;
				}
				
				@Override
				protected List<String> getOptions() {
					return Arrays.stream(JAVA_CLASS_REFERENCE_LOCATION.values()).map(e -> computeUiValue(e)).
							collect(Collectors.toList());
				}
				
				private String computeUiValue(JAVA_CLASS_REFERENCE_LOCATION location) {
					return location.getLabel() + " - " + location.getDescription();
				}

				@Override
				protected String modelToDisplayValue(String modelValue) {
					if (modelValue == null || modelValue.isEmpty()) {
						return "";
					}
					
					Optional<JAVA_CLASS_REFERENCE_LOCATION> location = Arrays.stream(JAVA_CLASS_REFERENCE_LOCATION.values()).filter(e -> {
						return Objects.equal(e.getLabel(), modelValue);
					}).findFirst();
					
					if (location.isPresent()) {
						return computeUiValue(location.get());
					}
					
					return "";
				}
				
				@Override
				protected String displayToModelValue(String uiValue) {
					if (uiValue.isEmpty()) {
						return "";
					}
					
					Optional<JAVA_CLASS_REFERENCE_LOCATION> location = Arrays.stream(JAVA_CLASS_REFERENCE_LOCATION.values()).filter(e -> {
						return Objects.equal(uiValue, computeUiValue(e));
					}).findFirst();
					
					if (location.isPresent()) {
						return location.get().getLabel();
					}
					
					return "";
				}
			};
		}
	}
}