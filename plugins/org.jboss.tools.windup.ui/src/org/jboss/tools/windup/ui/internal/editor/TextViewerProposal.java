/*******************************************************************************
 * Copyright (c) 2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.editor;

import org.eclipse.jface.fieldassist.IContentProposal;

public class TextViewerProposal implements IContentProposal {
	
	private static final String EMPTY = ""; //$NON-NLS-1$

	private String content = EMPTY;
	private String label = EMPTY;
	private String description = EMPTY;
	private int cursorPosition = 0;

	public TextViewerProposal(String label, int cursorPosition) {
		this.content = label;
		this.label = label;
		this.description = label;
		this.cursorPosition = cursorPosition;
	}

	@Override
	public String getContent() {
		return content;
	}

	@Override
	public int getCursorPosition() {
		return cursorPosition;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getLabel() {
		return label;
	}
}