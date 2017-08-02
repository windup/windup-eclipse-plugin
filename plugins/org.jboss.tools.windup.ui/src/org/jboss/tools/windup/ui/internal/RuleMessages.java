/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal;

import org.eclipse.osgi.util.NLS;

public class RuleMessages extends NLS {

    private static final String BUNDLE_NAME = "org.jboss.tools.windup.ui.internal.rulemessages"; //$NON-NLS-1$
    
    static  {
        NLS.initializeMessages(BUNDLE_NAME, RuleMessages.class);
    }

    private RuleMessages(){}
    
    public static String javaclass_description;
    public static String javaclass_locationSectionTitle;
    public static String javaclass_locationDescription;
    
    /*
     * 	<annotation-list></annotation-list>
					<annotation-literal />
					<annotation-type></annotation-type>
     */
    
    public static String javaclass_annotation_list_sectionTitle;
    public static String javaclass_annotation_list_description;
    
    public static String javaclass_annotation_literal_sectionTitle;
    public static String javaclass_annotation_literal_description;
    
    public static String javaclass_annotation_type_sectionTitle;
    public static String javaclass_annotation_type_description;
}
