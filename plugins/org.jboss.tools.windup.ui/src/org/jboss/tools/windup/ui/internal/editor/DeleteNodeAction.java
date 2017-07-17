package org.jboss.tools.windup.ui.internal.editor;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.ui.internal.XMLUIMessages;
import org.eclipse.wst.xml.ui.internal.actions.NodeAction;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

@SuppressWarnings({"restriction"})
public class DeleteNodeAction extends NodeAction {
	protected List list;
	protected IStructuredModel model;

	public DeleteNodeAction(IStructuredModel model, Node node) {
		setText(XMLUIMessages._UI_MENU_REMOVE);
		this.model = model;
		list = new Vector();
		list.add(node);
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