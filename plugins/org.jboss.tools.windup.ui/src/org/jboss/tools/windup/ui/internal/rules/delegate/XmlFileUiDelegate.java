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

import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.pde.internal.ui.editor.FormLayoutFactory;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wst.xml.core.internal.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQueryAction;
import org.jboss.tools.windup.ui.internal.editor.ElementAttributesContainer;

@SuppressWarnings("restriction")
public class XmlFileUiDelegate extends ElementUiDelegate {
	
	@Override
	protected boolean shouldFilterElementInsertAction(ModelQueryAction action) {
		return true;
	}
	
	@Override
	public Object[] getChildren() {
		return new Object[] {};
	}
	
	@Override
	protected void createTabs() {
		addTab(DetailsTab.class);
	}
	
	public static class DetailsTab extends ElementAttributesContainer {
		
		private NamespacesDelegate namespacesDelegate;
		
		@PostConstruct
		@SuppressWarnings("unchecked")
		private void createControls(Composite parent) {
			Composite mainDetailsContainer = toolkit.createComposite(parent);
			
			GridLayout layout = new GridLayout();
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			mainDetailsContainer.setLayout(layout);
		
			GridDataFactory.fillDefaults().grab(true, false).applyTo(mainDetailsContainer);
			
			Composite client = super.createSection(mainDetailsContainer, 2, toolkit, element, ExpandableComposite.TITLE_BAR |Section.NO_TITLE_FOCUS_BOX, 
					null, null);
			
			GridLayout glayout = FormLayoutFactory.createSectionClientGridLayout(false, 2);
			glayout.marginTop = 0;
			glayout.marginBottom = 5;
			client.setLayout(glayout);
			
			CMElementDeclaration ed = modelQuery.getCMElementDeclaration(element);
			if (ed != null) {
				List<CMAttributeDeclaration> availableAttributeList = modelQuery.getAvailableContent(element, ed, ModelQuery.INCLUDE_ATTRIBUTES);
			    for (CMAttributeDeclaration declaration : availableAttributeList) {
			    		rows.add(ElementAttributesContainer.createTextAttributeRow(element, toolkit, declaration, client, 2));
			    }
			    createSections(parent, mainDetailsContainer);
			}
			((Section)client.getParent()).setExpanded(true);
		}
		
		private void createSections(Composite parent, Composite detailsClient) {
			this.namespacesDelegate = createNamespaceDelegate(parent);
		}
		
		private NamespacesDelegate createNamespaceDelegate(Composite parent) {
			IEclipseContext child = context.createChild();
			child.set(Composite.class, parent);
			return ContextInjectionFactory.make(NamespacesDelegate.class, child);
		}
		
		@Override
		public void update() {
			super.update();
			namespacesDelegate.update();
		}
	}
}

