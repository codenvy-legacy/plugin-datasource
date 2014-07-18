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
package com.codenvy.ide.ext.datasource.server;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codenvy.dto.server.DtoFactory;
import com.codenvy.ide.ext.datasource.shared.ServicePaths;
import com.codenvy.ide.ext.datasource.shared.TextDTO;

@Path(ServicePaths.ENCRYPT_TEXT_PATH)
public class EncryptTextService {

    private static final byte[] SALT = {
                                     (byte)0xde, (byte)0x33, (byte)0x10, (byte)0x12,
                                     (byte)0xde, (byte)0x33, (byte)0x10, (byte)0x12,
                                     };
    /** The logger. */
    private static final Logger LOG  = LoggerFactory.getLogger(EncryptTextService.class);

    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public String encryptText(TextDTO textToEncrypt) throws Exception {
        String encryptedText = encrypt(textToEncrypt.getValue());
        TextDTO encryptedTextDTO = DtoFactory.getInstance().createDto(TextDTO.class).withValue(encryptedText);
        return DtoFactory.getInstance().toJson(encryptedTextDTO);
    }

    protected char[] getMasterPassword() {
        String masterPwd = System.getProperty("com.codenvy.security.masterpwd");
        if (masterPwd == null) {
            masterPwd = "changeMe";
        }
        return masterPwd.toCharArray();
    }

    public String encrypt(final String textToEncrypt) throws Exception {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(getMasterPassword()));
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
        String encryptedText = Base64.encodeBase64String(pbeCipher.doFinal(textToEncrypt.getBytes("UTF-8")));
        return encryptedText;
    }

    public String decryptText(String textToDecrypt) throws Exception {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(getMasterPassword()));
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
        return new String(pbeCipher.doFinal(Base64.decodeBase64(textToDecrypt)), "UTF-8");
    }

}
