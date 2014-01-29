package com.codenvy.ide.ext.datasource.shared.request;

import com.codenvy.dto.shared.DTO;

@DTO
public interface RequestResultDTO {

    int getResultType();

    void setResultType(int type);

    RequestResultDTO withResultType(int type);
}
