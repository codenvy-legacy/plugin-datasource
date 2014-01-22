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
package com.codenvy.ide.ext.datasource.client.sqleditor;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class SqlRequestLauncherViewImpl extends Composite implements SqlRequestLauncherView {

    private ActionDelegate actionDelegate;

    @UiField
    AcceptsOneWidget       editionZone;

    @UiField
    AcceptsOneWidget       resultZone;

    @UiField
    Label                  selectDatasourceLabel;

    @UiField
    ListBox                datasourceList;

    @UiField
    Label                  resultLimitLabel;

    @UiField
    TextBox                resultLimitInput;

    @UiField
    Button                 executeButton;


    @Inject
    public SqlRequestLauncherViewImpl(final SqlRequestLauncherViewImplUiBinder uiBinder,
                                      final SqlRequestLauncherConstants constants) {
        initWidget(uiBinder.createAndBindUi(this));
        selectDatasourceLabel.setText(constants.selectDatasourceLabel());
        resultLimitLabel.setText(constants.resultLimitLabel());
    }

    /** {@inheritDoc} */
    @Override
    public void setDelegate(final ActionDelegate delegate) {
        this.actionDelegate = delegate;
    }

    @UiHandler("executeButton")
    void handleExecuteClick(final ClickEvent e) {

    }

    interface SqlRequestLauncherViewImplUiBinder extends UiBinder<Widget, SqlRequestLauncherViewImpl> {
    }
}
