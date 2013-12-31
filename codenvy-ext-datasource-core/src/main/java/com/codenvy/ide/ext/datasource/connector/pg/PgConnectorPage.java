/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
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
package com.codenvy.ide.ext.datasource.connector.pg;

import com.codenvy.ide.api.notification.Notification;
import com.codenvy.ide.api.notification.Notification.Type;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.ext.datasource.client.DatasourceClientService;
import com.codenvy.ide.ext.datasource.connector.AbstractConnectorPage;
import com.codenvy.ide.ext.datasource.explorer.part.DatasourceExplorerView;
import com.codenvy.ide.ext.datasource.shared.DatabaseDTO;
import com.codenvy.ide.resources.marshal.StringUnmarshaller;
import com.codenvy.ide.rest.AsyncRequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

public class PgConnectorPage extends AbstractConnectorPage implements PgConnectorView.ActionDelegate {
    protected PgConnectorView         view;
    protected DatasourceClientService service;
    protected DtoFactory              dtoFactory;
    protected NotificationManager     notificationManager;
    protected DatasourceExplorerView  datasourceExplorerView;

    @Inject
    public PgConnectorPage(PgConnectorView view,
                           DatasourceClientService service,
                           DtoFactory dtoFactory,
                           NotificationManager notificationManager, DatasourceExplorerView datasourceExplorerView) {
        super("PostgreSQL", null, "postgres");
        this.view = view;
        this.service = service;
        this.dtoFactory = dtoFactory;
        this.notificationManager = notificationManager;
        this.datasourceExplorerView = datasourceExplorerView;
    }


    @Override
    public void go(AcceptsOneWidget container) {
        container.setWidget(view);
    }

    @Override
    public void commit(final CommitCallback callback) {

        try {
            service.fetchDatabaseInfo(view.getDatabaseName(), view.getHostname(), view.getPort(), view.getUsername(), view.getPassword(),
                                      new AsyncRequestCallback<String>(new StringUnmarshaller()) {
                                          @Override
                                          protected void onSuccess(String result) {
                                              DatabaseDTO database = dtoFactory.createDtoFromJson(result,
                                                                                                  DatabaseDTO.class);
                                              notificationManager.showNotification(new Notification(
                                                                                                    "Success getting database information",
                                                                                                    Type.INFO));
                                              datasourceExplorerView.setItems(database);
                                              callback.onSuccess();
                                          }

                                          @Override
                                          protected void onFailure(Throwable exception) {
                                              callback.onFailure(exception);
                                          }
                                      }

                   );
        } catch (RequestException e) {
            callback.onFailure(e);
        }
    }

}
