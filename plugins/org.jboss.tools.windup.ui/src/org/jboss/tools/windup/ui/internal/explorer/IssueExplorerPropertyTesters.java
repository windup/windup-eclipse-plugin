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

import javax.inject.Inject;

import org.eclipse.core.expressions.PropertyTester;
import org.jboss.tools.windup.ui.internal.services.IssueGroupService;

/**
 * Property testers for the Issue Explorer.
 */
public class IssueExplorerPropertyTesters {
	
	public static final String QUICKFIX = "hasQuickFix";  //$NON-NLS-1$
	public static final String FIXED = "isFixed";  //$NON-NLS-1$
	public static final String HIERARCHY = "isGroupByHierarchy"; //$NON-NLS-1$
	
	public static class QuickFixPropertyTester extends PropertyTester {
		
		@Inject private IssueGroupService groupService;
		
		@Override
		public boolean test(Object element, String property, Object[] args, Object expectedValue) {
			if (QUICKFIX.equals(property)) {
				if (element instanceof MarkerNode) {
					return ((MarkerNode)element).hasQuickFix();
				}
			}
			else if (FIXED.equals(property)) {
				if (element instanceof MarkerNode) {
					return !((MarkerNode)element).isFixed();
				}
			}
			else if (HIERARCHY.equals(property)) {
				return groupService.isGroupByHierarchy();
			}
			return false;
		}
	}
}
