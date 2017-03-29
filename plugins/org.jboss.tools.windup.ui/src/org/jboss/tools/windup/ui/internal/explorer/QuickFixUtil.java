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
import java.util.Dictionary;
import java.util.Hashtable;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.text.java.JavaFormattingStrategy;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.formatter.MultiPassContentFormatter;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.formatter.XMLFormatterFormatProcessor;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.jboss.tools.windup.model.domain.WindupConstants;
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
 * Utility for interacting with quick fixes.
 */
@SuppressWarnings("restriction")
public class QuickFixUtil {
	
	public static void applyQuickFix(QuickFix quickfix, IEventBroker broker, MarkerService markerService, WindupRmiClient windupClient) {
		IMarker quickfixMarker = markerService.findMarker(quickfix);
		if (quickfixMarker != null) {
			IResource newResource = QuickFixUtil.getQuickFixedResource(quickfix, quickfixMarker, windupClient, markerService);
			DocumentUtils.replace(quickfixMarker.getResource(), newResource);
			MarkerUtil.deleteMarker(quickfixMarker);
			
			/*
			 * We don't know what the transformation did to the file, so we clear all Windup marker associated with it.
			 */
			if (QuickfixType.TRANSFORMATION.toString().equals(quickfix.getQuickFixType())) {
				/*
				 * TODO: But if there's multiple issues associated with the resource, we now no longer can see them.
				 * But if we don't, if lines of code associated with hints are changed, they'll get marked as invalid,
				 * which doens't look good in UI. 
				 */
				//markerService.deleteWindupMarkers(marker.getResource());
			}
			
			/*
			 * What we could do is, go through the Windup markers on the marker.getResource(), 
			 * and if the issue has become stale (ie., the lines associated with an issue have changed)
			 * then delete the issue. Issue - MarkerSyncService doesn't run by now, so issues haven't been
			 * marked as stale. Of course, this would include other stale issues not created by
			 * the quickfix, but we probably could specially mark those if necessary to prevent deleting them.
			 */
			IResource quickfixResource = quickfixMarker.getResource();
			
			Hint hint = (Hint)quickfix.eContainer();
			IMarker hintMarker = markerService.findMarker(hint);

			/*
			 * This is temporary. We're deleting all Windup markers associated with a resource that has been 
			 * transformed by the quickfix. Ideally, we should only delete the onese that have become stale,
			 * but because the MarkerSync service hasn't yet been notified, we're clearing all of them for now.
			 */
			if (hintMarker != null) {
				IResource hintResource = hintMarker.getResource();
				if (!Objects.equal(hintResource, quickfixResource)) {
					for (IMarker dirtyMarker : MarkerUtil.collectWindupMarkers(quickfixResource)) {
						MarkerUtil.deleteMarker(dirtyMarker);
						Dictionary<String, Object> props = new Hashtable<String, Object>();
						props.put(WindupConstants.EVENT_ISSUE_MARKER, dirtyMarker);
						broker.post(WindupConstants.MARKER_DELETED, props);
					}
				}
			}
			
			if (!hint.isFixed()) {
				if (hintMarker != null && hintMarker.exists())	{
					QuickFixUtil.setFixed(hint, hintMarker, broker, markerService);
				}
			}
		}
	}
	
	public static void setFixed(Issue issue, IMarker marker, IEventBroker broker, MarkerService markerService) {
		Dictionary<String, Object> props = new Hashtable<String, Object>();
		IMarker updatedMarker = markerService.createFixedMarker(marker, issue);
		props.put(WindupConstants.EVENT_ISSUE_MARKER, marker);
		props.put(WindupConstants.EVENT_ISSUE_MARKER_UPDATE, updatedMarker);
		broker.post(WindupConstants.MARKER_CHANGED, props);
	}
	
	public static IResource getQuickFixedResource(QuickFix quickFix, IMarker marker, WindupRmiClient windupClient, MarkerService markerService) {
		Hint hint = (Hint)quickFix.eContainer();
		IResource original = marker.getResource();
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
		        	preview = QuickFixUtil.format(marker.getResource(), preview);
		        	return project.createResource(preview);
	        	}
	        } catch (RemoteException e) {
	        	WindupUIPlugin.log(e);
	        }
	        
		}
		return null;
	}
	
	public static void previewQuickFix(Hint hint, IMarker marker, IEventBroker broker,
			MarkerService markerService, WindupRmiClient windupClient) {
		Shell shell = Display.getCurrent().getActiveShell();
		QuickFixDiffDialog dialog = new QuickFixDiffDialog(shell, hint, windupClient, markerService);
		if (dialog.open() == IssueConstants.APPLY_FIX) {
			for (QuickFix quickfix : dialog.getQuickfixes()) {
				QuickFixUtil.applyQuickFix(quickfix, broker, markerService, windupClient);
			}
		}
	}
	
	public static boolean isIssueFixable(Issue issue) {
		return !issue.isStale() && !issue.isFixed() && !issue.getQuickFixes().isEmpty();
	}
	
	private static String format(IResource resource, String contents) {
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
		IDOMModel xmlModel = XMLRulesetModelUtil.getModel(resource.getLocation().toString(), true);
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
}
