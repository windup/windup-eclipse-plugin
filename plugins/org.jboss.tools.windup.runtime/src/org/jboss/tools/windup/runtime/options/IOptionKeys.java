/*******************************************************************************
 * Copyright (c) 2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.runtime.options;

public interface IOptionKeys {
	public static final String sourceModeOption = "sourceMode";
	public static final String 	skipReportsRenderingOption="skipReports";
	public static final String sourceOption = "source";
	public static final String targetOption = "target";
	public static final String inputOption = "input";
	public static final String outputOption = "output";
	public static final String userRulesDirectoryOption = "userRulesDirectory";
	public static final String scanPackagesOption = "packages";
	public static final String userIgnorePathOption = "userIgnorePath";
}
