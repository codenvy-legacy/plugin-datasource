package com.codenvy.ide.ext.datasource.server.ssl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class CodenvySSLSocketFactory extends SSLSocketFactory {

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
            // TODO log
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
            } catch (UnrecoverableKeyException uke) {

                // "Could not recover keys from client keystore.  Check password?",
                return (javax.net.ssl.SSLSocketFactory)javax.net.ssl.SSLSocketFactory
                                                                                     .getDefault();
            } catch (NoSuchAlgorithmException nsae) {
                // "Unsupported keystore algorithm [" + nsae.getMessage()
                return (javax.net.ssl.SSLSocketFactory)javax.net.ssl.SSLSocketFactory
                                                                                     .getDefault();
            } catch (KeyStoreException kse) {
                // "Could not create KeyStore instance ["
                // + kse.getMessage() + "]", SQL_STATE_BAD_SSL_PARAMS, 0, false,
                return (javax.net.ssl.SSLSocketFactory)javax.net.ssl.SSLSocketFactory
                                                                                     .getDefault();
            } catch (CertificateException nsae) {
                // throw SQLError.createSQLException("Could not load client"
                // + clientCertificateKeyStoreType + " keystore from "
                // + clientCertificateKeyStoreUrl, mysqlIO.getExceptionInterceptor());
                return (javax.net.ssl.SSLSocketFactory)javax.net.ssl.SSLSocketFactory
                                                                                     .getDefault();
            } catch (MalformedURLException mue) {
                // throw SQLError.createSQLException(clientCertificateKeyStoreUrl
                // + " does not appear to be a valid URL.", SQL_STATE_BAD_SSL_PARAMS, 0,
                // false, mysqlIO.getExceptionInterceptor());
                return (javax.net.ssl.SSLSocketFactory)javax.net.ssl.SSLSocketFactory
                                                                                     .getDefault();
            } catch (IOException ioe) {
                // SQLException sqlEx =
                // SQLError.createSQLException("Cannot open "
                // + clientCertificateKeyStoreUrl + " ["
                // + ioe.getMessage() + "]", SQL_STATE_BAD_SSL_PARAMS, 0, false,);

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
            } catch (NoSuchAlgorithmException nsae) {
                // "Unsupported keystore algorithm [" + nsae.getMessage()
                return (javax.net.ssl.SSLSocketFactory)javax.net.ssl.SSLSocketFactory
                                                                                     .getDefault();
            } catch (KeyStoreException kse) {
                // "Could not create KeyStore instance ["
                return (javax.net.ssl.SSLSocketFactory)javax.net.ssl.SSLSocketFactory
                                                                                     .getDefault();
            } catch (CertificateException nsae) {
                // throw SQLError.createSQLException("Could not load trust"
                // + trustCertificateKeyStoreType + " keystore from "
                // + trustCertificateKeyStoreUrl, SQL_STATE_BAD_SSL_PARAMS, 0, false,
                return (javax.net.ssl.SSLSocketFactory)javax.net.ssl.SSLSocketFactory
                                                                                     .getDefault();
            } catch (MalformedURLException mue) {
                // throw SQLError.createSQLException(trustCertificateKeyStoreUrl
                // + " does not appear to be a valid URL.", SQL_STATE_BAD_SSL_PARAMS, 0,
                return (javax.net.ssl.SSLSocketFactory)javax.net.ssl.SSLSocketFactory
                                                                                     .getDefault();
            } catch (IOException ioe) {
                // SQLError.createSQLException("Cannot open "
                // + trustCertificateKeyStoreUrl + " [" + ioe.getMessage()
                // + "]", SQL_STATE_BAD_SSL_PARAMS, 0, false,
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
                                    public void checkClientTrusted(X509Certificate[] chain,
                                                                   String authType) {
                                        // return without complaint
                                    }

                                    public void checkServerTrusted(X509Certificate[] chain,
                                                                   String authType) throws CertificateException {
                                        // return without complaint
                                    }

                                    public X509Certificate[] getAcceptedIssuers() {
                                        return null;
                                    }
                                }}, null);

            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException nsae) {
            // throw SQLError.createSQLException("TLS"
            // + " is not a valid SSL protocol.",
            return (javax.net.ssl.SSLSocketFactory)javax.net.ssl.SSLSocketFactory
                                                                                 .getDefault();
        } catch (KeyManagementException kme) {
            // throw SQLError.createSQLException("KeyManagementException: "
            return (javax.net.ssl.SSLSocketFactory)javax.net.ssl.SSLSocketFactory
                                                                                 .getDefault();
        }
    }

    public Socket createSocket() throws IOException {
        reloadIfNeeded();
        return wrappedSocketFactory.get().createSocket();
    }

    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        reloadIfNeeded();
        return wrappedSocketFactory.get().createSocket(host, port);
    }

    public String[] getDefaultCipherSuites() {
        reloadIfNeeded();
        return wrappedSocketFactory.get().getDefaultCipherSuites();
    }

    public String[] getSupportedCipherSuites() {
        reloadIfNeeded();
        return wrappedSocketFactory.get().getSupportedCipherSuites();
    }

    public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
        reloadIfNeeded();
        return wrappedSocketFactory.get().createSocket(s, host, port, autoClose);
    }

    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
        reloadIfNeeded();
        return wrappedSocketFactory.get().createSocket(host, port, localHost, localPort);
    }

    public Socket createSocket(InetAddress host, int port) throws IOException {
        reloadIfNeeded();
        return wrappedSocketFactory.get().createSocket(host, port);
    }

    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        reloadIfNeeded();
        return wrappedSocketFactory.get().createSocket(address, port, localAddress, localPort);
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || "".equals(s);
    }

}
