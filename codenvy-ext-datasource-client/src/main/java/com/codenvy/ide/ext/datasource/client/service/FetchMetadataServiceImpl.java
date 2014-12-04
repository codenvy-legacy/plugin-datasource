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
package com.codenvy.ide.ext.datasource.client.service;

import java.util.Collections;

import com.codenvy.ide.api.notification.Notification;
import com.codenvy.ide.api.notification.Notification.Type;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.ext.datasource.client.DatasourceClientService;
import com.codenvy.ide.ext.datasource.client.DatasourceClientServiceImpl;
import com.codenvy.ide.ext.datasource.client.editdatasource.EditDatasourceOpenNotificationHandler;
import com.codenvy.ide.ext.datasource.client.selection.DatabaseInfoErrorEvent;
import com.codenvy.ide.ext.datasource.client.selection.DatabaseInfoReceivedEvent;
import com.codenvy.ide.ext.datasource.client.store.DatabaseInfoStore;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseDTO;
import com.codenvy.ide.ext.datasource.shared.ExploreTableType;
import com.codenvy.ide.rest.AsyncRequestCallback;
import com.codenvy.ide.rest.StringUnmarshaller;
import com.codenvy.ide.util.loging.Log;
import com.google.gwt.http.client.RequestException;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Client-side service for database metadata loading.
 *
 * @author "Mickaël Leduque"
 */
public class FetchMetadataServiceImpl implements FetchMetadataService {

    /** DTO factory used to deserialize DTOs from JSON. */
    private final DtoFactory                            dtoFactory;

    /** Notification manager used to display metadata progress, success and failure. */
    private final NotificationManager                   notificationManager;
    private final DatasourceClientService               datasourceClientService;

    /** The event bus on which metadata loading (success, failure) events are posted. */
    private final EventBus                              eventBus;

    /** The i18n constants. */
    private final MetadataNotificationConstants         notificationConstants;

    /** Where the metadata is stored. */
    private final DatabaseInfoStore                     databaseInfoStore;

    private final EditDatasourceOpenNotificationHandler editDatasourceOpenNotificationHandler;

    @Inject
    protected FetchMetadataServiceImpl(final DtoFactory dtoFactory,
                                       final NotificationManager notificationManager,
                                       final EventBus eventBus,
                                       final DatasourceClientService datasourceClientService,
                                       final MetadataNotificationConstants notificationConstants,
                                       final DatabaseInfoStore databaseInfoStore,
                                       final EditDatasourceOpenNotificationHandler editDatasourceOpenNotificationHandler) {
        this.dtoFactory = dtoFactory;
        this.notificationManager = notificationManager;
        this.datasourceClientService = datasourceClientService;
        this.eventBus = eventBus;
        this.notificationConstants = notificationConstants;
        this.databaseInfoStore = databaseInfoStore;
        this.editDatasourceOpenNotificationHandler = editDatasourceOpenNotificationHandler;
    }

    @Override
    public void fetchDatabaseInfo(final DatabaseConfigurationDTO configuration, final ExploreTableType tableCategory) {
        if (configuration == null) {
            final Notification invalidConfigNotification = new Notification(notificationConstants.invalidConfigurationNotification(),
                                                                            Notification.Type.ERROR);
            notificationManager.showNotification(invalidConfigNotification);
            return;
        }
        // check if there is a pending fetch on this datasource
        // no synchronization needed, we are in pure single-threaded code (browser)
        final String datasourceId = configuration.getDatasourceId();
        if (databaseInfoStore.isFetchPending(datasourceId)) {
            Log.debug(FetchMetadataServiceImpl.class, "Fetch pending found for this datasource ("
                                                      + datasourceId
                                                      + ") - no need to trigger another fetch.");
            return;
        }
        databaseInfoStore.setFetchPending(datasourceId);

        final Notification fetchDatabaseNotification = new Notification(notificationConstants.notificationFetchStart(),
                                                                        Notification.Status.PROGRESS);
        notificationManager.showNotification(fetchDatabaseNotification);

        try {
            datasourceClientService.fetchDatabaseInfo(configuration, tableCategory,
                                                      new AsyncRequestCallback<String>(new StringUnmarshaller()) {
                                                          @Override
                                                          protected void onSuccess(final String result) {
                                                              onSuccessFetchingDatabaseInfo(configuration, datasourceId,
                                                                                            fetchDatabaseNotification, result);
                                                          }

                                                          @Override
                                                          protected void onFailure(final Throwable e) {
                                                              onFailureFetchingDatabaseInfo(configuration, datasourceId,
                                                                                            fetchDatabaseNotification, e);
                                                          }
                                                      });
        } catch (final RequestException e) {
            onFailureFetchingDatabaseInfo(configuration, datasourceId, fetchDatabaseNotification, e);
        }
    }

    protected void onSuccessFetchingDatabaseInfo(final DatabaseConfigurationDTO configuration,
                                                 final String datasourceId,
                                                 final Notification fetchDatabaseNotification,
                                                 final String result) {
        Log.debug(DatasourceClientServiceImpl.class, "Database metadata fetch success");
        DatabaseDTO database = dtoFactory.createDtoFromJson(result, DatabaseDTO.class);
        fetchDatabaseNotification.setMessage(notificationConstants.notificationFetchSuccess());
        fetchDatabaseNotification.setStatus(Notification.Status.FINISHED);
        databaseInfoStore.setDatabaseInfo(configuration.getDatasourceId(), database);
        eventBus.fireEvent(new DatabaseInfoReceivedEvent(configuration.getDatasourceId()));
        databaseInfoStore.clearFetchPending(datasourceId);
    }

    protected void onFailureFetchingDatabaseInfo(final DatabaseConfigurationDTO configuration,
                                                 final String datasourceId,
                                                 final Notification fetchDatabaseNotification,
                                                 final Throwable e) {
        Log.error(DatasourceClientServiceImpl.class, "Database metadata fetch failed: " + e.getMessage());
        fetchDatabaseNotification.setStatus(Notification.Status.FINISHED);
        editDatasourceOpenNotificationHandler.setConfiguration(configuration);
        notificationManager.showNotification(new Notification(notificationConstants.notificationFetchFailure() + " using datasource '"
                                                              + configuration.getDatasourceId() + "' - " + e.getMessage(),
                                                              Type.ERROR, editDatasourceOpenNotificationHandler));
        databaseInfoStore.setDatabaseInfo(configuration.getDatasourceId(),
                                          dtoFactory.createDto(DatabaseDTO.class).withSchemas(Collections.EMPTY_MAP));
        eventBus.fireEvent(new DatabaseInfoErrorEvent(configuration.getDatasourceId()));
        databaseInfoStore.clearFetchPending(datasourceId);
    }

    @Override
    public void fetchDatabaseInfo(final DatabaseConfigurationDTO configuration) {
        this.fetchDatabaseInfo(configuration, ExploreTableType.STANDARD);
    }
}
