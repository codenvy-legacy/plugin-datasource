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
public interface SchemaDTO extends DatabaseMetadataEntityDTO {

    SchemaDTO withName(String name);

    SchemaDTO withLookupKey(String lookupKey);

    SchemaDTO withTables(Map<String, TableDTO> tables);

    Map<String, TableDTO> getTables();

    void setTables(Map<String, TableDTO> tables);

}
