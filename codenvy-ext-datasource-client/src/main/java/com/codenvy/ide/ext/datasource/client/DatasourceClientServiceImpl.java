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

import javax.validation.constraints.NotNull;

import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.MultipleRequestExecutionMode;
import com.codenvy.ide.ext.datasource.shared.RequestParameterDTO;
import com.codenvy.ide.ext.datasource.shared.ServicePaths;
import com.codenvy.ide.ext.datasource.shared.request.RequestResultDTO;
import com.codenvy.ide.rest.AsyncRequest;
import com.codenvy.ide.rest.AsyncRequestCallback;
import com.codenvy.ide.rest.AsyncRequestFactory;
import com.google.gwt.http.client.RequestException;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * Implementation (REST) for the datasource server services client interface.
 */
@Singleton
public class DatasourceClientServiceImpl implements DatasourceClientService {

    private final String              restServiceContext;
    private final DtoFactory          dtoFactory;
    private final AsyncRequestFactory asyncRequestFactory;

    /**
     * @param restContext rest context
     * @param loader loader to show on server request
     */
    @Inject
    protected DatasourceClientServiceImpl(final @Named("restContext") String restContext,
                                          final DtoFactory dtoFactory,
                                          final AsyncRequestFactory asyncRequestFactory) {
        this.restServiceContext = restContext;
        this.dtoFactory = dtoFactory;
        this.asyncRequestFactory = asyncRequestFactory;
    }

    @Override
    public void fetchDatabaseInfo(final @NotNull DatabaseConfigurationDTO configuration,
                                  final @NotNull AsyncRequestCallback<String> asyncRequestCallback) throws RequestException {
        final String url = formatUrl(this.restServiceContext, ServicePaths.BASE_DATASOURCE_PATH,
                                     ServicePaths.DATABASE_METADATA_PATH, null);
        final AsyncRequest postRequest = this.asyncRequestFactory.createPostRequest(url, configuration, true);
        postRequest.send(asyncRequestCallback);
    }

    @Override
    public void executeSqlRequest(final DatabaseConfigurationDTO configuration,
                                  final int resultLimit,
                                  final String sqlRequest,
                                  final MultipleRequestExecutionMode execMode,
                                  final AsyncRequestCallback<String> asyncRequestCallback)
                                                                                          throws RequestException {
        final String url = formatUrl(this.restServiceContext, ServicePaths.BASE_DATASOURCE_PATH,
                                     ServicePaths.EXECUTE_SQL_REQUEST_PATH, null);
        final RequestParameterDTO requestParameterDTO = dtoFactory.createDto(RequestParameterDTO.class)
                                                                  .withDatabase(configuration)
                                                                  .withResultLimit(resultLimit)
                                                                  .withSqlRequest(sqlRequest)
                                                                  .withMultipleRequestExecutionMode(execMode);
        final AsyncRequest postRequest = this.asyncRequestFactory.createPostRequest(url, requestParameterDTO, true);
        postRequest.send(asyncRequestCallback);
    }

    @Override
    public void getAvailableDrivers(AsyncRequestCallback<String> asyncRequestCallback) throws RequestException {
        String url = formatUrl(this.restServiceContext, ServicePaths.BASE_DATASOURCE_PATH,
                               ServicePaths.DATABASE_TYPES_PATH, null);
        final AsyncRequest getRequest = this.asyncRequestFactory.createGetRequest(url, true);
        getRequest.send(asyncRequestCallback);
    }

    @Override
    public String getRestServiceContext() {
        return this.restServiceContext;
    }

    @Override
    public void exportAsCsv(final RequestResultDTO requestResult,
                            final AsyncRequestCallback<String> asyncRequestCallback) throws RequestException {
        String url = formatUrl(this.restServiceContext, ServicePaths.BASE_DATASOURCE_PATH,
                               ServicePaths.RESULT_CSV_PATH, null);
        AsyncRequest postRequest = this.asyncRequestFactory.createPostRequest(url, requestResult, true);
        postRequest.send(asyncRequestCallback);
    }

    @Override
    public void testDatabaseConnectivity(final @NotNull DatabaseConfigurationDTO configuration,
                                         final @NotNull AsyncRequestCallback<String> asyncRequestCallback) throws RequestException {
        String url = formatUrl(this.restServiceContext, ServicePaths.BASE_DATASOURCE_PATH,
                               ServicePaths.TEST_DATABASE_CONNECTIVITY_PATH, null);
        final AsyncRequest postRequest = this.asyncRequestFactory.createPostRequest(url, configuration, true);
        postRequest.send(asyncRequestCallback);
    }

    /**
     * Builds the target REST service url.
     * 
     * @param context the rest context
     * @param root the root of the service
     * @param service the rest service
     * @param param the parameters
     * @return the url
     */
    private String formatUrl(final String context, final String root, final String service, final String param) {
        StringBuilder sb = new StringBuilder(context);
        sb.append("/")
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
