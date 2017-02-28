package org.jboss.tools.windup.ui.internal.rules;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.jboss.tools.windup.ui.internal.Messages;
import org.jboss.tools.windup.windup.CustomRuleProvider;

public class NewXmlRuleHandler extends Action {
	
	private CustomRuleProvider provider;
	
	public void setProvider(CustomRuleProvider provider) {
		this.provider = provider;
	}
	
	@Override
	public String getText() {
		return Messages.newXmlRule;
	}
	
	@Override
	public ImageDescriptor getImageDescriptor() {
		return WindupUIPlugin.getImageDescriptor(WindupUIPlugin.IMG_NEW_XML_RULE);
	}
	
	@Override
	public void run() {
		
	}
}
