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

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CodenvySSLSocketFactory extends SSLSocketFactory {
    private static final Logger             LOG                               = LoggerFactory.getLogger(CodenvySSLSocketFactory.class);

    public static ThreadLocal<Boolean>      init                              = new ThreadLocal<Boolean>();
    public static ThreadLocal<String>       clientCertificateKeyStoreUrl      = new ThreadLocal<String>();
    public static ThreadLocal<String>       trustCertificateKeyStoreUrl       = new ThreadLocal<String>();
    public static ThreadLocal<String>       clientCertificateKeyStorePassword = new ThreadLocal<String>();
    public static ThreadLocal<String>       trustCertificateKeyStorePassword  = new ThreadLocal<String>();
    public static ThreadLocal<String>       trustCertificateKeyStoreType      = new ThreadLocal<String>();
    public static ThreadLocal<String>       clientCertificateKeyStoreType     = new ThreadLocal<String>();
    public static ThreadLocal<Boolean>      verifyServerCertificate           = new ThreadLocal<Boolean>();


    protected ThreadLocal<SSLSocketFactory> wrappedSocketFactory              = new ThreadLocal<SSLSocketFactory>();

    protected void reloadIfNeeded() {
        if (init.get() != null && init.get()) {
            init.set(false);
            wrappedSocketFactory.set(getSSLSocketFactoryDefaultOrConfigured());
        }
    }

    protected static SSLSocketFactory getSSLSocketFactoryDefaultOrConfigured() {
        if (isNullOrEmpty(clientCertificateKeyStoreUrl.get())
            && isNullOrEmpty(trustCertificateKeyStoreUrl.get())) {
            if (verifyServerCertificate.get()) {
                return (javax.net.ssl.SSLSocketFactory)javax.net.ssl.SSLSocketFactory.getDefault();
            }
        }

        TrustManagerFactory tmf = null;
        KeyManagerFactory kmf = null;

        try {
            tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        } catch (NoSuchAlgorithmException nsae) {
            LOG.error("An error occured while setting up custom SSL Socket factory. Falling back the the default one", nsae);
            return (javax.net.ssl.SSLSocketFactory)javax.net.ssl.SSLSocketFactory.getDefault();
        }

        if (!isNullOrEmpty(clientCertificateKeyStoreUrl.get())) {
            try {
                if (!isNullOrEmpty(clientCertificateKeyStoreType.get())) {
                    KeyStore clientKeyStore = KeyStore
                                                      .getInstance(clientCertificateKeyStoreType.get());
                    URL ksURL = new URL(clientCertificateKeyStoreUrl.get());
                    char[] password = (clientCertificateKeyStorePassword.get() == null) ? new char[0]
                        : clientCertificateKeyStorePassword.get().toCharArray();
                    clientKeyStore.load(ksURL.openStream(), password);
                    kmf.init(clientKeyStore, password);
                }
            } catch (Exception e) {
                LOG.error("An error occured while setting up custom SSL Socket factory. Falling back the the default one", e);
                return (javax.net.ssl.SSLSocketFactory)javax.net.ssl.SSLSocketFactory
                                                                                     .getDefault();
            }
        }

        if (!isNullOrEmpty(trustCertificateKeyStoreUrl.get())) {

            try {
                if (!isNullOrEmpty(trustCertificateKeyStoreType.get())) {
                    KeyStore trustKeyStore = KeyStore
                                                     .getInstance(trustCertificateKeyStoreType.get());
                    URL ksURL = new URL(trustCertificateKeyStoreUrl.get());

                    char[] password = (trustCertificateKeyStorePassword.get() == null) ? new char[0]
                        : trustCertificateKeyStorePassword.get().toCharArray();
                    trustKeyStore.load(ksURL.openStream(), password);
                    tmf.init(trustKeyStore);
                }
            } catch (Exception e) {
                LOG.error("An error occured while setting up custom SSL Socket factory. Falling back the the default one", e);
                return (javax.net.ssl.SSLSocketFactory)javax.net.ssl.SSLSocketFactory
                                                                                     .getDefault();
            }
        }

        SSLContext sslContext = null;

        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(isNullOrEmpty(clientCertificateKeyStoreUrl.get()) ? null : kmf.getKeyManagers(),
                            verifyServerCertificate.get() ? tmf.getTrustManagers()
                                : new X509TrustManager[]{new X509TrustManager() {
                                    @Override
                                    public void checkClientTrusted(X509Certificate[] chain,
                                                                   String authType) {
                                        // return without complaint
                                    }

                                    @Override
                                    public void checkServerTrusted(X509Certificate[] chain,
                                                                   String authType) throws CertificateException {
                                        // return without complaint
                                    }

                                    @Override
                                    public X509Certificate[] getAcceptedIssuers() {
                                        return null;
                                    }
                                }}, null);

            return sslContext.getSocketFactory();
        } catch (Exception e) {
            LOG.error("An error occured while setting up custom SSL Socket factory. Falling back the the default one", e);
            return (javax.net.ssl.SSLSocketFactory)javax.net.ssl.SSLSocketFactory
                                                                                 .getDefault();
        }
    }

    @Override
    public Socket createSocket() throws IOException {
        reloadIfNeeded();
        return wrappedSocketFactory.get().createSocket();
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        reloadIfNeeded();
        return wrappedSocketFactory.get().createSocket(host, port);
    }

    @Override
    public String[] getDefaultCipherSuites() {
        reloadIfNeeded();
        return wrappedSocketFactory.get().getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        reloadIfNeeded();
        return wrappedSocketFactory.get().getSupportedCipherSuites();
    }

    @Override
    public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
        reloadIfNeeded();
        return wrappedSocketFactory.get().createSocket(s, host, port, autoClose);
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
        reloadIfNeeded();
        return wrappedSocketFactory.get().createSocket(host, port, localHost, localPort);
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        reloadIfNeeded();
        return wrappedSocketFactory.get().createSocket(host, port);
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        reloadIfNeeded();
        return wrappedSocketFactory.get().createSocket(address, port, localAddress, localPort);
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || "".equals(s);
    }

}
