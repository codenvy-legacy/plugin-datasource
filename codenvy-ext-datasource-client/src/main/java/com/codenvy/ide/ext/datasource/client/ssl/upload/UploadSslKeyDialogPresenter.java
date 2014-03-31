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
package com.codenvy.ide.ext.datasource.client.ssl.upload;

import static com.codenvy.ide.api.notification.Notification.Type.ERROR;

import javax.validation.constraints.NotNull;

import com.codenvy.ide.api.notification.Notification;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.parts.ConsolePart;
import com.codenvy.ide.ext.datasource.client.ssl.SslKeyStoreClientService;
import com.codenvy.ide.ext.datasource.client.ssl.SslMessages;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class UploadSslKeyDialogPresenter implements UploadSslKeyDialogView.ActionDelegate {
    private UploadSslKeyDialogView   view;
    private SslMessages              constant;
    private ConsolePart              console;
    private NotificationManager      notificationManager;
    private AsyncCallback<Void>      callback;
    private SslKeyStoreClientService sslKeyStoreService;

    @Inject
    public UploadSslKeyDialogPresenter(UploadSslKeyDialogView view,
                                       SslMessages constant,
                                       ConsolePart console,
                                       SslKeyStoreClientService sslKeyStoreService,
                                       NotificationManager notificationManager) {
        this.view = view;
        this.sslKeyStoreService = sslKeyStoreService;
        this.view.setDelegate(this);
        this.constant = constant;
        this.console = console;
        this.notificationManager = notificationManager;
    }

    public void showDialog(@NotNull AsyncCallback<Void> callback) {
        this.callback = callback;
        view.setMessage("");
        view.setHost("");
        view.setEnabledUploadButton(false);
        view.showDialog();
    }

    @Override
    public void onCancelClicked() {
        view.close();
    }

    @Override
    public void onUploadClicked() {
        String alias = view.getAlias();
        if (alias.isEmpty()) {
            view.setMessage(constant.aliasValidationError());
            return;
        }
        view.setEncoding(FormPanel.ENCODING_MULTIPART);
        view.setAction(sslKeyStoreService.getUploadClientKeyAction(alias));
        view.submit();
    }

    @Override
    public void onSubmitComplete(@NotNull String result) {
        if (result.isEmpty()) {
            UploadSslKeyDialogPresenter.this.view.close();
            callback.onSuccess(null);
        } else {
            if (result.startsWith("<pre>") && result.endsWith("</pre>")) {
                result = result.substring(5, (result.length() - 6));
            }
            console.print(result);
            Notification notification = new Notification(result, ERROR);
            notificationManager.showNotification(notification);
            callback.onFailure(new Throwable(result));
        }
    }

    @Override
    public void onFileNameChanged() {
        String certFileName = view.getCertFileName();
        String keyFileName = view.getKeyFileName();
        view.setEnabledUploadButton(!certFileName.isEmpty() && !keyFileName.isEmpty());
    }
}
