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
package org.jboss.tools.windup.ui.internal.editor;

import org.eclipse.e4.core.di.annotations.Execute;

/**
 * Displays Windup help for a given context. 
 */
public class HelpHandler {
    @Execute
    public void execute() {
		//PlatformUI.getWorkbench().getHelpSystem().displayHelp(helpContextID);
    }	
}
