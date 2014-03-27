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

import java.util.ArrayList;
import java.util.List;

import com.codenvy.ide.api.ui.workspace.AbstractPartPresenter;
import com.codenvy.ide.ext.datasource.client.selection.DatabaseEntitySelectionEvent;
import com.codenvy.ide.ext.datasource.client.selection.DatabaseEntitySelectionHandler;
import com.codenvy.ide.ext.datasource.client.selection.DatabaseInfoReceivedEvent;
import com.codenvy.ide.ext.datasource.client.selection.DatabaseInfoReceivedHandler;
import com.codenvy.ide.ext.datasource.shared.ColumnDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseMetadataEntityDTO;
import com.codenvy.ide.ext.datasource.shared.SchemaDTO;
import com.codenvy.ide.ext.datasource.shared.TableDTO;
import com.codenvy.ide.util.loging.Log;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * The presenter part for the database item properties display.
 * 
 * @author "Mickaël Leduque"
 */
public class DataEntityPropertiesPresenter extends AbstractPartPresenter implements DataEntityPropertiesView.ActionDelegate,
                                                                        DatabaseEntitySelectionHandler,
                                                                        DatabaseInfoReceivedHandler {

    /** The view component. */
    private final DataEntityPropertiesView      view;

    private final ListDataProvider<Property>    dataProvider        = new ListDataProvider<Property>(new PropertyKeyProvider());

    private final DataEntityPropertiesConstants constants;

    private DatabaseDTO                         currentDatabaseInfo = null;

    @Inject
    public DataEntityPropertiesPresenter(final DataEntityPropertiesView view,
                                         final EventBus eventBus,
                                         final DataEntityPropertiesConstants constants) {
        super();
        this.view = view;
        this.view.setDelegate(this);
        this.view.bindDataProvider(this.dataProvider);
        this.constants = constants;
        eventBus.addHandler(DatabaseEntitySelectionEvent.getType(), this);
        eventBus.addHandler(DatabaseInfoReceivedEvent.getType(), this);
    }

    @Override
    public void go(final AcceptsOneWidget container) {
        container.setWidget(view);
    }

    @Override
    public String getTitle() {
        return "Datasource properties";
    }

    @Override
    public ImageResource getTitleImage() {
        return null;
    }

    @Override
    public String getTitleToolTip() {
        return null;
    }

    @Override
    public void onDatabaseEntitySelection(final DatabaseEntitySelectionEvent event) {
        DatabaseMetadataEntityDTO newSelection = event.getSelection();
        if (newSelection == null && this.currentDatabaseInfo == null) {
            updateDisplay(null);
        } else {
            // show the database when no item is selected
            if (newSelection == null) {
                updateDisplay(this.currentDatabaseInfo);
            } else {
                updateDisplay(newSelection);
            }
            this.view.setShown(true);
        }
    }

    private void updateDisplay(final DatabaseMetadataEntityDTO newSelection) {
        if (newSelection == null) {
            Log.info(DataEntityPropertiesPresenter.class, "No selection, hiding properties display.");
            this.view.setShown(false);
            setPropertyList(null);
            return;
        }

        if (newSelection instanceof DatabaseDTO) {
            Log.info(DataEntityPropertiesPresenter.class, "Show properties for database entity.");
            setPropertyList(getPropertiesForDatabase((DatabaseDTO)newSelection));
        } else if (newSelection instanceof SchemaDTO) {
            Log.info(DataEntityPropertiesPresenter.class, "Show properties for schema entity.");
            setPropertyList(getPropertiesForSchema((SchemaDTO)newSelection));
        } else if (newSelection instanceof TableDTO) {
            Log.info(DataEntityPropertiesPresenter.class, "Show properties for table entity.");
            setPropertyList(getPropertiesForTable((TableDTO)newSelection));
        } else if (newSelection instanceof ColumnDTO) {
            Log.info(DataEntityPropertiesPresenter.class, "Show properties for column entity.");
            setPropertyList(getPropertiesForColumn((ColumnDTO)newSelection));
        } else {
            Log.info(DataEntityPropertiesPresenter.class, "Unknown selection, hiding properties display.");
            this.view.setShown(false);
            setPropertyList(null);
        }
    }

    private void setPropertyList(List<Property> properties) {
        this.dataProvider.getList().clear();
        if (properties != null) {
            this.dataProvider.getList().addAll(properties);
        }
    }

    private List<Property> getCommonProperties(final DatabaseMetadataEntityDTO entityDTO) {
        List<Property> result = new ArrayList<Property>();
        result.add(new Property(constants.objectNameLabel(), entityDTO.getName()));
        return result;
    }

    private List<Property> getPropertiesForDatabase(final DatabaseDTO databaseDTO) {
        List<Property> result = getCommonProperties(databaseDTO);
        result.add(new Property(constants.objectTypeLabel(), constants.objectTypeDatabase()));
        result.add(new Property(constants.productNameLabel(), databaseDTO.getDatabaseProductName()));
        result.add(new Property(constants.productVersionLabel(), databaseDTO.getDatabaseProductVersion()));
        result.add(new Property(constants.usernameLabel(), databaseDTO.getUserName()));
        return result;
    }

    private List<Property> getPropertiesForSchema(final SchemaDTO schemaDTO) {
        List<Property> result = getCommonProperties(schemaDTO);
        result.add(new Property(constants.objectTypeLabel(), constants.objectTypeSchema()));
        return result;
    }

    private List<Property> getPropertiesForTable(final TableDTO tableDTO) {
        List<Property> result = getCommonProperties(tableDTO);
        result.add(new Property(constants.objectTypeLabel(), constants.objectTypeTable()));
        result.add(new Property(constants.tableTypeLabel(), tableDTO.getType()));
        String primaryKeyDisplay = constants.noValue();
        List<String> primaryKey = tableDTO.getPrimaryKey();
        if (primaryKey != null && !primaryKey.isEmpty()) {
            primaryKeyDisplay = formatPrimaryKey(primaryKey);
        }
        result.add(new Property(constants.primaryKeyLabel(), primaryKeyDisplay));
        return result;
    }

    private String formatPrimaryKey(List<String> primaryKey) {
        if (primaryKey.size() == 1) {
            return primaryKey.get(0);
        } else if (primaryKey.size() > 1) {
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < primaryKey.size(); i++) {
                sb.append(primaryKey.get(i));
                if (i < primaryKey.size()) {
                    sb.append(",");
                }
            }
            sb.append("]");
            return sb.toString();
        } else {
            return "";
        }
    }

    private List<Property> getPropertiesForColumn(ColumnDTO columnDTO) {
        List<Property> result = getCommonProperties(columnDTO);
        result.add(new Property(constants.objectTypeLabel(), constants.objectTypeColumn()));
        result.add(new Property(constants.dataTypeLabel(), columnDTO.getColumnDataType()));
        result.add(new Property(constants.columnSizeLabel(), Integer.toString(columnDTO.getDataSize())));
        result.add(new Property(constants.decimalDigitsLabel(), Integer.toString(columnDTO.getDecimalDigits())));
        result.add(new Property(constants.nullableLabel(), Boolean.toString(columnDTO.getNullable())));
        result.add(new Property(constants.defaultValueLabel(), columnDTO.getDefaultValue()));
        return result;
    }

    @Override
    public void onDatabaseInfoReceived(DatabaseInfoReceivedEvent event) {
        this.currentDatabaseInfo = event.getReceivedInfo();
        if (this.currentDatabaseInfo != null) {
            Log.info(DataEntityPropertiesPresenter.class, "Datasource selected : " + this.currentDatabaseInfo.getName());
        } else {
            Log.info(DataEntityPropertiesPresenter.class, "Datasource selected : null");
        }
        updateDisplay(this.currentDatabaseInfo);
    }
}
