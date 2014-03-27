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
package com.codenvy.ide.ext.datasource.client.properties;

import com.codenvy.ide.ext.datasource.client.DatasourceUiResources;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AbstractDataProvider;
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
    Panel                  mainContainer;

    @UiField(provided = true)
    CellTable<Property>    propertiesDisplay;

    @Inject
    public DataEntityPropertiesViewImpl(final DataEntityPropertiesViewUiBinder uiBinder,
                                        final CellTableResourcesProperties cellTableResources,
                                        final DatasourceUiResources datasourceUiResources) {
        this.propertiesDisplay = new CellTable<Property>(15, cellTableResources);
        propertiesDisplay.addColumn(new TextColumn<Property>() {

            @Override
            public String getValue(final Property property) {
                return property.getName();
            }

            @Override
            public String getCellStyleNames(final Context context, final Property object) {
                return datasourceUiResources.datasourceUiCSS().propertiesTableFirstColumn();
            }
        });
        propertiesDisplay.addColumn(new TextColumn<Property>() {

            @Override
            public String getValue(final Property property) {
                return property.getValue(); // goes through a SafeHtmlRenderer
            }
        });
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void bindDataProvider(final AbstractDataProvider<Property> dataProvider) {
        dataProvider.addDataDisplay(this.propertiesDisplay);
    }

    @Override
    public void setDelegate(final ActionDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public void setShown(final boolean shown) {
        this.mainContainer.setVisible(shown);
    }

    /**
     * The UiBinder interface for this widget.
     * 
     * @author "Mickaël Leduque"
     */
    interface DataEntityPropertiesViewUiBinder extends UiBinder<Widget, DataEntityPropertiesViewImpl> {
    }

    interface PropertiesStyle extends CssResource {
        String keyColumnText();

        String valueColumnText();
    }
}
