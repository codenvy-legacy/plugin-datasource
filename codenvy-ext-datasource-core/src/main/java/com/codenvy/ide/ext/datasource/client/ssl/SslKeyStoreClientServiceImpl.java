/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2014] Codenvy, S.A.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
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

    /** {@inheritDoc} */
    @Override
    public void getAllKeys(@NotNull AsyncRequestCallback<Array<SslKeyStoreEntry>> callback) {
        loader.setMessage("Retrieving SSL keys....");
        loader.show();
        asyncRequestFactory.createGetRequest(baseUrl + "/ssl-keystore/keystore").send(callback);
    }

    @Override
    public void deleteKey(SslKeyStoreEntry entry, AsyncRequestCallback<Void> callback) {
        loader.setMessage("Deleting SSL key entries for " + entry.getAlias());
        loader.show();
        asyncRequestFactory.createGetRequest(baseUrl + "/ssl-keystore/keystore/"
                                             + URL.encode(entry.getAlias()) + "/remove").send(callback);
    }

    @Override
    public String getUploadClientKeyAction(String alias) {
        return baseUrl + "/ssl-keystore/keystore/add?alias=" + alias;
    }
}
