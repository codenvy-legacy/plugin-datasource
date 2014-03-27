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
