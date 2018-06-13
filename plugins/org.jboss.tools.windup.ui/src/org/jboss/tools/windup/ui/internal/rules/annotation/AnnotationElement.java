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
package org.jboss.tools.windup.ui.internal.rules.annotation;

import java.util.List;

import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.ui.internal.tabletree.TreeContentHelper;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.editor.AddNodeAction;
import org.jboss.tools.windup.ui.internal.rules.delegate.ElementUiDelegate;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

@SuppressWarnings("restriction")
public class AnnotationElement {

	protected AnnotationElement parent;
	protected Element element;
	protected TreeContentHelper contentHelper = new TreeContentHelper();
	
	protected ModelQuery modelQuery;
	protected IStructuredModel structuredModel;
	
	public AnnotationElement(Element element, ModelQuery modelQuery, IStructuredModel structuredModel) {
		this(null, element);
		this.modelQuery = modelQuery;
		this.structuredModel = structuredModel;
	}
	
	public AnnotationElement(AnnotationElement parent, Element element) {
		this.parent = parent;
		this.element = element;
	}
	
	public AnnotationModel getModel() {
		if (parent != null) {
			return getModel();
		}
		return (AnnotationModel)this;
	}
	
	public AnnotationElement getParent() {
		return parent;
	}
	
	public ModelQuery getModelQuery() {
		if (modelQuery == null) {
			return parent.getModelQuery();
		}
		return modelQuery;
	}
	
	public IStructuredModel getStructuredModel() {
		if (structuredModel == null) {
			return parent.getStructuredModel();
		}
		return structuredModel;
	}
	
	public AnnotationElement[] getChildElements() {
		return new AnnotationElement[] {};
	}
	
	protected Element getJavaclassElement() {
		if (parent != null) {
			return parent.getJavaclassElement();
		}
		return element;
	}
	
	public Element getElement() {
		if (element == null) {
			return parent.getElement();
		}
		return element;
	}
	
	@SuppressWarnings("unchecked")
	protected List<CMAttributeDeclaration> getAttributes() {
		ModelQuery modelQuery = getModelQuery();
		Element element = getElement();
		CMElementDeclaration ed = modelQuery.getCMElementDeclaration(element);
		return modelQuery.getAvailableContent(element, ed, ModelQuery.INCLUDE_ATTRIBUTES);
	}
	
	public String getText() {
		return "";
	}
	
	public Image getImage() {
		return null;
	}
	
	public StyledString getStyledText() {
		return new StyledString(getText());
	}
	
	public static class AttributeElement extends AnnotationElement {
		
		protected CMNode cmNode;
		
		public AttributeElement(AnnotationElement parent, CMNode cmNode) {
			super(parent, null);
			this.cmNode = cmNode;
		}
		
		public CMNode getCmNode() {
			return cmNode;
		}
		
		@Override
		public Image getImage() {
			return WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_ATTRIBUTE);
		}
		
		@Override
		public String getText() {
			StringBuffer buff = new StringBuffer();
			buff.append(cmNode.getNodeName());
			buff.append(" = "); //$NON-NLS-1$
			buff.append(getValue());
			return buff.toString();
		}
		
		public String getValue() {
			String result = ""; //$NON-NLS-1$
			Node node = getNode();
			if (node != null) {
				result = contentHelper.getNodeValue(node);	
			}
			return result != null ? result : ""; //$NON-NLS-1$
		}
		
		protected Node getNode() {
			CMElementDeclaration parentEd = getModelQuery().getCMElementDeclaration(getElement());
			return ElementUiDelegate.findNode(getElement(), parentEd, cmNode);
		}
		
		public void setValue(String value) {
			IStructuredModel model = getStructuredModel();
			Node node = getNode();
			if (node != null) {
				String currentValue = getValue();
				if (!value.equals(currentValue)) {
					contentHelper.setNodeValue(node, value);
				}
			}
			else {
				AddNodeAction newNodeAction = new AddNodeAction(model, cmNode, getElement(), getElement().getChildNodes().getLength());
				newNodeAction.runWithoutTransaction();
				if (!newNodeAction.getResult().isEmpty()) {
					node = (Node)newNodeAction.getResult().get(0);
					contentHelper.setNodeValue(node, value);
				}
			}
		}
	}
}
