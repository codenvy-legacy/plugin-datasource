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
package com.codenvy.ide.ext.datasource.client.newdatasource.connector;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import com.codenvy.api.user.shared.dto.Profile;
import com.codenvy.ide.api.notification.Notification;
import com.codenvy.ide.api.notification.Notification.Type;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.ui.wizard.AbstractWizardPage;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.ext.datasource.client.DatasourceClientService;
import com.codenvy.ide.ext.datasource.client.events.DatasourceListChangeEvent;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizard;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardMessages;
import com.codenvy.ide.ext.datasource.client.store.DatasourceManager;
import com.codenvy.ide.ext.datasource.shared.ConnectionTestResultDTO;
import com.codenvy.ide.ext.datasource.shared.ConnectionTestResultDTO.Status;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseType;
import com.codenvy.ide.rest.AsyncRequestCallback;
import com.codenvy.ide.rest.StringUnmarshaller;
import com.codenvy.ide.util.loging.Log;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;

public abstract class AbstractNewDatasourceConnectorPage extends AbstractWizardPage
                                                                                   implements
                                                                                   AbstractNewDatasourceConnectorView.ActionDelegate {

    private final AbstractNewDatasourceConnectorView view;
    private final String                             datasourceId;
    private final DatasourceManager                  datasourceManager;
    private final EventBus                           eventBus;
    private final DatasourceClientService            service;
    private final NotificationManager                notificationManager;
    private final DtoFactory                         dtoFactory;
    private final NewDatasourceWizardMessages        messages;

    public AbstractNewDatasourceConnectorPage(@NotNull final AbstractNewDatasourceConnectorView view,
                                              @Nullable final String caption,
                                              @Nullable final ImageResource image,
                                              @NotNull final String datasourceId,
                                              @NotNull final DatasourceManager datasourceManager,
                                              @NotNull final EventBus eventBus,
                                              @NotNull final DatasourceClientService service,
                                              @NotNull final NotificationManager notificationManager,
                                              @NotNull final DtoFactory dtoFactory,
                                              @NotNull final NewDatasourceWizardMessages messages) {
        super(caption, image);
        view.setDelegate(this);
        this.datasourceId = datasourceId;
        this.datasourceManager = datasourceManager;
        this.eventBus = eventBus;
        this.service = service;
        this.view = view;
        this.notificationManager = notificationManager;
        this.dtoFactory = dtoFactory;
        this.messages = messages;
    }

    @Override
    public void go(final AcceptsOneWidget container) {
        container.setWidget(getView());
    }

    public AbstractNewDatasourceConnectorView getView() {
        return view;
    }

    /**
     * Returns the currently configured database.
     * 
     * @return the database
     */
    protected abstract DatabaseConfigurationDTO getConfiguredDatabase();

    @Override
    public String getNotice() {
        return null;
    }

    @Override
    public boolean isCompleted() {
        return true;
    }

    @Override
    public void focusComponent() {
        // do nothing
    }

    @Override
    public void removeOptions() {
        // do nothing
    }

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

        this.eventBus.fireEvent(new DatasourceListChangeEvent());
    }


    @Override
    public void onClickTestConnectionButton() {
        DatabaseConfigurationDTO configuration = getConfiguredDatabase();

        final Notification connectingNotification = new Notification(messages.startConnectionTest(),
                                                                     Notification.Status.PROGRESS);
        this.notificationManager.showNotification(connectingNotification);

        try {
            this.service.testDatabaseConnectivity(configuration, new AsyncRequestCallback<String>(new StringUnmarshaller()) {
                @Override
                protected void onSuccess(String result) {
                    final ConnectionTestResultDTO testResult = dtoFactory.createDtoFromJson(result, ConnectionTestResultDTO.class);
                    if (Status.SUCCESS.equals(testResult.getTestResult())) {
                        getView().onTestConnectionSuccess();
                        connectingNotification.setMessage(messages.connectionTestSuccessNotification());
                        connectingNotification.setStatus(Notification.Status.FINISHED);
                    } else {
                        getView().onTestConnectionFailure(messages.connectionTestFailureSuccessMessage() + " "
                                                          + testResult.getFailureMessage());
                        connectingNotification.setMessage(messages.connectionTestFailureSuccessNotification());
                        connectingNotification.setType(Type.ERROR);
                        connectingNotification.setStatus(Notification.Status.FINISHED);
                    }
                }

                @Override
                protected void onFailure(final Throwable exception) {
                    getView().onTestConnectionFailure(messages.connectionTestFailureSuccessMessage());
                    connectingNotification.setMessage(messages.connectionTestFailureSuccessNotification());
                    connectingNotification.setType(Type.ERROR);
                    connectingNotification.setStatus(Notification.Status.FINISHED);
                }
            }

                        );
        } catch (final RequestException e) {
            Log.info(AbstractNewDatasourceConnectorPage.class, e.getMessage());
            getView().onTestConnectionFailure(messages.connectionTestFailureSuccessMessage());
            connectingNotification.setMessage(messages.connectionTestFailureSuccessNotification());
            connectingNotification.setType(Type.ERROR);
            connectingNotification.setStatus(Notification.Status.FINISHED);
        }
    }

    public abstract Integer getDefaultPort();

    public abstract DatabaseType getDatabaseType();
}
