/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2013] - [2014] Codenvy, S.A.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.ide.ext.datasource.action;

import static com.codenvy.ide.api.notification.Notification.Type.ERROR;

import com.codenvy.ide.api.notification.Notification;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.ui.action.Action;
import com.codenvy.ide.api.ui.action.ActionEvent;
import com.codenvy.ide.api.ui.wizard.DefaultWizard;
import com.codenvy.ide.api.ui.wizard.WizardDialog;
import com.codenvy.ide.api.ui.wizard.WizardDialogFactory;
import com.google.inject.Inject;

public class NewDSConnectionAction extends Action {

    protected NotificationManager   notificationManager;
    protected WizardDialogFactory   wizardDialogFactory;
    protected DefaultWizard wizard;

    @Inject
    public NewDSConnectionAction(NotificationManager notificationManager,
                                 WizardDialogFactory wizardDialogFactory,
                                 @NewDatasourceWizard DefaultWizard wizard) {
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
