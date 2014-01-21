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

    @UiField
    PropertiesStyle        style;

    @Inject
    public DataEntityPropertiesViewImpl(final DataEntityPropertiesViewUiBinder uiBinder) {
        this.propertiesDisplay = new CellTable<Property>();
        propertiesDisplay.addColumn(new TextColumn<Property>() {

            @Override
            public String getValue(final Property property) {
                return property.getName();
            }

            @Override
            public String getCellStyleNames(final Context context, final Property object) {
                return style.keyColumnText();
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
    }
}
