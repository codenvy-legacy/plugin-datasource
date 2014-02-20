package com.codenvy.ide.ext.datasource.shared.request;

import com.codenvy.dto.shared.DTO;

@DTO
public interface ExecutionErrorResultDTO extends RequestResultDTO {

    static int TYPE = 0;

    ExecutionErrorResultDTO withSqlExecutionError(SqlExecutionError error);
}
