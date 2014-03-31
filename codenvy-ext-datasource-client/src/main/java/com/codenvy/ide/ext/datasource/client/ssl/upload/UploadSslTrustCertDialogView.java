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

import javax.validation.constraints.NotNull;

import com.codenvy.ide.api.mvp.View;

public interface UploadSslTrustCertDialogView extends View<UploadSslTrustCertDialogView.ActionDelegate> {
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

    void setEnabledUploadButton(boolean enabled);

    void setMessage(@NotNull String message);

    void setEncoding(@NotNull String encodingType);

    void setAction(@NotNull String url);

    void submit();

    void showDialog();

    void close();
}
