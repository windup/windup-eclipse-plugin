package org.jboss.tools.windup.ui.internal.launch;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.jboss.tools.windup.ui.internal.launch.OptionTypeControls.OptionTypeControl;
import org.jboss.windup.bootstrap.help.Help;
import org.jboss.windup.bootstrap.help.OptionDescription;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Factory for creating UI controls mapped to help options.
 */
public class OptionsWidgetManager {
	
	private final Help help;
	private Map<String, OptionTypeControl> optionTypeControls = Maps.newHashMap();
	private final Runnable optionChangedCallback;
	
	public OptionsWidgetManager(Help help, Composite parent, Runnable optionChangedCallback) {
		this.help = help;
		this.optionChangedCallback = optionChangedCallback;
		build(parent);
	}
	
	private void build(Composite parent) {
		for (OptionDescription option : help.getOptions()) {
			create(option, parent);
		}
	}
	
	private OptionTypeControl create(OptionDescription option, Composite parent) {
		if (!optionTypeControls.containsKey(option)) {
			OptionTypeControl control = createControl(option, parent);
			optionTypeControls.put(option.getName(), control);
		}
		return optionTypeControls.get(option);
	}
	
	private OptionTypeControl createControl(OptionDescription option, Composite parent) {
		OptionTypeControl optionTypeControl = OptionTypeControls.createControl(option, optionChangedCallback);
		optionTypeControl.createControls(parent);
		return optionTypeControl; 
	}
	
	public List<String> getOptions() {
		List<String> options = Lists.newArrayList();
    	 for (OptionDescription description : help.getOptions()) {
    		 options.add(description.getName());
    	 }
    	 Collections.sort(options);
		return options;
	}
	
	public OptionTypeControl findOptionTypeControl(String name) {
		return optionTypeControls.get(name);
	}
}
