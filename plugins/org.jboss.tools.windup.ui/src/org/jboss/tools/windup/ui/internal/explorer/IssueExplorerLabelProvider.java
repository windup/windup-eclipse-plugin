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
import static org.jboss.tools.windup.ui.WindupUIPlugin.IMG_REPORT;
import static org.jboss.tools.windup.ui.WindupUIPlugin.IMG_STALE_ISSUE;

import java.util.Map;

import org.apache.commons.lang.WordUtils;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.misc.StringMatcher;
import org.eclipse.ui.internal.misc.StringMatcher.Position;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.navigator.ICommonContentExtensionSite;
import org.eclipse.ui.navigator.ICommonLabelProvider;
import org.eclipse.ui.themes.IThemeManager;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.explorer.IssueExplorerContentProvider.ReportNode;
import org.jboss.tools.windup.ui.internal.explorer.IssueExplorerContentProvider.RootReportNode;
import org.jboss.tools.windup.ui.internal.explorer.IssueExplorerContentProvider.RuleGroupNode;
import org.jboss.tools.windup.ui.internal.explorer.IssueExplorerContentProvider.SeverityNode;
import org.jboss.tools.windup.ui.internal.explorer.IssueExplorerContentProvider.TreeNode;
import org.jboss.tools.windup.windup.Hint;
import org.jboss.tools.windup.windup.Issue;

import com.google.common.collect.Maps;

/**
 * The label provider for the Windup explorer.
 */
@SuppressWarnings("restriction")
public class IssueExplorerLabelProvider implements ICommonLabelProvider, IStyledLabelProvider, IFontProvider {
	
	private static final Color GREEN = new Color(Display.getDefault(), 107,169,128);
	
	private Map<String, Image> imageCache = Maps.newHashMap();
	
	private static final Image ERROR;
	private static final Image ERROR_QUICKFIX;
	private static final Image WARNING;
	private static final Image WARNING_QUICKFIX;
	private static final Image INFO;
	private static final Image FIXED;
	private static final Image INFO_QUICKFIX;
	private static final Image RULE;
	private static final Image REPORT;
	private static final Image STALE;
	
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
		REPORT = imageRegistry.get(IMG_REPORT);
		STALE = imageRegistry.get(IMG_STALE_ISSUE);
	}
	
	private static Color YELLOW = Display.getDefault().getSystemColor(SWT.COLOR_YELLOW);
	
	private WorkbenchLabelProvider workbenchProvider = new WorkbenchLabelProvider();
	
	private IssueExplorer issueExplorer;
	
	@Override
	public Image getImage(Object element) {
		if (element instanceof RuleGroupNode) {
			return RULE;
		}
		if (element instanceof ReportNode || element instanceof RootReportNode) {
			return REPORT;
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
			
			boolean isFixed = issue.isFixed();
			if (isFixed) {
				return FIXED;
			}
			
			if (issue.getIssue().isStale()) {
				return STALE;
			}
		
			boolean hasQuickFix = issue.hasQuickFix();
			
			Image result = null;
			switch (issue.getSeverity()) {
				case IMarker.SEVERITY_ERROR: {
					result = hasQuickFix ? ERROR_QUICKFIX : ERROR;
					break;
				}
				case IMarker.SEVERITY_WARNING: {
					result = hasQuickFix ? WARNING_QUICKFIX : WARNING;
					break;
				}
				default: {
					result = hasQuickFix ? INFO_QUICKFIX : INFO;
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
			String label = getLabel(node);
			if (node.isLeafParent()) {
				label += " (" + node.getChildren().size() + ")";
			}
			return label;
		}
		return workbenchProvider.getText(element);
	}
	
	private String getLabel(TreeNode node) {
		String label = "";
		if (node instanceof SeverityNode) {
			SeverityNode severityNode = (SeverityNode)node;
			label = WordUtils.capitalize(severityNode.getSeverity().toLowerCase());
		}
		else if (node instanceof RuleGroupNode) {
			RuleGroupNode ruleNode = (RuleGroupNode)node;
			label = ruleNode.getTitle();
		}
		else {
			Object segment = node.getSegment();
			if (segment instanceof IResource || segment instanceof IJavaElement || segment instanceof IMarker) { 
				label = workbenchProvider.getText(segment);
			}
			if (label == null || label.isEmpty()) {
				label = String.valueOf(segment);
			}
		}
		return label;
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
	
	private String getFiltertText() {
		String filterText = "";
		if (issueExplorer == null) {
			issueExplorer = (IssueExplorer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(IssueExplorer.VIEW_ID);
		}
		if (issueExplorer != null) {
			filterText = issueExplorer.getFilterText();
		}
		return filterText;
	}

	@Override
	public StyledString getStyledText(Object element) {
		String filterText = getFiltertText(); 
		
		StyledString style = new StyledString();
		
		if (element instanceof MarkerNode) {
			MarkerNode markerNode = (MarkerNode)element;
			
			Issue issue = markerNode.getIssue();
			
			if (issue instanceof Hint) {
				Hint hint = (Hint)issue;
				if (issue.isFixed()) {
					style.append(hint.getTitle(), new Styler() {
						@Override
						public void applyStyles(TextStyle textStyle) {
							textStyle.foreground = GREEN;
							textStyle.strikeout = true; 
						}
					});
				}
				else {
					style.append(hint.getTitle());
				}
				style.append(" [" + markerNode.getFileName() + " " + hint.getLineNumber() + "]", 
						StyledString.DECORATIONS_STYLER); 
			}

			else {
				style.append(markerNode.getFileName());
			}
		}
		
		else if (element instanceof TreeNode) {
			TreeNode node = (TreeNode)element;
			String label = getLabel(node);
			style.append(label);
						
			if (node instanceof RuleGroupNode) {
				RuleGroupNode ruleNode = (RuleGroupNode)node;
				style.append(" [rule id: " + ruleNode.getRuleId() + "]", StyledString.DECORATIONS_STYLER);
			}
			
			if (node.isLeafParent()) {
				style.append(" (" + node.getChildren().size() + ")", StyledString.COUNTER_STYLER);
			}
		}
		
		if (!filterText.isEmpty()) {
			StringMatcher matcher = IssueExplorer.getFilterMatcher(filterText);
			String label = style.getString();
			Position position = matcher.find(label, 0, label.length());
			if (position != null && (position.getEnd() - position.getStart()) > 0) {
				style.setStyle(position.getStart(), position.getEnd() - position.getStart(), new Styler() {
					@Override
					public void applyStyles(TextStyle textStyle) {
						textStyle.background = YELLOW;
					}
				});
			}
		}
		return style;
	}

	@Override
	public void init(ICommonContentExtensionSite aConfig) {
	}
	
	@Override
	public Font getFont(Object element) {
		if (element instanceof MarkerNode) {
			MarkerNode node = (MarkerNode)element;
			if (node.isFixed()) {
				return JFaceResources.getFontRegistry().getItalic(JFaceResources.DEFAULT_FONT);
			}
		}
		return null;
	}
}
