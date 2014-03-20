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

import javax.validation.constraints.NotNull;

import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.MultipleRequestExecutionMode;
import com.codenvy.ide.ext.datasource.shared.request.RequestResultDTO;
import com.codenvy.ide.rest.AsyncRequestCallback;
import com.google.gwt.http.client.RequestException;

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
