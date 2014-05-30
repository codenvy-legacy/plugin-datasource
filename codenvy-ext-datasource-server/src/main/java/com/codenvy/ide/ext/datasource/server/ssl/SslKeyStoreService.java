/*******************************************************************************
* Copyright (c) 2012-2014 Codenvy, S.A.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* Codenvy, S.A. - initial API and implementation
*******************************************************************************/

package com.codenvy.ide.ext.datasource.server.ssl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * JaxRS service that gives access to Java SSL KeyStore.
 */
@Path("ssl-keystore")
public class SslKeyStoreService {

    @GET
    public String init(){
        // temporary disabling to allow https if truststore and keystore are not setted
//        if (System.getProperty("javax.net.ssl.trustStore") == null) {
//            System.setProperty("javax.net.ssl.trustStore", System.getProperty("catalina.base") + "/truststore");
//        }
//        if (System.getProperty("javax.net.ssl.trustStorePassword") == null) {
//            System.setProperty("javax.net.ssl.trustStorePassword", "changeMe");
//        }
//        if (System.getProperty("javax.net.ssl.keyStore") == null) {
//            System.setProperty("javax.net.ssl.keyStore", System.getProperty("catalina.base") + "/keystore");
//        }
//        if (System.getProperty("javax.net.ssl.keyStorePassword") == null) {
//            System.setProperty("javax.net.ssl.keyStorePassword", "changeMe");
//        }
        return "ok";
    }

    @Path("keystore")
    public KeyStoreObject getClientKeyStore() throws Exception {
        return new KeyStoreObject();
    }

    @Path("truststore")
    public Object getTrustStore() throws Exception {
        return new TrustStoreObject();
    }
}
