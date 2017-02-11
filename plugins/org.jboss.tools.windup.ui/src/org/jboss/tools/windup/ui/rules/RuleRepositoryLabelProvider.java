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
package org.jboss.tools.windup.ui.rules;

import static org.jboss.tools.windup.ui.WindupUIPlugin.IMG_CONFIG_HOT;
import static org.jboss.tools.windup.ui.WindupUIPlugin.IMG_GROOVY_RULE;
import static org.jboss.tools.windup.ui.WindupUIPlugin.IMG_RULE;
import static org.jboss.tools.windup.ui.WindupUIPlugin.IMG_RULE_CONTAINER;
import static org.jboss.tools.windup.ui.WindupUIPlugin.IMG_XML_RULE;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.osgi.util.TextProcessor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.xml.ui.internal.util.SharedXMLEditorPluginImageHelper;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.rules.SystemRulesNode.SystemRulesetFileNode;
import org.jboss.windup.tooling.rules.Rule;
import org.jboss.windup.tooling.rules.RuleProvider;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;

@SuppressWarnings("restriction")
public class RuleRepositoryLabelProvider implements ILabelProvider, IStyledLabelProvider {
	
	private static final Image XML_RULE_PROVIDER;
	private static final Image GROOVY_RULE_PROVIDER;
	
	private static final Image SYSTEM_RULES;
	private static final Image RULE;
	private static final Image RULE_CONTAINER;
	
	static {
		ImageRegistry imageRegistry = WindupUIPlugin.getDefault().getImageRegistry();
		XML_RULE_PROVIDER = imageRegistry.get(IMG_XML_RULE);
		GROOVY_RULE_PROVIDER = imageRegistry.get(IMG_GROOVY_RULE);
		RULE_CONTAINER = imageRegistry.get(IMG_RULE_CONTAINER);
		SYSTEM_RULES = imageRegistry.get(IMG_CONFIG_HOT);
		RULE = imageRegistry.get(IMG_RULE);
	}

	@Override
	public String getText(Object element) {
		String result = null;
		if (element instanceof SystemRulesNode) {
			return Messages.systemRulesets;
		}
		else if (element instanceof RuleProvider) {
			RuleProvider ruleProvider = (RuleProvider)element;
			return ruleProvider.getProviderID();
		}
		else if (element instanceof Rule) {
			return ((Rule)element).getRuleID();
		}
		else if (element instanceof SystemRulesetFileNode) {
			return ((SystemRulesetFileNode)element).getFileName();
		}
		if (element instanceof Node) {
			Node node = (Node) element;
			switch (node.getNodeType()) {
				case Node.ATTRIBUTE_NODE : {
					result = node.getNodeName();
					break;
				}
				case Node.DOCUMENT_TYPE_NODE : {
					result = "DOCTYPE"; //$NON-NLS-1$
					break;
				}
				case Node.ELEMENT_NODE : {
					result = node.getNodeName();
					break;
				}
				case Node.PROCESSING_INSTRUCTION_NODE : {
					result = ((ProcessingInstruction) node).getTarget();
					break;
				}
			}
		}
		result = TextProcessor.process(result);
		return result != null ? result : ""; //$NON-NLS-1$
	}
	
	@SuppressWarnings("incomplete-switch")
	public Image getImage(Object object) {
		Image image = null;
		if (object instanceof SystemRulesNode) {
			image = SYSTEM_RULES;
		}
		else if (object instanceof RuleProvider) {
			image = RULE_CONTAINER;
		}
		else if (object instanceof SystemRulesetFileNode) {
			SystemRulesetFileNode node = (SystemRulesetFileNode)object;
			switch (node.getRuleProvider().getRuleProviderType()) {
				case XML:
					image = XML_RULE_PROVIDER;
					break;
				case GROOVY:
					image = GROOVY_RULE_PROVIDER;
					break;
			}
		}
		else if (object instanceof Rule) {
			image = RULE;
		}
		else if (object instanceof Node) {
			Node node = (Node) object;
			switch (node.getNodeType()) {
				case Node.ATTRIBUTE_NODE : {
					image = SharedXMLEditorPluginImageHelper.getImage(SharedXMLEditorPluginImageHelper.IMG_OBJ_ATTRIBUTE);
					break;
				}
				case Node.CDATA_SECTION_NODE : {
					image = SharedXMLEditorPluginImageHelper.getImage(SharedXMLEditorPluginImageHelper.IMG_OBJ_CDATASECTION);
					break;
				}
				case Node.COMMENT_NODE : {
					image = SharedXMLEditorPluginImageHelper.getImage(SharedXMLEditorPluginImageHelper.IMG_OBJ_COMMENT);
					break;
				}
				case Node.DOCUMENT_TYPE_NODE : {
					image = SharedXMLEditorPluginImageHelper.getImage(SharedXMLEditorPluginImageHelper.IMG_OBJ_DOCTYPE);
					break;
				}
				case Node.ELEMENT_NODE : {
					image = SharedXMLEditorPluginImageHelper.getImage(SharedXMLEditorPluginImageHelper.IMG_OBJ_ELEMENT);
					break;
				}
				case Node.PROCESSING_INSTRUCTION_NODE : {
					image = SharedXMLEditorPluginImageHelper.getImage(SharedXMLEditorPluginImageHelper.IMG_OBJ_PROCESSINGINSTRUCTION);
					break;
				}
				case Node.TEXT_NODE : {
					image = SharedXMLEditorPluginImageHelper.getImage(SharedXMLEditorPluginImageHelper.IMG_OBJ_TXTEXT);
					break;
				}
				case Node.ENTITY_REFERENCE_NODE : {
					image = SharedXMLEditorPluginImageHelper.getImage(SharedXMLEditorPluginImageHelper.IMG_OBJ_ENTITY_REFERENCE);
					break;
				}
			}
		}
		return image;
	}
	
	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}
	
	@Override
	public StyledString getStyledText(Object element) {
		StyledString style = new StyledString();
		return style;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {}
	@Override
	public void addListener(ILabelProviderListener listener) {}
	@Override
	public void dispose() {}
}
