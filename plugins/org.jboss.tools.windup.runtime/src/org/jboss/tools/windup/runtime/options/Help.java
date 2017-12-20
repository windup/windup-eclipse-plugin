package org.jboss.tools.windup.runtime.options;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * @author <a href="mailto:jesse.sightler@gmail.com">Jesse Sightler</a>
 */
public class Help
{
    private static final String HELP = "help";
    private static final String OPTION = "option";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    public static final String TYPE = "type";
    public static final String AVAILABLE_OPTIONS = "available-options";
    public static final String AVAILABLE_OPTION = "option";
    public static final String UI_TYPE = "ui-type";
    private static final String REQUIRED = "required";

    private List<OptionDescription> options = new ArrayList<>();

    public List<OptionDescription> getOptions()
    {
        return options;
    }

    private void addOption(OptionDescription optionDescription)
    {
        this.options.add(optionDescription);
    }

    private static File getFile(File windupHome) throws IOException
    {
        Path helpDirectory = windupHome.toPath().resolve("cache").resolve("help");
        if (!Files.exists(helpDirectory))
            Files.createDirectories(helpDirectory);
        Path helpPath = helpDirectory.resolve("help.xml");
        return helpPath.toFile();
    }

    public static Help load(File windupHome)
    {
        final Help result = new Help();
        try
        {
            Document doc = new SAXReader().read(getFile(windupHome));

            Iterator optionElementIterator = doc.getRootElement().elementIterator(OPTION);
            while (optionElementIterator.hasNext())
            {
                Element optionElement = (Element) optionElementIterator.next();

                String name = optionElement.attributeValue(NAME);
                String description = optionElement.element(DESCRIPTION).getTextTrim();
                String type = optionElement.element(TYPE).getTextTrim();
                String uiType = optionElement.element(UI_TYPE).getTextTrim();
                boolean required = Boolean.valueOf(optionElement.element(REQUIRED).getTextTrim());

                List<String> availableOptions = null;

                if (optionElement.element(AVAILABLE_OPTIONS) != null)
                {
                    availableOptions = new ArrayList<>();
                    for (Element availableOption : (List<Element>)optionElement.element(AVAILABLE_OPTIONS).elements(AVAILABLE_OPTION))
                    {
                        availableOptions.add(availableOption.getTextTrim());
                    }
                }

                OptionDescription option = new OptionDescription(name, description, type, uiType, availableOptions, required);
                result.addOption(option);
            }
        }
        catch (DocumentException | IOException e)
        {
            System.err.println("WARNING: Failed to load detailed help information!");
        }
        return result;
    }
}
