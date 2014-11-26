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
package com.codenvy.ide.ext.datasource.client.newdatasource;

import static com.codenvy.ide.api.notification.Notification.Type.ERROR;

import javax.validation.constraints.NotNull;

import com.codenvy.api.analytics.logger.AnalyticsEventLogger;
import com.codenvy.ide.api.action.Action;
import com.codenvy.ide.api.action.ActionEvent;
import com.codenvy.ide.api.app.AppContext;
import com.codenvy.ide.api.app.CurrentProject;
import com.codenvy.ide.api.notification.Notification;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.ext.datasource.client.DatasourceUiResources;
import com.codenvy.ide.ext.datasource.client.newdatasource.presenter.NewDatasourceWizardPresenter;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class NewDatasourceWizardAction extends Action {

    /** The {@link NotificationManager} used to show start, completion or error messages to the user. */
    protected NotificationManager notificationManager;

    private final NewDatasourceWizardPresenter wizard;

    private final AnalyticsEventLogger eventLogger;

    /** The messages interface. */
    private final NewDatasourceWizardMessages messages;

    private final AppContext appContext;

    @Inject
    public NewDatasourceWizardAction(@NotNull final DatasourceUiResources resources,
                                     @NotNull NewDatasourceWizardPresenter wizard,
                                     @NotNull final NotificationManager notificationManager,
                                     @NotNull final NewDatasourceWizardMessages messages,
                                     AnalyticsEventLogger eventLogger,
                                     AppContext appContext) {
        super(messages.newDatasourceMenuText(), messages.newDatasourceMenuDescription(), null,
              resources.newDatasourceMenuIcon());
        this.wizard = wizard;
        this.messages = messages;
        this.notificationManager = notificationManager;
        this.eventLogger = eventLogger;
        this.appContext = appContext;
    }

    @Override
    public void update(ActionEvent e) {
        CurrentProject currentProject = appContext.getCurrentProject();
        if (currentProject != null && currentProject.isReadOnly()) {
            e.getPresentation().setEnabledAndVisible(false);
        } else {
            e.getPresentation().setEnabledAndVisible(currentProject != null);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        actionPerformed();
    }

    /**
     * Reaction to action activation.
     */
    public void actionPerformed() {
        try {
            eventLogger.log(this);
            wizard.show();
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
