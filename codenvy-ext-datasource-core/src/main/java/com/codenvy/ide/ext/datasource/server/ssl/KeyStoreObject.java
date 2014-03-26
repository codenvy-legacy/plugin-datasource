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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codenvy.dto.server.DtoFactory;
import com.codenvy.ide.ext.datasource.shared.ssl.SslKeyStoreEntry;

/**
 * Jaxrs object for client Java SSL keystore. Access to key objects, List key objects and add new key objects.
 */
public class KeyStoreObject {

    private static final Logger LOG = LoggerFactory.getLogger(KeyStoreObject.class);

    protected String            keyStoreLocation;
    protected String            keyStorePassword;
    protected KeyStore          keystore;

    public KeyStoreObject() throws Exception {
        keyStoreLocation = getKeyStoreLocation();
        keyStorePassword = getKeyStorePassword();
        keystore = extractKeyStoreFromFile(keyStoreLocation, keyStorePassword);
    }

    protected String getKeyStorePassword() {
        String sPass = System.getProperty("javax.net.ssl.keyStorePassword");
        return sPass;
    }

    protected String getKeyStoreLocation() {
        String store = System.getProperty("javax.net.ssl.keyStore");
        return store;
    }

    protected KeyStore extractKeyStoreFromFile(String store, String sPass) throws Exception {
        KeyStore ks = KeyStore.getInstance("JKS");
        try (FileInputStream fis = new FileInputStream(store)) {
            ks.load(fis, sPass.toCharArray());
        } catch (FileNotFoundException e) {
            LOG.info("Couldn't find keystore file " + store);
            ks.load(null, sPass.toCharArray());
        }

        return ks;
    }

    @Path("{alias}")
    public KeyObject getKeyObject(@PathParam("alias") String alias) throws Exception {
        return new KeyObject(alias, keystore, keyStoreLocation, keyStorePassword);
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getKeyList(@Context UriInfo uriInfo) throws Exception {
        List<SslKeyStoreEntry> result = new ArrayList<SslKeyStoreEntry>(keystore.size());

        Enumeration<String> e = keystore.aliases();
        while (e.hasMoreElements()) {
            String alias = (String)e.nextElement();
            SslKeyStoreEntry sslKeyStoreEntry = DtoFactory.getInstance().createDto(SslKeyStoreEntry.class)
                                                          .withAlias(alias)
                                                          .withType(
                                                                    keystore.isKeyEntry(alias) ? "Key" : "Certificate"
                                                          );
            result.add(sslKeyStoreEntry);
        }
        return Response.ok().entity(result).type(MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("add")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    public Response addNewKeyCertificate(@QueryParam("alias") String alias,
                                         Iterator<FileItem> uploadedFilesIterator) throws Exception {
        PrivateKey privateKey = null;
        Certificate[] certs = null;
        while (uploadedFilesIterator.hasNext()) {
            FileItem fileItem = uploadedFilesIterator.next();
            if (!fileItem.isFormField()) {
                if ("keyFile".equals(fileItem.getFieldName())) {
                    KeyFactory kf = KeyFactory.getInstance("RSA");
                    privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(fileItem.get()));
                }
                if ("certFile".equals(fileItem.getFieldName())) {
                    CertificateFactory cf = CertificateFactory.getInstance("X.509");
                    certs = cf.generateCertificates(fileItem.getInputStream()).toArray(new Certificate[]{});
                }
            }
        }

        if (privateKey == null || certs == null) {
            throw new WebApplicationException(Response.ok("<pre>Can't find input file.</pre>", MediaType.TEXT_HTML).build());
        }

        keystore.setKeyEntry(alias, privateKey, keyStorePassword.toCharArray(), certs);
        keystore.store(new FileOutputStream(keyStoreLocation), keyStorePassword.toCharArray());

        return Response.ok("", MediaType.TEXT_HTML).build();
    }
}
