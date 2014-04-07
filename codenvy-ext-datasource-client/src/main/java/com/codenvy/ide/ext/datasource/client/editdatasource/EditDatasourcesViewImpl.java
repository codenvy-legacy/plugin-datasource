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

import com.codenvy.ide.ext.datasource.client.editdatasource.celllist.DatasourceCell;
import com.codenvy.ide.ext.datasource.client.editdatasource.celllist.DatasourceCellListResources;
import com.codenvy.ide.ext.datasource.client.editdatasource.celllist.DatasourceKeyProvider;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.SelectionModel;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * View implementation for the edit/delete datasource dialog.
 * 
 * @author "Mickaël Leduque"
 */
public class EditDatasourcesViewImpl extends DialogBox implements EditDatasourcesView {

    /** number of datasources that are visible in the datasource list. */
    private static final int           DATASOURCES_LIST_PAGE_SIZE = 8;

    @UiField(provided = true)
    SimplePager                        datasourcePager;

    @UiField(provided = true)
    CellList<DatabaseConfigurationDTO> datasourceList;

    @UiField
    Button                             deleteButton;

    @UiField
    Button                             editButton;

    @UiField
    Button                             closeButton;

    @UiField(provided = true)
    EditDatasourceMessages             messages;

    /** The delegate/control component. */
    private ActionDelegate             delegate;

    @Inject
    public EditDatasourcesViewImpl(final EditDatadourceViewImplUiBinder uiBinder,
                                   final EditDatasourceMessages messages,
                                   final @Named(DatasourceKeyProvider.NAME) DatasourceKeyProvider keyProvider,
                                   final DatasourceCellListResources dsListResources,
                                   final DatasourceCell datasourceCell) {
        this.datasourceList = new CellList<>(datasourceCell, dsListResources, keyProvider);
        this.datasourcePager = new SimplePager();
        this.messages = messages;
        Widget widget = uiBinder.createAndBindUi(this);
        setWidget(widget);

        this.setText(messages.editDatasourcesDialogText());
        this.setModal(true);
        this.datasourceList.setEmptyListWidget(new Label(messages.emptyDatasourceList()));
        this.datasourceList.setPageSize(DATASOURCES_LIST_PAGE_SIZE);
        this.datasourceList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
        this.datasourceList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.BOUND_TO_SELECTION);

        this.datasourcePager.setDisplay(this.datasourceList);
    }

    @Override
    public void showDialog() {
        this.center();
        this.show();
    }

    @Override
    public void closeDialog() {
        this.hide();
    }

    @Override
    public void setDelegate(final ActionDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public void bindDatasourceModel(final AbstractDataProvider<DatabaseConfigurationDTO> provider) {
        provider.addDataDisplay(this.datasourceList);
    }

    @Override
    public void bindSelectionModel(SelectionModel<DatabaseConfigurationDTO> selectionModel) {
        this.datasourceList.setSelectionModel(selectionModel);
    }

    @UiHandler("closeButton")
    public void handleCloseButton(final ClickEvent clickEvent) {
        this.delegate.closeDialog();
    }

    @UiHandler("editButton")
    public void handleEditButton(final ClickEvent clickEvent) {
        this.delegate.editSelectedDatasource();
    }

    @UiHandler("deleteButton")
    public void handleDeleteButton(final ClickEvent clickEvent) {
        this.delegate.deleteSelectedDatasources();
    }

    /**
     * UiBinder interface for this view.
     * 
     * @author "Mickaël Leduque"
     */
    interface EditDatadourceViewImplUiBinder extends UiBinder<Widget, EditDatasourcesViewImpl> {
    }
}
