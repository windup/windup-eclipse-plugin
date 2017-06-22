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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.pde.core.IBaseModel;
import org.eclipse.pde.core.plugin.IPluginAttribute;
import org.eclipse.pde.core.plugin.IPluginBase;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.internal.core.ischema.ISchemaAttribute;
import org.eclipse.pde.internal.core.ischema.ISchemaEnumeration;
import org.eclipse.pde.internal.core.ischema.ISchemaRestriction;
import org.eclipse.pde.internal.core.ischema.ISchemaSimpleType;
import org.eclipse.pde.internal.ui.PDEPlugin;
import org.eclipse.pde.internal.ui.PDEUIMessages;
import org.eclipse.pde.internal.ui.editor.FormLayoutFactory;
import org.eclipse.pde.internal.ui.editor.IContextPart;
import org.eclipse.pde.internal.ui.editor.contentassist.TypeFieldAssistDisposer;
import org.eclipse.pde.internal.ui.editor.plugin.JavaAttributeValue;
import org.eclipse.pde.internal.ui.editor.plugin.rows.ExtensionAttributeRow;
import org.eclipse.pde.internal.ui.editor.plugin.rows.ReferenceAttributeRow;
import org.eclipse.pde.internal.ui.editor.text.PDETextHover;
import org.eclipse.pde.internal.ui.parts.ComboPart;
import org.eclipse.pde.internal.ui.util.PDEJavaHelperUI;
import org.eclipse.pde.internal.ui.util.TextUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.xtext.util.Pair;
import org.eclipse.xtext.util.Tuples;
import org.jboss.tools.common.xml.XMLUtilities;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.windup.ast.java.data.TypeReferenceLocation;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Creatable
@SuppressWarnings({"unused", "restriction"})
public class RulesetWidgetFactory {
	
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
		static final String JAVA_CLASS_REFERENCES = "references";
		static final String JAVA_CLASS_LOCATION = "location"; //$NON-NLS-1$
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
	
	public INodeWidget createWidget(Element element, IEclipseContext context) {
		INodeWidget widget = null;
		String nodeName = element.getNodeName();
		switch (nodeName) {
			case RulesetConstants.RULE_NAME: {
				widget = createControls(RuleWidget.class, context);
				break;
			}
			case RulesetConstants.WHEN_NAME: {
				widget = createControls(WhenWidget.class, context);
				break;
			}
			case RulesetConstants.PERFORM_NAME: {
				widget = createControls(PerformWidget.class, context);
				break;
			}
			case RulesetConstants.OTHERWISE_NAME: {
				widget = createControls(OtherwiseWidget.class, context);
				break;
			}
			case RulesetConstants.WHERE_NAME: { 
				widget = createControls(WhereWidget.class, context);
				break;
			}
			case RulesetConstants.JAVACLASS_NAME: { 
				widget = createControls(JavaClassWidget.class, context);
				break;
			}
			case RulesetConstants.HINT_NAME: { 
				widget = createControls(HintWidget.class, context);
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
		void update();
		void setFocus();
		boolean isEditable();
		void fillContextMenu(IMenuManager manager);
	}
	
	private static abstract class NodeWidget implements INodeWidget {
		
		@Inject protected FormToolkit toolkit;
		@Inject protected Composite parent;
		@Inject protected Element element;
		@Inject protected IEclipseContext context;
		
		protected Section section;
		protected Control control;
		
		public Control getControl() {
			if (control == null) {
				createControls(createClient());
			}
			update();
			return control;
		}
		
		private Composite createClient() {
			Composite container = toolkit.createComposite(parent);
			GridLayoutFactory.fillDefaults().margins(0, 10).applyTo(container);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(container);
			
			parent = container;
			
			this.section = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR | Section.DESCRIPTION);
			section.clientVerticalSpacing = FormLayoutFactory.SECTION_HEADER_VERTICAL_SPACING;
			section.setText("Details"); //$NON-NLS-1$
			section.setDescription("Set the properties of '" + element.getNodeName() + "'. Required fields are denoted by '*'."); //$NON-NLS-1$
			
			section.setLayout(FormLayoutFactory.createClearGridLayout(false, 1));
			section.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.VERTICAL_ALIGN_BEGINNING));
			
			Composite client = toolkit.createComposite(section);
			int span = computeColumns();
			GridLayout glayout = FormLayoutFactory.createSectionClientGridLayout(false, span);
			client.setLayout(glayout);
			client.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			
			toolkit.paintBordersFor(client);
			section.setClient(client);
			
			this.control = container;
			return client;
		}
		
		protected int computeColumns() {
			return 2;
		}
		
		@Override
		public void setFocus() {
			section.getClient().setFocus();
			section.getClient().forceFocus();
		}
		
		protected void createLabel(Composite parent, String text) {
			Label label = toolkit.createLabel(parent, text, SWT.NULL);
			label.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
		}
		
		protected abstract void createControls(Composite parent);
		public abstract void update();
		
		@Override
		public boolean isEditable() {
			return true;
		}
		
		@Override
		public void fillContextMenu(IMenuManager manager) {
			
		}
	}
	
	private static class RuleWidget extends NodeWidget {
		
		private TextAttributeRow idRow;
		
		public RuleWidget() {
		}
		
		@Override
		public void createControls(Composite parent) {
			this.idRow = new TextAttributeRow(element, RulesetConstants.ID, true);
			idRow.createContents(parent, toolkit, 2);
		}
		
		@Override
		public void update() {
			idRow.bind();
		}
	}
	
	private static class WhenWidget extends NodeWidget {
		
		public WhenWidget() {
		}
		
		@Override
		public void createControls(Composite parent) {
		}
		
		@Override
		public void update() {
		}
		
		@Override
		public boolean isEditable() {
			return false;
		}
	}
	
	private static class PerformWidget extends NodeWidget {
		
		public PerformWidget() {
		}
		
		@Override
		public void createControls(Composite parent) {
		}
		
		@Override
		public void update() {
		}
		
		@Override
		public boolean isEditable() {
			return false;
		}
	}
	
	private static class OtherwiseWidget extends NodeWidget {
		
		public OtherwiseWidget() {
		}
		
		@Override
		public void createControls(Composite parent) {
		}
		
		@Override
		public void update() {
		}
	}
	
	private static class WhereWidget extends NodeWidget {
		
		public WhereWidget() {
		}
		
		@Override
		public void createControls(Composite parent) {
		}
		
		@Override
		public void update() {
		}
	}
	
	private static class JavaClassWidget extends NodeWidget {
		
		private ClassAttributeRow classRow;
		private ChoiceAttributeRow locationRow;
		
		public JavaClassWidget() {
		}
		
		@Override
		public void createControls(Composite parent) {
			IProject project = context.get(IFile.class).getProject();
			classRow = new ClassAttributeRow(element, RulesetConstants.JAVA_CLASS_REFERENCES, true, project);
			classRow.createContents(parent, toolkit, 2);
			locationRow = new ChoiceAttributeRow(element, true, RulesetConstants.JAVA_CLASS_LOCATION) {
				
				@Override
				protected List<String> getOptions() {
					return Arrays.stream(JAVA_CLASS_REFERENCE_LOCATION.values()).map(e -> computeUiValue(e)).
							collect(Collectors.toList());
				}
				
				private String computeUiValue(JAVA_CLASS_REFERENCE_LOCATION location) {
					return location.getLabel() + " - " + location.getDescription();
				}

				@Override
				protected void comboSelectionChanged() {
					Element child = findChildElement();
					if (child != null) {
						element.removeChild(child);
					}
					if (combo.getSelection().isEmpty()) {
						return;
					}
					child = element.getOwnerDocument().createElement(attribute);
					element.appendChild(child);
					XMLUtilities.setText(child, displayToModelValue(combo.getSelection()));
				}
				
				private Element findChildElement() {
					for (Node child = element.getFirstChild(); child != null; child = child.getNextSibling()) {
						if (child instanceof Element && Objects.equal(attribute, child.getNodeName())) {
							return (Element)child;
						}
					}
					return null;
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
				protected void update() {
					if (findChildElement() != null) {
						org.w3c.dom.Text text = findTextChild(findChildElement());
						if (text != null) {
							String modelValue = findChildElement().getTextContent();
							combo.setText(modelToDisplayValue(modelValue));
						}
					}
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
			locationRow.createContents(parent, toolkit, 3);
		}
		
		@Override
		public void update() {
			classRow.bind();
			locationRow.bind();
		}
		
		@Override
		protected int computeColumns() {
			return 3;
		}
	}
	
	private static class HintWidget extends NodeWidget {
		
		private TextAttributeRow titleRow;
		private ChoiceAttributeRow effortRow;
		private TextAttributeRow categoryIdRow;
		
		public HintWidget() {
		}
		
		@Override
		public void createControls(Composite parent) {
			titleRow = new TextAttributeRow(element, RulesetConstants.TITLE, true);
			titleRow.createContents(parent, toolkit, 3);
			effortRow = new ChoiceAttributeRow(element, false, RulesetConstants.EFFORT) {
				@Override
				protected List<String> getOptions() {
					return Arrays.stream(HINT_EFFORT.values()).map(e -> computeUiValue(e)).
							collect(Collectors.toList());
				}
				@Override
				protected String modelToDisplayValue(String modelValue) {
					if (modelValue == null || modelValue.isEmpty()) {
						return "";
					}
					
					int effort;
					
					try {
						effort = Integer.valueOf(modelValue);
					} catch (Exception e) {
						return "";
					}
					
					Optional<HINT_EFFORT> hintEffort = Arrays.stream(HINT_EFFORT.values()).filter(e -> {
						return Objects.equal(e.getEffort(), effort);
					}).findFirst();
					
					if(hintEffort.isPresent()) {
						return computeUiValue(hintEffort.get());
					}

					return "";
				}
				
				@Override
				protected String displayToModelValue(String uiValue) {
					if (uiValue.isEmpty()) {
						return "";
					}
					
					Optional<HINT_EFFORT> hintEffort = Arrays.stream(HINT_EFFORT.values()).filter(e -> {
						return Objects.equal(uiValue, computeUiValue(e));
					}).findFirst(); 
					
					if (hintEffort.isPresent()) {
						return String.valueOf(hintEffort.get().effort);
					}
					return "";
				}
				
				private String computeUiValue(HINT_EFFORT effort) {
					return effort.getLabel() + " - " + effort.getDescription();
				}
			};
			effortRow.createContents(parent, toolkit, 3);
			categoryIdRow = new TextAttributeRow(element, RulesetConstants.CATEGORY_ID, true);
			categoryIdRow.createContents(parent, toolkit, 2);
		}
		
		@Override
		public void update() {
			titleRow.bind();
			effortRow.bind();
			categoryIdRow.bind();
		}
		
		@Override
		protected int computeColumns() {
			return 3;
		}
	}
	
	private static abstract class AttributeRow {
		
		protected Element element;
		protected String attribute;
		protected boolean isRequired;
		
		protected boolean blockNotification;
		
		public AttributeRow(Element element, String attribute, boolean isRequired) {
			this.element = element;
			this.attribute = attribute;
			this.isRequired = isRequired;
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
			Label label = toolkit.createLabel(parent, getAttributeLabel(), SWT.NULL);
			label.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
		}
		
		protected String getAttributeLabel() {
			String label = attribute;
			if (isRequired) {
				return NLS.bind(Messages.ElementAttributeRow_AttrLabelReq, label);
			}
			return NLS.bind(Messages.ElementAttributeRow_AttrLabel, label);
		}
		
		protected String getValue() {
			return element.getAttribute(attribute);
		}
	}
	
	private static class TextAttributeRow extends AttributeRow {
		
		protected Text text;

		public TextAttributeRow(Element element, String attribute, boolean isRequired) {
			super(element, attribute, isRequired);
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
						element.setAttribute(attribute, text.getText());
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
	
	public static abstract class ReferenceAttributeRow extends TextAttributeRow {

		public ReferenceAttributeRow(Element element, String attribute, boolean isRequired) {
			super(element, attribute, isRequired);
		}

		@Override
		protected void createLabel(Composite parent, FormToolkit toolkit) {
			Hyperlink link = toolkit.createHyperlink(parent, getAttributeLabel(), SWT.NULL);
			link.addHyperlinkListener(new HyperlinkAdapter() {
				@Override
				public void linkActivated(HyperlinkEvent e) {
					openReference();
				}
			});
		}

		protected abstract void openReference();

	}
	
	public static abstract class ButtonAttributeRow extends ReferenceAttributeRow {

		public ButtonAttributeRow(Element element, String attribute, boolean isRequired) {
			super(element, attribute, isRequired);
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

		public ClassAttributeRow(Element element, String attribute, boolean isRequired, IProject project) {
			super(element, attribute, isRequired);
			this.project = project;
		}

		protected void openReference() {
			String name = TextUtil.trimNonAlphaChars(text.getText()).replace('$', '.');
			if (!name.isEmpty()) {
				openClass(name);
			}
		}
		
		private void openClass(String name) {
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
	
	public static class ChoiceAttributeRow extends AttributeRow {
		
		protected ComboPart combo;

		public ChoiceAttributeRow(Element element, String attribute, boolean isRequired) {
			super(element, attribute, isRequired);
		}
		
		public ChoiceAttributeRow(Element parent, boolean isRequired, String element) {
			super(parent, element, isRequired);
		}
		
		protected List<String> getOptions() {
			return Lists.newArrayList();
		}

		@Override
		public void createContents(Composite parent, FormToolkit toolkit, int span) {
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
			element.setAttribute(attribute, displayToModelValue(combo.getSelection()));
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
