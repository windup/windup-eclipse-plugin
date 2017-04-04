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
package org.jboss.tools.windup.model.domain;

/**
 * Eclipse marker IDs for Windup.
 */
public interface WindupMarker {
	/**
	 * The id for windup classification markers
	 */
	static final String WINDUP_CLASSIFICATION_MARKER_ID = "org.jboss.tools.windup.core.classificationMarker"; //$NON-NLS-1$
	/**
	 * The id for windup hint markers
	 */
	static final String WINDUP_HINT_MARKER_ID = "org.jboss.tools.windup.core.hintMarker"; //$NON-NLS-1$
	
	static final String WINDUP_QUICKFIX_ID = "org.jboss.tools.windup.core.quickfixMarker"; //$NON-NLS-1$

	static final String CONFIGURATION_ID = "CONFIGURATION_ID"; //$NON-NLS-1$
	static final String URI_ID = "URI_ID"; //$NON-NLS-1$
	static final String ELEMENT_ID = "ELEMENT_ID"; //$NON-NLS-1$
	
	static final String TITLE = "TITLE_ID";//$NON-NLS-1$
	static final String HINT = "HINT_ID"; //$NON-NLS-1$
	static final String SEVERITY = "SEVERITY_ID"; //$NON-NLS-1$
	static final String EFFORT = "EFFORT_ID"; //$NON-NLS-1$
	static final String RULE_ID = "RULE_ID"; //$NON-NLS-1$
	static final String SOURCE_SNIPPET = "SOURCE_ID"; //$NON-NLS-1$
	static final String LENGTH = "LENGTH_ID";//$NON-NLS-1$
	 
	static final String CLASSIFICATION = "CLASSIFICATION_ID"; //$NON-NLS-1$
	static final String DESCRIPTION = "DESCRIPTION_ID"; //$NON-NLS-1$
	
}
