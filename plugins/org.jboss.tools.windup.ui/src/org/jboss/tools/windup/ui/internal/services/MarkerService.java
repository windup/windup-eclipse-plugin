/*******************************************************************************
 * Copyright (c) 2019 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.services;

import static org.jboss.tools.windup.model.domain.WindupMarker.CONFIGURATION_ID;
import static org.jboss.tools.windup.model.domain.WindupMarker.DESCRIPTION;
import static org.jboss.tools.windup.model.domain.WindupMarker.EFFORT;
import static org.jboss.tools.windup.model.domain.WindupMarker.ELEMENT_ID;
import static org.jboss.tools.windup.model.domain.WindupMarker.RULE_ID;
import static org.jboss.tools.windup.model.domain.WindupMarker.SEVERITY;
import static org.jboss.tools.windup.model.domain.WindupMarker.SOURCE_SNIPPET;
import static org.jboss.tools.windup.model.domain.WindupMarker.TITLE;
import static org.jboss.tools.windup.model.domain.WindupMarker.URI_ID;
import static org.jboss.tools.windup.model.domain.WindupMarker.WINDUP_CLASSIFICATION_MARKER_ID;
import static org.jboss.tools.windup.model.domain.WindupMarker.WINDUP_HINT_MARKER_ID;
import static org.jboss.tools.windup.model.domain.WindupMarker.WINDUP_MARKER;
import static org.jboss.tools.windup.model.domain.WindupMarker.WINDUP_QUICKFIX_ID;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.model.domain.WindupMarker;
import org.jboss.tools.windup.model.domain.WorkspaceResourceUtils;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.explorer.IssueExplorer;
import org.jboss.tools.windup.ui.internal.explorer.MarkerUtil;
import org.jboss.tools.windup.windup.Classification;
import org.jboss.tools.windup.windup.ConfigurationElement;
import org.jboss.tools.windup.windup.Hint;
import org.jboss.tools.windup.windup.Issue;
import org.jboss.tools.windup.windup.MarkerElement;
import org.jboss.tools.windup.windup.QuickFix;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

/**
 * IMarkers get cached at three points of time:
 * 
 * case 1.) At Eclipse startup, if there was a previous execution of Windup,
 *     we will read and cache all Windup markers from the workspace.
 *     
 * case 2.) After Windup has been executed, any current markers from a previous
 * 	   execution of Windup will be deleted, and the current cache will be
 *     cleared. Then, all newly created markers resulting from the 
 *     execution of Windup will be cached.
 *     
 * case 3.) Windup markers can be deleted or they can be deleted and then re-created
 *     as a 'fixed' or 'stale' marker type. 'fixed' occurs when the user
 *     either manually modifies the line of code associated with the marker, and 
 *     then marks the issue as fixed, or applies a quickfix associated with 
 *     the markers' hint. 'stale' occurs if the line of code corresponding to 
 *     markers' hint is changed and the markers' hint contains a quickfix. 
 *     In either case, those specific markers will be updated in the cache.
 *     
 *  Thoughts:
 *   - What happens if a file is deleted? Should we update the cache?
 *   
 *     * The consequence of not updating the cache when a file is deleted is that
 *       we will need to always check to see if the IMarker exists after getting it
 *       from the cache.
 *       
 *       
 *   - What happens if a file is edited? Should we listen for resource changes and update update the cache?
 *     
 *     * Lines of code that contain a migration issue could be edited or deleted,
 *       in which case we should delete the marker if the line of code is deleted, 
 *       
 */
@Creatable
@Singleton
public class MarkerService {

	private BiMap<MarkerElement, IMarker> elementToMarkerMap = HashBiMap.create();
	private BiMap<IMarker, MarkerElement> markerToElementMap = elementToMarkerMap.inverse();
	private Multimap<IResource, MarkerElement> resourceElementsMap = ArrayListMultimap.create();
	
	@Inject private ModelService modelService;
	
	private void cache(IMarker marker, MarkerElement element) {
		elementToMarkerMap.put(element, marker);
		resourceElementsMap.put(marker.getResource(), element);
	}
	
	public IMarker findMarker(EObject element) {
		return elementToMarkerMap.get(element);
	}
	
	public List<MarkerElement> find(IResource resource) {
		return Lists.newArrayList(resourceElementsMap.get(resource));
	}
	
	@SuppressWarnings("unchecked")
	public <T extends MarkerElement> T find(IMarker marker) {
		return (T)markerToElementMap.get(marker);
	}
	
	private IssueExplorer getExplorer() {
		return (IssueExplorer)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(IssueExplorer.VIEW_ID);
	}
	
	private void notifyMarkersDeleted() {
		IssueExplorer explorer = getExplorer();
		if (explorer != null) {
			Display.getDefault().asyncExec(() -> {
				explorer.clear();
			});
		}
	}
	
	private void notifyMarkersCreated() {
		IssueExplorer explorer = getExplorer();
		if (explorer != null) {
			explorer.buildTree();
		}
	}
	
	private void notifyDeleted(EObject element) {
		if (element instanceof Issue) {
			IssueExplorer explorer = getExplorer();
			if (explorer != null) {
				explorer.delete((Issue)element);
			}
		}
	}
	
	public boolean cleanZombieMarker(Issue issue) {
		
		IMarker marker = (IMarker)issue.getMarker();
		
		if (marker == null || !marker.exists()) {
			modelService.deleteIssue(issue);
			elementToMarkerMap.remove(issue);
			if (marker != null && marker.getResource() != null) {
				resourceElementsMap.remove(marker.getResource(), issue);
			}
			notifyDeleted(issue);
			return true;
		}
		
		else {
			IResource resource = marker.getResource();
			if (resource == null || !resource.exists()) {
				modelService.deleteIssue(issue);
				elementToMarkerMap.remove(issue);
				resourceElementsMap.remove(marker.getResource(), issue);
				notifyDeleted(issue);
				return true;
			}
		}
		return false;
	}
	
	public void setFixed(Issue issue) {
		if (cleanZombieMarker(issue)) {
			return;
		}
		issue.setFixed(true);
		IMarker oldMarker = (IMarker)issue.getMarker();
		IMarker fixedMarker = createMarker(issue, oldMarker.getResource());
		try {
			fixedMarker.setAttributes(oldMarker.getAttributes());
			fixedMarker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO);
			oldMarker.delete();
			getExplorer().update(issue, oldMarker);
		} catch (CoreException e) {
			WindupUIPlugin.log(e);
		}
	}
	
	public void setStale(Issue issue) {
		if (cleanZombieMarker(issue)) {
			return;
		}
		issue.setStale(true);
		IMarker oldMarker = (IMarker)issue.getMarker();
		IMarker fixedMarker = createMarker(issue, oldMarker.getResource());
		try {
			fixedMarker.setAttributes(oldMarker.getAttributes());
			fixedMarker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO);
			oldMarker.delete();
			getExplorer().update(issue, oldMarker);
		} catch (CoreException e) {
			WindupUIPlugin.log(e);
		}
	}
	
	/**
	 * Case 1. Upon Eclipse startup, load cache from previous execution of Windup.
	 */
	private void loadCache() {
		List<IMarker> markers = MarkerUtil.collectAllWindupMarkers();
		for (IMarker marker : markers) {
			MarkerElement element = findElement(marker);
			if (element != null) {
				cache(marker, element);
			}
			else {
				deleteMarker(marker);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T extends MarkerElement> T findElement(IMarker marker) {
		String elementUri = marker.getAttribute(WindupMarker.URI_ID, null);
		URI uri = URI.createURI(elementUri);
		return (T)modelService.getModel().eResource().getEObject(uri.fragment());
	}
	
	public void delete(IMarker marker, EObject element) {
		elementToMarkerMap.remove(element);
		resourceElementsMap.remove(marker.getResource(), element);
		deleteMarker(marker);
		notifyDeleted(element);
	}
	
	public void clear() {
		for (Map.Entry<MarkerElement, IMarker> entry : elementToMarkerMap.entrySet()) {
			deleteMarker(entry.getValue());
		}
		elementToMarkerMap.clear();
		resourceElementsMap.clear();
		Display.getDefault().syncExec(() -> {
			notifyMarkersDeleted();
		});
	}
	
	public void clear(IResource resource) {
		List<MarkerElement> elements = Lists.newArrayList(resourceElementsMap.get(resource));
		for (MarkerElement element : elements) {
			IMarker marker = findMarker(element);
			delete(marker, element);
		}
	}
	
	private void deleteMarker(IMarker marker) {
		try {
			marker.delete();
		} catch (CoreException e) {
			WindupUIPlugin.log(e);
		}
	}
	
	/**
	 * Creates the markers corresponding to provided ConfigurationElement.
	 */
	public void generateMarkersForConfiguration(ConfigurationElement configuration) {
		clear();
		Display.getDefault().syncExec(() -> {
			try {
				WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
					@Override
					protected void execute(IProgressMonitor monitor)
							throws CoreException, InvocationTargetException, InterruptedException {
						monitor.beginTask(Messages.generateIssues, getTotalMarkerCount(configuration));
						monitor.subTask(Messages.generateIssues);
						createMarkers(configuration, monitor);
					}
				};
				new ProgressMonitorDialog(Display.getDefault().getActiveShell()).run(false, false, op);
				notifyMarkersCreated();
			} catch (InvocationTargetException | InterruptedException e) {
				Display.getDefault().syncExec(() -> {
					MessageDialog.openError(Display.getDefault().getActiveShell(), 
							Messages.launchErrorTitle, Messages.markersCreateError);
				});
				WindupUIPlugin.log(e);
			}
		});
	}
	
	private void createMarkers(ConfigurationElement configuration, IProgressMonitor monitor) throws CoreException {
		if (configuration.getWindupResult() == null) {
			return;
		}
		int count = 0;
		for (Iterator<Issue> iter = configuration.getWindupResult().getIssues().iterator(); iter.hasNext();) {
			Issue issue = iter.next();
			IFile resource = WorkspaceResourceUtils.getResource(issue.getFileAbsolutePath());
			if (resource == null || !resource.exists()) {
				WindupUIPlugin.logErrorMessage("MarkerService:: No resource associated with issue file: " + issue.getFileAbsolutePath()); //$NON-NLS-1$
				iter.remove();
				continue;
			}
			createWindupMarker(issue, configuration, resource);
			monitor.worked(++count);
		}
	}
	
	/**
	 * Helper method that actually creates the marker on the specified resource for the specified Windup migration issue.
	 */
	private void createWindupMarker(Issue issue, ConfigurationElement configuration, IResource resource) throws CoreException {
		
		IMarker marker = createMarker(issue, resource);
		issue.setMarker(marker);
		
		if (issue instanceof Hint) {
			Hint hint = (Hint)issue;
			marker.setAttribute(IMarker.LINE_NUMBER, hint.getLineNumber());
			marker.setAttribute(IMarker.LINE_NUMBER, hint.getLineNumber());
			//marker.setAttribute(IMarker.CHAR_START, hint.getColumn());
			//marker.setAttribute(IMarker.CHAR_END, hint.getLength());
			marker.setAttribute(SOURCE_SNIPPET, hint.getSourceSnippet());
		}
		else {
			//Classification classification = (Classification)issue;
			marker.setAttribute(IMarker.LINE_NUMBER, 1);
			marker.setAttribute(IMarker.CHAR_START, 0);
			marker.setAttribute(IMarker.CHAR_END, 0);
		}
		
		IJavaElement element = JavaCore.create(resource);
		if (element != null) {
			marker.setAttribute(ELEMENT_ID, element.getHandleIdentifier());
		}
		
		marker.setAttribute(DESCRIPTION, issue.getMessageOrDescription());
		marker.setAttribute(IMarker.MESSAGE, issue.getTitle());
		marker.setAttribute(TITLE, issue.getTitle());
		
		marker.setAttribute(URI_ID, EcoreUtil.getURI(issue).toString());
		marker.setAttribute(CONFIGURATION_ID, configuration.getName());
		marker.setAttribute(IMarker.SEVERITY, MarkerUtil.convertSeverity(issue.getSeverity()));
		marker.setAttribute(SEVERITY, issue.getSeverity());
        marker.setAttribute(RULE_ID, issue.getRuleId());
        marker.setAttribute(EFFORT, String.valueOf(issue.getEffort()));
        marker.setAttribute(IMarker.USER_EDITABLE, false);
        marker.setAttribute(WINDUP_MARKER, true);
        
        createQuickfixMarkers(issue);
	}
	
	private void createQuickfixMarkers(Issue issue) throws CoreException {
		for (QuickFix quickfix : issue.getQuickFixes()) {
			IFile resource = WorkspaceResourceUtils.getResource(quickfix.getFile());
			IMarker marker = createMarker(quickfix, resource);
			quickfix.setMarker(marker);
			marker.setAttribute(URI_ID, EcoreUtil.getURI(quickfix).toString());
		}
	}
	
	private IMarker createMarker(MarkerElement element, IResource resource) {
		String type = "";
		if (element instanceof Hint) {
			type = WINDUP_HINT_MARKER_ID;
		}
		else if (element instanceof Classification) {
			type = WINDUP_CLASSIFICATION_MARKER_ID;
		}
		else if (element instanceof QuickFix) {
			type = WINDUP_QUICKFIX_ID;
		}
		try {
			IMarker marker = resource.createMarker(type);
			cache(marker, element);
			return marker;
		} catch (CoreException e) {
			WindupUIPlugin.log(e);
		}
		return null;
	}
	
	/**
	 * Returns the total number of markers that will be created. Used for reporting progress.
	 */
	private int getTotalMarkerCount(ConfigurationElement configuration) {
		int count = 0;
		if (configuration.getWindupResult() == null) {
			return count;
		}
		
		for (Issue issue : configuration.getWindupResult().getIssues()) {
			count++;
			if (issue instanceof Hint) {
				Hint hint = (Hint)issue;
				count += hint.getQuickFixes().size();
			}
			else if (issue instanceof Classification) {
				Classification classification = (Classification)issue;
				count += classification.getQuickFixes().size();
			}
		}
		return count;
	}
	
	@PostConstruct
	private void init() {
		loadCache();
	}
}
