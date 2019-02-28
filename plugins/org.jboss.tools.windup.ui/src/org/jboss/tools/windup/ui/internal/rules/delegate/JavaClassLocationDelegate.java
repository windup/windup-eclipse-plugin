/*******************************************************************************
 * Copyright (c) 2019 Red Hat, Inc.
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

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;
import org.jboss.tools.windup.ui.internal.RuleMessages;
import org.jboss.tools.windup.ui.internal.editor.ElementAttributesContainer;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.ChoiceAttributeRow;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.NodeRow;
import org.jboss.tools.windup.ui.internal.rules.delegate.JavaClassDelegate.JAVA_CLASS_REFERENCE_LOCATION;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.common.base.Objects;

@SuppressWarnings({"restriction"})
public class JavaClassLocationDelegate extends ElementUiDelegate {
	
	protected void createTabs() {
		addTab(DetailsTab.class);
	}

	@Override
	public Object[] getChildren() {
		return super.getChildren();
	}
	
	private ChoiceAttributeRow createLocationRow(CMNode cmNode, Element listElement) {
		return new ChoiceAttributeRow(listElement, cmNode, true) {
			@Override
			protected Control createLabel(Composite parent, FormToolkit toolkit) {
				return null;
			}
			
			@Override
			protected Node getNode() {
				return listElement;
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
	
	public void createControls(Composite parent, Element element, CMElementDeclaration ed, List<NodeRow> rows) {
    		ChoiceAttributeRow row = createLocationRow(ed, element);
    		row.createContents(parent, toolkit, 2);
	  	rows.add(row);
	}
	
	public static class DetailsTab extends ElementAttributesContainer {
		
		@PostConstruct
		public void createControls(Composite parent) {
			Composite client = super.createSection(parent, 2, RuleMessages.javaclass_locationSectionTitle, RuleMessages.javaclass_locationDescription);
			CMElementDeclaration ed = modelQuery.getCMElementDeclaration(element);
			if (ed != null) {
				uiDelegate.createControls(client, element, ed, rows);
			}
		}
	}
}