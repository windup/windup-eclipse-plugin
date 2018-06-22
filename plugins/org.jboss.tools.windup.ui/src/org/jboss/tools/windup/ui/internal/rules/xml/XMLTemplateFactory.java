/*******************************************************************************
 * Copyright (c) 2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
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
