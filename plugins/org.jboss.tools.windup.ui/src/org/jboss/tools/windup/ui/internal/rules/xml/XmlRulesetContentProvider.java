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
package org.jboss.tools.windup.ui.internal.rules.xml;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.eclipse.wst.sse.ui.internal.contentoutline.IJFaceNodeAdapter;
import org.eclipse.wst.sse.ui.internal.contentoutline.IJFaceNodeAdapterFactory;
import org.eclipse.wst.xml.core.internal.contentmodel.CMDocument;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.CMDocumentManager;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.CMDocumentManagerListener;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.core.internal.contentmodel.util.CMDescriptionBuilder;
import org.eclipse.wst.xml.core.internal.contentmodel.util.CMDocumentCache;
import org.eclipse.wst.xml.core.internal.modelquery.ModelQueryUtil;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.ui.internal.tabletree.TreeContentHelper;
import org.jboss.tools.windup.windup.CustomRuleProvider;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

@SuppressWarnings("restriction")
public class XmlRulesetContentProvider implements ITreeContentProvider, CMDocumentManagerListener {

	protected CMDocumentManager documentManager;

	private TreeViewer fViewer = null;

	private TreeContentHelper treeContentHelper = new TreeContentHelper();

	private CMDescriptionBuilder descriptionBuilder = new CMDescriptionBuilder();

	private Color fCMColor = null;
	
	private CustomRuleProvider ruleProvider;
	
	public XmlRulesetContentProvider(CustomRuleProvider ruleProvider, TreeViewer treeViewer) {
		this.ruleProvider = ruleProvider;
		this.fViewer = treeViewer;
	}
	
	public Object[] getRules() {
		return XmlRulesetModelUtil.getRules(ruleProvider.getLocationURI()).
				stream().toArray(Object[]::new);
	}

	// CMDocumentManagerListener
	public void cacheCleared(CMDocumentCache cache) {
		doDelayedRefreshForViewers();
	}

	public void cacheUpdated(CMDocumentCache cache, final String uri, int oldStatus, int newStatus, CMDocument cmDocument) {
		if ((newStatus == CMDocumentCache.STATUS_LOADED) || (newStatus == CMDocumentCache.STATUS_ERROR)) {
			doDelayedRefreshForViewers();
		}
	}

	public void dispose() {
		if (documentManager != null) {
			documentManager.removeListener(this);
		}
		if (fViewer != null && fViewer.getInput() != null) {
			if (fViewer.getInput() instanceof IDOMNode) {
				IJFaceNodeAdapterFactory factory = (IJFaceNodeAdapterFactory) ((IDOMNode) fViewer.getInput()).getModel().getFactoryRegistry().getFactoryFor(IJFaceNodeAdapter.class);
				if (factory != null) {
					factory.removeListener(fViewer);
				}
			}
		}
		if (fCMColor != null) {
			fCMColor.dispose();
		}
	}

	private void doDelayedRefreshForViewers() {
		if ((fViewer != null) && !fViewer.getControl().isDisposed()) {
			fViewer.getControl().getDisplay().asyncExec(new Runnable() {
				public void run() {
					if ((fViewer != null) && !fViewer.getControl().isDisposed()) {
						fViewer.refresh(ruleProvider, true);
					}
				}
			});
		}
	}

	public Object[] getChildren(Object element) {
		if (element instanceof INodeNotifier) {
			((INodeNotifier) element).getAdapterFor(IJFaceNodeAdapter.class);
		}
		return treeContentHelper.getChildren(element);
	}

	public Object[] getElements(Object element) {
		return getChildren(element);
	}

	public Object getParent(Object o) {
		if (o instanceof INodeNotifier) {
			((INodeNotifier) o).getAdapterFor(IJFaceNodeAdapter.class);
		}

		Object result = null;
		if (o instanceof Node) {
			Node node = (Node) o;
			if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
				result = ((Attr) node).getOwnerElement();
			}
			else {
				result = node.getParentNode();
			}
		}
		return result;
	}

	public Element getRootElement(Document document) {
		Element rootElement = null;

		for (Node childNode = document.getFirstChild(); childNode != null; childNode = childNode.getNextSibling()) {
			if (childNode.getNodeType() == Node.ELEMENT_NODE) {
				rootElement = (Element) childNode;
				break;
			}
		}
		return rootElement;
	}

	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// remove our listeners to the old state
		if (oldInput != null) {
			Document domDoc = (Document) oldInput;
			ModelQuery mq = ModelQueryUtil.getModelQuery(domDoc);
			if (mq != null) {
				documentManager = mq.getCMDocumentManager();
				if (documentManager != null) {
					documentManager.removeListener(this);
				}
			}
		}

		if ((oldInput != null) && (oldInput instanceof IDOMNode)) {
			IJFaceNodeAdapterFactory factory = (IJFaceNodeAdapterFactory) ((IDOMNode) oldInput).getModel().getFactoryRegistry().getFactoryFor(IJFaceNodeAdapter.class);
			if (factory != null) {
				factory.removeListener(viewer);
			}
		}

		if ((newInput != null) && (newInput instanceof IDOMNode)) {
			IJFaceNodeAdapterFactory factory = (IJFaceNodeAdapterFactory) ((IDOMNode) newInput).getModel().getFactoryRegistry().getFactoryFor(IJFaceNodeAdapter.class);
			if (factory != null) {
				factory.addListener(viewer);
			}
		}

		if (newInput != null) {
			Document domDoc = (Document) newInput;
			ModelQuery mq = ModelQueryUtil.getModelQuery(domDoc);

			if (mq != null) {
				documentManager = mq.getCMDocumentManager();
				if (documentManager != null) {
					documentManager.setPropertyEnabled(CMDocumentManager.PROPERTY_ASYNC_LOAD, true);
					documentManager.addListener(this);
				}
			}
		}
	}

	public void propertyChanged(CMDocumentManager cmDocumentManager, String propertyName) {
		if (cmDocumentManager.getPropertyEnabled(CMDocumentManager.PROPERTY_AUTO_LOAD)) {
			doDelayedRefreshForViewers();
		}
	}
}
