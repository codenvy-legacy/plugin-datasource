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
package com.codenvy.ide.ext.datasource.client.newdatasource.connector;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import com.codenvy.api.user.shared.dto.Profile;
import com.codenvy.ide.api.notification.Notification;
import com.codenvy.ide.api.notification.Notification.Type;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.ui.wizard.AbstractWizardPage;
import com.codenvy.ide.ext.datasource.client.DatasourceClientService;
import com.codenvy.ide.ext.datasource.client.DatasourceManager;
import com.codenvy.ide.ext.datasource.client.events.DatasourceCreatedEvent;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizard;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.util.loging.Log;
import com.codenvy.ide.rest.AsyncRequestCallback;
import com.codenvy.ide.rest.StringUnmarshaller;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.EventBus;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.Window;

public abstract class AbstractNewDatasourceConnectorPage extends AbstractWizardPage implements
             JdbcDatasourceConnectorView.ActionDelegate {

    private JdbcDatasourceConnectorView      view;
    private String                           datasourceId;
    private DatasourceManager                datasourceManager;
    private EventBus                         eventBus;
    private DatasourceClientService          service;
    private NotificationManager              notificationManager;

    public AbstractNewDatasourceConnectorPage(@Nullable final JdbcDatasourceConnectorView view,
                                              @Nullable final String caption,
                                              @Nullable final ImageResource image,
                                              @NotNull final String datasourceId,
                                              @NotNull final DatasourceManager datasourceManager,
                                              @NotNull final EventBus eventBus,
                                              @NotNull final DatasourceClientService service,
                                              @NotNull final NotificationManager notificationManager) {
        super(caption, image);
        view.setDelegate(this);
        this.datasourceId = datasourceId;
        this.datasourceManager = datasourceManager;
        this.eventBus = eventBus;
        this.service = service;
        this.view = view;
        this.notificationManager = notificationManager;
    }

    public JdbcDatasourceConnectorView getView() {
        return view;
    }

    /**
     * Returns the currently configured database.
     * 
     * @return the database
     */
    protected abstract DatabaseConfigurationDTO getConfiguredDatabase();

    /** {@inheritDoc} */
    @Override
    public String getNotice() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isCompleted() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public void focusComponent() {
        // do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void removeOptions() {
        // do nothing
    }

    /** {@inheritDoc} */
    @Override
    public boolean inContext() {
        NewDatasourceConnector datasourceConnector = wizardContext.getData(NewDatasourceWizard.DATASOURCE_CONNECTOR);
        return datasourceConnector != null && datasourceConnector.getId().equals(datasourceId);
    }


    @Override
    public void commit(final CommitCallback callback) {
        DatabaseConfigurationDTO configuredDatabase = getConfiguredDatabase();
        Log.info(AbstractNewDatasourceConnectorPage.class, "Adding datasource with id " + configuredDatabase.getDatasourceId());
        this.datasourceManager.add(configuredDatabase);

        Log.info(AbstractNewDatasourceConnectorPage.class, "Persisting datasources...");
        final Notification requestNotification = new Notification("Persisting datasources...",
                                                                  Notification.Status.PROGRESS);
        this.datasourceManager.persist(new AsyncCallback<Profile>() {

            @Override
            public void onSuccess(Profile result) {
                Log.info(AbstractNewDatasourceConnectorPage.class, "Datasources persisted.");
                requestNotification.setMessage("Datasources saved");
                requestNotification.setStatus(Notification.Status.FINISHED);
            }

            @Override
            public void onFailure(Throwable caught) {
                Log.info(AbstractNewDatasourceConnectorPage.class, "Failed to persist datasources.");
                requestNotification.setStatus(Notification.Status.FINISHED);
                notificationManager.showNotification(new Notification("Failed to persist datasources", Type.ERROR));

            }
        });

        this.eventBus.fireEvent(new DatasourceCreatedEvent(configuredDatabase));
    }


    @Override
    public void onClickTestConnectionButton() {
        DatabaseConfigurationDTO configuration = getConfiguredDatabase();
        try {
            final Notification connectingNotification = new Notification("Establishing Database Connection...",
                    Notification.Status.PROGRESS);
            notificationManager.showNotification(connectingNotification);
            service.testDatabaseConnectivity(configuration, new AsyncRequestCallback<String>(new StringUnmarshaller()) {
                @Override
                protected void onSuccess(String result) {
                    Window.alert(result);
                    connectingNotification.setMessage("Succesfully connected to database");
                    connectingNotification.setStatus(Notification.Status.FINISHED);
                }

                @Override
                protected void onFailure(Throwable exception) {
                    connectingNotification.setStatus(Notification.Status.FINISHED);
                }
            }

            );
        } catch (RequestException e) {
            Log.info(AbstractNewDatasourceConnectorPage.class, e.getMessage());

        }
    }
}
