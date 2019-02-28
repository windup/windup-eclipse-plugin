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
package org.jboss.tools.windup.ui.internal.explorer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.FileUtils;
import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareUI;
import org.eclipse.compare.CompareViewerSwitchingPane;
import org.eclipse.compare.IEncodedStreamContentAccessor;
import org.eclipse.compare.IResourceProvider;
import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.DiffNode;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;

/**
 * Dialog for viewing a difference.
 */
public abstract class DiffDialog extends Dialog {
	
	private static final int WIDTH = 950;
	private static final int HEIGHT = 600;
	
	private ComparePreviewer viewer;
	
	public DiffDialog(Shell shell) {
		super(shell);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(WIDTH, HEIGHT);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Control control = doCreateDialogArea(parent);
		loadPreview();
		return control;
	}
	
	protected Control doCreateDialogArea(Composite parent) {
		Control control = super.createDialogArea(parent);
		viewer = new ComparePreviewer((Composite)control);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(viewer.getViewer().getControl().getParent());
		return control;
	}
	
	protected abstract IResource computeLeft();
	
	protected abstract IResource computeRight();
	
	protected void loadPreview() {
		try {
			IResource left = computeLeft();
			IResource right = computeRight();
			if (left != null && right != null) {
				String leftContents = FileUtils.readFileToString(left.getLocation().toFile());
				String rightContents = FileUtils.readFileToString(right.getLocation().toFile());
				viewer.setInput(new DiffNode(
						new CompareElement(leftContents, left.getFileExtension(), left),
						new CompareElement(rightContents, left.getFileExtension(), right)));
			}
			else {
				MessageDialog.openError(viewer.getShell(), "Quickfix Error", "Error while computing quickfix sources"); //$NON-NLS-1$
			}
		} catch (IOException e) {
			WindupUIPlugin.log(e);
		}
	}
	
	@Override
	protected boolean isResizable() {
		return true;
	}
	
	private static class ComparePreviewer extends CompareViewerSwitchingPane {
		private CompareConfiguration fCompareConfiguration;
		public ComparePreviewer(Composite parent) {
			super(parent, SWT.BORDER | SWT.FLAT, true);
			fCompareConfiguration= new CompareConfiguration();
			fCompareConfiguration.setLeftEditable(false);
			fCompareConfiguration.setLeftLabel(Messages.ComparePreviewer_original_source);
			fCompareConfiguration.setRightEditable(false);
			fCompareConfiguration.setRightLabel(Messages.ComparePreviewer_migrated_source);
			Dialog.applyDialogFont(this);
		}
		@Override
		protected Viewer getViewer(Viewer oldViewer, Object input) {
			return CompareUI.findContentViewer(oldViewer, (ICompareInput)input, this, fCompareConfiguration);
		}
		@Override
		public void setText(String text) {
			super.setText(text);
			setImage(WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_WINDUP));
		}
	}
	
	private static class CompareElement implements ITypedElement, IEncodedStreamContentAccessor, IResourceProvider {

		private static final String ENCODING = "UTF-8";	//$NON-NLS-1$
		
		private String content;
		private String type;
		private IResource resource;
		
		public CompareElement(String content, String type, IResource resource) {
			this.content = content;
			this.type = type;
			this.resource = resource;
		}
		@Override
		public String getName() {
			return "";
		}
		@Override
		public Image getImage() {
			return null;
		}
		@Override
		public String getType() {
			return type;
		}
		@Override
		public InputStream getContents() throws CoreException {
			try {
				return new ByteArrayInputStream(content.getBytes(ENCODING));
			} catch (UnsupportedEncodingException e) {
				return new ByteArrayInputStream(content.getBytes());
			}
		}
		@Override
		public String getCharset() {
			return ENCODING;
		}
		@Override
		public IResource getResource() {
			return resource;
		}
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IssueConstants.OK, Messages.ComparePreviewer_donePreviewFix, true);
		Button button = createButton(parent, IssueConstants.APPLY_FIX, Messages.ComparePreviewer_applyFix, false);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				setReturnCode(IssueConstants.APPLY_FIX);
				close();
			}
		});
	}
}
