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
package org.jboss.tools.windup.ui;

import jakarta.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MAdvancedFactory;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPlaceholder;
import org.eclipse.e4.ui.model.application.ui.basic.MPartSashContainer;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

/**
 * Represents the Windup perspective.
 */
public class WindupPerspective {

	public static final String ID = "org.jboss.tools.windup.ui.perspective";
	
	private static final String TOP_LEFT = "org.jboss.tools.windup.ui.partstack.topLeft";
	public static final String ISSUE_EXPLORER = "org.jboss.tools.windup.ui.explorer";
	public static final String EXPLORER = "org.eclipse.ui.navigator.ProjectExplorer";
	private static final String NAVIGATOR = "org.eclipse.ui.views.ResourceNavigator";
	
	private static final String BOTTOM_LEFT = "org.jboss.tools.windup.ui.partstack.bottomLeft";
	private static final String CONTENT_OUTLINE = "org.eclipse.ui.views.ContentOutline";
	private static final String MYLYN_TASKS = "org.eclipse.mylyn.tasks.ui.views.tasks";
	
	private static final String BOTTOM_RIGHT = "org.jboss.tools.windup.ui.partstack.bottomRight";
	private static final String PROBLEMS = "org.eclipse.ui.views.ProblemView";
	private static final String CONSOLE = "org.eclipse.ui.console.ConsoleView";
	private static final String PROPERTIES = "org.eclipse.ui.views.PropertySheet";
	
	private static final String EDITOR_STACK = "org.jboss.tools.windup.ui.partsashcontainer.editorArea";
	private static final String EDITOR = "org.eclipse.ui.editorss";
	
	@Inject private MApplication app;
	@Inject private EModelService modelService;
	
	@Execute
	public void setup() {
		MPerspective perspective = (MPerspective)modelService.findSnippet(app, ID);
		initTopLeft(perspective);
		initBottomLeft(perspective);
		initBottomRight(perspective);
		createEditorArea(perspective);
	}
	
	private void initTopLeft(MPerspective perspective) {
		MPartStack stack = (MPartStack)modelService.find(TOP_LEFT, perspective);
		MPlaceholder placeholder = createPlaceholder(stack, ISSUE_EXPLORER);
		stack.setSelectedElement(placeholder);
		createPlaceholder(stack, EXPLORER);
		createPlaceholder(stack, NAVIGATOR);
	}
	
	private void initBottomLeft(MPerspective perspective) {
		MPartStack stack = (MPartStack)modelService.find(BOTTOM_LEFT, perspective);
		MPlaceholder placeholder = createPlaceholder(stack, CONTENT_OUTLINE);
		stack.setSelectedElement(placeholder);
		createPlaceholder(stack, MYLYN_TASKS);
	}
	
	private void initBottomRight(MPerspective perspective) {
		MPartStack stack = (MPartStack)modelService.find(BOTTOM_RIGHT, perspective);
		MPlaceholder placeholder = createPlaceholder(stack, PROBLEMS);
		stack.setSelectedElement(placeholder);
		createPlaceholder(stack, PROPERTIES);
		createPlaceholder(stack, CONSOLE);
	}
	
	private void createEditorArea(MPerspective perspective) {
		MPartSashContainer stack = (MPartSashContainer)modelService.find(EDITOR_STACK, perspective);
		MPlaceholder placeholder = MAdvancedFactory.INSTANCE.createPlaceholder();
		placeholder.setCloseable(false);
		placeholder.setVisible(true);
		placeholder.setToBeRendered(true);
		placeholder.setElementId(EDITOR);
		placeholder.setContainerData("8400");
		stack.getChildren().add(placeholder);
	}
	
	private MPlaceholder createPlaceholder(MPartStack stack, String elementId) {
		MPlaceholder placeholder = MAdvancedFactory.INSTANCE.createPlaceholder();
		placeholder.setCloseable(false);
		placeholder.setVisible(true);
		placeholder.setToBeRendered(true);
		placeholder.setElementId(elementId);		
		stack.getChildren().add(placeholder);
		return placeholder;
	}
}
