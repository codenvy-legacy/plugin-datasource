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
package com.codenvy.ide.ext.datasource.client.sqllauncher;

import java.util.Collection;

import com.codenvy.ide.Resources;
import com.codenvy.ide.api.parts.base.BaseView;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * Implementation of the view component of the SQL request launcher.
 * 
 * @author "Mickaël Leduque"
 */
public class SqlRequestLauncherViewImpl extends BaseView<SqlRequestLauncherView.ActionDelegate> implements SqlRequestLauncherView {

    @UiField
    Widget           launcherContainer;

    /** The SQL edition zone. */
    @UiField
    AcceptsOneWidget editorZone;

    /** The request result display. */
    @UiField
    AcceptsOneWidget resultZone;

    /** The label for the datasource selection widget. */
    @UiField
    Label            selectDatasourceLabel;

    /** the datasource selection widget. */
    @UiField
    ListBox          datasourceList;

    /** The label for the request result limit widget. */
    @UiField
    Label            resultLimitLabel;

    /** The request result limit widget. */
    @UiField
    TextBox          resultLimitInput;

    /** The button that commands request execution. */
    @UiField
    Button           executeButton;


    @Inject
    public SqlRequestLauncherViewImpl(final Resources resources,
                                      final SqlRequestLauncherViewImplUiBinder uiBinder,
                                      final SqlRequestLauncherConstants constants) {
        super(resources);
        uiBinder.createAndBindUi(this);
        container.add(this.launcherContainer);

        selectDatasourceLabel.setText(constants.selectDatasourceLabel());
        resultLimitLabel.setText(constants.resultLimitLabel());
        executeButton.setText(constants.executeButtonLabel());
    }

    @Override
    public void setResultLimit(final int newResultLimit) {
        this.resultLimitInput.setValue(Integer.toString(newResultLimit));
    }

    @Override
    public AcceptsOneWidget getEditorZone() {
        return editorZone;
    }

    @Override
    public void setDatasourceList(final Collection<String> datasourceIds) {
        // save the currently selected item
        int index = this.datasourceList.getSelectedIndex();
        String selectedValue = null;
        if (index != -1) {
            selectedValue = this.datasourceList.getValue(index);
        }

        this.datasourceList.clear();
        if (datasourceIds != null) {
            for (String datasourceId : datasourceIds) {
                this.datasourceList.addItem(datasourceId);
            }
        }

        // restore selected value if needed
        if (index != -1) {
            for (int i = 0; i < this.datasourceList.getItemCount(); i++) {
                if (this.datasourceList.getItemText(i).equals(selectedValue)) {
                    this.datasourceList.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    /**
     * Handler for clicks on the execute button.
     * 
     * @param event the click event
     */
    @UiHandler("executeButton")
    void handleExecuteClick(final ClickEvent event) {

    }

    /**
     * The UiBinder interface for this component.
     */
    interface SqlRequestLauncherViewImplUiBinder extends UiBinder<Widget, SqlRequestLauncherViewImpl> {
    }
}
