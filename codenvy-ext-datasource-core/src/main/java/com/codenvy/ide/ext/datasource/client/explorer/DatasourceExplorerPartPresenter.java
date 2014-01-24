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
package com.codenvy.ide.ext.datasource.client.explorer;

import java.util.Map;

import javax.validation.constraints.NotNull;

import com.codenvy.ide.api.notification.Notification;
import com.codenvy.ide.api.notification.Notification.Type;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.parts.base.BasePresenter;
import com.codenvy.ide.api.preferences.PreferencesManager;
import com.codenvy.ide.api.selection.Selection;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.ext.datasource.client.DatasourceClientService;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseMetadataEntityDTO;
import com.codenvy.ide.ext.datasource.shared.DatasourceConfigPreferences;
import com.codenvy.ide.resources.marshal.StringUnmarshaller;
import com.codenvy.ide.rest.AsyncRequestCallback;
import com.codenvy.ide.util.loging.Log;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
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
                                                                  DatasourceExplorerView.ActionDelegate, DatasourceExplorerPart {
    protected DatasourceExplorerView  view;
    protected EventBus                eventBus;
    protected DatasourceClientService service;
    protected DtoFactory              dtoFactory;
    protected NotificationManager     notificationManager;
    protected PreferencesManager      preferencesManager;

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
    public DatasourceExplorerPartPresenter(DatasourceExplorerView view,
                                           EventBus eventBus,
                                           DatasourceClientService service,
                                           DtoFactory dtoFactory,
                                           NotificationManager notificationManager,
                                           PreferencesManager preferencesManager) {
        this.view = view;
        this.eventBus = eventBus;
        this.service = service;
        this.dtoFactory = dtoFactory;
        this.notificationManager = notificationManager;
        this.preferencesManager = preferencesManager;
        this.view.setTitle("DataSource Explorer");
        bind();
    }

    /** {@inheritDoc} */
    @Override
    public void go(AcceptsOneWidget container) {
        container.setWidget(view);
    }

    /** Adds behavior to view components */
    protected void bind() {
        view.setDelegate(this);
    }

    /** {@inheritDoc} */
    @Override
    public String getTitle() {
        return "Datasource Explorer";
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
        setSelection(new Selection<DatabaseMetadataEntityDTO>(dbMetadataEntity));
    }

    @Override
    public void onDatabaseMetadataEntityAction(@NotNull DatabaseMetadataEntityDTO dbMetadataEntity) {
        Window.alert("datasources preferences: " + preferencesManager.getValue("datasources"));
    }

    /** {@inheritDoc} */
    @Override
    public void onContextMenu(int mouseX, int mouseY) {
        // contextMenuPresenter.show(mouseX, mouseY);
    }

    @Override
    public void onClickExploreButton(String datasourceId) {
        try {
            // fetch datasource from datasourceId
            // TODO do it from a service
            String datasourcesJson = preferencesManager.getValue("datasources");

            if (datasourcesJson == null) {
                return;
            }
            DatasourceConfigPreferences datasourcesPreferences =
                                                                 dtoFactory.createDtoFromJson(datasourcesJson,
                                                                                              DatasourceConfigPreferences.class);

            Map<String, DatabaseConfigurationDTO> datasourcesMap = datasourcesPreferences.getDatasources();
            DatabaseConfigurationDTO datasourceObject = datasourcesMap.get(datasourceId);
            final Notification fetchDatabaseNotification = new Notification("Fetching database metadata ...", Notification.Status.PROGRESS);
            notificationManager.showNotification(fetchDatabaseNotification);
            service.fetchDatabaseInfo(datasourceObject,
                                      new AsyncRequestCallback<String>(new StringUnmarshaller()) {
                                          @Override
                                          protected void onSuccess(String result) {
                                              DatabaseDTO database = dtoFactory.createDtoFromJson(result,
                                                                                                  DatabaseDTO.class);
                                              fetchDatabaseNotification.setMessage("Succesfully fetched database metadata");
                                              fetchDatabaseNotification.setStatus(Notification.Status.FINISHED);
                                              view.setItems(database);
                                          }

                                          @Override
                                          protected void onFailure(Throwable exception) {
                                              fetchDatabaseNotification.setStatus(Notification.Status.FINISHED);
                                              notificationManager.showNotification(new Notification("Failed fetching database metadatas",
                                                                                                    Type.ERROR));
                                          }
                                      }

                   );
        } catch (RequestException e) {
            Log.error(DatasourceExplorerPartPresenter.class,
                      "Exception on database info fetch : " + e.getMessage());
            notificationManager.showNotification(new Notification("Failed fetching database metadatas",
                                                                  Type.ERROR));
        }
    }
}
