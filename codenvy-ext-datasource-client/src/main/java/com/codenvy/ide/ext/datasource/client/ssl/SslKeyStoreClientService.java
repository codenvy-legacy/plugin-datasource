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

import com.codenvy.ide.collections.Array;
import com.codenvy.ide.ext.datasource.shared.ssl.SslKeyStoreEntry;
import com.codenvy.ide.rest.AsyncRequestCallback;

public interface SslKeyStoreClientService {

    void getAllClientKeys(AsyncRequestCallback<Array<SslKeyStoreEntry>> callback);

    void getAllServerCerts(AsyncRequestCallback<Array<SslKeyStoreEntry>> asyncRequestCallback);

    void deleteClientKey(SslKeyStoreEntry entry, AsyncRequestCallback<Void> asyncRequestCallback);

    String getUploadClientKeyAction(String alias);

    void deleteServerCert(SslKeyStoreEntry key, AsyncRequestCallback<Void> asyncRequestCallback);

    String getUploadServerCertAction(String alias);

    void init(AsyncRequestCallback<Void> asyncRequestCallback);


}
