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

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.pde.internal.ui.editor.FormLayoutFactory;
import org.eclipse.pde.internal.ui.editor.text.IControlHoverContentProvider;
import org.eclipse.pde.internal.ui.editor.text.PDETextHover;
import org.eclipse.pde.internal.ui.parts.ComboPart;
import org.eclipse.pde.internal.ui.util.PDEJavaHelperUI;
import org.eclipse.pde.internal.ui.util.TextUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQueryAction;
import org.eclipse.wst.xml.core.internal.contentmodel.util.CMDescriptionBuilder;
import org.eclipse.wst.xml.core.internal.contentmodel.util.DOMNamespaceHelper;
import org.eclipse.wst.xml.core.internal.modelquery.ModelQueryUtil;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.ui.internal.tabletree.TreeContentHelper;
import org.eclipse.wst.xml.ui.internal.tabletree.XMLTableTreePropertyDescriptorFactory;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.rules.ElementUiDelegate;
import org.jboss.windup.ast.java.data.TypeReferenceLocation;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

@Creatable
@SuppressWarnings({"unused", "restriction"})
public class RulesetElementUiDelegateFactory {
		
	public static interface RulesetConstants {
		static final String ID = "id"; //$NON-NLS-1$
		static final String RULESET_NAME = "ruleset"; //$NON-NLS-1$
		static final String RULES_NAME = "rules"; //$NON-NLS-1$
		
		static final String RULE_NAME = "rule"; //$NON-NLS-1$
		static final String WHEN_NAME = "when"; //$NON-NLS-1$	
		static final String PERFORM_NAME = "perform"; //$NON-NLS-1$
		static final String WHERE_NAME = "where"; //$NON-NLS-1$
		static final String OTHERWISE_NAME = "otherwise"; //$NON-NLS-1$
		
		static final String JAVACLASS_NAME = "javaclass"; //$NON-NLS-1$
		
		// hint
		static final String HINT_NAME = "hint"; //$NON-NLS-1$
		static final String TITLE = "title"; //$NON-NLS-1$
		static final String EFFORT = "effort"; //$NON-NLS-1$
		static final String CATEGORY_ID = "category-id"; //$NON-NLS-1$
		static final String MESSAGE = "message"; //$NON-NLS-1$
		// javaclass
		static final String JAVA_CLASS_REFERENCES = "references"; //$NON-NLS-1$
		static final String JAVA_CLASS_LOCATION = "location"; //$NON-NLS-1$
		
		static final String LINK_NAME = "link"; //$NON-NLS-1$
		static final String LINK_HREF = "href"; //$NON-NLS-1$
		static final String TAG_NAME = "tag"; //$NON-NLS-1$
	}

	enum HINT_EFFORT {
		
		INFORMATION(0, "Information", "An informational warning with very low or no priority for migration."),
		TRIVIAL(1, "Trivial", "The migration is a trivial change or a simple library swap with no or minimal API changes."),
		COMPLEX(3, "Complex", "The changes required for the migration task are complex, but have a documented solution."),
		REDESIGN(5, "Redesign", "The migration task requires a redesign or a complete library change, with significant API changes."),
		REARCHITECTURE(7, "Rearchitecture", "The migration requires a complete rearchitecture of the component or subsystem."),
		UNKNOWN(13, "Unknown", "The migration solution is not known and may need a complete rewrite.");
		
		private int effort;
		private String label;
		private String description;
		
		HINT_EFFORT(int effort, String label, String description) {
			this.effort = effort;
			this.label = label;
			this.description = description;
		}
		
		public int getEffort() {
			return effort;
		}
		
		public String getLabel() {
			return label;
		}
		
		public String getDescription() {
			return description;
		}
	}
	
	enum JAVA_CLASS_REFERENCE_LOCATION {
		
		ANNOTATION(TypeReferenceLocation.ANNOTATION.toString(), "A Java class references the annotation."),
		CATCH_EXCEPTION_STATEMENT(TypeReferenceLocation.CATCH_EXCEPTION_STATEMENT.toString(), "A Java class method catches the specified type."),
		CONSTRUCTOR_CALL(TypeReferenceLocation.CONSTRUCTOR_CALL.toString(), "A Java class constructs the specified type."),
		ENUM_CONSTANT(TypeReferenceLocation.ENUM_CONSTANT.toString(), "A Java class declares the enumeration."),
		FIELD_DECLARATION(TypeReferenceLocation.FIELD_DECLARATION.toString(), "A Java class declares a field of the specified type."),
		IMPLEMENTS_TYPE(TypeReferenceLocation.IMPLEMENTS_TYPE.toString(), "A Java class implements the specified type; works transitively."),
		IMPORT(TypeReferenceLocation.IMPORT.toString(), "A Java class imports the type."), 
		INHERITANCE(TypeReferenceLocation.INHERITANCE.toString(), "A Java class inherits the specified type; works transitively."),
		INSTANCE_OF(TypeReferenceLocation.INSTANCE_OF.toString(), "A Java class of the specified type is used in an instanceof statement."),
		METHOD(TypeReferenceLocation.METHOD.toString(), "A Java class declares the referenced method."),
		METHOD_CALL(TypeReferenceLocation.METHOD_CALL.toString(), "A Java class calls the specified method; works transitively for interfaces."),
		METHOD_PARAMETER(TypeReferenceLocation.METHOD_PARAMETER.toString(), "A Java class declares the referenced method parameter."),
		RETURN_TYPE(TypeReferenceLocation.RETURN_TYPE.toString(), "A Java class returns the specified type."),
		TAGLIB_IMPORT(TypeReferenceLocation.TAGLIB_IMPORT.toString(), "This is only relevant for JSP sources and represents the import of a taglib into the JSP source file."),
		THROW_STATEMENT(TypeReferenceLocation.THROW_STATEMENT.toString(), "A method in the Java class throws the an instance of the specified type."),
		THROWS_METHOD_DECLARATION(TypeReferenceLocation.THROWS_METHOD_DECLARATION.toString(), "A Java class declares that it may throw the specified type."),
		TYPE(TypeReferenceLocation.TYPE.toString(), "A Java class declares the type."),
		VARIABLE_DECLARATION(TypeReferenceLocation.VARIABLE_DECLARATION.toString(), "A Java class declares a variable of the specified type."),
		VARIABLE_INITIALIZER(TypeReferenceLocation.VARIABLE_INITIALIZER.toString(), "A variable initalization expression value.");
		
		private String label;
		private String description;
		
		JAVA_CLASS_REFERENCE_LOCATION(String label, String description) {
			this.label = label;
			this.description = description;
		}
		
		public String getLabel() {
			return label;
		}
		
		public String getDescription() {
			return description;
		}
	}
	
	public IElementUiDelegate createElementUiDelegate(Element element, IEclipseContext context) {
		IElementUiDelegate uiDelegate = null;
		String nodeName = element.getNodeName();
		switch (nodeName) {
			case RulesetConstants.JAVACLASS_NAME: { 
				uiDelegate = createControls(JavaClassDelegate.class, context);
				break;
			}
			case RulesetConstants.JAVA_CLASS_LOCATION: {
				uiDelegate = createControls(LocationDelegate.class, context);
				break;
			}
			case RulesetConstants.HINT_NAME: { 
				uiDelegate = createControls(HintDelegate.class, context);
				break;
			}
			case RulesetConstants.LINK_NAME: {
				uiDelegate = createControls(LinkDelegate.class, context);
				break;
			}
			default: {
				uiDelegate = createControls(DefaultDelegate.class, context);
			}
		}
		return uiDelegate;
	}
	
	private <T extends IElementUiDelegate> T createControls(Class<T> clazz, IEclipseContext context) {
		return ContextInjectionFactory.make(clazz, context);
	}
	
	public static interface IElementUiDelegate {
		Control getControl();
		void update();
		void setFocus();
		void fillContextMenu(IMenuManager manager, TreeViewer viewer);
		Object[] getChildren();
	}
	
	public static class DefaultDelegate extends ElementUiDelegate {
		
		@Override
		protected void createTabs() {
			addTab(DetailsTab.class);
		}
		
		public static class DetailsTab extends ElementAttributesContainer {
			
			public DetailsTab() {
			}
			
			@PostConstruct
			private void createControls(Composite parent, CTabItem item) {
				item.setText(Messages.ruleElementDetails);
				Composite client = super.createSection(parent, 2);
				super.createControls(client, 2);
			}
		}
	}
	
	public static class LinkDelegate extends ElementUiDelegate {
		
		@Override
		protected boolean shouldFilterElementInsertAction(ModelQueryAction action) {
			return true;
		}
		
		@Override
		protected void createTabs() {
			addTab(DetailsTab.class);
		}
		
		private class DetailsTab extends ElementAttributesContainer {
			
			@PostConstruct
			@SuppressWarnings("unchecked")
			private void createControls(Composite parent, CTabItem item) {
				item.setText(Messages.ruleElementDetails);
				CMElementDeclaration ed = modelQuery.getCMElementDeclaration(element);
				if (ed != null) {
					List<CMAttributeDeclaration> availableAttributeList = modelQuery.getAvailableContent(element, ed, ModelQuery.INCLUDE_ATTRIBUTES);
				    for (CMAttributeDeclaration declaration : availableAttributeList) {
				    		Node node = findNode(element, ed, declaration);
					    	if (Objects.equal(declaration.getAttrName(), RulesetConstants.LINK_HREF)) {
					    		IProject project = context.get(IFile.class).getProject();
					    		ReferenceNodeRow row = new ReferenceNodeRow(element, node, declaration) {
								@Override
								protected void openReference() {
									if (node != null && !node.getNodeValue().isEmpty()) {
									 	try {
											PlatformUI.getWorkbench().getBrowserSupport().
												createBrowser(WindupUIPlugin.PLUGIN_ID).openURL(new URL(node.getNodeValue()));
										}
										catch (Exception e) {
											WindupUIPlugin.log(e);
										}
									}
								}
					    		}; 
							rows.add(row);
							row.createContents(parent, toolkit, 2);
					    	}
					    	else {
					    		rows.add(ElementAttributesContainer.createTextAttributeRow(element, toolkit, node, declaration, parent, 2));
					    	}
				    }
				}
			}
		}
	}

	public static class JavaClassDelegate extends ElementUiDelegate {
		
		@Override
		protected boolean shouldFilterElementInsertAction(ModelQueryAction action) {
			return true;
		}
		
		@Override
		protected void createTabs() {
			addTab(DetailsTab.class);
		}
		
		public static class DetailsTab extends ElementAttributesContainer {
			
			@PostConstruct
			@SuppressWarnings("unchecked")
			public void createControls(Composite parent, CTabItem item) {
				Composite client = super.createSection(parent, 3);
				CMElementDeclaration ed = modelQuery.getCMElementDeclaration(element);
				if (ed != null) {
					List<CMAttributeDeclaration> availableAttributeList = modelQuery.getAvailableContent(element, ed, ModelQuery.INCLUDE_ATTRIBUTES);
				    for (CMAttributeDeclaration declaration : availableAttributeList) {
				    		Node node = findNode(element, ed, declaration);
					    	if (Objects.equal(declaration.getAttrName(), RulesetConstants.JAVA_CLASS_REFERENCES)) {
					    		IFile file = context.get(IFile.class);
					    		IProject project = null;
					    		if (file != null) {
					    			project = file.getProject();
					    		}
					    		ClassAttributeRow row = new ClassAttributeRow(element, node, declaration, project);
							rows.add(row);
							row.createContents(client, toolkit, 2);
					    	}
					    	else {
					    		rows.add(ElementAttributesContainer.createTextAttributeRow(element, toolkit, node, declaration, client, 3));
					    	}
				    }
				}
			}
		}
	}
	
	public static class LocationDelegate extends ElementUiDelegate {
		
		@Override
		protected void createTabs() {
			addTab(DetailsTab.class);
		}
		
		public static class DetailsTab extends ElementAttributesContainer {
			
			private ChoiceAttributeRow createLocationRow(CMNode cmNode) {
				return new ChoiceAttributeRow(element.getParentNode(), element, cmNode) {
					
					@Override
					protected List<String> getOptions() {
						return Arrays.stream(JAVA_CLASS_REFERENCE_LOCATION.values()).map(e -> computeUiValue(e)).
								collect(Collectors.toList());
					}
					
					private String computeUiValue(JAVA_CLASS_REFERENCE_LOCATION location) {
						return location.getLabel() + " - " + location.getDescription();
					}

					private org.w3c.dom.Text findTextChild(Element element) {
						for (Node child = element.getFirstChild(); child != null; child = child.getNextSibling()) {
							if (child instanceof org.w3c.dom.Text) {
								return (org.w3c.dom.Text)child;
							}
						}
						return null;
					}
					
					@Override
					protected String modelToDisplayValue(String modelValue) {
						if (modelValue == null || modelValue.isEmpty()) {
							return "";
						}
						
						Optional<JAVA_CLASS_REFERENCE_LOCATION> location = Arrays.stream(JAVA_CLASS_REFERENCE_LOCATION.values()).filter(e -> {
							return Objects.equal(e.getLabel(), modelValue);
						}).findFirst();
						
						if (location.isPresent()) {
							return computeUiValue(location.get());
						}
						
						return "";
					}
					
					@Override
					protected String displayToModelValue(String uiValue) {
						if (uiValue.isEmpty()) {
							return "";
						}
						
						Optional<JAVA_CLASS_REFERENCE_LOCATION> location = Arrays.stream(JAVA_CLASS_REFERENCE_LOCATION.values()).filter(e -> {
							return Objects.equal(uiValue, computeUiValue(e));
						}).findFirst();
						
						if (location.isPresent()) {
							return location.get().getLabel();
						}
						
						return "";
					}
				};
			}
			
			@PostConstruct
			public void createControls(Composite parent) {
				Composite client = super.createSection(parent, 2);
				CMElementDeclaration ed = modelQuery.getCMElementDeclaration(element);
				ChoiceAttributeRow row = createLocationRow(ed);
				rows.add(row);
				row.createContents(client, toolkit, 2);
			}
		}
		
		@Override
		protected boolean shouldFilterElementInsertAction(ModelQueryAction action) {
			return true;
		}
	}
	
	
	public static abstract class NodeRow implements IControlHoverContentProvider {
		
		protected Node parent;
		protected Node node;
		protected CMNode cmNode;
		
		protected IStructuredModel model;
		protected ModelQuery modelQuery;
		
		protected boolean blockNotification;
		protected IInformationControl infoControl;
		
		protected XMLTableTreePropertyDescriptorFactory propertyDescriptorFactory = new XMLTableTreePropertyDescriptorFactory();
		protected TreeContentHelper contentHelper = new TreeContentHelper();
		
		public NodeRow(Node parent, Node node, CMNode cmNode) {
			this.parent = parent;
			this.node = node;
			this.cmNode = cmNode;
			this.model = ((IDOMNode) parent).getModel();
			this.modelQuery = ModelQueryUtil.getModelQuery(model);
		}
		
		public abstract void createContents(Composite parent, FormToolkit toolkit, int span);
		protected abstract void update();
		public abstract void setFocus();
		
		public void bind() {
			blockNotification = true;
			update();
			blockNotification = false;
		}
		
		protected void createLabel(Composite parent, FormToolkit toolkit) {
			createTextHover(parent);
			Label label = toolkit.createLabel(parent, getLabel(), SWT.NULL);
			label.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
			PDETextHover.addHoverListenerToControl(infoControl, label, this);
		}
		
		protected String getLabel() {
			if (node == null) {
				return getCmNodeLabel();
			}
			else {
				return getNodeLabel();
			}
		}
		
		protected String getCmNodeLabel() {
			String result = "?" + cmNode + "?"; //$NON-NLS-1$ //$NON-NLS-2$
				// https://bugs.eclipse.org/bugs/show_bug.cgi?id=155800
			if (cmNode.getNodeType() == CMNode.ELEMENT_DECLARATION){
				result = DOMNamespaceHelper.computeName(cmNode, parent, null);
			}
			else{
				result = cmNode.getNodeName();
			}				
			if(result == null) {
				result = (String) cmNode.getProperty("description"); //$NON-NLS-1$
			}
			if (result == null || result.length() == 0) {
				if (cmNode.getNodeType() == CMNode.GROUP) {
					CMDescriptionBuilder descriptionBuilder = new CMDescriptionBuilder();
					result = descriptionBuilder.buildDescription(cmNode);
				}
			}
			return result;
		}
		
		protected String getNodeLabel() {
			boolean required = false;
			String label = "";
			switch (node.getNodeType()) {
				case Node.ATTRIBUTE_NODE: {
					//CMAttributeDeclaration ad = modelQuery.getCMAttributeDeclaration((Attr) node);
					CMAttributeDeclaration ad = (CMAttributeDeclaration)cmNode;
					required = (ad.getUsage() == CMAttributeDeclaration.REQUIRED);
					label = ad.getAttrName();
					break;
				}
				case Node.ELEMENT_NODE: {
					label = node.getNodeName();
					break;
				}
			}
			if (required) {
				return NLS.bind(Messages.ElementAttributeRow_AttrLabelReq, label);
			}
			return NLS.bind(Messages.ElementAttributeRow_AttrLabel, label);
		}
		
		protected String getValue() {
			String result = ""; //$NON-NLS-1$
			if (node != null) {
				result = contentHelper.getNodeValue(node);	
			}
			return result != null ? result : ""; //$NON-NLS-1$
		}
		
		protected void setValue(String value) {
			try {
				model.aboutToChangeModel();
				if (node != null) {
					contentHelper.setNodeValue(node, value);
				}
				else {
					AddNodeAction newNodeAction = new AddNodeAction(model, cmNode, parent, parent.getChildNodes().getLength());
					newNodeAction.runWithoutTransaction();
					if (!newNodeAction.getResult().isEmpty()) {
						this.node = (Node)newNodeAction.getResult().get(0);
					}
				}
			}
			finally {
				model.changedModel();
			}
		}
		
		protected void createTextHover(Control control) {
			infoControl = PDETextHover.getInformationControlCreator().createInformationControl(control.getShell());
			infoControl.setSizeConstraints(300, 600);
		}

		@Override
		public String getHoverContent(Control c) {
			return null;
		}
	}
	
	public static class TextNodeRow extends NodeRow {
		
		protected Text text;

		public TextNodeRow(Node parentNode, Node node, CMNode cmNode) {
			super(parentNode, node, cmNode);
		}

		@Override
		public void createContents(Composite parent, FormToolkit toolkit, int span) {
			createLabel(parent, toolkit);
			text = toolkit.createText(parent, "", SWT.SINGLE); //$NON-NLS-1$
			text.setLayoutData(createGridData(span));
			text.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					if (!blockNotification) {
						TextNodeRow.this.setValue(text.getText());
					}
				}
			});
		}

		protected GridData createGridData(int span) {
			GridData gd = new GridData(span == 2 ? GridData.FILL_HORIZONTAL : GridData.HORIZONTAL_ALIGN_FILL);
			gd.widthHint = 20;
			gd.horizontalSpan = span - 1;
			gd.horizontalIndent = FormLayoutFactory.CONTROL_HORIZONTAL_INDENT;
			return gd;
		}

		@Override
		protected void update() {
			if (!Objects.equal(text.getText(), super.getValue())) {
				text.setText(super.getValue());
			}
		}

		@Override
		public void setFocus() {
			text.setFocus();
		}
	}
	
	public static abstract class ReferenceNodeRow extends TextNodeRow {

		public ReferenceNodeRow(Node parentNode, Node node, CMNode cmNode) {
			super(parentNode, node, cmNode);
		}

		@Override
		protected void createLabel(Composite parent, FormToolkit toolkit) {
			createTextHover(parent);
			Hyperlink link = toolkit.createHyperlink(parent, getLabel(), SWT.NULL);
			link.addHyperlinkListener(new HyperlinkAdapter() {
				@Override
				public void linkActivated(HyperlinkEvent e) {
					openReference();
				}
			});
			PDETextHover.addHoverListenerToControl(infoControl, link, this);
		}

		protected abstract void openReference();
	}
	
	public static abstract class ButtonAttributeRow extends ReferenceNodeRow {

		public ButtonAttributeRow(Node parentNode, Node node, CMNode cmNode) {
			super(parentNode, node, cmNode);
		}

		@Override
		public void createContents(Composite parent, FormToolkit toolkit, int span) {
			super.createContents(parent, toolkit, span);
			Button button = toolkit.createButton(parent, Messages.RulesetEditor_ReferenceAttributeRow_browse, SWT.PUSH);
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					browse();
				}
			});
		}

		@Override
		protected GridData createGridData(int span) {
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.widthHint = 20;
			gd.horizontalIndent = FormLayoutFactory.CONTROL_HORIZONTAL_INDENT;
			return gd;
		}

		protected abstract void browse();
	}
	
	public static class ClassAttributeRow extends ButtonAttributeRow {

		private IProject project;

		public ClassAttributeRow(Node parentNode, Node node, CMNode cmNode, IProject project) {
			super(parentNode, node, cmNode);
			this.project = project;
		}

		protected void openReference() {
			String name = TextUtil.trimNonAlphaChars(text.getText()).replace('$', '.');
			if (!name.isEmpty()) {
				openClass(name);
			}
		}
		
		private void openClass(String name) {
			if (project != null) {
				try {
					if (project.hasNature(JavaCore.NATURE_ID)) {
						IJavaProject javaProject = JavaCore.create(project);
						IJavaElement result = javaProject.findType(name);
						if (result != null) {
							JavaUI.openInEditor(result);
						}
					}
				} catch (Exception e) {
					WindupUIPlugin.log(e);
				}
			}
		}

		@Override
		protected void browse() {
			BusyIndicator.showWhile(text.getDisplay(), new Runnable() {
				@Override
				public void run() {
					doOpenSelectionDialog();
				}
			});
		}

		private void doOpenSelectionDialog() {
			String filter = text.getText();
			if (filter.length() == 0) {
				filter = "**"; //$NON-NLS-1$
			}
			String type = PDEJavaHelperUI.selectType(project, IJavaElementSearchConstants.CONSIDER_CLASSES_AND_INTERFACES, filter, null);
			if (type != null) {
				text.setText(type);
			}
		}
	}
	
	public static class ChoiceAttributeRow extends NodeRow {
		
		protected ComboPart combo;

		public ChoiceAttributeRow(Node parentNode, Node node, CMNode cmNode) {
			super(parentNode, node, cmNode);
		}
		
		protected List<String> getOptions() {
			return Lists.newArrayList();
		}

		@Override
		public void createContents(Composite parent, FormToolkit toolkit, int span) {
			super.createTextHover(parent);
			createLabel(parent, toolkit);
			combo = new ComboPart();
			combo.createControl(parent, toolkit, SWT.READ_ONLY);
			combo.add(""); //$NON-NLS-1$
			for (String option : getOptions()) {
				combo.add(option);
			}
			GridData gd = new GridData(span == 2 ? GridData.FILL_HORIZONTAL : GridData.HORIZONTAL_ALIGN_FILL);
			gd.widthHint = 20;
			gd.horizontalSpan = span - 1;
			gd.horizontalIndent = FormLayoutFactory.CONTROL_HORIZONTAL_INDENT;
			combo.getControl().setLayoutData(gd);
			combo.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (!blockNotification) {
						comboSelectionChanged();
					}
				}
			});
		}
		
		protected String displayToModelValue(String uiValue) {
			return uiValue;
		}
		
		protected String modelToDisplayValue(String modelValue) {
			return modelValue;
		}
		
		protected void comboSelectionChanged() {
			super.setValue(displayToModelValue(combo.getSelection()));
		}
		
		@Override
		protected void update() {
			combo.setText(modelToDisplayValue(super.getValue()));
		}

		@Override
		public void setFocus() {
			combo.getControl().setFocus();
		}
	}
}
