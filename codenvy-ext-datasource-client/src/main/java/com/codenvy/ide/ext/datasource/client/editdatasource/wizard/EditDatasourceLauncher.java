/*
 * Copyright 2014 Codenvy, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
