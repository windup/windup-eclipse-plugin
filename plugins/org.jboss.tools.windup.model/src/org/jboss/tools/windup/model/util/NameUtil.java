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
package org.jboss.tools.windup.model.util;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.jboss.tools.windup.windup.NamedElement;
import org.jboss.tools.windup.windup.WindupModel;
import org.jboss.tools.windup.windup.WindupPackage;

import com.google.common.collect.Lists;

/**
 * Utility for {@link NamedElement}s.
 */
public class NameUtil {
	
	private static final String CONFIGURATION_ELEMENT_PREFIX = "Run_configuration";
	
    private static Pattern namePattern = Pattern.compile("([a-zA-Z_]*)([0-9]+)");

	/**
	 * @return a unique configuration name.
	 */
	public static String generateUniqueConfigurationElementName(WindupModel model) {
        return getUniqueNameForEObject(model, CONFIGURATION_ELEMENT_PREFIX,
                WindupPackage.eINSTANCE.getWindupModel_ConfigurationElements());
    }
	
	/**
	 * @return a unique name for the type on the specified object.
	 */
	public static String getUniqueNameForEObject(EObject model, String prefix, EReference ref) {
		Set<String> existinNames = getAllExistingNames(model, ref);
        String nextName = generateName(prefix, existinNames);
        return nextName;
    }
	
	/**
	 * @return the set of names contained in the reference of the specified object.
	 */
	private static Set<String> getAllExistingNames(EObject model, EReference ref) {
        List<NamedElement> candidates = getAllNamedObjects(model, ref);
        Set<String> nameSet = candidates.stream().map(e -> e.getName()).collect(Collectors.toSet());
        return nameSet;
    }
	
	/**
	 * @return the actual list of named elements the object references.
	 */
	@SuppressWarnings("unchecked")
	private static List<NamedElement> getAllNamedObjects(EObject model, EReference ref) {
		Object obj = model.eGet(ref);
		return obj instanceof List ? Lists.newArrayList((List<NamedElement>)obj) : Lists.newArrayList();
	}
	
	public static String generateName(String candidate, Set<String> names) {
		Matcher matcher = namePattern.matcher(candidate);
		int index = 0; 
		if (matcher.matches()) {
			candidate = matcher.group(1);
			index = Integer.parseInt(matcher.group(2)) + 1;
		}
		return getNextUniqueName(names, index, candidate);
	}
	
	private static String getNextUniqueName(Set<String> names, int index, String prefixStr) {
        String newName = "";
        boolean done = false;
        while (!done) {
            newName = String.format("%s%d", prefixStr, index++);
        	if (!names.contains(newName)) {
        		done = true;
        	}
        }
        return newName;
    }
}
