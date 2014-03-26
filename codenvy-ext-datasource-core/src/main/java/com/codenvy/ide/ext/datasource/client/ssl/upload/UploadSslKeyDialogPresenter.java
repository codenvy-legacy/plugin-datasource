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
