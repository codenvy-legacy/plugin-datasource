package com.codenvy.ide.ext.datasource.shared.ssl;

import com.codenvy.dto.shared.DTO;

@DTO
public interface SslKeyStoreEntry {
    String getAlias();

    String getType();

    SslKeyStoreEntry withAlias(String alias);

    SslKeyStoreEntry withType(String type);

    void setAlias(String alias);

    void setType(String type);

}
