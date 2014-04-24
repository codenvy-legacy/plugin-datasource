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

import com.codenvy.ide.api.parts.base.BasePresenter;
import com.codenvy.ide.api.selection.Selection;
import com.codenvy.ide.ext.datasource.client.DatabaseInfoStore;
import com.codenvy.ide.ext.datasource.client.DatasourceManager;
import com.codenvy.ide.ext.datasource.client.events.DatasourceListChangeEvent;
import com.codenvy.ide.ext.datasource.client.events.DatasourceListChangeHandler;
import com.codenvy.ide.ext.datasource.client.properties.DataEntityPropertiesPresenter;
import com.codenvy.ide.ext.datasource.client.selection.DatabaseEntitySelectionEvent;
import com.codenvy.ide.ext.datasource.client.selection.DatabaseInfoErrorEvent;
import com.codenvy.ide.ext.datasource.client.selection.DatabaseInfoErrorHandler;
import com.codenvy.ide.ext.datasource.client.selection.DatabaseInfoReceivedEvent;
import com.codenvy.ide.ext.datasource.client.selection.DatabaseInfoReceivedHandler;
import com.codenvy.ide.ext.datasource.client.service.FetchMetadataService;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseMetadataEntityDTO;
import com.codenvy.ide.util.loging.Log;
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
                                                                  DatasourceListChangeHandler,
                                                                  DatabaseInfoReceivedHandler,
                                                                  DatabaseInfoErrorHandler {
    private final DatasourceExplorerView        view;
    private final EventBus                      eventBus;
    private final FetchMetadataService          service;
    private final DatasourceManager             datasourceManager;
    private final DataEntityPropertiesPresenter propertiesPresenter;
    private final DatasourceExplorerConstants   constants;
    private final DatabaseInfoStore             databaseInfoStore;

    /** The currently selected datasource. */
    private String                              selectedDatasource;

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
                                           @NotNull final FetchMetadataService service,
                                           @NotNull final DatasourceManager datasourceManager,
                                           @NotNull final DataEntityPropertiesPresenter propertiesPresenter,
                                           @NotNull final DatasourceExplorerConstants constants,
                                           @NotNull final DatabaseInfoStore databaseInfoStore) {
        this.view = view;
        this.eventBus = eventBus;
        this.service = service;
        this.datasourceManager = datasourceManager;
        this.propertiesPresenter = propertiesPresenter;
        this.constants = constants;
        this.databaseInfoStore = databaseInfoStore;

        this.view.setTitle(constants.datasourceExplorerPartTitle());
        bind();

        // register for datasource creation events
        this.eventBus.addHandler(DatasourceListChangeEvent.getType(), this);
        // register for datasource metadata ready events
        this.eventBus.addHandler(DatabaseInfoReceivedEvent.getType(), this);
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
        return constants.datasourceExplorerTabTitle(this.datasourceManager.getNames().size());
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
        DatabaseConfigurationDTO datasourceObject = this.datasourceManager.getByName(datasourceId);
        if (datasourceId == null || datasourceId.isEmpty()) {
            view.setItems(null);
            return;
        }

        service.fetchDatabaseInfo(datasourceObject);
    }

    @Override
    public void onSelectedDatasourceChanged(final String datasourceId) {
        this.selectedDatasource = datasourceId;
        DatabaseDTO dsMeta = databaseInfoStore.getDatabaseInfo(datasourceId);
        if (dsMeta != null) {
            view.setItems(dsMeta);
            return;
        }
        loadDatasource(datasourceId);

        // After selection, the datasource is selected
        final DatabaseDTO datasource = this.databaseInfoStore.getDatabaseInfo(this.selectedDatasource);
        eventBus.fireEvent(new DatabaseEntitySelectionEvent(datasource));
    }

    @Override
    public void onDatasourceListChange(final DatasourceListChangeEvent event) {
        setupDatasourceList();
        changeTabTitle();
    }

    /**
     * Fills the datasource list widget with the known datasource ids.
     */
    private void setupDatasourceList() {
        Collection<String> datasourceIds = this.datasourceManager.getNames();
        this.view.setDatasourceList(datasourceIds);
    }

    @Override
    public void onDatabaseInfoReceived(final DatabaseInfoReceivedEvent event) {
        if (this.selectedDatasource != null && this.selectedDatasource.equals(event.getDatabaseId())) {
            final DatabaseDTO datasource = this.databaseInfoStore.getDatabaseInfo(this.selectedDatasource);
            this.view.setItems(datasource);
        }
    }

    @Override
    public void onDatabaseInfoError(final DatabaseInfoErrorEvent event) {
        if (this.selectedDatasource != null && this.selectedDatasource.equals(event.getDatabaseId())) {
            this.view.setItems(null);
        }

    }

    /** Triggers a tab title change. */
    private void changeTabTitle() {
        firePropertyChange(TITLE_PROPERTY);
    }
}
