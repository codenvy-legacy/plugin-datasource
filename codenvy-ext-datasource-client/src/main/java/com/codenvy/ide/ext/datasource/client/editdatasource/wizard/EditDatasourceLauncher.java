/*******************************************************************************
 * Copyright (c) 2012-2014 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.ide.ext.datasource.client.editdatasource.wizard;

import static com.codenvy.ide.api.notification.Notification.Type.ERROR;

import javax.validation.constraints.NotNull;

import com.codenvy.ide.api.notification.Notification;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardMessages;
import com.codenvy.ide.ext.datasource.client.newdatasource.presenter.NewDatasourceWizardPresenter;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class EditDatasourceLauncher {

    private final Provider<NewDatasourceWizardPresenter> newDatasourcePageProvider;
    private final NewDatasourceWizardMessages            messages;
    protected NotificationManager                        notificationManager;

    @Inject
    public EditDatasourceLauncher(Provider<NewDatasourceWizardPresenter> newDatasourcePageProvider,
                                  @NotNull final NotificationManager notificationManager,
                                  @NotNull final NewDatasourceWizardMessages messages) {
        this.newDatasourcePageProvider = newDatasourcePageProvider;
        this.messages = messages;
        this.notificationManager = notificationManager;
    }

    public void launch(final DatabaseConfigurationDTO datasource) {
        NewDatasourceWizardPresenter newDatasourceWizardPresenter = newDatasourcePageProvider.get();
        newDatasourceWizardPresenter.initData(datasource);
        try {
            newDatasourceWizardPresenter.show();
        } catch (final Exception exception) {
            String errorMessage = messages.defaultNewDatasourceWizardErrorMessage();
            if (exception.getMessage() != null) {
                errorMessage = exception.getMessage();
            }

            final Notification notification = new Notification(errorMessage, ERROR);
            notificationManager.showNotification(notification);
        }
    }
}
