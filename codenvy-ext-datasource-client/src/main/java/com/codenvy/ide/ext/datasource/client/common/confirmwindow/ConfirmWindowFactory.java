package com.codenvy.ide.ext.datasource.client.common.confirmwindow;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.assistedinject.Assisted;

public interface ConfirmWindowFactory {

    ConfirmWindow createConfirmWindow(@NotNull @Assisted("title") String title,
                                      @NotNull @Assisted("message") String content,
                                      @Nullable ConfirmCallback confirmCallback,
                                      @Nullable CancelCallback cancelCallback);

    ConfirmWindow createConfirmWindow(@NotNull String title,
                                      @NotNull IsWidget content,
                                      @Nullable ConfirmCallback confirmCallback,
                                      @Nullable CancelCallback cancelCallback);
}
