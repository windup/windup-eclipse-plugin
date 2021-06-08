/*******************************************************************************
 * Copyright (c) 2021 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.launch;

import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.jboss.tools.windup.model.util.MavenUtil.ProjectInfo;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;

public class InputTreeLabelProvider extends LabelProvider implements IStyledLabelProvider {

	private WorkbenchLabelProvider workbenchProvider = new WorkbenchLabelProvider();
	
	@Override
	public Image getImage(Object element) {
		if (element instanceof ProjectInfo) {
			ProjectInfo info = (ProjectInfo)element;
			if (info.projectExists()) {
				return workbenchProvider.getImage(info.getProject());
			}
			else if (info.getProject() == null) {
				return workbenchProvider.getImage(info.getTopProject().getProject());
			}
		}
		return workbenchProvider.getImage(element);
	}
	
	@Override
	public String getText(Object element) {
		if (element instanceof ProjectInfo) {
			ProjectInfo info = (ProjectInfo)element;
			if (info.projectExists()) {
				return info.getProject().getName();
			}
			else {
				return info.getName();
			}
		}
		return "unknown"; //$NON-NLS-1$
	}
		
	@Override
	public StyledString getStyledText(Object element) {
		StyledString style = new StyledString();
		if (element instanceof ProjectInfo) {
			ProjectInfo info = (ProjectInfo)element;
			if (!info.isParentProject()) {
				style.append(info.getName());
				style.append(" [Submodule projects are implicitly analyzed]", StyledString.DECORATIONS_STYLER); //$NON-NLS-1$
			}
			else {
				style.append(info.getProject().getName());
			}
		}
		return style;
	}
}
