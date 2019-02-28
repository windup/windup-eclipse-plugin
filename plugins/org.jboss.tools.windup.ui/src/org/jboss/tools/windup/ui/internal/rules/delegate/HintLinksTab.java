/*******************************************************************************
 * Copyright (c) 2019 Red Hat, Inc.
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

import com.google.common.collect.Lists;

@SuppressWarnings({"restriction"})
public class HintLinksTab extends ElementAttributesContainer {
	
	private ListContainer listContainer;
	private Composite parentControl;
	private ScrolledComposite scroll;
		
	@PostConstruct
	public void createControls(Composite parent) {
		Pair<Section, Composite> result = ElementDetailsSection.createScrolledSection(toolkit, parent, RuleMessages.link_title, RuleMessages.link_description,
				ExpandableComposite.TITLE_BAR | Section.NO_TITLE_FOCUS_BOX | Section.TWISTIE, ElementDetailsSection.DEFAULT_SCROLL_SECTION_MAX_HEGHT);
		Section section = result.getFirst();
		Composite client = result.getSecond();
		this.scroll = (ScrolledComposite)section.getClient();
		this.parentControl = client;
		this.listContainer =  new ListContainer(toolkit, contentHelper, modelQuery, model, uiDelegateFactory, context);
		listContainer.createControls(client, collectLinks());
		ConfigurationBlock.addToolbarListener(client);
		createSectionToolbar(section);
	}
	
	public Section getSection() {
		return (Section)scroll.getParent();
	}
	
	private void createSectionToolbar(Section section) {
		ToolBar toolbar = new ToolBar(section, SWT.FLAT|SWT.HORIZONTAL);
		ToolItem addItem = new ToolItem(toolbar, SWT.PUSH);
		addItem.setImage(WindupUIPlugin.getDefault().getImageRegistry().get(WindupUIPlugin.IMG_ADD));
		addItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				CMElementDeclaration linkCmNode = getLinkCmNode();
				AddNodeAction action = (AddNodeAction)ElementUiDelegate.createAddElementAction(
						model, element, linkCmNode, element.getChildNodes().getLength(), null, null);
				action.run();
			}
		});
		section.setTextClient(toolbar);
	}
	
	public void initExpansion() {
		if (listContainer.getItemCount() > 0) {
			((Section)scroll.getParent()).setExpanded(true);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private CMElementDeclaration getLinkCmNode() {
		List candidates = modelQuery.getAvailableContent(element, elementDeclaration, 
				ModelQuery.VALIDITY_STRICT);
		Optional<CMElementDeclaration> found = candidates.stream().filter(candidate -> {
			if (candidate instanceof CMElementDeclaration) {
				return RulesetConstants.LINK_NAME.equals(((CMElementDeclaration)candidate).getElementName());
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
		loadLinks();
		scroll.setMinHeight(listContainer.computeHeight());
		StringBuffer buff = new StringBuffer();
		buff.append(RuleMessages.link_title);
		buff.append(" (" + listContainer.getItemCount() + ")");
		((Section)scroll.getParent()).setText(buff.toString());
	}
	
	private void loadLinks() {
		listContainer.createControls(parentControl, collectLinks());
		listContainer.bind();
	}
	
	private List<Element> collectLinks() {
		List<Element> links = Lists.newArrayList();
		NodeList list = element.getElementsByTagName(RulesetConstants.LINK_NAME);
		for (int i = 0; i < list.getLength(); i++) {
			links.add((Element)list.item(i));
		}
		return links;
	}
}
