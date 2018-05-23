/**
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 */
package org.jboss.tools.windup.ui.internal.rules;

import java.lang.reflect.InvocationTargetException;
import javax.inject.Inject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.jboss.tools.windup.model.domain.ModelService;

@Creatable
@SuppressWarnings("all")
public class CreateRulesetOperation extends WorkspaceModifyOperation {
  @Inject
  private ModelService modelService;
  
  private String locationURI;
  
  public String init(final String locationURI) {
    return this.locationURI = locationURI;
  }
  
  @Override
  protected void execute(final IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method addRulesetRepository(String, String) is undefined for the type ModelService");
  }
}
