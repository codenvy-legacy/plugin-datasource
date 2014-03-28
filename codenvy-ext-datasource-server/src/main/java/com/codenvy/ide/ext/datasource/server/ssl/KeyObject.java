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
package com.codenvy.ide.ext.datasource.server.ssl;

import java.io.FileOutputStream;
import java.security.KeyStore;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 * Jaxrs client key object from java ssl keystore, provides view and delete action.
 */
public class KeyObject {

    protected String   alias;
    protected KeyStore keystore;
    protected String   keyStoreLocation;
    protected String   keyStorePassword;

    public KeyObject(String alias, KeyStore keystore, String keyStoreLocation, String keyStorePassword) {
        this.alias = alias;
        this.keystore = keystore;
        this.keyStoreLocation = keyStoreLocation;
        this.keyStorePassword = keyStorePassword;
    }

    @GET
    @Path("remove")
    public String deleteKeyWithGetMethod(@QueryParam("callback") String calback) throws Exception {
        return deleteKey(calback);
    }

    @DELETE
    public String deleteKey(@QueryParam("callback") String calback) throws Exception {
        keystore.deleteEntry(alias);
        keystore.store(new FileOutputStream(keyStoreLocation), keyStorePassword.toCharArray());
        return calback + "();";
    }
}
