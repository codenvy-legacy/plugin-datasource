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
package com.codenvy.ide.ext.datasource.shared;

import java.util.List;

import com.codenvy.dto.shared.DTO;

@DTO
public interface DatabaseConfigurationDTO {

    String getDatasourceId();

    void setDatasourceId(String id);

    DatabaseConfigurationDTO withDatasourceId(String type);


    String getDatabaseName();

    void setDatabaseName(String databaseName);

    DatabaseConfigurationDTO withDatabaseName(String databaseName);


    DatabaseType getDatabaseType();

    void setDatabaseType(DatabaseType type);

    DatabaseConfigurationDTO withDatabaseType(DatabaseType type);


    String getUsername();

    DatabaseConfigurationDTO withUsername(String username);

    void setUsername(String username);


    String getPassword();

    void setPassword(String password);

    DatabaseConfigurationDTO withPassword(String password);


    /* should be in child classes */
    String getHostName();

    int getPort();


    void setHostName(String hostname);

    void setPort(int port);


    List<NuoDBBrokerDTO> getBrokers();

    void setBrokers(List<NuoDBBrokerDTO> brokers);


    boolean getUseSSL();

    DatabaseConfigurationDTO withUseSSL(boolean useSSL);

    void setUseSSL(boolean useSSL);


    boolean getVerifyServerCertificate();

    DatabaseConfigurationDTO withVerifyServerCertificate(boolean verifyServerCertificate);

    void setVerifyServerCertificate(boolean verifyServerCertificate);
}
