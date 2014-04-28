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

package com.codenvy.ide.ext.datasource.client.editdatasource;

import javax.validation.constraints.NotNull;

import com.codenvy.ide.ext.datasource.client.editdatasource.EditDatasourcesView.ActionDelegate;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * Widget used as footer in the "Manage Datasources" dialog window.
 * 
 * @author "Mickaël Leduque"
 */
public class EditWindowFooter extends Composite {

    /** The UI binder instance for this class. */
    private static EditWindowFooterUiBinder uiBinder = GWT.create(EditWindowFooterUiBinder.class);

    /** The action delegate. */
    private ActionDelegate                  actionDelegate;

    /** The i18n messages. */
    @UiField(provided = true)
    EditDatasourceMessages                  messages;

    @Inject
    public EditWindowFooter(final @NotNull EditDatasourceMessages messages) {
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
     * Handler set on the close button.
     * 
     * @param event the event that triggers the handler call
     */
    @UiHandler("closeButton")
    public void handleCloseClick(final ClickEvent event) {
        this.actionDelegate.closeDialog();
    }

    /**
     * The UI binder interface for this view component.
     * 
     * @author "Mickaël Leduque"
     */
    interface EditWindowFooterUiBinder extends UiBinder<Widget, EditWindowFooter> {
    }
}
