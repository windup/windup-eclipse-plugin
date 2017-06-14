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

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

@Creatable
@SuppressWarnings("unused")
public class RulesetWidgetFactory {
	
	public static final String NODE_RULE      = "rule";      //$NON-NLS-1$
	public static final String NODE_WHEN      = "when";      //$NON-NLS-1$
	public static final String NODE_PERFORM   = "perform";   //$NON-NLS-1$
	public static final String NODE_OTHERWISE = "otherwise"; //$NON-NLS-1$
	public static final String NODE_WHERE     = "where";     //$NON-NLS-1$
	
	public INodeWidget createWidget(IEclipseContext context) {
		INodeWidget widget = null;
		String nodeName = context.get(Node.class).getNodeName();
		switch (nodeName) {
			case NODE_RULE: {
				widget = createControls(RuleWidget.class, context);
				break;
			}
			case NODE_WHEN: {
				widget = createControls(WhenWidget.class, context);
				break;
			}
			case NODE_PERFORM: {
				widget = createControls(PerformWidget.class, context);
				break;
			}
			case NODE_OTHERWISE: {
				widget = createControls(OtherwiseWidget.class, context);
				break;
			}
			case NODE_WHERE: { 
				widget = createControls(WhereWidget.class, context);
				break;
			}
		}
		return widget;
	}
	
	private <T extends INodeWidget> T createControls(Class<T> clazz, IEclipseContext context) {
		return ContextInjectionFactory.make(clazz, context);
	}
	
	public static interface INodeWidget {
		Control getControl();
		void refresh();
	}
	
	private static abstract class NodeWidget implements INodeWidget {
		
		@Inject protected FormToolkit toolkit;
		@Inject protected Composite parent;
		@Inject protected Node node;
		
		protected Composite left;
		protected Composite right;
		
		protected Control control;
		
		public Control getControl() {
			if (control == null) {
				Composite client = createClient();
				createControls(client);
			}
			return control;
		}
		
		private Composite createClient() {
			Section section = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR | Section.DESCRIPTION);
			section.setText("Element Details"); //$NON-NLS-1$
			section.setDescription("Set the properties of '" + node.getNodeName() + "'. Required fields are denoted by '*'."); //$NON-NLS-1$
			GridLayoutFactory.fillDefaults().applyTo(section);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(section);
			
			Composite client = toolkit.createComposite(section);
			GridLayoutFactory.fillDefaults().applyTo(client);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(client);
			
			toolkit.paintBordersFor(client);
			section.setClient(client);
			
			this.control = section;
			return client;
		}
		
		protected void createPropertyLayout(Composite parent) {
			GridLayoutFactory.fillDefaults().numColumns(2).applyTo(parent);
			this.left = toolkit.createComposite(parent);
			this.right = toolkit.createComposite(parent);
		}
		
		protected String getAttributeValue(String attr) {
			String value = "";
			NamedNodeMap attributes = node.getAttributes();
			Node ruleIdNode = attributes.getNamedItem(attr);  //$NON-NLS-1$
			if (ruleIdNode != null) {
				value = ((Attr) ruleIdNode).getValue();
			}
			return value;
		}
		
		protected abstract void createControls(Composite parent);
	}
	
	private static class RuleWidget extends NodeWidget {
		
		private Text idText;
		
		public RuleWidget() {
		}
		
		@Override
		public void createControls(Composite parent) {
			super.createPropertyLayout(parent);
			toolkit.createLabel(left, "id");
			this.idText = toolkit.createText(right, "");
			refresh();
		}
		
		@Override
		public void refresh() {
			idText.setText(getAttributeValue("id"));
		}
	}
	
	private static class WhenWidget extends NodeWidget {
		
		public WhenWidget() {
		}
		
		@Override
		public void createControls(Composite parent) {
		}
		
		@Override
		public void refresh() {
		}
	}
	
	private static class PerformWidget extends NodeWidget {
		
		public PerformWidget() {
		}
		
		@Override
		public void createControls(Composite parent) {
		}
		
		@Override
		public void refresh() {
		}
	}
	
	private static class OtherwiseWidget extends NodeWidget {
		
		public OtherwiseWidget() {
		}
		
		@Override
		public void createControls(Composite parent) {
		}
		
		@Override
		public void refresh() {
		}
	}
	
	private static class WhereWidget extends NodeWidget {
		
		public WhereWidget() {
		}
		
		@Override
		public void createControls(Composite parent) {
		}
		
		@Override
		public void refresh() {
		}
	}
}
