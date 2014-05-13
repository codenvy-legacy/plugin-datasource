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

import java.util.Map;

import com.codenvy.dto.shared.DTO;

/**
 * Describe a database structure: basic informations, schemas, tables and columns.
 */
@DTO
public interface DatabaseDTO extends DatabaseMetadataEntityDTO {

    /* Database overrides. */
    DatabaseDTO withName(String name);

    DatabaseDTO withLookupKey(String lookupKey);

    DatabaseDTO withComment(String comment);

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
