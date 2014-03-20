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

import com.codenvy.ide.collections.Array;
import com.codenvy.ide.ext.datasource.shared.ssl.SslKeyStoreEntry;
import com.codenvy.ide.rest.AsyncRequestCallback;

public interface SslKeyStoreClientService {

    void getAllKeys(AsyncRequestCallback<Array<SslKeyStoreEntry>> callback);

    void deleteKey(SslKeyStoreEntry entry, AsyncRequestCallback<Void> asyncRequestCallback);

    String getUploadClientKeyAction(String alias);

}
