package com.codenvy.ide.ext.datasource.client.common.confirmwindow;

import javax.validation.constraints.NotNull;

import com.codenvy.ide.ext.datasource.client.common.confirmwindow.ConfirmWindowView.ActionDelegate;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class ConfirmWindowFooter extends Composite {

    private static ConfirmWindowFooterUiBinder uiBinder = GWT.create(ConfirmWindowFooterUiBinder.class);

    private ActionDelegate                     actionDelegate;

    @UiField(provided = true)
    ConfirmWindowMessages                      messages;

    @Inject
    public ConfirmWindowFooter(final @NotNull ConfirmWindowMessages messages) {
        this.messages = messages;
        initWidget(uiBinder.createAndBindUi(this));
    }

    public void setDelegate(final ActionDelegate delegate) {
        this.actionDelegate = delegate;
    }

    @UiHandler("okButton")
    public void handleOkClick(final ClickEvent event) {
        this.actionDelegate.accepted();
    }

    @UiHandler("cancelButton")
    public void handleCancelClick(final ClickEvent event) {
        this.actionDelegate.cancelled();
    }

    interface ConfirmWindowFooterUiBinder extends UiBinder<Widget, ConfirmWindowFooter> {
    }
}
