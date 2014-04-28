package com.codenvy.ide.ext.datasource.client.common.confirmwindow;

import com.google.gwt.user.client.ui.IsWidget;

public interface ConfirmWindowView {

    void setDelegate(ActionDelegate delegate);

    void showDialog();

    void closeDialog();

    void setContent(IsWidget content);

    void setTitle(String title);

    public interface ActionDelegate {

        void cancelled();

        void accepted();
    }
}
