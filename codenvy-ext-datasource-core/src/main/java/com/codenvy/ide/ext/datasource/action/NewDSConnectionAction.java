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
package com.codenvy.ide.ext.datasource.action;

import static com.codenvy.ide.api.notification.Notification.Type.ERROR;
import static com.codenvy.ide.api.notification.Notification.Type.INFO;

import com.codenvy.ide.api.notification.Notification;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.ui.action.Action;
import com.codenvy.ide.api.ui.action.ActionEvent;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.ext.datasource.client.DatasourceClientService;
import com.codenvy.ide.ext.datasource.shared.DatabaseDTO;
import com.codenvy.ide.rest.AsyncRequestCallback;
import com.codenvy.ide.rest.StringUnmarshaller;
import com.google.gwt.http.client.RequestException;
import com.google.inject.Inject;

public class NewDSConnectionAction extends Action {

    protected DatasourceClientService service;
    protected NotificationManager     notificationManager;
    protected DtoFactory              dtoFactory;

    @Inject
    public NewDSConnectionAction(DatasourceClientService service,
                                 NotificationManager notificationManager, DtoFactory dtoFactory) {
        super("New Datasource Connection");
        this.service = service;
        this.notificationManager = notificationManager;
        this.dtoFactory = dtoFactory;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            service.fetchDatabase(new AsyncRequestCallback<String>(
                                                                   new StringUnmarshaller()) {
                @Override
                protected void onSuccess(String result) {
                    DatabaseDTO database = dtoFactory.createDtoFromJson(result,
                                                                        DatabaseDTO.class);
                    notificationManager.showNotification(new Notification(
                                                                          "Success ", INFO));
                    notificationManager.showNotification(new Notification(
                                                                          database.getName(), INFO));
                }

                @Override
                protected void onFailure(Throwable exception) {
                    notificationManager.showNotification(new Notification(
                                                                          "Failed : " + exception.getMessage(), ERROR));
                }

            });

        } catch (RequestException e1) {
            String errorMassage = e1.getMessage() != null ? e1.getMessage()
                : "Failing getting database DTO";
            Notification notification = new Notification(errorMassage, ERROR);
            notificationManager.showNotification(notification);
        }
    }

}
