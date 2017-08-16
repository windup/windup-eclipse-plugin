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
package org.jboss.tools.windup.ui.tests;

import org.eclipse.jdt.core.dom.Annotation;
import org.jboss.tools.windup.ui.internal.rules.delegate.AnnotationUtil;
import org.jboss.tools.windup.ui.internal.rules.delegate.SnippetAnnotationVisitor;
import org.junit.Assert;
import org.junit.Test;

public class ParseAnnotationTests extends WindupUiTest {
	
	private static final String MARKER_ANNOTATION = "@MyAnnotation";

	@Test
	public void testParseMarkerAnnotation() {
		Annotation annotation = AnnotationUtil.getAnnotationElement(MARKER_ANNOTATION);

	}
}

