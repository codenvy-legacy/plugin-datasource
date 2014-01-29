package com.codenvy.ide.ext.datasource.shared.request;

import com.codenvy.dto.shared.DTO;

@DTO
public interface UpdateResultDTO extends RequestResultDTO {

    static int TYPE = 2;

    int getUpdateCount();

    void setUpdateCount(int count);

    UpdateResultDTO withUpdateCount(int count);

    UpdateResultDTO withResultType(int type);
}
