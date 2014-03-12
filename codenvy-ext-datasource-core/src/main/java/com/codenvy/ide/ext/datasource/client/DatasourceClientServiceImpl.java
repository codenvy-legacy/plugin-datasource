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
import com.codenvy.ide.ext.datasource.shared.MultipleRequestExecutionMode;
import com.codenvy.ide.ext.datasource.shared.RequestParameterDTO;
import com.codenvy.ide.ext.datasource.shared.ServicePaths;
import com.codenvy.ide.ext.datasource.shared.request.RequestResultDTO;
import com.codenvy.ide.rest.AsyncRequest;
import com.codenvy.ide.rest.AsyncRequestCallback;
import com.codenvy.ide.ui.loader.Loader;
import com.codenvy.ide.util.Utils;
import com.codenvy.ide.websocket.MessageBus;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.URL;
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
        fetchDatabaseInfo(dto, asyncRequestCallback);
    }

    @Override
    public void fetchDatabaseInfo(final @NotNull DatabaseConfigurationDTO configuration,
                                  final @NotNull AsyncRequestCallback<String> asyncRequestCallback) throws RequestException {
        String url =
                     formatUrl(this.restServiceContext, ServicePaths.BASE_DATASOURCE_PATH, ServicePaths.DATABASE_METADATA_PATH,
                               null);
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
                                  final MultipleRequestExecutionMode execMode,
                                  final AsyncRequestCallback<String> asyncRequestCallback)
                                                                                          throws RequestException {
        String url =
                     formatUrl(this.restServiceContext, ServicePaths.BASE_DATASOURCE_PATH, ServicePaths.EXECUTE_SQL_REQUEST_PATH,
                               null);
        RequestParameterDTO requestParameterDTO = dtoFactory.createDto(RequestParameterDTO.class)
                                                            .withDatabase(configuration)
                                                            .withResultLimit(resultLimit)
                                                            .withSqlRequest(sqlRequest)
                                                            .withMultipleRequestExecutionMode(execMode);
        AsyncRequest.build(RequestBuilder.POST, url, true)
                    .data(dtoFactory.toJson(requestParameterDTO))
                    .header(CONTENTTYPE, APPLICATION_JSON)
                    .header(ACCEPT, APPLICATION_JSON)
                    .send(asyncRequestCallback);
    }

    @Override
    public void getAvailableDrivers(AsyncRequestCallback<String> asyncRequestCallback) throws RequestException {
        String url =
                     formatUrl(this.restServiceContext, ServicePaths.BASE_DATASOURCE_PATH, ServicePaths.DATABASE_TYPES_PATH,
                               null);
        AsyncRequest.build(RequestBuilder.GET, url, true)
                    .header(ACCEPT, APPLICATION_JSON)
                    .send(asyncRequestCallback);
    }

    @Override
    public String getRestServiceContext() {
        return this.restServiceContext;
    }

    @Override
    public String buildCsvExportUrl(final RequestResultDTO requestResult) {
        // the dto is converted to json then uri-escaped
        final String jsonParameter = dtoFactory.toJson(requestResult);
        return formatUrl(this.restServiceContext, ServicePaths.BASE_DATASOURCE_PATH, ServicePaths.RESULT_CSV_PATH,
                         URL.encode(jsonParameter));
    }

    public void testDatabaseConnectivity(final @NotNull DatabaseConfigurationDTO configuration,
                                         final @NotNull AsyncRequestCallback<String> asyncRequestCallback) throws RequestException {
        String url =
                     formatUrl(this.restServiceContext, ServicePaths.BASE_DATASOURCE_PATH, ServicePaths.TEST_DATABASE_CONNECTIVITY_PATH,
                               null);
        AsyncRequest.build(RequestBuilder.POST, url, true)
                    .data(dtoFactory.toJson(configuration))
                    .header(CONTENTTYPE, APPLICATION_JSON)
                    .header(ACCEPT, APPLICATION_JSON)
                    .send(asyncRequestCallback);
    }

    private String formatUrl(final String context, final String root, final String service, final String param) {
        StringBuilder sb = new StringBuilder(context).append("/")
                                                     .append(root)
                                                     .append("/")
                                                     .append(service);
        if (param != null) {
            sb.append('/')
              .append(param);
        }
        return sb.toString();
    }

}
