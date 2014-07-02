/*******************************************************************************
 * Copyright (c) 2012-2014 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.ide.ext.datasource.client.ssl;

import javax.validation.constraints.NotNull;

import com.codenvy.ide.collections.Array;
import com.codenvy.ide.ext.datasource.client.inject.DatasourceGinModule;
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
    protected SslKeyStoreClientServiceImpl(@Named(DatasourceGinModule.DATASOURCE_CONTEXT_NAME) String baseUrl,
                                           Loader loader,
                                           AsyncRequestFactory asyncRequestFactory) {
        this.baseUrl = baseUrl;
        this.loader = loader;
        this.asyncRequestFactory = asyncRequestFactory;
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
