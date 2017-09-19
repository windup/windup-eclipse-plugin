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
package org.jboss.tools.windup.ui.internal.services;

import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.IDocument;
import org.eclipse.wst.sse.core.internal.format.IStructuredFormatProcessor;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.format.FormatProcessorXML;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.RulesetConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.base.Objects;

@SuppressWarnings("restriction")
@Creatable
@Singleton
public class RulesetDOMService {

	public Element findOrCreateRulesetElement(Document document) {
		NodeList nodeList = document.getElementsByTagName(RulesetConstants.RULESET_NAME);
		if (nodeList.getLength() > 0) {
			return (Element)nodeList.item(0);
		}
		Element rulesetElement = document.createElement(RulesetConstants.RULESET_NAME);
		document.appendChild(rulesetElement);
		return rulesetElement;
	}
	
	public Element findOrCreateRulesElement(Element rulesetElement) {
		NodeList nodeList = rulesetElement.getOwnerDocument().getElementsByTagName(RulesetConstants.RULES_NAME);
		if (nodeList.getLength() > 0) {
			return (Element)nodeList.item(0);
		}
		Element rulesElement = rulesetElement.getOwnerDocument().createElement(RulesetConstants.RULES_NAME);
		rulesetElement.appendChild(rulesetElement);
		return rulesElement;
	}
	
	public Element createRuleElement(Element rulesElement) {
		Element ruleElement = rulesElement.getOwnerDocument().createElement(RulesetConstants.RULE_NAME);
		generateNextRuleId(ruleElement);
		rulesElement.appendChild(ruleElement);
		return ruleElement;
	}
	
	public Element createJavaClassReferencesImportElement(String fullyQualifiedName, Element rulesetElement) {
		Element javaClassElement = createJavaClassElement(rulesetElement.getOwnerDocument());
		javaClassElement.setAttribute(RulesetConstants.JAVA_CLASS_REFERENCES, fullyQualifiedName);
		return javaClassElement;
	}
	
	public Element createPerformElement(Element container) {
		Element performElement = container.getOwnerDocument().createElement(RulesetConstants.PERFORM_NAME);
		container.appendChild(performElement);
		return performElement;
	}
	
	public Element createHintElement(Element performElement) {
		Element hintElement = performElement.getOwnerDocument().createElement(RulesetConstants.HINT_NAME);
		performElement.appendChild(hintElement);
		return hintElement;
	}
	
	public void populateDefaultHintElement(Element hintElement) {
		hintElement.setAttribute(RulesetConstants.TITLE, "");
		hintElement.setAttribute(RulesetConstants.EFFORT, "1");
		hintElement.setAttribute(RulesetConstants.CATEGORY_ID, "");
		createMessageElement(hintElement);
	}
	
	public void createMessageElement(Element hintElement) {
		Element messageElement = hintElement.getOwnerDocument().createElement(RulesetConstants.MESSAGE);
		hintElement.appendChild(messageElement);
	}
	
	public Element createWhenElement(Document document) {
		Element whenElement = document.createElement(RulesetConstants.WHEN_NAME);
		return whenElement;
	}
	
	public Element createJavaClassElement(Document document) {
		Element javaclassElement = document.createElement(RulesetConstants.JAVACLASS_NAME);
		return javaclassElement;
	}
	
	public Element createJavaClassLocation(Element javaClassElement) {
		Element locationElement = javaClassElement.getOwnerDocument().createElement(RulesetConstants.JAVA_CLASS_LOCATION);
		javaClassElement.appendChild(locationElement);
		return locationElement;
	}
	
	public Element createXMLFileElement(Document document) {
		Element xmlfileElement = document.createElement(RulesetConstants.XMLFILE);
		return xmlfileElement;
	}
	
	public ITypeRoot getEditorInput(JavaEditor editor) {
		return JavaUI.getEditorInputTypeRoot(editor.getEditorInput());
	}
	
	public IDocument getDocument(JavaEditor editor) {
		return JavaUI.getDocumentProvider().getDocument(editor.getEditorInput());
	}
	
	public String getRulesetId(Document document) {
		String rulesetId = "";
		NodeList nodeList = document.getElementsByTagName(RulesetConstants.RULESET_NAME);
		if (nodeList.getLength() == 1) {
			Element element = (Element)nodeList.item(0);
			rulesetId = element.getAttribute(RulesetConstants.ID);
		}
		return rulesetId;
	}
	
	public void generateNextRuleId(Element ruleElement) {
		Element rulesetElement = findOrCreateRulesetElement(ruleElement.getOwnerDocument());
		String id = rulesetElement.getAttribute(RulesetConstants.ID);
		NodeList rules = rulesetElement.getElementsByTagName(RulesetConstants.RULE_NAME);
		id += "-" + rules.getLength();
		ruleElement.setAttribute(RulesetConstants.ID, id);
	}
	
	public void insertBeforePreviousSibling(Node node) {
		Node previousSibling = findPreviousSibling((Element)node);
		if (previousSibling != null) {
			Node parent = node.getParentNode();
			parent.removeChild(node);
			parent.insertBefore(node, previousSibling);
		}
	}
	
	public Node findPreviousSibling(Element element) {
		NodeList nodeList = ((Element)element.getParentNode()).getElementsByTagName(element.getTagName());
		Node previousSibling = null;
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (Objects.equal(node, element)) {
				break;
			}
			previousSibling = node;
		}
		return previousSibling;
	}
	
	public void insertAfterNextSibling(Node node) {
		Node parent = node.getParentNode();
		Node nextSibling = findNextSibling((Element)node, 2);
		parent.removeChild(node);
		parent.insertBefore(node, nextSibling);
	}
	
	public Node findNextSibling(Element element, int position) {
		NodeList nodeList = ((Element)element.getParentNode()).getElementsByTagName(element.getTagName());
		for (int i = 0; i < nodeList.getLength() - position; i++) {
			Node node = nodeList.item(i);
			if (Objects.equal(node, element)) {
				return nodeList.item(i+position);
			}
		}
		return null;
	}
	
	public static void format(IStructuredModel model, Node element, boolean deep) {
		if (model.getStructuredDocument() != null) {
			try {
				model.aboutToChangeModel();
				IStructuredFormatProcessor formatProcessor = new FormatProcessorXML();
				formatProcessor.formatNode(element);
			}
			finally {
				model.changedModel();
	
			}
		}
	}
}
