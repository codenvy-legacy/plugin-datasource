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

import java.util.Map;

import com.codenvy.dto.shared.DTO;

@DTO
public interface DatabaseDTO extends DatabaseMetadataEntityDTO {

    /* Database name. */
    DatabaseDTO withName(String name);

    DatabaseDTO withLookupKey(String lookupKey);

    /* JDBC driver. */
    DatabaseDTO withJdbcDriverName(String driverName);

    void setJdbcDriverName(String driverName);

    String getJdbcDriverName();

    DatabaseDTO withJdbcDriverVersion(String driverVersion);

    void setJdbcDriverVersion(String driverVersion);

    String getJdbcDriverVersion();

    /* Database product. */
    DatabaseDTO withDatabaseProductName(String productName);

    void setDatabaseProductName(String productName);

    String getDatabaseProductName();

    DatabaseDTO withDatabaseProductVersion(String productVersion);

    void setDatabaseProductVersion(String productVersion);

    String getDatabaseProductVersion();

    /* Datasource user name. */
    DatabaseDTO withUserName(String userName);

    void setUserName(String userName);

    String getUserName();

    /* Schemas. */
    DatabaseDTO withSchemas(Map<String, SchemaDTO> schemas);

    Map<String, SchemaDTO> getSchemas();

    void setSchemas(Map<String, SchemaDTO> schemas);

}
