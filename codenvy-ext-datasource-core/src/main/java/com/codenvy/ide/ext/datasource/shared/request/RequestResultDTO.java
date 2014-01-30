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

}
