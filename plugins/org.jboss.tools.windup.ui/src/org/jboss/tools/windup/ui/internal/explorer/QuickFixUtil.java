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

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.Document;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.model.util.DocumentUtils;
import org.jboss.tools.windup.windup.Hint;
import org.jboss.tools.windup.windup.QuickFix;

/**
 * Utility for interacting with quick fixes.
 */
public class QuickFixUtil {
	
	/**
	 * Applies the provided replacement quick fix the resource mapped to the specified hint. 
	 */
	public static void applyReplacementQuickFix(QuickFix quickFix, Hint hint) {
		IResource resource = ModelService.getIssueResource(hint);
		String searchText = quickFix.getSearchString();
		String replacement = quickFix.getReplacementString();
		int lineNumber = hint.getLineNumber() - 1;
		Document document = DocumentUtils.replace(resource, lineNumber, searchText, replacement);
		TempProject project = new TempProject();
		IResource newResource = project.createResource(document.get());
		DocumentUtils.replace(resource, newResource);
	}
}
