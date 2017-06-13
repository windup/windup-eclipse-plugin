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

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.w3c.dom.Node;

public class RulesetWidgetFactory {
	
	public static final String NODE_RULE      = "rule";      //$NON-NLS-1$
	public static final String NODE_WHEN      = "when";      //$NON-NLS-1$
	public static final String NODE_PERFORM   = "perform";   //$NON-NLS-1$
	public static final String NODE_OTHERWISE = "otherwise"; //$NON-NLS-1$
	public static final String NODE_WHERE     = "where";     //$NON-NLS-1$
	
	public INodeWidget createWidget(Node element, IEclipseContext context) {
		INodeWidget widget = null;
		String nodeName = element.getNodeName();
		switch (nodeName) {
			case NODE_RULE: {
				widget = createControls(RuleWidget.class, context.createChild());
				break;
			}
			case NODE_WHEN: {
				widget = createControls(WhenWidget.class, context.createChild());
				break;
			}
			case NODE_PERFORM: {
				widget = createControls(PerformWidget.class, context.createChild());
				break;
			}
			case NODE_OTHERWISE: {
				widget = createControls(OtherwiseWidget.class, context.createChild());
				break;
			}
			case NODE_WHERE: { 
				widget = createControls(WhereWidget.class, context.createChild());
				break;
			}
		}
		return widget;
	}
	
	private <T extends INodeWidget> T createControls(Class<T> clazz, IEclipseContext child) {
		return ContextInjectionFactory.make(clazz, child);
	}
	
	public static interface INodeWidget {
		Control getControl();
	}
	
	private static abstract class NodeWidget implements INodeWidget {
		
		@Inject protected FormToolkit toolkit;
		@Inject protected Composite parent;
		
		@Inject @Named("element") private String element; 
		
		protected Control control;
		
		public Control getControl() {
			if (control == null) {
				Composite client = createClient();
				control = createControls(client);
			}
			return control;
		}
		
		private Composite createClient() {
			Section section = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR | Section.DESCRIPTION);
			section.setText("Element Details"); //$NON-NLS-1$
			section.setDescription("Set the properties of '" + element + "'. Required fields are denoted by '*'."); //$NON-NLS-1$
			GridLayoutFactory.fillDefaults().applyTo(section);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(section);
			
			Composite client = toolkit.createComposite(section);
			GridLayoutFactory.fillDefaults().applyTo(client);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(client);
			
			toolkit.paintBordersFor(client);
			section.setClient(client);
			return client;
		}
		
		protected abstract Control createControls(Composite parent);
	}
	
	private static class RuleWidget extends NodeWidget {
		@Override
		public Control createControls(Composite parent) {
			toolkit.createLabel(parent, "id");
			return null;
		}
	}
	
	private static class WhenWidget extends NodeWidget {
		@Override
		public Control createControls(Composite parent) {
			return null;
		}
	}
	
	private static class PerformWidget extends NodeWidget {
		@Override
		public Control createControls(Composite parent) {
			return null;
		}
	}
	
	private static class OtherwiseWidget extends NodeWidget {
		@Override
		public Control createControls(Composite parent) {
			return null;
		}
	}
	
	private static class WhereWidget extends NodeWidget {
		@Override
		public Control createControls(Composite parent) {
			return null;
		}
	}
}
