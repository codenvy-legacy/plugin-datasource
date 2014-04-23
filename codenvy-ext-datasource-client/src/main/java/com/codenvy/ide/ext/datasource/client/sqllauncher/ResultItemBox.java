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

package com.codenvy.ide.ext.datasource.client.sqllauncher;

import com.codenvy.ide.ext.datasource.client.DatasourceUiResources;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * Widget that embeds one result item (one table, one row count or one error.
 * 
 * @author "Mickaël Leduque"
 */
public class ResultItemBox extends Composite {

    private static ResultItemBoxUiBinder uiBinder = GWT.create(ResultItemBoxUiBinder.class);

    @UiField
    HTMLPanel                            mainPanel;

    @UiField
    SimplePanel                          headerPlace;

    @UiField
    FlowPanel                            resultPlace;

    @UiField
    SimplePanel                          footerPlace;

    @UiField(provided = true)
    final DatasourceUiResources          datasourceUiResources;

    @Inject
    public ResultItemBox(final DatasourceUiResources datasourceUiResources) {
        this.datasourceUiResources = datasourceUiResources;
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * Decides the widget used as result header.
     * 
     * @param header the header
     */
    public void setHeader(final RequestResultHeader header) {
        headerPlace.setWidget(header);
    }

    /**
     * Insert a widget in the result section.
     * 
     * @param item the widget to include
     */
    public void addResultItem(final Widget item) {
        resultPlace.add(item);
    }

    /**
     * Decides the widget used as result footer.
     * 
     * @param footer the footer
     */
    public void setFooter(final Widget footer) {
        footerPlace.setWidget(footer);
    }

    /**
     * UIBinder interface for the ResultItemBox.
     * 
     * @author "Mickaël Leduque"
     */
    interface ResultItemBoxUiBinder extends UiBinder<Widget, ResultItemBox> {
    }
}
