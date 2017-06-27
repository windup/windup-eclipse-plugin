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
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.IElementUiDelegate;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.common.collect.Maps;

@Creatable
public class RulesetElementUiDelegateRegistry {
	
	private Map<Node, IElementUiDelegate> elementUiDelegates = Maps.newHashMap();
	
	@Inject private RulesetElementUiDelegateFactory factory;
	
	public IElementUiDelegate getOrCreateUiDelegate(Element element, Composite container, IEclipseContext context) {
		IElementUiDelegate delegate = elementUiDelegates.get(element);
		if (delegate == null) {
			IEclipseContext child = context.createChild();
			child.set(Element.class, element);
			child.set(Composite.class, container);
			delegate = factory.createElementUiDelegate(element, child);
			if (delegate != null) {
				elementUiDelegates.put(element, delegate);
				return delegate;
			}
		}
		else {
			delegate.update();
			return delegate;
		}
		return null;
	}
	
	public boolean update(Element element) {
		boolean updated = false;
		IElementUiDelegate widget = elementUiDelegates.get(element);
		if (widget != null) {
			widget.update();
			updated = true;
		}
		return updated;
	}
	
	public IElementUiDelegate getUiDelegate(Element element) {
		return elementUiDelegates.get(element);
	}
}
