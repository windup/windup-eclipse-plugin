package org.jboss.tools.windup.ui.internal.rules.xml;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class XMLRuleTemplate {
  public interface Template {
    public abstract String getName();
    
    public abstract String generate();
  }
  
  public static class StubRuleTemplate implements XMLRuleTemplate.Template {
    @Override
    public String getName() {
      return "stub";
    }
    
    @Override
    public String generate() {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("<rule id=\"1\">");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("<!-- rule condition, when it could be fired -->");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("<when>");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("</when>");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("<!-- rule operation, what to do if it is fired -->");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("<perform>");
      _builder.newLine();
      _builder.append("    ");
      _builder.append("</perform>");
      _builder.newLine();
      _builder.append("</rule>");
      _builder.newLine();
      return _builder.toString();
    }
  }
  
  public List<XMLRuleTemplate.Template> getTemplates() {
    ArrayList<XMLRuleTemplate.Template> _xblockexpression = null;
    {
      ArrayList<XMLRuleTemplate.Template> templates = CollectionLiterals.<XMLRuleTemplate.Template>newArrayList();
      XMLRuleTemplate.StubRuleTemplate _stubRuleTemplate = new XMLRuleTemplate.StubRuleTemplate();
      templates.add(_stubRuleTemplate);
      _xblockexpression = templates;
    }
    return _xblockexpression;
  }
}
