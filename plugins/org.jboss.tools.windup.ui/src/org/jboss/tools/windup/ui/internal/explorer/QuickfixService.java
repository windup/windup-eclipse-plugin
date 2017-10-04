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

import java.io.File;
import java.rmi.RemoteException;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.text.java.JavaFormattingStrategy;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.formatter.MultiPassContentFormatter;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.formatter.XMLFormatterFormatProcessor;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.jboss.tools.windup.model.domain.WorkspaceResourceUtils;
import org.jboss.tools.windup.model.util.DocumentUtils;
import org.jboss.tools.windup.runtime.WindupRmiClient;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.rules.xml.XMLRulesetModelUtil;
import org.jboss.tools.windup.ui.internal.services.MarkerService;
import org.jboss.tools.windup.windup.ConfigurationElement;
import org.jboss.tools.windup.windup.Hint;
import org.jboss.tools.windup.windup.Input;
import org.jboss.tools.windup.windup.Issue;
import org.jboss.tools.windup.windup.QuickFix;
import org.jboss.windup.tooling.ExecutionBuilder;
import org.jboss.windup.tooling.data.QuickfixType;
import org.jboss.windup.tooling.quickfix.QuickfixLocationDTO;

import com.google.common.base.Objects;

/**
 * Service for interacting with quick fixes.
 */

@SuppressWarnings("restriction")
@Creatable
@Singleton
public class QuickfixService {
	
	@Inject private MarkerService markerService;
	@Inject private WindupRmiClient windupClient;
	
	public void applyQuickFix(QuickFix quickfix) {
		IMarker quickfixMarker =(IMarker)quickfix.getMarker();
		IResource quickfixResource = quickfixMarker.getResource();
		IDocument document = findDocument(quickfixResource);
		IResource newResource = getQuickFixedResource(document, quickfix, quickfixMarker);
		
		if (document == null) {
			DocumentUtils.replace(quickfixMarker.getResource(), newResource);
		}
		
		markerService.delete(quickfixMarker, quickfix);
		
		Hint hint = (Hint)quickfix.eContainer();
		IMarker hintMarker = (IMarker)hint.getMarker();
		if (hintMarker.exists()) {
			/*
			 * This is temporary. We're deleting all Windup markers associated with a resource that has been 
			 * transformed by the quickfix. Ideally, we should only delete the ones that have become stale,
			 * but because the MarkerSync service hasn't yet been notified, we're clearing all of them for now.
			 */
			IResource hintResource = hintMarker.getResource();
			if (!Objects.equal(hintResource, quickfixResource)) {
				markerService.clear(quickfixResource);
			}
			
			if (!hint.isFixed()) {
				if (hintMarker != null && hintMarker.exists())	{
					markerService.setFixed(hint);
				}
			}
		}
		else {
			markerService.clear(quickfixResource);
		}
	}
	
	private IDocument findDocument(IResource resource) {
		FileEditorInput input = new FileEditorInput((IFile)resource);
		IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findEditor(input);
		if (editor != null) {
			ITextEditor textEditor = editor.getAdapter(ITextEditor.class);
			if (textEditor != null) {
				return textEditor.getDocumentProvider().getDocument(input);
			}
		}
		return null;
	}

	public IResource getQuickFixedResource(IDocument document, QuickFix quickFix, IMarker marker) {
		Hint hint = (Hint)quickFix.eContainer();
		IResource original = marker.getResource();
		TempProject project = new TempProject();
		if (QuickfixType.REPLACE.toString().equals(quickFix.getQuickFixType())) {
			int lineNumber = hint.getLineNumber()-1;
			String searchString = quickFix.getSearchString();
			String replacement = quickFix.getReplacementString();
			if (document != null) {
				replacement = replacement != null ? replacement : "";
				DocumentUtils.replace(document, lineNumber, searchString, replacement);
				return marker.getResource();
			}
			else {
				document = DocumentUtils.replace(original, lineNumber, searchString, replacement);
				return project.createResource(document.get());
			}
		}
		else if (QuickfixType.DELETE_LINE.toString().equals(quickFix.getQuickFixType())) {
			int lineNumber = hint.getLineNumber()-1;
			if (document != null) {
				DocumentUtils.deleteLine(original, lineNumber);
				return marker.getResource();
			}
			else {
				document = DocumentUtils.deleteLine(original, lineNumber);
				return project.createResource(document.get());
			}
		}
		else if (QuickfixType.INSERT_LINE.toString().equals(quickFix.getQuickFixType())) {
			int lineNumber = hint.getLineNumber();
			lineNumber = lineNumber > 1 ? lineNumber - 2 : lineNumber-1;
			String newLine = quickFix.getReplacementString();
			if (document != null) {
				DocumentUtils.insertLine(original, lineNumber, newLine);
				return marker.getResource();
			}
			else {
				document = DocumentUtils.insertLine(original, lineNumber, newLine);
				return project.createResource(document.get());
			}
		}
		else if (QuickfixType.TRANSFORMATION.toString().equals(quickFix.getQuickFixType())) {
			
			// TODO: Quickfixes need their own location info.
			int lineNumber = hint.getLineNumber(); // marker.getAttribute(IMarker.LINE_NUMBER, hint.getLineNumber());
			int column = hint.getColumn(); // marker.getAttribute(IMarker.CHAR_START, hint.getColumn());
			int length = hint.getLength(); //marker.getAttribute(IMarker.CHAR_END, hint.getLength());
			
			Input input = (Input)hint.eContainer().eContainer();
			ConfigurationElement configuration = (ConfigurationElement)input.eContainer();
			String reportBaseLocation = configuration.getReportDirectory();
			File reportDirectory = new File(reportBaseLocation);
			
	        QuickfixLocationDTO locationDTO = new QuickfixLocationDTO(
	        		reportDirectory,
	        		original.getLocation().toFile(),
	        		lineNumber,
	        		column,
	        		length);
	        try {
		        	if (windupClient.isWindupServerRunning()) {
		        		ExecutionBuilder builder = windupClient.getExecutionBuilder();
			        	String preview = builder.transform(quickFix.getTransformationId(), locationDTO);
			        	preview = format(marker.getResource(), preview);
			        	return project.createResource(preview);
		        	}
	        } catch (RemoteException e) {
	        		WindupUIPlugin.log(e);
	        }
	        
		}
		return null;
	}
	
	public void previewQuickFix(Issue issue, IMarker marker) {
		Shell shell = Display.getCurrent().getActiveShell();
		QuickFixDiffDialog dialog = new QuickFixDiffDialog(shell, issue, this);
		if (dialog.open() == IssueConstants.APPLY_FIX) {
			for (QuickFix quickfix : dialog.getQuickfixes()) {
				applyQuickFix(quickfix);
			}
		}
	}
	
	private String format(IResource resource, String contents) {
		/*
		 * Java Formatting
		 */
		if (JavaCore.create(resource) != null) {
			MultiPassContentFormatter formatter= new MultiPassContentFormatter(/*IJavaPartitions.JAVA_PARTITIONING*/"__dftl_partitioning", IDocument.DEFAULT_CONTENT_TYPE);
			formatter.setMasterStrategy(new JavaFormattingStrategy());
			Document document = new Document(contents);
			formatter.format(document, new Region(0, document.getLength()));
			return document.get();
		}
		/*
		 * XML Formatting
		 */
		IDOMModel xmlModel = XMLRulesetModelUtil.getModel(WorkspaceResourceUtils.getFile(resource.getLocation().toString()), true);
		if (xmlModel != null) {
			XMLFormatterFormatProcessor formatProcessor = new XMLFormatterFormatProcessor();
			try {
				TempProject project = new TempProject();
				IFile file = (IFile)project.createResource(contents);
				IStructuredModel model = StructuredModelManager.getModelManager().createUnManagedStructuredModelFor(file);
				formatProcessor.formatModel(model);
				StructuredModelManager.getModelManager().saveStructuredDocument(model.getStructuredDocument(), file);
				return FileUtils.readFileToString(file.getLocation().toFile());
			} catch (Exception e) {
				WindupUIPlugin.log(e);
			}
		}
		
		return contents;
	}
	
	public static boolean isIssueFixable(Issue issue) {
		return !issue.isStale() && !issue.isFixed() && !issue.getQuickFixes().isEmpty();
	}
}
