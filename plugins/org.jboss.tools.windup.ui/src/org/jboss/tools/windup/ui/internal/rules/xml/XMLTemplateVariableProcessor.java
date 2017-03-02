package org.jboss.tools.windup.ui.internal.rules.xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.text.templates.TemplateVariableResolver;

public class XMLTemplateVariableProcessor implements IContentAssistProcessor {

	private static Comparator<TemplateVariableProposal> fgTemplateVariableProposalComparator= new Comparator<TemplateVariableProposal>() {
		@Override
		public int compare(TemplateVariableProposal proposal0, TemplateVariableProposal proposal1) {
			return proposal0.getDisplayString().compareTo(proposal1.getDisplayString());
		}

		@Override
		public boolean equals(Object arg0) {
			return false;
		}

		@Override
		public int hashCode() {
			return super.hashCode();
		}
	};


	private TemplateContextType fContextType;

	public void setContextType(TemplateContextType contextType) {
		fContextType= contextType;
	}

	public TemplateContextType getContextType() {
		return fContextType;
	}

	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer,	int documentOffset) {

		if (fContextType == null)
			return null;

		List<TemplateVariableProposal> proposals= new ArrayList<>();

		String text= viewer.getDocument().get();
		int start= getStart(text, documentOffset);
		int end= documentOffset;

		String string= text.substring(start, end);
		String prefix= (string.length() >= 2)
			? string.substring(2)
			: null;

		int offset= start;
		int length= end - start;

		for (Iterator<TemplateVariableResolver> iterator= fContextType.resolvers(); iterator.hasNext(); ) {
			TemplateVariableResolver variable= iterator.next();

			if (prefix == null || variable.getType().startsWith(prefix))
				proposals.add(new TemplateVariableProposal(variable, offset, length, viewer));
		}

		Collections.sort(proposals, fgTemplateVariableProposalComparator);
		return proposals.toArray(new ICompletionProposal[proposals.size()]);
	}

	private int getStart(String string, int end) {
		int start= end;

		if (start >= 1 && string.charAt(start - 1) == '$')
			return start - 1;

		while ((start != 0) && Character.isUnicodeIdentifierPart(string.charAt(start - 1)))
			start--;

		if (start >= 2 && string.charAt(start - 1) == '{' && string.charAt(start - 2) == '$')
			return start - 2;

		return end;
	}

	@Override
	public IContextInformation[] computeContextInformation(ITextViewer viewer, int documentOffset) {
		return null;
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return new char[] {'$'};
	}

	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		return null;
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		return null;
	}
}
