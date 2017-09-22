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
package org.jboss.tools.windup.ui.tests;

import static org.junit.Assert.assertTrue;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.jboss.tools.windup.ui.internal.rules.RulesetEditor;
import org.jboss.tools.windup.ui.internal.rules.RulesetEditorWrapper;
import org.junit.Test;

public class RulesetEditorTests extends WindupUiTest {

	@Test
	public void testOpenRulesetEditor() throws PartInitException {
		RulesetEditorWrapper editor = openRulesetEditor();
		assertTrue(editor != null);
	}
	
	private RulesetEditorWrapper openRulesetEditor() throws PartInitException {
		IFile demo = ResourcesPlugin.getWorkspace().getRoot().getProject("demo").getFile("demo/custom.rules.rhamt.xml");
		return (RulesetEditorWrapper)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new FileEditorInput(demo), RulesetEditor.ID);
	}
}
