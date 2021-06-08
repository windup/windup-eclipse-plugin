/*******************************************************************************
 * Copyright (c) 2021 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.rules

import javax.inject.Inject
import org.eclipse.xtext.generator.JavaIoFileSystemAccess

class RulesetGenerator {
    
    @Inject JavaIoFileSystemAccess fsa
    @Inject XmlTemplate xmlTemplate
    
    def generateXmlRulesetQuickstartTemplate(String fileName, String rulesetId, String location) {
        fsa.outputPath = location
        var content = xmlTemplate.generateQuickstartTemplateContent(rulesetId)
        fsa.generateFile(fileName, content)
    }
    
    def generateXmlRulesetStub(String fileName, String rulesetId, String location) {
        fsa.outputPath = location
        var content = xmlTemplate.generateTemplateStubContent(rulesetId)
        fsa.generateFile(fileName, content)
    }
}