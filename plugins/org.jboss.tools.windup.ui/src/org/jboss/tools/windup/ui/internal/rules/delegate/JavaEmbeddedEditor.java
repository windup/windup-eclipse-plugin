package org.jboss.tools.windup.ui.internal.rules.delegate;

import java.util.regex.Pattern;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.internal.debug.ui.JDIDebugUIPlugin;
import org.eclipse.jdt.internal.debug.ui.JDISourceViewer;
import org.eclipse.jdt.internal.debug.ui.contentassist.IJavaDebugContentAssistContext;
import org.eclipse.jdt.internal.debug.ui.contentassist.JavaDebugContentAssistProcessor;
import org.eclipse.jdt.internal.debug.ui.contentassist.TypeContext;
import org.eclipse.jdt.internal.debug.ui.display.DisplayViewerConfiguration;
import org.eclipse.jdt.internal.debug.ui.propertypages.PropertyPageMessages;
import org.eclipse.jdt.ui.text.IJavaPartitions;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITextViewerExtension6;
import org.eclipse.jface.text.IUndoManager;
import org.eclipse.jface.text.IUndoManagerExtension;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.operations.OperationHistoryActionHandler;
import org.eclipse.ui.operations.RedoActionHandler;
import org.eclipse.ui.operations.UndoActionHandler;
import org.eclipse.ui.texteditor.IAbstractTextEditorHelpContextIds;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;

@SuppressWarnings("restriction")
public class JavaEmbeddedEditor {

//	private Button fConditional;
//	private Button fWhenTrue;
//	private Button fWhenChange;

	private JDISourceViewer fViewer;
	private IContentAssistProcessor fCompletionProcessor;
	private IHandlerService fHandlerService;
	private IHandler fContentAssistHandler;
	private IHandlerActivation fContentAssistActivation;
	private IHandler fUndoHandler;
	private IHandlerActivation fUndoActivation;
	private IHandler fRedoHandler;
	private IHandlerActivation fRedoActivation;

	private IDocumentListener fDocumentListener;

	private Combo fConditionHistory;
	private IDialogSettings fConditionHistoryDialogSettings;
	private boolean fReplaceConditionInHistory;
	private int fSeparatorIndex;

	private IViewSite fBreakpointsViewSite;
	private IAction fViewUndoAction;
	private IAction fViewRedoAction;
	private OperationHistoryActionHandler fViewerUndoAction;
	private OperationHistoryActionHandler fViewerRedoAction;


	/**
	 * Property id for breakpoint condition expression.
	 */
	public static final int PROP_CONDITION= 0x1001;

	/**
	 * Property id for breakpoint condition enabled state.
	 */
	public static final int PROP_CONDITION_ENABLED= 0x1002;

	/**
	 * Property id for breakpoint condition suspend policy.
	 */
	public static final int PROP_CONDITION_SUSPEND_POLICY= 0x1003;

	private static final int MAX_HISTORY_SIZE= 10;
	private static final String DS_SECTION_CONDITION_HISTORY= "conditionHistory"; //$NON-NLS-1$
	private static final String DS_KEY_HISTORY_ENTRY_COUNT= "conditionHistoryEntryCount"; //$NON-NLS-1$
	private static final String DS_KEY_HISTORY_ENTRY_PREFIX= "conditionHistoryEntry_"; //$NON-NLS-1$
	private static final Pattern NEWLINE_PATTERN= Pattern.compile("\r\n|\r|\n"); //$NON-NLS-1$;
	
	public JavaEmbeddedEditor(Composite parent) {
		fConditionHistoryDialogSettings = DialogSettings.getOrCreateSection(JDIDebugUIPlugin.getDefault().getDialogSettings(), DS_SECTION_CONDITION_HISTORY);
		createControls(parent);
		load();
		setEnabled(true, true);
	}
	
	public void setInput(Object input) {
		load();
	}

	private void load() {
		if (fDocumentListener != null) {
			fViewer.getDocument().removeDocumentListener(fDocumentListener);
			fDocumentListener = null;
		}
		fViewer.unconfigure();
		IDocument document = new Document();
		JDIDebugUIPlugin.getDefault().getJavaTextTools().setupJavaDocumentPartitioner(document, IJavaPartitions.JAVA_PARTITIONING);
		fViewer.setInput(document);
		String condition = null;
		IType type = null;
		boolean controlsEnabled = false;
		boolean conditionEnabled = false;
		boolean whenTrue = true;
		IJavaDebugContentAssistContext context = new TypeContext(null, -1);
		/*String source = null;
		ICompilationUnit compilationUnit = type.getCompilationUnit();
		if (compilationUnit != null && compilationUnit.getJavaProject().getProject().exists()) {
			source = compilationUnit.getSource();
		}
		else {
			IClassFile classFile = type.getClassFile();
			if (classFile != null) {
				source = classFile.getSource();
			}
		}
		int position= -1;
		if (breakpoint instanceof IJavaWatchpoint) {
			IField[] fields = type.getFields();
			// Taking first field
			ISourceRange sourceRange = fields[0].getNameRange();
			position = sourceRange.getOffset();
			if (source != null && position != -1) {
				try {
					// to get offset of the first character of line in which field is defined
					int lineNumber = new Document(source).getLineOfOffset(position);
					position = new Document(source).getLineOffset(lineNumber);
				}
				catch (BadLocationException e) {
					// ignore, breakpoint line is out-of-date with the document
				}
			}

		} else {
			int lineNumber = breakpoint.getMarker().getAttribute(IMarker.LINE_NUMBER, -1);
			if (source != null && lineNumber != -1) {
				try {
					position = new Document(source).getLineOffset(lineNumber - 1);
				}
				catch (BadLocationException e) {
					// ignore, breakpoint line is out-of-date with the document
				}
			}
		}
		context = new TypeContext(type, position);*/
		fCompletionProcessor = new JavaDebugContentAssistProcessor(context);
		document.set((condition == null ? "" : condition)); //$NON-NLS-1$
		fViewer.configure(new DisplayViewerConfiguration() {
			@Override
			public IContentAssistProcessor getContentAssistantProcessor() {
					return fCompletionProcessor;
			}
		});
		fDocumentListener = new IDocumentListener() {
			@Override
			public void documentAboutToBeChanged(DocumentEvent event) {
			}
			@Override
			public void documentChanged(DocumentEvent event) {
				//setDirty(PROP_CONDITION);
			}
		};
		fViewer.getDocument().addDocumentListener(fDocumentListener);
//		fConditional.setEnabled(controlsEnabled);
//		fConditional.setSelection(conditionEnabled);
//		fWhenTrue.setSelection(whenTrue);
//		fWhenChange.setSelection(!whenTrue);
		//setEnabled(conditionEnabled && breakpoint != null && breakpoint.supportsCondition(), false);
		//setDirty(false);
		checkIfUsedInBreakpointsView();
		registerViewerUndoRedoActions();
	}

	public Control createControls(Composite parent) {
		fViewer = new JDISourceViewer(parent, null, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.LEFT_TO_RIGHT);
		fViewer.setEditable(false);
		ControlDecoration decoration = new ControlDecoration(fViewer.getControl(), SWT.TOP | SWT.LEFT);
		decoration.setShowOnlyOnFocus(true);
		FieldDecoration dec = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_CONTENT_PROPOSAL);
		decoration.setImage(dec.getImage());
		decoration.setDescriptionText(dec.getDescription());
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		// set height/width hints based on font
		GC gc = new GC(fViewer.getTextWidget());
		gc.setFont(fViewer.getTextWidget().getFont());
		FontMetrics fontMetrics = gc.getFontMetrics();
		gd.heightHint = Dialog.convertHeightInCharsToPixels(fontMetrics, 17);
		gd.widthHint = Dialog.convertWidthInCharsToPixels(fontMetrics, 40);
		gc.dispose();
		fViewer.getControl().setLayoutData(gd);
		fContentAssistHandler= new AbstractHandler() {
			@Override
			public Object execute(ExecutionEvent event) throws org.eclipse.core.commands.ExecutionException {
				fViewer.doOperation(ISourceViewer.CONTENTASSIST_PROPOSALS);
				return null;
			}
		};
		fUndoHandler= new AbstractHandler() {
			@Override
			public Object execute(ExecutionEvent event) throws org.eclipse.core.commands.ExecutionException {
				fViewer.doOperation(ITextOperationTarget.UNDO);
				return null;
			}
		};
		fRedoHandler= new AbstractHandler() {
			@Override
			public Object execute(ExecutionEvent event) throws org.eclipse.core.commands.ExecutionException {
				fViewer.doOperation(ITextOperationTarget.REDO);
				return null;
			}
		};
		fHandlerService = PlatformUI.getWorkbench().getAdapter(IHandlerService.class);
		fViewer.getControl().addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				activateHandlers();
			}
			@Override
			public void focusLost(FocusEvent e) {
				deactivateHandlers();
			}
		});
		parent.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				dispose();
			}
		});
		fViewer.getTextWidget().addVerifyKeyListener(new VerifyKeyListener() {
			@Override
			public void verifyKey(VerifyEvent event) {
				fViewer.getTextWidget().getText();
			}
		});
		return parent;
	}
	
	public JDISourceViewer getSourceViewer() {
		return fViewer;
	}

	protected void dispose() {
		deactivateHandlers();
		if (fDocumentListener != null) {
			fViewer.getDocument().removeDocumentListener(fDocumentListener);
		}
		fViewer.dispose();
	}

	public void setFocus() {
	}
	
	public void save() {
	}

	public boolean isDirty() {
		return false;
	}

	private void activateHandlers() {
		fContentAssistActivation= fHandlerService.activateHandler(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS, fContentAssistHandler);
		checkIfUsedInBreakpointsView();
		if (fBreakpointsViewSite == null) {
			fUndoActivation= fHandlerService.activateHandler(IWorkbenchCommandConstants.EDIT_UNDO, fUndoHandler);
			fRedoActivation= fHandlerService.activateHandler(IWorkbenchCommandConstants.EDIT_REDO, fRedoHandler);
		} else {
			registerViewerUndoRedoActions();
		}
	}

	private void deactivateHandlers() {
		if (fContentAssistActivation != null) {
			fHandlerService.deactivateHandler(fContentAssistActivation);
			fContentAssistActivation= null;
		}
		if (fUndoActivation != null) {
			fHandlerService.deactivateHandler(fUndoActivation);
			fUndoActivation= null;
		}
		if (fRedoActivation != null) {
			fHandlerService.deactivateHandler(fRedoActivation);
			fRedoActivation= null;
		}

		if (fBreakpointsViewSite != null) {
			fBreakpointsViewSite.getActionBars().setGlobalActionHandler(ITextEditorActionConstants.UNDO, fViewUndoAction);
			fBreakpointsViewSite.getActionBars().setGlobalActionHandler(ITextEditorActionConstants.REDO, fViewRedoAction);
			fBreakpointsViewSite.getActionBars().updateActionBars();
			disposeViewerUndoRedoActions();
		}
	}

	private void disposeViewerUndoRedoActions() {
		if (fViewerUndoAction != null) {
			fViewerUndoAction.dispose();
			fViewerUndoAction= null;
		}
		if (fViewerRedoAction != null) {
			fViewerRedoAction.dispose();
			fViewerRedoAction= null;
		}
	}

	public void setEnabled(boolean enabled, boolean focus) {
		fViewer.setEditable(enabled);
		fViewer.getTextWidget().setEnabled(enabled);
//		fWhenChange.setEnabled(enabled);
//		fWhenTrue.setEnabled(enabled);
		if (enabled) {
			fViewer.updateViewerColors();
			if (focus) {
				setFocus();
			}
		} else {
			Color color = fViewer.getControl().getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
			fViewer.getTextWidget().setBackground(color);
		}
		if (hasConditionHistory()) {
			fConditionHistory.setEnabled(enabled);
		}
	}

	public Object getInput() {
		return null;
	}


	/**
	 * Tells whether this editor shows a condition history drop-down list.
	 *
	 * @return <code>true</code> if this editor shows a condition history drop-down list,
	 *         <code>false</code> otherwise
	 */
	private boolean hasConditionHistory() {
		return fConditionHistory != null;
	}

	/**
	 * Initializes the condition history drop-down with values.
	 */
	private void initializeConditionHistoryDropDown() {
		fConditionHistory.setItems(getConditionHistoryLabels());
		String userHint= PropertyPageMessages.JavaBreakpointConditionEditor_choosePreviousCondition;
		fConditionHistory.add(userHint, 0);
		fConditionHistory.setText(userHint);
	}

	/**
	 * Returns the condition history labels for the current breakpoint.
	 *
	 * @return an array of strings containing the condition history labels
	 */
	private String[] getConditionHistoryLabels() {
		String[] conditions= getConditionHistory();
		String[] labels= new String[conditions.length];
		for (int i= 0; i < conditions.length; i++) {
			labels[i]= NEWLINE_PATTERN.matcher(conditions[i]).replaceAll(" "); //$NON-NLS-1$
		}
		return labels;
	}

	/**
	 * Returns the condition history entries for the current breakpoint.
	 *
	 * @return an array of strings containing the history of conditions
	 */
	private String[] getConditionHistory() {
		/*fSeparatorIndex= -1;

		// Get global history
		String[] globalItems= readConditionHistory(fConditionHistoryDialogSettings);

		// Get local history
		Stack<String> localHistory=  fLocalConditionHistory.get(fBreakpoint);
		if (localHistory == null) {
			return globalItems;
		}

		// Create combined history
		int localHistorySize= Math.min(localHistory.size(), MAX_HISTORY_SIZE);
		String[] historyItems= new String[localHistorySize + globalItems.length + 1];
		for (int i= 0; i < localHistorySize; i++) {
			historyItems[i]= localHistory.get(localHistory.size() - i - 1);
		}
		fSeparatorIndex= localHistorySize;
		historyItems[localHistorySize]= getSeparatorLabel();
		System.arraycopy(globalItems, 0, historyItems, localHistorySize + 1, globalItems.length);
		return historyItems;*/
		return new String[] {};
	}

	/**
	 * Updates the local and global condition histories.
	 */
	public void updateConditionHistories() {
		/*String newItem= fViewer.getDocument().get();
		if (newItem.length() == 0) {
			return;
		}

		// Update local history
		Stack<String> localHistory= fLocalConditionHistory.get(fBreakpoint);
		if (localHistory == null) {
			localHistory= new Stack<>();
			fLocalConditionHistory.put(fBreakpoint, localHistory);
		}

		localHistory.remove(newItem);
		localHistory.push(newItem);

		// Update global history
		String[] globalItems= readConditionHistory(fConditionHistoryDialogSettings);
		if (globalItems.length > 0 && newItem.equals(globalItems[0])) {
			return;
		}

		if (!fReplaceConditionInHistory) {
			String[] tempItems= new String[globalItems.length + 1];
			System.arraycopy(globalItems, 0, tempItems, 1, globalItems.length);
			globalItems= tempItems;
		} else if (globalItems.length == 0) {
			globalItems= new String[1];
		}
		fReplaceConditionInHistory= true;
		globalItems[0]= newItem;
		storeConditionHistory(globalItems, fConditionHistoryDialogSettings);*/
	}

	/**
	 * Reads the condition history from the given dialog settings.
	 *
	 * @param dialogSettings the dialog settings
	 * @return the condition history
	 */
	private static String[] readConditionHistory(IDialogSettings dialogSettings) {
		int count= 0;
		try {
			count= dialogSettings.getInt(DS_KEY_HISTORY_ENTRY_COUNT);
		} catch (NumberFormatException ex) {
			// No history yet
		}
		count= Math.min(count, MAX_HISTORY_SIZE);
		String[] conditions= new String[count];
		for (int i= 0; i < count; i++) {
			conditions[i]= dialogSettings.get(DS_KEY_HISTORY_ENTRY_PREFIX + i);
		}
		return conditions;
	}

	/**
	 * Writes the given conditions into the given dialog settings.
	 *
	 * @param conditions an array of strings containing the conditions
	 * @param dialogSettings the dialog settings
	 */
	private static void storeConditionHistory(String[] conditions, IDialogSettings dialogSettings) {
		int length= Math.min(conditions.length, MAX_HISTORY_SIZE);
		int count= 0;
		outer: for (int i= 0; i < length; i++) {
			for (int j= 0; j < i; j++) {
				if (conditions[i].equals(conditions[j])) {
					break outer;
				}
			}
			dialogSettings.put(DS_KEY_HISTORY_ENTRY_PREFIX + count, conditions[i]);
			count= count + 1;
		}
		dialogSettings.put(DS_KEY_HISTORY_ENTRY_COUNT, count);
	}

	/**
	 * Returns the label for the history separator.
	 *
	 * @return the label for the history separator
	 */
	private String getSeparatorLabel() {
		int borderWidth= fConditionHistory.computeTrim(0, 0, 0, 0).width;
		Rectangle rect= fConditionHistory.getBounds();
		int width= rect.width - borderWidth;

		GC gc= new GC(fConditionHistory);
		gc.setFont(fConditionHistory.getFont());

		int fSeparatorWidth= gc.getAdvanceWidth('-');
		String separatorLabel= PropertyPageMessages.JavaBreakpointConditionEditor_historySeparator;
		int fMessageLength= gc.textExtent(separatorLabel).x;

		gc.dispose();

		StringBuffer dashes= new StringBuffer();
		int chars= (((width - fMessageLength) / fSeparatorWidth) / 2) - 2;
		for (int i= 0; i < chars; i++) {
			dashes.append('-');
		}

		StringBuffer result= new StringBuffer();
		result.append(dashes);
		result.append(" " + separatorLabel + " "); //$NON-NLS-1$//$NON-NLS-2$
		result.append(dashes);
		return result.toString().trim();
	}

	private void registerViewerUndoRedoActions() {
		if (!fViewer.getTextWidget().isFocusControl()) {
			return;
		}

		disposeViewerUndoRedoActions();
		IUndoContext undoContext= getUndoContext();
		if (undoContext != null) {
			fViewerUndoAction= new UndoActionHandler(fBreakpointsViewSite, getUndoContext());
			PlatformUI.getWorkbench().getHelpSystem().setHelp(fViewerUndoAction, IAbstractTextEditorHelpContextIds.UNDO_ACTION);
			fViewerUndoAction.setActionDefinitionId(IWorkbenchCommandConstants.EDIT_UNDO);

			fViewerRedoAction= new RedoActionHandler(fBreakpointsViewSite, getUndoContext());
			PlatformUI.getWorkbench().getHelpSystem().setHelp(fViewerRedoAction, IAbstractTextEditorHelpContextIds.REDO_ACTION);
			fViewerRedoAction.setActionDefinitionId(IWorkbenchCommandConstants.EDIT_REDO);
		}
		fBreakpointsViewSite.getActionBars().setGlobalActionHandler(ITextEditorActionConstants.UNDO, fViewerUndoAction);
		fBreakpointsViewSite.getActionBars().setGlobalActionHandler(ITextEditorActionConstants.REDO, fViewerRedoAction);
		fBreakpointsViewSite.getActionBars().updateActionBars();
	}

	/**
	 * Returns this editor's viewer's undo manager undo context.
	 *
	 * @return the undo context or <code>null</code> if not available
	 * @since 3.1
	 */
	private IUndoContext getUndoContext() {
		IUndoManager undoManager= ((ITextViewerExtension6)fViewer).getUndoManager();
		if (undoManager instanceof IUndoManagerExtension) {
			return ((IUndoManagerExtension)undoManager).getUndoContext();
		}
		return null;
	}

	private void checkIfUsedInBreakpointsView() {
		if (fBreakpointsViewSite != null) {
			return;
		}

		IWorkbenchWindow activeWorkbenchWindow= PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (activeWorkbenchWindow != null && activeWorkbenchWindow.getActivePage() != null && activeWorkbenchWindow.getActivePage().getActivePart() != null) {
			IWorkbenchPartSite site= activeWorkbenchWindow.getActivePage().getActivePart().getSite();
			if ("org.eclipse.debug.ui.BreakpointView".equals(site.getId())) { //$NON-NLS-1$
				fBreakpointsViewSite= (IViewSite)site;
				fViewUndoAction= fBreakpointsViewSite.getActionBars().getGlobalActionHandler(ITextEditorActionConstants.UNDO);
				fViewRedoAction= fBreakpointsViewSite.getActionBars().getGlobalActionHandler(ITextEditorActionConstants.REDO);
			}
		}
	}

}

