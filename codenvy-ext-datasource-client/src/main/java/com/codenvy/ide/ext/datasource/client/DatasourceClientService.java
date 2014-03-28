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

import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.MultipleRequestExecutionMode;
import com.codenvy.ide.ext.datasource.shared.request.RequestResultDTO;
import com.codenvy.ide.rest.AsyncRequestCallback;
import com.google.gwt.http.client.RequestException;

/**
 * Client interface for the datasource plugin server services.
 */
public interface DatasourceClientService {

    void fetchDatabaseInfo(@NotNull DatabaseConfigurationDTO configuration,
                           @NotNull AsyncRequestCallback<String> asyncRequestCallback) throws RequestException;

    void executeSqlRequest(@NotNull DatabaseConfigurationDTO configuration,
                           int resultLimit,
                           @NotNull String sqlRequest,
                           MultipleRequestExecutionMode execmode,
                           @NotNull AsyncRequestCallback<String> asyncRequestCallback) throws RequestException;

    void getAvailableDrivers(@NotNull AsyncRequestCallback<String> asyncRequestCallback) throws RequestException;

    String getRestServiceContext();

    void exportAsCsv(final RequestResultDTO requestResult,
                     final AsyncRequestCallback<String> asyncRequestCallback) throws RequestException;

    void testDatabaseConnectivity(@NotNull DatabaseConfigurationDTO configuration,
                                  @NotNull AsyncRequestCallback<String> asyncRequestCallback) throws RequestException;
}
