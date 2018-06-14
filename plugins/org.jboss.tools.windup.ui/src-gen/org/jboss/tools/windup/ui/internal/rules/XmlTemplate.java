/**
 * Copyright (c) 2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 */
package org.jboss.tools.windup.ui.internal.rules;

import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class XmlTemplate {
  public CharSequence generateQuickstartTemplateContent(final String rulesetId) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<?xml version=\"1.0\"?>");
    _builder.newLine();
    _builder.append("<ruleset id=\"");
    _builder.append(rulesetId);
    _builder.append("\" xmlns=\"http://windup.jboss.org/schema/jboss-ruleset\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("xsi:schemaLocation=\"http://windup.jboss.org/schema/jboss-ruleset http://windup.jboss.org/schema/jboss-ruleset/windup-jboss-ruleset.xsd\">");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<metadata>");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<description>");
    _builder.newLine();
    _builder.append("            ");
    _builder.append("This is a description of rules. This is a template for new rulesets. Change this.");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("</description>");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<!-- version ranges applied to from and to technologies -->");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dependencies>");
    _builder.newLine();
    _builder.append("            ");
    _builder.append("<addon id=\"org.jboss.windup.rules,2.4.0.Final\"/>");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("</dependencies>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</metadata>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<rules>");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<rule id=\"");
    _builder.append(rulesetId, "        ");
    _builder.append("-00001\">");
    _builder.newLineIfNotEmpty();
    _builder.append("            ");
    _builder.append("<!-- rule condition, when it could be fired -->");
    _builder.newLine();
    _builder.append("            ");
    _builder.append("<when>");
    _builder.newLine();
    _builder.append("            ");
    _builder.newLine();
    _builder.append("            ");
    _builder.append("</when>");
    _builder.newLine();
    _builder.append("            ");
    _builder.append("<!-- rule operation, what to do if it is fired -->");
    _builder.newLine();
    _builder.append("            ");
    _builder.append("<perform>");
    _builder.newLine();
    _builder.append("            ");
    _builder.newLine();
    _builder.append("            ");
    _builder.append("</perform>");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("</rule>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</rules>");
    _builder.newLine();
    _builder.append("</ruleset>");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence generateTemplateStubContent(final String rulesetId) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<?xml version=\"1.0\"?>");
    _builder.newLine();
    _builder.append("<ruleset id=\"");
    _builder.append(rulesetId);
    _builder.append("\" xmlns=\"http://windup.jboss.org/schema/jboss-ruleset\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
    _builder.newLineIfNotEmpty();
    _builder.append("    ");
    _builder.append("xsi:schemaLocation=\"http://windup.jboss.org/schema/jboss-ruleset http://windup.jboss.org/schema/jboss-ruleset/windup-jboss-ruleset.xsd\">");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<metadata>");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<description>");
    _builder.newLine();
    _builder.append("            ");
    _builder.append("This is a description of rules. This is a template for new rulesets. Change this.");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("</description>");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<!-- version ranges applied to from and to technologies -->");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("<dependencies>");
    _builder.newLine();
    _builder.append("             ");
    _builder.append("<addon id=\"org.jboss.windup.rules,2.4.0.Final\"/>");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("</dependencies>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</metadata>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("<rules>");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("</rules>");
    _builder.newLine();
    _builder.append("</ruleset>");
    _builder.newLine();
    return _builder;
  }
}
