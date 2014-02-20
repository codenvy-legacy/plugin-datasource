package com.codenvy.ide.ext.datasource.shared.request;

import com.codenvy.dto.shared.DTO;

@DTO
public interface UpdateResultDTO extends RequestResultDTO {

    static int TYPE = 2;

    UpdateResultDTO withUpdateCount(int count);

    UpdateResultDTO withResultType(int type);

    UpdateResultDTO withOriginRequest(String request);
}
