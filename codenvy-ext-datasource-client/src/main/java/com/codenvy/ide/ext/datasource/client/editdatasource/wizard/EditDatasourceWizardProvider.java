/*******************************************************************************
* Copyright (c) 2012-2014 Codenvy, S.A.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* Codenvy, S.A. - initial API and implementation
*******************************************************************************/
package com.codenvy.ide.ext.datasource.client.editdatasource.wizard;

import com.codenvy.ide.util.loging.Log;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class EditDatasourceWizardProvider implements Provider<EditDatasourceWizard> {
    private EditDatasourceWizardFactory wizardFactory;

    @Inject
    public EditDatasourceWizardProvider(final EditDatasourceWizardFactory wizardFactory) {
        this.wizardFactory = wizardFactory;
    }

    @Override
    public EditDatasourceWizard get() {
        Log.debug(EditDatasourceWizardProvider.class, "Create new Edit datasource wizard.");
        return wizardFactory.create("Edit Datasource Wizard");
    }
}
