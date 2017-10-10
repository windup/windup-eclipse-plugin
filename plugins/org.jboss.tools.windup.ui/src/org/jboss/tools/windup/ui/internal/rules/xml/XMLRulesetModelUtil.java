package org.jboss.tools.windup.ui.internal.rules.xml;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.ide.undo.CreateFileOperation;
import org.eclipse.ui.ide.undo.WorkspaceUndoUtil;
import org.eclipse.ui.internal.editors.text.WorkspaceOperationRunner;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.xtext.util.Pair;
import org.eclipse.xtext.util.Tuples;
import org.jboss.tools.windup.model.domain.ModelService;
import org.jboss.tools.windup.model.domain.WorkspaceResourceUtils;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.ui.internal.editor.RulesetElementUiDelegateFactory;
import org.jboss.tools.windup.ui.internal.explorer.TempProject;
import org.jboss.tools.windup.ui.internal.rules.RulesetEditorWrapper;
import org.jboss.tools.windup.ui.internal.rules.RulesNode.RulesetFileNode;
import org.jboss.tools.windup.windup.CustomRuleProvider;
import org.jboss.windup.tooling.rules.Rule;
import org.jboss.windup.tooling.rules.RuleProvider;
import org.jboss.windup.tooling.rules.RuleProviderRegistry;
import org.jboss.windup.tooling.rules.RuleProvider.RuleProviderType;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

@SuppressWarnings("restriction")
public class XMLRulesetModelUtil {

	public static String getRulesetId(String locationURI) {
		String rulesetId = null;
		IFile file = WorkspaceResourceUtils.getFile(locationURI);
		if (file == null && new File(locationURI).exists()) {
			file = XMLRulesetModelUtil.getExternallyLinkedRuleProvider(locationURI);
		}
		if (file != null) {
			IDOMModel model = XMLRulesetModelUtil.getModel(file, false);
			if (model != null) {
				Document document = model.getDocument();
				NodeList rulesets = document.getElementsByTagName("ruleset");  //$NON-NLS-1$
				if (rulesets.getLength() > 0) {
					Node ruleset = rulesets.item(0);
					NamedNodeMap attributes = ruleset.getAttributes();
					Node rulesetIdNode = attributes.getNamedItem("id");  //$NON-NLS-1$
					if (rulesetIdNode != null) {
						rulesetId = ((Attr) rulesetIdNode).getValue();
					}
				}
				model.releaseFromRead();
			}
		}
		return rulesetId;
	}
	
	public static List<Node> getExternalRules(String locationURI) {
		Shell shell = Display.getDefault().getActiveShell();
		
		IProject project = new TempProject().createTmpProject();
		IPath path = project.getFullPath().append(new File(locationURI).getAbsolutePath());
		IFile newFileHandle = IDEWorkbenchPlugin.getPluginWorkspace().getRoot().getFile(path);
		
		path = Path.fromOSString(locationURI);
		URI uri = URIUtil.toURI(path);
		
		IRunnableWithProgress op = monitor -> {
			CreateFileOperation op1 = new CreateFileOperation(newFileHandle,uri, null, IDEWorkbenchMessages.WizardNewFileCreationPage_title);
			try {
				op1.execute(monitor, WorkspaceUndoUtil.getUIInfoAdapter(shell));
			} catch (final ExecutionException e) {
				WindupUIPlugin.log(e);
			}
		};
		try {
			new WorkspaceOperationRunner().run(true, true, op);
		} catch (Exception e) {
			WindupUIPlugin.log(e);
			return Lists.newArrayList();
		}

		return XMLRulesetModelUtil.getRules(newFileHandle);
	}
	
	public static List<Node> getRules(IFile file) {
		List<Node> rules = Lists.newArrayList();
		IDOMModel model = XMLRulesetModelUtil.getModel(file, false);
		if (model != null) {
			Document document = model.getDocument();
			collectRuleNodes(document, rules);
			model.releaseFromRead();
		}
		return rules;
	}
	
	public static void collectRuleNodes(Document document, List<Node> rules) {
		collectNodes("rule", rules, document); //$NON-NLS-1$
		collectNodes("file-mapping", rules, document); //$NON-NLS-1$
		collectNodes("package-mapping", rules, document); //$NON-NLS-1$
		collectNodes("javaclass-ignore", rules, document); //$NON-NLS-1$
	}
	
	public static void collectNodes(String tag, List<Node> rules, Document document) {
		NodeList ruleNodes = document.getElementsByTagName(tag);
		if (ruleNodes.getLength() > 0) {
			for (int i = 0; i < ruleNodes.getLength(); i++) {
				Node ruleNode = ruleNodes.item(i);
				rules.add(ruleNode);
			}
		}
	}
	
	public static String getRuleId(Node node) {
		String ruleId = "";
		NamedNodeMap attributes = node.getAttributes();
		Node ruleIdNode = attributes.getNamedItem("id");  //$NON-NLS-1$
		if (ruleIdNode != null) {
			ruleId = ((Attr) ruleIdNode).getValue();
		}
		return ruleId;
	}
	
	public static String getWhereParam(Element where) {
		String param = where.getAttribute(RulesetElementUiDelegateFactory.RulesetConstants.PARAM);
		return (param != "" && !param.isEmpty()) ? param : null; //$NON-NLS-1$
	}
	
	public static String getWherePattern(Element where) {
		Element matches = findChildWithName(where, RulesetElementUiDelegateFactory.RulesetConstants.MATCHES);
		if (matches != null) {
			String pattern = matches.getAttribute(RulesetElementUiDelegateFactory.RulesetConstants.PATTERN);
			return (pattern != "" && !pattern.isEmpty()) ? pattern : null; //$NON-NLS-1$
		}
		return null;
	}
	
	public static final Element findChildWithName(Element parent, String name) {
		NodeList children = parent.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			if (Objects.equal(children.item(i).getNodeName(), name)) {
				return (Element)children.item(i);
			}
		}
		return null;
	}
	
	public static IDOMModel getModel(IFile file, boolean edit) {
		try {
			IStructuredModel model = null;
			if (!edit) {
				model = StructuredModelManager.getModelManager().getModelForRead(file);
			}
			else {
				model = StructuredModelManager.getModelManager().getModelForEdit(file);
			}
			if (model != null && model instanceof IDOMModel) {
				return (IDOMModel) model;
			}
		} catch (IOException | CoreException e) {
			WindupUIPlugin.log(e);
		}
		return null;
	}
	
	public static void openRuleInEditor(Object provider, Node ruleNode) {
		IFile file = null;
		if (provider instanceof CustomRuleProvider) {
			String locationUri = ((CustomRuleProvider)provider).getLocationURI();
			file = WorkspaceResourceUtils.getFile(locationUri);
		}
		else if (provider instanceof RuleProvider) {
			String location = ((RuleProvider)provider).getOrigin();
			file = XMLRulesetModelUtil.getExternallyLinkedRuleProvider(location);
		}
		if (file != null && file.exists()) {
			try {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				IEditorPart editor = IDE.openEditor(page, file);
				if (editor != null && ruleNode != null) {
					if (editor instanceof RulesetEditorWrapper) {
						((RulesetEditorWrapper)editor).selectAndReveal((Element)ruleNode);
					}
					else {
						editor.getSite().getSelectionProvider().setSelection(new StructuredSelection(ruleNode));
						ITextEditor textEditor = editor.getAdapter(ITextEditor.class);
						if (ruleNode instanceof IndexedRegion && textEditor != null) {
							int start = ((IndexedRegion) ruleNode).getStartOffset();
							int length = ((IndexedRegion) ruleNode).getEndOffset() - start;
							if ((start > -1) && (length > -1)) {
								textEditor.selectAndReveal(start, length);
							}
						}
					}
				}
			} catch (PartInitException e) {
				WindupUIPlugin.log(e);
		    		MessageDialog.openError(
						Display.getDefault().getActiveShell(), 
						Messages.openRuleset, 
						Messages.errorOpeningRuleset);
			}
		}
	}
	
	public static IFile getExternallyLinkedRuleProvider(String location) {
		return new TempProject().createTmpProject().getFile(location);
	}
	
	public static Pair<Object, Node> findRuleProvider(String ruleId, RuleProviderRegistry ruleProviderRegistry, ModelService modelService) {
		List<RuleProvider> systemRuleProviders = XMLRulesetModelUtil.readSystemRuleProviders(ruleProviderRegistry);
		for (RuleProvider ruleProvider : systemRuleProviders) {
			for (Rule rule : ruleProvider.getRules()) {
				if (Objects.equal(ruleId, rule.getRuleID())) {
					List<Node> ruleNodes = XMLRulesetModelUtil.getExternalRules(ruleProvider.getOrigin());
					for (Node ruleNode : ruleNodes) {
						String ruleNodeId = XMLRulesetModelUtil.getRuleId(ruleNode);
						if (Objects.equal(ruleId, ruleNodeId)) {
							return Tuples.create(ruleProvider, ruleNode);
						}
					}
				}
			}
		}
		for (CustomRuleProvider ruleProvider : modelService.getModel().getCustomRuleRepositories()) {
			List<Object> children = Lists.newArrayList();
			children.add(new RulesetFileNode(ruleProvider, new File(ruleProvider.getLocationURI()), RuleProviderType.XML));
			IFile file = WorkspaceResourceUtils.getFile(ruleProvider.getLocationURI());
			for (Node ruleNode : XMLRulesetModelUtil.getRules(file)) {
				String ruleNodeId = XMLRulesetModelUtil.getRuleId(ruleNode);
				if (Objects.equal(ruleId, ruleNodeId)) {
					return Tuples.create(ruleProvider, ruleNode);
				}
			}
		}
		return null;
	}
	
	public static List<RuleProvider> readSystemRuleProviders(RuleProviderRegistry ruleProviderRegistry) {
		List<RuleProvider> ruleProviders = ruleProviderRegistry.getRuleProviders().stream().filter(provider -> {
			return isFileBasedProvider(provider);
		}).collect(Collectors.toList());
		sortRuleProviders(ruleProviders);
		return ruleProviders;
	}
	
	private static boolean isFileBasedProvider(RuleProvider provider) {
		switch (provider.getRuleProviderType()) {
			case GROOVY:
			case XML:
				return true;
			default: 
				return false;
		}
	}
	
	private static void sortRuleProviders(List<RuleProvider> ruleProviders) {
		Collections.sort(ruleProviders, (RuleProvider provider1, RuleProvider provider2) -> {
			if (provider1.getProviderID() == null) {
				return -1;
			}
			if (provider2.getProviderID() == null) {
				return 1;
			}
			return provider1.getProviderID().compareTo(provider2.getProviderID());
		});
	}
	
	
	/*private IDocument getDocument(IEditorInput editorInput) {
		return getDocumentProvider().getDocument(editorInput);
	}
	
	private IDocumentProvider getDocumentProvider(IEditorInput editorInput) {
		IDocumentProvider documentProvider = DocumentProviderRegistry.getDefault().getDocumentProvider(editorInput);
		try {
			documentProvider.connect(editorInput);
		} catch (CoreException e) {
			WindupUIPlugin.log(e);
		}
		return documentProvider;
	}*/
}
