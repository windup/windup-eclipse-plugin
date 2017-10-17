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
package org.jboss.tools.windup.ui.internal.rules.delegate;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.xtext.util.Pair;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.RuleMessages;
import org.jboss.tools.windup.ui.internal.editor.AddNodeAction;
import org.jboss.tools.windup.ui.internal.editor.ElementAttributesContainer;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.RulesetConstants;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

@SuppressWarnings({"restriction"})
public class TaskRuleComments extends ElementAttributesContainer {
	
	private ListContainer listContainer;
	private Composite parentControl;
	private ScrolledComposite scroll;

	@PostConstruct
	private void createControls(Composite parent) {
		Pair<Section, Composite> result = ElementDetailsSection.createScrolledSection(toolkit, parent, RuleMessages.commentTitle, "",
				ExpandableComposite.TITLE_BAR | Section.NO_TITLE_FOCUS_BOX, ElementDetailsSection.DEFAULT_SCROLL_SECTION_MAX_HEGHT);
		Section section = result.getFirst();
		Composite client = result.getSecond();
		this.scroll = (ScrolledComposite)section.getClient();
		this.parentControl = client;
		this.listContainer =  new ListContainer(toolkit, contentHelper, modelQuery, model, uiDelegateFactory, context);
		listContainer.createControls(client, collectComments());
		createSectionToolbar(section);
	}
	
	private void createSectionToolbar(Section section) {
		ToolBar toolbar = new ToolBar(section, SWT.FLAT|SWT.HORIZONTAL);
		ToolItem addItem = new ToolItem(toolbar, SWT.PUSH);
		addItem.setImage(WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_ADD));
		addItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				CMElementDeclaration linkCmNode = getCommentCmNode();
				AddNodeAction action = (AddNodeAction)ElementUiDelegate.createAddElementAction(
						model, element, linkCmNode, element.getChildNodes().getLength(), null, null);
				action.run();
			}
		});
		section.setTextClient(toolbar);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private CMElementDeclaration getCommentCmNode() {
		List candidates = modelQuery.getAvailableContent(element, elementDeclaration, 
				ModelQuery.VALIDITY_STRICT);
		Optional<CMElementDeclaration> found = candidates.stream().filter(candidate -> {
			if (candidate instanceof CMElementDeclaration) {
				return RulesetConstants.COMMENT.equals(((CMElementDeclaration)candidate).getElementName());
			}
			return false;
		}).findFirst();
		if (found.isPresent()) {
			return found.get();
		}
		return null;
	}
	
	@Override
	protected void bind() {
		super.bind();
		loadComments();
		scroll.setMinHeight(listContainer.computeHeight());
		StringBuffer buff = new StringBuffer();
		buff.append(RuleMessages.commentTitle);
		buff.append(" (" + listContainer.getItemCount() + ")");
		((Section)scroll.getParent()).setText(buff.toString());
	}
	
	private void loadComments() {
		listContainer.createControls(parentControl, collectComments());
		listContainer.bind();
	}
	
	private List<Element> collectComments() {
		List<Element> links = Lists.newArrayList();
		if (element != null) {
			NodeList list = element.getElementsByTagName(RulesetConstants.COMMENT);
			for (int i = 0; i < list.getLength(); i++) {
				links.add((Element)list.item(i));
			}
		}
		return links;
	}
	
	public boolean isContainerFor(Element element) {
		return Objects.equal(this.element, element);
	}
}
