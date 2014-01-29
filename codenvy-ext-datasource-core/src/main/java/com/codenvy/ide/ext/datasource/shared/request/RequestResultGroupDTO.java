package com.codenvy.ide.ext.datasource.shared.request;

import java.util.List;

import com.codenvy.dto.shared.DTO;

@DTO
public interface RequestResultGroupDTO {

    List<RequestResultDTO> getResults();

    void setResults(List<RequestResultDTO> results);

    RequestResultGroupDTO withResults(List<RequestResultDTO> results);
}
