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

package com.codenvy.ide.ext.datasource.client.common.messagewindow;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import com.codenvy.ide.ext.datasource.client.common.interaction.ConfirmCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * Message/information window {@link MessageWindow} implementation.
 * 
 * @author "Mickaël Leduque"
 */
public class MessageWindowPresenter implements MessageWindow, MessageWindowView.ActionDelegate {

    /** This component view. */
    private final MessageWindowView view;

    /** The callback used on OK. */
    private final ConfirmCallback   confirmCallback;

    @AssistedInject
    public MessageWindowPresenter(final @NotNull MessageWindowView view,
                                  final @NotNull @Assisted("title") String title,
                                  final @NotNull @Assisted("message") String message,
                                  final @Nullable @Assisted ConfirmCallback confirmCallback) {
        this(view, title, new Label(message), confirmCallback);
    }

    @AssistedInject
    public MessageWindowPresenter(final @NotNull MessageWindowView view,
                                  final @NotNull @Assisted String title,
                                  final @NotNull @Assisted IsWidget content,
                                  final @Nullable @Assisted ConfirmCallback confirmCallback) {
        this.view = view;
        this.view.setContent(content);
        this.view.setTitle(title);
        this.confirmCallback = confirmCallback;
        this.view.setDelegate(this);
    }

    @Override
    public void accepted() {
        this.view.closeDialog();
        if (this.confirmCallback != null) {
            this.confirmCallback.accepted();
        }
    }

    @Override
    public void inform() {
        this.view.showDialog();
    }
}
