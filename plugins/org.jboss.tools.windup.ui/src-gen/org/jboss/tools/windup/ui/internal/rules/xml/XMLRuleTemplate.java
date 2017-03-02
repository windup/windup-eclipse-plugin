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
      return _builder.toString();
    }
  }
  
  public static class StubRuleTemplate2 implements XMLRuleTemplate.Template {
    @Override
    public String getName() {
      return "stub2";
    }
    
    @Override
    public String generate() {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("<rule id=\"2\">");
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
  
  public static class StubRuleTemplate3 implements XMLRuleTemplate.Template {
    @Override
    public String getName() {
      return "stub3";
    }
    
    @Override
    public String generate() {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("<rule id=\"3\">");
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
      XMLRuleTemplate.StubRuleTemplate2 _stubRuleTemplate2 = new XMLRuleTemplate.StubRuleTemplate2();
      templates.add(_stubRuleTemplate2);
      XMLRuleTemplate.StubRuleTemplate3 _stubRuleTemplate3 = new XMLRuleTemplate.StubRuleTemplate3();
      templates.add(_stubRuleTemplate3);
      _xblockexpression = templates;
    }
    return _xblockexpression;
  }
}
