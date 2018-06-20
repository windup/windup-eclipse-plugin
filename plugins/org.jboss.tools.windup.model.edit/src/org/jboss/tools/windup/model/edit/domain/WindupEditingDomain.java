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
package org.jboss.tools.windup.model.edit.domain;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.transaction.RollbackException;
import org.eclipse.emf.transaction.TransactionalCommandStack;
import org.eclipse.emf.transaction.impl.InternalTransaction;
import org.eclipse.emf.transaction.impl.TransactionalEditingDomainImpl;

public class WindupEditingDomain extends TransactionalEditingDomainImpl {
    
	public WindupEditingDomain (AdapterFactory adapterFactory, TransactionalCommandStack stack, ResourceSet resourceSet) {
        super(adapterFactory, stack, resourceSet);
    }

    @Override
    public Object runExclusive(Runnable read) throws InterruptedException {
        return super.runExclusive(read);
    }

    @Override
    public void precommit(InternalTransaction tx) throws RollbackException {
        super.precommit(tx);
    }

    @Override
    protected void postcommit(InternalTransaction tx) {
    	super.postcommit(tx);
    }
}