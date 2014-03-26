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
package com.codenvy.ide.ext.datasource.shared.request;

import java.util.List;

import com.codenvy.dto.shared.DTO;

@DTO
public interface RequestResultDTO {

    int getResultType();

    void setResultType(int type);

    RequestResultDTO withResultType(int type);

    List<String> getHeaderLine();

    void setHeaderLine(List<String> line);

    RequestResultDTO withHeaderLine(List<String> line);

    List<List<String>> getResultLines();

    void setResultLines(List<List<String>> lines);

    RequestResultDTO withResultLines(List<List<String>> lines);

    int getUpdateCount();

    void setUpdateCount(int count);

    RequestResultDTO withUpdateCount(int count);


    SqlExecutionError getSqlExecutionError();

    void setSqlExecutionError(SqlExecutionError error);

    RequestResultDTO withSqlExecutionError(SqlExecutionError error);


    String getOriginRequest();

    void setOriginRequest(String request);

    RequestResultDTO withOriginRequest(String request);
}
