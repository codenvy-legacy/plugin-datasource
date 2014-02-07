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
package com.codenvy.ide.ext.datasource.client;

import java.util.List;

import com.codenvy.ide.api.notification.Notification;
import com.codenvy.ide.api.notification.Notification.Type;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.ext.datasource.client.events.JdbcDriversFetchedEvent;
import com.codenvy.ide.ext.datasource.shared.DriversDTO;
import com.codenvy.ide.rest.AsyncRequestCallback;
import com.codenvy.ide.rest.StringUnmarshaller;
import com.google.gwt.http.client.RequestException;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class AvailableJdbcDriversServiceRestImpl implements AvailableJdbcDriversService {

    List<String>                      drivers;
    protected DatasourceClientService datasourceClientService;
    protected DtoFactory              dtoFactory;
    protected NotificationManager     notificationManager;
    protected EventBus                eventBus;

    @Inject
    public AvailableJdbcDriversServiceRestImpl(DatasourceClientService datasourceClientService,
                                               DtoFactory dtoFactory,
                                               NotificationManager notificationManager, EventBus eventBus) {
        this.datasourceClientService = datasourceClientService;
        this.dtoFactory = dtoFactory;
        this.notificationManager = notificationManager;
        this.eventBus = eventBus;
    }

    @Override
    public void fetch() {
        try {
            datasourceClientService.getAvailableDrivers(new AsyncRequestCallback<String>(new StringUnmarshaller()) {
                @Override
                protected void onSuccess(String result) {
                    DriversDTO driversDto = dtoFactory.createDtoFromJson(result, DriversDTO.class);
                    drivers = driversDto.getDrivers();
                    eventBus.fireEvent(new JdbcDriversFetchedEvent(drivers));
                }

                @Override
                protected void onFailure(Throwable exception) {
                    notificationManager.showNotification(new Notification("Failed getting available JDBC drivers from server: "
                                                                          + exception.getMessage(), Type.ERROR));
                }
            });
        } catch (RequestException e) {
            notificationManager.showNotification(new Notification("Failed getting available JDBC drivers from server: " + e.getMessage(),
                                                                  Type.ERROR));
        }
    }

    @Override
    public List<String> getDrivers() {
        return drivers;
    }

}
