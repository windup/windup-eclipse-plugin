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
package org.jboss.tools.windup.ui.internal.editor;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;
import org.eclipse.wst.xml.core.internal.contentmodel.util.CMDescriptionBuilder;
import org.eclipse.wst.xml.core.internal.contentmodel.util.DOMContentBuilder;
import org.eclipse.wst.xml.core.internal.contentmodel.util.DOMContentBuilderImpl;
import org.eclipse.wst.xml.core.internal.contentmodel.util.DOMNamespaceHelper;
import org.eclipse.wst.xml.ui.internal.XMLUIMessages;
import org.eclipse.wst.xml.ui.internal.actions.NodeAction;
import org.eclipse.wst.xml.ui.internal.editor.CMImageUtil;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.services.RulesetDOMService;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.collect.Lists;

@SuppressWarnings({"restriction"})
public class AddNodeAction extends NodeAction {
	protected CMNode cmnode;
	protected String description;
	protected int index;
	protected int nodeType;
	protected Node parent;
	protected String undoDescription;
	
	protected IStructuredModel model;
	protected RulesSectionContentProvider provider = new RulesSectionContentProvider();

	protected List<Node> result = Lists.newArrayList();
	
	public AddNodeAction(IStructuredModel model, CMNode cmnode, Node parent, int index) {
		this.model = model;
		this.cmnode = cmnode;
		this.parent = parent;
		this.index = index;

		String text = getLabel(parent, cmnode);
		setText(text);
		description = text;
		undoDescription = XMLUIMessages._UI_MENU_ADD + " " + text; //$NON-NLS-1$ 
		ImageDescriptor descriptor = CMImageUtil.getImageDescriptor(cmnode);
		if (descriptor == null) {
			//descriptor = imageDescriptorCache.getImageDescriptor(cmnode);
			descriptor = WindupUIPlugin.getImageDescriptor(WindupUIPlugin.IMG_XML_RULE);
		}
		setImageDescriptor(descriptor);
	}

	public AddNodeAction(IStructuredModel model, int nodeType, Node parent, int index) {
		this.model = model;
		this.nodeType = nodeType;
		this.index = index;
		this.parent = parent;
		switch (nodeType) {
			case Node.COMMENT_NODE : {
				description = XMLUIMessages._UI_MENU_COMMENT;
				undoDescription = XMLUIMessages._UI_MENU_ADD_COMMENT;
				break;
			}
			case Node.PROCESSING_INSTRUCTION_NODE : {
				description = XMLUIMessages._UI_MENU_PROCESSING_INSTRUCTION;
				undoDescription = XMLUIMessages._UI_MENU_ADD_PROCESSING_INSTRUCTION;
				break;
			}
			case Node.CDATA_SECTION_NODE : {
				description = XMLUIMessages._UI_MENU_CDATA_SECTION;
				undoDescription = XMLUIMessages._UI_MENU_ADD_CDATA_SECTION;
				break;
			}
			case Node.TEXT_NODE : {
				description = XMLUIMessages._UI_MENU_PCDATA;
				undoDescription = XMLUIMessages._UI_MENU_ADD_PCDATA;
				break;
			}
		}
		setText(description);
		//setImageDescriptor(imageDescriptorCache.getImageDescriptor(new Integer(nodeType)));
		setImageDescriptor(WindupUIPlugin.getImageDescriptor(WindupUIPlugin.IMG_XML_RULE));
	}
	
	public void beginNodeAction(NodeAction action) {
		model.beginRecording(action, action.getUndoDescription());
	}
	
	public void endNodeAction(NodeAction action) {
		model.endRecording(action);
	}
	
	@SuppressWarnings("unchecked")
	public void insert(Node parent, CMNode cmnode, int index) {
		Document document = parent.getNodeType() == Node.DOCUMENT_NODE ? (Document) parent : parent.getOwnerDocument();
		DOMContentBuilder builder = createDOMContentBuilder(document);
		builder.setBuildPolicy(DOMContentBuilder.BUILD_ONLY_REQUIRED_CONTENT);
		builder.build(parent, cmnode);
		insertNodesAtIndex(parent, builder.getResult(), index);
	}
	
	public void insertNodesAtIndex(Node parent, List<Node> list, int index) {
		insertNodesAtIndex(parent, list, index, true);
	}
	
	protected boolean isWhitespaceTextNode(Node node) {
		return (node != null) && (node.getNodeType() == Node.TEXT_NODE) && (node.getNodeValue().trim().length() == 0);
	}
	
	public void insertNodesAtIndex(Node parent, List<Node> list, int index, boolean format) {
		NodeList nodeList = parent.getChildNodes();
		if (index == -1) {
			index = nodeList.getLength();
		}
		Node refChild = (index < nodeList.getLength()) ? nodeList.item(index) : null;

		// here we consider the case where the previous node is a 'white
		// space' Text node
		// we should really do the insert before this node
		//
		int prevIndex = index - 1;
		Node prevChild = (prevIndex < nodeList.getLength()) ? nodeList.item(prevIndex) : null;
		if (isWhitespaceTextNode(prevChild)) {
			refChild = prevChild;
		}

		for (Iterator<?> i = list.iterator(); i.hasNext();) {
			Node newNode = (Node) i.next();

			if (newNode.getNodeType() == Node.ATTRIBUTE_NODE) {
				Element parentElement = (Element) parent;
				parentElement.setAttributeNode((Attr) newNode);
			}
			else {
				parent.insertBefore(newNode, refChild);
			}
		}

		boolean formatDeep = false;
		for (Iterator<?> i = list.iterator(); i.hasNext();) {
			Node newNode = (Node) i.next();
			if (newNode.getNodeType() == Node.ELEMENT_NODE) {
				formatDeep = true;
			}

			if (format) {
				RulesetDOMService.format(model, /* newNode */ newNode.getParentNode(), formatDeep);
			}
		}
		result.addAll(list);
	}
	
	public List<Node> getResult() {
		return result;
	}
	
	public DOMContentBuilder createDOMContentBuilder(Document document) {
		DOMContentBuilderImpl builder = new DOMContentBuilderImpl(document);
		return builder;
	}

	protected void addNodeForCMNode() {
		if (parent != null) {
			insert(parent, cmnode, index);
		}
	}

	protected void addNodeForNodeType() {
		Document document = parent.getNodeType() == Node.DOCUMENT_NODE ? (Document) parent : parent.getOwnerDocument();
		Node newChildNode = null;
		boolean format = true;
		switch (nodeType) {
			case Node.COMMENT_NODE : {
				newChildNode = document.createComment(XMLUIMessages._UI_COMMENT_VALUE);
				break;
			}
			case Node.PROCESSING_INSTRUCTION_NODE : {
				newChildNode = document.createProcessingInstruction(XMLUIMessages._UI_PI_TARGET_VALUE, XMLUIMessages._UI_PI_DATA_VALUE);
				break;
			}
			case Node.CDATA_SECTION_NODE : {
				newChildNode = document.createCDATASection(""); //$NON-NLS-1$
				break;
			}
			case Node.TEXT_NODE : {
				format = false;
				newChildNode = document.createTextNode(parent.getNodeName());
				break;
			}
		}

		if (newChildNode != null) {
			insertNodesAtIndex(parent, Lists.newArrayList(newChildNode), index, format);
		}
	}

	public String getUndoDescription() {
		return undoDescription;
	}

	public void run() {
		if (validateEdit(model, Display.getDefault().getActiveShell())) {
			beginNodeAction(this);
			if (cmnode != null) {
				addNodeForCMNode();
			}
			else {
				addNodeForNodeType();
			}
			endNodeAction(this);
		}
	}
	
	public String getLabel(Node parent, CMNode cmnode) {
		String result = "?" + cmnode + "?"; //$NON-NLS-1$ //$NON-NLS-2$
		if (cmnode != null) {
			// https://bugs.eclipse.org/bugs/show_bug.cgi?id=155800
			if (cmnode.getNodeType() == CMNode.ELEMENT_DECLARATION){
				result = DOMNamespaceHelper.computeName(cmnode, parent, null);
			}
			else{
				result = cmnode.getNodeName();
			}				
			if(result == null) {
				result = (String) cmnode.getProperty("description"); //$NON-NLS-1$
			}
			if (result == null || result.length() == 0) {
				if (cmnode.getNodeType() == CMNode.GROUP) {
					CMDescriptionBuilder descriptionBuilder = new CMDescriptionBuilder();
					result = descriptionBuilder.buildDescription(cmnode);
				}
			}
		}
		return result;
	}
}