package com.codenvy.ide.ext.datasource.client.service;

import com.codenvy.ide.api.notification.Notification;
import com.codenvy.ide.api.notification.Notification.Type;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.ext.datasource.client.DatasourceClientService;
import com.codenvy.ide.ext.datasource.client.DatasourceClientServiceImpl;
import com.codenvy.ide.ext.datasource.client.selection.DatabaseInfoErrorEvent;
import com.codenvy.ide.ext.datasource.client.selection.DatabaseInfoReceivedEvent;
import com.codenvy.ide.ext.datasource.client.store.DatabaseInfoStore;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseDTO;
import com.codenvy.ide.rest.AsyncRequestCallback;
import com.codenvy.ide.rest.StringUnmarshaller;
import com.codenvy.ide.util.loging.Log;
import com.google.gwt.http.client.RequestException;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class FetchMetadataServiceImpl implements FetchMetadataService {

    private final DtoFactory                    dtoFactory;
    private final NotificationManager           notificationManager;
    private final DatasourceClientService       datasourceClientService;
    private final EventBus                      eventBus;
    private final MetadataNotificationConstants notificationConstants;
    private final DatabaseInfoStore             databaseInfoStore;

    @Inject
    protected FetchMetadataServiceImpl(final DtoFactory dtoFactory,
                                       final NotificationManager notificationManager,
                                       final EventBus eventBus,
                                       final DatasourceClientService datasourceClientService,
                                       final MetadataNotificationConstants notificationConstants,
                                       final DatabaseInfoStore databaseInfoStore) {
        this.dtoFactory = dtoFactory;
        this.notificationManager = notificationManager;
        this.datasourceClientService = datasourceClientService;
        this.eventBus = eventBus;
        this.notificationConstants = notificationConstants;
        this.databaseInfoStore = databaseInfoStore;
    }

    @Override
    public void fetchDatabaseInfo(final DatabaseConfigurationDTO configuration) {

        final Notification fetchDatabaseNotification = new Notification(notificationConstants.notificationFetchStart(),
                                                                        Notification.Status.PROGRESS);
        notificationManager.showNotification(fetchDatabaseNotification);

        try {
            this.datasourceClientService.fetchDatabaseInfo(configuration, new AsyncRequestCallback<String>(new StringUnmarshaller()) {

                @Override
                protected void onSuccess(final String result) {
                    Log.info(DatasourceClientServiceImpl.class, "Database metadata fetch success");
                    DatabaseDTO database = dtoFactory.createDtoFromJson(result,
                                                                        DatabaseDTO.class);
                    fetchDatabaseNotification.setMessage(notificationConstants.notificationFetchSuccess());
                    fetchDatabaseNotification.setStatus(Notification.Status.FINISHED);

                    databaseInfoStore.setDatabaseInfo(configuration.getDatasourceId(), database);
                    eventBus.fireEvent(new DatabaseInfoReceivedEvent(configuration.getDatasourceId()));
                }

                @Override
                protected void onFailure(final Throwable e) {
                    Log.error(DatasourceClientServiceImpl.class, "Database metadata fetch failed: " + e.getMessage());
                    fetchDatabaseNotification.setStatus(Notification.Status.FINISHED);
                    notificationManager.showNotification(new Notification(notificationConstants.notificationFetchFailure(),
                                                                          Type.ERROR));

                    databaseInfoStore.setDatabaseInfo(configuration.getDatasourceId(), null);
                    // clean up current database
                    eventBus.fireEvent(new DatabaseInfoErrorEvent(configuration.getDatasourceId()));

                }
            });
        } catch (final RequestException e) {
            Log.error(DatasourceClientServiceImpl.class, "Exception while fetching database metadata: " + e.getMessage());
            notificationManager.showNotification(new Notification(notificationConstants.notificationFetchFailure(),
                                                                  Type.ERROR));

            databaseInfoStore.setDatabaseInfo(configuration.getDatasourceId(), null);
            // clean up current database
            this.eventBus.fireEvent(new DatabaseInfoErrorEvent(configuration.getDatasourceId()));

        }


    }

}
