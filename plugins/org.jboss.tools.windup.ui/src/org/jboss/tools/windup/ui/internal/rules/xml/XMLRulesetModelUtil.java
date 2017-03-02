package org.jboss.tools.windup.ui.internal.rules.xml;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.DocumentProviderRegistry;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.jboss.tools.windup.model.domain.WorkspaceResourceUtils;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.collect.Lists;

@SuppressWarnings("restriction")
public class XMLRulesetModelUtil {

	public static String getRulesetId(String locationURI) {
		String rulesetId = null;
		IDOMModel model = XMLRulesetModelUtil.getModel(locationURI, false);
		if (model != null) {
			Document document = model.getDocument();
			NodeList rulesets = document.getElementsByTagName("ruleset");  //$NON-NLS-1$
			if (rulesets.getLength() > 0) {
				Node ruleset = rulesets.item(0);
				NamedNodeMap attributes = ruleset.getAttributes();
				Node rulesetIdNode = attributes.getNamedItem("id");  //$NON-NLS-1$
				if (rulesetIdNode != null) {
					rulesetId = ((Attr) rulesetIdNode).getValue();
				}
			}
			model.releaseFromRead();
		}
		return rulesetId;
	}
	
	public static List<Node> getRules(String locationURI) {
		List<Node> rules = Lists.newArrayList();
		IDOMModel model = XMLRulesetModelUtil.getModel(locationURI, false);
		if (model != null) {
			Document document = model.getDocument();
			NodeList ruleNodes = document.getElementsByTagName("rule");  //$NON-NLS-1$
			if (ruleNodes.getLength() > 0) {
				for (int i = 0; i < ruleNodes.getLength(); i++) {
					Node ruleNode = ruleNodes.item(i);
					rules.add(ruleNode);
				}
			}
			model.releaseFromRead();
		}
		return rules;
	}
	
	public static String getRuleId(Node node) {
		String ruleId = "";
		NamedNodeMap attributes = node.getAttributes();
		Node ruleIdNode = attributes.getNamedItem("id");  //$NON-NLS-1$
		if (ruleIdNode != null) {
			ruleId = ((Attr) ruleIdNode).getValue();
		}
		return ruleId;
	}
	
	public static IDOMModel getModel(String locationURI, boolean edit) {
		try {
			IStructuredModel model = null;
			if (!edit) {
				model = StructuredModelManager.getModelManager().getModelForRead(WorkspaceResourceUtils.getFile(locationURI));
			}
			else {
				model = StructuredModelManager.getModelManager().getModelForEdit(WorkspaceResourceUtils.getFile(locationURI));
			}
			if (model != null && model instanceof IDOMModel) {
				return (IDOMModel) model;
			}
		} catch (IOException | CoreException e) {
			WindupUIPlugin.log(e);
		}
		return null;
	}
	
	
	/*private IDocument getDocument(IEditorInput editorInput) {
		return getDocumentProvider().getDocument(editorInput);
	}
	
	private IDocumentProvider getDocumentProvider(IEditorInput editorInput) {
		IDocumentProvider documentProvider = DocumentProviderRegistry.getDefault().getDocumentProvider(editorInput);
		try {
			documentProvider.connect(editorInput);
		} catch (CoreException e) {
			WindupUIPlugin.log(e);
		}
		return documentProvider;
	}*/
}
