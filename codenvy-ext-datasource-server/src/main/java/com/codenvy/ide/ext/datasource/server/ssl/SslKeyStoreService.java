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
