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
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.internal.core.CompilationUnit;
import org.eclipse.jdt.internal.corext.dom.GenericVisitor;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jdt.ui.SharedASTProvider;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.RulesetConstants;
import org.jboss.tools.windup.ui.internal.rules.RulesetEditor;
import org.jboss.tools.windup.ui.internal.rules.RulesetEditorWrapper;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.google.common.collect.Lists;

@SuppressWarnings({ "restriction" })
public class RulesetEditorTests extends WindupUiTest {

	@Test
	public void testOpenRulesetEditor() throws PartInitException {
		RulesetEditorWrapper editor = openRulesetEditor();
		assertTrue(editor != null);
	}
	
	@Test
	public void testCreateJavaRuleFromSelectedImport() throws PartInitException {
		RulesetEditorWrapper editor = openRulesetEditor();
		assertTrue(editor != null);
		
		Document document = editor.getDocument();
		assertTrue(document != null);
		
		NodeList rules = document.getElementsByTagName(RulesetConstants.RULE_NAME);
		assertTrue(rules.getLength() == 0);
		
		JavaEditor javaEditor = openJavaEditor();
		assertTrue(javaEditor != null);
		
		List<ASTNode> nodes = Lists.newArrayList();

		CompilationUnit root = (CompilationUnit)domService.getEditorInput(javaEditor);
		
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(root);
		parser.setResolveBindings(true);
		ASTNode node = parser.createAST(new NullProgressMonitor());
		
		node.accept(new ASTVisitor() {
			@Override
			public boolean visit(TypeDeclaration node) {
				return true;
			}
			@Override
			public boolean visit(AnnotationTypeDeclaration node) {
				// TODO Auto-generated method stub
				return super.visit(node);
			}
			@Override
			public boolean visit(AnnotationTypeMemberDeclaration node) {
				// TODO Auto-generated method stub
				return super.visit(node);
			}
		});
		assertTrue(!nodes.isEmpty());
		
		ruleCreationService.createRuleFromJavaEditorSelection(document, new ASTNode[] {nodes.get(0)});
		
		rules = document.getElementsByTagName(RulesetConstants.RULE_NAME);
		assertTrue(rules.getLength() == 1);
	}
		
	private RulesetEditorWrapper openRulesetEditor() throws PartInitException {
		IFile demo = ResourcesPlugin.getWorkspace().getRoot().getProject("demo").getFile("demo/custom.rules.rhamt.xml");
		return (RulesetEditorWrapper)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new FileEditorInput(demo), RulesetEditor.ID);
	}
	
	private ITextEditor getTextEditor(RulesetEditorWrapper wrapper) {
		return (ITextEditor) wrapper.getAdapter(ITextEditor.class);
	}
	
	private JavaEditor openJavaEditor() throws PartInitException {
		IFile javaFile = ResourcesPlugin.getWorkspace().getRoot().getProject("demo").getFile("demo/src/org/windup/examples/migration/SampleService.java");
		
		IJavaElement element = JavaCore.create(javaFile);
		CompilationUnit unit = (CompilationUnit)element;

		return (JavaEditor)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new FileEditorInput(javaFile), "org.eclipse.jdt.ui.CompilationUnitEditor");
	}
}
