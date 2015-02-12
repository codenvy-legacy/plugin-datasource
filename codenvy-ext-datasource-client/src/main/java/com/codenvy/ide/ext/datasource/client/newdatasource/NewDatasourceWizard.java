/*******************************************************************************
 * Copyright (c) 2012-2015 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.ide.ext.datasource.client.newdatasource;

import com.codenvy.api.user.shared.dto.ProfileDescriptor;
import com.codenvy.ide.api.notification.Notification;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.wizard.AbstractWizard;
import com.codenvy.ide.ext.datasource.client.events.DatasourceListChangeEvent;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.AbstractNewDatasourceConnectorPage;
import com.codenvy.ide.ext.datasource.client.store.DatasourceManager;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.util.loging.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.event.shared.EventBus;

import javax.annotation.Nonnull;

@Singleton
public class NewDatasourceWizard extends AbstractWizard<DatabaseConfigurationDTO> {
    public static final String DATASOURCE_NAME_KEY = "DatasourceName";

    private final NotificationManager notificationManager;
    private final DatasourceManager   datasourceManager;
    private final EventBus            eventBus;

    @Inject
    public NewDatasourceWizard(@Assisted DatabaseConfigurationDTO dataObject,
                               NotificationManager notificationManager,
                               DatasourceManager datasourceManager,
                               EventBus eventBus) {
        super(dataObject);
        this.notificationManager = notificationManager;
        this.datasourceManager = datasourceManager;
        this.eventBus = eventBus;
    }

    @Override
    public void complete(@Nonnull final CompleteCallback callback) {
        Log.debug(AbstractNewDatasourceConnectorPage.class, "Adding datasource with id " + dataObject.getDatasourceId());
        datasourceManager.add(dataObject);

        Log.debug(AbstractNewDatasourceConnectorPage.class, "Persisting datasources...");
        final Notification requestNotification = new Notification("Persisting datasources...",
                                                                  Notification.Status.PROGRESS);
        datasourceManager.persist(new AsyncCallback<ProfileDescriptor>() {

            @Override
            public void onSuccess(ProfileDescriptor result) {
                Log.debug(AbstractNewDatasourceConnectorPage.class, "Datasources persisted.");
                requestNotification.setMessage("Datasources saved");
                requestNotification.setStatus(Notification.Status.FINISHED);
                callback.onCompleted();
            }

            @Override
            public void onFailure(Throwable caught) {
                Log.error(AbstractNewDatasourceConnectorPage.class, "Failed to persist datasources.");
                requestNotification.setStatus(Notification.Status.FINISHED);
                notificationManager.showNotification(new Notification("Failed to persist datasources", Notification.Type.ERROR));
                callback.onFailure(caught);

            }
        });

        eventBus.fireEvent(new DatasourceListChangeEvent());
    }
}
