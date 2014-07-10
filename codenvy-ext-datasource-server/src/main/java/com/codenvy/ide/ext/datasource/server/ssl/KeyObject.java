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
