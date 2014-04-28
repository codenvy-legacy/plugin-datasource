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

package com.codenvy.ide.ext.datasource.client.common.interaction;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import com.codenvy.ide.ext.datasource.client.common.confirmwindow.CancelCallback;
import com.codenvy.ide.ext.datasource.client.common.confirmwindow.ConfirmCallback;
import com.codenvy.ide.ext.datasource.client.common.confirmwindow.ConfirmWindow;
import com.codenvy.ide.ext.datasource.client.common.messagewindow.MessageWindow;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.assistedinject.Assisted;

/**
 * Factory for {@link MessageWindow} and {@link ConfirmWindow} components.
 * 
 * @author "Mickaël Leduque"
 */
public interface DialogFactory {

    /**
     * Create a confirm window with only text as content.
     * 
     * @param title the window title
     * @param content the window content/text
     * @param confirmCallback the callback used on OK
     * @param cancelCallback the callback used on cancel
     * @return a {@link ConfirmWindow} instance
     */
    ConfirmWindow createConfirmWindow(@NotNull @Assisted("title") String title,
                                      @NotNull @Assisted("message") String content,
                                      @Nullable ConfirmCallback confirmCallback,
                                      @Nullable CancelCallback cancelCallback);

    /**
     * Create a confirm window with a widget as content.
     * 
     * @param title the window title
     * @param content the window content
     * @param confirmCallback the callback used on OK
     * @param cancelCallback the callback used on cancel
     * @return a {@link ConfirmWindow} instance
     */
    ConfirmWindow createConfirmWindow(@NotNull String title,
                                      @NotNull IsWidget content,
                                      @Nullable ConfirmCallback confirmCallback,
                                      @Nullable CancelCallback cancelCallback);

    /**
     * Create a message window with only text as content.
     * 
     * @param title the window title
     * @param content the window content/text
     * @param confirmCallback the callback used on OK
     * @return a {@link ConfirmWindow} instance
     */
    MessageWindow createMessageWindow(@NotNull @Assisted("title") String title,
                                      @NotNull @Assisted("message") String content,
                                      @Nullable ConfirmCallback confirmCallback);

    /**
     * Create a message window with a widget as content.
     * 
     * @param title the window title
     * @param content the window content
     * @param confirmCallback the callback used on OK
     * @return a {@link ConfirmWindow} instance
     */
    MessageWindow createMessageWindow(@NotNull String title,
                                      @NotNull IsWidget content,
                                      @Nullable ConfirmCallback confirmCallback);
}
