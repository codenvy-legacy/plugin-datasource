package com.codenvy.ide.ext.datasource.shared.request;

import com.codenvy.dto.shared.DTO;

@DTO
public interface SqlExecutionError {

    int getErrorCode();

    void setErrorCode(int code);

    SqlExecutionError withErrorCode(int code);


    String getErrorMessage();

    void setErrorMessage(String message);

    SqlExecutionError withErrorMessage(String message);
}
