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

import static org.jboss.tools.windup.ui.WindupUIPlugin.IMG_RULE;
import static org.jboss.tools.windup.ui.WindupUIPlugin.IMG_XML_RULE;
import static org.jboss.tools.windup.ui.WindupUIPlugin.IMG_JAVA;

import java.util.List;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.editor.RulesetWidgetFactory.JavaClassNodeConfig;
import org.jboss.tools.windup.ui.internal.editor.RulesetWidgetFactory.PerformNodeConfig;
import org.jboss.tools.windup.ui.internal.editor.RulesetWidgetFactory.RuleNodeConfig;
import org.jboss.tools.windup.ui.internal.editor.RulesetWidgetFactory.WhenNodeConfig;
import org.jboss.tools.windup.ui.internal.rules.xml.XMLRulesetModelUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.common.collect.Lists;

public class RulesSectionContentProvider implements ITreeContentProvider, ILabelProvider, IStyledLabelProvider {
	
	private static final Image RULE;
	private static final Image XML_NODE;
	private static final Image JAVA;

	static {
		ImageRegistry imageRegistry = WindupUIPlugin.getDefault().getImageRegistry();
		RULE = imageRegistry.get(IMG_RULE);
		XML_NODE = imageRegistry.get(IMG_XML_RULE);
		JAVA = imageRegistry.get(IMG_JAVA);
	}
	
	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object element) {
		if (element instanceof Document) {
			Document document = (Document)element;
			List<Node> rules = Lists.newArrayList();
			XMLRulesetModelUtil.collectRuleNodes(document, rules);
			return rules.toArray();
		}
		if (element instanceof Element) {
			Element node = (Element) element;
			if (isRuleNode(node)) {
				List<Element> children = Lists.newArrayList();
				for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
					if (child instanceof Element) {
						if (isWhenNode((Element)child)) {
							children.add((Element)child);
						}
						else if (isPerformNode(((Element)child))) {
							children.add((Element)child);
						}
					}
				}
				return children.toArray();
			}
			else if (isWhenNode(node)) {
				List<Element> children = Lists.newArrayList();
				for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
					if (child instanceof Element) {
						if (isJavaClassNode((Element)child)) {
							children.add((Element)child);
						}
					}
				}
				return children.toArray();
			}
		}
		return new Object[0];
	}
	
	@Override
	public Object getParent(Object element) {
		return null;
	}
	
	public boolean hasChildren(Object element) {
		if (element instanceof Node) {
			return getChildren(element).length > 0;
		}
		return false;
	}
	
	@Override
	public String getText(Object node) {
		String text = "";
		if (node instanceof Element) {
			Element element = (Element)node; 
			if (isRuleNode(element)) {
				text = XMLRulesetModelUtil.getRuleId((Node)element);
			}
			else {
				text = element.getNodeName();
			}
		}
		return text;
	}
	
	private boolean isRuleNode(Element element) {
		return RuleNodeConfig.NAME.equals(element.getNodeName());
	}
	
	private boolean isWhenNode(Element element) {
		return WhenNodeConfig.NAME.equals(element.getNodeName());
	}
	
	private boolean isPerformNode(Element element) {
		return PerformNodeConfig.NAME.equals(element.getNodeName());
	}
	
	private boolean isJavaClassNode(Element element) {
		return JavaClassNodeConfig.NAME.equals(element.getNodeName());
	}
	
	@Override
	public Image getImage(Object node) {
		Image image = null;
		if (node instanceof Element) {
			Element element = (Element)node;
			if (isRuleNode(element)) {
				image = RULE;
			}
			else if (isJavaClassNode(element)) {
				return JAVA;
			}
			else {
				image = XML_NODE;
			}
		}
		return image;
	}

	@Override
	public StyledString getStyledText(Object element) {
		StyledString style = new StyledString(getText(element));
		return style;
	}

	@Override
	public void addListener(ILabelProviderListener listener) {}
	@Override
	public void removeListener(ILabelProviderListener listener) {}
	
	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}
	
	@Override
	public void dispose() {
	}
}
