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
