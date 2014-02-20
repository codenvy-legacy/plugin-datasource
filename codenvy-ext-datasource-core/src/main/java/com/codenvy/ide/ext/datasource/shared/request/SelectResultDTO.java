package com.codenvy.ide.ext.datasource.shared.request;

import java.util.List;

import com.codenvy.dto.shared.DTO;

@DTO
public interface SelectResultDTO extends RequestResultDTO {

    static int TYPE = 1;

    SelectResultDTO withHeaderLine(List<String> line);

    SelectResultDTO withResultLines(List<List<String>> lines);

    SelectResultDTO withResultType(int type);

    SelectResultDTO withOriginRequest(String request);
}
