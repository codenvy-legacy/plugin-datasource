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

import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.ui.wizard.DefaultWizard;
import com.codenvy.ide.api.ui.wizard.WizardPage;
import com.codenvy.ide.ext.datasource.client.newdatasource.InitializableWizardPage;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;

public class EditDatasourceWizard extends DefaultWizard {

    private DatabaseConfigurationDTO configuration;

    public EditDatasourceWizard(final NotificationManager notificationManager) {
        super(notificationManager, "Edit datasource");
    }

    public void initData(final DatabaseConfigurationDTO configuration) {
        this.configuration = configuration;
    }

    @Override
    public WizardPage flipToFirst() {
        WizardPage page = super.flipToFirst();
        if (page instanceof InitializableWizardPage) {
            ((InitializableWizardPage)page).initPage(configuration);
        }
        return page;
    }

    @Override
    public WizardPage flipToNext() {
        WizardPage page = super.flipToNext();
        if (page instanceof InitializableWizardPage) {
            ((InitializableWizardPage)page).initPage(configuration);
        }
        return page;
    }

    @Override
    public WizardPage flipToPrevious() {
        WizardPage page = super.flipToPrevious();
        if (page instanceof InitializableWizardPage) {
            ((InitializableWizardPage)page).initPage(configuration);
        }
        return page;
    }
}
