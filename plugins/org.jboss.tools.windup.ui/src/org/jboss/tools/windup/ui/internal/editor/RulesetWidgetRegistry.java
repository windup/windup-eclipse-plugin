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

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.jboss.tools.windup.ui.internal.editor.RulesetWidgetFactory.INodeWidget;
import org.w3c.dom.Node;

import com.google.common.collect.Maps;

@Creatable
public class RulesetWidgetRegistry {
	
	private Map<Node, INodeWidget> elementWidgets = Maps.newHashMap();
	
	@Inject private RulesetWidgetFactory factory;
	
	public Control getOrCreate(Node node, Composite container, IEclipseContext context) {
		INodeWidget widget = elementWidgets.get(node);
		if (widget == null) {
			IEclipseContext child = context.createChild();
			child.set(Node.class, node);
			child.set(Composite.class, container);
			widget = factory.createWidget(context);
			if (widget != null) {
				elementWidgets.put(node, widget);
				return widget.getControl();
			}
		}
		else {
			widget.refresh();
			return widget.getControl();
		}
		return null;
	}
	
	public void refresh(Node node) {
		
	}
}
