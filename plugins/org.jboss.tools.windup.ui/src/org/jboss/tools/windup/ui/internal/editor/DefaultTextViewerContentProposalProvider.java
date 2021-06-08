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
package org.jboss.tools.windup.ui.internal.editor;

import java.util.List;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;

public class DefaultTextViewerContentProposalProvider implements IContentProposalProvider {
	
	private ITextViewerContentProviderDelegate delegate;
	
	public DefaultTextViewerContentProposalProvider(ITextViewerContentProviderDelegate delegate) {
		this.delegate = delegate;
	}
	
	public IContentProposal[] getProposals(String contents, int position) {
		String prefix = DefaultTextViewerContentProposalProvider.getPrefix(contents, position, delegate);
		if (prefix != null) {
			return delegate.getProposals(prefix).stream().map(p -> {
				return new TextViewerProposal(p, position);
			}).toArray(IContentProposal[]::new);
		}
		return new IContentProposal[] {};
	}
	
	public static String getPrefix(String contents, int position, ITextViewerContentProviderDelegate delegate) {
		if (contents.length() == 1 && contents.indexOf(delegate.getActivationChar()) != -1) {
			return ""; //$NON-NLS-1$
		}
		int index = position-1;
		while (index >= 0) {
			if (contents.charAt(index) == delegate.getActivationChar()) {
				return contents.substring(index+1, position);
			}
			index--;
		}
		return null;
	}

	public static interface ITextViewerContentProviderDelegate {
		char getActivationChar();
		List<String> getProposals(String prefix);
	}
}