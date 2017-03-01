package org.jboss.tools.windup.ui.internal.rules.xml;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.templates.TemplateVariableResolver;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;

final class TemplateVariableProposal implements ICompletionProposal {

	private TemplateVariableResolver fVariable;
	private int fOffset;
	private int fLength;
	private ITextViewer fViewer;

	private Point fSelection;

	/**
	 * Creates a template variable proposal.
	 *
	 * @param variable the template variable
	 * @param offset the offset to replace
	 * @param length the length to replace
	 * @param viewer the viewer
	 */
	public TemplateVariableProposal(TemplateVariableResolver variable, int offset, int length, ITextViewer viewer) {
		fVariable= variable;
		fOffset= offset;
		fLength= length;
		fViewer= viewer;
	}

	@Override
	public void apply(IDocument document) {

		try {
			String variable= fVariable.getType().equals("dollar") ? "$$" : "${" + fVariable.getType() + '}'; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			document.replace(fOffset, fLength, variable);
			fSelection= new Point(fOffset + variable.length(), 0);

		} catch (BadLocationException e) {
			Shell shell= fViewer.getTextWidget().getShell();
			MessageDialog.openError(shell, "Error while applying completion", e.getMessage()); //$NON-NLS-1$
		}
	}

	@Override
	public Point getSelection(IDocument document) {
		return fSelection;
	}

	@Override
	public String getAdditionalProposalInfo() {
		return null;
	}

	@Override
	public String getDisplayString() {
		return fVariable.getType() + " - " + fVariable.getDescription(); //$NON-NLS-1$
	}

	@Override
	public Image getImage() {
		return null;
	}

	@Override
	public IContextInformation getContextInformation() {
		return null;
	}
}