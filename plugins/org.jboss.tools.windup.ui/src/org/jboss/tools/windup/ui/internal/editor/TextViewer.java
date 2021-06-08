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

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.editor.DefaultTextViewerContentProposalProvider.ITextViewerContentProviderDelegate;

public class TextViewer {
	
	private static String KEY_PRESS = "Ctrl+Space";
	
	private Text text;
	private ITextViewerContentProviderDelegate contentProposalProvider;
	
	public TextViewer (Text text, ITextViewerContentProviderDelegate contentProposalProvider) {
		this.text = text;
		this.contentProposalProvider = contentProposalProvider;
		init();
	}
	
	private void init() {
		try {
			ContentProposalAdapter adapter = null;
			DefaultTextViewerContentProposalProvider scp = new DefaultTextViewerContentProposalProvider(contentProposalProvider);
			KeyStroke ks = KeyStroke.getInstance(KEY_PRESS);
			adapter = new ContentProposalAdapter(text, new TextViewerContentAdapter(contentProposalProvider), scp, ks, 
					new char[] {contentProposalProvider.getActivationChar()});
			adapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_INSERT);
			adapter.setLabelProvider(new TextViewer.TextViewerLabelProvider());
		}
		catch (Exception e) {
			WindupUIPlugin.log(e);
		}
	}

	public static class TextViewerLabelProvider extends LabelProvider {
		
		@Override
		public String getText(Object element) {
			IContentProposal proposal = (IContentProposal)element;
			return proposal.getLabel() == null ? proposal.getContent()
					: proposal.getLabel();
		}
		@Override
		public Image getImage(Object element) {
			return WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_PARAM);
		}
	}
	
	public static class TextViewerContentAdapter extends TextContentAdapter {
		
		private ITextViewerContentProviderDelegate contentProposalProvider;
		
		public TextViewerContentAdapter(ITextViewerContentProviderDelegate contentProposalProvider) {
			this.contentProposalProvider = contentProposalProvider;
		}
		
		@Override
		public void insertControlContents(Control control, String proposal, int cursorPosition) {
			Text text = (Text)control;
			String original = text.getText();

			String prefix = DefaultTextViewerContentProposalProvider.getPrefix(original, cursorPosition, contentProposalProvider);

			if (prefix != null && prefix.length() > 0) {
				StringBuffer buff = new StringBuffer(original);
				int start = cursorPosition-prefix.length();
				int end = prefix.length();
				buff.replace(start, start+end, proposal);
				int newLocation = cursorPosition - prefix.length() + proposal.length();
				text.setText(buff.toString());
				text.setSelection(newLocation, newLocation);
			}
			else {
				text.insert(proposal);
			}
		}
	}
}
