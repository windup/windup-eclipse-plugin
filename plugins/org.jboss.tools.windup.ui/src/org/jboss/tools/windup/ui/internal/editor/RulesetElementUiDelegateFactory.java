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

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

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
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.jface.fieldassist.SimpleContentProposalProvider;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.internal.text.html.BrowserInformationControl;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.pde.internal.ui.editor.FormLayoutFactory;
import org.eclipse.pde.internal.ui.parts.ComboPart;
import org.eclipse.pde.internal.ui.util.PDEJavaHelperUI;
import org.eclipse.pde.internal.ui.util.TextUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMDocumentation;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNodeList;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.core.internal.contentmodel.util.CMDescriptionBuilder;
import org.eclipse.wst.xml.core.internal.contentmodel.util.DOMNamespaceHelper;
import org.eclipse.wst.xml.core.internal.modelquery.ModelQueryUtil;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.ui.internal.tabletree.TreeContentHelper;
import org.eclipse.wst.xml.ui.internal.tabletree.XMLTableTreePropertyDescriptorFactory;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.editor.DefaultTextViewerContentProposalProvider.ITextViewerContentProviderDelegate;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.ChoiceAttributeRow;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.RulesetConstants;
import org.jboss.tools.windup.ui.internal.issues.IssueDetailsView;
import org.jboss.tools.windup.ui.internal.rules.delegate.ClassificationDelegate;
import org.jboss.tools.windup.ui.internal.rules.delegate.ControlInformationSupport;
import org.jboss.tools.windup.ui.internal.rules.delegate.ElementUiDelegate;
import org.jboss.tools.windup.ui.internal.rules.delegate.HintDelegate;
import org.jboss.tools.windup.ui.internal.rules.delegate.JavaClassAnnotationListDelegate;
import org.jboss.tools.windup.ui.internal.rules.delegate.JavaClassAnnotationLiteralDelegate;
import org.jboss.tools.windup.ui.internal.rules.delegate.JavaClassAnnotationTypeDelegate;
import org.jboss.tools.windup.ui.internal.rules.delegate.JavaClassDelegate;
import org.jboss.tools.windup.ui.internal.rules.delegate.JavaClassLocationDelegate;
import org.jboss.tools.windup.ui.internal.rules.delegate.LinkDelegate;
import org.jboss.tools.windup.ui.internal.rules.delegate.NamespaceDelegate;
import org.jboss.tools.windup.ui.internal.rules.delegate.QuickfixDelegate;
import org.jboss.tools.windup.ui.internal.rules.delegate.RuleDelegate;
import org.jboss.tools.windup.ui.internal.rules.delegate.WhereDelegate;
import org.jboss.tools.windup.ui.internal.rules.delegate.XmlFileUiDelegate;
import org.jboss.tools.windup.ui.internal.rules.xml.XMLRulesetModelUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

@Creatable
@SuppressWarnings({"unused", "restriction"})
public class RulesetElementUiDelegateFactory {
	
	enum CATEGORY {
		MANDATORY, OPTIONAL, POTENTIAL
	}
		
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
		static final String CDATA = "#cdata-section"; //$NON-NLS-1$
		
		// classification
		static final String CLASSIFICATION = "classification"; //$NON-NLS-1$
		static final String DESCRIPTION = "description"; //$NON-NLS-1$
		
		// quickfix
		static final String QUICKFIX = "quickfix"; //$NON-NLS-1$
		static final String QUICKFIX_TYPE = "type"; //$NON-NLS-1$
		static final String SEARCH = "search"; //$NON-NLS-1$
		static final String REPLACEMENT = "replacement"; //$NON-NLS-1$
		static final String NEWLINE = "newline"; //$NON-NLS-1$
		
		static final String WHERE = "where"; //$NON-NLS-1$ 
		static final String PARAM = "param"; //$NON-NLS-1$
		static final String MATCHES = "matches"; //$NON-NLS-1$
		static final String PATTERN = "pattern"; //$NON-NLS-1$
		
		// javaclass
		static final String JAVA_CLASS_REFERENCES = "references"; //$NON-NLS-1$
		static final String JAVA_CLASS_LOCATION = "location"; //$NON-NLS-1$
		static final String JAVA_CLASS_ANNOTATION_LIST = "annotation-list"; //$NON-NLS-1$
		static final String JAVA_CLASS_ANNOTATION_LITERAL = "annotation-literal"; //$NON-NLS-1$
		static final String JAVA_CLASS_ANNOTATION_TYPE = "annotation-type"; //$NON-NLS-1$
		
		static final String LINK_NAME = "link"; //$NON-NLS-1$
		static final String LINK_HREF = "href"; //$NON-NLS-1$
		static final String LINK_TITLE = "title"; //$NON-NLS-1$
		
		static final String TAG_NAME = "tag"; //$NON-NLS-1$
		
		// annotation-literal
		static final String LITERAL_NAME = "name"; //$NON-NLS-1$
		static final String LITERAL_PATTERN = "pattern"; //$NON-NLS-1$
		
		// annotation-type
		static final String TYPE_PATTERN = "pattern"; //$NON-NLS-1$
		
		// annotation-list
		static final String LIST_NAME = "name"; //$NON-NLS-1$
		
		static final String XMLFILE = "xmlfile"; //$NON-NLS-1$
		static final String XPATH = "matches"; //$NON-NLS-1$
		
		static final String URI = "uri"; //$NON-NLS-1$
		static final String NAMESPACE = "namespace"; //$NON-NLS-1$
		
		static final String TASK = "task"; //$NON-NLS-1$ 
		static final String IS_TASK = "isTask"; // $NON-NLS-1$
		static final String COMMENT = "comment"; //$NON_NLS-1$
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
				uiDelegate = createControls(JavaClassLocationDelegate.class, context);
				break;
			}
			case RulesetConstants.JAVA_CLASS_ANNOTATION_LIST: { 
				uiDelegate = createControls(JavaClassAnnotationListDelegate.class, context);
				break;
			}
			case RulesetConstants.JAVA_CLASS_ANNOTATION_LITERAL: { 
				uiDelegate = createControls(JavaClassAnnotationLiteralDelegate.class, context);
				break;
			}
			case RulesetConstants.JAVA_CLASS_ANNOTATION_TYPE: { 
				uiDelegate = createControls(JavaClassAnnotationTypeDelegate.class, context);
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
			case RulesetConstants.CLASSIFICATION: {
				uiDelegate = createControls(ClassificationDelegate.class, context);
				break;
			}
			case RulesetConstants.QUICKFIX: {
				uiDelegate = createControls(QuickfixDelegate.class, context);
				break;
			}
			case RulesetConstants.WHERE: {
				uiDelegate = createControls(WhereDelegate.class, context);
				break;
			}
			case RulesetConstants.XMLFILE: {
				uiDelegate = createControls(XmlFileUiDelegate.class, context);
				break;
			}
			case RulesetConstants.NAMESPACE: {
				uiDelegate = createControls(NamespaceDelegate.class, context);
				break;
			}
			case RulesetConstants.RULE_NAME: {
				uiDelegate = createControls(RuleDelegate.class, context);
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
		void createControls(Composite parent, Element element, CMElementDeclaration ed, List<NodeRow> rows);
		
		List<Element> collectTreeChildren(Element parent);
		void createTreeItemControls(Composite parent, Element element);
		void createChildTreeItemControls(Composite parent, Element element);
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
			private void createControls(Composite parent) {
				Composite client = super.createSection(parent, 2);
				super.createControls(client, 2);
			}
		}
	}
	
	public static abstract class NodeRow {
		
		protected Node parent;
		protected CMNode cmNode;
		
		protected IStructuredModel model;
		protected ModelQuery modelQuery;
		
		protected boolean blockNotification;
		private IInformationControl infoControl;
		
		protected XMLTableTreePropertyDescriptorFactory propertyDescriptorFactory = new XMLTableTreePropertyDescriptorFactory();
		protected TreeContentHelper contentHelper = new TreeContentHelper();
		
		public NodeRow(Node parent, CMNode cmNode) {
			this.parent = parent;
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
		
		protected String getCmNodeLabel() {
			CMAttributeDeclaration ad = (CMAttributeDeclaration)cmNode;
			boolean required = (ad.getUsage() == CMAttributeDeclaration.REQUIRED);
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
			if (required) {
				return NLS.bind(Messages.ElementAttributeRow_AttrLabelReq, result);
			}
			return NLS.bind(Messages.ElementAttributeRow_AttrLabel, result);
		}
		
		protected String getNodeLabel() {
			boolean required = false;
			String label = "";
			Node node = getNode();
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
		
		protected Control createLabel(Composite parent, FormToolkit toolkit) {
			//createTextHover(parent);
			Label label = toolkit.createLabel(parent, getLabel(), SWT.NULL);
			label.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
			
			String documentation = getDocumentation();
			if (documentation != null) {
				if (BrowserInformationControl.isAvailable(parent)) {
					label.setData(ControlInformationSupport.INFORMATION, documentation);
					new ControlInformationSupport(label);
				}
			}
			return label;
		}
		
		public String getDocumentation() {
			try {
				Object documentation = cmNode.getProperty("documentation"); //$NON-NLS-1$
				if (documentation != null && documentation instanceof CMNodeList) {
					CMNodeList nodeList = (CMNodeList)documentation;
					for (int i = 0; i < nodeList.getLength(); i++) {
						Object node = nodeList.item(i);
						if (node instanceof CMDocumentation) {
							String html = ((CMDocumentation)node).getValue();
							Document document = Jsoup.parse(html);
							IssueDetailsView.addPrism(document);
							org.jsoup.nodes.Element body = document.body();
							org.jsoup.nodes.Element style = document.createElement("style"); //$NON-NLS-1$
							style.html("body {background-color: #fff7be}"); //$NON-NLS-1$
							document.head().appendChild(style);
							return document.html();
						}
					}
				}
			} catch (Exception e) {
				WindupUIPlugin.log(e);
			}
			return null;
		}
		
		protected String getLabel() {
			if (getNode() == null) {
				return getCmNodeLabel();
			}
			else {
				return getNodeLabel();
			}
		}
		
		public String getValue() {
			String result = ""; //$NON-NLS-1$
			Node node = getNode();
			if (node != null) {
				result = contentHelper.getNodeValue(node);	
			}
			return result != null ? result : ""; //$NON-NLS-1$
		}
		
		protected Node getNode() {
			CMElementDeclaration parentEd = modelQuery.getCMElementDeclaration((Element)parent);
			return ElementUiDelegate.findNode((Element)parent, parentEd, cmNode);
		}
		
		protected void setValue(String value) {
			try {
				model.aboutToChangeModel();
				Node node = getNode();
				if (node != null) {
					contentHelper.setNodeValue(node, value);
				}
				else {
					AddNodeAction newNodeAction = new AddNodeAction(model, cmNode, parent, parent.getChildNodes().getLength());
					newNodeAction.runWithoutTransaction();
					if (!newNodeAction.getResult().isEmpty()) {
						node = (Node)newNodeAction.getResult().get(0);
						contentHelper.setNodeValue(node, value);
					}
				}
			}
			finally {
				model.changedModel();
			}
		}
		
		protected void createTextHover(Control control) {
			//infoControl = PDETextHover.getInformationControlCreator().createInformationControl(control.getShell());
			//infoControl.setSizeConstraints(300, 600);
		}
	}
	
	public static class TextNodeRow extends NodeRow implements ITextViewerContentProviderDelegate {
		
		private static String KEY_PRESS = "Ctrl+Space";
		
		protected Text text;
		protected Control label;

		public TextNodeRow(Node parentNode, CMNode cmNode) {
			super(parentNode, cmNode);
		}
		
		@Override
		public void createContents(Composite parent, FormToolkit toolkit, int span) {
			this.label = createLabel(parent, toolkit);
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
			
			new TextViewer(text, this);
		}
		
		@Override
		public char getActivationChar() {
			return '{'; //$NON-NLS-1$
		}
		
		@Override
		public List<String> getProposals(String prefix) {
			return collectProposals(prefix);
		}
		
		public List<String> collectProposals(String prefix) {
			Element rule = findRuleParent();
			List<Element> whereChildren = findWhereChildren(rule);
			List<String> names = Lists.newArrayList();
			for (Element child : whereChildren) {
				String name = XMLRulesetModelUtil.getWhereParam(child);
				if (name != null && (name.contains(prefix) || prefix.isEmpty())) {
					names.add(name);
				}
			}
			return names;
		}
		
		private Element findRuleParent() {
			Element parent = (Element)super.parent;
			while (parent != null) {
				if (isRuleNode(parent)) {
					return parent;
				}
				parent = (Element)parent.getParentNode();
			}
			return null;
		}
		
		private boolean isRuleNode(Element element) {
			return RulesetConstants.RULE_NAME.equals(element.getNodeName());
		}
		
		private List<Element> findWhereChildren(Element rule) {
			List<Element> whereChildren = Lists.newArrayList();
			NodeList children = rule.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				if (children.item(i) instanceof Element && isWhereNode((Element)children.item(i))) {
					whereChildren.add((Element)children.item(i));
				}
			}
			return whereChildren;
		}
		
		private boolean isWhereNode(Element element) {
			return RulesetConstants.WHERE.equals(element.getNodeName());
		}

		protected GridData createGridData(int span) {
			GridData gd = new GridData(span == 2 ? GridData.FILL_HORIZONTAL : GridData.HORIZONTAL_ALIGN_FILL);
			gd.widthHint = 20;
			gd.horizontalSpan = span - 1;
			gd.horizontalIndent = FormLayoutFactory.CONTROL_HORIZONTAL_INDENT;
			return gd;
		}
		
		public Control getLabelControl() {
			return label;
		}
		
		public Control getTextControl() {
			return text;
		}
		
		public void setText(String textValue) {
			text.setText(textValue);
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
	
	private static class ProposalProvider extends SimpleContentProposalProvider {
		public ProposalProvider() {
			super(null);
		}
		
		@Override
		public IContentProposal[] getProposals(String contents, int position) {
			return super.getProposals(contents, position);
		}
	}
	
	public static abstract class ReferenceNodeRow extends TextNodeRow {

		public ReferenceNodeRow(Node parentNode, CMNode cmNode) {
			super(parentNode, cmNode);
		}

		@Override
		protected Control createLabel(Composite parent, FormToolkit toolkit) {
			createTextHover(parent);
			Hyperlink link = toolkit.createHyperlink(parent, getLabel(), SWT.NULL);
			link.addHyperlinkListener(new HyperlinkAdapter() {
				@Override
				public void linkActivated(HyperlinkEvent e) {
					openReference();
				}
			});
			String documentation = getDocumentation();
			if (documentation != null) {
				if (BrowserInformationControl.isAvailable(parent)) {
					link.setData(ControlInformationSupport.INFORMATION, documentation);
					ControlInformationSupport support = new ControlInformationSupport(link);
				}
			}
			return link;
		}

		protected abstract void openReference();
	}
	
	public static abstract class ButtonAttributeRow extends ReferenceNodeRow {

		public ButtonAttributeRow(Node parentNode, CMNode cmNode) {
			super(parentNode, cmNode);
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
	
	public abstract static class ClassAttributeRow extends ButtonAttributeRow {

		private IProject project;

		public ClassAttributeRow(Node parentNode, CMNode cmNode, IProject project) {
			super(parentNode, cmNode);
			this.project = project;
		}

		protected void openReference() {
			String name = TextUtil.trimNonAlphaChars(text.getText()).replace('$', '.'); //$NON-NLS-1$ //$NON-NLS-2$
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
	
	public abstract static class ChoiceAttributeRow extends NodeRow {
		
		protected ComboPart combo;
		private Control label;
		private boolean readOnly;

		public ChoiceAttributeRow(Node parentNode, CMNode cmNode, boolean readOnly) {
			super(parentNode, cmNode);
			this.readOnly = readOnly;
		}
		
		protected List<String> getOptions() {
			return Lists.newArrayList();
		}
		
		public Control getLabelControl() {
			return label;
		}
		
		public ComboPart getCombo() {
			return combo;
		}

		@Override
		public void createContents(Composite parent, FormToolkit toolkit, int span) {
			super.createTextHover(parent);
			this.label = createLabel(parent, toolkit);
			combo = new ComboPart();
			combo.createControl(parent, toolkit, readOnly ? SWT.READ_ONLY : SWT.NONE);
			combo.add(""); //$NON-NLS-1$
			for (String option : getOptions()) {
				combo.add(option);
			}
			GridData gd = new GridData(span == 2 ? GridData.FILL_HORIZONTAL : GridData.HORIZONTAL_ALIGN_FILL);
			if (!readOnly) {
				gd.widthHint = 20;
				gd.horizontalIndent = 3;
				gd.horizontalSpan = span - 1;
			}
			else {
				gd.widthHint = 20;
				gd.horizontalSpan = span - 1;				
			}
			//gd.horizontalIndent = FormLayoutFactory.CONTROL_HORIZONTAL_INDENT;
			combo.getControl().setLayoutData(gd);
			combo.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (!blockNotification) {
						comboSelectionChanged();
					}
				}
			});
			if (!readOnly) {
				combo.addModifyListener(new ModifyListener() {
					@Override
					public void modifyText(ModifyEvent e) {
						if (!blockNotification) {
							comboSelectionChanged();
						}
					}
				});
			}
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
	
	public static ChoiceAttributeRow createCategoryRow(Element element, CMNode cmNode) {
		return new ChoiceAttributeRow(element, cmNode, false) {
			@Override
			protected List<String> getOptions() {
				return Arrays.stream(CATEGORY.values()).map(c -> c.toString().toLowerCase()).collect(Collectors.toList());
			}
		};
	}
}
