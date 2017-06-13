/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.editor;

import java.util.Map;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.widgets.Control;
import org.jboss.tools.windup.ui.internal.editor.RulesetWidgetFactory.INodeWidget;
import org.w3c.dom.Node;

import com.google.common.collect.Maps;

public class RulesetWidgetRegistry {
	
	private RulesetWidgetFactory factory;
	private Map<Node, INodeWidget> elementWidgets = Maps.newHashMap();
	
	public RulesetWidgetRegistry() {
		factory = new RulesetWidgetFactory();
	}
	
	public Control getControl(Node element, IEclipseContext context) {
		INodeWidget widget = elementWidgets.get(element);
		if (widget == null) {
			widget = factory.createWidget(element, context);
			if (widget != null) {
				elementWidgets.put(element, widget);
			}
		}
		return widget.getControl();
	}
}
