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

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.bindings.TriggerSequence;
import org.eclipse.jface.bindings.keys.KeySequence;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.URLHyperlink;
import org.eclipse.jface.text.source.AnnotationModel;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.IAnnotationAccess;
import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.ISharedTextColors;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.OverviewRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.mylyn.internal.wikitext.ui.WikiTextUiPlugin;
import org.eclipse.mylyn.internal.wikitext.ui.editor.MarkupProjectionViewer;
import org.eclipse.mylyn.internal.wikitext.ui.editor.syntax.FastMarkupPartitioner;
import org.eclipse.mylyn.internal.wikitext.ui.editor.syntax.MarkupDocumentProvider;
import org.eclipse.mylyn.wikitext.markdown.MarkdownLanguage;
import org.eclipse.mylyn.wikitext.parser.Attributes;
import org.eclipse.mylyn.wikitext.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.parser.builder.HtmlDocumentBuilder;
import org.eclipse.mylyn.wikitext.parser.markup.AbstractMarkupLanguage;
import org.eclipse.mylyn.wikitext.ui.editor.MarkupSourceViewerConfiguration;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.ProgressAdapter;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.internal.editors.text.EditorsPlugin;
import org.eclipse.ui.keys.IBindingService;
import org.eclipse.ui.texteditor.AnnotationPreference;
import org.eclipse.ui.texteditor.DefaultMarkerAnnotationAccess;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.MarkerAnnotationPreferences;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;
import org.eclipse.wst.xml.core.internal.contentmodel.CMElementDeclaration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.editor.AddNodeAction;
import org.jboss.tools.windup.ui.internal.editor.ElementAttributesContainer;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory.RulesetConstants;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.base.Objects;

@SuppressWarnings("restriction")
public class HintMessageTab extends ElementAttributesContainer {
	
	protected static final int VERTICAL_RULER_WIDTH = 12;
	
	private static final String CSS_CLASS_EDITOR_PREVIEW = "editorPreview"; //$NON-NLS-1$
	
	private Browser browser;
	private IDocument document;
	private SashForm sash;
	
	private SourceViewer sourceViewer;
	private MarkdownLanguage language = new MarkdownLanguage();
	
	private IHandlerService handlerService;
	private IHandlerActivation contentAssistHandlerActivation;
	
	@PostConstruct
	public void createControls(Composite parent, CTabItem item) {
		item.setText(Messages.messageTab);
		handlerService = PlatformUI.getWorkbench().getService(IHandlerService.class);
		parent = createMessageSection(parent);
		createSash(parent);
		createSourceViewer(sash);
		parent = createPreviewSection(sash);
		createBrowser(parent);
		updatePreview();
	}
	
	private Composite createMessageSection(Composite parent) {
		Section section = createSection(parent, Messages.RulesetEditor_messageSection, Section.DESCRIPTION|ExpandableComposite.TITLE_BAR|Section.NO_TITLE_FOCUS_BOX);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(section);
		section.setDescription(NLS.bind(Messages.RulesetEditor_messageContentAssist, getBinding()));
		return (Composite)section.getClient();
	}
	
	private String getBinding() {
	    final IBindingService bindingSvc= PlatformUI.getWorkbench().getAdapter(IBindingService.class);
		TriggerSequence binding= bindingSvc.getBestActiveBindingFor(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
		if (binding instanceof KeySequence) {
			return binding.format();
		}
		return "";
    }
	
	private Composite createPreviewSection(Composite parent) {
		Section section = createSection(parent, Messages.RulesetEditor_previewSection, ExpandableComposite.TITLE_BAR|Section.NO_TITLE_FOCUS_BOX);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(section);
		section.setDescription(NLS.bind(Messages.RulesetEditor_messageSectionDescription, RulesetConstants.HINT_NAME));
		return (Composite)section.getClient();
	}
	
	@Override
	protected void bind() {
		String message = getElementMessage();
		if (!Objects.equal(document.get(), message)) {
			document.set(getElementMessage());
		}
		sourceViewer.invalidateTextPresentation();
		updatePreview();
	}
	
	private void createSash(Composite parent) {
		this.sash = new SashForm(parent, SWT.SMOOTH|SWT.HORIZONTAL);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(sash);
		sash.setOrientation(SWT.VERTICAL);
		sash.setFont(parent.getFont());
		sash.setVisible(true);
	}
	
	private void createBrowser(Composite parent) {
		browser = new Browser(parent, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(browser);
		browser.addLocationListener(new LocationListener() {
			public void changed(LocationEvent event) {
				event.doit = false;
			}
			public void changing(LocationEvent event) {
				// if it looks like an absolute URL
				if (event.location.matches("([a-zA-Z]{3,8})://?.*")) { //$NON-NLS-1$
					// workaround for browser problem (bug 262043)
					int idxOfSlashHash = event.location.indexOf("/#"); //$NON-NLS-1$
					if (idxOfSlashHash != -1) {
						// allow javascript-based scrolling to work
						if (!event.location.startsWith("file:///#")) { //$NON-NLS-1$
							event.doit = false;
						}
						return;
					}
					// workaround end
					event.doit = false;
					try {
						IWebBrowser webBrowser = PlatformUI.getWorkbench()
								.getBrowserSupport()
								.createBrowser("org.eclipse.ui.browser"); //$NON-NLS-1$
						webBrowser.openURL(new URL(event.location));
					} catch (Exception e) {
						new URLHyperlink(new Region(0, 1), event.location).open();
					}
				}
			}
		});
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private CMElementDeclaration getMessageCmNode() {
		List candidates = modelQuery.getAvailableContent(element, elementDeclaration, 
				ModelQuery.VALIDITY_STRICT);
		Optional<CMElementDeclaration> found = candidates.stream().filter(candidate -> {
			if (candidate instanceof CMElementDeclaration) {
				return RulesetConstants.MESSAGE.equals(((CMElementDeclaration)candidate).getElementName());
			}
			return false;
		}).findFirst();
		if (found.isPresent()) {
			return found.get();
		}
		return null;
	} 
	
	private Node getMessageNode() {
		NodeList list = element.getElementsByTagName(RulesetConstants.MESSAGE);
		if (list.getLength() > 0) {
			return list.item(0);
		}
		return null;
	}
	
	private String getElementMessage() {
		String message = "";
		Element node = (Element)getMessageNode();
		if (node != null) {
			message = getCdataMessage(node);
			if (message == null) {
				message = contentHelper.getNodeValue(node);
			}
		}
		return message;
	}
	
	private String getCdataMessage(Node messageNode) {
		String message = null;
		Node cdataNode = getCdataNode(messageNode);
		if (cdataNode != null) {
			message = contentHelper.getNodeValue(cdataNode);
		}
		return message;
	}

	private Node getCdataNode(Node messageNode) {
		NodeList childList = messageNode.getChildNodes();
		for (int i = 0; i < childList.getLength(); i++) {
			Node child = childList.item(i);
			if (Objects.equal(child.getNodeName(), RulesetConstants.CDATA)) {
				return child;
			}
		}
		return null;
	}
	
	protected void setMessage(String value) {
		Node node = getMessageNode();
		CMNode cmNode = getMessageCmNode();
		try {
			model.aboutToChangeModel();
			if (node != null) {
				Node cdataNode = getCdataNode(node);
				String currentMessage = contentHelper.getNodeValue(node);
				if (currentMessage != null && !currentMessage.isEmpty()) {
					contentHelper.setNodeValue(node, value);
				}
				else if (cdataNode != null) {
					contentHelper.setNodeValue(cdataNode, value);
				}
				else {
					createCdataNode(node, value);
				}
			}
			else {
				AddNodeAction newNodeAction = new AddNodeAction(model, cmNode, element, element.getChildNodes().getLength());
				newNodeAction.runWithoutTransaction();
				List<Node> newNodes = newNodeAction.getResult();
				if (!newNodes.isEmpty()) {
					node = newNodes.get(0);
					contentHelper.setNodeValue(node, "");
					createCdataNode(node, value);
				}
			}
		}
		finally {
			model.changedModel();
		}
	}
	
	private void createCdataNode(Node messageNode, String value) {
		Node cdataNode = messageNode.getOwnerDocument().createCDATASection(value);
		messageNode.appendChild(cdataNode);
	}
	
	protected void createSourceViewer(Composite parent) {
		String string = getElementMessage();
		IStorage storage = new StringInput.StringStorage(string);
		IEditorInput editorInput = new StringInput(storage);
		CompositeRuler ruler = new CompositeRuler();
		ISharedTextColors colors = EditorsPlugin.getDefault().getSharedTextColors();
		int styles = SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION | SWT.WRAP;
		IOverviewRuler overviewRuler = new OverviewRuler(new DefaultMarkerAnnotationAccess(), VERTICAL_RULER_WIDTH, colors);
		
		this.sourceViewer = new MarkupProjectionViewer(parent, ruler, overviewRuler, true,	styles);
		
		GridDataFactory.fillDefaults().grab(true, false).hint(SWT.DEFAULT, 500).applyTo(sourceViewer.getControl());
		try {
			MarkupDocumentProvider documentProvider = new MarkupDocumentProvider();
			documentProvider.connect(editorInput);
			this.document = documentProvider.getDocument(editorInput);
			//sourceViewer.setDocument(document);
			
			((AbstractMarkupLanguage) language).setEnableMacros(false);
			documentProvider.setMarkupLanguage(language);
			
			MarkupSourceViewerConfiguration sourceViewerConfiguration = new MarkupSourceViewerConfiguration(WikiTextUiPlugin.getDefault().getPreferenceStore());
			sourceViewerConfiguration.initializeDefaultFonts();
			sourceViewerConfiguration.setMarkupLanguage(language);
			sourceViewer.configure(sourceViewerConfiguration);
			
			IDocumentPartitioner partitioner = document.getDocumentPartitioner();
			FastMarkupPartitioner fastMarkupPartitioner = (FastMarkupPartitioner) partitioner;
			fastMarkupPartitioner.setMarkupLanguage(language);
		} catch (CoreException e) {
			WindupUIPlugin.log(e);
		}
		
		sourceViewer.getTextWidget().addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				deactivateEditorHandlers();
			}
			@Override
			public void focusGained(FocusEvent e) {
				activatEditorHandlers();
			}
		});
		
		IDocumentListener documentListener = new IDocumentListener() {
			public void documentAboutToBeChanged(DocumentEvent event) {}
			public void documentChanged(DocumentEvent event) {
				if (!blockNotification) {
					setMessage(document.get());
					updatePreview();
				}
			}
		};
		
		sourceViewer.getTextWidget().addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				document.removeDocumentListener(documentListener);
				deactivateEditorHandlers();
			}
		});
		
		document.addDocumentListener(documentListener);
		
		configureAsEditor(sourceViewer, (Document)document);
	}
	
	private void configureAsEditor(ISourceViewer viewer, Document document) {
		IAnnotationAccess annotationAccess = new DefaultMarkerAnnotationAccess();
		final SourceViewerDecorationSupport support = new SourceViewerDecorationSupport(viewer, null, annotationAccess,
				EditorsUI.getSharedTextColors());
		Iterator<?> e = new MarkerAnnotationPreferences().getAnnotationPreferences().iterator();
		while (e.hasNext()) {
			support.setAnnotationPreference((AnnotationPreference) e.next());
		}
		support.install(EditorsUI.getPreferenceStore());
		viewer.getTextWidget().addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				support.uninstall();
			}
		});
		AnnotationModel annotationModel = new AnnotationModel();
		viewer.setDocument(document, annotationModel);
	}
	
	private void activatEditorHandlers() {
		contentAssistHandlerActivation = handlerService.activateHandler(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS, 
				new ActionHandler(new AssistAction(sourceViewer)));
	}
	
	private void deactivateEditorHandlers() {
		if (contentAssistHandlerActivation != null) {
			handlerService.deactivateHandler(contentAssistHandlerActivation);
			contentAssistHandlerActivation = null;
		}
	}
	
	private static class AssistAction extends Action {
		private ITextOperationTarget fOperationTarget;

		public AssistAction(SourceViewer sourceViewer) {
			this.fOperationTarget = sourceViewer.getTextOperationTarget();
		}
		
		@Override
		public void run() {
			BusyIndicator.showWhile(Display.getDefault(), new Runnable() {
				@Override
				public void run() {
					fOperationTarget.doOperation(ISourceViewer.CONTENTASSIST_PROPOSALS);
				}
			});
		}
	}
	
	/**
	 * JavaScript that returns the current top scroll position of the browser widget
	 */
	private static final String JAVASCRIPT_GETSCROLLTOP = "function getScrollTop() { " //$NON-NLS-1$
			+ "  if(typeof pageYOffset!='undefined') return pageYOffset;" //$NON-NLS-1$
			+ "  else{" + //$NON-NLS-1$
			"var B=document.body;" + //$NON-NLS-1$
			"var D=document.documentElement;" + //$NON-NLS-1$
			"D=(D.clientHeight)?D:B;return D.scrollTop;}" //$NON-NLS-1$
			+ "}; return getScrollTop();"; //$NON-NLS-1$
	
	private void updatePreview() {
		Object result = browser.evaluate(JAVASCRIPT_GETSCROLLTOP);
		final int verticalScrollbarPos = result != null ? ((Number) result).intValue() : 0;
		String title = file == null ? "" : file.getName(); //$NON-NLS-1$
		if (title.lastIndexOf('.') != -1) {
			title = title.substring(0, title.lastIndexOf('.'));
		}
		StringWriter writer = new StringWriter();
		HtmlDocumentBuilder builder = new HtmlDocumentBuilder(writer) {
			@Override
			protected void emitAnchorHref(String href) {
				if (href != null && href.startsWith("#")) { //$NON-NLS-1$
					writer.writeAttribute("onclick", //$NON-NLS-1$
							String.format("javascript: window.location.hash = '%s'; return false;", href)); //$NON-NLS-1$
					writer.writeAttribute("href", "#"); //$NON-NLS-1$//$NON-NLS-2$
				} else {
					super.emitAnchorHref(href);
				}
			}

			@Override
			public void beginHeading(int level, Attributes attributes) {
				attributes.appendCssClass(CSS_CLASS_EDITOR_PREVIEW);
				super.beginHeading(level, attributes);
			}

			@Override
			public void beginBlock(BlockType type, Attributes attributes) {
				attributes.appendCssClass(CSS_CLASS_EDITOR_PREVIEW);
				super.beginBlock(type, attributes);
			}
		};
		builder.setTitle(title);

		String css = WikiTextUiPlugin.getDefault().getPreferences().getMarkupViewerCss();
		builder.addCssStylesheet(new HtmlDocumentBuilder.Stylesheet(new StringReader(css)));

		AbstractMarkupLanguage markupLanguage = (AbstractMarkupLanguage)language.clone();
		markupLanguage.setEnableMacros(true);

		markupLanguage.setFilterGenerativeContents(false);
		markupLanguage.setBlocksOnly(false);

		MarkupParser markupParser = new MarkupParser();
		markupParser.setBuilder(builder);
		markupParser.setMarkupLanguage(markupLanguage);

		markupParser.parse(document.get());
		browser.addProgressListener(new ProgressAdapter() {
			@Override
			public void completed(ProgressEvent event) {
				browser.removeProgressListener(this);
				browser.execute(String.format("window.scrollTo(0,%d);", verticalScrollbarPos)); //$NON-NLS-1$
			}
		});
		String xhtml = writer.toString();
		browser.setText(xhtml);
	}
}
