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

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jdt.internal.ui.javaeditor.JavaTextSelection;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;
import org.jboss.tools.windup.ui.internal.rules.RulesetEditor;
import org.jboss.tools.windup.ui.internal.rules.RulesetEditorWrapper;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@SuppressWarnings({ "restriction" })
public class RulesetEditorTests extends WindupUiTest {

	@Test
	public void testOpenRulesetEditor() throws PartInitException {
		RulesetEditorWrapper editor = openRulesetEditor();
		assertTrue(editor != null);
	}
	
	@Test
	public void testCreateJavaRuleFromSelectedImplementInterface() throws PartInitException {
		RulesetEditorWrapper editor = openRulesetEditor();
		assertTrue(editor != null);
		
		Document document = editor.getDocument();
		assertTrue(document != null);
		
		JavaEditor javaEditor = openJavaEditor();
		assertTrue(javaEditor != null);
		
		int offset = 0;
		int length = 38;
		
		JavaTextSelection javaSelection = new JavaTextSelection(domService.getEditorInput(javaEditor), domService.getDocument(javaEditor), offset, length);
		
		ASTNode[] nodes = javaSelection.resolveSelectedNodes();
		assertTrue(nodes.length == 1);
		
		ASTNode node = nodes[0];
		assertTrue(node.getParent() instanceof TypeDeclaration && ((TypeDeclaration)node.getParent()).superInterfaceTypes().contains(node));
		
		List<Element> elements = ruleCreationService.createRulesFromEditorSelection(document, javaSelection);
		
		assertTrue(elements.size() == 1); 
	}
		
	private RulesetEditorWrapper openRulesetEditor() throws PartInitException {
		IFile demo = ResourcesPlugin.getWorkspace().getRoot().getProject("demo").getFile("demo/custom.rules.rhamt.xml");
		return (RulesetEditorWrapper)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new FileEditorInput(demo), RulesetEditor.ID);
	}
	
	private ITextEditor getTextEditor(RulesetEditorWrapper wrapper) {
		return (ITextEditor) wrapper.getAdapter(ITextEditor.class);
	}
	
	private JavaEditor openJavaEditor() throws PartInitException {
		IFile javaFile = ResourcesPlugin.getWorkspace().getRoot().getProject("demo").getFile("demo/src/org/windup/examples/migration.SampleService.java");
		return (JavaEditor)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new FileEditorInput(javaFile), "org.eclipse.jdt.ui.CompilationUnitEditor");
	}
}
