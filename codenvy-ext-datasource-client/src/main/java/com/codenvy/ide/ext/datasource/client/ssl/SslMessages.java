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
package com.codenvy.ide.ext.datasource.client.ssl;

import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;
import com.google.gwt.i18n.client.Messages;

@DefaultLocale("en")
public interface SslMessages extends Messages {
    @DefaultMessage("Cancel")
    String cancelButton();

    @DefaultMessage("Upload")
    String uploadButton();

    @DefaultMessage("Key/Cert files")
    String fileNameFieldTitle();

    @DefaultMessage("Alias")
    String keyAlias();

    @DefaultMessage("Alias can not be empty")
    String aliasValidationError();

    @DefaultMessage("Upload Private Key")
    String uploadClientSslKey();

    @DefaultMessage("Upload Trust Certificate")
    String uploadServerSslCert();

    @DefaultMessage("SSL Keystore")
    String sslManagerTitle();

    @DefaultMessage("Do you want to delete ssh keys for <b>{0}</b>")
    String deleteSslKeyQuestion(String alias);

    @DefaultMessage("Upload SSL client key")
    String dialogUploadSslKeyTitle();

    @DefaultMessage("Upload SSL server trust certificate")
    String dialogUploadSslTrustCertTitle();

    @DefaultMessage("SSL Trust Certificates")
    String headerTrustList();

    @DefaultMessage("SSL Private Key Store")
    String headerKeyList();
}
