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

import com.codenvy.ide.api.notification.Notification;
import com.codenvy.ide.api.notification.Notification.Type;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.ui.wizard.AbstractWizardPage;
import com.codenvy.ide.ext.datasource.client.DatasourceManager;
import com.codenvy.ide.ext.datasource.client.events.DatasourceCreatedEvent;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizard;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.util.loging.Log;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.EventBus;

public abstract class AbstractNewDatasourceConnectorPage extends AbstractWizardPage {
    private final String              datasourceId;
    private final DatasourceManager   datasourceManager;
    private final EventBus            eventBus;
    private final NotificationManager notificationManager;

    public AbstractNewDatasourceConnectorPage(@Nullable final String caption,
                                              @Nullable final ImageResource image,
                                              @NotNull final String datasourceId,
                                              @NotNull final DatasourceManager datasourceManager,
                                              @NotNull final EventBus eventBus,
                                              @NotNull final NotificationManager notificationManager) {
        super(caption, image);
        this.datasourceId = datasourceId;
        this.datasourceManager = datasourceManager;
        this.eventBus = eventBus;
        this.notificationManager = notificationManager;
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
        this.datasourceManager.persist(new AsyncCallback<Void>() {

            @Override
            public void onSuccess(Void result) {
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
}
