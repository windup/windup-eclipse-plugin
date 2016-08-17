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

import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jboss.tools.windup.ui.WindupUIPlugin;

/**
 * Represents a class grouping.
 */
public class ClassGroupNode extends IssueGroupNode<IJavaElement> {

	private IJavaElement element;
	
	public ClassGroupNode(IssueGroupNode<?> parent, List<IMarker> issues, IJavaElement element) {
		super(parent, issues);
		this.element = element;
	}
	
	@Override
	public String getLabel() {
		return element.getElementName();
	}
	
	@Override
	public IJavaElement getType() {
		if (element instanceof ICompilationUnit) {
			ICompilationUnit unit = (ICompilationUnit) element;
			try {
				IType[] types = unit.getTypes();
				if (types != null && types.length > 0) {
					return types[0];
				}
			} catch (JavaModelException e) {
				WindupUIPlugin.log(e);
			}
		}
		return element;
	}
}
