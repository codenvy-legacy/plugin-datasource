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
