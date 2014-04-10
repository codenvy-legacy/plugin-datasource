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
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Iterator;

import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.fileupload.FileItem;

/**
 * The trustStore is pretty similar to the keystore except that - it doesn't take key
 */
public class TrustStoreObject extends KeyStoreObject {

    public TrustStoreObject() throws Exception {
        super();
    }

    protected String getKeyStorePassword() {
        String sPass = System.getProperty("javax.net.ssl.trustStorePassword");
        return sPass;
    }

    protected String getKeyStoreLocation() {
        String store = System.getProperty("javax.net.ssl.trustStore");
        return store;
    }

    @Override
    public Response addNewKeyCertificate(@QueryParam("alias") String alias,
                                         Iterator<FileItem> uploadedFilesIterator) throws Exception {
        Certificate[] certs = null;
        while (uploadedFilesIterator.hasNext()) {
            FileItem fileItem = uploadedFilesIterator.next();
            if (!fileItem.isFormField()) {
                if ("certFile".equals(fileItem.getFieldName())) {
                    CertificateFactory cf = CertificateFactory.getInstance("X.509");
                    certs = cf.generateCertificates(fileItem.getInputStream()).toArray(new Certificate[]{});
                }
            }
        }

        if (certs == null) {
            throw new WebApplicationException(Response.ok("<pre>Can't find input file.</pre>", MediaType.TEXT_HTML).build());
        }

        keystore.setCertificateEntry(alias, certs[0]);
        keystore.store(new FileOutputStream(keyStoreLocation), keyStorePassword.toCharArray());

        return Response.ok("", MediaType.TEXT_HTML).build();
    }
}
