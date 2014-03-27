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
package com.codenvy.ide.ext.datasource.client.explorer;

import java.util.Collection;

import javax.validation.constraints.NotNull;

import com.codenvy.ide.api.notification.Notification;
import com.codenvy.ide.api.notification.Notification.Type;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.parts.base.BasePresenter;
import com.codenvy.ide.api.selection.Selection;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.ext.datasource.client.DatabaseInfoStore;
import com.codenvy.ide.ext.datasource.client.DatasourceClientService;
import com.codenvy.ide.ext.datasource.client.DatasourceManager;
import com.codenvy.ide.ext.datasource.client.MetadataNotificationConstants;
import com.codenvy.ide.ext.datasource.client.events.DatasourceCreatedEvent;
import com.codenvy.ide.ext.datasource.client.events.DatasourceCreatedHandler;
import com.codenvy.ide.ext.datasource.client.properties.DataEntityPropertiesPresenter;
import com.codenvy.ide.ext.datasource.client.selection.DatabaseEntitySelectionEvent;
import com.codenvy.ide.ext.datasource.client.selection.DatabaseInfoReceivedEvent;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseMetadataEntityDTO;
import com.codenvy.ide.rest.AsyncRequestCallback;
import com.codenvy.ide.rest.StringUnmarshaller;
import com.codenvy.ide.util.loging.Log;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Project Explorer display Project Model in a dedicated Part (view).
 * 
 * @author <a href="mailto:nzamosenchuk@exoplatform.com">Nikolay Zamosenchuk</a>
 */
@Singleton
public class DatasourceExplorerPartPresenter extends BasePresenter implements
                                                                  DatasourceExplorerView.ActionDelegate,
                                                                  DatasourceExplorerPart,
                                                                  DatasourceCreatedHandler {
    private final DatasourceExplorerView        view;
    private final EventBus                      eventBus;
    private final DatasourceClientService       service;
    private final DtoFactory                    dtoFactory;
    private final NotificationManager           notificationManager;
    private final DatasourceManager             datasourceManager;
    private final DataEntityPropertiesPresenter propertiesPresenter;
    private final DatasourceExplorerConstants   constants;
    private final MetadataNotificationConstants notificationConstants;
    private final DatabaseInfoStore             databaseInfoStore;

    /**
     * Instantiates the ProjectExplorer Presenter
     * 
     * @param view
     * @param eventBus
     * @param resources
     * @param resourceProvider
     * @param contextMenuPresenter
     */
    @Inject
    public DatasourceExplorerPartPresenter(@NotNull final DatasourceExplorerView view,
                                           @NotNull final EventBus eventBus,
                                           @NotNull final DatasourceClientService service,
                                           @NotNull final DtoFactory dtoFactory,
                                           @NotNull final NotificationManager notificationManager,
                                           @NotNull final DatasourceManager datasourceManager,
                                           @NotNull final DataEntityPropertiesPresenter propertiesPresenter,
                                           @NotNull final DatasourceExplorerConstants constants,
                                           @NotNull final MetadataNotificationConstants notificationConstants,
                                           @NotNull final DatabaseInfoStore databaseInfoStore) {
        this.view = view;
        this.eventBus = eventBus;
        this.service = service;
        this.dtoFactory = dtoFactory;
        this.notificationManager = notificationManager;
        this.datasourceManager = datasourceManager;
        this.propertiesPresenter = propertiesPresenter;
        this.constants = constants;
        this.notificationConstants = notificationConstants;
        this.databaseInfoStore = databaseInfoStore;

        this.view.setTitle(constants.datasourceExplorerTitle());
        bind();

        // register for datasource creation events
        this.eventBus.addHandler(DatasourceCreatedEvent.getType(), this);
    }

    /** {@inheritDoc} */
    @Override
    public void go(AcceptsOneWidget container) {
        container.setWidget(view);

        // fill the datasources list (deferred until insertion)
        setupDatasourceList();
        propertiesPresenter.go(view.getPropertiesDisplayContainer());
    }

    /** Adds behavior to view components */
    protected void bind() {
        view.setDelegate(this);
    }

    /** {@inheritDoc} */
    @Override
    public String getTitle() {
        return constants.datasourceExplorerTitle();
    }

    /** {@inheritDoc} */
    @Override
    public ImageResource getTitleImage() {
        return null;// resources.projectExplorer();
    }

    /** {@inheritDoc} */
    @Override
    public String getTitleToolTip() {
        return "";
    }

    @Override
    public void onDatabaseMetadataEntitySelected(@NotNull DatabaseMetadataEntityDTO dbMetadataEntity) {
        Log.info(DatasourceExplorerPartPresenter.class, "Database entity selected : " + dbMetadataEntity);
        setSelection(new Selection<DatabaseMetadataEntityDTO>(dbMetadataEntity));
        eventBus.fireEvent(new DatabaseEntitySelectionEvent(dbMetadataEntity));
    }

    @Override
    public void onDatabaseMetadataEntityAction(@NotNull DatabaseMetadataEntityDTO dbMetadataEntity) {
        // do nothing ATM
    }

    /** {@inheritDoc} */
    @Override
    public void onContextMenu(int mouseX, int mouseY) {
        // contextMenuPresenter.show(mouseX, mouseY);
    }

    @Override
    public void onClickExploreButton(final String datasourceId) {
        loadDatasource(datasourceId);
    }

    protected void loadDatasource(final String datasourceId) {
        try {
            DatabaseConfigurationDTO datasourceObject = this.datasourceManager.getByName(datasourceId);
            if (datasourceId == null || datasourceId.isEmpty()) {
                view.setItems(null);
                eventBus.fireEvent(new DatabaseInfoReceivedEvent(null));
                return;
            }

            final Notification fetchDatabaseNotification = new Notification(notificationConstants.notificationFetchStart(),
                                                                            Notification.Status.PROGRESS);
            notificationManager.showNotification(fetchDatabaseNotification);
            service.fetchDatabaseInfo(datasourceObject,
                                      new AsyncRequestCallback<String>(new StringUnmarshaller()) {
                                          @Override
                                          protected void onSuccess(String result) {
                                              DatabaseDTO database = dtoFactory.createDtoFromJson(result,
                                                                                                  DatabaseDTO.class);
                                              fetchDatabaseNotification.setMessage(notificationConstants.notificationFetchSuccess());
                                              fetchDatabaseNotification.setStatus(Notification.Status.FINISHED);
                                              view.setItems(database);
                                              eventBus.fireEvent(new DatabaseInfoReceivedEvent(database));
                                              databaseInfoStore.setDatabaseInfo(datasourceId, database);
                                          }

                                          @Override
                                          protected void onFailure(Throwable exception) {
                                              fetchDatabaseNotification.setStatus(Notification.Status.FINISHED);
                                              notificationManager.showNotification(new Notification(
                                                                                                    notificationConstants.notificationFetchFailure(),
                                                                                                    Type.ERROR));
                                              // clean up current database
                                              eventBus.fireEvent(new DatabaseInfoReceivedEvent(null));
                                          }
                                      }

                   );
        } catch (RequestException e) {
            Log.error(DatasourceExplorerPartPresenter.class,
                      "Exception on database info fetch : " + e.getMessage());
            notificationManager.showNotification(new Notification(notificationConstants.notificationFetchFailure(),
                                                                  Type.ERROR));
        }
    }

    @Override
    public void onSelectedDatasourceChanged(String datasourceId) {
        DatabaseDTO dsMeta = databaseInfoStore.getDatabaseInfo(datasourceId);
        if (dsMeta != null) {
            view.setItems(dsMeta);
            eventBus.fireEvent(new DatabaseInfoReceivedEvent(dsMeta));
            return;
        }
        loadDatasource(datasourceId);
    }

    @Override
    public void onDatasourceCreated(final DatasourceCreatedEvent event) {
        setupDatasourceList();
    }

    /**
     * Fills the datasource list widget with the known datasource ids.
     */
    private void setupDatasourceList() {
        Collection<String> datasourceIds = this.datasourceManager.getNames();
        this.view.setDatasourceList(datasourceIds);
    }
}
