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
package com.codenvy.ide.ext.datasource.client.newdatasource;

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
import static com.codenvy.ide.api.notification.Notification.Type.ERROR;

import com.codenvy.ide.api.notification.Notification;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.ui.action.Action;
import com.codenvy.ide.api.ui.action.ActionEvent;
import com.codenvy.ide.api.ui.wizard.DefaultWizard;
import com.codenvy.ide.api.ui.wizard.WizardDialog;
import com.codenvy.ide.api.ui.wizard.WizardDialogFactory;
import com.google.inject.Inject;

public class NewDatasourceAction extends Action {

    protected NotificationManager notificationManager;
    protected WizardDialogFactory wizardDialogFactory;
    protected DefaultWizard       wizard;

    @Inject
    public NewDatasourceAction(NotificationManager notificationManager,
                               WizardDialogFactory wizardDialogFactory,
                               @NewDatasourceWizardQualifier DefaultWizard wizard) {
        super("New Datasource Connection");
        this.notificationManager = notificationManager;
        this.wizardDialogFactory = wizardDialogFactory;
        this.wizard = wizard;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            WizardDialog wizardDialog = wizardDialogFactory.create(wizard);
            wizardDialog.show();
        } catch (Exception e1) {
            String errorMassage = e1.getMessage() != null ? e1.getMessage()
                : "An error occured while creating the datasource connection";
            Notification notification = new Notification(errorMassage, ERROR);
            notificationManager.showNotification(notification);
        }
    }

}
