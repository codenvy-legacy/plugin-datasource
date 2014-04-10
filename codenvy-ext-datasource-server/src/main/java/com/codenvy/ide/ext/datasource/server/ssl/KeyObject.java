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
