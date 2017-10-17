/*******************************************************************************
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.rules.delegate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.pde.internal.ui.editor.FormLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wst.xml.core.internal.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.editor.AddNodeAction;
import org.jboss.tools.windup.ui.internal.editor.ElementAttributesContainer;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.NodeRow;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.RulesetConstants;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.base.Objects;

@SuppressWarnings("restriction")
public class RuleDelegate extends ElementUiDelegate {
	
	private static class BooleanAttributeRow extends NodeRow {
		
		private Button button;

		public BooleanAttributeRow(Node parent, CMNode cmNode) {
			super(parent, cmNode);
		}

		@Override
		public void createContents(Composite parent, FormToolkit toolkit, int span) {
			button = toolkit.createButton(parent, Messages.taskRule, SWT.CHECK);
			GridDataFactory.fillDefaults().grab(true, false).span(span, 1).applyTo(button);
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (!blockNotification) {
						setValue(button.getSelection());
					}
				}
			});
		}
		
		protected void setValue(boolean value) {
		}
		
		@Override
		protected void update() {
			boolean selected = false;
			String value = super.getValue();
			if (value != null && !value.isEmpty()) {
				selected = Boolean.valueOf(value);
			}
			if (button.getSelection() != selected) {
				button.setSelection(selected);
			}
		}
		
		@Override
		public void setFocus() {
		}
	}
	
	@Override
	protected void createTabs() {
		addTab(DetailsTab.class);
	}
	
	public static class DetailsTab extends ElementAttributesContainer {
		
		private BooleanAttributeRow typeRow;
		
		private Composite stackComposite;
		
		private Composite taskParent;
		private Composite placeholder;
		
		private TaskRuleComments commentsSection;
		
		@PostConstruct
		@SuppressWarnings("unchecked")
		public void createControls(Composite parent) {
			Composite mainDetailsContainer = toolkit.createComposite(parent);
			
			GridLayout layout = new GridLayout();
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			mainDetailsContainer.setLayout(layout);
		
			GridDataFactory.fillDefaults().grab(true, false).applyTo(mainDetailsContainer);
			
			Composite client = super.createSection(mainDetailsContainer, 2, toolkit, element, ExpandableComposite.TITLE_BAR |Section.NO_TITLE_FOCUS_BOX, 
					null, null);
			
			GridLayout glayout = FormLayoutFactory.createSectionClientGridLayout(false, 2);
			glayout.marginTop = 0;
			glayout.marginBottom = 5;
			client.setLayout(glayout);
			
			CMElementDeclaration ed = modelQuery.getCMElementDeclaration(element);
			if (ed != null) {
				List<CMAttributeDeclaration> availableAttributeList = modelQuery.getAvailableContent(element, ed, ModelQuery.INCLUDE_ATTRIBUTES);
			    for (CMAttributeDeclaration declaration : availableAttributeList) {
				    	if (Objects.equal(declaration.getAttrName(), RulesetConstants.IS_TASK)) {
				    		typeRow = new BooleanAttributeRow(element, declaration) {
				    			@Override
				    			protected void setValue(boolean value) {
			    					try {
			    						model.aboutToChangeModel();
			    						Node node = getNode();
			    						if (node != null) {
			    							contentHelper.setNodeValue(node, String.valueOf(value));
			    						}
			    						else {
			    							AddNodeAction newNodeAction = new AddNodeAction(model, cmNode, parent, parent.getChildNodes().getLength());
			    							newNodeAction.runWithoutTransaction();
			    							if (!newNodeAction.getResult().isEmpty()) {
			    								node = (Node)newNodeAction.getResult().get(0);
			    								contentHelper.setNodeValue(node, String.valueOf(value));
			    							}
			    						}
			    						Element task = findTaskElement();
				    					if (task != null) {
					    					element.removeChild(task);
					    				}
					    				else if (value) {
					    					createTaskElement();
					    				}
			    					}
			    					finally {
			    						model.changedModel();
			    					}
				    			}
				    		};
				    		rows.add(typeRow);
				    		typeRow.createContents(client, toolkit, 2);
				    	}
				    	else {
				    		rows.add(ElementAttributesContainer.createTextAttributeRow(element, toolkit, declaration, client, 2));
				    	}
			    }
			}
			((Section)client.getParent()).setExpanded(true);
			
			createStack(parent);
		}
		
		private boolean isTaskType() {
			String value = typeRow.getValue();
			if (value != null && !value.isEmpty()) {
				return Boolean.valueOf(value);
			}
			return false;
		}
		
		private void createStack(Composite parent) {
			this.stackComposite = toolkit.createComposite(parent);
			stackComposite.setLayout(new StackLayout());
			GridDataFactory.fillDefaults().grab(true, true).applyTo(stackComposite);
			
			placeholder = toolkit.createComposite(stackComposite);
			GridLayoutFactory.fillDefaults().applyTo(placeholder);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(placeholder);
		}
		
		private void createTaskDetails() {
			taskParent = toolkit.createComposite(stackComposite);
			GridLayoutFactory.fillDefaults().applyTo(taskParent);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(taskParent);
			commentsSection = createCommentsArea(taskParent);
		}
		
		private TaskRuleComments createCommentsArea(Composite parent) {
			Element task = findTaskElement();
			CMElementDeclaration dec = modelQuery.getCMElementDeclaration(task);
			IEclipseContext commentContext = context.createChild();
			commentContext.set(Composite.class, parent);
			commentContext.set(Element.class, task);
			commentContext.set(CMElementDeclaration.class, dec);
			return ContextInjectionFactory.make(TaskRuleComments.class, commentContext);
		}
		
		private Element findTaskElement() {
			NodeList list = element.getElementsByTagName(RulesetConstants.TASK);
			if (list.getLength() > 0) {
				return (Element)list.item(0);
			}
			return null;
		}
		
		private void createTaskElement() {
			Element task = element.getOwnerDocument().createElement(RulesetConstants.TASK);
			element.appendChild(task);
		}
		
		private void updateStack() {
			Composite top = placeholder;
			Element taskElement = findTaskElement();
			if (isTaskType() && taskElement != null) {
				if (commentsSection != null && !commentsSection.isContainerFor(taskElement)) {
					taskParent.dispose();
					commentsSection = null;
				}
				if (commentsSection == null) {
					createTaskDetails();
				}
				commentsSection.update();
				top = taskParent;
			}
			else if (commentsSection != null){
				taskParent.dispose();
				commentsSection = null;
			}
			((StackLayout)stackComposite.getLayout()).topControl = top;
			stackComposite.layout(true, true);	
		}
		
		@Override
		protected void bind() {
			super.bind();
			updateStack();
		}
	}
	
	@Override
	public Object[] getChildren() {
		Object[] children = super.getChildren();
		List<Object> filtered = Arrays.stream(children).filter(child -> {
			if (Objects.equal(((Node)child).getNodeName(), RulesetConstants.TASK)) {
				return false;
			}
			return true;
		}).collect(Collectors.toList());
		return filtered.toArray(new Object[filtered.size()]);
	}
}