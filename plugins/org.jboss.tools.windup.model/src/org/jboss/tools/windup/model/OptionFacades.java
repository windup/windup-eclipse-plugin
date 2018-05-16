/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.model;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import org.jboss.tools.windup.model.Facades.IFacade;
import org.jboss.tools.windup.runtime.options.Help;
import org.jboss.tools.windup.runtime.options.OptionDescription;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

/**
 * Contains all the modeling associated with the generated help.xml.
 */
public class OptionFacades {
	
	public static enum Type {
		STRING, BOOLEAN, FILE, PATH
	}
	
	public static enum UiType {
		SINGLE, MANY, SELECT_MANY, FILE_OR_DIRECTORY, DIRECTORY
	}
	
	public static interface OptionTypeFacade<T> extends IFacade {
		OptionDescription getOptionDescription();
		T newInstance(String value);
		List<?> newInstance(List<String> values);
	}
	
	private static abstract class AbstractOptionTypeFacade<T> implements OptionTypeFacade<T> {
		private OptionDescription optionDescription;
		public AbstractOptionTypeFacade (OptionDescription optionDescription) {
			this.optionDescription = optionDescription;
		}
		@Override
		public OptionDescription getOptionDescription() {
			return this.optionDescription;
		}
		@Override
		public abstract T newInstance(String value);
		@Override
		public abstract List<T> newInstance(List<String> values);
	}
	
	private static class StringOptionTypeFacade extends AbstractOptionTypeFacade<String> {
		public StringOptionTypeFacade(OptionDescription optionDescription) {
			super(optionDescription);
		}
		@Override
		public String newInstance(String value) {
			return value;
		}
		@Override
		public List<String> newInstance(List<String> values) {
			return Lists.newArrayList(values);
		}
	}
	
	private static class BooleanOptionTypeFacade extends AbstractOptionTypeFacade<Boolean> {
		public BooleanOptionTypeFacade(OptionDescription optionDescription) {
			super(optionDescription);
		}
		@Override
		public Boolean newInstance(String value) {
			return new Boolean(value);
		}
		@Override
		public List<Boolean> newInstance(List<String> values) {
			List<Boolean> bools = Lists.newArrayList();
			values.stream().forEach(v -> bools.add(new Boolean(v)));
			return bools;
		}
	}
	
	private static class FileOptionTypeFacade extends AbstractOptionTypeFacade<File> {
		public FileOptionTypeFacade(OptionDescription optionDescription) {
			super(optionDescription);
		}
		@Override
		public File newInstance(String value) {
			return new File(value);
		}
		@Override
		public List<File> newInstance(List<String> values) {
			List<File> files = Lists.newArrayList();
			values.stream().forEach(v -> files.add(new File(v)));
			return files;
		}
	}
	
	private static class PathOptionTypeFacade extends AbstractOptionTypeFacade<Path> {
		public PathOptionTypeFacade(OptionDescription optionDescription) {
			super(optionDescription);
		}
		@Override
		public Path newInstance(String value) {
			File file = new File(value);
			return file.toPath();
		}
		@Override
		public List<Path> newInstance(List<String> values) {
			List<Path> paths = Lists.newArrayList();
			values.stream().forEach(v -> paths.add(new File(v).toPath()));
			return paths;
		}
	}
	
	public static OptionsFacadeManager createOptionsFacadeManager(Help help) {
		OptionsFacadeManager mgr = new OptionsFacadeManager(help);
		attachOptionTypeFacades(mgr);
		return mgr;
	}
	
	private static void attachOptionTypeFacades(OptionsFacadeManager mgr) {
		for (OptionDescription optionDescription : mgr.getOptionDescriptions()) {
			Type type = Type.valueOf(optionDescription.getType().toUpperCase());
			IFacade facade = null;
			switch (type) {
				case STRING: 	facade = new StringOptionTypeFacade(optionDescription); break;
				case BOOLEAN: 	facade = new BooleanOptionTypeFacade(optionDescription); break;
				case FILE: 		facade = new FileOptionTypeFacade(optionDescription); break;
				case PATH:		facade = new PathOptionTypeFacade(optionDescription); break;
			}
			mgr.addFacade(optionDescription, facade);
		}
	}
	
	public static class OptionsFacadeManager {
		private Multimap<OptionDescription, IFacade> facades = ArrayListMultimap.create();
		private Help help;
		
		public OptionsFacadeManager (Help help) {
			this.help = help;
		}
		
		@SuppressWarnings("unchecked")
		public <E extends IFacade> E getFacade(OptionDescription option, Class<E> type) {
			for (IFacade facade : getFacades(option)) {
				if (type.isInstance(facade)) {
					return (E)facade;
				}
			}
			return null;
		}
		
		public List<? extends IFacade> getFacades(OptionDescription option) {
			return (List<? extends IFacade>) facades.get(option);
		}
		
		public <E extends IFacade> void addFacade(OptionDescription option, E facade) {
			facades.put(option, facade);
		}
		
		public List<OptionDescription> getOptionDescriptions() {
			return help.getOptions();
		}
		
		public OptionDescription findOptionDescription(String name) {
			return facades.keySet().stream().filter(d -> d.getName().equals(name)).findFirst().get();
		}
	}
	
	public static boolean isSingleValued(OptionDescription option) {
		 UiType uiType = UiType.valueOf(option.getUiType().toUpperCase());
		return uiType == UiType.SINGLE || uiType == UiType.DIRECTORY || uiType == UiType.FILE_OR_DIRECTORY;
	}
}
