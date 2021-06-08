/*******************************************************************************
 * Copyright (c) 2021 Red Hat, Inc.
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
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.ui.internal.tabletree.TreeContentHelper;
import org.eclipse.xtext.util.Pair;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.RuleMessages;
import org.jboss.tools.windup.ui.internal.editor.AddNodeAction;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.RulesetConstants;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.common.base.Objects;

@SuppressWarnings({"restriction"})
public class JavaClassAnnotationLiteralContainer {
	
	private Element element;
	private IStructuredModel model;
	private ModelQuery modelQuery;
	private CMElementDeclaration elementDeclaration;

	private FormToolkit toolkit;
	private Composite parentControl;
	private ScrolledComposite scroll;
	private ListContainer annotationLiteralContainer;
	private RulesetElementUiDelegateFactory uiDelegateFactory;
	private IEclipseContext context;
	private TreeContentHelper contentHelper;
	
	public JavaClassAnnotationLiteralContainer(Element element, IStructuredModel model, ModelQuery modelQuery, CMElementDeclaration elementDeclaration, 
			FormToolkit toolkit, RulesetElementUiDelegateFactory uiDelegateFactory, IEclipseContext context, TreeContentHelper contentHelper) {
		this.element = element;
		this.model = model;
		this.modelQuery = modelQuery;
		this.elementDeclaration = elementDeclaration;
		this.toolkit = toolkit;
		this.uiDelegateFactory = uiDelegateFactory;
		this.context = context;
		this.contentHelper = contentHelper;
	}
	
	private void initExpansionState() {
		if (annotationLiteralContainer.getItemCount() > 0) {
			((Section)scroll.getParent()).setExpanded(true);
		}
	}
	
	public void bind() {
		loadLocations(collectAnnotationLiteralElements());
		scroll.setMinHeight(annotationLiteralContainer.computeHeight());
		StringBuffer buff = new StringBuffer();
		buff.append(RuleMessages.javaclass_annotation_literal_sectionTitle);
		buff.append(" (" + annotationLiteralContainer.getItemCount() + ")");
		((Section)scroll.getParent()).setText(buff.toString());
	}
	
	private void loadLocations(List<Element> literals) {
		annotationLiteralContainer.createControls(parentControl, literals);
		annotationLiteralContainer.bind();
	}
	
	private List<Element> collectAnnotationLiteralElements() {
		Object[] elementChildren = contentHelper.getChildren(element);
		return Arrays.stream(elementChildren).filter(o -> {
			if (o instanceof Element && 
					Objects.equal(RulesetConstants.JAVA_CLASS_ANNOTATION_LITERAL, ((Element)o).getNodeName())) {
				return true;
			}
			return false;
		}).map(o -> (Element)o).collect(Collectors.toList());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private CMElementDeclaration getLiteralCmNode() {
		List candidates = modelQuery.getAvailableContent(element, elementDeclaration, 
				ModelQuery.VALIDITY_STRICT);
		Optional<CMElementDeclaration> found = candidates.stream().filter(candidate -> {
			if (candidate instanceof CMElementDeclaration) {
				return RulesetConstants.JAVA_CLASS_ANNOTATION_LITERAL.equals(((CMElementDeclaration)candidate).getElementName());
			}
			return false;
		}).findFirst();
		if (found.isPresent()) {
			return found.get();
		}
		return null;
	}
	
	public Section createControls(Composite parent) {
		Pair<Section, Composite> result = ElementDetailsSection.createScrolledSection(toolkit, parent,
				RuleMessages.javaclass_annotation_literal_sectionTitle, RuleMessages.javaclass_annotation_literal_description,
				ExpandableComposite.TITLE_BAR | Section.NO_TITLE_FOCUS_BOX | Section.TWISTIE,
				ElementDetailsSection.DEFAULT_SCROLL_SECTION_MAX_HEGHT);
		Section section = result.getFirst();
		Composite client = result.getSecond();
		this.scroll = (ScrolledComposite)section.getClient();
		this.parentControl = client;
		this.annotationLiteralContainer = new ListContainer(toolkit, contentHelper, modelQuery, model, uiDelegateFactory, context);
		annotationLiteralContainer.createControls(client, collectAnnotationLiteralElements());
		initExpansionState();
		createSectionToolbar(section);
		return section;
 	}
	
	private void createSectionToolbar(Section section) {
		ToolBar toolbar = new ToolBar(section, SWT.FLAT|SWT.HORIZONTAL);
		ToolItem addItem = new ToolItem(toolbar, SWT.PUSH);
		addItem.setImage(WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_ADD));
		addItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				createAnnotationLiteral(element);
			}
		});
		section.setTextClient(toolbar);
	}
	
	private Node createAnnotationLiteral(Element parent) {
		CMElementDeclaration literalCmNode = getLiteralCmNode();
		AddNodeAction action = (AddNodeAction)ElementUiDelegate.createAddElementAction(
				model, parent, literalCmNode, parent.getChildNodes().getLength(), null, null);
		action.run();
		if (!action.getResult().isEmpty()) {
			return action.getResult().get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public void createAnnotationLiteralWithValue(String name, String value, Element parent) {
		Node literalNode = createAnnotationLiteral(parent);
		CMElementDeclaration literalCmNode = getLiteralCmNode();
		List<CMAttributeDeclaration> availableAttributeList = modelQuery.getAvailableContent((Element)literalNode, literalCmNode, ModelQuery.INCLUDE_ATTRIBUTES);
		if (name != null) {
			CMAttributeDeclaration nameDeclaration = availableAttributeList.get(0);
			AddNodeAction newNameAction = new AddNodeAction(model, nameDeclaration, literalNode, literalNode.getChildNodes().getLength());
			newNameAction.runWithoutTransaction();
			if (!newNameAction.getResult().isEmpty()) {
				Node nameNode = (Node)newNameAction.getResult().get(0);
				contentHelper.setNodeValue(nameNode, name);
			}
		}
		CMAttributeDeclaration patterDeclaration = availableAttributeList.get(1);
		AddNodeAction newPatternAction = new AddNodeAction(model, patterDeclaration, literalNode, literalNode.getChildNodes().getLength());
		newPatternAction.runWithoutTransaction();
		if (!newPatternAction.getResult().isEmpty()) {
			Node patternNode = (Node)newPatternAction.getResult().get(0);
			contentHelper.setNodeValue(patternNode, value);
		}
	}
}