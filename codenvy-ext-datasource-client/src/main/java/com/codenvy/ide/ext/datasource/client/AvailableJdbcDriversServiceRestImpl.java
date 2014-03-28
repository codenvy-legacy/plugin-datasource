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
