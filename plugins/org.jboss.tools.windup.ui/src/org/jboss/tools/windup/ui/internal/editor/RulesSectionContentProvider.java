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

import java.util.List;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.rules.xml.XMLRulesetModelUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.google.common.collect.Lists;

public class RulesSectionContentProvider implements ITreeContentProvider, ILabelProvider, IStyledLabelProvider {
	
	private static final Image RULE;

	static {
		ImageRegistry imageRegistry = WindupUIPlugin.getDefault().getImageRegistry();
		RULE = imageRegistry.get(IMG_RULE);
	}
	
	private TreeViewer treeViewer;
	
	public RulesSectionContentProvider (TreeViewer treeViewer) {
		this.treeViewer = treeViewer;
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
		/*if (element instanceof Node) {
			Node node = (Node) element;
			List<Node> list = Lists.newArrayList();
			
			for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
				if (child instanceof Element && .equals(child.getNodeName()))
					return (Element) child;
			}
			
		}*/
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
	public String getText(Object element) {
		String ruleId = "";
		if (element instanceof Node) {
			ruleId = XMLRulesetModelUtil.getRuleId((Node)element);
		}
		return ruleId;
	}
	
	@Override
	public Image getImage(Object element) {
		Image image = null;
		if (element instanceof Node) {
			image = RULE;
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
