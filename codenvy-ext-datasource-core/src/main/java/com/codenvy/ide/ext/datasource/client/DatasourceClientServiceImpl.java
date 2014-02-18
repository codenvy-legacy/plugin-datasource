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

import static com.codenvy.ide.rest.HTTPHeader.ACCEPT;
import static com.codenvy.ide.rest.HTTPHeader.CONTENTTYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.validation.constraints.NotNull;

import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.RequestParameterDTO;
import com.codenvy.ide.rest.AsyncRequest;
import com.codenvy.ide.rest.AsyncRequestCallback;
import com.codenvy.ide.ui.loader.Loader;
import com.codenvy.ide.util.Utils;
import com.codenvy.ide.websocket.MessageBus;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestException;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.web.bindery.event.shared.EventBus;

@Singleton
public class DatasourceClientServiceImpl implements DatasourceClientService {

    private final Loader     loader;
    private final String     wsName;
    private final String     restServiceContext;
    private final MessageBus wsMessageBus;
    private final EventBus   eventBus;
    private final DtoFactory dtoFactory;

    /**
     * @param restContext rest context
     * @param loader loader to show on server request
     */
    @Inject
    protected DatasourceClientServiceImpl(@Named("restContext") String restContext, Loader loader,
                                          MessageBus wsMessageBus, EventBus eventBus, DtoFactory dtoFactory) {
        this.loader = loader;
        this.wsName = '/' + Utils.getWorkspaceName();
        this.restServiceContext = restContext + wsName;
        this.wsMessageBus = wsMessageBus;
        this.eventBus = eventBus;
        this.dtoFactory = dtoFactory;
    }

    @Override
    public void fetchDatabaseInfo(@NotNull String databaseName,
                                  @NotNull String hostname,
                                  @NotNull int port,
                                  @NotNull String username,
                                  @NotNull String password,
                                  @NotNull AsyncRequestCallback<String> asyncRequestCallback
        ) throws RequestException {
        DatabaseConfigurationDTO dto = dtoFactory.createDto(DatabaseConfigurationDTO.class)
                                                 .withDatabaseName(databaseName)
                                                 .withHostname(hostname)
                                                 .withPort(port)
                                                 .withUsername(username)
                                                 .withPassword(password);
        String url = restServiceContext + "/datasource/database";
        AsyncRequest.build(RequestBuilder.POST, url, true).data(dtoFactory.toJson(dto)).header(CONTENTTYPE, APPLICATION_JSON)
                    .header(ACCEPT, APPLICATION_JSON).send(asyncRequestCallback);
    }

    @Override
    public void fetchDatabaseInfo(final @NotNull DatabaseConfigurationDTO configuration,
                                  final @NotNull AsyncRequestCallback<String> asyncRequestCallback) throws RequestException {
        String url = restServiceContext + "/datasource/database";
        AsyncRequest.build(RequestBuilder.POST, url, true)
                    .data(dtoFactory.toJson(configuration))
                    .header(CONTENTTYPE, APPLICATION_JSON)
                    .header(ACCEPT, APPLICATION_JSON)
                    .send(asyncRequestCallback);
    }

    @Override
    public void executeSqlRequest(final DatabaseConfigurationDTO configuration,
                                  final int resultLimit,
                                  final String sqlRequest,
                                  final AsyncRequestCallback<String> asyncRequestCallback)
                                                                                          throws RequestException {
        String url = restServiceContext + "/datasource/executeSqlRequest";
        RequestParameterDTO requestParameterDTO = dtoFactory.createDto(RequestParameterDTO.class)
                                                            .withDatabase(configuration)
                                                            .withResultLimit(resultLimit)
                                                            .withSqlRequest(sqlRequest);
        AsyncRequest.build(RequestBuilder.POST, url, true)
                    .data(dtoFactory.toJson(requestParameterDTO))
                    .header(CONTENTTYPE, APPLICATION_JSON)
                    .header(ACCEPT, APPLICATION_JSON)
                    .send(asyncRequestCallback);
    }

    @Override
    public void getAvailableDrivers(AsyncRequestCallback<String> asyncRequestCallback) throws RequestException {
        String url = restServiceContext + "/datasource/drivers";
        AsyncRequest.build(RequestBuilder.GET, url, true)
                    .header(ACCEPT, APPLICATION_JSON)
                    .send(asyncRequestCallback);
    }

}
