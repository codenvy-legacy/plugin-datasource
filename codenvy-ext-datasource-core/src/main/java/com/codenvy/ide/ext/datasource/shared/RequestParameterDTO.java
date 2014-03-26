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
public interface RequestParameterDTO {

    DatabaseConfigurationDTO getDatabase();

    void setDatabase(DatabaseConfigurationDTO database);

    RequestParameterDTO withDatabase(DatabaseConfigurationDTO database);

    String getSqlRequest();

    void setSqlRequest(String sqlRequest);

    RequestParameterDTO withSqlRequest(String sqlRequest);

    int getResultLimit();

    void setResultLimit(int resultLimit);

    RequestParameterDTO withResultLimit(int resultLimit);


    MultipleRequestExecutionMode getMultipleRequestExecutionMode();

    void setMultipleRequestExecutionMode(MultipleRequestExecutionMode mode);

    RequestParameterDTO withMultipleRequestExecutionMode(MultipleRequestExecutionMode mode);
}
