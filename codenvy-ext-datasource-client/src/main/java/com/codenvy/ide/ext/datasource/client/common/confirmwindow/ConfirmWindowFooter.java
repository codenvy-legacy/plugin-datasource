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

package com.codenvy.ide.ext.datasource.client.common.confirmwindow;

import javax.validation.constraints.NotNull;

import com.codenvy.ide.ext.datasource.client.common.confirmwindow.ConfirmWindowView.ActionDelegate;
import com.codenvy.ide.ext.datasource.client.common.interaction.InteractionWindowMessages;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * The footer show on confirmation windows.
 * 
 * @author "Mickaël Leduque"
 */
public class ConfirmWindowFooter extends Composite {

    /** The UI binder instance. */
    private static ConfirmWindowFooterUiBinder uiBinder = GWT.create(ConfirmWindowFooterUiBinder.class);

    /** The action delegate. */
    private ActionDelegate                     actionDelegate;

    /** The i18n messages. */
    @UiField(provided = true)
    InteractionWindowMessages                      messages;

    @Inject
    public ConfirmWindowFooter(final @NotNull InteractionWindowMessages messages) {
        this.messages = messages;
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * Sets the action delegate.
     * 
     * @param delegate the new value
     */
    public void setDelegate(final ActionDelegate delegate) {
        this.actionDelegate = delegate;
    }

    /**
     * Handler set on the OK button.
     * 
     * @param event the event that triggers the handler call
     */
    @UiHandler("okButton")
    public void handleOkClick(final ClickEvent event) {
        this.actionDelegate.accepted();
    }

    /**
     * Handler set on the cancel button.
     * 
     * @param event the event that triggers the handler call
     */
    @UiHandler("cancelButton")
    public void handleCancelClick(final ClickEvent event) {
        this.actionDelegate.cancelled();
    }

    /**
     * The UI binder interface for this component.
     * 
     * @author "Mickaël Leduque"
     */
    interface ConfirmWindowFooterUiBinder extends UiBinder<Widget, ConfirmWindowFooter> {
    }
}
