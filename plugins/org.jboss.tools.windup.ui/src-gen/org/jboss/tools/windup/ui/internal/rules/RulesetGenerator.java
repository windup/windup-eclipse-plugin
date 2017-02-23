/**
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 */
package org.jboss.tools.windup.ui.internal.rules;

import javax.inject.Inject;
import org.eclipse.xtext.generator.JavaIoFileSystemAccess;
import org.jboss.tools.windup.ui.internal.rules.XmlTemplate;

@SuppressWarnings("all")
public class RulesetGenerator {
  @Inject
  private JavaIoFileSystemAccess fsa;
  
  @Inject
  private XmlTemplate xmlTemplate;
  
  public void generateXmlRulesetQuickstartTemplate(final String fileName, final String rulesetId, final String location) {
    this.fsa.setOutputPath(location);
    CharSequence content = this.xmlTemplate.generateQuickstartTemplateContent(rulesetId);
    this.fsa.generateFile(fileName, content);
  }
  
  public void generateXmlRulesetStub(final String fileName, final String rulesetId, final String location) {
    this.fsa.setOutputPath(location);
    CharSequence content = this.xmlTemplate.generateTemplateStubContent(rulesetId);
    this.fsa.generateFile(fileName, content);
  }
}
