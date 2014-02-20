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
