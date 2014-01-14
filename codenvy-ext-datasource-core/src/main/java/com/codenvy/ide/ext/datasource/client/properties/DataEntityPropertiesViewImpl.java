/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2013] - [2014] Codenvy, S.A.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.ide.ext.datasource.client.properties;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * Implementation of the view for data item properties display.
 * 
 * @author Mickaël LEDUQUE
 */
public class DataEntityPropertiesViewImpl extends Composite implements DataEntityPropertiesView {

    /** The action delegate for the view. */
    private ActionDelegate delegate;

    @UiField
    Label objectName;

    @Inject
    public DataEntityPropertiesViewImpl(final DataEntityPropertiesViewUiBinder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setDelegate(final ActionDelegate delegate) {
        this.delegate = delegate;
    }

    /**
     * The UiBinder interface for this widget.
     * 
     * @author "Mickaël Leduque"
     */
    interface DataEntityPropertiesViewUiBinder extends UiBinder<Widget, DataEntityPropertiesViewImpl> {
    }

    @Override
    public void setObjectName(String name) {
        // the name parameter comes from user input, but Label#setText(String) doesn't use it as HTML
        // so it's safe
        objectName.setText(name);
    }
}
