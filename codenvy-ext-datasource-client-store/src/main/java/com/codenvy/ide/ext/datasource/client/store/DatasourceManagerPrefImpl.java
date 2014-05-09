/*
 * Copyright 2014 Codenvy, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codenvy.ide.ext.datasource.client.store;

import java.util.Iterator;
import java.util.Set;

import com.codenvy.api.user.shared.dto.Profile;
import com.codenvy.ide.api.preferences.PreferencesManager;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.DatasourceConfigPreferences;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DatasourceManagerPrefImpl implements DatasourceManager {

    private static final String PREFERENCE_KEY = "datasources";

    private PreferencesManager  preferencesManager;

    private DtoFactory          dtoFactory;

    @Inject
    public DatasourceManagerPrefImpl(final PreferencesManager prefManager,
                                     final DtoFactory dtoFactory) {
        this.preferencesManager = prefManager;
        this.dtoFactory = dtoFactory;
    }

    @Override
    public Iterator<DatabaseConfigurationDTO> getDatasources() {
        return getDatasourceConfigPreferences().getDatasources().values().iterator();
    }

    @Override
    public void add(final DatabaseConfigurationDTO configuration) {
        DatasourceConfigPreferences datasourcesPreferences = getDatasourceConfigPreferences();
        datasourcesPreferences.getDatasources().put(configuration.getDatasourceId(), configuration);
        saveDatasourceConfigPreferences(datasourcesPreferences);
    }

    @Override
    public void remove(final DatabaseConfigurationDTO configuration) {
        DatasourceConfigPreferences datasourcesPreferences = getDatasourceConfigPreferences();
        datasourcesPreferences.getDatasources().remove(configuration.getDatasourceId());
        saveDatasourceConfigPreferences(datasourcesPreferences);
    }

    @Override
    public DatabaseConfigurationDTO getByName(final String name) {
        DatasourceConfigPreferences datasourcesPreferences = getDatasourceConfigPreferences();
        return datasourcesPreferences.getDatasources().get(name);
    }

    @Override
    public void persist(final AsyncCallback<Profile> callback) {
        this.preferencesManager.flushPreferences(callback);
    }

    public Set<String> getNames() {
        DatasourceConfigPreferences datasourcesPreferences = getDatasourceConfigPreferences();
        return datasourcesPreferences.getDatasources().keySet();
    }

    private DatasourceConfigPreferences getDatasourceConfigPreferences() {
        String datasourcesJson = this.preferencesManager.getValue(PREFERENCE_KEY);
        DatasourceConfigPreferences datasourcesPreferences;
        if (datasourcesJson == null) {
            datasourcesPreferences = dtoFactory.createDto(DatasourceConfigPreferences.class);
        } else {
            datasourcesPreferences =
                                     dtoFactory.createDtoFromJson(datasourcesJson,
                                                                  DatasourceConfigPreferences.class);
        }
        return datasourcesPreferences;
    }

    private void saveDatasourceConfigPreferences(final DatasourceConfigPreferences datasourcesPreferences) {
        preferencesManager.setPreference(PREFERENCE_KEY, dtoFactory.toJson(datasourcesPreferences));
    }

    public String toString() {
        return "DatasourceManagerPrefImpl[" + preferencesManager.getValue(PREFERENCE_KEY) + "]";
    }

    @Override
    public Iterator<DatabaseConfigurationDTO> iterator() {
        return getDatasources();
    }
}
