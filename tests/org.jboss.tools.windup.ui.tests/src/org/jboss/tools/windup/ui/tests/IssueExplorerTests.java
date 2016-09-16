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
package org.jboss.tools.windup.ui.tests;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jboss.tools.windup.ui.internal.explorer.IssueExplorer;
import org.jboss.tools.windup.ui.internal.explorer.MarkerNode;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * Tests surrounding the Issue Explorer.
 */
public class IssueExplorerTests extends WindupUiTest {
	
	/**
	 * Issue Explorer should be open by default.
	 */
	@Test
	public void testOpenIssueExplorerOpen() {
		assertNotNull(partService.findPart(IssueExplorer.VIEW_ID));
	}
	
	/**
	 * Issue Explorer should contain migration issues after Windup execution. 
	 */
	@Ignore
	@Test
	public void testIssueExplorerPopulated() {
		super.runWindup(super.createRunConfiguration());
		IssueExplorer explorer = super.getIssueExplorer();
		Tree tree = explorer.getCommonViewer().getTree();
		List<TreeItem> items = Lists.newArrayList();
		Display.getDefault().syncExec(() -> {
			TreeItem[] treeItems = tree.getItems();
			List<TreeItem> markerItems = Arrays.stream(treeItems).filter(item -> {
				return item.getData() instanceof MarkerNode;
			}).collect(Collectors.toList());
			items.addAll(markerItems);
		});
		assertTrue(228 == items.size());
	}
}
