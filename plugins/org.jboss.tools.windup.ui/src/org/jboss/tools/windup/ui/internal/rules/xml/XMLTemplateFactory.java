package org.jboss.tools.windup.ui.internal.rules.xml;

import java.util.List;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.jboss.tools.windup.ui.internal.rules.xml.XMLRuleTemplate.Template;

@Creatable
public class XMLTemplateFactory {
	
	private XMLRuleTemplate ruleTemplates;
	
	public List<Template> getTemplates() {
		ruleTemplates = new XMLRuleTemplate();
		return ruleTemplates.getTemplates();
	}
}
