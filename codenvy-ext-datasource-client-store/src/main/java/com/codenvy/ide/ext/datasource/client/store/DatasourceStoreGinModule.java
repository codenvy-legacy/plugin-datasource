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

import com.codenvy.ide.api.extension.ExtensionGinModule;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.multibindings.GinMultibinder;
import com.google.inject.Singleton;

@ExtensionGinModule
public class DatasourceStoreGinModule extends AbstractGinModule {


    @Override
    protected void configure() {
        // bind the datasource manager and the datasource metadat store
        bind(DatasourceManager.class).to(DatasourceManagerPrefImpl.class).in(Singleton.class);
        bind(DatabaseInfoStore.class).to(DatabaseInfoStoreImpl.class);
        bind(DatabaseInfoOracle.class).to(DatabaseInfoOracleImpl.class);

        GinMultibinder<PreStoreProcessor> preStoreProcessorBinder = GinMultibinder.newSetBinder(binder(), PreStoreProcessor.class);
        preStoreProcessorBinder.addBinding().to(SortMetadataProcessor.class);
    }
}
