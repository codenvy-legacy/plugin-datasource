package com.codenvy.ide.ext.datasource.client;

import java.util.Iterator;
import java.util.Set;

import com.codenvy.api.user.shared.dto.Profile;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DatasourceManager {

    Iterator<DatabaseConfigurationDTO> getDatasources();

    void add(final DatabaseConfigurationDTO configuration);

    public void remove(final DatabaseConfigurationDTO configuration);

    public DatabaseConfigurationDTO getByName(final String name);

    public Set<String> getNames();

    public void persist(AsyncCallback<Profile> callback);
}
