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
package org.jboss.tools.windup.ui.rules

import org.eclipse.ui.actions.WorkspaceModifyOperation
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.core.runtime.CoreException
import java.lang.reflect.InvocationTargetException
import javax.inject.Inject
import org.jboss.tools.windup.model.domain.ModelService
import org.eclipse.e4.core.di.annotations.Creatable

@Creatable
class CreateRulesetOperation extends WorkspaceModifyOperation {
    
    @Inject ModelService modelService
    var String locationURI
    var String rulesetId
    
    def init(String locationURI, String rulesetId) {
        this.locationURI = locationURI
        this.rulesetId = rulesetId;
    }
    
    override protected execute(IProgressMonitor monitor) throws CoreException, 
            InvocationTargetException, InterruptedException {
        modelService.addRulesetRepository(locationURI, rulesetId)
    }
}