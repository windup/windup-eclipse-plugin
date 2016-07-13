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

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

/**
 * Represents a {@link Command} that can be executed on a {@link CommandStack} 
 * that returns a result.
 * 
 * @param <T> the return value type
 */
public class CommandWithResult<T> extends RecordingCommand {

    private T result;
    
    public CommandWithResult(TransactionalEditingDomain domain) {
        super(domain);
    }

    public T getResultObject() {
        return result;
    }
    
    protected void setResultObject(T obj) {
        result = obj;
    }
    
    @Override
    protected void doExecute() {
    }
}