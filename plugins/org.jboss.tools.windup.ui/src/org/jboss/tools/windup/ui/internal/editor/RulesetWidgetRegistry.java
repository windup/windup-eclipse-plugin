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
import org.jboss.tools.windup.ui.internal.editor.RulesetWidgetFactory.INodeWidget;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.common.collect.Maps;

@Creatable
public class RulesetWidgetRegistry {
	
	private Map<Node, INodeWidget> elementWidgets = Maps.newHashMap();
	
	@Inject private RulesetWidgetFactory factory;
	
	public INodeWidget getOrCreateWidget(Element element, Composite container, IEclipseContext context) {
		INodeWidget widget = elementWidgets.get(element);
		if (widget == null) {
			IEclipseContext child = context.createChild();
			child.set(Element.class, element);
			child.set(Composite.class, container);
			widget = factory.createWidget(element, child);
			if (widget != null) {
				elementWidgets.put(element, widget);
				return widget;
			}
		}
		else {
			widget.update();
			return widget;
		}
		return null;
	}
	
	public boolean update(Element element) {
		boolean updated = false;
		INodeWidget widget = elementWidgets.get(element);
		if (widget != null) {
			widget.update();
			updated = true;
		}
		return updated;
	}
	
	public INodeWidget getWidget(Element element) {
		return elementWidgets.get(element);
	}
}
