package com.codenvy.ide.ext.datasource.shared;

import com.codenvy.dto.shared.DTO;

@DTO
public interface ConnectionTestResultDTO {

    /**
     * The test result, success or failure.
     * 
     * @return true for success, false for failure.
     */
    Status getTestResult();

    String getFailureMessage();

    void setTestResult(Status result);

    void setFailureMessage(String message);

    ConnectionTestResultDTO withTestResult(Status result);

    ConnectionTestResultDTO withFailureMessage(String message);

    enum Status {
        SUCCESS,
        FAILURE
    }
}
