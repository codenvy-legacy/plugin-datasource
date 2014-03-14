/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2013] - [2014] Codenvy, S.A.
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
package com.codenvy.ide.ext.datasource.server;

import static org.mockito.Mockito.when;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;
import java.security.cert.Certificate;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseType;

/**
 * Test the datasource service getDatabase() method that is used to retrieve a database catalog information. Tests are ignored as they needs
 * exiting and running database. Customize the database configuration with yours for testing.
 */
@RunWith(MockitoJUnitRunner.class)
public class TestDatasourceServiceDatabaseDTO {
    @Mock
    protected DatabaseConfigurationDTO databaseConfig;

    @Ignore
    @Test
    public void testPostgresDTOgeneration() throws Exception {
        when(databaseConfig.getDatabaseType()).thenReturn(DatabaseType.POSTGRES);
        when(databaseConfig.getDatabaseName()).thenReturn("wafa");
        when(databaseConfig.getHostname()).thenReturn("localhost");
        when(databaseConfig.getPort()).thenReturn(5432);
        when(databaseConfig.getUsername()).thenReturn("postgres");
        when(databaseConfig.getPassword()).thenReturn("nuxeospirit");

        String json = getDatabaseJsonDTOFromDatasourceService(databaseConfig);
        System.out.println(json);
        Assert.assertNotNull(json);
        Assert.assertTrue(json.contains("\"databaseProductName\":\"PostgreSQL\""));
    }

    protected String getDatabaseJsonDTOFromDatasourceService(DatabaseConfigurationDTO databaseConfig) throws Exception {
        DatasourceService dsService = new DatasourceService(new JdbcUrlBuilder(), new SqlRequestService());
        return dsService.getDatabase(databaseConfig);
    }

    @Ignore
    @Test
    public void testMySqlDTOgeneration() throws Exception {
        when(databaseConfig.getDatabaseType()).thenReturn(DatabaseType.MYSQL);
        when(databaseConfig.getDatabaseName()).thenReturn("aucoffre_db");
        when(databaseConfig.getHostname()).thenReturn("localhost");
        when(databaseConfig.getPort()).thenReturn(3306);
        when(databaseConfig.getUsername()).thenReturn("root");
        when(databaseConfig.getPassword()).thenReturn("selucreh");

        String json = getDatabaseJsonDTOFromDatasourceService(databaseConfig);
        System.out.println(json);
        Assert.assertNotNull(json);
        Assert.assertTrue(json.contains("\"databaseProductName\":\"MySQL\""));
    }

    @Test
    public void testMySqlDTOgenerationWithSSL() throws Exception {
        when(databaseConfig.getDatabaseType()).thenReturn(DatabaseType.MYSQL);
        when(databaseConfig.getDatabaseName()).thenReturn("");
        when(databaseConfig.getPort()).thenReturn(3306);
        when(databaseConfig.getUsername()).thenReturn("root");
        when(databaseConfig.getPassword()).thenReturn("serlii");
        when(databaseConfig.getUseSSL()).thenReturn(true);
        when(databaseConfig.getVerifyServerCertificate()).thenReturn(true);
        when(databaseConfig.getRequireSSL()).thenReturn(false);
        when(databaseConfig.getHostname()).thenReturn("173.194.83.207");
        // when(databaseConfig.getHostname()).thenReturn("173.194.84.213");


        // from Google SQL Cloud cert files:
        // keytool -import -alias mysqlServerCACert -file server-ca.pem -keystore truststore
        // openssl pkcs12 -export -in client-cert.pem -inkey client-key.pem -certfile client-cert.pem -name "Name" -out client.p12
        // keytool -importkeystore -srckeystore client.p12 -srcstoretype pkcs12 -destkeystore keystore

        System.setProperty("javax.net.ssl.trustStore", "/home/sunix/ssl/instance1/truststore");
        System.setProperty("javax.net.ssl.trustStorePassword", "kafpass");
        System.setProperty("javax.net.ssl.keyStore", "/home/sunix/ssl/instance1/keystore");
        System.setProperty("javax.net.ssl.keyStorePassword", "kafpass");

        String store = System.getProperty("javax.net.ssl.keyStore");
        String sPass = System.getProperty("javax.net.ssl.keyStorePassword");
        // KeyStore ks = KeyStore.getInstance(Security.getp)

        // display the keystore values
        KeyStore ks = KeyStore.getInstance("JKS");
        System.out.println();
        System.out.println("KeyStore Object Info: ");
        System.out.println("Type = " + ks.getType());
        System.out.println("Provider = " + ks.getProvider());
        System.out.println("toString = " + ks.toString());

        FileInputStream fis = new FileInputStream(store);
        ks.load(fis, sPass.toCharArray());
        fis.close();

        System.out.println();
        System.out.println("KeyStore Content: ");
        System.out.println("Size = " + ks.size());
        Enumeration<String> e = ks.aliases();
        while (e.hasMoreElements()) {
            String name = (String)e.nextElement();
            System.out.print("Entry:   " + name + ": ");
            if (ks.isKeyEntry(name))
                System.out.println(" Key entry");
            else
                System.out.println(" Certificate entry");

            Certificate[] certificates = ks.getCertificateChain(name);
            for (int i = 0; i < certificates.length; i++) {
                System.out.println("     certificateChainType: " + certificates[i].getType());
                System.out.println("     public key algo: " + certificates[i].getPublicKey().getAlgorithm());
                
            }

        }


        /* Read private key */
        // openssl pkcs8 -topk8 -nocrypt -in client-key1.pem -out client-key1.cert -outform der
        String keyfile = "/home/sunix/ssl/instance1/client-key1.cert";
        String certfile = "/home/sunix/ssl/instance1/client-cert1.pem";
        InputStream fl = new FileInputStream(keyfile);
        byte[] key = new byte[fl.available()];
        fl.read(key, 0, fl.available());
        fl.close();
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(key));

        /* generate certificate entry */
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate[] certs = cf.generateCertificates(new FileInputStream(certfile)).toArray(new Certificate[] {});
        
        /* load keystore, store as new keys */
        ks = KeyStore.getInstance("JKS", "SUN");
        ks.load(new FileInputStream(store), sPass.toCharArray());
        ks.setKeyEntry("yes", privateKey, sPass.toCharArray(), certs);
        ks.store(new FileOutputStream(store), sPass.toCharArray());

        
        
        String json = getDatabaseJsonDTOFromDatasourceService(databaseConfig);
        System.out.println(json);
        Assert.assertNotNull(json);
        Assert.assertTrue(json.contains("\"databaseProductName\":\"MySQL\""));
    }


    @Ignore
    @Test
    public void testOracleDTOgeneration() throws Exception {
        when(databaseConfig.getDatabaseType()).thenReturn(DatabaseType.ORACLE);
        when(databaseConfig.getDatabaseName()).thenReturn("xe");
        when(databaseConfig.getHostname()).thenReturn("192.168.86.191");
        when(databaseConfig.getPort()).thenReturn(1521);
        when(databaseConfig.getUsername()).thenReturn("admin");
        when(databaseConfig.getPassword()).thenReturn("admin");

        String json = getDatabaseJsonDTOFromDatasourceService(databaseConfig);
        System.out.println(json);
        Assert.assertNotNull(json);
        Assert.assertTrue(json.contains("\"databaseProductName\":\"Oracle\""));
    }

    @Ignore
    @Test
    public void testSqlserverDTOgeneration() throws Exception {
        when(databaseConfig.getDatabaseType()).thenReturn(DatabaseType.JTDS);
        when(databaseConfig.getDatabaseName()).thenReturn("master");
        when(databaseConfig.getHostname()).thenReturn("192.168.56.101");
        when(databaseConfig.getPort()).thenReturn(1433);
        when(databaseConfig.getUsername()).thenReturn("sa");
        when(databaseConfig.getPassword()).thenReturn("admin");

        String json = getDatabaseJsonDTOFromDatasourceService(databaseConfig);
        System.out.println(json);
        Assert.assertNotNull(json);
        Assert.assertTrue(json.contains("\"databaseProductName\":\"Microsoft SQL Server\""));
    }

}
