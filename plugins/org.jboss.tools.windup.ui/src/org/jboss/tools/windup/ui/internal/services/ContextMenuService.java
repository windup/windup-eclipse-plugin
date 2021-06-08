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
package org.jboss.tools.windup.ui.internal.services;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.IVerticalRulerInfo;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.internal.e4.compatibility.CompatibilityPart;
import org.eclipse.ui.texteditor.AbstractMarkerAnnotationModel;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.ITextEditorExtension;
import org.jboss.tools.windup.model.domain.WindupMarker;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.explorer.IssueExplorer;
import org.jboss.tools.windup.ui.internal.explorer.QuickfixService;
import org.jboss.tools.windup.ui.internal.issues.IssueDetailsView;
import org.jboss.tools.windup.windup.Hint;
import org.jboss.tools.windup.windup.Issue;
import org.jboss.tools.windup.windup.QuickFix;

import com.google.common.collect.Lists;

/**
 * Service for populating active editor context menu.
 */
@SuppressWarnings("restriction")
public class ContextMenuService implements MouseListener, IMenuListener {
	
    private static final IMarker[] EMPTY = new IMarker[0];
	
	private IVerticalRulerInfo ruler;
	private ITextEditor editor;
	
	@Inject private MarkerService markerService;
	@Inject private EPartService partService;
	@Inject private QuickfixService quickfixService;
	
	private List<IMarker> markers = Lists.newArrayList();
	
	private WindupAction SHOW_IN_EXPLORER_ACTION = new WindupAction(Messages.showInIssueExplorer,
			WindupUIPlugin.getImageDescriptor(WindupUIPlugin.IMG_WINDUP), () -> {
			MPart part = partService.showPart(IssueExplorer.VIEW_ID, PartState.ACTIVATE);
			CompatibilityPart compatPart = (CompatibilityPart)part.getObject();
			IssueExplorer view = (IssueExplorer)compatPart.getPart();
			view.showIssue(markers.get(0));
		});
	
	private WindupAction SHOW_DETAILS_ACTION = new WindupAction(Messages.showIssueDetails,
			WindupUIPlugin.getImageDescriptor(WindupUIPlugin.IMG_WINDUP), () -> {
			MPart part = partService.showPart(IssueDetailsView.ID, PartState.ACTIVATE);
			IssueDetailsView view = (IssueDetailsView)part.getObject();
			view.showIssueDetails(markers.get(0));
		});
	
	private void previewQuickFix() {
		IMarker marker = markers.get(0);
		Issue issue = markerService.find(marker);
		if (issue instanceof Hint) {
			Hint hint = (Hint)issue;
			quickfixService.previewQuickFix(hint, marker);
		}
	}
	
	private void applyQuickFix() {
		IMarker marker = markers.get(0);
		Issue issue = markerService.find(marker);
		if (issue instanceof Hint) {
			for (QuickFix quickfix : issue.getQuickFixes()) {
				quickfixService.applyQuickFix(quickfix);
			}
		}
	}
	
	private void markAsFixed() {
		IMarker marker = markers.get(0);
		Issue issue = markerService.find(marker);
		markerService.setFixed(issue);
	}
	
	public static class WindupAction extends Action {
		private Runnable runner;
		public WindupAction(String label, ImageDescriptor descriptor, Runnable runner) {
			super(label, descriptor);
			this.runner = runner;
		}
		@Override
		public void run() {
			runner.run();
		}
	}
		
	@Inject
	private void execute(@Named(IServiceConstants.ACTIVE_PART) MPart part) {
		if (editor != null) {
			if (ruler != null) {
				Control control = ruler.getControl();
				if (control != null && !control.isDisposed()) {
					control.removeMouseListener(this);
				}
			}
			if (editor instanceof ITextEditorExtension) {
				((ITextEditorExtension)editor).removeRulerContextMenuListener(this);
			}
			editor = null;
			ruler = null;
		}
		if (part != null) {
			Object client = part.getObject();
			if (client instanceof CompatibilityPart) {
				IWorkbenchPart workbenchPart = ((CompatibilityPart) client).getPart();
				if (!(workbenchPart instanceof ITextEditor)) {
					workbenchPart = workbenchPart.getAdapter(ITextEditor.class);
				}
				if (workbenchPart instanceof ITextEditor) {
					this.editor = (ITextEditor)workbenchPart;
					if (editor instanceof ITextEditorExtension) {
						((ITextEditorExtension)editor).addRulerContextMenuListener(this);
					}
					
					this.ruler = (IVerticalRulerInfo)editor.getAdapter(IVerticalRulerInfo.class);
					if (ruler != null) {
						Control control = ruler.getControl();
						if (control != null && !control.isDisposed()) {
							control.addMouseListener(this);
						}
					}
				}
				else {
					this.editor = null;
					this.ruler = null;
				}
			}
		}
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {}
	@Override
	public void mouseDown(MouseEvent e) {}
	@Override
	public void mouseUp(MouseEvent e) {}

	@Override
	public void menuAboutToShow(IMenuManager manager) {
		markers.clear();
		
		IResource resource = (IResource) editor.getEditorInput().getAdapter(IFile.class);
        if(resource == null){
            return;
        }
		
        IMarker[] allMarkers = getMarkers(resource, IResource.DEPTH_ZERO);
        if(allMarkers.length == 0) {
            return;
        }
        
		AbstractMarkerAnnotationModel model = getModel();
		IDocument document = getDocument();
		for (int i = 0; i < allMarkers.length; i++) {
			if (includesRulerLine(model.getMarkerPosition(allMarkers[i]), document)) {
				if (isWindupMarker(allMarkers[i])) {
					markers.add(allMarkers[i]);
				} 
			}
		}
		if (!markers.isEmpty()) {
			manager.add(SHOW_IN_EXPLORER_ACTION);
			manager.add(SHOW_DETAILS_ACTION);
			IMarker marker = markers.get(0);
			Issue issue = markerService.find(marker);
			if (!issue.isStale() && !issue.isFixed() && !issue.getQuickFixes().isEmpty()) {
				manager.add(new WindupAction(Messages.PreviewQuickFix, null, this::previewQuickFix));
				manager.add(new WindupAction(Messages.ApplyQuickFix, null, this::applyQuickFix));
			}
			if (!issue.isStale() && !issue.isFixed()) {
				manager.add(new WindupAction(Messages.MarkAsFixed, 
						WindupUIPlugin.getImageDescriptor(WindupUIPlugin.IMG_FIXED), 
						this::markAsFixed));
			}
		}
	}
	
	private static boolean isWindupMarker(IMarker marker) {
		return marker != null && marker.exists();
	}
	
	private boolean includesRulerLine(Position position, IDocument document) {
		if (position != null && ruler != null) {
			try {
				int markerLine = document.getLineOfOffset(position.getOffset());
				int line = ruler.getLineOfLastMouseButtonActivity();
				if (line == markerLine) {
					return true;
				}
			} catch (BadLocationException e) {
				WindupUIPlugin.log(e);
			}
		}
		return false;
	}
	
	private static IMarker[] getMarkers(IResource fileOrFolder, int depth) {
        if(fileOrFolder.getType() == IResource.PROJECT) {
            if(!fileOrFolder.isAccessible()) {
                return EMPTY;
            }
        }
        try {
            IMarker[] markers1 = fileOrFolder.findMarkers(WindupMarker.WINDUP_HINT_MARKER_ID, true, depth);
            IMarker[] markers2 = fileOrFolder.findMarkers(WindupMarker.WINDUP_CLASSIFICATION_MARKER_ID, true, depth);
            return (IMarker[]) ArrayUtils.addAll(markers1, markers2);
        } catch (CoreException e) {
            WindupUIPlugin.log(e);
        }
        return EMPTY;
    }
	
	private AbstractMarkerAnnotationModel getModel() {
        if(editor == null) {
            return null;
        }
        IDocumentProvider provider = editor.getDocumentProvider();
        IAnnotationModel model = provider.getAnnotationModel(editor.getEditorInput());
        if (model instanceof AbstractMarkerAnnotationModel) {
            return (AbstractMarkerAnnotationModel) model;
        }
        return null;
    }
	
	private IDocument getDocument() {
		IDocumentProvider provider = editor.getDocumentProvider();
		return provider.getDocument(editor.getEditorInput());
	}
}
