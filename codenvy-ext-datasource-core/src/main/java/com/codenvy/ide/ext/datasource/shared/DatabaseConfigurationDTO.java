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
package com.codenvy.ide.ext.datasource.shared;

import com.codenvy.dto.shared.DTO;

@DTO
public interface DatabaseConfigurationDTO {

    String getDatasourceId();

    String getHostname();

    int getPort();

    String getDatabaseName();

    String getUsername();

    String getPassword();

    DatabaseType getDatabaseType();

    Boolean getUseSSL();

    Boolean getVerifyServerCertificate();

    Boolean getRequireSSL();
    
    String getServerSslCert();

    DatabaseConfigurationDTO withDatasourceId(String type);

    DatabaseConfigurationDTO withDatabaseName(String databaseName);

    DatabaseConfigurationDTO withHostname(String hostname);

    DatabaseConfigurationDTO withPort(int port);

    DatabaseConfigurationDTO withUsername(String username);

    DatabaseConfigurationDTO withPassword(String password);

    DatabaseConfigurationDTO withDatabaseType(DatabaseType type);

    DatabaseConfigurationDTO withUseSSL(boolean useSSL);

    DatabaseConfigurationDTO withVerifyServerCertificate(boolean verifyServerCertificate);

    DatabaseConfigurationDTO withRequireSSL(boolean requireSSL);

    void setDatasourceId(String id);

    void setDatabaseName(String databaseName);

    void setHostname(String hostname);

    void setPort(int port);

    void setUsername(String username);

    void setPassword(String password);

    void setDatabaseType(DatabaseType type);

    void setUseSSL(boolean useSSL);

    void setVerifyServerCertificate(boolean verifyServerCertificate);

    void setRequireSSL(boolean requireSSL);


}
