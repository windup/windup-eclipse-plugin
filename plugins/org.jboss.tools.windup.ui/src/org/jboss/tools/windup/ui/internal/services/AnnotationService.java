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
package org.jboss.tools.windup.ui.internal.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.ui.internal.tabletree.TreeContentHelper;
import org.jboss.tools.windup.ui.internal.editor.AddNodeAction;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.RulesetConstants;
import org.jboss.tools.windup.ui.internal.rules.delegate.ElementUiDelegate;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.common.base.Objects;

public class AnnotationService {
	
	private IStructuredModel model;
	private ModelQuery modelQuery;
	private TreeContentHelper contentHelper = new TreeContentHelper();
	
	public AnnotationService(IStructuredModel model, ModelQuery modelQuery) {
		this.model = model;
		this.modelQuery = modelQuery;
	}
	
	// List
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private CMElementDeclaration getListCmNode(Element parent) {
		CMElementDeclaration elementDeclaration = modelQuery.getCMElementDeclaration(parent);
		List candidates = modelQuery.getAvailableContent(parent, elementDeclaration, 
				ModelQuery.VALIDITY_STRICT);
		Optional<CMElementDeclaration> found = candidates.stream().filter(candidate -> {
			if (candidate instanceof CMElementDeclaration) {
				return RulesetConstants.JAVA_CLASS_ANNOTATION_LIST.equals(((CMElementDeclaration)candidate).getElementName());
			}
			return false;
		}).findFirst();
		if (found.isPresent()) {
			return found.get();
		}
		return null;
	}
	
	private Node createAnnotationList(Element parent) {
		CMElementDeclaration listCmNode = getListCmNode(parent);
		AddNodeAction action = (AddNodeAction)ElementUiDelegate.createAddElementAction(
				model, parent, listCmNode, parent.getChildNodes().getLength(), null, null);
		action.run();
		if (!action.getResult().isEmpty()) {
			return action.getResult().get(0);
		}
		return null;
	}

	public Node createAnnotationList(String name, Element parent) {
		Node annotationList = createAnnotationList(parent);
		if (name != null) {
			CMElementDeclaration listCmNode = getListCmNode(parent);
			List<CMAttributeDeclaration> availableAttributeList = modelQuery.getAvailableContent((Element)annotationList, listCmNode, ModelQuery.INCLUDE_ATTRIBUTES);
			CMAttributeDeclaration nameDeclaration = availableAttributeList.get(1);
			AddNodeAction newNodeAction = new AddNodeAction(model, nameDeclaration, annotationList, annotationList.getChildNodes().getLength());
			newNodeAction.runWithoutTransaction();
			if (!newNodeAction.getResult().isEmpty() && name != null && !name.isEmpty()) {
				Node nameNode = (Node)newNodeAction.getResult().get(0);
				contentHelper.setNodeValue(nameNode, name);
			}
		}
		return annotationList;
	}
	
	// Type
	
	private List<Element> collectAnnotationTypeElements(Element parent) {
		Object[] elementChildren = contentHelper.getChildren(parent);
		return Arrays.stream(elementChildren).filter(o -> {
			if (o instanceof Element && 
					Objects.equal(RulesetConstants.JAVA_CLASS_ANNOTATION_TYPE, ((Element)o).getNodeName())) {
				return true;
			}
			return false;
		}).map(o -> (Element)o).collect(Collectors.toList());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private CMElementDeclaration getAnnotationTypeCmNode(Element parent) {
		CMElementDeclaration elementDeclaration = modelQuery.getCMElementDeclaration(parent);
		List candidates = modelQuery.getAvailableContent(parent, elementDeclaration, 
				ModelQuery.VALIDITY_STRICT);
		Optional<CMElementDeclaration> found = candidates.stream().filter(candidate -> {
			if (candidate instanceof CMElementDeclaration) {
				return RulesetConstants.JAVA_CLASS_ANNOTATION_TYPE.equals(((CMElementDeclaration)candidate).getElementName());
			}
			return false;
		}).findFirst();
		if (found.isPresent()) {
			return found.get();
		}
		return null;
	}
	
	private Node createAnnotationType(Element parent) {
		CMElementDeclaration linkCmNode = getAnnotationTypeCmNode(parent);
		AddNodeAction action = (AddNodeAction)ElementUiDelegate.createAddElementAction(
				model, parent, linkCmNode, parent.getChildNodes().getLength(), null, null);
		action.run();
		if (!action.getResult().isEmpty()) {
			return action.getResult().get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Node createAnnotationTypeWithPattern(String pattern, Element parent) {
		Node annotationType = createAnnotationType(parent);
		CMElementDeclaration typeCmNode = getAnnotationTypeCmNode(parent);
		List<CMAttributeDeclaration> availableAttributeList = modelQuery.getAvailableContent((Element)annotationType, typeCmNode, ModelQuery.INCLUDE_ATTRIBUTES);
		CMAttributeDeclaration patterDeclaration = availableAttributeList.get(1);
		AddNodeAction newNodeAction = new AddNodeAction(model, patterDeclaration, annotationType, annotationType.getChildNodes().getLength());
		newNodeAction.runWithoutTransaction();
		if (!newNodeAction.getResult().isEmpty()) {
			Node patternNode = (Node)newNodeAction.getResult().get(0);
			contentHelper.setNodeValue(patternNode, pattern);
		}
		return annotationType;
	}
	
	// Literal
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private CMElementDeclaration getLiteralCmNode(Element parent) {
		CMElementDeclaration elementDeclaration = modelQuery.getCMElementDeclaration(parent);
		List candidates = modelQuery.getAvailableContent(parent, elementDeclaration, 
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
	
	private Node createAnnotationLiteral(Element parent) {
		CMElementDeclaration literalCmNode = getLiteralCmNode(parent);
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
		CMElementDeclaration literalCmNode = getLiteralCmNode(parent);
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
