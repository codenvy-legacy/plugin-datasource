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
package com.codenvy.ide.ext.datasource.client.newdatasource;

import static com.codenvy.ide.api.notification.Notification.Type.ERROR;

import javax.validation.constraints.NotNull;

import com.codenvy.ide.api.notification.Notification;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.ui.action.Action;
import com.codenvy.ide.api.ui.action.ActionEvent;
import com.codenvy.ide.api.ui.wizard.DefaultWizard;
import com.codenvy.ide.api.ui.wizard.WizardDialog;
import com.codenvy.ide.api.ui.wizard.WizardDialogFactory;
import com.codenvy.ide.ext.datasource.client.DatasourceUiResources;
import com.google.inject.Inject;

/**
 * {@link Action} to trigger the new datasource creation wizard.
 */
public class NewDatasourceAction extends Action {

    /** The {@link NotificationManager} used to show start, completion or error messages to the user. */
    protected NotificationManager             notificationManager;

    /** The factory to create the new datasource dialog. */
    private final WizardDialogFactory         wizardDialogFactory;

    /** The new datasource wizard template. */
    private final DefaultWizard               wizard;

    /** The messages interface. */
    private final NewDatasourceWizardMessages messages;

    @Inject
    public NewDatasourceAction(@NotNull final NotificationManager notificationManager,
                               @NotNull final WizardDialogFactory wizardDialogFactory,
                               @NotNull final @NewDatasourceWizardQualifier DefaultWizard wizard,
                               @NotNull final DatasourceUiResources resources,
                               @NotNull final NewDatasourceWizardMessages messages) {
        super(messages.newDatasourceMenuText(), messages.newDatasourceMenuDescription(), null,
              resources.newDatasourceMenuIcon());
        this.notificationManager = notificationManager;
        this.wizardDialogFactory = wizardDialogFactory;
        this.wizard = wizard;
        this.messages = messages;
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        actionPerformed();
    }

    /**
     * Reaction to action activation.
     */
    public void actionPerformed() {
        try {
            WizardDialog wizardDialog = wizardDialogFactory.create(wizard);
            wizardDialog.show();
        } catch (final Exception exception) {
            String errorMessage = this.messages.defaultNewDatasourceWizardErrorMessage();
            if (exception.getMessage() != null) {
                errorMessage = exception.getMessage();
            }

            final Notification notification = new Notification(errorMessage, ERROR);
            notificationManager.showNotification(notification);
        }
    }
}
