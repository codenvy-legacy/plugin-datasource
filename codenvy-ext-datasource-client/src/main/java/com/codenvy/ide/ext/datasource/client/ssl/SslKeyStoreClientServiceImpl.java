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
package com.codenvy.ide.ext.datasource.client.ssl;

import javax.validation.constraints.NotNull;

import com.codenvy.ide.collections.Array;
import com.codenvy.ide.ext.datasource.shared.ssl.SslKeyStoreEntry;
import com.codenvy.ide.rest.AsyncRequestCallback;
import com.codenvy.ide.rest.AsyncRequestFactory;
import com.codenvy.ide.ui.loader.Loader;
import com.google.gwt.http.client.URL;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

@Singleton
public class SslKeyStoreClientServiceImpl implements SslKeyStoreClientService {

    private final String              baseUrl;
    private final Loader              loader;
    private final AsyncRequestFactory asyncRequestFactory;

    @Inject
    protected SslKeyStoreClientServiceImpl(@Named("restContext") String baseUrl,
                                           Loader loader,
                                           AsyncRequestFactory asyncRequestFactory) {
        this.baseUrl = baseUrl;
        this.loader = loader;
        this.asyncRequestFactory = asyncRequestFactory;
    }

    @Override
    public void init(AsyncRequestCallback<Void> callback) {
        loader.setMessage("Init SSL ....");
        loader.show();
        asyncRequestFactory.createGetRequest(baseUrl + "/ssl-keystore").send(callback);
    }

    /** {@inheritDoc} */
    @Override
    public void getAllClientKeys(@NotNull AsyncRequestCallback<Array<SslKeyStoreEntry>> callback) {
        loader.setMessage("Retrieving SSL Client keys....");
        loader.show();
        asyncRequestFactory.createGetRequest(baseUrl + "/ssl-keystore/keystore").send(callback);
    }

    @Override
    public void getAllServerCerts(AsyncRequestCallback<Array<SslKeyStoreEntry>> callback) {
        loader.setMessage("Retrieving SSL Server certs....");
        loader.show();
        asyncRequestFactory.createGetRequest(baseUrl + "/ssl-keystore/truststore").send(callback);
    }

    @Override
    public void deleteClientKey(SslKeyStoreEntry entry, AsyncRequestCallback<Void> callback) {
        loader.setMessage("Deleting SSL client key entries for " + entry.getAlias());
        loader.show();
        asyncRequestFactory.createGetRequest(baseUrl + "/ssl-keystore/keystore/"
                                             + URL.encode(entry.getAlias()) + "/remove").send(callback);
    }

    @Override
    public void deleteServerCert(SslKeyStoreEntry entry, AsyncRequestCallback<Void> callback) {
        loader.setMessage("Deleting SSL server cert entries for " + entry.getAlias());
        loader.show();
        asyncRequestFactory.createGetRequest(baseUrl + "/ssl-keystore/truststore/"
                                             + URL.encode(entry.getAlias()) + "/remove").send(callback);
    }

    @Override
    public String getUploadClientKeyAction(String alias) {
        return baseUrl + "/ssl-keystore/keystore/add?alias=" + alias;
    }

    @Override
    public String getUploadServerCertAction(String alias) {
        return baseUrl + "/ssl-keystore/truststore/add?alias=" + alias;
    }

}
