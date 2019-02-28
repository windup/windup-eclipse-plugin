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
package org.jboss.tools.windup.ui.tests;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.internal.core.CompilationUnit;
import org.eclipse.jdt.ui.SharedASTProvider;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.RulesetConstants;
import org.jboss.tools.windup.ui.internal.rules.RulesetEditor;
import org.jboss.tools.windup.ui.internal.rules.RulesetEditorWrapper;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
	public void testCreateJavaRuleFromSelectedAnnotation() throws Exception {
		RulesetEditorWrapper editor = openRulesetEditor();
		assertTrue(editor != null);
		
		Document document = editor.getDocument();
		assertEmptyRuleset(document);
		
		CompilationUnit cu = getTestCompilationUnit();
		assertTrue(cu != null);
		
		List<ASTNode> nodes = Lists.newArrayList();
		
		ASTNode ast = SharedASTProvider.getAST(cu, SharedASTProvider.WAIT_YES, null);
		ast.accept(new ASTVisitor() {
			public boolean visit(org.eclipse.jdt.core.dom.SingleMemberAnnotation node) {
				nodes.add(node);
				return false;
			}
		});
		assertTrue(!nodes.isEmpty());
		
		ruleCreationService.createRuleFromJavaEditorSelection(document, new ASTNode[] {nodes.get(0)});
		assertTrue(document.getElementsByTagName(RulesetConstants.RULE_NAME).getLength() == 1);
		assertTrue(document.getElementsByTagName(RulesetConstants.HINT_NAME).getLength() == 1);
	}
	
	private void assertEmptyRuleset(Document document) {
		assertTrue(document != null);
		
		NodeList rulesets = document.getElementsByTagName(RulesetConstants.RULESET_NAME);
		assertTrue(rulesets.getLength() == 1);
		
		NodeList rulesElements = document.getElementsByTagName(RulesetConstants.RULES_NAME);
		Element rules = (Element)rulesElements.item(0);
		
		NodeList children = rules.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			rules.removeChild(children.item(i));
		}
		
		NodeList ruleElements = document.getElementsByTagName(RulesetConstants.RULE_NAME);
		assertTrue(ruleElements.getLength() == 0);
	}
	
	@Test
	public void testCreateJavaRuleFromSelectedInterface() throws Exception {
		RulesetEditorWrapper editor = openRulesetEditor();
		assertTrue(editor != null);
		
		Document document = editor.getDocument();
		assertEmptyRuleset(document);
		
		CompilationUnit cu = getTestCompilationUnit();
		assertTrue(cu != null);
		
		List<ASTNode> nodes = Lists.newArrayList();
		
		ASTNode ast = SharedASTProvider.getAST(cu, SharedASTProvider.WAIT_YES, null);
		ast.accept(new ASTVisitor() {
			public boolean visit(SimpleType node) {
				if (node.getParent() != null && node.getParent() instanceof TypeDeclaration && 
						((TypeDeclaration)node.getParent()).superInterfaceTypes().contains(node)) {
					nodes.add(node);
				}
				return false;
			}
		});
		assertTrue(!nodes.isEmpty());
		
		ruleCreationService.createRuleFromJavaEditorSelection(document, new ASTNode[] {nodes.get(0)});
		assertTrue(document.getElementsByTagName(RulesetConstants.RULE_NAME).getLength() == 1);
		assertTrue(document.getElementsByTagName(RulesetConstants.HINT_NAME).getLength() == 1);
	}
	
	private RulesetEditorWrapper openRulesetEditor() throws PartInitException {
		IFile demo = projectProvider.getProject().getFile("custom.rules.rhamt.xml");
		assertTrue(demo.exists());
		return (RulesetEditorWrapper)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(new FileEditorInput(demo), RulesetEditor.ID);
	}
	
	private CompilationUnit getTestCompilationUnit() throws PartInitException {
		IFile javaFile = projectProvider.getProject().getFile("src/org/windup/examples/migration/SampleService.java");
		assertTrue(javaFile.exists());
		return (CompilationUnit)JavaCore.create(javaFile);
	}
}
