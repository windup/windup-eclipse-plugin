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
package org.jboss.tools.windup.ui.internal.rules

class XmlTemplate {
    def generateQuickstartTemplateContent(String rulesetId) {
        ''' 
        <?xml version="1.0"?>
        <ruleset id="«rulesetId»" xmlns="http://windup.jboss.org/schema/jboss-ruleset" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://windup.jboss.org/schema/jboss-ruleset http://windup.jboss.org/schema/jboss-ruleset/windup-jboss-ruleset.xsd">
            <metadata>
                <description>
                    This is a description of rules. This is a template for new rulesets. Change this.
                </description>
                <!-- version ranges applied to from and to technologies -->
                <dependencies>
                </dependencies>
            </metadata>
            <rules>
                <rule id="«rulesetId»-00001">
                    <!-- rule condition, when it could be fired -->
                    <when>
                    
                    </when>
                    <!-- rule operation, what to do if it is fired -->
                    <perform>
                    
                    </perform>
                </rule>
            </rules>
        </ruleset>
        '''
    }
    
    def generateTemplateStubContent(String rulesetId) {
        ''' 
        <?xml version="1.0"?>
        <ruleset id="«rulesetId»" xmlns="http://windup.jboss.org/schema/jboss-ruleset" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://windup.jboss.org/schema/jboss-ruleset http://windup.jboss.org/schema/jboss-ruleset/windup-jboss-ruleset.xsd">
            
        </ruleset>
        '''
    }
}