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
package org.jboss.tools.windup.ui.internal.editor;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.ui.internal.XMLUIMessages;
import org.eclipse.wst.xml.ui.internal.actions.NodeAction;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

@SuppressWarnings({"restriction", "rawtypes"})
public class DeleteNodeAction extends NodeAction {
	protected List list;
	protected IStructuredModel model;

	@SuppressWarnings("unchecked")
	public DeleteNodeAction(IStructuredModel model, Node node) {
		setText(XMLUIMessages._UI_MENU_REMOVE);
		this.model = model;
		list = new Vector();
		list.add(node);
	}

	public DeleteNodeAction(IStructuredModel model, List<Element> nodes) {
		setText(XMLUIMessages._UI_MENU_REMOVE);
		this.model = model;
		list = nodes;
	}
	
	public String getUndoDescription() {
		return XMLUIMessages.DELETE;
	}
	
	public void beginNodeAction(NodeAction action) {
		model.beginRecording(action, action.getUndoDescription());
	}

	public void endNodeAction(NodeAction action) {
		model.endRecording(action);
	}

	protected boolean isWhitespaceTextNode(Node node) {
		return (node != null) && (node.getNodeType() == Node.TEXT_NODE) && (node.getNodeValue().trim().length() == 0);
	}

	public void run() {
		if (validateEdit(model, WindupUIPlugin.getActiveWorkbenchShell())) {
			beginNodeAction(this);
			for (Iterator i = list.iterator(); i.hasNext();) {
				Node node = (Node) i.next();
				if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
					Attr attr = (Attr) node;
					attr.getOwnerElement().removeAttributeNode(attr);
				}
				else {
					Node parent = node.getParentNode();
					if (parent != null) {
						Node previousSibling = node.getPreviousSibling();
						if ((previousSibling != null) && isWhitespaceTextNode(previousSibling)) {
							parent.removeChild(previousSibling);
						}
						parent.removeChild(node);
					}
				}
			}
			endNodeAction(this);
		}
	}
}