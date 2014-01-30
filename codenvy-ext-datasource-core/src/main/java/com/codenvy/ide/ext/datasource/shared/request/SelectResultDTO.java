package com.codenvy.ide.ext.datasource.shared.request;

import java.util.List;

import com.codenvy.dto.shared.DTO;

@DTO
public interface SelectResultDTO extends RequestResultDTO {

    static int TYPE = 1;

    List<String> getHeaderLine();

    void setHeaderLine(List<String> line);

    SelectResultDTO withHeaderLine(List<String> line);

    List<List<String>> getResultLines();

    void setResultLines(List<List<String>> lines);

    SelectResultDTO withResultLines(List<List<String>> lines);

    SelectResultDTO withResultType(int type);
}
