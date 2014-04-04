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
package com.codenvy.ide.ext.datasource.client.newdatasource.connector.nuodb;

import java.util.Set;

import com.codenvy.ide.ext.datasource.client.newdatasource.connector.AbstractNewDatasourceConnectorView;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.ImplementedBy;

@ImplementedBy(NuoDBDatasourceConnectorViewImpl.class)
public interface NuoDBDatasourceConnectorView extends AbstractNewDatasourceConnectorView {

    /** Additional delegate methods. */
    public interface NuoActionDelegate {

        void onAddBroker();

        void onDeleteBrokers();
    }

    String getDatabaseName();

    void bindBrokerList(ListDataProvider<NuoDBBroker> dataProvider);

    Set<NuoDBBroker> getBrokerSelection();

    void setNuoDelegate(NuoActionDelegate delegate);

    void setDatabaseName(String databaseName);

    void setUsername(String username);

    void setPassword(String password);
}
