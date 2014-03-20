/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
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
package com.codenvy.ide.ext.datasource.server.ssl;

import javax.ws.rs.Path;

/**
 * JaxRS service that gives access to Java SSL KeyStore.
 */
@Path("ssl-keystore")
public class SslKeyStoreService {

    @Path("keystore")
    public KeyStoreObject getClientKeyStore() throws Exception {
        return new KeyStoreObject();
    }

    // @Path("truststore")
    // public Object getTrustStore(){
    // return new TrustStoreObject();
    // }
}
