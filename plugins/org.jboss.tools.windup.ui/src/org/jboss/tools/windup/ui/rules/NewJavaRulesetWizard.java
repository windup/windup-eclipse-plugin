package org.jboss.tools.windup.ui.rules;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;

public class NewJavaRulesetWizard extends Wizard implements INewWizard {

	public NewJavaRulesetWizard() {
		setDefaultPageImageDescriptor(WindupUIPlugin.getImageDescriptor(WindupUIPlugin.IMG_JAVA_WIZ));
		setWindowTitle(Messages.NewJavaRuleset_title);
		setNeedsProgressMonitor(true);
	}
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

	@Override
	public boolean performFinish() {
		return false;
	}
}
