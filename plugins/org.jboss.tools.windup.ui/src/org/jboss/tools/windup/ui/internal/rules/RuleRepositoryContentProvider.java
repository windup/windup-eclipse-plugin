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
package org.jboss.tools.windup.ui.internal.rules;

import static org.jboss.tools.windup.ui.WindupUIPlugin.IMG_GROOVY_RULE;
import static org.jboss.tools.windup.ui.WindupUIPlugin.IMG_RULE;
import static org.jboss.tools.windup.ui.WindupUIPlugin.IMG_RULE_REPO;
import static org.jboss.tools.windup.ui.WindupUIPlugin.IMG_RULE_SET;
import static org.jboss.tools.windup.ui.WindupUIPlugin.IMG_XML_RULE;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.osgi.util.TextProcessor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.sse.core.internal.provisional.IModelStateListener;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.rules.RulesNode.CustomRulesNode;
import org.jboss.tools.windup.ui.internal.rules.RulesNode.RulesetFileNode;
import org.jboss.tools.windup.ui.internal.rules.RulesNode.SystemRulesNode;
import org.jboss.tools.windup.ui.internal.rules.xml.XmlRulesetModelUtil;
import org.jboss.tools.windup.windup.CustomRuleProvider;
import org.jboss.windup.tooling.rules.Rule;
import org.jboss.windup.tooling.rules.RuleProvider;
import org.jboss.windup.tooling.rules.RuleProvider.RuleProviderType;
import org.w3c.dom.Node;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@SuppressWarnings("restriction")
public class RuleRepositoryContentProvider implements ITreeContentProvider, ILabelProvider, IStyledLabelProvider {
	
	private static final Image XML_RULE_PROVIDER;
	private static final Image GROOVY_RULE_PROVIDER;
	
	private static final Image REPOSITORY;
	private static final Image RULE;
	private static final Image RULE_SET;
	
	private Map<Node, CustomRuleProvider> nodeMap = Maps.newHashMap();
	
	private Map<CustomRuleProvider, IModelStateListener> listenerMap = Maps.newHashMap();
	
	static {
		ImageRegistry imageRegistry = WindupUIPlugin.getDefault().getImageRegistry();
		XML_RULE_PROVIDER = imageRegistry.get(IMG_XML_RULE);
		GROOVY_RULE_PROVIDER = imageRegistry.get(IMG_GROOVY_RULE);
		REPOSITORY = imageRegistry.get(IMG_RULE_REPO);
		RULE = imageRegistry.get(IMG_RULE);
		RULE_SET = imageRegistry.get(IMG_RULE_SET);
	}
	
	private TreeViewer treeViewer;
	
	public RuleRepositoryContentProvider (TreeViewer treeViewer) {
		this.treeViewer = treeViewer;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof RuleRepositoryInput) {
			return ((RuleRepositoryInput)parentElement).getChildren();
		}
		else if (parentElement instanceof SystemRulesNode) {
			return ((SystemRulesNode)parentElement).getChildren();
		}
		else if (parentElement instanceof CustomRulesNode) {
			return ((CustomRulesNode)parentElement).getChildren();
		}
		else if (parentElement instanceof RuleProvider) {
			List<Rule> rules = ((RuleProvider)parentElement).getRules();
			List<Object> children = Lists.newArrayList(rules);
			RuleProvider provider = (RuleProvider)parentElement;
			children.add(0, new RulesetFileNode(new File(provider.getOrigin()), provider.getRuleProviderType()));
			return children.stream().toArray(Object[]::new);
		}
		else if (parentElement instanceof CustomRuleProvider) {
			CustomRuleProvider provider = (CustomRuleProvider)parentElement;
			List<Object> children = Lists.newArrayList();
			children.add(new RulesetFileNode(new File(provider.getLocationURI()), RuleProviderType.XML));
			List<Node> ruleNodes = XmlRulesetModelUtil.getRules(provider.getLocationURI());
			ruleNodes.forEach(node -> nodeMap.put(node, provider));
			children.addAll(ruleNodes);
			listen(provider);
			
			return children.stream().toArray(Object[]::new);
		}
		return new Object[0];
	}
	
	public CustomRuleProvider getProvider(Node node) {
		return nodeMap.get(node);
	}
	
	private void listen(CustomRuleProvider ruleProvider) {
		IDOMModel model = XmlRulesetModelUtil.getModel(ruleProvider.getLocationURI());
		IModelStateListener listener = listenerMap.get(ruleProvider);
		if (listener == null) {
			listener = new IModelStateListener() {
				@Override
				public void modelResourceMoved(IStructuredModel oldModel, IStructuredModel newModel) {
					refresh(this, model, ruleProvider);
				}
				@Override
				public void modelResourceDeleted(IStructuredModel theModel) {
					refresh(this, model, ruleProvider);
				}
				@Override
				public void modelReinitialized(IStructuredModel structuredModel) {
					refresh(this, model, ruleProvider);
				}
				@Override
				public void modelDirtyStateChanged(IStructuredModel model, boolean isDirty) {
				}
				@Override
				public void modelChanged(IStructuredModel theModel) {
					refresh(this, model, ruleProvider);
				}
				@Override
				public void modelAboutToBeReinitialized(IStructuredModel structuredModel) {
				}
				@Override
				public void modelAboutToBeChanged(IStructuredModel model) {
				}
			};
			listenerMap.put(ruleProvider, listener);
		}
		else {
			model.removeModelStateListener(listener);
		}
		model.addModelStateListener(listener);
	}
	
	private void refresh(IModelStateListener listener, IDOMModel model, CustomRuleProvider ruleProvider) {
		model.removeModelStateListener(listener);
		listenerMap.remove(ruleProvider);
		if (!treeViewer.getTree().isDisposed()) {
			treeViewer.refresh(ruleProvider);
		}
	}
	
	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof RuleRepositoryInput) {
			return ((RuleRepositoryInput)element).getChildren().length > 0;
		}
		else if (element instanceof SystemRulesNode) {
			return ((SystemRulesNode)element).getChildren().length > 0;
		}
		else if (element instanceof CustomRulesNode) {
			return ((CustomRulesNode)element).getChildren().length > 0;
		}
		else if (element instanceof RuleProvider || element instanceof CustomRuleProvider) {
			return true; // file node
		}
		else if (element instanceof Node) {
			return getChildren(element).length > 0;
		}
		return false;
	}
	
	//
	// Label provider
	//
	@Override
	public String getText(Object element) {
		String result = null;
		if (element instanceof SystemRulesNode) {
			return Messages.systemRulesets;
		}
		else if (element instanceof CustomRulesNode) {
			return Messages.customRulesets;
		}
		else if (element instanceof RuleProvider) {
			RuleProvider ruleProvider = (RuleProvider)element;
			return ruleProvider.getProviderID();
		}
		else if (element instanceof Rule) {
			return ((Rule)element).getRuleID();
		}
		else if (element instanceof RulesetFileNode) {
			return ((RulesetFileNode)element).getName();
		}
		else if (element instanceof CustomRuleProvider) {
			CustomRuleProvider provider = (CustomRuleProvider)element;
			return XmlRulesetModelUtil.getRulesetId(provider.getLocationURI());
		}
		if (element instanceof Node) {
			result = XmlRulesetModelUtil.getRuleId((Node)element);
		}
		result = TextProcessor.process(result);
		return result != null ? result : ""; //$NON-NLS-1$
	}
	
	@SuppressWarnings("incomplete-switch")
	public Image getImage(Object object) {
		Image image = null;
		if (object instanceof SystemRulesNode) {
			image = REPOSITORY;
		}
		else if (object instanceof CustomRulesNode) {
			image = REPOSITORY;
		} 
		else if (object instanceof RuleProvider) {
			image = RULE_SET;
		}
		else if (object instanceof CustomRuleProvider) {
			return RULE_SET;
		}
		else if (object instanceof RulesetFileNode) {
			RulesetFileNode node = (RulesetFileNode)object;
			switch (node.getRuleProviderType()) {
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
			image = RULE;
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
