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

import com.codenvy.ide.api.extension.Extension;
import com.codenvy.ide.rest.AsyncRequestCallback;
import com.codenvy.ide.util.loging.Log;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
@Extension(title = "SSL Extension", version = "1.0.0")
public class SslExtension {

    @Inject
    public SslExtension(SslKeyStoreClientService sslKeyStoreClientService) {
        Log.info(SslExtension.class, "Init SSL callback");
        sslKeyStoreClientService.init(new AsyncRequestCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                Log.info(SslExtension.class, "Succeeded SSL init");
            }

            @Override
            public void onFailure(Throwable exception) {
                Log.error(SslExtension.class, "Failed tnit SSL callback", exception);
            }
        });
    }
}
