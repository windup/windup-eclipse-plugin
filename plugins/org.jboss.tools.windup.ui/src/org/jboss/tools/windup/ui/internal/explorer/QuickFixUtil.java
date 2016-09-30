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
import org.jboss.tools.windup.model.util.DocumentUtils;
import org.jboss.tools.windup.windup.Hint;
import org.jboss.tools.windup.windup.QuickFix;
import org.jboss.windup.reporting.model.QuickfixType;

/**
 * Utility for interacting with quick fixes.
 */
public class QuickFixUtil {
	
	public static void applyQuickFix(IResource original, QuickFix quickFix, Hint hint) {
		IResource newResource = QuickFixUtil.getQuickFixedResource(original, quickFix, hint);
		DocumentUtils.replace(original, newResource);
	}
	
	public static IResource getQuickFixedResource(IResource original, QuickFix quickFix, Hint hint) {
		TempProject project = new TempProject();
		if (QuickfixType.REPLACE.toString().equals(quickFix.getQuickFixType())) {
			int lineNumber = hint.getLineNumber()-1;
			String searchString = quickFix.getSearchString();
			String replacement = quickFix.getReplacementString();
			Document document = DocumentUtils.replace(original, lineNumber, searchString, replacement);
			return project.createResource(document.get());
		}
		else if (QuickfixType.DELETE_LINE.toString().equals(quickFix.getQuickFixType())) {
			int lineNumber = hint.getLineNumber()-1;
			Document document = DocumentUtils.deleteLine(original, lineNumber);
			return project.createResource(document.get());
		}
		else if (QuickfixType.INSERT_LINE.toString().equals(quickFix.getQuickFixType())) {
			int lineNumber = hint.getLineNumber();
			lineNumber = lineNumber > 1 ? lineNumber - 2 : lineNumber - 1;
			String newLine = quickFix.getReplacementString();
			Document document = DocumentUtils.insertLine(original, lineNumber, newLine);
			return project.createResource(document.get());
		}
		return null;
	}
}
