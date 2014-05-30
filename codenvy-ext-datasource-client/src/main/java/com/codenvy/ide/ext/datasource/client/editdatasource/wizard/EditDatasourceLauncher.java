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

import com.codenvy.ide.api.ui.wizard.WizardDialog;
import com.codenvy.ide.api.ui.wizard.WizardDialogFactory;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.google.inject.Inject;

public class EditDatasourceLauncher {

    private final EditDatasourceWizard editDatasourceWizard;
    private final WizardDialogFactory  wizardDialogFactory;

    @Inject
    public EditDatasourceLauncher(final @EditDatasourceWizardQualifier EditDatasourceWizard editDatasourceWizard,
                                  final WizardDialogFactory wizardDialogFactory) {
        this.editDatasourceWizard = editDatasourceWizard;
        this.wizardDialogFactory = wizardDialogFactory;
    }

    public void launch(final DatabaseConfigurationDTO datasource) {
        editDatasourceWizard.initData(datasource);
        final WizardDialog dialog = this.wizardDialogFactory.create(editDatasourceWizard);

        dialog.show();
    }
}
