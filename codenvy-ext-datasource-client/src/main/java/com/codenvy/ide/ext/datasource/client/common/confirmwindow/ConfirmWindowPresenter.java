package com.codenvy.ide.ext.datasource.client.common.confirmwindow;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

public class ConfirmWindowPresenter implements ConfirmWindow, ConfirmWindowView.ActionDelegate {

    private final ConfirmWindowView view;

    private final ConfirmCallback   confirmCallback;
    private final CancelCallback    cancelCallback;

    @AssistedInject
    public ConfirmWindowPresenter(final @NotNull ConfirmWindowView view,
                                  final @NotNull @Assisted("title") String title,
                                  final @NotNull @Assisted("message") String message,
                                  final @Nullable @Assisted ConfirmCallback confirmCallback,
                                  final @Nullable @Assisted CancelCallback cancelCallback) {
        this(view, title, new Label(message), confirmCallback, cancelCallback);
    }

    @AssistedInject
    public ConfirmWindowPresenter(final @NotNull ConfirmWindowView view,
                                  final @NotNull @Assisted String title,
                                  final @NotNull @Assisted IsWidget content,
                                  final @Nullable @Assisted ConfirmCallback confirmCallback,
                                  final @Nullable @Assisted CancelCallback cancelCallback) {
        this.view = view;
        this.view.setContent(content);
        this.view.setTitle(title);
        this.confirmCallback = confirmCallback;
        this.cancelCallback = cancelCallback;
        this.view.setDelegate(this);
    }

    @Override
    public void cancelled() {
        this.view.closeDialog();
        if (this.cancelCallback != null) {
            this.cancelCallback.cancelled();
        }
    }

    @Override
    public void accepted() {
        this.view.closeDialog();
        if (this.confirmCallback != null) {
            this.confirmCallback.accepted();
        }
    }

    @Override
    public void confirm() {
        this.view.showDialog();
    }
}
