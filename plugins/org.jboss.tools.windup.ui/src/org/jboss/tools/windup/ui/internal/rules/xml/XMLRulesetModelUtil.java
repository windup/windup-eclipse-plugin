package org.jboss.tools.windup.ui.internal.rules.xml;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ide.undo.CreateFileOperation;
import org.eclipse.ui.ide.undo.WorkspaceUndoUtil;
import org.eclipse.ui.internal.editors.text.WorkspaceOperationRunner;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.jboss.tools.windup.model.domain.WorkspaceResourceUtils;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.explorer.TempProject;
import org.jboss.windup.tooling.rules.RuleProvider;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.collect.Lists;

@SuppressWarnings("restriction")
public class XMLRulesetModelUtil {

	public static String getRulesetId(String locationURI) {
		String rulesetId = null;
		IDOMModel model = XMLRulesetModelUtil.getModel(WorkspaceResourceUtils.getFile(locationURI), false);
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
		return rulesetId;
	}
	
	public static IFile getExternallyLinkedRuleProvider(RuleProvider ruleProvider) {
		String name = new File(ruleProvider.getOrigin()).getName();
		return new TempProject().createTmpProject().getFile(name);
	}
	
	public static List<Node> getExternalRules(String locationURI) {
		Shell shell = Display.getDefault().getActiveShell();
		
		IProject project = new TempProject().createTmpProject();
		IPath path = project.getFullPath().append(new File(locationURI).getName());
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
			collectNodes("rule", rules, document); //$NON-NLS-1$
			collectNodes("file-mapping", rules, document); //$NON-NLS-1$
			collectNodes("package-mapping", rules, document); //$NON-NLS-1$
			collectNodes("javaclass-ignore", rules, document); //$NON-NLS-1$
			model.releaseFromRead();
		}
		return rules;
	}
	
	private static void collectNodes(String tag, List<Node> rules, Document document) {
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
