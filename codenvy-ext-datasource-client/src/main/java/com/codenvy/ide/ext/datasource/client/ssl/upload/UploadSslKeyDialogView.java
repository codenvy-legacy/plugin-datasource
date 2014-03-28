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

import com.codenvy.ide.api.mvp.View;

import javax.validation.constraints.NotNull;

public interface UploadSslKeyDialogView extends View<UploadSslKeyDialogView.ActionDelegate> {
    public interface ActionDelegate {
        void onCancelClicked();

        void onUploadClicked();

        void onSubmitComplete(@NotNull String result);

        void onFileNameChanged();
    }

    @NotNull
    String getAlias();

    void setHost(@NotNull String host);

    @NotNull
    String getCertFileName();
    
    @NotNull
    String getKeyFileName();

    void setEnabledUploadButton(boolean enabled);

    void setMessage(@NotNull String message);

    void setEncoding(@NotNull String encodingType);

    void setAction(@NotNull String url);

    void submit();

    void showDialog();

    void close();
}
