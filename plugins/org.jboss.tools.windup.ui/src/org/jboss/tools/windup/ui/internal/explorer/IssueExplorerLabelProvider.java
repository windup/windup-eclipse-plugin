/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.explorer;

import static org.jboss.tools.windup.ui.WindupUIPlugin.IMG_ERROR;
import static org.jboss.tools.windup.ui.WindupUIPlugin.IMG_FIXED;
import static org.jboss.tools.windup.ui.WindupUIPlugin.IMG_INFO;
import static org.jboss.tools.windup.ui.WindupUIPlugin.IMG_QUICKFIX_ERROR;
import static org.jboss.tools.windup.ui.WindupUIPlugin.IMG_QUICKFIX_INFO;
import static org.jboss.tools.windup.ui.WindupUIPlugin.IMG_QUICKFIX_WARNING;
import static org.jboss.tools.windup.ui.WindupUIPlugin.IMG_RULE;
import static org.jboss.tools.windup.ui.WindupUIPlugin.IMG_WARNING;

import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonLabelProvider;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.explorer.IssueExplorerContentProvider.RuleGroupNode;
import org.jboss.tools.windup.ui.internal.explorer.IssueExplorerContentProvider.SeverityNode;
import org.jboss.tools.windup.ui.internal.explorer.IssueExplorerContentProvider.TreeNode;

import com.google.common.collect.Maps;

/**
 * The label provider for the Windup explorer.
 */
public class IssueExplorerLabelProvider implements ICommonLabelProvider, IColorProvider {
	
	private Map<String, Image> imageCache = Maps.newHashMap();
	
	private static final Image ERROR;
	private static final Image ERROR_QUICKFIX;
	private static final Image WARNING;
	private static final Image WARNING_QUICKFIX;
	private static final Image INFO;
	private static final Image FIXED;
	private static final Image INFO_QUICKFIX;
	private static final Image RULE;
	
	static {
		ImageRegistry imageRegistry = WindupUIPlugin.getDefault().getImageRegistry();
		ERROR = imageRegistry.get(IMG_ERROR);
		ERROR_QUICKFIX = imageRegistry.get(IMG_QUICKFIX_ERROR);
		WARNING = imageRegistry.get(IMG_WARNING);
		WARNING_QUICKFIX = imageRegistry.get(IMG_QUICKFIX_WARNING);
		INFO = imageRegistry.get(IMG_INFO);
		INFO_QUICKFIX = imageRegistry.get(IMG_QUICKFIX_INFO);
		RULE = imageRegistry.get(IMG_RULE);
		FIXED = imageRegistry.get(IMG_FIXED);
	}
	
	private WorkbenchLabelProvider workbenchProvider = new WorkbenchLabelProvider();
	
	@Override
	public Image getImage(Object element) {
		if (element instanceof RuleGroupNode) {
			return RULE;
		}
		if (element instanceof SeverityNode) {
			SeverityNode node = (SeverityNode)element;
			int severity = (int)node.getSegment();
			Image image = null;
			switch (severity) {
				case IMarker.SEVERITY_ERROR:
					image = ERROR; break;
				case IMarker.SEVERITY_WARNING:
					image = WARNING; break;
				case IMarker.SEVERITY_INFO:
					image = INFO;
			}
			return image;
		}
		if (element instanceof MarkerNode) {
			MarkerNode issue = (MarkerNode)element;
			boolean hasQuickFix = issue.hasQuickFix();
			boolean isFixed = issue.isFixed();
			Image result = null;
			switch (issue.getSeverity()) {
				case IMarker.SEVERITY_ERROR: {
					result = isFixed ? FIXED : hasQuickFix ? ERROR_QUICKFIX : ERROR;
					break;
				}
				case IMarker.SEVERITY_WARNING: {
					result = isFixed ? FIXED : hasQuickFix ? WARNING_QUICKFIX : WARNING;
					break;
				}
				default: {
					result = isFixed ? FIXED : hasQuickFix ? INFO_QUICKFIX : INFO;
				}
			}
			return result;
		}
		else if (element instanceof TreeNode) {
			TreeNode node = (TreeNode)element;
			element = node.getSegment();
		}
		return workbenchProvider.getImage(element);
	}

	@Override
	public String getText(Object element) {
		if (element instanceof TreeNode) {
			TreeNode node = (TreeNode)element;
			String label = "";
			if (element instanceof SeverityNode) {
				SeverityNode severityNode = (SeverityNode)element;
				label = severityNode.getSeverity();
			}
			else {
				element = node.getSegment();
				if (element instanceof IResource || element instanceof IJavaElement || element instanceof IMarker) { 
					label = workbenchProvider.getText(element);
				}
				if (label == null || label.isEmpty()) {
					label = String.valueOf(element);
				}
			}
			if (node.isLeafParent()) {
				label += " (" + node.getChildren().size() + ")";
			}
			return label;
		}
		return workbenchProvider.getText(element);
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {
		imageCache.values().stream().forEach(i -> i.dispose());
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
	}

	@Override
	public void restoreState(IMemento aMemento) {
	}

	@Override
	public void saveState(IMemento aMemento) {
	}

	@Override
	public String getDescription(Object anElement) {
		return null;
	}

	@Override
	public Color getForeground(Object element) {
		return null;
	};

	@Override
	public Color getBackground(Object element) {
		return null;
	}

	@Override
	public void init(ICommonContentExtensionSite aConfig) {
	}
}
