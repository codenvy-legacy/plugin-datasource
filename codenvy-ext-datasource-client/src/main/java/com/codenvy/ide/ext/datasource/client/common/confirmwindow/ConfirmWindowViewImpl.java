package com.codenvy.ide.ext.datasource.client.common.confirmwindow;

import javax.validation.constraints.NotNull;

import com.codenvy.ide.ui.window.Window;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class ConfirmWindowViewImpl extends Window implements ConfirmWindowView {

    private static ConfirmWindowUiBinder uiBinder = GWT.create(ConfirmWindowUiBinder.class);

    @UiField
    SimplePanel                          content;

    private final ConfirmWindowFooter    footer;

    @Inject
    public ConfirmWindowViewImpl(final @NotNull ConfirmWindowFooter footer) {
        Widget widget = uiBinder.createAndBindUi(this);
        setWidget(widget);

        this.footer = footer;
        getFooter().add(this.footer);
    }

    @Override
    public void setDelegate(final ActionDelegate delegate) {
        this.footer.setDelegate(delegate);
    }


    @Override
    protected void onClose() {
    }

    @Override
    public void showDialog() {
        this.show();
    }

    @Override
    public void closeDialog() {
        this.hide();
    }

    @Override
    public void setContent(final IsWidget content) {
        this.content.clear();
        this.content.setWidget(content);
    }

    interface ConfirmWindowUiBinder extends UiBinder<Widget, ConfirmWindowViewImpl> {
    }
}
